# StudyMate Pro - Technical Design

## Architecture

StudyMate follows a lightweight layered architecture.

### Presentation Layer

`DashboardFrame` and Swing components collect input, display tables, navigate between modules and refresh the dashboard.

### Service / Business Layer

- `SubjectService` - subject validation and uniqueness rules.
- `TaskService` - task validation and task-related business checks.
- `ProgressService` - subject-wise completion calculations.
- `StudyAnalytics` - task completion, goal progress, habit consistency, overall score and rule-based task prioritization.
- `ValidationUtil` - reusable validation methods.

### Repository Layer

`DataStore` owns the application collections and persistence operations.

### Storage Layer

Java object serialization stores the `DataStore` object graph in `studymate_dashboard_data.ser`.

## Key Design Decisions

### Why Java Swing?

The project is a Java desktop application, so Swing provides a standard-library GUI without requiring external UI dependencies.

### Why local serialization?

The current scope is a single-user offline desktop application. Serialization is simple, reliable for the prototype scale and directly demonstrates Java I/O and persistence. The repository layer isolates this choice so it can later be replaced by a database repository.

### Why rule-based prioritization?

The requirement is to solve a practical problem and demonstrate understandable processing logic. A deterministic rule system is transparent, testable and honest; it is not presented as artificial intelligence or machine learning.

## Main Workflow

```text
Open application
      |
      v
Load DataStore
      |
      v
Dashboard
      |
      v
Select module
      |
      v
Enter / modify data
      |
      v
Validate input
   /       \
invalid    valid
  |          |
show error   v
           Update object list
                 |
                 v
              saveData()
                 |
                 v
           Refresh UI/analytics
```

## Core OOP Concepts Demonstrated

- Encapsulation through private fields and public getters/setters.
- Abstraction through service classes that expose business operations.
- Composition through `DataStore` maintaining collections of domain objects.
- Separation of concerns through model/service/repository/view packages.
- Reusable utility methods for validation and UI construction.

## Storage Schema

```text
DataStore
  |
  +-- subjects: List<Subject>
  +-- tasks: List<Task>
  +-- exams: List<Exam>
  +-- notes: List<Note>
  +-- goals: List<Goal>
  +-- habits: List<Habit>
  +-- scheduleItems: List<ScheduleItem>
  +-- reminders: List<Reminder>
```
