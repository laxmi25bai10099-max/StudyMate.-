package com.studymate.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Habit implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private boolean[] weekLog;
    private int currentStreak, bestStreak;
    // Date-based log used by the yearly activity heatmap.
    private Map<LocalDate, Boolean> activityLog;

    public Habit(String title, boolean[] weekLog, int currentStreak, int bestStreak) {
        this.title = title;
        this.weekLog = weekLog == null ? new boolean[7] : weekLog;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
        this.activityLog = new HashMap<>();
    }

    public String getTitle() { return title; }
    public boolean[] getWeekLog() { return weekLog; }
    public int getCurrentStreak() { return currentStreak; }
    public int getBestStreak() { return bestStreak; }

    public void setTitle(String v) { title = v; }
    public void setWeekLog(boolean[] v) { weekLog = v == null ? new boolean[7] : v; }
    public void setCurrentStreak(int v) { currentStreak = v; }
    public void setBestStreak(int v) { bestStreak = v; }

    public Map<LocalDate, Boolean> getActivityLog() {
        if (activityLog == null) activityLog = new HashMap<>();
        return activityLog;
    }

    public boolean isCompletedOn(LocalDate date) {
        return Boolean.TRUE.equals(getActivityLog().get(date));
    }

    public void setCompletedOn(LocalDate date, boolean completed) {
        if (completed) getActivityLog().put(date, true);
        else getActivityLog().remove(date);
    }
}
