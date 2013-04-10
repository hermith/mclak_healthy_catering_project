/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 *
 * @author aleksalr
 */
public class User {

    String username;
    String password;
    String role_id;

    /**
     * Constructor for creating user object using login information
     *
     * @param username
     * @param password
     * @param role_id
     */
    public User(String username) {
        this.username = username;
        this.password = null;
        this.role_id = null;
    }
}
