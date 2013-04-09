package user;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author linnk
 */
@Named
@SessionScoped
public class UserBean implements Serializable{
    String username="";
    String password="";
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(username, password);
        } catch (ServletException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {        
            request.logout();
        } catch (ServletException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    /**
     * MÅ LAGE METODE FOR ALLE BRUKERE NB NB NB NB NB
     * @return 
     */
    public boolean isLoggedIn() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObject = context.getRequest();
        if (requestObject instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) requestObject;
            System.out.println(request.getRemoteUser());
            if (request.getRemoteUser() != null) {
                System.out.println("Is hear");
                return true;
            }
        }
        return false;
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
}
