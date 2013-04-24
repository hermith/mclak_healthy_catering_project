/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author aleksalr
 */
@Named
@SessionScoped
public class StatisticsBean implements Serializable {

    private ArrayList<Order> activeOrders;
    private ArrayList<Order> orders;
    @Inject
    StatisticsHandler sthandler;

    public StatisticsBean() {
        this.orders = new ArrayList<Order>();
    }

    public ArrayList<Order> getActiveOrders() {
        //TODO Get non-delivered orders from DB
        return activeOrders;
    }

    public ArrayList<Order> getOrderHistory() {
        //TODO Get delived orders from DB
        return activeOrders;
    }

    public int getNumOfOrders() {
        return sthandler.getNumOfOrders();
    }
    
    public double getTotalPriceAllOrders(){
        return sthandler.getTotalPriceAllOrders();
    }
    
    public double getHighestOrderPrice(){
        return sthandler.getHighestOrder();
    }
    
    public double getLowestOrderPrice(){
        return sthandler.getLowestOrder();
    }
    
    public double getAverageOrderPrice(){
        return sthandler.getAverageOrderPrice();
    }
    
    public double getStandardDeviation(){
        return sthandler.getStandardDeviation();
    }
    
    
}
