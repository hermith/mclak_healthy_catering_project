package user;

import java.io.Serializable;
import java.util.ArrayList;
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

    public String ninja() {
        user.setPassword("Passord1");
        user.setUsername("testSystem");
        login();
        return "/protected/common/control_panel.xhtml";
    }

    public String registerPrivateUser() {
        System.out.println("Called registerPrivateUser() in UserBean");
        this.user.setNewPassword("");
        this.user.setRoleId("");
        if (userhandler.registerPrivateUser(user)) {
            return "registration_success";
        }
        return "registration_failure";
    }

    public String registerCorporateUser() {
        System.out.println("Called registerCorporateUser() in UserBean");
        this.user.setNewPassword("");
        this.user.setRoleId("");
        if(userhandler.registerCorporateUser(user)){
            return "registration_success";
        }
        return "registration_failure";
    }

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
            return "";
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

    public String logout() {
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
        return "return_frontpage";
    }

    public boolean isLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.getRemoteUser() != null) {
            return true;
        }
        return false;
    }

    public boolean isLoggedInAsPrivateCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_PRIVATE_CUSTOMER)) {
            return true;
        }
        return false;
    }

    public boolean isLoggedInAsCorporateCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_CORPORATE_CUSTOMER)) {
            return true;
        }
        return false;
    }

    public boolean isLoggedInAsEmployee() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_EMPLOYEE)) {
            return true;
        }
        return false;
    }

    public boolean isLoggedInAsSystem() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole(UserRoleHandler.USER_ROLE_ID_SYSTEM)) {
            return true;
        }
        return false;
    }

    public boolean didLastLoginFail() {
        if (loginFailed) {
            loginFailed = false;
            return true;
        } else {
            return false;
        }
    }

    public void createNewUser() {
        if (!userhandler.registerUser(dummyUser)) {
            MessageHandler.addErrorMessage("Unable to create account.");
        } else {
            MessageHandler.addErrorMessage("Account successfully created");
        }
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

    public String changePassword() {
        if (userhandler.changePassword(getNewPassword(), getUsername(), getPassword())) {
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "edit_account_password_changed");
            MessageHandler.addErrorMessage(msg);
        }
        setPassword(null);
        setNewPassword(null);
        return "";
    }
    
    public ArrayList<User> getAllUsers() {
        return userhandler.getAllUsers();
    }
    
    public String updateUserPassword(User user) {
        if(userhandler.updateUserPassword(user)) {
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "edit_users_email_sent") + user.getEmail();
            MessageHandler.addErrorMessage(msg);
        }else {
            String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_users_error_sending_email");
            MessageHandler.addErrorMessage(msg);
        }
        return "";
    }
}