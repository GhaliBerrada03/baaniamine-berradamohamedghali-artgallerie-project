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
    private OeuvreServices oeuvreServices = new OeuvreServices();
    private VenteArtService venteArtService = new VenteArtService();

    public OeuvrePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] columnNames = { "ID", "Title", "Artist", "Category", "Price", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        oeuvreTable = new JTable(tableModel);
        add(new JScrollPane(oeuvreTable), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Artworks");
        JButton buyBtn = new JButton("Buy Selected");

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
        try {
            List<Oeuvre> oeuvres = oeuvreServices.findAllOeuvre();
            for (Oeuvre o : oeuvres) {
                tableModel.addRow(new Object[] {
                        o.getIdOeuvre(),
                        o.getTitre(),
                        o.getArtiste(),
                        o.getCategorie(),
                        o.getPrix() + " €",
                        o.getStatut() == null ? "DISPONIBLE" : o.getStatut()
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
