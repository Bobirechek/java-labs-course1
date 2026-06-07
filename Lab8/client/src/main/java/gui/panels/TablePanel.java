package gui.panels;

import gui.utils.GuiTheme;
import managers.LocaleManager;
import models.HumanBeing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TablePanel extends JPanel {

    private static final String[] COL_KEYS = {
        "col.id", "col.name", "col.x", "col.y",
        "col.created","col.realHero","col.toothpick", "col.speed",
        "col.soundtrack","col.weapon","col.mood","col.car","col.owner"
    };

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> sorter;

    private JTextField filterField;
    private JComboBox<String> filterColCombo;
    // Сохраняем как поля, чтобы обновлять при смене языка
    private JComboBox<String> sortColCombo;
    private JLabel filterLabel;
    private JLabel colLabel;
    private JLabel sortLbl;

    private List<HumanBeing> allData = new ArrayList<>();
    private List<HumanBeing> streamSortedData = null;
    private int lastSortColIdx = -1;
    private boolean lastSortAsc = true;

    public TablePanel() {
        setLayout(new BorderLayout(0, 6));
        setBackground(GuiTheme.BG_PANEL);
        setBorder(new EmptyBorder(8, 8, 8, 8));

        // Панель фильтра и сортировки
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        filterBar.setBackground(GuiTheme.BG_PANEL);

        filterLabel = new JLabel();
        filterLabel.setForeground(GuiTheme.TEXT_MUTED);
        filterLabel.setFont(filterLabel.getFont().deriveFont(12f));

        colLabel = new JLabel();
        colLabel.setForeground(GuiTheme.TEXT_MUTED);
        colLabel.setFont(colLabel.getFont().deriveFont(12f));

        filterField = new JTextField(18);
        filterField.setBackground(GuiTheme.BG_CARD);
        filterField.setForeground(GuiTheme.TEXT_PRIMARY);
        filterField.setCaretColor(GuiTheme.TEXT_PRIMARY);
        filterField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GuiTheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        filterField.setPreferredSize(new Dimension(160, 28));

        filterColCombo = new JComboBox<>(buildColNames());
        filterColCombo.setBackground(GuiTheme.BG_CARD);
        filterColCombo.setForeground(GuiTheme.TEXT_PRIMARY);
        filterColCombo.setPreferredSize(new Dimension(130, 28));

        JButton clearFilterBtn = new JButton("x");
        clearFilterBtn.setToolTipText("Сбросить фильтр");
        clearFilterBtn.setContentAreaFilled(false);
        clearFilterBtn.setBorderPainted(false);
        clearFilterBtn.setForeground(GuiTheme.TEXT_MUTED);
        clearFilterBtn.setFont(clearFilterBtn.getFont().deriveFont(11f));
        clearFilterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearFilterBtn.addActionListener(e -> filterField.setText(""));

        // Разделитель
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 22));
        sep.setForeground(GuiTheme.BORDER_COLOR);

        // Сортировка через Streams API
        sortLbl = new JLabel();
        sortLbl.setForeground(GuiTheme.TEXT_MUTED);
        sortLbl.setFont(sortLbl.getFont().deriveFont(12f));

        sortColCombo = new JComboBox<>(buildColNames());
        sortColCombo.setBackground(GuiTheme.BG_CARD);
        sortColCombo.setForeground(GuiTheme.TEXT_PRIMARY);
        sortColCombo.setPreferredSize(new Dimension(130, 28));

        JButton sortAscBtn  = new JButton("Asc");
        JButton sortDescBtn = new JButton("Desc");
        for (JButton b : new JButton[]{sortAscBtn, sortDescBtn}) {
            b.setBackground(GuiTheme.BG_CARD);
            b.setForeground(GuiTheme.TEXT_PRIMARY);
            b.setBorderPainted(true);
            b.setFocusPainted(false);
            b.setContentAreaFilled(true);
            b.setMargin(new Insets(2, 8, 2, 8));
            b.setFont(b.getFont().deriveFont(Font.BOLD, 11f));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        JButton resetSortBtn = new JButton("x");
        resetSortBtn.setToolTipText("Сбросить сортировку");
        resetSortBtn.setContentAreaFilled(false);
        resetSortBtn.setBorderPainted(false);
        resetSortBtn.setForeground(GuiTheme.TEXT_MUTED);
        resetSortBtn.setFont(resetSortBtn.getFont().deriveFont(11f));
        resetSortBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetSortBtn.addActionListener(e -> {
            streamSortedData = null;
            lastSortColIdx = -1;
            lastSortAsc = true;
            applyFilter();
        });

        // Привязываем сортировку — Streams API
        sortAscBtn.addActionListener(e -> applyStreamSort(sortColCombo.getSelectedIndex(), true));
        sortDescBtn.addActionListener(e -> applyStreamSort(sortColCombo.getSelectedIndex(), false));

        filterBar.add(filterLabel);
        filterBar.add(filterField);
        filterBar.add(clearFilterBtn);
        filterBar.add(colLabel);
        filterBar.add(filterColCombo);
        filterBar.add(sep);
        filterBar.add(sortLbl);
        filterBar.add(sortColCombo);
        filterBar.add(sortAscBtn);
        filterBar.add(sortDescBtn);
        filterBar.add(resetSortBtn);

        // Таблица
        tableModel = new DefaultTableModel(buildColNames(), 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                if (c == 0) return Long.class;
                if (c == 2 || c == 3) return Long.class;
                if (c == 7) return Double.class;
                if (c == 5) return Boolean.class;
                return String.class;
            }
        };

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component comp = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? GuiTheme.BG_PANEL : GuiTheme.TABLE_STRIPE);
                    comp.setForeground(GuiTheme.TEXT_PRIMARY);
                } else {
                    comp.setBackground(GuiTheme.TABLE_SEL);
                    comp.setForeground(Color.WHITE);
                }
                return comp;
            }
        };
        table.setBackground(GuiTheme.BG_PANEL);
        table.setForeground(GuiTheme.TEXT_PRIMARY);
        table.setGridColor(GuiTheme.BORDER_COLOR);
        table.setRowHeight(28);
        table.setSelectionBackground(GuiTheme.TABLE_SEL);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(table.getFont().deriveFont(12f));
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setBackground(GuiTheme.BG_CARD);
        header.setForeground(GuiTheme.TEXT_PRIMARY);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 12f));
        header.setReorderingAllowed(true);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Цвет владельца в колонке Owner (12)
        table.getColumnModel().getColumn(12).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel && val != null && !val.toString().isEmpty()) {
                    lbl.setForeground(GuiTheme.getUserColor(val.toString()));
                }
                return lbl;
            }
        });

        int[] widths = {55, 110, 60, 60, 130, 90, 90, 110, 150, 80, 80, 100, 100};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(GuiTheme.BG_PANEL);
        scroll.getViewport().setBackground(GuiTheme.BG_PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(GuiTheme.BORDER_COLOR));

        add(filterBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Слушатель фильтра
        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        };
        filterField.getDocument().addDocumentListener(dl);
        filterColCombo.addActionListener(e -> applyFilter());

        LocaleManager.addListener(this::updateTexts);
        updateTexts();
    }

    // Streams-сортировка
    private void applyStreamSort(int colIdx, boolean ascending) {
        Comparator<HumanBeing> cmp = getComparatorForColumn(colIdx);
        if (cmp == null) return;
        if (!ascending) cmp = cmp.reversed();
        // Запоминаем параметры сортировки
        lastSortColIdx = colIdx;
        lastSortAsc = ascending;
        streamSortedData = allData.stream()
                .sorted(cmp)
                .collect(Collectors.toList());
        applyFilter();
    }

    private Comparator<HumanBeing> getComparatorForColumn(int col) {
        switch (col) {
            case 0:  return Comparator.comparingLong(
                         (HumanBeing h) -> h.getId() != null ? h.getId() : 0L);
            case 1:  return Comparator.comparing(
                         (HumanBeing h) -> h.getName() != null ? h.getName() : "");
            case 2:  return Comparator.comparingLong(
                         (HumanBeing h) -> h.getCoordinates() != null ? h.getCoordinates().getX() : 0L);
            case 3:  return Comparator.comparingLong(
                         (HumanBeing h) -> h.getCoordinates() != null ? h.getCoordinates().getY() : 0L);
            case 4:  return Comparator.comparing(
                         (HumanBeing h) -> h.getCreationDate() != null ? h.getCreationDate().toString() : "");
            case 5:  return Comparator.comparingInt(
                         (HumanBeing h) -> h.isRealHero() ? 1 : 0);
            case 7:  return Comparator.comparingDouble(HumanBeing::getImpactSpeed);
            case 8:  return Comparator.comparing(
                         (HumanBeing h) -> h.getSoundtrackName() != null ? h.getSoundtrackName() : "");
            case 11: return Comparator.comparing(
                         (HumanBeing h) -> h.getCar() != null && h.getCar().getName() != null
                                 ? h.getCar().getName() : "");
            case 12: return Comparator.comparing(
                         (HumanBeing h) -> h.getOwnerLogin() != null ? h.getOwnerLogin() : "");
            default: return null;
        }
    }

    // Данные
    public void setData(List<HumanBeing> humans) {
        if (humans == null) humans = new ArrayList<>();
        this.allData = new ArrayList<>(humans);
        // Если была активная сортировка — применяем её снова к новым данным
        if (lastSortColIdx >= 0) {
            Comparator<HumanBeing> cmp = getComparatorForColumn(lastSortColIdx);
            if (cmp != null) {
                if (!lastSortAsc) cmp = cmp.reversed();
                streamSortedData = allData.stream()
                        .sorted(cmp)
                        .collect(Collectors.toList());
            } else {
                streamSortedData = null;
            }
        } else {
            streamSortedData = null;
        }
        applyFilter();
    }

    private void applyFilter() {
        String text   = filterField.getText().trim().toLowerCase();
        int    colIdx = filterColCombo.getSelectedIndex();

        // Если есть stream-сортировка — берём отсортированный список как базу
        List<HumanBeing> base = (streamSortedData != null) ? streamSortedData : allData;

        List<HumanBeing> filtered;
        if (text.isEmpty()) {
            filtered = base;
        } else {
            final int ci = colIdx;
            filtered = base.stream()
                    .filter(h -> {
                        if (h == null) return false;
                        Object[] row = toRow(h);
                        if (ci < 0 || ci >= row.length) {
                            return Arrays.stream(row)
                                    .anyMatch(v -> v != null
                                            && v.toString().toLowerCase().contains(text));
                        }
                        Object val = row[ci];
                        return val != null && val.toString().toLowerCase().contains(text);
                    })
                    .collect(Collectors.toList());
        }
        refreshTable(filtered);
    }

    private void refreshTable(List<HumanBeing> data) {
        long selectedId = getSelectedId();
        tableModel.setRowCount(0);
        for (HumanBeing h : data) {
            if (h != null) tableModel.addRow(toRow(h));
        }
        if (selectedId >= 0) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Object v = tableModel.getValueAt(i, 0);
                if (v instanceof Long && (Long) v == selectedId) {
                    try {
                        int vi = table.convertRowIndexToView(i);
                        table.setRowSelectionInterval(vi, vi);
                        table.scrollRectToVisible(table.getCellRect(vi, 0, true));
                    } catch (Exception ignored) {}
                    break;
                }
            }
        }
    }

    private Object[] toRow(HumanBeing h) {
        return new Object[]{
            h.getId(),
            h.getName(),
            h.getCoordinates() != null ? h.getCoordinates().getX() : 0L,
            h.getCoordinates() != null ? h.getCoordinates().getY() : 0L,
            LocaleManager.formatDate(h.getCreationDate()),
            h.isRealHero(),
            h.getHasToothpick(),
            h.getImpactSpeed(),
            h.getSoundtrackName(),
            h.getWeaponType(),
            h.getMood(),
            h.getCar() != null ? h.getCar().getName() : "",
            h.getOwnerLogin() != null ? h.getOwnerLogin() : ""
        };
    }

    public HumanBeing getSelectedHuman() {
        int vi = table.getSelectedRow();
        if (vi < 0) return null;
        try {
            int mi = table.convertRowIndexToModel(vi);
            Object idVal = tableModel.getValueAt(mi, 0);
            if (!(idVal instanceof Long)) return null;
            long id = (Long) idVal;
            return allData.stream()
                    .filter(h -> h != null && h.getId() != null && h.getId() == id)
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private long getSelectedId() {
        int vi = table.getSelectedRow();
        if (vi < 0) return -1;
        try {
            int mi = table.convertRowIndexToModel(vi);
            Object v = tableModel.getValueAt(mi, 0);
            if (v instanceof Long) return (Long) v;
        } catch (Exception ignored) {}
        return -1;
    }

    // Локализация
    public void updateTexts() {
        String[] names = buildColNames();
        tableModel.setColumnIdentifiers(names);

        int prevFilter = filterColCombo.getSelectedIndex();
        filterColCombo.setModel(new DefaultComboBoxModel<>(names));
        filterColCombo.setSelectedIndex(prevFilter >= 0 && prevFilter < names.length ? prevFilter : 0);

        int prevSort = sortColCombo.getSelectedIndex();
        sortColCombo.setModel(new DefaultComboBoxModel<>(names));
        sortColCombo.setSelectedIndex(prevSort >= 0 && prevSort < names.length ? prevSort : 0);

        filterLabel.setText(LocaleManager.get("main.filter.label"));
        colLabel.setText(LocaleManager.get("main.filter.col"));
        sortLbl.setText(LocaleManager.get("main.sort.label"));

        int[] widths = {55, 110, 60, 60, 130, 90, 90, 110, 150, 80, 80, 100, 100};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private String[] buildColNames() {
        return Arrays.stream(COL_KEYS)
                .map(LocaleManager::get)
                .toArray(String[]::new);
    }

    public JTable getTable() { return table; }
}