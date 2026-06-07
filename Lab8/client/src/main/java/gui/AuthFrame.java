package gui;

import common.Response;
import gui.utils.GuiTheme;
import managers.LocaleManager;
import managers.NetworkManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;


public class AuthFrame extends JFrame {

    private final NetworkManager network;

    private JLabel titleLabel;
    private JLabel loginLabel;
    private JLabel passLabel;
    private JTextField loginField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JButton registerBtn;
    private JLabel statusLabel;
    private JComboBox<String> langCombo;

    private float logoAlpha = 0f;
    private Timer fadeTimer;

    // Флаг идёт ли запрос к серверу
    private boolean requestInProgress = false;

    public AuthFrame(NetworkManager network) {
        this.network = network;
        initUI();
        startFadeAnimation();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(420, 530);
        setLocationRelativeTo(null);
        try {
            setShape(new RoundRectangle2D.Double(0, 0, 420, 530, 20, 20));
        } catch (UnsupportedOperationException ignored) {
            // Не все платформы поддерживают setShape — просто работаем без скруглений
        }

        JPanel root = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, GuiTheme.BG_DARK,
                        0, getHeight(), new Color(22, 22, 48));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(20, 40, 24, 40));

        // Перетаскивание окна
        MouseAdapter drag = new MouseAdapter() {
            Point p;
            public void mousePressed(MouseEvent e)  { p = e.getLocationOnScreen(); }
            public void mouseDragged(MouseEvent e)  {
                if (p == null) return;
                Point now = e.getLocationOnScreen();
                setLocation(getX() + now.x - p.x, getY() + now.y - p.y);
                p = now;
            }
        };
        root.addMouseListener(drag);
        root.addMouseMotionListener(drag);

        // Верхняя панель: язык + закрытие
        JPanel topBar = buildTopBar();
        topBar.setAlignmentX(CENTER_ALIGNMENT);

        // Логотип
        JPanel logoPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        Math.max(0f, Math.min(1f, logoAlpha))));
                drawLogo(g2, getWidth() / 2, 45);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(100, 90); }
            @Override public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE, 90); }
        };
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(CENTER_ALIGNMENT);

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(GuiTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Форма
        JPanel form = buildForm();
        form.setAlignmentX(CENTER_ALIGNMENT);

        // Кнопки
        loginBtn = GuiTheme.createAccentButton("");
        registerBtn = GuiTheme.createAccentButton("");
        registerBtn.setForeground(GuiTheme.TEXT_MUTED);
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        registerBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Статус
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(GuiTheme.DANGER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(12f));
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        root.add(topBar);
        root.add(Box.createVerticalStrut(8));
        root.add(logoPanel);
        root.add(Box.createVerticalStrut(4));
        root.add(titleLabel);
        root.add(Box.createVerticalStrut(20));
        root.add(form);
        root.add(Box.createVerticalStrut(16));
        root.add(loginBtn);
        root.add(Box.createVerticalStrut(8));
        root.add(registerBtn);
        root.add(Box.createVerticalStrut(10));
        root.add(statusLabel);

        setContentPane(root);
        updateTexts();
        bindActions();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Переключатель языка
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        langPanel.setOpaque(false);
        JLabel langLbl = new JLabel("🌐");
        langLbl.setForeground(GuiTheme.TEXT_MUTED);
        langLbl.setFont(langLbl.getFont().deriveFont(12f));

        String[] localeNames = LocaleManager.SUPPORTED_LOCALES.stream()
                .map(LocaleManager::getLocaleName).toArray(String[]::new);
        langCombo = new JComboBox<>(localeNames);
        langCombo.setPreferredSize(new Dimension(120, 24));
        langCombo.setFont(langCombo.getFont().deriveFont(11f));
        langCombo.setBackground(GuiTheme.BG_CARD);
        langCombo.setForeground(GuiTheme.TEXT_PRIMARY);

        int locIdx = LocaleManager.SUPPORTED_LOCALES.indexOf(LocaleManager.getCurrentLocale());
        langCombo.setSelectedIndex(Math.max(0, locIdx));
        langCombo.addActionListener(e -> {
            int idx = langCombo.getSelectedIndex();
            if (idx >= 0 && idx < LocaleManager.SUPPORTED_LOCALES.size()) {
                LocaleManager.setLocale(LocaleManager.SUPPORTED_LOCALES.get(idx));
                updateTexts();
            }
        });
        langPanel.add(langLbl);
        langPanel.add(langCombo);

        // Кнопка закрытия
        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(GuiTheme.TEXT_MUTED);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setFont(closeBtn.getFont().deriveFont(13f));
        closeBtn.addActionListener(e -> System.exit(0));

        bar.add(langPanel, BorderLayout.WEST);
        bar.add(closeBtn,  BorderLayout.EAST);
        return bar;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets  = new Insets(5, 0, 5, 0);

        loginLabel = new JLabel();
        loginLabel.setForeground(GuiTheme.TEXT_MUTED);
        loginLabel.setFont(loginLabel.getFont().deriveFont(Font.BOLD, 12f));

        loginField = new JTextField(20);
        styleField(loginField);
        loginField.setToolTipText("Логин (от 1 до 64 символов)");

        passLabel = new JLabel();
        passLabel.setForeground(GuiTheme.TEXT_MUTED);
        passLabel.setFont(passLabel.getFont().deriveFont(Font.BOLD, 12f));

        passField = new JPasswordField(20);
        styleField(passField);
        passField.setToolTipText("Пароль (минимум 1 символ)");

        c.gridy = 0; form.add(loginLabel, c);
        c.gridy = 1; form.add(loginField, c);
        c.gridy = 2; form.add(passLabel,  c);
        c.gridy = 3; form.add(passField,  c);
        return form;
    }

    private void styleField(JTextField f) {
        f.setBackground(GuiTheme.BG_CARD);
        f.setForeground(GuiTheme.TEXT_PRIMARY);
        f.setCaretColor(GuiTheme.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GuiTheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        f.setPreferredSize(new Dimension(0, 36));
    }

    private void bindActions() {
        loginBtn   .addActionListener(e -> doAuth(false));
        registerBtn.addActionListener(e -> doAuth(true));

        // Enter в любом поле = логин
        KeyAdapter enterKey = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doAuth(false);
            }
        };
        loginField.addKeyListener(enterKey);
        passField .addKeyListener(enterKey);

        // Tab: login → pass → loginBtn
        loginField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) { passField.requestFocus(); e.consume(); }
            }
        });
    }

    // Авторизация
    private void doAuth(boolean isRegister) {
        if (requestInProgress) return;

        String login = loginField.getText().trim();
        String pass = new String(passField.getPassword());   // не trim: пароль может содержать пробелы

        // Валидация
        if (login.isEmpty()) {
            setStatus(LocaleManager.get("auth.login") + ": "
                    + LocaleManager.get("err.empty"), GuiTheme.DANGER);
            loginField.requestFocus();
            return;
        }
        if (login.length() > 64) {
            setStatus(LocaleManager.get("auth.login") + ": "
                    + LocaleManager.get("err.too_long"), GuiTheme.DANGER);
            loginField.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            setStatus(LocaleManager.get("auth.password") + ": "
                    + LocaleManager.get("err.empty"), GuiTheme.DANGER);
            passField.requestFocus();
            return;
        }

        // Блокируем UI
        requestInProgress = true;
        loginBtn   .setEnabled(false);
        registerBtn.setEnabled(false);
        langCombo  .setEnabled(false);
        setStatus(LocaleManager.get("main.status.loading"), GuiTheme.TEXT_MUTED);

        final String finalLogin = login;
        final String finalPass  = pass;

        new Thread(() -> {
            Response resp;
            try {
                resp = isRegister
                        ? network.register(finalLogin, finalPass)
                        : network.login(finalLogin, finalPass);
            } catch (Exception ex) {
                resp = new Response(LocaleManager.get("auth.error.conn") + ": " + ex.getMessage(), null);
            }

            final Response finalResp = resp;
            SwingUtilities.invokeLater(() -> {
                requestInProgress = false;
                loginBtn   .setEnabled(true);
                registerBtn.setEnabled(true);
                langCombo  .setEnabled(true);

                if (finalResp == null) {
                    setStatus(LocaleManager.get("auth.error.conn"), GuiTheme.DANGER);
                    return;
                }
                if (network.isLoggedIn()) {
                    openMainWindow();
                } else {
                    String msg = finalResp.getMessage();
                    setStatus(msg != null && !msg.isBlank() ? msg
                            : LocaleManager.get("auth.error.conn"), GuiTheme.DANGER);
                    // Снова показываем пароль пустым (безопасность)
                    passField.setText("");
                    passField.requestFocus();
                }
            });
        }, "auth-thread").start();
    }

    private void openMainWindow() {
        dispose();
        MainFrame main = new MainFrame(network);
        main.setVisible(true);
    }

    private void setStatus(String msg, Color color) {
        if (statusLabel == null) return;
        statusLabel.setText(msg != null && !msg.isBlank() ? msg : " ");
        statusLabel.setForeground(color);
    }

    public void updateTexts() {
        setTitle(LocaleManager.get("app.title"));
        if (titleLabel != null) titleLabel.setText(LocaleManager.get("auth.title"));
        if (loginLabel != null) loginLabel.setText(LocaleManager.get("auth.login"));
        if (passLabel != null) passLabel.setText(LocaleManager.get("auth.password"));
        if (loginBtn != null) loginBtn.setText(LocaleManager.get("auth.btn.login"));
        if (registerBtn != null) registerBtn.setText(LocaleManager.get("auth.btn.register"));
    }

    // Анимация логотипа
    private void startFadeAnimation() {
        fadeTimer = new Timer(25, e -> {
            logoAlpha += 0.04f;
            repaint();
            if (logoAlpha >= 1f) {
                logoAlpha = 1f;
                ((Timer) e.getSource()).stop();
            }
        });
        fadeTimer.start();
    }

    private void drawLogo(Graphics2D g2, int cx, int cy) {
        // Голова
        g2.setColor(GuiTheme.ACCENT);
        g2.fillOval(cx - 16, cy - 18, 32, 32);

        // Тело
        g2.setColor(new Color(79, 82, 221));
        g2.fillRoundRect(cx - 18, cy + 16, 36, 28, 8, 8);

        // Ноги
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(GuiTheme.ACCENT);
        g2.drawLine(cx - 6, cy + 44, cx - 9, cy + 56);
        g2.drawLine(cx + 6, cy + 44, cx + 9, cy + 56);

        // Руки
        g2.drawLine(cx - 18, cy + 22, cx - 28, cy + 32);
        g2.drawLine(cx + 18, cy + 22, cx + 28, cy + 32);

        // Глаза
        g2.setColor(GuiTheme.BG_DARK);
        g2.fillOval(cx - 9, cy - 11, 6, 6);
        g2.fillOval(cx + 3, cy - 11, 6, 6);

        // Улыбка
        g2.setStroke(new BasicStroke(2f));
        g2.drawArc(cx - 7, cy - 1, 14, 8, 0, -180);
    }
}
