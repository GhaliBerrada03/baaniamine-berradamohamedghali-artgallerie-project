package ui;

import model.Client;
import model.Oeuvre;
import model.VenteArt;
import services.OeuvreServices;
import services.VenteArtService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class OeuvrePanel extends JPanel {
    private JTable oeuvreTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    private OeuvreServices oeuvreServices = new OeuvreServices();
    private VenteArtService venteArtService = new VenteArtService();

    public OeuvrePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(ThemeManager.BACKGROUND);

        // Header Top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Collections & Acquisitions");
        titleLabel.setFont(ThemeManager.FONT_SUBTITLE);
        titleLabel.setForeground(ThemeManager.PRIMARY);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Filter UI
        JPanel filterUI = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterUI.setOpaque(false);
        filterUI.add(new JLabel("FILTER BY STATUS:"));

        statusFilter = new JComboBox<>(new String[] { "ALL", "DISPONIBLE", "VENDUE" });
        statusFilter.setFont(ThemeManager.FONT_BOLD);
        statusFilter.setPreferredSize(new Dimension(150, 30));
        statusFilter.addActionListener(e -> loadArtworks());
        filterUI.add(statusFilter);

        topPanel.add(filterUI, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table Styling
        String[] columnNames = { "ID", "Title", "Artist", "Category", "Price", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        oeuvreTable = new JTable(tableModel);
        ThemeManager.styleTable(oeuvreTable);

        JScrollPane scrollPane = new JScrollPane(oeuvreTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        JButton refreshBtn = ThemeManager.createModernButton("REFRESH LIST");
        JButton buyBtn = ThemeManager.createModernButton("ACQUIRE ARTWORK");
        buyBtn.setBackground(ThemeManager.SECONDARY);
        buyBtn.setForeground(ThemeManager.PRIMARY);

        btnPanel.add(refreshBtn);
        btnPanel.add(buyBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Actions
        refreshBtn.addActionListener(e -> loadArtworks());
        buyBtn.addActionListener(e -> purchaseArtwork());

        loadArtworks();
    }

    public void loadArtworks() {
        tableModel.setRowCount(0);
        String selectedStatus = (String) statusFilter.getSelectedItem();

        try {
            List<Oeuvre> oeuvres = oeuvreServices.findAllOeuvre();
            for (Oeuvre o : oeuvres) {
                String status = o.getStatut() == null ? "DISPONIBLE" : o.getStatut();

                // Filtering Logic
                if (!"ALL".equals(selectedStatus) && !selectedStatus.equals(status)) {
                    continue;
                }

                tableModel.addRow(new Object[] {
                        o.getIdOeuvre(),
                        o.getTitre(),
                        o.getArtiste(),
                        o.getCategorie(),
                        o.getPrix() + " €",
                        status
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void purchaseArtwork() {
        int row = oeuvreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an artwork.");
            return;
        }

        String status = (String) tableModel.getValueAt(row, 5);
        if ("VENDUE".equals(status)) {
            JOptionPane.showMessageDialog(this, "Already sold.");
            return;
        }

        try {
            Client client = SessionManager.getInstance().getCurrentClient();
            if (client == null) {
                JOptionPane.showMessageDialog(this, "Login required.");
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);
            Oeuvre o = oeuvreServices.findOeuvre(id);

            VenteArt v = new VenteArt(client, o, LocalDateTime.now());
            venteArtService.newVente(v);
            oeuvreServices.updatedstatut(id);

            JOptionPane.showMessageDialog(this, "Success!");
            loadArtworks();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
