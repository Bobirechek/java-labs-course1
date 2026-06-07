package gui.dialogs;

import gui.utils.GuiTheme;
import managers.LocaleManager;
import models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;


public class HumanBeingDialog extends JDialog {

    private boolean confirmed = false;
    private HumanBeing result;

    private JTextField nameField;
    private JTextField xField;
    private JTextField yField;
    private JCheckBox realHeroBox;
    private JComboBox<String> toothpickCombo;
    private JTextField speedField;
    private JTextField soundtrackField;
    private JComboBox<String> weaponCombo;
    private JComboBox<String> moodCombo;
    private JTextField carNameField;
    private JCheckBox carCoolBox;

    public HumanBeingDialog(Frame parent, boolean isEdit, HumanBeing existing) {
        super(parent, "", true);
        initUI(isEdit, existing);
    }

    private void initUI(boolean isEdit, HumanBeing existing) {
        setTitle(LocaleManager.get(isEdit ? "dlg.edit.title" : "dlg.add.title"));
        setSize(480, 620);
        setLocationRelativeTo(getParent());
        setResizable(true);
        setMinimumSize(new Dimension(400, 500));

        // Закрытие по ESC
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(GuiTheme.BG_PANEL);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel titleLbl = new JLabel(getTitle(), SwingConstants.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLbl.setForeground(GuiTheme.TEXT_PRIMARY);
        titleLbl.setBorder(new EmptyBorder(0, 0, 16, 0));

        // ── Form ──
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GuiTheme.BG_PANEL);
        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets  = new Insets(3, 0, 3, 0);

        nameField = styledField();
        xField = styledField();
        yField = styledField();
        realHeroBox = styledCheck();
        speedField = styledField();
        soundtrackField = styledField();
        carNameField = styledField();
        carCoolBox = styledCheck();

        toothpickCombo = styledCombo(new String[]{"—", "true", "false"});
        weaponCombo = buildEnumCombo(WeaponType.values());
        moodCombo = buildEnumCombo(Mood.values());

        // Placeholder
        setPlaceholder(xField, "например: 100");
        setPlaceholder(yField, "например: 200");
        setPlaceholder(speedField, "0.0 .. 597.0");

        int row = 0;
        row = addRow(form, c, row, LocaleManager.get("dlg.field.name"), nameField);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.x"), xField);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.y"), yField);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.realHero"), realHeroBox);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.toothpick"), toothpickCombo);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.speed"), speedField);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.soundtrack"), soundtrackField);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.weapon"), weaponCombo);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.mood"), moodCombo);
        row = addRow(form, c, row, LocaleManager.get("dlg.field.carName"), carNameField);
              addRow(form, c, row, LocaleManager.get("dlg.field.carCool"), carCoolBox);

        if (existing != null) fillFrom(existing);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBackground(GuiTheme.BG_PANEL);
        scroll.getViewport().setBackground(GuiTheme.BG_PANEL);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        // Buttons
        JButton saveBtn   = GuiTheme.createAccentButton(LocaleManager.get("dlg.btn.save"));
        JButton cancelBtn = GuiTheme.createAccentButton(LocaleManager.get("dlg.btn.cancel"));
        cancelBtn.setForeground(GuiTheme.TEXT_MUTED);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(GuiTheme.BG_PANEL);
        btnPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        saveBtn  .addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        // Enter = сохранить
        getRootPane().setDefaultButton(saveBtn);

        root.add(titleLbl, BorderLayout.NORTH);
        root.add(scroll,   BorderLayout.CENTER);
        root.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(root);
    }

    // Валидация и сохранение
    private void onSave() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError(LocaleManager.get("dlg.field.name") + ": " + LocaleManager.get("err.empty"));
                nameField.requestFocus();
                return;
            }
            if (name.length() > 200) {
                showError(LocaleManager.get("dlg.field.name") + ": " + LocaleManager.get("err.too_long"));
                nameField.requestFocus();
                return;
            }

            long x;
            try {
                String xStr = xField.getText().trim();
                if (xStr.isEmpty()) throw new NumberFormatException();
                x = Long.parseLong(xStr);
            } catch (NumberFormatException ex) {
                showError(LocaleManager.get("dlg.field.x") + ": " + LocaleManager.get("err.long"));
                xField.requestFocus();
                return;
            }

            long y;
            try {
                String yStr = yField.getText().trim();
                if (yStr.isEmpty()) throw new NumberFormatException();
                y = Long.parseLong(yStr);
            } catch (NumberFormatException ex) {
                showError(LocaleManager.get("dlg.field.y") + ": " + LocaleManager.get("err.long"));
                yField.requestFocus();
                return;
            }

            double speed;
            try {
                // Принимаем и точку, и запятую (для разных локалей)
                String sStr = speedField.getText().trim().replace(',', '.');
                if (sStr.isEmpty()) throw new NumberFormatException();
                speed = Double.parseDouble(sStr);
                if (Double.isNaN(speed) || Double.isInfinite(speed)) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError(LocaleManager.get("dlg.field.speed") + ": " + LocaleManager.get("err.double"));
                speedField.requestFocus();
                return;
            }
            if (speed > 597) {
                showError(LocaleManager.get("dlg.field.speed") + ": " + LocaleManager.get("err.speed_max"));
                speedField.requestFocus();
                return;
            }
            if (speed < 0) {
                showError(LocaleManager.get("dlg.field.speed") + ": " + LocaleManager.get("err.speed_neg"));
                speedField.requestFocus();
                return;
            }

            String soundtrack = soundtrackField.getText().trim();
            if (soundtrack.isEmpty()) {
                showError(LocaleManager.get("dlg.field.soundtrack") + ": " + LocaleManager.get("err.empty"));
                soundtrackField.requestFocus();
                return;
            }

            Boolean hasToothpick = null;
            String tp = (String) toothpickCombo.getSelectedItem();
            if ("true".equals(tp))  hasToothpick = true;
            if ("false".equals(tp)) hasToothpick = false;

            WeaponType weapon = null;
            int wi = weaponCombo.getSelectedIndex();
            if (wi > 0 && wi <= WeaponType.values().length) weapon = WeaponType.values()[wi - 1];

            Mood mood = null;
            int mi = moodCombo.getSelectedIndex();
            if (mi > 0 && mi <= Mood.values().length) mood = Mood.values()[mi - 1];

            Car car = null;
            String carName = carNameField.getText().trim();
            if (!carName.isEmpty()) {
                car = new Car(carName, carCoolBox.isSelected());
            }

            result = new HumanBeing.Builder()
                    .name(name)
                    .coordinates(new Coordinates(x, y))
                    .creationDate(LocalDateTime.now())
                    .realHero(realHeroBox.isSelected())
                    .hasToothpick(hasToothpick)
                    .impactSpeed(speed)
                    .soundtrackName(soundtrack)
                    .weaponType(weapon)
                    .mood(mood)
                    .car(car)
                    .build();

            confirmed = true;
            dispose();

        } catch (Exception ex) {
            showError(LocaleManager.get("err.unexpected") + ": " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                LocaleManager.get("err.validation_title"), JOptionPane.WARNING_MESSAGE);
    }

    // Заполнение из существующего объекта
    private void fillFrom(HumanBeing h) {
        if (h.getName() != null)         nameField.setText(h.getName());
        if (h.getCoordinates() != null) {
            xField.setText(String.valueOf(h.getCoordinates().getX()));
            yField.setText(String.valueOf(h.getCoordinates().getY()));
        }
        realHeroBox.setSelected(h.isRealHero());

        if (h.getHasToothpick() == null) toothpickCombo.setSelectedIndex(0);
        else if (h.getHasToothpick()) toothpickCombo.setSelectedIndex(1);
        else toothpickCombo.setSelectedIndex(2);

        speedField.setText(String.valueOf(h.getImpactSpeed()));
        if (h.getSoundtrackName() != null) soundtrackField.setText(h.getSoundtrackName());

        if (h.getWeaponType() != null) {
            for (int i = 0; i < WeaponType.values().length; i++) {
                if (WeaponType.values()[i] == h.getWeaponType()) {
                    weaponCombo.setSelectedIndex(i + 1);
                    break;
                }
            }
        }
        if (h.getMood() != null) {
            for (int i = 0; i < Mood.values().length; i++) {
                if (Mood.values()[i] == h.getMood()) {
                    moodCombo.setSelectedIndex(i + 1);
                    break;
                }
            }
        }
        if (h.getCar() != null && h.getCar().getName() != null) {
            carNameField.setText(h.getCar().getName());
            if (h.getCar().getCool() != null) carCoolBox.setSelected(h.getCar().getCool());
        }
    }

    public boolean isConfirmed()   { return confirmed; }
    public HumanBeing getResult()  { return result; }

    // UI helpers
    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setBackground(GuiTheme.BG_CARD);
        f.setForeground(GuiTheme.TEXT_PRIMARY);
        f.setCaretColor(GuiTheme.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GuiTheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        f.setPreferredSize(new Dimension(0, 32));
        return f;
    }

    private JCheckBox styledCheck() {
        JCheckBox cb = new JCheckBox();
        cb.setBackground(GuiTheme.BG_PANEL);
        cb.setForeground(GuiTheme.TEXT_PRIMARY);
        return cb;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(GuiTheme.BG_CARD);
        cb.setForeground(GuiTheme.TEXT_PRIMARY);
        return cb;
    }

    private JComboBox<String> buildEnumCombo(Object[] vals) {
        String[] items = new String[vals.length + 1];
        items[0] = "—";
        for (int i = 0; i < vals.length; i++) items[i + 1] = vals[i].toString();
        return styledCombo(items);
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setToolTipText(placeholder);
    }

    private int addRow(JPanel p, GridBagConstraints c, int row, String lbl, JComponent comp) {
        c.gridy = row;
        c.gridx = 0;
        JLabel label = new JLabel(lbl);
        label.setForeground(GuiTheme.TEXT_MUTED);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 11f));
        p.add(label, c);
        c.gridy = row + 1;
        p.add(comp, c);
        return row + 2;
    }
}
