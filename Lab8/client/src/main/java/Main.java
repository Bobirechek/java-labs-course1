import gui.AuthFrame;
import gui.utils.GuiTheme;
import managers.LocaleManager;
import managers.NetworkManager;

import javax.swing.*;
import java.net.UnknownHostException;


public class Main {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        System.setProperty("file.encoding", "UTF-8");

        GuiTheme.applyGlobalDefaults();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            GuiTheme.applyGlobalDefaults();
        } catch (Exception ignored) {}

        String host = (args.length > 0 && !args[0].isBlank()) ? args[0].trim() : "localhost";

        SwingUtilities.invokeLater(() -> {
            NetworkManager network;
            try {
                network = new NetworkManager(host);
            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(null,
                        "Не удалось разрешить адрес сервера: " + host + "\n" + e.getMessage(),
                        "Ошибка запуска", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
                return;
            }

            // Устанавливаем русскую локаль по умолчанию
            LocaleManager.setLocale(LocaleManager.RUSSIAN);

            AuthFrame auth = new AuthFrame(network);
            auth.setVisible(true);
        });
    }
}
