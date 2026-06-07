package gui.utils;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GuiTheme {

    public static final Color BG_DARK = new Color(18, 18, 30);
    public static final Color BG_PANEL = new Color(28, 28, 45);
    public static final Color BG_CARD = new Color(38, 38, 58);
    public static final Color ACCENT = new Color(99, 102, 241);
    public static final Color ACCENT_HOVER = new Color(79,  82, 221);
    public static final Color SUCCESS = new Color(52, 211, 153);
    public static final Color DANGER = new Color(248, 113, 113);
    public static final Color WARNING = new Color(251, 191, 36);
    public static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);
    public static final Color BORDER_COLOR = new Color(55, 65, 81);
    public static final Color TABLE_STRIPE = new Color(33, 33, 50);
    public static final Color TABLE_SEL = new Color(67, 70, 160);

    private static final Color[] USER_COLORS = {
        new Color(99,  102, 241),
        new Color(52,  211, 153),
        new Color(251, 191, 36),
        new Color(248, 113, 113),
        new Color(96,  165, 250),
        new Color(167, 139, 250),
        new Color(251, 146, 60),
        new Color(45,  212, 191),
    };

    private static final Map<String, Color> userColorMap = new HashMap<>();
    private static final AtomicInteger colorIndex = new AtomicInteger(0);

    public static Color getUserColor(String login) {
        if (login == null || login.isEmpty()) return USER_COLORS[0];
        return userColorMap.computeIfAbsent(login, k -> USER_COLORS[colorIndex.getAndIncrement() % USER_COLORS.length]);
    }

    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background", BG_PANEL);
        UIManager.put("Frame.background", BG_DARK);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.background", BG_PANEL);
        UIManager.put("Button.background", ACCENT);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focusPainted", false);
        UIManager.put("TextField.background", BG_CARD);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("TextField.border",              BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        UIManager.put("PasswordField.background", BG_CARD);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", BG_CARD);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground",  ACCENT);
        UIManager.put("ComboBox.selectionForeground",  Color.WHITE);
        UIManager.put("Table.background", BG_PANEL);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground", TABLE_SEL);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", BORDER_COLOR);
        UIManager.put("TableHeader.background", BG_CARD);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
        UIManager.put("ScrollPane.background", BG_PANEL);
        UIManager.put("Viewport.background", BG_PANEL);
        UIManager.put("TabbedPane.background", BG_PANEL);
        UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
        UIManager.put("TabbedPane.selected", BG_CARD);
        UIManager.put("TabbedPane.contentAreaColor", BG_PANEL);
        UIManager.put("CheckBox.background", BG_PANEL);
        UIManager.put("CheckBox.foreground", TEXT_PRIMARY);
        UIManager.put("ToolTip.background", BG_CARD);
        UIManager.put("ToolTip.foreground", TEXT_PRIMARY);
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
    }

    // Кнопка с accent-цветом и скруглёнными углами
    public static JButton createAccentButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? ACCENT_HOVER : ACCENT;
                if (!isEnabled()) bg = BG_CARD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 16, 34));
        return btn;
    }

    // Кнопка с danger-цветом
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? DANGER.darker() : DANGER;
                if (!isEnabled()) bg = BG_CARD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 16, 34));
        return btn;
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        );
    }
}
