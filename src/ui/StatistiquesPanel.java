package ui;

import dao.VenteArtdao;
import model.VenteArt;
import services.VenteArtService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class StatistiquesPanel extends JPanel {

    private VenteArtService venteArtService = new VenteArtService();
    private VenteArtdao venteArtdao = new VenteArtdao();

    public StatistiquesPanel() {
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loadStats();
    }

    public void refreshStats() {
        removeAll();
        loadStats();
        revalidate();
        repaint();
    }

    private void loadStats() {
        try {
            List<VenteArt> sales = venteArtService.showAllventes();
            add(createCard("Total Sales", String.valueOf(sales.size()), new Color(52, 152, 219)));
            int revenue = sales.stream().mapToInt(v -> v.getOuvre().getPrix()).sum();
            add(createCard("Total Revenue", revenue + " MAD", new Color(39, 174, 96)));
            add(createTopArtistesTable());
            add(createTopClientsTable(sales));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valLabel.setForeground(Color.WHITE);

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblLabel.setForeground(Color.WHITE);

        card.add(valLabel, BorderLayout.CENTER);
        card.add(lblLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createTopArtistesTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Top Artists by Sales"));
        try {
            List<String> top = venteArtdao.afficherTopArtiste();
            String[] cols = {"Artist", "Sales"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            for (String entry : top) {
                String[] parts = entry.split("\\|");
                model.addRow(new Object[]{parts[0], parts[1].replace("ventes", "").trim()});
            }
            JTable table = new JTable(model);
            table.setAutoCreateRowSorter(true);
            panel.add(new JScrollPane(table), BorderLayout.CENTER);
        } catch (Exception e) {
            panel.add(new JLabel("Error loading data"), BorderLayout.CENTER);
        }
        return panel;
    }

    private JPanel createTopClientsTable(List<VenteArt> sales) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Top Clients by Purchases"));
        String[] cols = {"Client", "Purchases"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Map<String, Long> counts = sales.stream()
                .collect(Collectors.groupingBy(v -> v.getClient().getNom(), Collectors.counting()));
        counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> model.addRow(new Object[]{e.getKey(), e.getValue()}));
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}