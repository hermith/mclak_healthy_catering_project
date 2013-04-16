/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import shopping.customer.Customer;
import shopping.product.Product;

/**
 * @author aleksalr
 */
@Named
@SessionScoped
public class ShoppingBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Customer customer;
    private ShoppingCart shoppingCart;

    public ShoppingBean() {
        customer = new Customer();
        shoppingCart = new ShoppingCart();
    }

    public String getEmail() {
        if (customer != null) {
            return customer.getEmail();
        }
        return null;
    }

    public void setEmail(String email) {
        if (customer != null) {
            this.customer.setEmail(email);
        }
    }

    public String getAddress() {
        if (customer != null) {
            return customer.getAddress();
        }
        return null;
    }

    public void setAddress(String address) {
        if (customer != null) {
            this.customer.setAddress(address);
        }
    }

    public String getPhoneNumber() {
        if (customer != null) {
            return customer.getPhoneNumber();
        }
        return null;
    }

    public void setPhoneNumber(String phone) {
        if (customer != null) {
            this.customer.setPhoneNumber(phone);
        }
    }

    public int getZipCode() {
        if (customer != null) {
            return customer.getZipCode();
        }
        return -1;
    }

    public void setZipCode(int zipCode) {
        if (customer != null) {
            this.customer.setZipCode(zipCode);
        }
    }

    /**
     * Gets the menu from the database.
     */
    public ArrayList<String> getMenu() {
        ArrayList<String> prod = new ArrayList<String>();
        prod.add("hei");
        prod.add("du");
        return prod;
    }

    /**
     * Add a product.
     *
     * @return true/false
     */
    public boolean addProduct(Product product) {
        return false;
    }

    public ArrayList<Product> getActiveOrders() {
        return null;
    }
}