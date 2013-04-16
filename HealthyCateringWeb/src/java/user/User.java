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

    private String username;
    private String password;
    private String roleId;

    public User() {
        this.username = null;
        this.password = null;
        this.roleId = null;
    }

    /**
     * Constructor for catching a user object from the database upon login.
     *
     * @param username
     * @param roleId
     */
    public User(String username, String roleId) {
        this.username = username;
        this.roleId = roleId;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", roleId=" + roleId + '}';
    }
}