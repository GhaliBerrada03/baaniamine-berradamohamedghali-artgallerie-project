package services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private static final String USERNAME = "baaamine97@gmail.com";
    private static final String APP_PASSWORD = "febk dksr mrqa ejax";

    private Session buildSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, APP_PASSWORD);
            }
        });
    }

    // Méthode 1 : Email de confirmation après un achat
    public void sendPurchaseConfirmation(String toEmail, String clientNom, String titreOeuvre, int prix) {
        try {
            Session session = buildSession();
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject("Confirmation de votre achat - Art Gallery");
            msg.setText(
                    "Bonjour " + clientNom + ",\n\n" +
                            "Votre achat a bien été enregistré :\n" +
                            "🎨 Oeuvre  : " + titreOeuvre + "\n" +
                            "💰 Prix    : " + prix + " MAD\n\n" +
                            "Merci pour votre confiance.\n" +
                            "Art Gallery"
            );
            Transport.send(msg);
            System.out.println("Email de confirmation envoyé à " + toEmail);
        } catch (MessagingException e) {
            System.out.println("Erreur envoi confirmation : " + e.getMessage());
        }
    }

    // Méthode 2 : Email de réinitialisation de mot de passe
    public void sendPasswordReset(String toEmail, String clientNom, String newPassword) {
        try {
            Session session = buildSession();
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject("Réinitialisation de votre mot de passe - Art Gallery");
            msg.setText(
                    "Bonjour " + clientNom + ",\n\n" +
                            "Votre nouveau mot de passe est : " + newPassword + "\n\n" +
                            "Veuillez le changer après connexion.\n" +
                            "Art Gallery"
            );
            Transport.send(msg);
            System.out.println("Email de réinitialisation envoyé à " + toEmail);
        } catch (MessagingException e) {
            System.out.println("Erreur envoi réinitialisation : " + e.getMessage());
        }
    }
}
