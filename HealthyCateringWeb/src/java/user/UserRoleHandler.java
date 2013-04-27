package user;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * UserRoleHandler is used to show user role in a human readable manner and in the correct language.
 *
 * @author Christer Olsen
 */
@Named
@ApplicationScoped
public class UserRoleHandler {

    public final static String USER_ROLE_ID_PRIVATE_CUSTOMER = "private_customer";
    public final static String USER_ROLE_ID_CORPORATE_CUSTOMER = "corporate_customer";
    public final static String USER_ROLE_ID_EMPLOYEE = "employee";
    public final static String USER_ROLE_ID_SYSTEM = "system";
    public final static String USER_ROLE_ID_NOT_FOUND = "###";

    /**
     * Find and return the correct locale representation of the user role.
     *
     * @param userRoleId
     * @return locale String
     */
    public String getUserRoleNameById(String userRoleId) {
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getApplication().evaluateExpressionGet(context, "#{texts.user_role" + userRoleId + "}", String.class);
        if (name == null || name.equals("")) {
            Logger.getLogger(UserRoleHandler.class.getName()).log(Level.SEVERE, "Failed to find category name for category id {0}!", userRoleId);
            return context.getApplication().evaluateExpressionGet(context, "#{texts." + USER_ROLE_ID_NOT_FOUND + "}", String.class);
        }
        return name;
    }

    /*
     * Getters and setters below
     */
    public synchronized String getUserRoleIdPrivateCustomer() {
        return USER_ROLE_ID_PRIVATE_CUSTOMER;
    }

    public synchronized String getUserRoleIdCorporateCustomer() {
        return USER_ROLE_ID_CORPORATE_CUSTOMER;
    }

    public String getUserRoleIdEmployee() {
        return USER_ROLE_ID_EMPLOYEE;
    }

    public String getUserRoleIdSystem() {
        return USER_ROLE_ID_SYSTEM;
    }
}