# Design Documentation

## Problem Statement
Individuals balancing professional deliverables and personal errands need a lightweight CLI tool to categorize tasks, calculate effective priority scores dynamically, filter high-priority items, and retain task data across sessions without external database setup.

## User Stories
- **US-1 (Add Tasks):** As a user, I want to add Work Tasks (with project codes) and Personal Tasks (with urgency flags) with auto-incremented IDs so that I can categorize and track my responsibilities.
- **US-2 (View & Prioritize):** As a user, I want to view all tasks sorted automatically by their computed priority score so that I can focus on the highest-impact items first.
- **US-3 (Filter Tasks):** As a user, I want to filter tasks by a chosen minimum priority threshold (1–5) so that I can isolate urgent or high-priority work.
- **US-4 (Complete Tasks):** As a user, I want to mark a task as completed by its ID so that I can track progress.
- **US-5 (Persist Data):** As a user, I want tasks to load on startup and save to a CSV file upon exit so that my task list persists between runs.

## UML Class Diagram
*(Editable PlantUML source: [`docs/diagram.puml`](diagram.puml))*

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

## Design Pattern: Strategy
- **Participating Classes:** `TaskFilterStrategy` (interface), `PriorityFilter` (concrete strategy), and `ConsoleApp` (client).
- **Benefit:** Decouples task filtering criteria from the CLI menu loop in `ConsoleApp`, allowing new filtering policies to be added without modifying the client.

## Design Decisions & Known Limitations
- **Design Decisions:** Uses an abstract `Task` class with `WorkTask` and `PersonalTask` subclasses for polymorphic priority scoring, and CSV persistence with `try-with-resources` and auto-incremented IDs (`TaskStorage#getNextAvailableId`) for zero-dependency storage.
- **Known Limitations:** CSV fields are comma-delimited, so task titles and project codes cannot contain commas (enforced via `InvalidTaskDataException`).
