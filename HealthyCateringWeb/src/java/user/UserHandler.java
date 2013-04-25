package user;

import database.DatabaseHandler;
import email.EmailHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import locale.MessageHandler;
import locale.MessageType;
import shopping.ShoppingBean;

/**
 *
 * @author aleksalr, Linn
 */
@ApplicationScoped
public class UserHandler {

    @Inject
    DatabaseHandler db;
    @Inject
    EmailHandler email;

    public UserHandler() {
    }

    public boolean registerPrivateUser(User user) {
        if (db.insertUser(user)) {
            FacesContext context = FacesContext.getCurrentInstance();
            ((ShoppingBean) context.getApplication().evaluateExpressionGet(context, "#{shoppingBean}", ShoppingBean.class)).registerPrivateCustomer(user.getUsername());
            return true;
        }
        return false;
    }

    public boolean registerCorporateUser(User user) {
        System.out.println("Got this user: " + user.getUsername() + " - " + user.getPassword() + " - "
                + user.getEmail() + " - " + user.getNewPassword() + " - " + user.getRoleId());
        return (db.insertUser(user));
    }

    public boolean registerUser(User user) {
        try {
            String pw = email.generatePassword();
            user.setPassword(pw);
            if (db.insertUser(user)) {
                email.sendGeneratedPassword(user.getEmail(), pw);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * Updates user's password in the database, and sends the password on email
     * to the user.
     *
     * @param user
     * @return if password was updated and sent to email
     */
    public boolean updateUserPassword(User user) {
        try {
            String pw = email.generatePassword();
            user.setPassword(pw);
            if (db.updateUserPassword(pw, user.getUsername())) {
                email.sendGeneratedPassword(user.getEmail(), pw);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public User getUser(String userName) {
        //TODO Sjekk noko
        return db.selectUser(userName);
    }

    /**
     * Changes current users password in the database. Displays appropriate
     * messages to the user if something went wrong.
     *
     * @param password
     * @param username
     * @param oldPassword
     * @return password changed (true/false)
     */
    public boolean changePassword(String password, String username, String oldPassword) {
        if (db.selectUserPassword(username).equals(oldPassword)) {
            if (!db.updateUserPassword(password, username)) {
                String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_account_password_not_changed");
                MessageHandler.addErrorMessage(msg);
                return false;
            }
        } else {
            String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_account_old_password_invalid");
            MessageHandler.addErrorMessage(msg);
            return false;
        }
        return true;
    }

    public ArrayList<User> getAllUsers() {
        return db.selectUsers();
    }

    /**
     * Checks whether or not the given username corresponds with the stored
     * email for that user, if it does, it will generate a new password and send
     * it to the user.
     *
     * @param email Input email
     * @param username Input username
     * @return If password has been generated and sent.
     */
    public boolean generateNewPasswordUser(String email, String username) {
        User user = db.selectUser(username);
        if (user.getEmail().equals(email)) {
            String pw = this.email.generatePassword();
            if (db.updateUserPassword(pw, user.getUsername())) {
                this.email.sendGeneratedPassword(user.getEmail(), pw);
            }
            return true;
        } else {
            return false;
        }
    }
}
