package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;

/**
 * @author colsen91
 */
@ApplicationScoped
public class CustomerHandler {
    @Inject
    private DatabaseHandler databaseHandler;
    
    public ArrayList<Customer> getAllCustomers() {
        //return databaseHandler.selectCustomers();
        
        //test
        ArrayList<Customer> all = new ArrayList<Customer>();
        all.add(new PrivateCustomer(1, "linnkri", "address", "phone", 1212, "by","first","last"));
        //test
        return all;
    }
}