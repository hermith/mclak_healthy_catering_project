package info;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import shopping.product.Product;

/**
 *
 * Storage object, used to store orders, contains information about delivery
 * date, to be delivered, an ArrayList of customers as well as well as a
 * customer ID.
 *
 * @author Karl, Linn
 */
public class Order {

    private int orderID;
    private int customerID;
    private ArrayList<Product> products;
    private Date placedDate;
    private Date deliveryDate;
    private Date deliveredDate;
    public boolean delivery;

    /**
     * Standard constructor, initiates all the relevant fields to be set later
     * with the set methods.
     */
    public Order() {
        orderID = -1;
        customerID = -1;
        products = null;
        deliveredDate = null;
        deliveryDate = null;
        delivery = false;
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
    public Order(int orderID, int customerID, ArrayList<Product> products, Date placedDate, Date deliveryDate, Date deliveredDate, boolean delivery) {
        this.orderID = orderID;
        this.products = products;
        this.customerID = customerID;
        this.placedDate = placedDate;
        this.deliveryDate = deliveryDate;
        this.deliveredDate = deliveredDate;
        this.delivery = delivery;
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
     * Gets all the IDs of the products as a string, seperated with ", "
     * 
     * @return All product IDs
     */
    public String getProductIds() {
        StringBuilder b = new StringBuilder();
        for (Product p : products) {
            b.append(p.getId()).append(", ");
        }
        b.delete(b.length() - 2, b.length());
        return b.toString();
    }
    
    /**
     * Returns whether or not the order is delivered or not.
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
    public String convertDateToString(Date date) {
        if (date != null) {
            GregorianCalendar greg = new GregorianCalendar();
            greg.setTimeInMillis(date.getTime());
            return (greg.get(GregorianCalendar.YEAR))
                    + "." + greg.get(GregorianCalendar.MONTH)
                    + "." + greg.get(GregorianCalendar.DATE);
        } else {
            return "Not delivered";
        }
    }

    /**
     * GETTERS AND SETTERS BELOW
     */
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDeliveryDateAsString() {
        return convertDateToString(deliveryDate);
    }

    public void setDeliveryDate(Date date) {
        this.deliveryDate = date;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveredDate(Date date) {
        this.deliveredDate = date;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setPlacedDate(Date date) {
        this.placedDate = date;
    }

    public Date getPlacedDate() {
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
}
