package com.studymate.util;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

/**
 * One place for every colour, font and reusable component style, so the whole
 * application keeps the same look.
 */
public class UITheme {

    // Brand colours
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);        // dark navy slate
    public static final Color SIDEBAR_HOVER = new Color(30, 41, 59);     // hover slate
    public static final Color MAIN_BG = new Color(241, 245, 249);        // soft light slate
    public static final Color CARD_BG = new Color(255, 255, 255);        // white card
    public static final Color ACCENT_PRIMARY = new Color(37, 99, 235);   // royal blue
    public static final Color ACCENT_LIGHT = new Color(219, 234, 254);   // light blue tag

    // Status colours (background + matching text colour)
    public static final Color COLOR_PENDING_BG = new Color(254, 243, 199);
    public static final Color COLOR_PENDING_FG = new Color(180, 83, 9);
    public static final Color COLOR_INPROGRESS_BG = new Color(224, 231, 255);
    public static final Color COLOR_INPROGRESS_FG = new Color(67, 56, 202);
    public static final Color COLOR_COMPLETED_BG = new Color(220, 252, 231);
    public static final Color COLOR_COMPLETED_FG = new Color(21, 128, 61);
    public static final Color COLOR_HIGH_BG = new Color(254, 226, 226);
    public static final Color COLOR_HIGH_FG = new Color(185, 28, 28);

    // Text colours
    public static final Color TEXT_DARK = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color BORDER_COLOR = new Color(226, 232, 240);

    // Fonts
    public static final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);

    /** White panel with a thin rounded border, used as the base of every card. */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        return card;
    }

    /** Small bold button, keeps its own border. */
    public static JButton createStyledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(FONT_SMALL_BOLD);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(6, 14, 6, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /** Coloured pill used for statuses such as Pending or Completed. */
    public static JLabel createBadge(String text, Color background, Color foreground) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(background);
        badge.setForeground(foreground);
        badge.setFont(FONT_SMALL_BOLD);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }

    /** Flat, borderless button used for the toolbars and quick actions. */
    public static JButton styledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}