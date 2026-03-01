
package ui;

import model.Client;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        ThemeManager.applyTheme();
        setTitle("Art Gallery Premium Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.BACKGROUND);

        // Custom Modern Header
        Client user = SessionManager.getInstance().getCurrentClient();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ThemeManager.PRIMARY);
        header.setPreferredSize(new Dimension(1100, 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        JLabel titleLabel = new JLabel("GALERIE D'ART");
        titleLabel.setFont(ThemeManager.FONT_TITLE);
        titleLabel.setForeground(ThemeManager.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, " + (user != null ? user.getNom() : "Guest"));
        userLabel.setFont(ThemeManager.FONT_BOLD);
        userLabel.setForeground(ThemeManager.SECONDARY);

        JButton logoutBtn = ThemeManager.createModernButton("LOGOUT");
        logoutBtn.setMargin(new Insets(2, 5, 2, 5));
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.addActionListener(e -> {
            SessionManager.getInstance().setCurrentClient(null);
            dispose();
            new LoginFrame().setVisible(true);
        });

        userPanel.add(userLabel);
        userPanel.add(logoutBtn);
        header.add(userPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Tabbed Pane - Modern Styling Attempt
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(ThemeManager.FONT_BOLD);
        tabs.setBackground(ThemeManager.WHITE);
        tabs.setForeground(ThemeManager.PRIMARY);

        OeuvrePanel oeuvrePanel = new OeuvrePanel();
        ClientPanel clientPanel = new ClientPanel();
        VentePanel ventePanel = new VentePanel();
        StatistiquesPanel statistiquesPanel = new StatistiquesPanel();

        tabs.addTab(" GALLERY STORE ", oeuvrePanel);
        tabs.addTab(" CLIENT  ", clientPanel);
        tabs.addTab(" SALES ARCHIVE ", ventePanel);
        tabs.addTab(" ANALYTICS ", statistiquesPanel);

        tabs.addChangeListener(e -> {
            int index = tabs.getSelectedIndex();
            if (index == 0)
                oeuvrePanel.loadArtworks();
            else if (index == 1)
                clientPanel.loadClients();
            else if (index == 2)
                ventePanel.loadAllSales();
            else if (index == 3)
                statistiquesPanel.refreshStats();
        });

        add(tabs, BorderLayout.CENTER);
    }
}
