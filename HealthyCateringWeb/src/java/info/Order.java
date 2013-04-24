/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import shopping.product.Product;

/**
 *
 * @author Karl
 */
public class Order {

    private int orderID;
    private int customerID;
    private ArrayList<Product> products;
    private Date placedDate;
    private Date deliveryDate;
    private Date deliveredDate;
    public boolean delivery;

    public Order() {
        orderID = -1;
        customerID = -1;
        products = null;
        deliveredDate = null;
        deliveryDate = null;
        delivery = false;
    }

    public Order(int orderID, int customerID, ArrayList<Product> products, Date placedDate, Date deliveryDate, Date deliveredDate, boolean delivery) {
        this.orderID = orderID;
        this.products = products;
        this.customerID = customerID;
        this.placedDate = placedDate;
        this.deliveryDate = deliveryDate;
        this.deliveredDate = deliveredDate;
        this.delivery = delivery;
    }

    public int numberOfProducts() {
        return products.size();
    }

    public double getPrice() {
        double total = 0;
        for (Product prod : products) {
            total += prod.getPrice();
        }
        return total;
    }

    public String getProductIds() {
        StringBuilder b = new StringBuilder();
        for (Product p : products) {
            b.append(p.getId()).append(", ");
        }
        b.delete(b.length() - 2, b.length());
        return b.toString();
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDeliveryDateAsString() {
        return convertDateToString(deliveryDate);
    }

    public boolean isDelivered() {
        if (deliveredDate == null) {
            return false;
        }

        return true;
    }

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
