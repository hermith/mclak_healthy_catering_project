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

    private ArrayList<Order> allorders;
    @Inject
    DatabaseHandler dbhandler;

    public StatisticsHandler() {
        this.allorders = new ArrayList<Order>();
    }

    public double getTotalPriceAllOrders() {
        double total = 0;
        for (Order order : allorders) {
            total += order.getPrice();
        }
        return total;
    }

    public int getNumOfOrders() {
        allorders = dbhandler.selectOrders();
        return allorders.size();
    }

    public double getHighestOrder() {
        allorders = dbhandler.selectOrders();
        double currentHigh = 0;
        for (Order o : allorders) {
            if (o.getPrice() > currentHigh) {
                currentHigh = o.getPrice();
            }
        }

        return currentHigh;
    }

    public double getLowestOrder() {
        allorders = dbhandler.selectOrders();
        double currentLow = allorders.get(0).getPrice();
        for (Order o : allorders) {
            if (o.getPrice() < currentLow) {
                currentLow = o.getPrice();
            }
        }

        return currentLow;
    }

    public double getAverageOrderPrice() {
        allorders = dbhandler.selectOrders();
        double sum = 0;
        for (Order o : allorders) {
            sum += o.getPrice();
        }
        double average = sum / allorders.size();
        return average;
    }

    public double getStandardDeviation() {
        allorders = dbhandler.selectOrders();
        double sum = 0;
        double avg = getAverageOrderPrice();
        for (Order o : allorders) {
            sum += Math.pow((avg - o.getPrice()), 2);
        }
        double dev = sum / allorders.size();
        double stdDev = Math.sqrt(dev);
        return stdDev;
    }

    public String getAllOrdersAsString() {
        allorders = dbhandler.selectOrders();
        String so = "";

        for (int i = 0; i < allorders.size(); i++) {

            so += allorders.get(i).getOrderID();
            so += " ";
            so += allorders.get(i).getPrice();
            so += " ";
        }
        
        return so;


    }
}