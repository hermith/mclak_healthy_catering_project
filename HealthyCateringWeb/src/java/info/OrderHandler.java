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
 * @author colsen91
 */
@ApplicationScoped
public class OrderHandler {

    @Inject
    private DatabaseHandler db;
    private Date contractsLastChecked = null;
    private ArrayList<Contract> contractsCheckedToday;
    private static final long MILLIS_IN_A_DAY = 86401000l;

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

    public ArrayList<Order> getOrderHistory() {
        return db.selectOrders();
    }

    public Order getOrderFromID(int orderID) {
        return db.selectOrder(orderID);
    }

    public Customer getCustomerFromID(int customerID) {
        return db.selectCustomer(customerID);
    }

    public ArrayList<Order> getOrderHistory(int customerId) {
        return db.selectOrders(customerId);
    }

    public void markOrderAsDelivered(int orderID, Timestamp stamp) {
        db.updateOrderSetDateDelivered(orderID, stamp);
    }

    public void markOrderAsPrepared(int orderID) {
        db.updateOrderSetPrepared(orderID, true);
    }

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
