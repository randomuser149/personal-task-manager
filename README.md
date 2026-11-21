# Personal Task Manager

A Java 17 command-line task management application built with Maven.

## Problem Statement
Individuals balancing professional deliverables and personal errands need a lightweight tool to categorize tasks, calculate effective priority scores dynamically, filter high-priority items, and retain task data across sessions without external database setup.

## Contributors
- `randomuser149`

## Prerequisites
- Java 17+
- Apache Maven 3.8+

## Build, Test, and Run Instructions
From a clean checkout:

1. **Run the JUnit 5 test suite:**

       mvn test

2. **Compile and run the application:**

       mvn compile exec:java -Dexec.mainClass="com.taskmanager.ui.ConsoleApp"

## Evaluation Guide
1. Run `mvn test` and verify all 5 unit tests pass.
2. Start the application with `mvn compile exec:java -Dexec.mainClass="com.taskmanager.ui.ConsoleApp"`.
3. Add a **Work Task** (option `2`, priority `4`, project `PROJ-101`) and a **Personal Task** (option `3`, priority `3`, urgent `true`).
4. Select option `1` to verify polymorphic priority scoring (`WorkTask` = `priority * 2`; `PersonalTask` = `priority + 3` if urgent) and descending score order.
5. Select option `5` and enter threshold `4` to verify Strategy-based filtering.
6. Test validation by entering priority `9` or a title containing a comma (`,`)—`InvalidTaskDataException` is caught gracefully.
7. Select option `6` to save to `tasks.csv`, then restart the application to confirm tasks reload automatically.

## UML Class Diagram
*(Editable PlantUML source available at [`docs/diagram.puml`](docs/diagram.puml))*

~~~mermaid
classDiagram
    class ConsoleApp {
        -List~Task~ tasks$
        -TaskStorage storage$
        +main(String[] args)$ void
        -loadData()$ void
        -viewTasks(List~Task~ taskList)$ void
        -addWorkTask(Scanner scanner)$ void
        -addPersonalTask(Scanner scanner)$ void
        -markTaskCompleted(Scanner scanner)$ void
        -filterByPriority(Scanner scanner)$ void
    }

    class Task {
        <<abstract>>
        -String id
        -String title
        -int priority
        -boolean completed
        +Task(String id, String title, int priority)
        +getId() String
        +getTitle() String
        +getPriority() int
        +isCompleted() boolean
        +setCompleted(boolean completed) void
        +calculatePriorityScore()* int
        +compareTo(Task other) int
    }

    class WorkTask {
        -String projectCode
        +WorkTask(String id, String title, int priority, String projectCode)
        +getProjectCode() String
        +calculatePriorityScore() int
    }

    class PersonalTask {
        -boolean urgent
        +PersonalTask(String id, String title, int priority, boolean urgent)
        +isUrgent() boolean
        +calculatePriorityScore() int
    }

    class TaskFilterStrategy {
        <<interface>>
        +matches(Task task) boolean
    }

    class PriorityFilter {
        -int minPriority
        +PriorityFilter(int minPriority)
        +matches(Task task) boolean
    }

    class TaskStorage {
        -String filePath
        +TaskStorage(String filePath)
        +loadTasks() List~Task~
        +saveTasks(List~Task~ tasks) void
        +getNextAvailableId(List~Task~ tasks) String
    }

    class InvalidTaskDataException {
        +InvalidTaskDataException(String message)
    }

    ConsoleApp "1" o-- "0..*" Task : manages
    ConsoleApp "1" --> "1" TaskStorage : uses
    ConsoleApp ..> PriorityFilter : creates
    Task <|-- WorkTask
    Task <|-- PersonalTask
    TaskFilterStrategy <|.. PriorityFilter
    Task ..> InvalidTaskDataException : throws
    TaskStorage ..> InvalidTaskDataException : throws
~~~

## Implemented Requirements & Design Pattern
- **Encapsulation:** Private fields with constructor validation across domain, strategy, and persistence classes.
- **Abstraction, Inheritance & Polymorphism:** Abstract `Task` extended by `WorkTask` and `PersonalTask` overriding `calculatePriorityScore()` at runtime.
- **Collections & Generics:** `ArrayList<Task>`, `Comparable<Task>`, and lambda sorting (`taskList.sort(...)`).
- **Exceptions & Persistence:** Custom checked `InvalidTaskDataException` and CSV file persistence using `try-with-resources` in `TaskStorage`.
- **Strategy Pattern:** `TaskFilterStrategy` and `PriorityFilter` decouple filtering rules from `ConsoleApp`.
- **Design Details:** See [`docs/design.md`](docs/design.md) for user stories, design decisions, and known limitations.
