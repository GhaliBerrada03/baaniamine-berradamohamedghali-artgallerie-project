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
        setTitle("Art Gallery - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        JButton forgotPasswordBtn = new JButton("Forgot Password?");
        forgotPasswordBtn.setBorderPainted(false);
        forgotPasswordBtn.setContentAreaFilled(false);
        forgotPasswordBtn.setForeground(Color.BLUE);
        forgotPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(forgotPasswordBtn);

        add(panel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> performLogin());
        forgotPasswordBtn.addActionListener(e -> handleForgotPassword());
    }

    private void handleForgotPassword() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            email = JOptionPane.showInputDialog(this, "Enter your email to reset password:");
            if (email == null || email.trim().isEmpty()) return;
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
                    JOptionPane.showMessageDialog(this, "Failed to reset password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Email not found in our database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Client> clients = clientService.showClients();

            Client foundClient   = null;
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
                JOptionPane.showMessageDialog(this, "Email not found: " + email, "Login Failed", JOptionPane.ERROR_MESSAGE);

            } else {
                // Wrong password → auto send reset email
                String newPass = "Reset" + (int) (Math.random() * 9000 + 1000);
                boolean success = clientService.resetPassword(clientToReset.getId_client(), newPass);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Incorrect password.\n\nA new password has been automatically sent to " + email + "!",
                            "Security Notification", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect password. Failed to send reset email.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

class SessionManager {
    private static SessionManager instance;
    private Client currentClient;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public Client getCurrentClient() { return currentClient; }
    public void setCurrentClient(Client currentClient) { this.currentClient = currentClient; }
    public boolean isLoggedIn() { return currentClient != null; }
}
