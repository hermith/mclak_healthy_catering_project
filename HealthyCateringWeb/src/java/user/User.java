package user;

/**
 * The User class is used for storing information about a user.
 *
 * @author Aleksander L. Rasch
 */
public class User implements Comparable<User> {

    private String username;
    private String password;
    private String newPassword;
    private String email;
    private String roleId;

    /**
     * Standard constructor which initiates all object variables to null.
     */
    public User() {
        this.username = null;
        this.password = null;
        this.roleId = null;
        this.email = null;
        this.newPassword = null;
    }

    /**
     * Constructor for catching a user object from the database upon login.
     *
     * @param username The username to be stored.
     * @param roleId The role id to be stored.
     * @param email The email to be stored.
     */
    public User(String username, String roleId, String email) {
        this.username = username;
        this.roleId = roleId;
        this.password = null;
        this.email = email;
        this.newPassword = null;
    }

    /**
     * Return the username variable.
     *
     * @return The stored username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username variable to a new given value.
     *
     * @param username The new username to be stored.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Return the password variable.
     *
     * @return The stored password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password variable to a new given value.
     *
     * @param password The new password to be stored.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the roleId variable.
     *
     * @return The stored roleId.
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Set the roleId variable to a new given value.
     *
     * @param roleId The new roleId to be stored.
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * Return the email variable.
     *
     * @return The stored email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email variable to a new given value.
     *
     * @param email The new email to be stored.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Return the newPassword variable.
     *
     * @return The stored newPassword.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set the newPassword variable to a new given value.
     *
     * @param newPassword The new newPassword to be stored.
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", email=" + email + ", roleId=" + roleId + '}';
    }

    @Override
    public int compareTo(User o) {
        if (this.username.compareTo(o.username) < 0) {
            return -1;
        } else if (this.username.equals(o.username)) {
            return 0;
        } else {
            return 1;
        }
    }
}