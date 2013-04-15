package user;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class UserBean implements Serializable {

    User user;

    public UserBean() {
        this.user = new User();
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

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Attempting to log in user {0}.", user);
            request.login(user.getUsername(), user.getPassword());
            user.setPassword(null); // TODO Heller hent en bruker fra databasen slik at brukerrolle også blir tatt med.
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "User {0} logged in.", user);
            // TODO Be shopping bean om å hente ned info fra databasen om et evt. kundeobjekt som er knyttet til denne brukeren.
        } catch (ServletException ex1) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, "Failed to log in user " + user + ".", ex1);
        } catch (NullPointerException ex2) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, "Failed to log in user due to nullpointer exception!", ex2);
        }
        return "";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Attempting to log out {0}.", user);
            request.logout();
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
}