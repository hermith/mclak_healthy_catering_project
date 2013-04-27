package info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import shopping.product.Product;

/**
 *
 * Storage object, used to store orders, contains information about delivery
 * date, to be delivered, an ArrayList of customers as well as well as a
 * customer ID.
 *
 * @author Karl, Linn
 */
public class Order implements Comparable<Order> {

    private int orderID;
    private int customerID;
    private ArrayList<Product> products;
    private Timestamp placedDate;
    private Timestamp deliveryDate;
    private Timestamp deliveredDate;
    private boolean delivery;
    private boolean active;
    private boolean prepared;

    /**
     * Standard constructor, initiates all the relevant fields to be set later
     * with the set methods.
     */
    public Order() {
        orderID = -1;
        customerID = -1;
        products = null;
        deliveredDate = null;
        deliveryDate = new Timestamp(new Date().getTime());
        delivery = false;
        active = false;
        prepared = false;
    }

    /**
     * Constructor for initiating object with data.
     *
     * @param orderID ID of the order
     * @param customerID ID of the customer of the order.
     * @param products ArrayList of products on the order
     * @param placedDate Order placed date
     * @param deliveryDate To be delivered date
     * @param deliveredDate Was delievered date
     * @param delivery Is delievered
     */
    public Order(int orderID, int customerID, ArrayList<Product> products, Timestamp placedDate, Timestamp deliveryDate, Timestamp deliveredDate, boolean delivery) {
        this.orderID = orderID;
        this.products = products;
        this.customerID = customerID;
        this.placedDate = placedDate;
        this.deliveryDate = deliveryDate;
        this.deliveredDate = deliveredDate;
        this.delivery = delivery;
        active = false;
        prepared = false;
    }

    /**
     *
     * Returns the number of products in this order.
     *
     * @return Size of the product array
     */
    public int numberOfProducts() {
        return products.size();
    }

    /**
     * Gets the total price of all the products in this order.
     *
     * @return Total price of products
     */
    public double getPrice() {
        double total = 0;
        for (Product prod : products) {
            total += prod.getPrice();
        }
        return total;
    }

    /**
     * Gets all the IDs of the products as a string, separated with ", ", but
     * grouped by id with count.
     *
     * @return All product IDs
     */
    public String getProductIds() {
        HashMap<String, Integer> keys = new HashMap<String, Integer>();
        for (Product p : products) {
            String id = "" + p.getId();
            if (keys.containsKey(id)) {
                keys.put(id, keys.get(id) + 1);
            } else {
                keys.put(id, 1);
            }
        }

        StringBuilder b = new StringBuilder();
        for (String s : keys.keySet()) {
            b.append(keys.get(s)).append((char) 215).append(s).append(", ");
        }
        b.delete(b.length() - 2, b.length());
        return b.toString();
    }

    /**
     * Gets all the IDs of the products as a string, separated with ", ", but
     * grouped by id with count.
     *
     * @return All product IDs
     */
    public String getProductIdsWithCommaSeparator() {
        StringBuilder b = new StringBuilder();
        for (Product p : products) {
            b.append(p.getId()).append(", ");
        }
        b.delete(b.length() - 2, b.length());
        return b.toString();
    }

    /**
     * Gets all the IDs of the products as an array
     *
     * @return All product IDs
     */
    public int[] getProductIdsArray() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (Product p : products) {
            ints.add(p.getId());
        }
        int[] ids = new int[ints.size()];

        for (int i : ids) {
            ints.add(i);
        }

        return ids;
    }

    /**
     * Returns whether or not the order is delivered.
     *
     * @return Order delivered
     */
    public boolean isDelivered() {
        if (deliveredDate == null) {
            return false;
        }
        return true;
    }

    /**
     * Converts an java.util.Date object to a more humanly readable String. For
     * use in DataTables and such.
     *
     * @param date util.Date to convert
     * @return Friendly formatted String
     */
    public String convertDateToString(Timestamp date) {
        if (date != null) {
            GregorianCalendar greg = new GregorianCalendar();
            greg.setTimeInMillis(date.getTime());
            String hour = (greg.get(GregorianCalendar.HOUR_OF_DAY)<10)?"0"+(greg.get(GregorianCalendar.HOUR_OF_DAY)):""+(greg.get(GregorianCalendar.HOUR_OF_DAY));
            String minute = (greg.get(GregorianCalendar.MINUTE)<10)?"0"+(greg.get(GregorianCalendar.MINUTE)):""+(greg.get(GregorianCalendar.MINUTE));
            String month = (greg.get(GregorianCalendar.MONTH) + 1)<10?"0"+(greg.get(GregorianCalendar.MONTH) + 1):""+(greg.get(GregorianCalendar.MONTH) + 1);
            String dateout = (greg.get(GregorianCalendar.DATE)<10)?"0"+greg.get(GregorianCalendar.DATE):""+greg.get(GregorianCalendar.DATE);
            return dateout
                    + "." + month
                    + "." + greg.get(GregorianCalendar.YEAR)
                    + " " + hour
                    + ":" + minute;
        } else {
            return "Not delivered";
        }
    }

    //GETTERS AND SETTERS BELOW
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDeliveryDateAsString() {
        return convertDateToString(deliveryDate);
    }

    public void setDeliveryDate(Timestamp date) {
        this.deliveryDate = date;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveredDate(Timestamp date) {
        this.deliveredDate = date;
    }

    public Timestamp getDeliveredDate() {
        return deliveredDate;
    }

    public void setPlacedDate(Timestamp date) {
        this.placedDate = date;
    }

    public Timestamp getPlacedDate() {
        return placedDate;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", customerID=" + customerID + ", products=" + products + ", placedDate=" + placedDate + ", deliveryDate=" + deliveryDate + ", deliveredDate=" + deliveredDate + ", delivery=" + delivery + ", active=" + active + ", prepared=" + prepared + '}';
    }

    @Override
    public int compareTo(Order o) {
        if (this.deliveryDate.before(o.getDeliveryDate())) {
            return -1;
        } else if (this.deliveryDate.equals(o.getDeliveredDate())) {
            return 0;
        } else {
            return 1;
        }
    }
}