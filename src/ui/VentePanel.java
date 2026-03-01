package ui;

import com.toedter.calendar.JDateChooser;
import model.VenteArt;
import services.VenteArtService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VentePanel extends JPanel {
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private VenteArtService venteArtService = new VenteArtService();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VentePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(ThemeManager.BACKGROUND);

        // Filter UI
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);

        JLabel fromLabel = new JLabel("PERIOD FROM:");
        fromLabel.setFont(ThemeManager.FONT_BOLD);
        filterPanel.add(fromLabel);

        startDateChooser = new JDateChooser();
        startDateChooser.setPreferredSize(new Dimension(150, 30));
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        filterPanel.add(startDateChooser);

        JLabel toLabel = new JLabel("TO:");
        toLabel.setFont(ThemeManager.FONT_BOLD);
        filterPanel.add(toLabel);

        endDateChooser = new JDateChooser();
        endDateChooser.setPreferredSize(new Dimension(150, 30));
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        filterPanel.add(endDateChooser);

        JButton searchBtn = ThemeManager.createModernButton("FILTER SALES");
        JButton resetBtn = ThemeManager.createModernButton("RESET");
        resetBtn.setBackground(ThemeManager.ACCENT);

        filterPanel.add(searchBtn);
        filterPanel.add(resetBtn);
        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = { "ID", "Client", "Artwork", "Date" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(tableModel);
        ThemeManager.styleTable(salesTable);

        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        searchBtn.addActionListener(e -> filterSales());
        resetBtn.addActionListener(e -> loadAllSales());

        loadAllSales();
    }

    public void loadAllSales() {
        tableModel.setRowCount(0);
        startDateChooser.setDate(null);
        endDateChooser.setDate(null);
        try {
            List<VenteArt> sales = venteArtService.showAllventes();
            displaySales(sales);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterSales() {
        Date start = startDateChooser.getDate();
        Date end = endDateChooser.getDate();

        if (start == null || end == null) {
            JOptionPane.showMessageDialog(this, "Please select both Start and End dates.");
            return;
        }

        try {
            java.time.LocalDate localStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            java.time.LocalDate localEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (localEnd.isBefore(localStart)) {
                JOptionPane.showMessageDialog(this, "End date cannot be before Start date.");
                return;
            }

            List<VenteArt> allSales = venteArtService.showAllventes();
            List<VenteArt> filtered = allSales.stream()
                    .filter(v -> {
                        java.time.LocalDate venteDate = v.getDateVente().toLocalDate();
                        return (venteDate.isEqual(localStart) || venteDate.isAfter(localStart)) &&
                                (venteDate.isEqual(localEnd) || venteDate.isBefore(localEnd));
                    })
                    .collect(Collectors.toList());

            tableModel.setRowCount(0);
            displaySales(filtered);

            if (filtered.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No sales found in this range.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during search: " + e.getMessage());
        }
    }

    private void displaySales(List<VenteArt> sales) {
        for (VenteArt v : sales) {
            tableModel.addRow(new Object[] {
                    v.getIdVente(),
                    v.getClient().getNom(),
                    v.getOuvre().getTitre(),
                    v.getDateVente().format(formatter)
            });
        }
    }
}
