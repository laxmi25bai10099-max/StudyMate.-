package com.studymate.service;

import com.studymate.model.Task;
import com.studymate.repository.DataStore;

import java.time.LocalDate;

/*
 * This class contains the basic task-related operations.
 * It is mainly used by the UI for checking task details,
 * counting tasks, and finding overdue tasks.
 */
public class TaskService {

    private static final String COMPLETED = "Completed";

    private final DataStore data;

    public TaskService(DataStore data) {
        this.data = data;
    }

    /*
     * Checks whether the task form has all the required details.
     * Returns an error message if something is missing.
     * Returns null when the details are valid.
     */
    public String validate(String title, String subject, String type, String date) {

        String[] values = {title, subject, type};
        String[] fieldNames = {"Task title", "Subject", "Task type"};

        for (int i = 0; i < values.length; i++) {
            String error = ValidationUtil.required(values[i], fieldNames[i]);

            if (error != null) {
                return error;
            }
        }

        // Check the date after checking the other fields.
        return ValidationUtil.date(date);
    }

    // Returns the number of tasks for a particular subject.
    public int countForSubject(String subject) {

        int count = 0;

        for (Task task : data.getTasks()) {
            if (task.getSubject().equalsIgnoreCase(subject)) {
                count++;
            }
        }

        return count;
    }

    // A task is overdue if its due date has passed and it is not completed.
    public boolean isOverdue(Task task) {

        boolean notCompleted =
                !COMPLETED.equalsIgnoreCase(task.getStatus());

        return notCompleted
                && task.getDueDate().isBefore(LocalDate.now());
    }
}
