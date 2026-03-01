package ui;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import model.Client;
import services.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Properties;

public class ClientPanel extends JPanel {
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private ClientService clientService = new ClientService();

    // SMTP Configuration (derived from EmailService)
    private static final String USERNAME = "baaamine97@gmail.com";
    private static final String APP_PASSWORD = "febk dksr mrqa ejax";

    public ClientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(ThemeManager.BACKGROUND);

        // Header Title
        JLabel titleLabel = new JLabel("Client Management & Communication");
        titleLabel.setFont(ThemeManager.FONT_SUBTITLE);
        titleLabel.setForeground(ThemeManager.PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        // Table Styling
        String[] columnNames = { "ID", "Name", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientTable = new JTable(tableModel);
        ThemeManager.styleTable(clientTable);

        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        JButton refreshBtn = ThemeManager.createModernButton("REFRESH CLIENTS");
        JButton sendEmailBtn = ThemeManager.createModernButton("SEND CORRESPONDENCE");
        sendEmailBtn.setBackground(ThemeManager.SECONDARY);
        sendEmailBtn.setForeground(ThemeManager.PRIMARY);

        refreshBtn.addActionListener(e -> loadClients());
        sendEmailBtn.addActionListener(e -> sendEmailToSelectedClient());

        btnPanel.add(refreshBtn);
        btnPanel.add(sendEmailBtn);
        add(btnPanel, BorderLayout.SOUTH);

        loadClients();
    }

    public void loadClients() {
        tableModel.setRowCount(0);
        try {
            List<Client> clients = clientService.showClients();
            for (Client c : clients) {
                tableModel.addRow(new Object[] { c.getId_client(), c.getNom(), c.getEmail() });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading clients: " + e.getMessage());
        }
    }

    private void sendEmailToSelectedClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client from the table first.");
            return;
        }

        String clientEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String clientName = (String) tableModel.getValueAt(selectedRow, 1);

        // Create custom dialog for email input
        JPanel emailPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField subjectField = new JTextField();
        JTextArea messageArea = new JTextArea(10, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        emailPanel.add(new JLabel("To: " + clientName + " (" + clientEmail + ")"));
        emailPanel.add(new JLabel("Subject:"));
        emailPanel.add(subjectField);
        emailPanel.add(new JLabel("Message:"));
        emailPanel.add(new JScrollPane(messageArea));

        int result = JOptionPane.showConfirmDialog(this, emailPanel,
                "Send Email to " + clientName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String subject = subjectField.getText().trim();
            String content = messageArea.getText().trim();

            if (subject.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Subject and Message cannot be empty.");
                return;
            }

            // Send email in a separate thread to keep UI responsive
            new Thread(() -> {
                try {
                    sendEmail(clientEmail, subject, content);
                    SwingUtilities.invokeLater(
                            () -> JOptionPane.showMessageDialog(this, "Email sent successfully to " + clientEmail));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Failed to send email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        }
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
