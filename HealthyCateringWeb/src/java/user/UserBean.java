package user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import locale.MessageHandler;
import locale.MessageType;
import shopping.ShoppingBean;

/**
 *
 * @author aleksalr, Linn
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    User user;
    boolean loginFailed;
    @Inject
    UserHandler userhandler;
    User dummyUser;

    public UserBean() {
        this.user = new User();
        this.dummyUser = new User();
    }

    //TODO - Remove?
    public String ninja() {
        user.setPassword("Passord1");
        user.setUsername("testSystem");
        login();
        return "/protected/common/control_panel.xhtml";
    }

    /**
     * Registers a new private user based on credentials they've typed.
     *
     * @return - Empty strings for refreshing instead of redirecting.
     */
    public String registerPrivateUser() {
        Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Attempting to register private user.", user);
        this.user.setNewPassword("");
        this.user.setRoleId(UserRoleHandler.USER_ROLE_ID_PRIVATE_CUSTOMER);
        if (userhandler.registerPrivateUser(user)) {
            user = new User();
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "user_reg_success");
            MessageHandler.addErrorMessage(msg);
            return "registration_success";
        }
        user = new User();
        String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "user_reg_fail");
        MessageHandler.addErrorMessage(msg);
        return "registration_failure";
    }

    /**
     * Registers a new corporate user based on credentials they've typed.
     *
     * @return - Empty strings for refreshing instead of redirecting.
     */
    public String registerCorporateUser() {
        Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Attempting to register corporate user.", user);
        this.user.setNewPassword("");
        this.user.setRoleId(UserRoleHandler.USER_ROLE_ID_CORPORATE_CUSTOMER);
        if (userhandler.registerCorporateUser(user)) {
            user = new User();
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "user_reg_success");
            MessageHandler.addErrorMessage(msg);
            return "";
        }
        user = new User();
        String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "user_reg_fail");
        MessageHandler.addErrorMessage(msg);
        return "";
    }

    /**
     * Generates new password for a user if they've forgotten it.
     *
     * User needs to enter valid email and username.
     */
    public void generateNewPasswordUser() {
        if (userhandler.generateNewPasswordUser(user.getEmail(), user.getUsername())) {
            MessageHandler.addErrorMessage("New password sent");
            loginFailed = true;
        } else {
            MessageHandler.addErrorMessage("Username nad password not like");
            loginFailed = true;
        }
    }

    /**
     * Method for logging in with the visitor's entered username/password.
     *
     * @return - String used to redirect visitor to intended page.
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        loginFailed = false;
        if (user.getUsername().trim().isEmpty()) {
            MessageHandler.addErrorMessage(MessageHandler.getLocalizedText(MessageType.ERROR, "login_required_username"));
            loginFailed = true;
        }
        if (user.getPassword().trim().isEmpty()) {
            MessageHandler.addErrorMessage(MessageHandler.getLocalizedText(MessageType.ERROR, "login_required_password"));
            loginFailed = true;
        }
        if (loginFailed) {
            return "";
        }

        try {
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Attempting to log in user {0}.", user);
            request.login(user.getUsername(), user.getPassword());
            user = userhandler.getUser(user.getUsername());
            ((ShoppingBean) context.getApplication().evaluateExpressionGet(context, "#{shoppingBean}", ShoppingBean.class)).initiateCustomer(user.getUsername());
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "User {0} logged in.", user);
            loginFailed = false;
            return "return_frontpage";
            // TODO Be shopping bean om å hente ned info fra databasen om et evt. kundeobjekt som er knyttet til denne brukeren.
        } catch (ServletException ex1) {
            loginFailed = true;
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, "Failed to log in user " + user + ".", ex1);
        } catch (NullPointerException ex2) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, "Failed to log in user due to nullpointer exception!", ex2);
        }

        MessageHandler.addErrorMessage(MessageHandler.getLocalizedText(MessageType.ERROR, "login_wrong_username_or_password"));
        return "";
    }
    
    public void loginMobile() {
        login();
    }

    /**
     * Logout method for non-mobile users.
     *
     * @return - String for redirection to index.xhtml.
     */
    public String logout() {
        logoutLogic();
        return "return_frontpage";
    }

    /**
     * Returns string for proper redirect if logging out on mobile device.
     *
     * @return - String used for redirection.
     */
    public String logoutMobile() {
        logoutLogic();
        return "/touch/";
    }

    /**
     * Logs the user out and resets User object.
     */
    private void logoutLogic() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Attempting to log out {0}.", user);
            request.logout();
            ((ShoppingBean) context.getApplication().evaluateExpressionGet(context, "#{shoppingBean}", ShoppingBean.class)).resetVars();
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "User {0} logged out.", user);
            user = new User();
        } catch (ServletException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, "Failed to log out " + user + ".", ex);
        }
    }

    /**
     * Checks whether or not a user is logged in.
     *
     * @return - True if user is logged in.
     */
    public boolean isLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.getRemoteUser() != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether or not user is logged in as private customer
     *
     * @return - True if logged in as corporate private
     */
    public boolean isLoggedInAsPrivateCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_PRIVATE_CUSTOMER)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether or not user is logged in as corporate customer
     *
     * @return - True if logged in as corporate customer
     */
    public boolean isLoggedInAsCorporateCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_CORPORATE_CUSTOMER)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether or not user is logged in as employee
     *
     * @return - True if logged in as employee
     */
    public boolean isLoggedInAsEmployee() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_EMPLOYEE)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether or not user is logged in as System/Administrator
     *
     * @return - True if logged in as System
     */
    public boolean isLoggedInAsSystem() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_SYSTEM)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether or not the last login attempt failed.
     *
     * @return True if last login failed. False if last login was successful.
     */
    public boolean didLastLoginFail() {
        if (loginFailed) {
            loginFailed = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends the user object to the UserHandler for insertion into Database
     */
    public void createNewUser() {
        if (!userhandler.registerUser(dummyUser)) {
            MessageHandler.addErrorMessage("Unable to create account.");
        } else {
            MessageHandler.addErrorMessage("Account successfully created");
        }
    }

    /**
     * Getters and setters for User object contained in the bean
     */
    public String getUsername() {
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public void setUsername(String username) {
        if (user != null) {
            this.user.setUsername(username);
        }
    }

    public String getPassword() {
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    public void setPassword(String password) {
        if (user != null) {
            this.user.setPassword(password);
        }
    }

    public String getNewPassword() {
        if (user != null) {
            return user.getNewPassword();
        }
        return null;
    }

    public void setNewPassword(String password) {
        if (user != null) {
            this.user.setNewPassword(password);
        }
    }

    public String getRoleId() {
        if (user != null) {
            return user.getRoleId();
        }
        return null;
    }

    public void setRoleId(String roleId) {
        if (user != null) {
            this.user.setRoleId(roleId);
        }
    }

    public void setEmail(String email) {
        if (email != null) {
            this.user.setEmail(email);
        }
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    /**
     *
     * Getters and setters for dummyUser below, used to create new employee and
     * system users.
     *
     */
    public String getDummyUsername() {
        if (dummyUser != null) {
            return dummyUser.getUsername();
        }
        return null;
    }

    public void setDummyUsername(String username) {
        if (dummyUser != null) {
            this.dummyUser.setUsername(username);
        }
    }

    public String getDummyPassword() {
        if (dummyUser != null) {
            return dummyUser.getPassword();
        }
        return null;
    }

    public void setDummyPassword(String password) {
        if (dummyUser != null) {
            this.dummyUser.setPassword(password);
        }
    }

    public String getDummyRoleId() {
        if (dummyUser != null) {
            if (dummyUser.getRoleId() != null) {
                return dummyUser.getRoleId();
            }
        }
        return UserRoleHandler.USER_ROLE_ID_EMPLOYEE;
    }

    public void setDummyRoleId(String roleId) {
        System.out.println("ID: " + roleId);
        if (dummyUser != null) {
            this.dummyUser.setRoleId(roleId);
        }
    }

    public void setDummyEmail(String email) {
        if (email != null) {
            this.dummyUser.setEmail(email);
        }
    }

    public String getDummyEmail() {
        return this.dummyUser.getEmail();
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = userhandler.getAllUsers();
        Collections.sort(users);
        return users;
    }

    /**
     * Calls updateUserPassword(user) in UserHandler.java. Updates user password
     * and sends email to the user.
     *
     * @param user
     */
    public void updateUserPassword(User user) {
        if (userhandler.updateUserPassword(user)) {
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "edit_users_email_sent") + user.getEmail();
            MessageHandler.addErrorMessage(msg);
        } else {
            String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_users_error_sending_email");
            MessageHandler.addErrorMessage(msg);
        }
    }

    /**
     * Calls changePassword() in UserHandler.java. Updates current user
     * password.
     *
     * @return
     */
    public void changePassword() {
        if (userhandler.changePassword(getNewPassword(), getUsername(), getPassword())) {
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "edit_account_password_changed");
            MessageHandler.addErrorMessage(msg);
        }
        setPassword(null);
        setNewPassword(null);
    }
}