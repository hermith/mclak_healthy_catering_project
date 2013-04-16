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

    private int customerID;
    private ArrayList<Product> products;
    private Date deliveryDate;
    private Date deliveredDate;
    public boolean delivery;

    public Order() {
        customerID = -1;
        products = null;
        deliveredDate = null;
        deliveryDate = null;
        delivery = false;
    }
    
    public Order(int customerID, ArrayList<Product> products, Date deliveryDate, Date deliveredDate) {
        this.products = products;
        
        this.customerID = customerID;

        this.deliveryDate = deliveryDate;
        this.deliveredDate = deliveredDate;
    }

    public int numberOfProducts() {
        return products.size();
    }

    public String getProductIds() {
        StringBuilder b = new StringBuilder();
        for (Product p : products)
            b.append(p.getId()).append(", ");
        b.delete(b.length()-2, b.length());
        return b.toString();
    }
    
    public int getCustomerID() {
        return customerID;
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String deliveryDate() {
        return convertDateToString(deliveryDate);
    }

    public boolean isDelivered() {
        if (deliveredDate == null) {
            return false;
        }

        return true;
    }
    
    private String convertDateToString(Date date) {
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTimeInMillis(date.getTime());
        return (greg.get(GregorianCalendar.YEAR)-1900)
                + "." + greg.get(GregorianCalendar.MONTH)
                + "." + greg.get(GregorianCalendar.DATE);
    }
    
    public void setDeliveryDate(Date date) {
        this.deliveryDate = date;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
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
}
