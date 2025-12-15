package javaProjects;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class FileManager {

    private JFrame frame;
    private JTextArea textArea;
    private JMenu recentFilesMenu;
    private LinkedList<File> recentFiles = new LinkedList<>();

    private EditorFeatures editorFeatures;

    public FileManager(JFrame frame, JTextArea textArea, EditorFeatures editorFeatures) {
        this.frame = frame;
        this.textArea = textArea;
        this.editorFeatures = editorFeatures;
        recentFilesMenu = new JMenu("Recent Files");
    }


    public JMenu getRecentFilesMenu() {
        return recentFilesMenu;
    }

    public void newFile() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Clear current document?", "New File", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            textArea.setText("");
        }
    }

    public void openFile() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                textArea.read(br, null);

                // IMPORTANT: reset undo/redo after loading new document
                editorFeatures.resetUndoManager();

                addRecentFile(file);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error opening file");
            }
        }
    }


    public void saveFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                textArea.write(bw);
                addRecentFile(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file");
            }
        }
    }

    private void addRecentFile(File file) {
        recentFiles.remove(file);
        recentFiles.addFirst(file);
        if (recentFiles.size() > 5) recentFiles.removeLast();
        updateRecentMenu();
    }

    private void updateRecentMenu() {
        recentFilesMenu.removeAll();
        for (File file : recentFiles) {
            JMenuItem item = new JMenuItem(file.getAbsolutePath());
            item.addActionListener(e -> openRecent(file));
            recentFilesMenu.add(item);
        }
    }

    private void openRecent(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            textArea.read(br, null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Cannot open file");
        }
    }

    public void exit() {
        System.exit(0);
    }
}
