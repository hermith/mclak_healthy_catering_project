/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

/**
 *
 * @author linnk
 */
public class Contract {
    private int contractId;
    private Order order;
    private int dayOfWeek;
    private boolean active;
    
    public Contract() {
        contractId = -1;
        order = new Order();
        dayOfWeek = -1;
        active = false;
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
}
