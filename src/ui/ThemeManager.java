package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ThemeManager {
    // Color Palette
    public static final Color PRIMARY = new Color(28, 40, 51); // Deep Navy
    public static final Color SECONDARY = new Color(212, 175, 55); // Gold accent
    public static final Color BACKGROUND = new Color(245, 245, 240); // Cream White
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(100, 100, 100);
    public static final Color ACCENT = new Color(52, 73, 94);
    public static final Color WHITE = Color.WHITE;

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public static void applyTheme() {
        try {
            // Priority: FlatLaf (Modern, sleek, dark/light modes)
            // If you add the FlatLaf JAR to your lib folder, this will take effect!
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            try {
                // Fallback: System Look (Better than default Cross-Platform)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        // Global Style Overrides
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.font", FONT_REGULAR);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT_REGULAR);
        UIManager.put("PasswordField.font", FONT_REGULAR);

        // Table styling (global)
        UIManager.put("Table.font", FONT_REGULAR);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("Table.rowHeight", 35);
        UIManager.put("Table.selectionBackground", SECONDARY);
        UIManager.put("Table.selectionForeground", PRIMARY);
    }

    public static JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOLD);
        button.setForeground(WHITE);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), 10, 10));
                g2.dispose();
                super.paint(g, c);
            }
        });

        return button;
    }

    public static JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    public static JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(35);
        table.setSelectionBackground(SECONDARY);
        table.setSelectionForeground(PRIMARY);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(5, 5));

        // Custom Header Styling
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
    }

    private static class HeaderRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(PRIMARY);
            setForeground(WHITE);
            setFont(FONT_BOLD);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(255, 255, 255, 50)));
            return this;
        }
    }
}
