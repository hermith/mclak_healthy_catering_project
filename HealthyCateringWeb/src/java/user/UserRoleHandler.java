package user;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Christer Olsen
 */
@Named
@ApplicationScoped
public class UserRoleHandler {
    private final static String ROLE_ID_CUSTOMER = "Customer";
    private final static String ROLE_ID_EMPLOYEE = "Employee";
    private final static String ROLE_ID_SYSTEM = "System";
    
    public synchronized String getRoleIdCustomer() {
        return ROLE_ID_CUSTOMER;
    }
    
    public String getRoleIdEmployee() {
        return ROLE_ID_EMPLOYEE;
    }
    
    public String getRoleIdSystem() {
        return ROLE_ID_SYSTEM;
    }
    
    // TODO
}