package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.product.Product;

/**
 * @author aleksalr
 */
@ApplicationScoped
public class StatisticsHandler {

    private ArrayList<Order> allorders;
    private ArrayList<Product> allproducts;
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

    public String getProductsNeverOrdered() {
        allorders = dbhandler.selectOrders();
        ArrayList<Product> notOrdered = dbhandler.selectProducts();
        int[] ids;
        
        
        
        return "";

    }

    public boolean isInArray(int check, int[] array) {
        for (int i : array) {
            if (check == i) {
                return true;
            }
        }
        return false;
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

    public String getProductFrequency() {
        allorders = dbhandler.selectOrders();
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
                pfAsString += pf.get(i).getId()+ " ";
                pfAsString += pf.get(i).getFrequency() + " ";
            }
        }

        return pfAsString;
    }
}