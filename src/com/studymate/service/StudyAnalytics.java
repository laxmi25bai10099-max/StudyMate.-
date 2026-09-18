package com.studymate.service;

import com.studymate.model.Task;
import com.studymate.model.Goal;
import com.studymate.model.Habit;
import com.studymate.model.Exam;
import com.studymate.repository.DataStore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Business rules for progress, task prioritization and dashboard analytics. */
public final class StudyAnalytics {
    private StudyAnalytics() { }

    public static int completedTasks(DataStore ds) {
        int count = 0;
        for (Task task : ds.getTasks()) if ("Completed".equalsIgnoreCase(task.getStatus())) count++;
        return count;
    }

    public static int taskCompletionPercent(DataStore ds) {
        int total = ds.getTasks().size();
        return total == 0 ? 0 : completedTasks(ds) * 100 / total;
    }

    public static int goalProgressPercent(DataStore ds) {
        if (ds.getGoals().isEmpty()) return 0;
        int total = 0, current = 0;
        for (Goal goal : ds.getGoals()) {
            total += goal.getMaxProgress();
            current += goal.getCurrentProgress();
        }
        return total == 0 ? 0 : current * 100 / total;
    }

    public static int habitConsistencyPercent(DataStore ds) {
        if (ds.getHabits().isEmpty()) return 0;
        int completed = 0, possible = ds.getHabits().size() * 7;
        for (Habit habit : ds.getHabits()) {
            boolean[] log = habit.getWeekLog();
            for (int i = 0; i < Math.min(7, log.length); i++) if (log[i]) completed++;
        }
        return possible == 0 ? 0 : completed * 100 / possible;
    }

    public static int overallStudyScore(DataStore ds) {
        return (taskCompletionPercent(ds) + goalProgressPercent(ds) + habitConsistencyPercent(ds)) / 3;
    }

    /** Returns incomplete tasks ordered by a transparent rule-based priority score. */
    public static List<TaskScore> prioritizedTasks(DataStore ds) {
        List<TaskScore> result = new ArrayList<>();
        for (Task task : ds.getTasks()) {
            if ("Completed".equalsIgnoreCase(task.getStatus())) continue;
            int score = score(task, ds);
            String level = score >= 8 ? "Critical" : score >= 5 ? "High" : score >= 3 ? "Medium" : "Low";
            result.add(new TaskScore(task, score, level));
        }
        result.sort(Comparator.comparingInt(TaskScore::getScore).reversed()
                .thenComparing(ts -> ts.getTask().getDueDate()));
        return result;
    }

    private static int score(Task task, DataStore ds) {
        int score = 0;
        long days = ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate());
        if (days < 0) score += 6;
        else if (days <= 1) score += 5;
        else if (days <= 3) score += 3;
        else if (days <= 7) score += 2;
        switch (task.getPriority() == null ? "" : task.getPriority().toLowerCase()) {
            case "high": score += 3; break;
            case "medium": score += 2; break;
            default: score += 1;
        }
        for (Exam exam : ds.getExams()) {
            long examDays = ChronoUnit.DAYS.between(LocalDate.now(), exam.getDate());
            if (examDays >= 0 && examDays <= 7 && exam.getSubject().equalsIgnoreCase(task.getSubject())) {
                score += 3;
                break;
            }
        }
        return score;
    }

    public static final class TaskScore {
        private final Task task;
        private final int score;
        private final String level;
        public TaskScore(Task task, int score, String level) { this.task = task; this.score = score; this.level = level; }
        public Task getTask() { return task; }
        public int getScore() { return score; }
        public String getLevel() { return level; }
    }
}
