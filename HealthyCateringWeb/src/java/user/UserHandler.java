/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import database.DatabaseHandler;
import email.EmailHandler;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
            String pw = email.sendGeneratedPassword(user.getEmail());
            user.setPassword(pw);
            if (!db.insertUser(user)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
