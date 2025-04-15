import java.time.LocalDate
import ru.nsu.dunaev.Main.*

def config = new Config()

def tasksFile = new File("tasks_config.groovy")
if (tasksFile.exists()) {
    def tasksConfig = evaluate(tasksFile)
    config.tasks = tasksConfig.tasks
}

config.with {
    groups = [
            new Group(name: "23215", students: [
                    new Student(githubNick: "7dorm", fullName: "John Doe", repoUrl: "https://github.com/7dorm/OOP"),
            ])
    ]

    checks = [
            new CheckTask(taskId: "Task_2_1_1", studentNicks: ["7dorm"]),
            new CheckTask(taskId: "Task_2_3_1", studentNicks: ["7dorm"])
    ]

    checkpoints = [
            new Checkpoint(name: "Midterm", date: LocalDate.of(2025, 3, 15)),
            new Checkpoint(name: "Final", date: LocalDate.of(2025, 5, 30))
    ]

    settings = [
            gradingScale: [90: "A", 80: "B", 70: "C", 60: "D"],
            timeoutSeconds: 300,
            bonusPoints: [
                    "7dorm": ["Task_2_1_1": 1, "Task_2_3_1": 1]
            ]
    ]
}

config