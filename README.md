# StudyMate Pro – VITyarthi Project

## About the Project

StudyMate Pro is a Java Swing-based desktop application developed to help students manage their academic activities in one place. It provides an offline workspace for managing subjects, tasks, notes, schedules, exams, reminders, goals, habits, and study progress.

## Problem Statement

Students often manage academic information across different apps or notebooks, which can make it difficult to track deadlines, exams, tasks, and study progress. StudyMate Pro brings these activities together in one simple application.

## Objectives

- Apply Object-Oriented Programming concepts in a practical project.
- Develop a user-friendly Java Swing desktop application.
- Implement multiple functional CRUD-based modules.
- Store data locally without requiring a database server.
- Add validation and error handling.
- Track academic and productivity progress.
- Provide rule-based task recommendations.
- Demonstrate modular programming and software testing.

## Main Features

- **Dashboard** – Overview of tasks, subjects, progress, and suggestions.
- **Subjects** – Add, edit, delete, and manage subjects.
- **Tasks** – Create, edit, delete, and complete study tasks.
- **Notes** – Create, edit, and delete study notes.
- **Schedule** – Maintain a weekly study timetable.
- **Reminders** – Set date and time-based reminders.
- **Exams & Tests** – Record exams and view countdowns.
- **Goals** – Create goals and track their progress.
- **Habit Tracker** – Track daily habits, streaks, and yearly activity.
- **Progress & Reports** – View study and productivity statistics.
- **Achievements** – Track activity-based milestones.
- **Settings** – Manage saved data and reset sample data.

## Technology Used

- Java
- Java Swing / AWT
- Java Time API
- Java Object Serialization
- Object-Oriented Programming
- Layered architecture: `model`, `service`, `repository`, `view`, `util`
- Dependency-free self-testing

The application does not require MySQL, JDBC, SQL Server, or third-party runtime libraries.

## Data Storage

StudyMate Pro is a single-user offline application. Application data is stored locally using Java Object Serialization through the repository layer.
### Command-Line Execution

Open PowerShell in the project root directory and run:


```powershell
if (!(Test-Path bin)) { New-Item -ItemType Directory bin }

javac -d bin (Get-ChildItem -Recurse -Filter *.java .\src | ForEach-Object { $_.FullName })

java -cp bin com.studymate.Main
```

## How to Run using VS code

1. Install JDK 17 or JDK 21.
2. Open the project folder in VS Code.
3. Open `src/com/studymate/Main.java`.
4. Click **Run** or **Debug** above the `main()` method.
5. The StudyMate dashboard will open automatically.

> **Important:** Use the Java **Run | Debug** option in `Main.java`. Do not use the Code Runner extension or `Ctrl + Alt + N`.

## Testing

The project includes a dependency-free self-test class:

`test/StudyMateSelfTest.java`

It tests important areas such as validation, calculations, progress tracking, and application logic.

## Sample Data

The first run includes sample subjects, tasks, exams, goals, reminders, notes, schedules, and habit activity so the main features can be viewed immediately.

Sample data can be restored using:

**Settings → Reset Sample Data**

## Reminder Limitation

Reminders are checked while the application is running. They do not generate operating-system notifications after the application is closed.

## Task Recommendation

StudyMate uses a simple rule-based approach to recommend tasks based on factors such as deadline, priority, and upcoming examinations. The rules are transparent and easy to test.

## Future Enhancements

- SQLite/MySQL database support
- User accounts and role-based access
- Cloud synchronization
- Mobile/web versions
- OS-level notifications
- Advanced charts and PDF reports

## Conclusion

StudyMate Pro brings common academic activities into one offline desktop application. The project demonstrates practical use of Java, OOP, Swing GUI development, file-based persistence, validation, modular programming, testing, and basic data-driven calculations.ctical example of how students could manage their various study activities, I was also able to demonstrate Java OOP concepts, Swing GUI development, file-based persistence, validation, modular programming, testing and basic data-driven calculations, in one complete application.
