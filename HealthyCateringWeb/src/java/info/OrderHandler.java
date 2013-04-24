package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.Customer;

/**
 * @author colsen91
 */

@ApplicationScoped
public class OrderHandler {

    @Inject
    DatabaseHandler db;

    public ArrayList<Order> getActiveOrders() {
        return db.selectUndeliveredOrders();
    }

    public ArrayList<Order> getOrderHistory() {
        return db.selectOrders();
    }

    public Order getOrderFromID(int orderID) {
        return db.selectOrder(orderID);
    }

    public Customer getCustomerFromID(int customerID) {
        return db.selectCustomer(customerID);
    }
    
    public ArrayList<Order> getOrderHistory(int customerId){
        return db.selectOrders(customerId);
    }
    
}