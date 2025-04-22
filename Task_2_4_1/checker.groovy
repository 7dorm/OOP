import java.time.LocalDate
import ru.nsu.dunaev.Main.*

def config = new Config()

def tasksFile = new File("tasks_config.groovy")
if (tasksFile.exists()) {
    def tasksConfig = evaluate(tasksFile)
    config.tasks = tasksConfig.tasks.collect { groovyTask ->
        new Task(
                id: groovyTask.id,
                name: groovyTask.name,
                maxPoints: groovyTask.maxPoints,
                softDeadline: groovyTask.softDeadline,
                hardDeadline: groovyTask.hardDeadline
        )
    }
}

config.with {
    groups = [
            new Group(name: "23215", students: [
                    new Student(githubNick: "7dorm", fullName: "Dunaev", repoUrl: "https://github.com/7dorm/OOP"),
                    new Student(githubNick: "ryasuke1", fullName: "Hubanov", repoUrl: "https://github.com/ryasuke1/OOP"),
                    new Student(githubNick: "Nikolay56615", fullName: "Lebedev", repoUrl: "https://github.com/Nikolay56615/OOP"),
                    new Student(githubNick: "AYETATAPIN", fullName: "Demodov", repoUrl: "https://github.com/AYETATAPIN/OOP")
            ])
    ]

    checks = [
            new CheckTask(taskId: "Task_1_1_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_1_1_2", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_1_1_3", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_1_2_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_1_2_2", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_1_3_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_1_4_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_2_1_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_2_2_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),
            new CheckTask(taskId: "Task_2_3_1", studentNicks: ["7dorm", "ryasuke1", "Nikolay56615", "AYETATAPIN"]),

    ]

    checkpoints = [
            new Checkpoint(name: "Midterm", date: LocalDate.of(2025, 1, 15)),
            new Checkpoint(name: "Final", date: LocalDate.of(2025, 5, 30))
    ]

    settings = [
            gradingScale: [90: "A", 80: "B", 70: "C", 60: "D"],
            bonusPoints: [
                    "":[]
            ]
    ]
}

config

//                    "7dorm": ["Task_1_1_3": , "Task_1_2_2": 10]
