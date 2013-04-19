/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;

import info.Order;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import locale.MessageHandler;
import locale.MessageType;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
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
    private String username;

    public ShoppingBean() {
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
        shoppingCart = new ShoppingCart();
        order = new Order();
    }

    public void initiateCustomer(String username) {
        Customer customer = shoppingHandler.getCustomer(username);
        if (customer != null) {
            if (customer instanceof PrivateCustomer) {
                privateCustomer = (PrivateCustomer) customer;
            } else {
                corporateCustomer = (CorporateCustomer) customer;
            }
        }
        this.username = username;
    }

    public void resetVars() {
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
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
    
    /**
     * ZipCode String format
     */
    
    public void setPrivateZipCodeString (String zip) {
        if(privateCustomer != null) {
            this.privateCustomer.setZipCode(Integer.parseInt(zip));
        }
    }
    
    public String getPrivateZipCodeString() {
        if(privateCustomer != null) {
            return Integer.toString(privateCustomer.getZipCode());
        }return null;
    }
    
    public void setCorporateZipCodeString(String zip) {
        if(corporateCustomer != null) {
            this.corporateCustomer.setZipCode(Integer.parseInt(zip));
        }
    }
    
    public String getCorporateZipCodeString() {
        if(corporateCustomer != null) {
            return Integer.toString(corporateCustomer.getZipCode());
        }return null;
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
        return "";
    }

    /**
     * @return total price
     */
    public float getTotalPrice() {
        return shoppingCart.getTotalPrice();
    }

    /**
     * Converts java.util.Date to java.sql.Date, and passes it to the
     * order-object.
     *
     * @param dato
     */
    public void setDeliveryDate(Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        order.setDeliveryDate(sqlDate);
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

    /**
     * NB Husk å fikse en sjekk på zipCode
     *
     * @return
     */
    public String saveChangesAccount() {
        Customer customer = shoppingHandler.getCustomer(username);
        boolean status = false;
        if (customer instanceof PrivateCustomer) {
            int zipCode = Integer.parseInt(getPrivateZipCodeString());
            PrivateCustomer newCustomer = new PrivateCustomer(customer.getCustomerId(), getPrivateEmail(), getPrivateAddress(), getPrivatePhoneNumber(), zipCode, getPrivateCity(), getFirstName(), getLastName());
            if (shoppingHandler.fixCustomer(customer, newCustomer)) {
                status = true;
            }
        } else if (customer instanceof CorporateCustomer) {
            int zipCode = Integer.parseInt(getCorporateZipCodeString());
            CorporateCustomer newCustomer = new CorporateCustomer(customer.getCustomerId(), getCorporateEmail(), getCorporateAddress(), getCorporatePhoneNumber(), zipCode, getCorporateCity(), getCompanyName());
            if (shoppingHandler.fixCustomer(customer, newCustomer)) {
                status = true;
            }
        }
        if (status) {
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "edit_account_changes_saved");
            MessageHandler.addErrorMessage(msg);
        }else {
            String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_account_changes_not_saved");
            MessageHandler.addErrorMessage(msg);
        }
        initiateCustomer(username);
        return "";
    }
}