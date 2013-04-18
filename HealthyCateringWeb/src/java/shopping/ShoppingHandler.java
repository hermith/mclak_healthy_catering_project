package shopping;

import database.DatabaseHandler;
import info.Order;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;

/**
 * @author colsen91
 */
@ApplicationScoped
public class ShoppingHandler implements Serializable{

    @Inject
    private DatabaseHandler database;
    private ArrayList<Product> products;
    
    public ShoppingHandler() {
        products = new ArrayList<Product>();
    }

    public boolean insertOrder(Order order) {
        //return database.insertOrder(order);
        return false;
    }

    public ArrayList<Product> getMenu() {
        if(database.hasProductsTableChanged()){
            products = database.selectProducts();
            database.setProductsTableChanged(false);
        }
        return products;
    }
    
    public Customer getCustomer(String username) {
        return database.selectCustomer(username);
    }
    
    public boolean updateCustomer(Customer customer) {
        return database.updateCustomer(customer);
    }
    
    public boolean fixCustomer(Customer customer, Customer newCustomer) {
        if(customer instanceof PrivateCustomer) {
            PrivateCustomer privateCustomer = (PrivateCustomer) newCustomer;
            PrivateCustomer privateCustomerDatabase = (PrivateCustomer) customer;
            if(privateCustomer.getFirstName().equals("")) {
                privateCustomer.setFirstName(privateCustomerDatabase.getFirstName());
            }if(privateCustomer.getLastName().equals("")) {
                privateCustomer.setLastName(privateCustomerDatabase.getLastName());
            }
        }else if(customer instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) newCustomer;
            CorporateCustomer corporateCustomerDatabase = (CorporateCustomer) customer;
            if(corporateCustomer.getCompanyName().equals("")) {
                corporateCustomer.setCompanyName(corporateCustomerDatabase.getCompanyName());
            }
        }if(newCustomer.getAddress().equals("")){
            newCustomer.setAddress(customer.getAddress());
        }if(newCustomer.getEmail().equals("")){
            newCustomer.setEmail(customer.getEmail());
        }if(newCustomer.getPhoneNumber().equals("")){
            newCustomer.setPhoneNumber(customer.getPhoneNumber());
        }if(newCustomer.getCity().equals("")){
            newCustomer.setCity(customer.getCity());
        }
        return updateCustomer(newCustomer);
    }
}