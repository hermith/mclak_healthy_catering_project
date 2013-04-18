/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import database.DatabaseHandler;
import email.EmailHandler;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import locale.MessageHandler;
import locale.MessageType;

/**
 *
 * @author aleksalr
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
        return (db.insertUser(user));
    }

    public boolean registerCorporateUser(User user) {
        //return (db.insertUser(user));
        return true;
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

    public User getUser(String userName) {
        //TODO Sjekk noko
        return db.selectUser(userName);
    }

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
}
