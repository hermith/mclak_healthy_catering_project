package info;

import database.DatabaseHandler;
import info.product.ProductFreq;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.product.Product;

/**
 * Used to handle logic related to statistics.
 *
 * @author aleksalr
 */
@ApplicationScoped
public class StatisticsHandler {

    private ArrayList<Order> allorders;
    @Inject
    DatabaseHandler dbhandler;

    public StatisticsHandler() {
        this.allorders = new ArrayList<Order>();
    }

    /**
     * Gets the running total of all orders ever registered. Includes both
     * delivered and undelivered orders.
     *
     * @return Total price for all orders.
     */
    public double getTotalPriceAllOrders() {
        double total = 0;
        for (Order order : allorders) {
            total += order.getPrice();
        }
        return total;
    }

    /**
     * Gets the number of orders registered. Includes both delivered and
     * undelivered.
     *
     * @return Total number of orders.
     */
    public int getNumOfOrders() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        return allorders.size();
    }

    /**
     * Gets the price of the most expensive order registered.
     *
     * @return Highest order price registered.
     */
    public double getHighestOrder() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        double currentHigh = 0;
        for (Order o : allorders) {
            if (o.getPrice() > currentHigh) {
                currentHigh = o.getPrice();
            }
        }

        return currentHigh;
    }

    /**
     * Gets the price of the least expensive order registered.
     *
     * @return Lowest order price registered.
     */
    public double getLowestOrder() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        double currentLow = allorders.get(0).getPrice();
        for (Order o : allorders) {
            if (o.getPrice() < currentLow) {
                currentLow = o.getPrice();
            }
        }
        return currentLow;
    }

    /**
     * Gets the arithmetic mean price of all orders registered.
     *
     * @return Average price of all orders.
     */
    public double getAverageOrderPrice() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        double sum = 0;
        for (Order o : allorders) {
            sum += o.getPrice();
        }
        double average = sum / allorders.size();
        return average;
    }

    /**
     * Gets the standard deviation for all orders
     *
     * @return standard deviation
     */
    public double getStandardDeviation() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        double sum = 0;
        double avg = getAverageOrderPrice();
        for (Order o : allorders) {
            sum += Math.pow((avg - o.getPrice()), 2);
        }
        double dev = sum / allorders.size();
        double stdDev = Math.sqrt(dev);
        return stdDev;
    }

    /**
     * Helper method for checking whether an int is contained in an int array
     *
     * @param check - Int to be checked towards an array
     * @param array - The array towards which to check whether or not contains
     * the int
     * @return True if the int is contained in the given array, false if not.
     */
    public boolean isInArray(int check, int[] array) {
        for (int i : array) {
            if (check == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ID's and price of every order registered as a string.
     *
     * @return String containing ID's and price of every order registered.
     */
    public String getAllOrdersAsString() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        String so = "";

        for (int i = 0; i < allorders.size(); i++) {

            so += allorders.get(i).getOrderID();
            so += " ";
            so += allorders.get(i).getPrice();
            so += " ";
        }

        return so;
    }

    /**
     * Gets product ID's along with how many times each product has been
     * ordered.
     *
     * @return String containing each product ID and frequency of order.
     */
    public String getProductFrequency() {
        allorders = filterInactiveOrders(dbhandler.selectOrders());
        ArrayList<ProductFreq> pf = new ArrayList<ProductFreq>();
        String idString;
        String[] idArrayString;
        int[] ids = new int[dbhandler.selectProducts().size() + 1];
        int index;

        for (Order o : allorders) {
            idString = o.getProductIdsWithCommaSeparator();
            idArrayString = idString.split(",");

            for (int i = 0; i < idArrayString.length; i++) {
                idArrayString[i] = idArrayString[i].replaceAll(" ", "");
                System.out.println(idArrayString[i]);
            }

            for (String s : idArrayString) {
                index = Integer.parseInt(s);
                ids[index]++;
            }

        }

        Product product;
        for (int i = 1; i < ids.length; i++) {
            product = dbhandler.selectProduct(i);
            pf.add(new ProductFreq(i, ids[i]));
        }

        String pfAsString = "";
        for (int i = 1; i < pf.size(); i++) {
            if (pf.get(i).getFrequency() != 0) {
                pfAsString += pf.get(i).getId() + " ";
                pfAsString += pf.get(i).getFrequency() + " ";
            }
        }

        return pfAsString;
    }

    /**
     * Method which removes inactive orders from an original ArrayList of
     * Orders.
     *
     * @param orders The orders to filter.
     * @return The initial ArrayList of orders without the inactive ones.
     */
    private ArrayList<Order> filterInactiveOrders(ArrayList<Order> orders) {
        ArrayList<Order> toRemove = new ArrayList<Order>();
        for (Order o : orders) {
            if (!o.isActive()) {
                toRemove.add(o);
            }
        }

        for (Order o : toRemove) {
            orders.remove(o);
        }
        return orders;
    }
}