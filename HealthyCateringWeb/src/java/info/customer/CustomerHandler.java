package info.customer;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;

/**
 * CustomerHandler is used to handle logic related to customers. Mainly
 * functions for employees.
 *
 * @author Linn Kristin
 */
@ApplicationScoped
public class CustomerHandler {

    @Inject
    private DatabaseHandler databaseHandler;

    /**
     * Calls selectCustomers() in DatabaseHandler.
     *
     * @return Array with all customers in the database.
     */
    public ArrayList<Customer> getAllCustomers() {
        return databaseHandler.selectCustomers();
    }

    /**
     * Calls selectCustomer(id) in DatabaseHandler.java.
     *
     * @param id
     * @return Customer-object with correct id.
     */
    public Customer selectCustomer(int id) {
        return databaseHandler.selectCustomer(id);
    }

    /**
     * Calls updateCustomer(customer) in DatabaseHandler.java.
     *
     * @param customer
     * @return True if all changes were made and no errors occured.
     */
    public boolean updateCustomer(Customer customer) {
        return databaseHandler.updateCustomer(customer);
    }

    /**
     * Fixes the customer and calls updateCustomer(customer).
     *
     * @param customer
     * @return True if all changes were made and no error occured.
     */
    public boolean fixCustomer(Customer customer) {
        Customer customerDatabase = selectCustomer(customer.getCustomerId());
        if (customer instanceof PrivateCustomer) {
            PrivateCustomer privateCustomer = (PrivateCustomer) customer;
            PrivateCustomer privateCustomerDatabase = (PrivateCustomer) customerDatabase;
            if (privateCustomer.getFirstName().equals("")) {
                privateCustomer.setFirstName(privateCustomerDatabase.getFirstName());
            }
            if (privateCustomer.getLastName().equals("")) {
                privateCustomer.setLastName(privateCustomerDatabase.getLastName());
            }
        } else if (customer instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
            CorporateCustomer corporateCustomerDatabase = (CorporateCustomer) customerDatabase;
            if (corporateCustomer.getCompanyName().equals("")) {
                corporateCustomer.setCompanyName(corporateCustomerDatabase.getCompanyName());
            }
        }
        if (customer.getAddress().equals("")) {
            customer.setAddress(customerDatabase.getAddress());
        }
        if (customer.getEmail().equals("")) {
            customer.setEmail(customerDatabase.getEmail());
        }
        if (customer.getPhoneNumber().equals("")) {
            customer.setPhoneNumber(customerDatabase.getPhoneNumber());
        }
        if (customer.getCity().equals("")) {
            customer.setCity(customerDatabase.getCity());
        }
        return updateCustomer(customer);
    }
}