package info.customer;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.CorporateCustomer;
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
        return databaseHandler.selectCustomers();
    }
    
    public Customer selectCustomer(int id) {
        return databaseHandler.selectCustomer(id);
    }
    
    public boolean updateCustomer(Customer customer) {
        return databaseHandler.updateCustomer(customer);
    }
    
    public boolean fixCustomer(Customer customer) {
        Customer customerDatabase = selectCustomer(customer.getCustomerId());
        if(customer instanceof PrivateCustomer) {
            PrivateCustomer privateCustomer = (PrivateCustomer) customer;
            PrivateCustomer privateCustomerDatabase = (PrivateCustomer) customerDatabase;
            if(privateCustomer.getFirstName().equals("")) {
                privateCustomer.setFirstName(privateCustomerDatabase.getFirstName());
            }if(privateCustomer.getLastName().equals("")) {
                privateCustomer.setLastName(privateCustomerDatabase.getLastName());
            }
        }else if(customer instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
            CorporateCustomer corporateCustomerDatabase = (CorporateCustomer) customerDatabase;
            if(corporateCustomer.getCompanyName().equals("")) {
                corporateCustomer.setCompanyName(corporateCustomerDatabase.getCompanyName());
            }
        }if(customer.getAddress().equals("")){
            customer.setAddress(customerDatabase.getAddress());
        }if(customer.getEmail().equals("")){
            customer.setEmail(customerDatabase.getEmail());
        }if(customer.getPhoneNumber().equals("")){
            customer.setPhoneNumber(customerDatabase.getPhoneNumber());
        }if(customer.getCity().equals("")){
            customer.setCity(customerDatabase.getCity());
        }
        return updateCustomer(customer);
    }
}