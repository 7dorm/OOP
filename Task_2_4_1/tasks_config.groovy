import java.time.LocalDate
class Task {
    String id
    String name
    int maxPoints
    LocalDate softDeadline
    LocalDate hardDeadline
}
[
        tasks: [
                new Task(id: "Task_2_1_1", name: "Simple Numbers", maxPoints: 10,
                        softDeadline: LocalDate.of(2025, 3, 1), hardDeadline: LocalDate.of(2025, 3, 15)),
                new Task(id: "Task_2_3_1", name: "Snake", maxPoints: 10,
                        softDeadline: LocalDate.of(2025, 4, 1), hardDeadline: LocalDate.of(2025, 4, 15))
        ]
]