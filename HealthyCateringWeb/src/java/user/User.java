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
    public User(String username, String password, String role_id) {
        this.username = username;
        this.password = password;
        this.role_id = role_id;
    }

    /**
     * Constructor for catching a user object from the database upon login.
     *
     * @param username
     * @param role_id
     */
    public User(String username, String role_id) {
        this.username = username;
        this.role_id = role_id;
        this.password = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
