package ru.nsu.dunaev;

import groovy.lang.GroovyShell;
import groovy.lang.Binding;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String DEFAULT_CONFIG_FILE = "checker.groovy";
    private static final String GIT_CONFIG_CHECK = "git config --get credential.helper";
    private static final String OUTPUT_DIR = "repos";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: oop-checker <command> [config_file]");
            System.exit(1);
        }

        String command = args[0];
        String configFile = args.length > 1 ? args[1] : DEFAULT_CONFIG_FILE;

        if (!checkGitConfig()) {
            System.err.println("Git must be configured with no authentication prompts");
            System.exit(1);
        }

        System.out.println(configFile);
        Config config = loadConfig(configFile);
        if (config == null) {
            System.err.println("Failed to load configuration");
            System.exit(1);
        }

        if ("test".equalsIgnoreCase(command)) {
            processRepositories(config);
        } else {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }
    }

    private static boolean checkGitConfig() {
        try {
            Process process = Runtime.getRuntime().exec(GIT_CONFIG_CHECK);
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private static Config loadConfig(String configFile) {
        try {
            Binding binding = new Binding();
            GroovyShell shell = new GroovyShell(binding);
            File file = new File(configFile);
            if (!file.exists()) {
                System.err.println("Config file not found: " + configFile);
                return null;
            }

            Object result = shell.evaluate(file);
            return (Config) result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void processRepositories(Config config) {
        Map<String, Map<String, Map<String, CheckResult>>> results = new HashMap<>();
        Map<String, List<Activity>> activities = new HashMap<>();

        new File(OUTPUT_DIR).mkdirs();

        for (Group group : config.groups) {
            for (Student student : group.students) {
                String repoPath = cloneRepository(student, group.name);
                if (repoPath == null) continue;

                Map<String, CheckResult> studentResults = new HashMap<>();
                activities.put(student.githubNick, collectActivity(repoPath));

                Arrays.stream(Objects.requireNonNull(new File(repoPath).listFiles())).forEach(e -> {
                    if (e.isDirectory()) {
                        File file = new File(e.getAbsolutePath(), "gradlew");
                        if (file.isFile()) {
                            file.setExecutable(true);
                        }
                    }
                }
                );


                for (CheckTask check : config.checks) {
                    if (check.studentNicks.contains(student.githubNick)) {
                        Task task = config.tasks.stream()
                                .filter(t -> t.id.equals(check.taskId))
                                .findFirst()
                                .orElse(null);
                        if (task == null) continue;



                        CheckResult result = processTask(repoPath, task, config, student.githubNick);
                        studentResults.put(task.id, result);
                    }
                }
                results.computeIfAbsent(group.name, k -> new HashMap<>())
                        .put(student.githubNick, studentResults);
            }
        }

        generateReport(config, results, activities);
    }

    private static String cloneRepository(Student student, String groupName) {
        String repoPath = String.format("%s/%s_%s", OUTPUT_DIR, groupName, student.githubNick);
        try {
            deleteDirectory(new File(repoPath));
            ProcessBuilder pb = new ProcessBuilder("git", "clone", student.repoUrl, repoPath);
            Process process = pb.start();
            if (process.waitFor() != 0) {
                System.err.println("Failed to clone " + student.repoUrl);
                return null;
            }
            return repoPath;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    private static CheckResult processTask(String repoPath, Task task, Config config, String studentNick) {
        CheckResult result = new CheckResult();
        StringBuilder failureDetails = new StringBuilder();

        result.buildPassed = runCommand(repoPath + "/" + task.id, getGradleCommand("clean", "build")) == 0;
        if (!result.buildPassed) {
            String buildOutput = runCommandWithOutput(repoPath, getGradleCommand("clean", "build"));
            failureDetails.append("Build failed: ").append(buildOutput).append("\n");
        }

        result.docsPassed = runCommand(repoPath + "/" + task.id, getGradleCommand("javadoc")) == 0;
        if (!result.docsPassed) {
            String javadocOutput = runCommandWithOutput(repoPath, getGradleCommand("javadoc"));
            failureDetails.append("Javadoc failed: ").append(javadocOutput).append("\n");
        }

        File checkstyleConfig = new File(repoPath + "/" + task.id, "config/checkstyle/checkstyle.xml");
        if (!checkstyleConfig.exists()) {
            System.err.println("Checkstyle config missing in " + repoPath + "; skipping style check");
            result.stylePassed = false;
            failureDetails.append("Checkstyle config missing\n");
        } else {
            String checkstyleOutput = runCommandWithOutput(repoPath, getGradleCommand("checkstyleMain"));
            result.stylePassed = runCommand(repoPath + "/" + task.id, getGradleCommand("checkstyleMain")) == 0;
            if (!result.stylePassed) {
                failureDetails.append("Checkstyle failed: ").append(checkstyleOutput).append("\n");
                if (checkstyleOutput.contains("Missing a Javadoc comment")) {
                    System.err.println("Only Javadoc issues detected; treating style check as passed for " + studentNick);
                    result.stylePassed = true;
                }
            }
        }

        String testOutput = runCommandWithOutput(repoPath + "/" + task.id, getGradleCommand("test"));
        if (!testOutput.contains("test")) {
            System.err.println("No test output for " + studentNick + " in " + repoPath + "; checking XML results");
            result.testsPassed = parseTestResultsFromXml(repoPath + "/" + task.id);
        } else {
            result.testsPassed = parseTests(testOutput);
        }
        if (result.testsPassed[0] == 0 && result.testsPassed[1] == 0 && result.testsPassed[2] == 0) {
            System.err.println("No tests found for " + studentNick + " in " + repoPath);
            failureDetails.append("No tests found\n");
        }

        result.failureDetails = failureDetails.toString();

        result.points = calculatePoints(task, result, config);
        result.bonusPoints = getBonusPoints(config, task.id, studentNick);

        return result;
    }

    private static String[] getGradleCommand(String... args) {
        String os = System.getProperty("os.name").toLowerCase();
        String gradle = os.contains("win") ? "gradlew.bat" : "./gradlew";
        List<String> command = new ArrayList<>();
        command.add(gradle);
        command.addAll(Arrays.asList(args));
        return command.toArray(new String[0]);
    }

    private static int runCommand(String workingDir, String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(workingDir));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int timeoutSeconds = 300;
            if (!process.waitFor(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS)) {
                process.destroy();
                return 1;
            }
            return process.exitValue();
        } catch (IOException | InterruptedException e) {
            return 1;
        }
    }

    private static String runCommandWithOutput(String workingDir, String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(workingDir));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes());
            int timeoutSeconds = 300;
            if (!process.waitFor(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS)) {
                process.destroy();
                return "";
            }
            return output;
        } catch (IOException | InterruptedException e) {
            return "";
        }
    }

    private static int[] parseTests(String output) {
        try {
            Pattern pattern = Pattern.compile("Tests run: (\\d+), Failures: (\\d+), Errors: (\\d+), Skipped: (\\d+)");
            Matcher matcher = pattern.matcher(output);
            if (matcher.find()) {
                int total = Integer.parseInt(matcher.group(1));
                int failures = Integer.parseInt(matcher.group(2));
                int errors = Integer.parseInt(matcher.group(3));
                int skipped = Integer.parseInt(matcher.group(4));
                return new int[]{total - failures - errors - skipped, failures + errors, skipped};
            }
            String[] lines = output.split("\n");
            for (String line : lines) {
                Pattern fallback = Pattern.compile("(\\d+)\\s*tests?,\\s*(\\d+)\\s*failed,\\s*(\\d+)\\s*skipped");
                Matcher fallbackMatcher = fallback.matcher(line);
                if (fallbackMatcher.find()) {
                    int total = Integer.parseInt(fallbackMatcher.group(1));
                    int failed = Integer.parseInt(fallbackMatcher.group(2));
                    int skipped = Integer.parseInt(fallbackMatcher.group(3));
                    return new int[]{total - failed - skipped, failed, skipped};
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse tests: " + e.getMessage());
        }
        return new int[]{0, 0, 0};
    }

    private static int[] parseTestResultsFromXml(String repoPath) {
        File testResultsDir = new File(repoPath, "build/test-results/test");
        if (!testResultsDir.exists()) {
            return new int[]{0, 0, 0};
        }
        int passed = 0, failed = 0, skipped = 0;
        try {
            for (File xmlFile : testResultsDir.listFiles((dir, name) -> name.endsWith(".xml"))) {
                String content = Files.readString(xmlFile.toPath());
                if (content.contains("<testcase")) {
                    int testCount = content.split("<testcase").length - 1;
                    int failureCount = content.split("<failure").length - 1;
                    int errorCount = content.split("<error").length - 1;
                    int skipCount = content.split("<skipped").length - 1;
                    passed += testCount - failureCount - errorCount - skipCount;
                    failed += failureCount + errorCount;
                    skipped += skipCount;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to parse test XML results in " + repoPath + ": " + e.getMessage());
        }
        return new int[]{passed, failed, skipped};
    }

    private static int calculatePoints(Task task, CheckResult result, Config config) {
        int points = task.maxPoints;

//        if (LocalDate.now().isAfter(task.hardDeadline)) {
//            points = 0;
//        } else if (LocalDate.now().isAfter(task.softDeadline)) {
//            points = 5;
//        }

        int passedTests = result.testsPassed[0];
        int totalTests = passedTests + result.testsPassed[1] + result.testsPassed[2];
        double ratio = totalTests > 0 ? (double) passedTests / totalTests : 1;
        points = (int) (points * ratio);

        double penalty = 1.0;
        if (!result.buildPassed) penalty = 0;
        if (!result.docsPassed) penalty *= 0.8;
        if (!result.stylePassed) penalty *= 0.9;
        points = (int) (points * penalty);
        return points;
    }

    private static int getBonusPoints(Config config, String taskId, String studentNick) {
        Map<String, Map<String, Integer>> bonusPoints = (Map<String, Map<String, Integer>>) config.settings.getOrDefault("bonusPoints", new HashMap<>());
        return bonusPoints.getOrDefault(studentNick, new HashMap<>()).getOrDefault(taskId, 0);
    }

    private static List<Activity> collectActivity(String repoPath) {
        List<Activity> activities = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=%ci", "--since=12 months ago");
            pb.directory(new File(repoPath));
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes());
            String[] commits = output.split("\n");
            Map<LocalDate, Integer> commitCounts = new HashMap<>();
            for (String commit : commits) {
                if (!commit.trim().isEmpty()) {
                    LocalDate date = LocalDate.parse(commit.trim().substring(0, 10));
                    commitCounts.merge(date, 1, Integer::sum);
                }
            }
            commitCounts.forEach((date, count) -> activities.add(new Activity(date, count)));
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return activities;
    }

    private static void generateReport(Config config, Map<String, Map<String, Map<String, CheckResult>>> results, Map<String, List<Activity>> activities) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("table { border-collapse: collapse; width: 100%; }")
                .append("th, td { border: 1px solid black; padding: 8px; text-align: left; }")
                .append("th { background-color: #f2f2f2; }")
                .append(".failure-details { font-size: 0.8em; color: red; }")
                .append("</style></head><body>");

        for (Group group : config.groups) {
            html.append("<h2>Group ").append(group.name).append("</h2>");

            for (CheckTask check : config.checks) {
                Task task = config.tasks.stream()
                        .filter(t -> t.id.equals(check.taskId))
                        .findFirst()
                        .orElse(null);
                if (task == null) continue;

                html.append("<h3>Lab ").append(task.id).append(" (").append(task.name).append(")</h3>")
                        .append("<table>")
                        .append("<tr><th>Student</th><th>Build</th><th>Docs</th><th>Style</th><th>Tests</th><th>Bonus</th><th>Total</th><th>Details</th></tr>");

                for (String nick : check.studentNicks) {
                    Student student = group.students.stream()
                            .filter(s -> s.githubNick.equals(nick))
                            .findFirst()
                            .orElse(null);
                    if (student == null) continue;

                    CheckResult result = results.getOrDefault(group.name, new HashMap<>())
                            .getOrDefault(nick, new HashMap<>())
                            .getOrDefault(task.id, new CheckResult());

                    html.append("<tr>")
                            .append("<td>").append(student.fullName).append("</td>")
                            .append("<td>").append(result.buildPassed ? "+" : "-").append("</td>")
                            .append("<td>").append(result.docsPassed ? "+" : "-").append("</td>")
                            .append("<td>").append(result.stylePassed ? "+" : "-").append("</td>")
                            .append("<td>").append(String.format("%d/%d/%d", result.testsPassed[0], result.testsPassed[1], result.testsPassed[2])).append("</td>")
                            .append("<td>").append(result.bonusPoints).append("</td>")
                            .append("<td>").append(result.points + result.bonusPoints).append("</td>")
                            .append("<td class='failure-details'>").append(result.failureDetails.replace("\n", "<br>")).append("</td>")
                            .append("</tr>");
                }
                html.append("</table>");
            }

            html.append("<h3>Overall Statistics for Group ").append(group.name).append("</h3>")
                    .append("<table>")
                    .append("<tr><th>Student</th>");

            for (CheckTask check : config.checks) {
                Task task = config.tasks.stream()
                        .filter(t -> t.id.equals(check.taskId))
                        .findFirst()
                        .orElse(null);
                if (task != null) {
                    html.append("<th>").append(task.id).append("</th>");
                }
            }

            html.append("<th>Sum</th><th>Activity</th><th>Grade</th></tr>");

            for (Student student : group.students) {
                html.append("<tr><td>").append(student.fullName).append("</td>");
                int totalPoints = 0;

                for (CheckTask check : config.checks) {
                    CheckResult result = results.getOrDefault(group.name, new HashMap<>())
                            .getOrDefault(student.githubNick, new HashMap<>())
                            .getOrDefault(check.taskId, new CheckResult());
                    int points = result.points + result.bonusPoints;
                    html.append("<td>").append(points).append("</td>");
                    totalPoints += points;
                }

                double activityRate = calculateActivityRate(activities.getOrDefault(student.githubNick, new ArrayList<>()));
                String grade = calculateGrade(totalPoints, activityRate, config);

                html.append("<td>").append(totalPoints).append("</td>")
                        .append("<td>").append(String.format("%.0f%%", activityRate * 100)).append("</td>")
                        .append("<td>").append(grade).append("</td>")
                        .append("</tr>");
            }
            html.append("</table>");
        }

        html.append("</body></html>");

        try {
            Files.writeString(Paths.get("report.html"), html.toString());
            System.out.println("Report generated: report.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double calculateActivityRate(List<Activity> activities) {
        if (activities.isEmpty()) return 0.0;
        Set<Integer> activeWeeks = new HashSet<>();
        LocalDate start = activities.stream()
                .map(a -> a.date)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());
        LocalDate end = LocalDate.now();
        long totalWeeks = java.time.temporal.ChronoUnit.WEEKS.between(start, end) + 1;

        for (Activity activity : activities) {
            long week = java.time.temporal.ChronoUnit.WEEKS.between(start, activity.date);
            activeWeeks.add((int) week);
        }

        return (double) activeWeeks.size() / totalWeeks;
    }

    private static String calculateGrade(int points, double activityRate, Config config) {
        Map<Integer, String> gradingScale = (Map<Integer, String>) config.settings.getOrDefault("gradingScale", new HashMap<>());
        int taskCount = config.checks.size();
        System.out.println(((double) points / ((double)taskCount * 10)) * 100);
        return gradingScale.entrySet().stream()
                .filter(e -> (int)(((double) points / ((double)taskCount * 10)) * 100) >= e.getKey())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("-");
    }

    static class Config {
        List<Task> tasks = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Checkpoint> checkpoints = new ArrayList<>();
        List<CheckTask> checks = new ArrayList<>();
        Map<String, Object> settings = new HashMap<>();
    }

    static class Task {
        String id;
        String name;
        int maxPoints;
        LocalDate softDeadline;
        LocalDate hardDeadline;
    }

    static class Group {
        String name;
        List<Student> students = new ArrayList<>();
    }

    static class Student {
        String githubNick;
        String fullName;
        String repoUrl;
    }

    static class Checkpoint {
        String name;
        LocalDate date;
    }

    static class CheckTask {
        String taskId;
        List<String> studentNicks;
    }

    static class CheckResult {
        boolean buildPassed;
        boolean docsPassed;
        boolean stylePassed;
        int[] testsPassed = new int[]{0, 0, 0};
        int points;
        int bonusPoints;
        String failureDetails = "";
    }

    static class Activity {
        LocalDate date;
        int commitCount;

        Activity(LocalDate date, int commitCount) {
            this.date = date;
            this.commitCount = commitCount;
        }
    }
}