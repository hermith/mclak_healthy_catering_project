/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import database.DatabaseHandler;
import java.sql.Date;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;
import shopping.product.SingleProduct;

/**
 *
 * @author Karl
 */
@Named
@ApplicationScoped
public class InfoBean {

    @Inject
    private DatabaseHandler db;
    private ArrayList<Order> activeOrders = new ArrayList<Order>();
    private Customer selectedCustomer;
    private Order selectedOrder;
    private boolean detailOrder;

    public InfoBean() {
        ArrayList<Product> temp = new ArrayList<Product>();
        temp.add(new SingleProduct(123, "Yup", "Good", 130, 130));
        temp.add(new SingleProduct(123, "Yup", "Good", 130, 130));
        temp.add(new SingleProduct(123, "Yup", "Good", 130, 130));
        temp.add(new SingleProduct(123, "Yup", "Good", 130, 130));
        temp.add(new SingleProduct(123, "Yup", "Good", 130, 130));

        activeOrders.add(new Order(1, 666, temp, new Date(2013, 04, 17), new Date(2013, 04, 18), new Date(2013, 04, 19)));
    }

    public ArrayList<Order> getActiveOrders() {
        return activeOrders;
    }

    public void lookUpOrder(int orderID) {
        // TODO: Get customer from DB
        System.out.println("" + orderID);
        selectedOrder = activeOrders.get(0);
        selectedCustomer = new PrivateCustomer(123456, "møkje@penge.no", "Rikbotnfjord", "99 99 33 33", 5670, "Okidokiland", "Langnavnesen", "Ivar");
        detailOrder = true;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public boolean isCustomerOrder() {
        return detailOrder;
    }

    public void closeDetailedInfo() {
        detailOrder = false;
    }
}