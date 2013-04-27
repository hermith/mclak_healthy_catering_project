package email;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This file is used to send emails from the company email account that is
 * noreply.healthycatering@gmail.com
 *
 * @author Karl Jørgen Overå
 */
@ApplicationScoped
public class EmailHandler {

    private static String FROM_ADRESS = "noreply.healthycatering@gmail.com";
    private static String EMAIL_LOGIN = "noreply.healthycatering";
    private static String PASSWORD_MAIL_SUBJECT = "Your password for your HealthyCatering account";
    private static String PASSWORD_MAIL_BODY = "This is your generated password:\n\n\t%PW%\n\nAfter you log in you should change your password immediately.\n\nRegards\nHealthyCatering staff.";
    private String emailPassword;
    private Properties props;

    /**
     * The contructor initiates the properties required to connect to the gmail
     * smtp server.
     */
    public EmailHandler() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        emailPassword = getPasswordFromFile();
    }

    /**
     * Sends an custom email, to format body text use \n (newline) and \t
     * (tablature)
     *
     * @param recipient The recipient of the email.
     * @param title The title, or subject, of the email.
     * @param body The body, or text, of the email.
     * @return True if the email was sent successfully. Throwns an Error if the
     * email wasn't sent.
     */
    public boolean sendMail(String recipient, String title, String body) {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_LOGIN, emailPassword);
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
     * Method for sending a new password to e given recipient.
     *
     * @param recipient The recipient of the generated password
     * @param pw The generated password.
     * @return The generated password as a String, throws an error if email
     * failed.
     */
    public boolean sendGeneratedPassword(String recipient, String pw) throws Error {
        sendMail(recipient, PASSWORD_MAIL_SUBJECT, PASSWORD_MAIL_BODY.replace("%PW%", pw));
        return true;
    }

    /**
     * Generates a 8-character long password with randomized capital,
     * non-capital and numbers.
     *
     * @return A generated 8-length password
     */
    public String generatePassword() {
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

    /**
     * This gets the password of the gmail account from a local file. This is
     * used to make sure the password is not commited to public records on the
     * VCS (Github).
     *
     * @return The local stored password
     */
    private String getPasswordFromFile() {
        FileInputStream stream;
        System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
        try {
            stream = new FileInputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/")) + "/secret_password");
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            stream.close();
            return Charset.defaultCharset().decode(bb).toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }
}