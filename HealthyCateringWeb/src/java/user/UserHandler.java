package user;

import database.DatabaseHandler;
import email.EmailHandler;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import locale.MessageHandler;
import locale.MessageType;
import shopping.ShoppingBean;

/**
 * UserHandler contains all the logic relating to user management.
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

    /**
     * Registers private user and enters the information in the database.
     *
     * @param user - User object containing the new user's credentials.
     * @return - True if registration was successful. False if an error occured.
     */
    public boolean registerPrivateUser(User user) {
        if (db.insertUser(user)) {
            FacesContext context = FacesContext.getCurrentInstance();
            ((ShoppingBean) context.getApplication().evaluateExpressionGet(context, "#{shoppingBean}", ShoppingBean.class)).registerPrivateCustomer(user.getUsername());
            return true;
        }
        return false;
    }

    /**
     * Registers corporate user and enters the information in the database.
     *
     * @param user - User object containing the new user's credentials.
     * @return - True if registration was successful. False if an error occured.
     */
    public boolean registerCorporateUser(User user) {
        if (db.insertUser(user)) {
            FacesContext context = FacesContext.getCurrentInstance();
            ((ShoppingBean) context.getApplication().evaluateExpressionGet(context, "#{shoppingBean}", ShoppingBean.class)).registerCorporateCustomer(user.getUsername());
            return true;
        }
        return false;
    }

    /**
     * Register a new user. Generates a password and sends it by email to the
     * user.
     *
     * @param user
     * @return Whether the process was successful
     */
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
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, e.toString());
            return false;
        }
    }

    /**
     * Updates user's password in the database, and sends the password by email
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
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, e.toString());
            return false;
        }
    }

    /**
     * Calls selectUser(userName) in DatabaseHandler.java.
     *
     * @param userName
     * @return User with the correct username
     */
    public User getUser(String userName) {
        return db.selectUser(userName);
    }

    /**
     * Changes current users password in the database. Displays appropriate
     * messages to the user if something went wrong.
     *
     * @param password
     * @param username
     * @param oldPassword
     * @return True if password was changed
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

    /**
     * Calls selectUsers() in DatabaseHandler.java.
     *
     * @return Array with all the users in the database.
     */
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
