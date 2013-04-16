/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import database.DatabaseHandler;
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
    
    public UserHandler(){
        
    }
    
    public boolean registerPrivateUser(User user){
        db.registerNewUser(user);
        return false;
    }
    
    public boolean registerCorporateUser(User user){
        db.registerNewUser(user);
        return false;
    }
    
}
