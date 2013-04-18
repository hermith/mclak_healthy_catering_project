/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

import java.util.Properties;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * Use this to send emails to customers. Inject this into handlers to use it.
 * 
 * @author Karl
 */
@ApplicationScoped
public class EmailHandler {

    private static String FROM_ADRESS = "noreply.healthycatering@gmail.com";
    private static String EMAIL_LOGIN = "noreply.healthycatering";
    private static String EMAIL_PASSWORD = "Passord1"; // Probably not a safe way to store this.
    private static String PASSWORD_MAIL_SUBJECT = "Your password for your HealthyCatering account";
    private static String PASSWORD_MAIL_BODY = "This is your generated password:\n\n\t%PW%\n\nAfter you log in you should change your password immediately.\n\nRegards\nHealthyCatering staff.";
    private Properties props;

    public EmailHandler() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    /**
     * Sends an custom email, to format body text use \n (newline) and \t (tablature)
     * 
     * @param recipient
     * @param title
     * @param body
     * @return Success, throws an exception if it's unable to send the email.
     */
    public boolean sendMail(String recipient, String title, String body) {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_LOGIN, EMAIL_PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_ADRESS));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(title);
            message.setText(body);

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            throw new Error("Failed to send email, reason: " + e.getCause());
        }
    }

    /**
     *
     * @param recipient The recipient of the generated password
     * @return The generated password as a String, throws an error if email
     * failed.
     */
    public String sendGeneratedPassword(String recipient) {
        String pw = generatePassword();
        try {
            sendMail(recipient, PASSWORD_MAIL_SUBJECT, PASSWORD_MAIL_BODY.replace("%PW%", pw));
            return pw;
        } catch (Error e) {
            throw e;
        }

    }

    private String generatePassword() {
        // Numbers are 48-57 (0-9)
        // Letters are 65-90 (A-Z)
        // Letters are 97-122 (a-z)
        int PASSWORD_LENGTH = 8;
        Random r = new Random();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            switch (r.nextInt(3)) {
                case 0:
                    b.append((char) (r.nextInt(9) + 48));
                    break;
                case 1:
                    b.append((char) (r.nextInt(25) + 65));
                    break;
                case 2:
                    b.append((char) (r.nextInt(25) + 97));
                    break;
            }
        }

        return b.toString();
    }
}
