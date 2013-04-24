package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author colsen91
 */
@ApplicationScoped
public class StatisticsHandler {

    private ArrayList<Order> orders;
    @Inject
    DatabaseHandler dbhandler;
    
    public StatisticsHandler(){
        this.orders = new ArrayList<Order>();
    }
    
    public double getTotalPriceAllOrders(){
        double total = 0;
        for(Order order : orders){
            total += order.getPrice();
        }
        return total;
    }
    
    public int getNumOfOrders(){
        ArrayList<Order> orders = dbhandler.selectOrders();
        return orders.size();
    }
}