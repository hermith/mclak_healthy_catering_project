package info;

/**
 *
 * @author linnk
 */
public class OrderStatus {
    private Order order;
    private boolean prepared;
    
    public OrderStatus () {
        order = new Order();
        prepared = false;
    }
    
    public OrderStatus(Order order) {
        this.order = order;
        prepared = false;
    }

    //GETTER AND SETTTER
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }
}
