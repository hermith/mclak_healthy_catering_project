/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;

import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import shopping.customer.Customer;

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
}