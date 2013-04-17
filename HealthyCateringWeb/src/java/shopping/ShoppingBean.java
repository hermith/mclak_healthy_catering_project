/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;

import info.Order;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import shopping.customer.CorporateCustomer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;

/**
 * @author aleksalr
 */
@Named
@SessionScoped
public class ShoppingBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private PrivateCustomer privateCustomer;
    private CorporateCustomer corporateCustomer;
    private ShoppingCart shoppingCart;
    @Inject
    private ShoppingHandler shoppingHandler;
    private Order order;

    public ShoppingBean() {
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
        shoppingCart = new ShoppingCart();
        order = new Order();
    }

    /*
     * Getters and setters for privateCustomer
     */
    public String getFirstName() {
        if (privateCustomer != null) {
            return ((PrivateCustomer) privateCustomer).getFirstName();
        }
        return null;
    }

    public void setFirstName(String firstName) {
        if (privateCustomer != null) {
            ((PrivateCustomer) privateCustomer).setFirstName(firstName);
        }
    }

    public String getLastName() {
        if (privateCustomer != null) {
            return ((PrivateCustomer) privateCustomer).getLastName();
        }
        return null;
    }

    public void setLastName(String lastName) {
        if (privateCustomer != null) {
            ((PrivateCustomer) privateCustomer).setLastName(lastName);
        }
    }

    public String getPrivateEmail() {
        if (privateCustomer != null) {
            return privateCustomer.getEmail();
        }
        return null;
    }

    public void setPrivateEmail(String email) {
        if (privateCustomer != null) {
            this.privateCustomer.setEmail(email);
        }
    }

    public String getPrivateAddress() {
        if (privateCustomer != null) {
            return privateCustomer.getAddress();
        }
        return null;
    }

    public void setPrivateAddress(String address) {
        if (privateCustomer != null) {
            this.privateCustomer.setAddress(address);
        }
    }

    public String getPrivatePhoneNumber() {
        if (privateCustomer != null) {
            return privateCustomer.getPhoneNumber();
        }
        return null;
    }

    public void setPrivatePhoneNumber(String phone) {
        if (privateCustomer != null) {
            this.privateCustomer.setPhoneNumber(phone);
        }
    }

    public int getPrivateZipCode() {
        if (privateCustomer != null) {
            return privateCustomer.getZipCode();
        }
        return -1;
    }

    public void setPrivateZipCode(int zipCode) {
        if (privateCustomer != null) {
            this.privateCustomer.setZipCode(zipCode);
        }
    }

    public String getPrivateCity() {
        if (privateCustomer != null) {
            return privateCustomer.getCity();
        }
        return null;
    }

    public void setPrivateCity(String city) {
        if (privateCustomer != null) {
            this.privateCustomer.setCity(city);
        }
    }

    /*
     * Getters and setters for corporateCustomer
     */
    public String getCompanyName() {
        if (corporateCustomer != null) {
            return corporateCustomer.getCompanyName();
        }
        return null;
    }

    public void setCompanyName(String companyName) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setCompanyName(companyName);
        }
    }

    public String getCorporateEmail() {
        if (corporateCustomer != null) {
            return corporateCustomer.getEmail();
        }
        return null;
    }

    public void setCorporateEmail(String email) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setEmail(email);
        }
    }

    public String getCorporateAddress() {
        if (corporateCustomer != null) {
            return corporateCustomer.getAddress();
        }
        return null;
    }

    public void setCorporateAddress(String address) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setAddress(address);
        }
    }

    public String getCorporatePhoneNumber() {
        if (corporateCustomer != null) {
            return corporateCustomer.getPhoneNumber();
        }
        return null;
    }

    public void setCorporatePhoneNumber(String phone) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setPhoneNumber(phone);
        }
    }

    public int getCorporateZipCode() {
        if (corporateCustomer != null) {
            return corporateCustomer.getZipCode();
        }
        return -1;
    }

    public void setCorporateZipCode(int zipCode) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setZipCode(zipCode);
        }
    }

    public String getCorporateCity() {
        if (corporateCustomer != null) {
            return corporateCustomer.getCity();
        }
        return null;
    }

    public void setCorporateCity(String city) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setCity(city);
        }
    }

    /**
     *
     * @return the menu
     */
    public ArrayList<Product> getMenu() {
        return shoppingHandler.getMenu();
    }

    /**
     * Add a product.
     *
     * @return
     */
    public String addProduct(Product product) {
        if (shoppingCart.addProduct(product)) {
            return "";
        } else {
            return "";
        }
    }

    /**
     *
     * @return
     */
    public boolean shoppingCartIsEmpty() {
        return shoppingCart.isEmpty();
    }

    /**
     *
     * @return
     */
    public ArrayList<Product> getProducts() {
        return shoppingCart.getProducts();
    }

    public String deleteProduct(Product product) {
        if (shoppingCart.deleteProduct(product)) {
            return "";
        } else {
            return "";
        }
    }

    /**
     * Places the order.
     *
     * @return
     */
    public String placeOrder() {
        if (privateCustomer != null) {
            order.setCustomerID(privateCustomer.getCustomerId());
        } else if (corporateCustomer != null) {
            order.setCustomerID(corporateCustomer.getCustomerId());
        }
        order.setProducts(getProducts());
        if (shoppingHandler.insertOrder(order)) {
            shoppingCart = new ShoppingCart();
            order = new Order();
        }
        System.out.println("Delivery: " + isDelivery() + ", deliverydate");
        return "";
    }

    /**
     * @return total price
     */
    public float getTotalPrice() {
        return shoppingCart.getTotalPrice();
    }

    /**
     *
     * @param dato
     */
    public void setDeliveryDate(Date date) {
        order.setDeliveryDate(date);
    }

    public Date getDeliveryDate() {
        return order.getDeliveryDate();
    }

    public boolean isDelivery() {
        return order.delivery;
    }

    public void setDelivery(boolean del) {
        order.setDelivery(del);
    }
}