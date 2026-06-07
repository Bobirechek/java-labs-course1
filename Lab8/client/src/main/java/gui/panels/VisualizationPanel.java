package gui.panels;

import gui.utils.GuiTheme;
import managers.LocaleManager;
import models.HumanBeing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class VisualizationPanel extends JPanel {

    private static final int WORLD_W = 1000;
    private static final int WORLD_H = 800;

    private static class ObjectState {
        HumanBeing human;
        float alpha = 0f;
        float scale = 0.1f;
        boolean dying = false;
        float pulse = 0f;
        int pulseDir = 1;
        // Для bounce-анимации появления
        boolean appeared = false;

        ObjectState(HumanBeing h) { this.human = h; }
    }

    private final List<ObjectState> states = new ArrayList<>();
    private final javax.swing.Timer animTimer;
    private HumanBeing selected  = null;
    private Consumer<HumanBeing> onSelect;

    private double camX = 0;
    private double camY = 0;
    private double zoom = 1.0;
    private Point dragStart;

    private String hintText = "";
    private String controlHint = "";

    public VisualizationPanel() {
        setBackground(GuiTheme.BG_DARK);
        setPreferredSize(new Dimension(800, 600));

        animTimer = new javax.swing.Timer(16, e -> tick());
        animTimer.start();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    dragStart = e.getPoint();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    handleClick(e.getX(), e.getY(), false);
                }
            }
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    handleClick(e.getX(), e.getY(), true);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) dragStart = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null && SwingUtilities.isRightMouseButton(e)) {
                    camX -= (e.getX() - dragStart.x) / zoom;
                    camY -= (e.getY() - dragStart.y) / zoom;
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });

        addMouseWheelListener(e -> {
            double factor = e.getWheelRotation() < 0 ? 1.1 : 0.9;
            zoom = Math.max(0.15, Math.min(6.0, zoom * factor));
            repaint();
        });

        LocaleManager.addListener(() -> {
            hintText = LocaleManager.get("vis.click.info");
            controlHint = LocaleManager.get("vis.control.hint");
            repaint();
        });
        hintText = LocaleManager.get("vis.click.info");
        controlHint = LocaleManager.get("vis.control.hint");
    }

    public void setOnSelect(Consumer<HumanBeing> c) { this.onSelect = c; }

    public void stopAnimation() { animTimer.stop(); }

    // Обновление данных
    public void updateData(List<HumanBeing> humans) {
        if (humans == null) humans = Collections.emptyList();
        synchronized (states) {
            Set<Long> incoming = new HashSet<>();
            for (HumanBeing h : humans) {
                if (h != null && h.getId() != null) incoming.add(h.getId());
            }
            for (HumanBeing h : humans) {
                if (h == null || h.getId() == null) continue;
                ObjectState ex = findState(h.getId());
                if (ex != null) {
                    ex.human = h;
                    ex.dying = false;
                } else {
                    states.add(new ObjectState(h));
                }
            }
            for (ObjectState s : states) {
                if (s.human.getId() != null && !incoming.contains(s.human.getId())) {
                    s.dying = true;
                }
            }
        }
    }

    private ObjectState findState(long id) {
        for (ObjectState s : states) {
            if (s.human.getId() != null && s.human.getId() == id) return s;
        }
        return null;
    }

    // Анимация
    private void tick() {
        boolean changed = false;
        synchronized (states) {
            Iterator<ObjectState> it = states.iterator();
            while (it.hasNext()) {
                ObjectState s = it.next();
                if (s.dying) {
                    s.alpha -= 0.06f;
                    s.scale -= 0.05f;
                    if (s.alpha <= 0) { it.remove(); changed = true; continue; }
                } else {
                    if (!s.appeared) {
                        if (s.alpha < 1f) {
                            s.alpha = Math.min(1f, s.alpha + 0.06f);
                        }
                        if (s.scale < 1.25f) {
                            s.scale = Math.min(1.25f, s.scale + 0.08f);
                        } else {
                            s.scale = Math.max(1.0f, s.scale - 0.04f);
                            if (s.scale <= 1.0f) {
                                s.scale    = 1.0f;
                                s.appeared = true;
                            }
                        }
                    }
                    s.pulse += 0.03f * s.pulseDir;
                    if (s.pulse >  1f) { s.pulse =  1f; s.pulseDir = -1; }
                    if (s.pulse <  0f) { s.pulse =  0f; s.pulseDir =  1; }
                }
                changed = true;
            }
        }
        if (changed) repaint();
    }

    // Отрисовка
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawBackground(g2);
        drawGrid(g2);

        g2.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2.scale(zoom, zoom);
        g2.translate(-camX - WORLD_W / 2.0, -camY - WORLD_H / 2.0);

        List<ObjectState> snapshot;
        synchronized (states) { snapshot = new ArrayList<>(states); }
        for (ObjectState s : snapshot) drawObject(g2, s);

        g2.dispose();
        drawHUD(g);
    }

    private void drawBackground(Graphics2D g2) {
        RadialGradientPaint rgp = new RadialGradientPaint(
                getWidth() / 2f, getHeight() / 2f,
                Math.max(getWidth(), getHeight()) * 0.8f,
                new float[]{0f, 1f},
                new Color[]{new Color(22, 22, 40), GuiTheme.BG_DARK});
        g2.setPaint(rgp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(40, 40, 60, 120));
        g2.setStroke(new BasicStroke(0.5f));
        int step = 60;
        for (int x = 0; x < getWidth();  x += step) g2.drawLine(x, 0, x, getHeight());
        for (int y = 0; y < getHeight(); y += step) g2.drawLine(0, y, getWidth(), y);
    }

    private void drawObject(Graphics2D g2, ObjectState s) {
        HumanBeing h = s.human;
        if (h.getCoordinates() == null) return;

        double wx = safeMap(h.getCoordinates().getX(), 400, WORLD_W);
        double wy = safeMap(h.getCoordinates().getY(), 300, WORLD_H);

        Color userColor = GuiTheme.getUserColor(h.getOwnerLogin());
        boolean isSel = selected != null
                && h.getId() != null
                && Objects.equals(selected.getId(), h.getId());

        Graphics2D g = (Graphics2D) g2.create();
        g.translate(wx, wy);
        g.scale(s.scale, s.scale);

        float alpha = Math.max(0f, Math.min(1f, s.alpha));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Круг
        float haloR = 28 + s.pulse * 5 + (isSel ? 6 : 0);
        g.setColor(new Color(userColor.getRed(), userColor.getGreen(), userColor.getBlue(),
                isSel ? 70 : 35));
        g.fillOval((int)(-haloR), (int)(-haloR), (int)(haloR * 2), (int)(haloR * 2));

        if (isSel) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2f));
            g.drawOval(-26, -26, 52, 52);
        }

        drawHumanFigure(g, userColor, h.isRealHero(), h.getImpactSpeed());

        // Имя под фигуркой
        g.setColor(GuiTheme.TEXT_PRIMARY);
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();
        String lbl = h.getName() != null
                ? (h.getName().length() > 10 ? h.getName().substring(0, 8) + ".." : h.getName())
                : "?";
        g.drawString(lbl, -fm.stringWidth(lbl) / 2, 32);

        g.dispose();
    }

    private void drawHumanFigure(Graphics2D g, Color color, boolean realHero, double speed) {
        double safeSpeed = Double.isNaN(speed) || Double.isInfinite(speed)
                ? 0 : Math.max(0, Math.min(597, speed));

        g.setColor(color);
        g.fillOval(-9, -24, 18, 18); // голова

        g.setColor(color.darker());
        g.fillRoundRect(-6, -6, 12, 18, 4, 4); // тело

        g.setColor(color);
        g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(-3, 12, -5, 22); // левая нога
        g.drawLine(3,  12,  5, 22); // правая нога

        // Руки — угол зависит от скорости удара
        double armAngle = -(0.2 + (safeSpeed / 597.0) * 0.7);
        int ax = (int)(12 * Math.cos(armAngle));
        int ay = (int)(12 * Math.sin(armAngle));
        g.drawLine(-6, 0, -6 - ax,  ay);
        g.drawLine( 6, 0,  6 + ax,  ay);

        g.setColor(GuiTheme.BG_DARK);
        g.fillOval(-6, -21, 4, 4); // левый глаз
        g.fillOval( 2, -21, 4, 4); // правый глаз

        g.setStroke(new BasicStroke(1.5f));
        g.drawArc(-4, -10, 8, 6, 0, -180); // улыбка

        if (realHero) {
            g.setColor(GuiTheme.WARNING);
            g.setStroke(new BasicStroke(2f));
            int[] xs = {-8, -5, 0, 5, 8, 8, -8};
            int[] ys = {-28, -24, -28, -24, -28, -24, -24};
            g.fillPolygon(xs, ys, 7);
            g.setColor(GuiTheme.WARNING.darker());
            g.drawPolygon(xs, ys, 7);
        }
    }

    private void drawHUD(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Подсказка клика
        g2.setColor(new Color(148, 163, 184, 140));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
        g2.drawString(hintText, 10, getHeight() - 10);

        // Подсказка управления 
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2.setColor(new Color(100, 100, 140));
        g2.drawString(controlHint, 10, getHeight() - 25);

        // Zoom
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(148, 163, 184, 140));
        String zoomStr = String.format("%.0f%%", zoom * 100);
        g2.drawString("Zoom: " + zoomStr, getWidth() - 75, getHeight() - 10);

        // Информация о выбранном объекте
        if (selected != null) {
            String coords = "";
            if (selected.getCoordinates() != null) {
                coords = "[" + selected.getCoordinates().getX()
                       + ", " + selected.getCoordinates().getY() + "]";
            }
            String info = String.format("ID:%s  %s  %s  @%s",
                    selected.getId()       != null ? selected.getId()       : "?",
                    selected.getName()     != null ? selected.getName()     : "?",
                    coords,
                    selected.getOwnerLogin() != null ? selected.getOwnerLogin() : "?");
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            int tw = g2.getFontMetrics().stringWidth(info) + 20;
            g2.setColor(new Color(28, 28, 45, 210));
            g2.fillRoundRect(8, 8, tw, 28, 8, 8);
            g2.setColor(GuiTheme.ACCENT);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(8, 8, tw, 28, 8, 8);
            g2.setColor(GuiTheme.TEXT_PRIMARY);
            g2.drawString(info, 18, 27);
        }

        g2.dispose();
    }

    // Обработка кликов
    private void handleClick(int px, int py, boolean showPopup) {
        double cx = getWidth()  / 2.0;
        double cy = getHeight() / 2.0;
        double wxOrigin = -camX - WORLD_W / 2.0;
        double wyOrigin = -camY - WORLD_H / 2.0;

        List<ObjectState> snapshot;
        synchronized (states) { snapshot = new ArrayList<>(states); }

        for (ObjectState s : snapshot) {
            if (s.dying || s.human.getCoordinates() == null) continue;

            double ox = safeMap(s.human.getCoordinates().getX(), 400, WORLD_W);
            double oy = safeMap(s.human.getCoordinates().getY(), 300, WORLD_H);
            double screenX = cx + (ox + wxOrigin) * zoom;
            double screenY = cy + (oy + wyOrigin) * zoom;
            double dx = px - screenX;
            double dy = py - screenY;
            double hitR = 28 * Math.max(0.1, s.scale) * zoom;

            if (dx * dx + dy * dy < hitR * hitR) {
                selected = s.human;
                repaint();
                if (showPopup) {
                    final HumanBeing clicked = s.human;
                    SwingUtilities.invokeLater(() -> showInfoPopup(clicked));
                }
                if (onSelect != null) onSelect.accept(selected);
                return;
            }
        }
        selected = null;
        repaint();
    }

    private void showInfoPopup(HumanBeing h) {
        if (h == null) return;
        String coords = h.getCoordinates() != null
                ? h.getCoordinates().getX() + ", " + h.getCoordinates().getY() : "—";
        String info = "<html><b>" + esc(h.getName()) + "</b><br>"
                + "ID: " + h.getId() + "<br>"
                + "Coords: [" + coords + "]<br>"
                + "Speed: " + h.getImpactSpeed() + "<br>"
                + "Soundtrack: " + esc(h.getSoundtrackName()) + "<br>"
                + "Weapon: " + h.getWeaponType() + "<br>"
                + "Mood: " + h.getMood() + "<br>"
                + "Real hero: " + h.isRealHero() + "<br>"
                + "Owner: " + esc(h.getOwnerLogin())
                + "</html>";
        JOptionPane.showMessageDialog(this, info,
                h.getName() != null ? h.getName() : "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static String esc(String s) {
        if (s == null) return "—";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static double safeMap(long coord, long modulo, int worldMax) {
        return ((coord % modulo) + modulo) % modulo / (double) modulo * worldMax;
    }

    public HumanBeing getSelected() { return selected; }

    public void reset() {
        selected = null;
        camX = 0; camY = 0; zoom = 1.0;
        repaint();
    }
}