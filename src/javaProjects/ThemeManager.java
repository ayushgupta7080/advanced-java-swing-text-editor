package javaProjects;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    private JTextArea textArea;

    public ThemeManager(JTextArea textArea) {
        this.textArea = textArea;
        applyLightTheme();
    }

    public void applyLightTheme() {
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.BLACK);
    }

    public void applyDarkTheme() {
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
    }
}
