
package com.studymate.repository;

import com.studymate.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataStore implements Serializable {

    private static final long serialVersionUID = 1L;

    // File where all StudyMate data is saved
    private static final String FILE_NAME =
            System.getProperty("user.home")
            + File.separator
            + "StudyMateData"
            + File.separator
            + "studymate_dashboard_data.ser";

    private List<Subject> subjects = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private List<Exam> exams = new ArrayList<>();
    private List<Goal> goals = new ArrayList<>();
    private List<Habit> habits = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private List<ScheduleItem> scheduleItems = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();

    // Getters for the stored data
    public List<Subject> getSubjects() {
        return subjects;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    // Save the current data to a file
    public void saveData() {

        try {
            File parent = new File(FILE_NAME).getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }
        } catch (Exception ignored) {
            // Ignore folder creation errors here
        }

        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            output.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load saved data when the application starts
    public static DataStore loadData() {

        File file = new File(FILE_NAME);

        // If no saved file exists, start with sample data
        if (!file.exists()) {
            DataStore data = new DataStore();
            data.seedSampleData();
            data.saveData();
            return data;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (DataStore) input.readObject();

        } catch (Exception e) {

            // If the saved file cannot be read, use fresh sample data
            DataStore data = new DataStore();
            data.seedSampleData();
            return data;
        }
    }

    // Remove current data and load the sample data again
    public void resetToSampleData() {

        subjects.clear();
        tasks.clear();
        exams.clear();
        goals.clear();
        habits.clear();
        notes.clear();
        scheduleItems.clear();
        reminders.clear();

        seedSampleData();
    }

    // Adds some data so the dashboard is not empty when first opened
    private void seedSampleData() {

        LocalDate today = LocalDate.now();

        // Subjects
        subjects.add(new Subject(
                "CS101", "Data Structures", "Dr. Smith", "LH-101"));

        subjects.add(new Subject(
                "MAT102", "Mathematics", "Prof. Johnson", "LH-102"));

        subjects.add(new Subject(
                "PHY201", "Physics", "Dr. Alan", "AB-201"));

        subjects.add(new Subject(
                "CS202", "Digital Logic", "Dr. Clara", "LAB-1"));

        // Tasks
        tasks.add(new Task(
                "Java Lab Report", "Java", "Lab",
                today.plusDays(2), "High", "Pending"));

        tasks.add(new Task(
                "Maths Assignment", "Mathematics", "Assignment",
                today.plusDays(4), "Medium", "In Progress"));

        tasks.add(new Task(
                "Physics Notes", "Physics", "Notes",
                today.plusDays(1), "Low", "Completed"));

        tasks.add(new Task(
                "DSA Practice", "Computer Science", "Task",
                today.plusDays(3), "High", "Pending"));

        tasks.add(new Task(
                "English Essay", "English", "Assignment",
                today.plusDays(7), "Medium", "Pending"));

        // Exams
        exams.add(new Exam(
                "Java", "Midterm", today.plusDays(5),
                "10:00 AM", "AB-201"));

        exams.add(new Exam(
                "Mathematics", "Test", today.plusDays(12),
                "11:00 AM", "LH-102"));

        exams.add(new Exam(
                "Physics", "End Sem", today.plusDays(26),
                "09:00 AM", "AB-201"));

        // Goals
        goals.add(new Goal(
                "Complete 5 Java assignments", "Academic", 3, 5));

        goals.add(new Goal(
                "Solve 20 DSA problems", "Academic", 12, 20));

        goals.add(new Goal(
                "Improve CGPA to 9+", "Academic", 6, 9));

        goals.add(new Goal(
                "Read 2 books this month", "Personal", 1, 2));

        // Habits
        Habit javaStudy = new Habit(
                "Study Java",
                new boolean[]{true, true, true, true, false, false, false},
                4, 14);

        Habit reading = new Habit(
                "Read 30 min",
                new boolean[]{true, true, true, false, true, false, false},
                3, 10);

        Habit exercise = new Habit(
                "Exercise",
                new boolean[]{true, true, true, true, true, false, false},
                5, 12);

        Habit dsa = new Habit(
                "Practice DSA",
                new boolean[]{true, true, true, false, false, true, true},
                2, 8);

        Habit sleep = new Habit(
                "Sleep on time",
                new boolean[]{true, false, true, false, true, false, false},
                1, 7);

        habits.add(javaStudy);
        habits.add(reading);
        habits.add(exercise);
        habits.add(dsa);
        habits.add(sleep);

        // Add some previous habit activity for the heatmap
        seedHabitHistory(javaStudy, today, 0);
        seedHabitHistory(reading, today, 1);
        seedHabitHistory(exercise, today, 2);
        seedHabitHistory(dsa, today, 3);
        seedHabitHistory(sleep, today, 4);

        // Notes
        notes.add(new Note(
                "Java OOP Concepts",
                "Java",
                "Encapsulation, Inheritance, Polymorphism...",
                "Java, OOP",
                today.minusDays(1)));

        notes.add(new Note(
                "Important Formulas",
                "Mathematics",
                "Calculus and integration formulas...",
                "Maths, Formulas",
                today.minusDays(3)));

        notes.add(new Note(
                "Project Ideas",
                "General",
                "AI Study mate desktop app layout...",
                "Ideas, Project",
                today.minusDays(5)));

        // Timetable
        scheduleItems.add(new ScheduleItem(
                "Mon", "08:00 - 10:00",
                "Data Structures", "LH-101"));

        scheduleItems.add(new ScheduleItem(
                "Mon", "10:15 - 12:00",
                "Mathematics", "LH-102"));

        scheduleItems.add(new ScheduleItem(
                "Fri", "08:00 - 10:00",
                "English", "LH-101"));

        // Reminders
        reminders.add(new Reminder(
                "Java lab submission",
                "Finish and submit the lab report.",
                java.time.LocalDateTime.now()
                        .plusDays(1)
                        .withHour(18)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)));

        reminders.add(new Reminder(
                "Maths revision",
                "Revise integration formulas.",
                java.time.LocalDateTime.now()
                        .plusDays(2)
                        .withHour(20)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)));
    }

    // Creates some demo history for the habit heatmap
    private void seedHabitHistory(
            Habit habit, LocalDate today, int offset) {

        for (int daysAgo = 1; daysAgo <= 45; daysAgo++) {

            int pattern = (daysAgo + offset * 2) % 7;

            if (pattern == 0
                    || pattern == 1
                    || daysAgo % (5 + offset) == 0) {

                habit.setCompletedOn(
                        today.minusDays(daysAgo), true);
            }
        }

        // Set today's status
        habit.setCompletedOn(
                today, offset % 2 == 0);

        recalculateSeedStreak(habit, today);
    }

    // Recalculate the current and best streak after adding demo history
    private void recalculateSeedStreak(
            Habit habit, LocalDate today) {

        int currentStreak = 0;
        LocalDate date = today;

        while (habit.isCompletedOn(date)) {
            currentStreak++;
            date = date.minusDays(1);
        }

        int bestStreak = 0;
        int runningStreak = 0;

        for (int i = 44; i >= 0; i--) {

            if (habit.isCompletedOn(today.minusDays(i))) {
                runningStreak++;
                bestStreak = Math.max(
                        bestStreak, runningStreak);
            } else {
                runningStreak = 0;
            }
        }

        habit.setCurrentStreak(currentStreak);
        habit.setBestStreak(
                Math.max(bestStreak, habit.getBestStreak()));
    }
}
