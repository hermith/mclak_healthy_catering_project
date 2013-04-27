package info;

import database.DatabaseHandler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.Contract;
import shopping.customer.Customer;

/**
 * OrderHandler is used to handle everything related to orders.
 *
 * @author colsen91, Karl
 */
@ApplicationScoped
public class OrderHandler {

    @Inject
    private DatabaseHandler db;
    private Date contractsLastChecked = null;
    private ArrayList<Contract> contractsCheckedToday;
    private static final long MILLIS_IN_A_DAY = 86401000l;

    /**
     * Calls selectUndeliveredOrders() in DatabaseHandler.java. Removes inactice
     * (active=false) orders.
     *
     * @return Array with active and undelivered orders.
     */
    public ArrayList<Order> getUndeliveredOrders() {
        checkContracts();
        ArrayList<Order> orders = db.selectUndeliveredOrders();
        ArrayList<Order> toRemove = new ArrayList<Order>();
        for (Order o : orders) {
            if (!o.isActive()) {
                toRemove.add(o);
            }
        }

        for (Order o : toRemove) {
            orders.remove(o);
        }

        Collections.sort(orders);
        return orders;
    }

    /**
     * Calls selectOrders() in DatabaseHandler.java.
     *
     * @return Array with all orders in the database.
     */
    public ArrayList<Order> getOrderHistory() {
        return db.selectOrders();
    }

    /**
     * Calls selectOrder(orderID) in DatabaseHandler.java.
     *
     * @param orderID
     * @return Order with the correct orderID.
     */
    public Order getOrderFromID(int orderID) {
        return db.selectOrder(orderID);
    }

    /**
     * Calls selectCustomer(customerID) in DatabaseHandler.java.
     *
     * @param customerID
     * @return Customer with the correct customerID.
     */
    public Customer getCustomerFromID(int customerID) {
        return db.selectCustomer(customerID);
    }

    /**
     * Calls selectOrders(customerId) in DatabaseHandler.java.
     *
     * @param customerId
     * @return Array with all orders on the given customerId.
     */
    public ArrayList<Order> getOrderHistory(int customerId) {
        return db.selectOrders(customerId);
    }

    /**
     * Calls updateOrderSetDateDelivered(orderID, stamp) in
     * DatabaseHandler.java. Used mark an order as delivered.
     *
     * @param orderID
     * @param stamp
     */
    public void markOrderAsDelivered(int orderID, Timestamp stamp) {
        db.updateOrderSetDateDelivered(orderID, stamp);
    }

    /**
     * Calls updateOrderSetPrepared(orderID, true) in DatabaseHandler.java. Used
     * to mark an order as prepared.
     *
     * @param orderID
     */
    public void markOrderAsPrepared(int orderID) {
        db.updateOrderSetPrepared(orderID, true);
    }

    /**
     * Checks for any contracts to be delivered on current date.
     */
    private void checkContracts() {
        if (contractsLastChecked == null) {
            contractsCheckedToday = db.selectContracts();
            for (Contract c : contractsCheckedToday) {
                if (c.getDayOfWeek() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                    insertOrderFromContract(c);
                }
            }
            contractsLastChecked = new Date();
        } else if ((System.currentTimeMillis() - contractsLastChecked.getTime()) > MILLIS_IN_A_DAY) {
            contractsLastChecked = null;
            checkContracts();
        } else {
            ArrayList<Contract> contractsToCheck = db.selectContracts();
            for (Contract c : contractsToCheck) {
                if (!contractsCheckedToday.contains(c) && c.getDayOfWeek() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                    insertOrderFromContract(c);
                    contractsCheckedToday.add(c);
                }
            }
        }
    }

    /**
     * Inserts the order to a given contract in the database.
     *
     * @param contract
     * @return Whether the order was inserted.
     */
    private boolean insertOrderFromContract(Contract contract) {
        Order orderCopy = contract.getOrder();
        orderCopy.setActive(true);
        orderCopy.setPlacedDate(new Timestamp(System.currentTimeMillis()));
        Timestamp deliveryDate = new Timestamp(System.currentTimeMillis());
        deliveryDate.setHours(contract.getTime().getHours());
        deliveryDate.setMinutes(contract.getTime().getMinutes());
        orderCopy.setDeliveryDate(deliveryDate);
        orderCopy.setPrepared(false);
        return db.insertOrder(orderCopy);
    }
}
