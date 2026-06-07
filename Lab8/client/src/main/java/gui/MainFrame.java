package gui;

import common.Response;
import gui.dialogs.HumanBeingDialog;
import gui.panels.TablePanel;
import gui.panels.VisualizationPanel;
import gui.utils.GuiTheme;
import managers.LocaleManager;
import managers.NetworkManager;
import models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainFrame extends JFrame {

    private final NetworkManager network;

    // Флаг для предотвращения спама кнопок
    private final AtomicBoolean operationInProgress = new AtomicBoolean(false);

    private JLabel userLabel;
    private JLabel userValueLabel;
    private JLabel langLabel;
    private JComboBox<String> langCombo;
    private JLabel statusLabel;

    private JTabbedPane tabbedPane;
    private TablePanel tablePanel;
    private VisualizationPanel visualPanel;

    private JButton addBtn, editBtn, deleteBtn, refreshBtn, clearBtn;
    private JButton sortBtn, reorderBtn, infoBtn, helpBtn;
    private JButton logoutBtn, exitBtn, commandsBtn;

    private javax.swing.Timer refreshTimer;

    // Listener-ссылка для удаления при logout
    private final Runnable localeListener = this::updateTexts;

    public MainFrame(NetworkManager network) {
        this.network = network;
        initUI();
        LocaleManager.addListener(localeListener);
        startAutoRefresh();
    }

    private void initUI() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { confirmExit(); }
        });

        setTitle(LocaleManager.get("app.title"));
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(GuiTheme.BG_DARK);
        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        setContentPane(root);
        updateTexts();
    }

    // Верхняя панель
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setBackground(GuiTheme.BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, GuiTheme.BORDER_COLOR),
                new EmptyBorder(8, 16, 8, 16)));

        JLabel appTitle = new JLabel("◈ HumanBeing Collection");
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        appTitle.setForeground(GuiTheme.ACCENT);

        // Текущий пользователь
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        userPanel.setOpaque(false);
        userLabel = new JLabel();
        userLabel.setForeground(GuiTheme.TEXT_MUTED);
        userLabel.setFont(userLabel.getFont().deriveFont(12f));
        userValueLabel = new JLabel(network.getCurrentLogin() != null ? network.getCurrentLogin() : "?");
        userValueLabel.setForeground(GuiTheme.SUCCESS);
        userValueLabel.setFont(userValueLabel.getFont().deriveFont(Font.BOLD, 13f));
        userPanel.add(userLabel);
        userPanel.add(userValueLabel);

        // Смена языка
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        langPanel.setOpaque(false);
        langLabel = new JLabel();
        langLabel.setForeground(GuiTheme.TEXT_MUTED);
        langLabel.setFont(langLabel.getFont().deriveFont(11f));

        String[] names = LocaleManager.SUPPORTED_LOCALES.stream()
                .map(LocaleManager::getLocaleName).toArray(String[]::new);
        langCombo = new JComboBox<>(names);
        langCombo.setBackground(GuiTheme.BG_CARD);
        langCombo.setForeground(GuiTheme.TEXT_PRIMARY);
        langCombo.setPreferredSize(new Dimension(130, 26));
        langCombo.setSelectedIndex(Math.max(0,
                LocaleManager.SUPPORTED_LOCALES.indexOf(LocaleManager.getCurrentLocale())));
        langCombo.addActionListener(e -> {
            int idx = langCombo.getSelectedIndex();
            if (idx >= 0 && idx < LocaleManager.SUPPORTED_LOCALES.size()) {
                LocaleManager.setLocale(LocaleManager.SUPPORTED_LOCALES.get(idx));
            }
        });
        langPanel.add(langLabel);
        langPanel.add(langCombo);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(userPanel, BorderLayout.WEST);
        centerPanel.add(langPanel, BorderLayout.EAST);

        bar.add(appTitle,    BorderLayout.WEST);
        bar.add(centerPanel, BorderLayout.CENTER);
        return bar;
    }

    // Sidebar
    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(GuiTheme.BG_PANEL);
        side.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, GuiTheme.BORDER_COLOR),
                new EmptyBorder(16, 12, 16, 12)));
        side.setPreferredSize(new Dimension(175, 0));

        addBtn = sideBtn("main.btn.add",     GuiTheme.ACCENT);
        editBtn = sideBtn("main.btn.edit",    GuiTheme.BG_CARD);
        deleteBtn = sideBtn("main.btn.delete",  GuiTheme.DANGER);
        refreshBtn = sideBtn("main.btn.refresh", GuiTheme.BG_CARD);
        clearBtn = sideBtn("main.btn.clear",   new Color(180, 130, 20));
        sortBtn = sideBtn("main.btn.sort",    GuiTheme.BG_CARD);
        reorderBtn = sideBtn("main.btn.reorder", GuiTheme.BG_CARD);
        infoBtn = sideBtn("main.btn.info",    GuiTheme.BG_CARD);
        helpBtn = sideBtn("main.btn.help",    GuiTheme.BG_CARD);
        commandsBtn = sideBtn("cmd.dialog.title", GuiTheme.BG_CARD);
        logoutBtn = sideBtn("main.btn.logout",  new Color(60, 50, 30));
        exitBtn = sideBtn("main.btn.exit",    new Color(80, 20, 20));

        side.add(sectionLabel("COLLECTION"));
        side.add(Box.createVerticalStrut(4));
        side.add(addBtn); side.add(Box.createVerticalStrut(4));
        side.add(editBtn); side.add(Box.createVerticalStrut(4));
        side.add(deleteBtn); side.add(Box.createVerticalStrut(4));
        side.add(refreshBtn); side.add(Box.createVerticalStrut(4));
        side.add(clearBtn);
        side.add(Box.createVerticalStrut(16));
        side.add(sectionLabel("TOOLS"));
        side.add(Box.createVerticalStrut(4));
        side.add(sortBtn); side.add(Box.createVerticalStrut(4));
        side.add(reorderBtn); side.add(Box.createVerticalStrut(4));
        side.add(infoBtn); side.add(Box.createVerticalStrut(4));
        side.add(helpBtn); side.add(Box.createVerticalStrut(4));
        side.add(commandsBtn);
        side.add(Box.createVerticalGlue());
        side.add(sectionLabel("ACCOUNT"));
        side.add(Box.createVerticalStrut(4));
        side.add(logoutBtn); side.add(Box.createVerticalStrut(4));
        side.add(exitBtn);

        addBtn.addActionListener(e -> doAdd());
        editBtn.addActionListener(e -> doEdit());
        deleteBtn.addActionListener(e -> doDelete());
        refreshBtn.addActionListener(e -> doRefresh());
        clearBtn.addActionListener(e -> doClear());
        sortBtn.addActionListener(e -> doSort());
        reorderBtn.addActionListener(e -> doReorder());
        infoBtn.addActionListener(e -> doInfo());
        helpBtn.addActionListener(e -> doHelp());
        commandsBtn.addActionListener(e -> doCommandsMenu());
        logoutBtn.addActionListener(e -> doLogout());
        exitBtn.addActionListener(e -> confirmExit());

        return side;
    }

    private JButton sideBtn(String key, Color bg) {
        JButton btn = new JButton(LocaleManager.get(key)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = isEnabled()
                    ? (getModel().isRollover() ? bg.brighter() : bg)
                    : GuiTheme.BG_DARK;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(GuiTheme.TEXT_MUTED);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 10f));
        l.setBorder(new EmptyBorder(4, 2, 2, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // Center
    private JComponent buildCenter() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(GuiTheme.BG_PANEL);
        tabbedPane.setForeground(GuiTheme.TEXT_PRIMARY);

        tablePanel  = new TablePanel();
        visualPanel = new VisualizationPanel();

        // Двойной клик в таблице — редактировать
        tablePanel.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) doEdit();
            }
        });

        // Выбор в визуализации — предлагаем редактировать если владелец
        visualPanel.setOnSelect(h -> {
            if (h == null) return;
            String owner = h.getOwnerLogin();
            String me    = network.getCurrentLogin();
            if (owner != null && me != null && owner.equals(me)) {
                int r = JOptionPane.showConfirmDialog(MainFrame.this,
                        h.getName() + " — " + LocaleManager.get("main.btn.edit") + "?",
                        LocaleManager.get("msg.confirm.title"),
                        JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) doEditHuman(h);
            }
        });

        tabbedPane.addTab(LocaleManager.get("main.tab.table"),  tablePanel);
        tabbedPane.addTab(LocaleManager.get("main.tab.visual"), visualPanel);

        return tabbedPane;
    }

    // Status bar
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(GuiTheme.BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, GuiTheme.BORDER_COLOR),
                new EmptyBorder(4, 12, 4, 12)));
        statusLabel = new JLabel(LocaleManager.get("main.status.ready"));
        statusLabel.setForeground(GuiTheme.TEXT_MUTED);
        statusLabel.setFont(statusLabel.getFont().deriveFont(11f));
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    // Автообновление
    private void startAutoRefresh() {
        doRefresh();
        refreshTimer = new javax.swing.Timer(3000, e -> doRefreshSilent());
        refreshTimer.start();
    }

    private void doRefresh() {
        setStatus(LocaleManager.get("main.status.loading"), GuiTheme.TEXT_MUTED);
        new Thread(() -> {
            Response r = network.sendCommand(CommandType.SHOW, null, null);
            SwingUtilities.invokeLater(() -> {
                applyShowResponse(r);
                setStatus(LocaleManager.get("main.status.ready"), GuiTheme.TEXT_MUTED);
            });
        }, "refresh-thread").start();
    }

    private void doRefreshSilent() {
        new Thread(() -> {
            Response r = network.sendCommand(CommandType.SHOW, null, null);
            SwingUtilities.invokeLater(() -> applyShowResponse(r));
        }, "silent-refresh-thread").start();
    }

    private void applyShowResponse(Response r) {
        if (r == null || r.getData() == null) return;
        if (!(r.getData() instanceof Collection<?>)) return;
        List<HumanBeing> list = new ArrayList<>();
        for (Object o : (Collection<?>) r.getData()) {
            if (o instanceof HumanBeing) list.add((HumanBeing) o);
        }
        tablePanel.setData(list);
        visualPanel.updateData(list);
    }

    // Блокировка кнопок во время операций
    private void setButtonsEnabled(boolean enabled) {
        addBtn.setEnabled(enabled);
        editBtn.setEnabled(enabled);
        deleteBtn.setEnabled(enabled);
        clearBtn.setEnabled(enabled);
        sortBtn.setEnabled(enabled);
        reorderBtn.setEnabled(enabled);
        commandsBtn.setEnabled(enabled);
    }

    // Команды
    private void doAdd() {
        HumanBeingDialog dlg = new HumanBeingDialog(this, false, null);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            sendCommandAndRefresh(CommandType.ADD, null, dlg.getResult());
        }
    }

    private void doEdit() {
        HumanBeing h = tablePanel.getSelectedHuman();
        if (h == null) {
            showInfo(LocaleManager.get("msg.noselect")); return;
        }
        doEditHuman(h);
    }

    private void doEditHuman(HumanBeing h) {
        if (h == null) return;
        String owner = h.getOwnerLogin();
        String me = network.getCurrentLogin();
        if (owner == null || me == null || !owner.equals(me)) {
            showInfo(LocaleManager.get("msg.notowner")); return;
        }
        HumanBeingDialog dlg = new HumanBeingDialog(this, true, h);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            sendCommandAndRefresh(CommandType.UPDATE, h.getId(), dlg.getResult());
        }
    }

    private void doDelete() {
        HumanBeing h = tablePanel.getSelectedHuman();
        if (h == null) h = visualPanel.getSelected();
        if (h == null) { showInfo(LocaleManager.get("msg.noselect")); return; }

        String owner = h.getOwnerLogin();
        String me    = network.getCurrentLogin();
        if (owner == null || me == null || !owner.equals(me)) {
            showInfo(LocaleManager.get("msg.notowner")); return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                LocaleManager.get("msg.confirm.delete") + "\n\"" + h.getName() + "\"",
                LocaleManager.get("msg.confirm.title"),
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            sendCommandAndRefresh(CommandType.REMOVE_BY_ID, h.getId(), null);
        }
    }

    private void doClear() {
        int r = JOptionPane.showConfirmDialog(this,
                LocaleManager.get("msg.confirm.delete") + " (clear)",
                LocaleManager.get("msg.confirm.title"),
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            sendCommandAndRefresh(CommandType.CLEAR, null, null);
        }
    }

    private void doSort() {
        sendCommandAndRefresh(CommandType.SORT, null, null);
    }

    private void doReorder() {
        sendCommandAndRefresh(CommandType.REORDER, null, null);
    }

    private void doInfo() {
        if (!startOperation()) return;
        new Thread(() -> {
            Response r = network.sendCommand(CommandType.INFO, null, null);
            SwingUtilities.invokeLater(() -> {
                endOperation();
                showResponseText(r, LocaleManager.get("main.btn.info"));
            });
        }, "info-thread").start();
    }

    private void doHelp() {
        // Help показываем локально — не идём на сервер, язык клиента
        String helpText = LocaleManager.get("help.text");
        JTextArea ta = new JTextArea(helpText);
        ta.setEditable(false);
        ta.setLineWrap(false);
        ta.setBackground(GuiTheme.BG_CARD);
        ta.setForeground(GuiTheme.TEXT_PRIMARY);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(560, 320));
        sp.setBorder(BorderFactory.createLineBorder(GuiTheme.BORDER_COLOR));
        JOptionPane.showMessageDialog(this, sp,
                LocaleManager.get("main.btn.help"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void doCommandsMenu() {
        String[] options = {
            "COUNT_BY_SOUNDTRACK_NAME",
            "FILTER_CONTAINS_NAME",
            "PRINT_FIELD_DESCENDING_IMPACT_SPEED",
            "REMOVE_GREATER",
            "EXECUTE_SCRIPT"
        };
        String choice = (String) JOptionPane.showInputDialog(this,
                LocaleManager.get("cmd.dialog.title"),
                LocaleManager.get("cmd.dialog.title"),
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == null) return;

        switch (choice) {
            case "COUNT_BY_SOUNDTRACK_NAME" -> {
                String arg = JOptionPane.showInputDialog(this,
                        LocaleManager.get("cmd.count_soundtrack"));
                if (arg == null) return;
                sendAndShowResult(CommandType.COUNT_BY_SOUNDTRACK_NAME, arg.trim());
            }
            case "FILTER_CONTAINS_NAME" -> {
                String arg = JOptionPane.showInputDialog(this,
                        LocaleManager.get("cmd.filter_name"));
                if (arg == null) return;
                sendAndShowResult(CommandType.FILTER_CONTAINS_NAME, arg.trim());
            }
            case "PRINT_FIELD_DESCENDING_IMPACT_SPEED" ->
                sendAndShowResult(CommandType.PRINT_FIELD_DESCENDING_IMPACT_SPEED, null);
            case "REMOVE_GREATER" -> {
                HumanBeingDialog dlg = new HumanBeingDialog(this, false, null);
                dlg.setVisible(true);
                if (dlg.isConfirmed())
                    sendCommandAndRefresh(CommandType.REMOVE_GREATER, null, dlg.getResult());
            }
            case "EXECUTE_SCRIPT" -> {
                // Выбор файла через JFileChooser
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Выберите файл скрипта");
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "Текстовые файлы (*.txt)", "txt"));
                fc.setAcceptAllFileFilterUsed(true);
                int res = fc.showOpenDialog(this);
                if (res != JFileChooser.APPROVE_OPTION) return;
                java.io.File file = fc.getSelectedFile();
                if (file == null || !file.exists()) {
                    showInfo("Файл не найден: " + (file != null ? file.getPath() : ""));
                    return;
                }
                if (!file.canRead()) {
                    showInfo("Нет прав на чтение файла: " + file.getPath());
                    return;
                }
                sendAndShowResult(CommandType.EXECUTE_SCRIPT, file.getAbsolutePath());
            }
        }
    }

    private void sendAndShowResult(CommandType type, String arg) {
        if (!startOperation()) return;
        new Thread(() -> {
            Response r = network.sendCommand(type, arg, null);
            SwingUtilities.invokeLater(() -> {
                endOperation();
                if (r == null) { showInfo(LocaleManager.get("err.server_null")); return; }
                StringBuilder sb = new StringBuilder();
                if (r.getMessage() != null) sb.append(r.getMessage());
                if (r.getData() instanceof Collection<?>) {
                    for (Object o : (Collection<?>) r.getData())
                        sb.append('\n').append(o);
                }
                String text = sb.toString().trim();
                if (text.isEmpty()) text = "OK";

                JTextArea ta = new JTextArea(text);
                ta.setEditable(false);
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.setBackground(GuiTheme.BG_CARD);
                ta.setForeground(GuiTheme.TEXT_PRIMARY);
                ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane sp = new JScrollPane(ta);
                sp.setPreferredSize(new Dimension(480, 280));
                JOptionPane.showMessageDialog(this, sp, type.name(), JOptionPane.INFORMATION_MESSAGE);
            });
        }, "cmd-thread").start();
    }

    private void sendCommandAndRefresh(CommandType type, java.io.Serializable arg, HumanBeing human) {
        if (!startOperation()) return;
        setStatus(LocaleManager.get("main.status.loading"), GuiTheme.TEXT_MUTED);
        new Thread(() -> {
            Response r = network.sendCommand(type, arg, human);
            SwingUtilities.invokeLater(() -> {
                endOperation();
                if (r != null && r.getMessage() != null && !r.getMessage().isBlank()) {
                    // Показываем только если это не стандартное "OK" (оно не несёт смысла)
                    String msg = r.getMessage().trim();
                    if (!msg.equalsIgnoreCase("ok") && !msg.equalsIgnoreCase("success")) {
                        showInfo(msg);
                    }
                }
                if (r == null) {
                    setStatus(LocaleManager.get("err.server_null"), GuiTheme.DANGER);
                } else {
                    setStatus(LocaleManager.get("main.status.ready"), GuiTheme.TEXT_MUTED);
                }
                doRefresh();
            });
        }, "cmd-refresh-thread").start();
    }

    private void showResponseText(Response r, String title) {
        if (r == null) { showInfo(LocaleManager.get("err.server_null")); return; }
        String text = r.getMessage() != null ? r.getMessage() : "";
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBackground(GuiTheme.BG_CARD);
        ta.setForeground(GuiTheme.TEXT_PRIMARY);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, sp, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Операция в процессе
    private boolean startOperation() {
        if (!operationInProgress.compareAndSet(false, true)) return false;
        setButtonsEnabled(false);
        return true;
    }

    private void endOperation() {
        operationInProgress.set(false);
        setButtonsEnabled(true);
    }

    // Logout / Exit
    private void doLogout() {
        int r = JOptionPane.showConfirmDialog(this,
                LocaleManager.get("main.btn.logout") + "?",
                LocaleManager.get("msg.confirm.title"),
                JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        cleanup();
        network.logout();
        dispose();
        AuthFrame auth = new AuthFrame(network);
        auth.setVisible(true);
    }

    private void confirmExit() {
        int r = JOptionPane.showConfirmDialog(this,
                LocaleManager.get("main.btn.exit") + "?",
                LocaleManager.get("msg.confirm.title"),
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            cleanup();
            System.exit(0);
        }
    }

    // Освобождает ресурсы перед закрытием окна
    private void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.stop();
            refreshTimer = null;
        }
        LocaleManager.removeListener(localeListener);
        visualPanel.stopAnimation();
    }

    // Helpers
    private void setStatus(String msg, Color color) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
            statusLabel.setForeground(color);
        }
    }

    private void showInfo(String msg) {
        if (msg == null || msg.isBlank()) return;
        JOptionPane.showMessageDialog(this, msg,
                LocaleManager.get("app.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateTexts() {
        setTitle(LocaleManager.get("app.title"));
        if (userLabel     != null) userLabel.setText(LocaleManager.get("main.user.label"));
        if (langLabel     != null) langLabel.setText(LocaleManager.get("main.lang.label"));
        if (statusLabel   != null) statusLabel.setText(LocaleManager.get("main.status.ready"));

        if (tabbedPane != null) {
            tabbedPane.setTitleAt(0, LocaleManager.get("main.tab.table"));
            tabbedPane.setTitleAt(1, LocaleManager.get("main.tab.visual"));
        }

        updateSideBtn(addBtn, "main.btn.add");
        updateSideBtn(editBtn, "main.btn.edit");
        updateSideBtn(deleteBtn, "main.btn.delete");
        updateSideBtn(refreshBtn, "main.btn.refresh");
        updateSideBtn(clearBtn, "main.btn.clear");
        updateSideBtn(sortBtn, "main.btn.sort");
        updateSideBtn(reorderBtn, "main.btn.reorder");
        updateSideBtn(infoBtn, "main.btn.info");
        updateSideBtn(helpBtn, "main.btn.help");
        updateSideBtn(commandsBtn, "cmd.dialog.title");
        updateSideBtn(logoutBtn, "main.btn.logout");
        updateSideBtn(exitBtn, "main.btn.exit");

        if (tablePanel != null) tablePanel.updateTexts();
        repaint();
    }

    private void updateSideBtn(JButton btn, String key) {
        if (btn != null) btn.setText(LocaleManager.get(key));
    }
}
