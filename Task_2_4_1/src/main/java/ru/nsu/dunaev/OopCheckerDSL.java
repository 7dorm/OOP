package ru.nsu.dunaev;

import groovy.lang.Closure;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Config {
    List<Task> tasks = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    List<Checkpoint> checkpoints = new ArrayList<>();
    List<CheckTask> checks = new ArrayList<>();
    Map<String, Object> settings = new HashMap<>();
}

class Task {
    String id;
    String name;
    int maxPoints;
    LocalDate softDeadline;
    LocalDate hardDeadline;
}

class Group {
    String name;
    List<Student> students = new ArrayList<>();
}

class Student {
    String githubNick;
    String fullName;
    String repoUrl;
}

class Checkpoint {
    String name;
    LocalDate date;
}

class CheckTask {
    String taskId;
    List<String> studentNicks;
}

class CheckResult {
    boolean buildPassed;
    boolean docsPassed;
    boolean stylePassed;
    int[] testsPassed = new int[]{0, 0, 0};
    int points;
    int bonusPoints;
}

class Activity {
    LocalDate date;
    int commitCount;

    Activity(LocalDate date, int commitCount) {
        this.date = date;
        this.commitCount = commitCount;
    }
}

public class OopCheckerDSL {
    private Config config = new Config();

    Config getConfig() {
        return config;
    }

    void run(Closure closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}