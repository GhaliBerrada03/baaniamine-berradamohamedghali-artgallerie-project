package ui;

import model.Client;
import services.ClientService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private ClientService clientService = new ClientService();

    public LoginFrame() {
        ThemeManager.applyTheme();
        setTitle("Art Gallery - Premium Access");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        mainPanel.setBackground(ThemeManager.WHITE);

        // Header / Logo area
        JLabel titleLabel = new JLabel("ART GALLERY");
        titleLabel.setFont(ThemeManager.FONT_TITLE);
        titleLabel.setForeground(ThemeManager.PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(ThemeManager.FONT_REGULAR);
        subtitleLabel.setForeground(ThemeManager.SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form fields
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(ThemeManager.FONT_BOLD);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField = ThemeManager.createModernTextField();
        emailField.setMaximumSize(new Dimension(400, 40));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(ThemeManager.FONT_BOLD);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = ThemeManager.createModernPasswordField();
        passwordField.setMaximumSize(new Dimension(400, 40));

        loginButton = ThemeManager.createModernButton("SIGN IN");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(400, 45));

        JButton forgotPasswordBtn = new JButton("Forgot your password?");
        forgotPasswordBtn.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        forgotPasswordBtn.setBorderPainted(false);
        forgotPasswordBtn.setContentAreaFilled(false);
        forgotPasswordBtn.setForeground(ThemeManager.TEXT_SECONDARY);
        forgotPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding components with spacing
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        mainPanel.add(emailLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(emailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(forgotPasswordBtn);

        add(mainPanel, BorderLayout.CENTER);

        // Footer decoration
        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(450, 10));
        footer.setBackground(ThemeManager.PRIMARY);
        add(footer, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> performLogin());
        forgotPasswordBtn.addActionListener(e -> handleForgotPassword());
    }

    private void handleForgotPassword() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            email = JOptionPane.showInputDialog(this, "Enter your email to reset password:");
            if (email == null || email.trim().isEmpty())
                return;
            email = email.trim();
        }

        try {
            List<Client> clients = clientService.showClients();
            Client found = null;
            for (Client c : clients) {
                if (c.getEmail().equalsIgnoreCase(email)) {
                    found = c;
                    break;
                }
            }

            if (found != null) {
                String newPass = "Reset" + (int) (Math.random() * 9000 + 1000);
                boolean success = clientService.resetPassword(found.getId_client(), newPass);
                if (success) {
                    JOptionPane.showMessageDialog(this, "A new password has been sent to " + email);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reset password. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Email not found in our database.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Client> clients = clientService.showClients();

            Client foundClient = null;
            Client clientToReset = null;
            boolean emailMatched = false;

            // Hash the typed password using setPassword() which calls the hash internally
            Client temp = new Client();
            temp.setPassword(password);
            String hashedInput = temp.getPassword();

            for (Client c : clients) {
                if (c.getEmail().equalsIgnoreCase(email)) {
                    emailMatched = true;
                    String dbPass = (c.getPassword() != null) ? c.getPassword().trim() : "";

                    if (hashedInput.equals(dbPass)) {
                        foundClient = c;
                    } else {
                        clientToReset = c;
                    }
                    break;
                }
            }

            if (foundClient != null) {
                SessionManager.getInstance().setCurrentClient(foundClient);
                JOptionPane.showMessageDialog(this, "Welcome " + foundClient.getNom() + "!");
                this.dispose();
                new MainFrame().setVisible(true);

            } else if (!emailMatched) {
                JOptionPane.showMessageDialog(this, "Email not found: " + email, "Login Failed",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                // Wrong password → auto send reset email
                String newPass = "Reset" + (int) (Math.random() * 9000 + 1000);
                boolean success = clientService.resetPassword(clientToReset.getId_client(), newPass);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Incorrect password.\n\nA new password has been automatically sent to " + email + "!",
                            "Security Notification", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect password. Failed to send reset email.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        ThemeManager.applyTheme();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

class SessionManager {
    private static SessionManager instance;
    private Client currentClient;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public boolean isLoggedIn() {
        return currentClient != null;
    }
}
