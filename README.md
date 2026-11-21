# Personal Task Manager

## Project Summary
A Java 17 command-line task management application built with Maven that allows users to create work and personal tasks, dynamically compute and sort tasks by priority score, filter tasks by priority threshold, and persist data across sessions in CSV format.

## Prerequisites
- Java 17+
- Apache Maven 3.8+

## Build, Test, and Run Commands
From a clean checkout:

1. **Run the JUnit 5 test suite:**

       mvn test

2. **Compile and run the application:**

       mvn compile exec:java -Dexec.mainClass="com.taskmanager.ui.ConsoleApp"

3. **Evaluate the application:**
   - Add a Work Task (`2`, priority `4`, project `PROJ-101`) and a Personal Task (`3`, priority `3`, urgent `true`).
   - View All Tasks (`1`) to verify polymorphic priority scoring (`WorkTask` = `priority * 2`; `PersonalTask` = `priority + 3` if urgent) and descending score order.
   - Filter Tasks (`5`, threshold `4`) to verify Strategy-based filtering.
   - Enter an invalid priority (`9`) or a comma in a title to verify `InvalidTaskDataException` handling.
   - Save & Exit (`6`), then rerun the application to confirm tasks reload from `tasks.csv`.

## Sample Usage

    === PERSONAL TASK MANAGER ===

    --- MENU ---
    1. View All Tasks
    2. Add Work Task
    3. Add Personal Task
    4. Mark Task Completed
    5. Filter Tasks by Priority
    6. Save & Exit
    Choose an option: 2
    Assigning Task ID: 1
    Enter Title: Implement Strategy Pattern
    Enter Priority (1 = Lowest, 5 = Highest): 4
    Enter Project Code (e.g., PROJ-101): PROJ-101
    Work task added successfully!

## Implemented Requirements
- **Encapsulation:** Private fields with constructor validation across `Task`, `WorkTask`, `PersonalTask`, `PriorityFilter`, and `TaskStorage`.
- **Abstraction, Inheritance & Polymorphism:** Abstract `Task` extended by `WorkTask` and `PersonalTask` overriding `calculatePriorityScore()` at runtime.
- **Relationships:** Aggregation between `ConsoleApp` (`1`) and `Task` (`0..*`), and association between `ConsoleApp` (`1`) and `TaskStorage` (`1`).
- **Collections & Generics:** `ArrayList<Task>`, `Comparable<Task>`, and lambda sorting (`taskList.sort(...)`).
- **Exceptions:** Custom checked `InvalidTaskDataException` enforcing priority bounds (`1–5`), non-empty titles, and CSV delimiter rules.
- **Persistence:** `TaskStorage` reads and writes `tasks.csv` using `try-with-resources`.
- **Documentation (`docs/`):** Viewable UML class diagram & design notes in [`docs/design.md`](docs/design.md); editable PlantUML source in [`docs/diagram.puml`](docs/diagram.puml).

## Chosen Pattern: Strategy
- **Participating Classes:** `TaskFilterStrategy` (interface), `PriorityFilter` (concrete strategy), and `ConsoleApp` (client).
- **Benefit:** Decouples task filtering rules from the CLI menu loop so new filtering policies can be introduced without modifying `ConsoleApp`.

## Contributors
- **Fődi Mátyás** (`randomuser149`)
