/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;


import info.CustomerHandler;
import info.Order;
import info.OrderHandler;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Maria
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {
    
    private Order order;
    private OrderHandler orderHandler;
    private CustomerHandler customerHandler;
            
    public CustomerBean(){
        
    }
    
    public ArrayList<Order> getOrderHistory(){
        return orderHandler.getOrderHistory();
    }
    
    
    
    
}
