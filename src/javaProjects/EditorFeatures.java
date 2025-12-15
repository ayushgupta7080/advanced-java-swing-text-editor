package javaProjects;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;

public class EditorFeatures {

    private JTextArea textArea;
    private UndoManager undoManager = new UndoManager();
    private JLabel statusLabel;

    public EditorFeatures(JFrame frame, JTextArea textArea, JLabel statusLabel) {
        this.textArea = textArea;
        this.statusLabel = statusLabel;

        // Undo / Redo support
        textArea.getDocument().addUndoableEditListener(undoManager);

        // Status bar updates
        textArea.addCaretListener(e -> updateStatus());
    }

    /* ================= FONT MENU ================= */

    public JMenu getFontMenu() {
        JMenu fontMenu = new JMenu("Font");

        JMenu familyMenu = new JMenu("Font Family");
        JMenu sizeMenu = new JMenu("Font Size");

        String[] families = {"Arial", "Times New Roman", "Consolas"};
        int[] sizes = {10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40};

        // Font family options
        for (String family : families) {
            JMenuItem item = new JMenuItem(family);
            item.addActionListener(e ->
                    applyFont(family, textArea.getFont().getStyle(), textArea.getFont().getSize())
            );
            familyMenu.add(item);
        }

        // Font size options
        for (int size : sizes) {
            JMenuItem item = new JMenuItem(String.valueOf(size));
            item.addActionListener(e ->
                    applyFont(textArea.getFont().getFamily(), textArea.getFont().getStyle(), size)
            );
            sizeMenu.add(item);
        }

        JCheckBoxMenuItem boldItem = new JCheckBoxMenuItem("Bold");
        JCheckBoxMenuItem italicItem = new JCheckBoxMenuItem("Italic");

        boldItem.addActionListener(e ->
                updateStyle(boldItem.isSelected(), italicItem.isSelected())
        );

        italicItem.addActionListener(e ->
                updateStyle(boldItem.isSelected(), italicItem.isSelected())
        );

        fontMenu.add(familyMenu);
        fontMenu.add(sizeMenu);
        fontMenu.addSeparator();
        fontMenu.add(boldItem);
        fontMenu.add(italicItem);

        return fontMenu;
    }

    private void applyFont(String family, int style, int size) {
        textArea.setFont(new Font(family, style, size));
    }

    private void updateStyle(boolean bold, boolean italic) {
        int style = Font.PLAIN;
        if (bold) style |= Font.BOLD;
        if (italic) style |= Font.ITALIC;

        Font current = textArea.getFont();
        textArea.setFont(new Font(current.getFamily(), style, current.getSize()));
    }

    /* ================= UNDO / REDO ================= */

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    // undo working again
    public void resetUndoManager() {
        undoManager.discardAllEdits();
        textArea.getDocument().addUndoableEditListener(undoManager);
    }


    /* ================= FIND & REPLACE ================= */

    public void showFindReplaceDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Find & Replace");
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2));
        dialog.setLocationRelativeTo(null);

        JTextField findField = new JTextField();
        JTextField replaceField = new JTextField();

        JButton findBtn = new JButton("Find");
        JButton replaceBtn = new JButton("Replace All");

        findBtn.addActionListener(e -> findText(findField.getText()));
        replaceBtn.addActionListener(e ->
                textArea.setText(
                        textArea.getText().replace(findField.getText(), replaceField.getText())
                )
        );

        dialog.add(new JLabel("Find"));
        dialog.add(findField);
        dialog.add(new JLabel("Replace"));
        dialog.add(replaceField);
        dialog.add(findBtn);
        dialog.add(replaceBtn);

        dialog.setVisible(true);
    }

    private void findText(String text) {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();

        if (text == null || text.isEmpty()) return;

        String content = textArea.getText();
        DefaultHighlighter.DefaultHighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

        int index = 0;
        while ((index = content.indexOf(text, index)) >= 0) {
            try {
                highlighter.addHighlight(
                        index,
                        index + text.length(),
                        painter
                );
                index += text.length(); // move past current match
            } catch (BadLocationException ignored) {}
        }
    }

    /* ================= STATUS BAR ================= */

    private void updateStatus() {
        int caret = textArea.getCaretPosition();
        int line = 1, column = 1;

        try {
            line = textArea.getLineOfOffset(caret) + 1;
            column = caret - textArea.getLineStartOffset(line - 1) + 1;
        } catch (Exception ignored) {}

        statusLabel.setText(
                "Line: " + line +
                        "  Column: " + column +
                        "  Characters: " + textArea.getText().length()
        );
    }

    /* ================= KEY BINDINGS ================= */

    public void setupKeyBindings(FileManager fm) {
        InputMap im = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = textArea.getActionMap();

        // Find
        im.put(KeyStroke.getKeyStroke("control F"), "find");
        am.put("find", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                showFindReplaceDialog();
            }
        });

        // Open
        im.put(KeyStroke.getKeyStroke("control O"), "open");
        am.put("open", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fm.openFile();
            }
        });

        // Save
        im.put(KeyStroke.getKeyStroke("control S"), "save");
        am.put("save", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fm.saveFile();
            }
        });
    }

}
