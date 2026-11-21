# Design Documentation

## Problem Statement
Individuals balancing professional deliverables and personal errands need a lightweight tool to categorize tasks, calculate effective priority scores dynamically, filter high-priority items, and retain task data across sessions without external database setup.

## User Stories
- **US-1 (Add Tasks):** As a user, I want to add Work Tasks (with project codes) and Personal Tasks (with urgency flags) with auto-incremented IDs so that I can categorize and track my responsibilities.
- **US-2 (View & Prioritize):** As a user, I want to view all tasks sorted automatically by their computed priority score so that I can focus on the highest-impact items first.
- **US-3 (Filter Tasks):** As a user, I want to filter tasks by a chosen minimum priority threshold (1–5) so that I can isolate urgent or high-priority work.
- **US-4 (Complete Tasks):** As a user, I want to mark a task as completed by its ID so that I can track progress.
- **US-5 (Persist Data):** As a user, I want tasks to load on startup and save to a CSV file upon exit so that my task list persists between runs.

## Design Pattern: Strategy
- **Participating Classes:** `TaskFilterStrategy` (strategy interface), `PriorityFilter` (concrete strategy), and `ConsoleApp` (client).
- **Design Benefit:** Decouples task filtering criteria from the CLI menu loop in `ConsoleApp`, allowing new filtering policies (such as filtering by completion status or task subtype) to be added without modifying the client.

## Main Design Decisions & Known Limitations
- **Design Decisions:**
  - Used an abstract `Task` superclass with `WorkTask` and `PersonalTask` subclasses to encapsulate shared state while allowing polymorphic `calculatePriorityScore()` behavior.
  - Used CSV file persistence with `try-with-resources` and auto-incrementing numeric IDs (`TaskStorage#getNextAvailableId`) for human-readable storage with zero external dependencies.
- **Known Limitations:**
  - CSV records are comma-delimited, so task titles and project codes cannot contain commas (enforced via `InvalidTaskDataException`).
