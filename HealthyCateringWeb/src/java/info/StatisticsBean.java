/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import java.util.ArrayList;

/**
 *
 * @author aleksalr
 */
public class StatisticsBean {
    
    private ArrayList<Order> activeOrders = new ArrayList<Order>();

    public ArrayList<Order> getActiveOrders() {
        //TODO Get non-delivered orders from DB
        return activeOrders;
    }

    public ArrayList<Order> getOrderHistory() {
        //TODO Get delived orders from DB
        return activeOrders;
    }
}
