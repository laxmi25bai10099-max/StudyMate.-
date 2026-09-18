package com.studymate;

import com.studymate.view.DashboardFrame;

import javax.swing.SwingUtilities;

/**
 * Entry point of the StudyMate desktop application.
 */
public class Main {

    public static void main(String[] args) {
        // Swing components must always be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.setVisible(true);
        });
    }
}