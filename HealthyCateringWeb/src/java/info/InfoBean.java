package info;

import info.customer.CustomerHandler;
import info.product.ProductHandler;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import locale.MessageHandler;
import locale.MessageType;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;

/**
 * InfoBean is used to handle everything related to employee functions. Mainly view all
 * orders and customers.
 *
 * @author Karl, Linn
 */
@Any
@Named
@SessionScoped
public class InfoBean implements Serializable {

    private Customer selectedCustomer;
    private Customer selectedOrderCustomer;
    private Order selectedOrder;
    private boolean detailOrder;
    private boolean editCustomer;
    private boolean showActiveOrders;
    @Inject
    private CustomerHandler customerHandler;
    @Inject
    private OrderHandler orderHandler;
    @Inject
    private ProductHandler productHandler;

    public InfoBean() {
    }

    /**
     * Calls getUndeliveredOrders() in OrderHandler.java.
     *
     * @return Array with undelivered orders.
     */
    public ArrayList<Order> getUndeliveredOrders() {
        ArrayList<Order> orders = orderHandler.getUndeliveredOrders();
        Collections.sort(orders);
        return orders;
    }

    /**
     * @return Array with unprepared orders (prepared=false).
     */
    public ArrayList<Order> getUnpreparedOrders() {
        ArrayList<Order> puorder = new ArrayList<Order>();
        for (Order o : orderHandler.getUndeliveredOrders()) {
            if (!o.isPrepared()) {
                puorder.add(o);
            }
        }
        Collections.sort(puorder);
        return puorder;
    }

    /**
     * Calls getOrderHistory() in OrderHandler.java.
     *
     * @return Array with all orders in the system.
     */
    public ArrayList<Order> getOrderHistory() {
        return orderHandler.getOrderHistory();
    }

    /**
     * Used to show edit customer view. Initializes the variable used to
     * temporarily store the information.
     *
     * @param customer
     */
    public void editCustomer(Customer customer) {
        selectedCustomer = customer;
        editCustomer = true;
    }

    /**
     * Find the correct order in the database and stores data. Used to show
     * details in a order.
     *
     * @param orderID
     */
    public void lookUpOrder(int orderID) {
        selectedOrder = orderHandler.getOrderFromID(orderID);
        selectedOrderCustomer = orderHandler.getCustomerFromID(selectedOrder.getCustomerID());
        detailOrder = true;
    }

    /**
     * Calls selectCustomer(customer_id) in CustomerHandler.java.
     *
     * @param customer_id
     * @return Customer object with correct id
     */
    public Customer getCustomer(int customer_id) {
        return customerHandler.selectCustomer(customer_id);
    }

    /**
     * Releases the variable used to temporarily store information about an
     * order. Used to close the order detail window.
     */
    public void closeDetailedInfo() {
        selectedCustomer = null;
        detailOrder = false;
    }

    /**
     * Close edit customer view. Releases the variable used to temporarily store
     * information about a customer.
     */
    public void closeEditCustomer() {
        selectedCustomer = null;
        editCustomer = false;
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context
                .getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    /**
     * Calls getAllCustomers() in CustomerHandler.java.
     *
     * @return Array with all customers in the database.
     */
    public ArrayList<Customer> getAllCustomers() {
        return customerHandler.getAllCustomers();
    }

    /**
     * @return if the selectedCustomer is a PrivateCustomer-object.
     */
    public boolean selectedLoggedInAsPrivate() {
        if (selectedCustomer instanceof PrivateCustomer) {
            return true;
        }
        return false;
    }

    /**
     * Save changes to the selectedCustomer.
     */
    public void saveChangesCustomer() {
        if (customerHandler.fixCustomer(selectedCustomer)) {
            String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "edit_account_changes_saved");
            MessageHandler.addErrorMessage(msg);
        } else {
            String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_account_changes_not_saved");
            MessageHandler.addErrorMessage(msg);
        }
    }

    public boolean isShowActiveOrders() {
        return showActiveOrders;
    }

    public void setShowActiveOrders(boolean showActiveOrders) {
        this.showActiveOrders = showActiveOrders;
    }

    /**
     * Calls findQuantity(product, selectedOrder.getProducts()).
     *
     * @param product
     * @return quantity of the current product in the selectedOrder->productlist
     */
    public int findQuantityProduct(Product product) {
        if (selectedOrder != null) {
            return productHandler.findQuantity(product, selectedOrder.getProducts());
        }
        return 0;
    }

    /**
     * Calls findQuantity(product, selectedOrder.getProducts()).
     *
     * @param product
     * @return quantity of the current product in the selectedOrder->productlist
     */
    public int findQuantityProductsFromList(Product product, ArrayList<Product> products) {
        return productHandler.findQuantity(product, products);
    }

    /**
     * Calls getUniqueProductsList(selectedOrder.getProducts()). Sort the
     * products in selectedOrder with only one of each product.
     *
     * @return Unique product list
     */
    public ArrayList<Product> getOrderProducts() {
        if (selectedOrder != null) {
            return productHandler.getUniqueProductsList(selectedOrder.getProducts());
        }
        return null;
    }

    /**
     * Calls getUniqueProductList(products) in ProductHandler.java. Used to
     * display products with quantity.
     *
     * @param products
     * @return Array with unique products.
     */
    public ArrayList<Product> getUniqueOrdersFromList(ArrayList<Product> products) {
        return productHandler.getUniqueProductsList(products);
    }

    /**
     * Marks an order as delivered with the timestamp of the current time.
     *
     * @param orderID Order to set as delivered
     */
    public void markOrderAsDelivered(int orderID) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        orderHandler.markOrderAsDelivered(orderID, stamp);
    }

    /**
     * Marks and order as prepared.
     *
     * @param orderID Order to be marked as prepared
     */
    public void markOrderAsPrepared(int orderID) {
        orderHandler.markOrderAsPrepared(orderID);
    }

    /*
     * Getters and setters for selectedCustomer 
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public Customer getSelectedOrderCustomer() {
        return selectedOrderCustomer;
    }

    public void setSelectedOrderCustomer(Customer selectedOrderCustomer) {
        this.selectedOrderCustomer = selectedOrderCustomer;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    /*
     * Getters and setters for selectedCustomer
     */
    public String getFirstName() {
        if (selectedCustomer != null) {
            if (selectedCustomer instanceof PrivateCustomer) {
                PrivateCustomer customer = (PrivateCustomer) selectedCustomer;
                return customer.getFirstName();
            }
        }
        return null;
    }

    public void setFirstName(String firstname) {
        if (selectedCustomer != null) {
            if (selectedCustomer instanceof PrivateCustomer) {
                PrivateCustomer customer = (PrivateCustomer) selectedCustomer;
                customer.setFirstName(firstname);
            }
        }
    }

    public String getLastName() {
        if (selectedCustomer != null) {
            if (selectedCustomer instanceof PrivateCustomer) {
                PrivateCustomer customer = (PrivateCustomer) selectedCustomer;
                return customer.getLastName();
            }
        }
        return null;
    }

    public void setLastName(String lastname) {
        if (selectedCustomer != null) {
            if (selectedCustomer instanceof PrivateCustomer) {
                PrivateCustomer customer = (PrivateCustomer) selectedCustomer;
                customer.setLastName(lastname);
            }
        }
    }

    public String getCompanyName() {
        if (selectedCustomer != null) {
            if (selectedCustomer instanceof CorporateCustomer) {
                CorporateCustomer customer = (CorporateCustomer) selectedCustomer;
                return customer.getCompanyName();
            }
        }
        return null;
    }

    public void setCompanyName(String companyName) {
        if (selectedCustomer != null) {
            if (selectedCustomer instanceof CorporateCustomer) {
                CorporateCustomer customer = (CorporateCustomer) selectedCustomer;
                customer.setCompanyName(companyName);
            }
        }
    }

    public String getEmail() {
        if (selectedCustomer != null) {
            return selectedCustomer.getEmail();
        }
        return null;
    }

    public void setEmail(String email) {
        if (selectedCustomer != null) {
            selectedCustomer.setEmail(email);
        }
    }

    public String getAddress() {
        if (selectedCustomer != null) {
            return selectedCustomer.getAddress();
        }
        return null;
    }

    public void setAddress(String address) {
        if (selectedCustomer != null) {
            selectedCustomer.setAddress(address);
        }
    }

    public String getZipCode() {
        if (selectedCustomer != null) {
            return Integer.toString(selectedCustomer.getZipCode());
        }
        return null;
    }

    public void setZipCode(String zip) {
        if (selectedCustomer != null) {
            try {
                selectedCustomer.setZipCode(Integer.parseInt(zip));
            } catch (Exception e) {
            }
        }
    }

    public String getCity() {
        if (selectedCustomer != null) {
            return selectedCustomer.getCity();
        }
        return null;
    }

    public void setCity(String city) {
        if (selectedCustomer != null) {
            selectedCustomer.setCity(city);
        }
    }

    public String getPhoneNumber() {
        if (selectedCustomer != null) {
            return selectedCustomer.getPhoneNumber();
        }
        return null;
    }

    public void setPhoneNumber(String number) {
        if (selectedCustomer != null) {
            selectedCustomer.setPhoneNumber(number);
        }
    }

    //Getters and setters
    public boolean isCustomerOrder() {
        return detailOrder;
    }

    public boolean isPopupEditCustomer() {
        return editCustomer;
    }
}