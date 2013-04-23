package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * @author colsen91
 */
public class OrderHandler {

    @Inject
    DatabaseHandler db;

    /*public ArrayList<Order> getActiveOrders() {
        return db.selectOrders(true);
    }

    public ArrayList<Order> getOrderHistory() {
        return db.selectOrders(false);
    }*/
    
    
}