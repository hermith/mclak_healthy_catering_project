/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import database.DatabaseHandler;
import java.sql.Date;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;

/**
 *
 * @author Karl
 */
@Named
@ApplicationScoped
public class InfoBean {
    
    @Inject
    private DatabaseHandler db;
    private ArrayList<Order> activeOrders = new ArrayList<Order>();
    private Customer selectedCustomer;
    
    private boolean detailCustomer;

    public InfoBean() {
        // Dunno ka d her va, men ga bare rød squiggly... Christer
        //activeOrders.add(new Order(1, new int[]{1, 2, 3}, new Date(2013, 04, 17), null));
        //activeOrders.add(new Order(2, new int[]{2, 3, 4}, new Date(2013, 04, 17), null));
        //activeOrders.add(new Order(3, new int[]{2, 5, 7}, new Date(2013, 04, 17), null));
    }

    public ArrayList<Order> getActiveOrders() {
        return activeOrders;
    }

    public void lookUpCustomer(int customerId) {
        // TODO: Get customer from DB
        if (customerId == 1) {
            selectedCustomer = new PrivateCustomer(1, "@", "heime", "99", 7030, "Trond", "Karl", "Overå");
        } else if (customerId == 2) {
            selectedCustomer = new PrivateCustomer(1, "@", "deime", "99", 7030, "asdasda", "Kasdsaarl", "asddas");
        } else {
            selectedCustomer = new PrivateCustomer(1, "@", "heime", "99", 13213, "sadsadsa", "sadsadsad", "asdsadsa");
        }
        
        detailCustomer = true;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }
    
    public boolean isCustomerDetails() {
        return detailCustomer;
    }
    
    public void closeDetailedInfo() {
        detailCustomer = false;
    }
}