package info;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import shopping.product.Product;

/**
 *
 * @author linnk
 */
public class Contract {

    private int contractId;
    private Order order;
    private int dayOfWeek;
    private Time time;
    private boolean active;

    public Contract() {
        contractId = -1;
        order = new Order();
        dayOfWeek = 1;
        active = false;
        time = new Time(new Date().getTime());
    }

    /**
     *
     * @param contractId
     * @param order
     * @param dayOfWeek
     * @param time
     * @param active
     */
    public Contract(int contractId, Order order, int dayOfWeek, Time time, boolean active) {
        this.contractId = contractId;
        this.order = order;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.active = active;
    }

    //GETTER AND SETTER
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDelivery(boolean delivery) {
        this.order.setDelivery(delivery);
    }

    public boolean isDelivery() {
        return this.order.isDelivery();
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setProducts(ArrayList<Product> products) {
        this.order.setProducts(products);
    }

    public void setCustomerId(int id){
        this.order.setCustomerID(id);
    }
    
    public void setPlacedDate(Timestamp date) {
        order.setPlacedDate(date);
    }
}
