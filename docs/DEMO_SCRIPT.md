# StudyMate Pro - VITyarthi Demo Script

## 1. Problem (30 seconds)

"Students manage assignments, exams, notes, schedules and goals in different places. StudyMate Pro centralizes these academic activities in one Java desktop application."

## 2. Architecture (45 seconds)

Point to the architecture diagram and explain:

"The application is organized into model, service, repository and view packages. The Swing UI collects input, service classes apply validation and business rules, DataStore manages collections and Java serialization persists the object graph locally."

## 3. Functional Demo (2-3 minutes)

Recommended order:

1. Add a subject.
2. Add a task with a due date and priority.
3. Mark the task completed.
4. Add an exam for the same subject.
5. Add a goal and increment progress.
6. Toggle a habit.
7. Open Progress & Reports.
8. Return to Dashboard and show live counts and recommended tasks.
9. Restart the application to demonstrate persistence.

## 4. Explain the Recommendation Feature

"The recommendation engine is rule-based, not machine learning. It assigns points for deadline urgency, user priority and an upcoming exam for the same subject. This produces an explainable score and a Critical/High/Medium/Low level."

## 5. Explain OOP

- Each academic entity is represented by a Java class.
- Fields are private and accessed through methods.
- Services separate business logic from the UI.
- DataStore composes lists of domain objects.
- Packages provide separation of concerns.

## 6. Explain Persistence

"DataStore uses ObjectOutputStream to serialize the application's object graph into a local .ser file. ObjectInputStream loads it when the application starts."

## 7. Explain Limitations

"The current version is a single-user offline desktop application. Cloud sync, authentication and database-backed multi-user support are planned future enhancements."
