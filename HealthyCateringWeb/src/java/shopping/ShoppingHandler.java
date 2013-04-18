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
    
    public boolean saveChangesAccount(Customer customer) {
        return database.updateCustomer(customer);
    }
    
    public boolean fixPrivateCustomer(PrivateCustomer customer, String email, String address, String phoneNumber, int zipCode, String city, String firstName, String lastName) {
        if(!email.equals("")){
            customer.setEmail(email);
        }if(!address.equals("")){
            customer.setAddress(address);
        }if(!phoneNumber.equals("")){
            customer.setPhoneNumber(phoneNumber);
        }if(zipCode != 0){
            customer.setZipCode(zipCode);
        }if(!city.equals("")){
            customer.setCity(city);
        }if(!firstName.equals("")){
            customer.setFirstName(firstName);
        }if(!lastName.equals("")){
            customer.setLastName(lastName);
        }
        return saveChangesAccount(customer);
    }
    
    public boolean fixCorporateCustomer(CorporateCustomer customer, String email, String address, String phoneNumber, int zipCode, String city, String companyName) {
        if(!email.equals("")){
            customer.setEmail(email);
        }if(!address.equals("")){
            customer.setAddress(address);
        }if(!phoneNumber.equals("")){
            customer.setPhoneNumber(phoneNumber);
        }if(zipCode != 0){
            customer.setZipCode(zipCode);
        }if(!city.equals("")){
            customer.setCity(city);
        }if(!companyName.equals("")){
            customer.setCompanyName(companyName);
        }
        return saveChangesAccount(customer);
    }
}