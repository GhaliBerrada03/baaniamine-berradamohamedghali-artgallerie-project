package ui;

import model.Client;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Art Gallery Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header info
        Client user = SessionManager.getInstance().getCurrentClient();
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        header.add(new JLabel("Current User: " + (user != null ? user.getNom() : "Guest")), BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            SessionManager.getInstance().setCurrentClient(null);
            dispose();
            new LoginFrame().setVisible(true);
        });
        header.add(logoutBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        OeuvrePanel oeuvrePanel = new OeuvrePanel();
        ClientPanel clientPanel = new ClientPanel();
        VentePanel ventePanel = new VentePanel();
        StatistiquesPanel statistiquesPanel = new StatistiquesPanel();
        tabs.addTab("Artworks & Store", oeuvrePanel);
        tabs.addTab("Client List", clientPanel);
        tabs.addTab("Sales History & Search", ventePanel);
        tabs.addTab("Statistiques", statistiquesPanel);

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
