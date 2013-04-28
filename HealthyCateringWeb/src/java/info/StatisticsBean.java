package info;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Used to display statistics.
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

    /**
     * Gets the number of registered orders. Includes both delivered and
     * undelivered.
     *
     * @return Total number of orders.
     */
    public int getNumOfOrders() {
        return sthandler.getNumOfOrders();
    }

    /**
     * Gets the running total of all orders ever registered. Includes both
     * delivered and undelivered orders.
     *
     * @return Total price for all orders.
     */
    public String getTotalPriceAllOrders() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(sthandler.getTotalPriceAllOrders());
    }

    /**
     * Gets the price of the most expensive order registered.
     *
     * @return Highest order price registered.
     */
    public String getHighestOrderPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(sthandler.getHighestOrder());
    }

    /**
     * Gets the price of the least expensive order registered.
     *
     * @return Lowest order price registered.
     */
    public String getLowestOrderPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(sthandler.getLowestOrder());
    }

    /**
     * Gets the arithmetic mean price of all orders registered.
     *
     * @return Average price of all orders.
     */
    public String getAverageOrderPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(sthandler.getAverageOrderPrice());
    }

    /**
     * Gets the standard deviation for all orders.
     *
     * @return Standard deviation
     */
    public String getStandardDeviation() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(sthandler.getStandardDeviation());
    }

    /**
     * Gets the ID's and price of every order registered as a string.
     *
     * @return String containing ID's and price of every order registered.
     */
    public String getAllOrdersAsString() {
        return sthandler.getAllOrdersAsString();
    }

    /**
     * Gets product ID's along with how many times each product has been
     * ordered.
     *
     * @return String containing each product ID and frequency of order.
     */
    public String getProductFrequency() {
        return sthandler.getProductFrequency();
    }
}
