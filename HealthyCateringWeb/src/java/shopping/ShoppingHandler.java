package shopping;

import database.DatabaseHandler;
import info.Order;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.Customer;
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
}