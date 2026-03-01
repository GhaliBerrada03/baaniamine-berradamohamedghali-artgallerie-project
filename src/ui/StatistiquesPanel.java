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
        setLayout(new GridLayout(2, 2, 20, 20)); // Increased gap
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(ThemeManager.BACKGROUND);
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
            add(createCard("TOTAL ACQUISITIONS", String.valueOf(sales.size()), ThemeManager.PRIMARY));
            int revenue = sales.stream().mapToInt(v -> v.getOuvre().getPrix()).sum();
            add(createCard("TOTAL REVENUE VALUE", revenue + " MAD", ThemeManager.SECONDARY));

            // Top Artistes Chart
            List<String> rawTop = venteArtdao.afficherTopArtiste();
            Map<String, Integer> artistData = new LinkedHashMap<>();
            for (String entry : rawTop) {
                String[] parts = entry.split("\\|");
                artistData.put(parts[0].trim(), Integer.parseInt(parts[1].replace("ventes", "").trim()));
            }
            add(new ModernBarChart("ARTIST SALES PERFORMANCE", artistData));

            // Top Clients Chart
            Map<String, Integer> clientData = sales.stream()
                    .collect(Collectors.groupingBy(v -> v.getClient().getNom(),
                            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));

            // Sort by count and limit to top 5
            Map<String, Integer> sortedClientData = clientData.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                            LinkedHashMap::new));

            add(new ModernBarChart("VIP CLIENT ACQUISITIONS", sortedClientData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valLabel.setForeground(color == ThemeManager.SECONDARY ? ThemeManager.PRIMARY : ThemeManager.WHITE);

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(ThemeManager.FONT_BOLD);
        lblLabel.setForeground(color == ThemeManager.SECONDARY ? ThemeManager.PRIMARY : ThemeManager.WHITE);

        card.add(valLabel, BorderLayout.CENTER);
        card.add(lblLabel, BorderLayout.SOUTH);
        return card;
    }

    private static class ModernBarChart extends JPanel {
        private String title;
        private Map<String, Integer> data;

        public ModernBarChart(String title, Map<String, Integer> data) {
            this.title = title;
            this.data = data;
            setBackground(ThemeManager.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 40;

            // Header Title
            g2.setColor(ThemeManager.PRIMARY);
            g2.setFont(ThemeManager.FONT_BOLD);
            g2.drawString(title, padding, 30);

            if (data == null || data.isEmpty()) {
                g2.setFont(ThemeManager.FONT_REGULAR);
                g2.setColor(Color.GRAY);
                g2.drawString("No data available to display", width / 2 - 80, height / 2);
                return;
            }

            int maxValue = data.values().stream().max(Integer::compare).orElse(1);
            int barHeight = 25;
            int gap = 25;
            int currentY = 70;
            int totalAvailableWidth = width - (padding * 2) - 150; // space for labels

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                // Name
                g2.setColor(ThemeManager.PRIMARY);
                g2.setFont(ThemeManager.FONT_REGULAR);
                String label = entry.getKey();
                if (label.length() > 15)
                    label = label.substring(0, 12) + "...";
                g2.drawString(label, padding, currentY + barHeight / 2 + 5);

                // Bar Shadow/Background
                g2.setColor(new Color(240, 240, 240));
                g2.fillRoundRect(padding + 120, currentY, totalAvailableWidth, barHeight, 8, 8);

                // Actual Bar
                int barWidth = (int) ((double) entry.getValue() / maxValue * totalAvailableWidth);
                g2.setColor(ThemeManager.SECONDARY);
                g2.fillRoundRect(padding + 120, currentY, barWidth, barHeight, 8, 8);

                // Value Label
                g2.setColor(ThemeManager.PRIMARY);
                g2.setFont(ThemeManager.FONT_BOLD);
                g2.drawString(String.valueOf(entry.getValue()), padding + 125 + barWidth, currentY + barHeight / 2 + 5);

                currentY += barHeight + gap;
                if (currentY > height - padding)
                    break;
            }
        }
    }
}
