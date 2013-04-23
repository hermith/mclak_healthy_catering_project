package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * @author colsen91
 */
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
}