package javaProjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StyleEditor extends JFrame {

    JTextArea textArea;
    JLabel statusLabel;

    FileManager fileManager;
    EditorFeatures editorFeatures;
    ThemeManager themeManager;

    public StyleEditor() {
        super("Advanced Java Swing Text Editor");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Status Bar
        statusLabel = new JLabel("Line: 1  Column: 1  Characters: 0");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(statusLabel, BorderLayout.SOUTH);

        // Managers
        editorFeatures = new EditorFeatures(this, textArea, statusLabel);
        fileManager = new FileManager(this, textArea, editorFeatures);
        themeManager = new ThemeManager(textArea);

        setJMenuBar(createMenuBar());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        /* -------- File Menu -------- */
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenu recentMenu = fileManager.getRecentFilesMenu();
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> fileManager.newFile());
        openItem.addActionListener(e -> fileManager.openFile());
        saveItem.addActionListener(e -> fileManager.saveFile());
        exitItem.addActionListener(e -> fileManager.exit());

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(recentMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        /* -------- Edit Menu -------- */
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem findItem = new JMenuItem("Find / Replace");

        undoItem.addActionListener(e -> editorFeatures.undo());
        redoItem.addActionListener(e -> editorFeatures.redo());
        findItem.addActionListener(e -> editorFeatures.showFindReplaceDialog());

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(findItem);

        /* -------- Font Menu -------- */
        JMenu fontMenu = editorFeatures.getFontMenu();

        /* -------- View Menu -------- */
        JMenu viewMenu = new JMenu("View");
        JMenuItem lightMode = new JMenuItem("Light Mode");
        JMenuItem darkMode = new JMenuItem("Dark Mode");

        lightMode.addActionListener(e -> themeManager.applyLightTheme());
        darkMode.addActionListener(e -> themeManager.applyDarkTheme());

        viewMenu.add(lightMode);
        viewMenu.add(darkMode);

        bar.add(fileMenu);
        bar.add(editMenu);
        bar.add(fontMenu);
        bar.add(viewMenu);

        editorFeatures.setupKeyBindings(fileManager);

        return bar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StyleEditor::new);
    }
}
