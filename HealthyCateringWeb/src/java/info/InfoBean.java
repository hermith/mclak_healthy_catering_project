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

/**
 *
 * @author Karl
 */
@Named
@ApplicationScoped
public class InfoBean {

    @Inject
    private DatabaseHandler db;
    ArrayList<Order> activeOrders = new ArrayList<Order>();

    public InfoBean() {
        activeOrders.add(new Order(1, new int[]{1, 2, 3}, new Date(2013, 04, 17), null));
        activeOrders.add(new Order(2, new int[]{2, 3, 4}, new Date(2013, 04, 17), null));
        activeOrders.add(new Order(3, new int[]{2, 5, 7}, new Date(2013, 04, 17), null));
    }

    public ArrayList<Order> getActiveOrders() {
        return activeOrders;
    }
}
