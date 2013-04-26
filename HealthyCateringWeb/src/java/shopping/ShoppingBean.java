package shopping;

import info.Contract;
import info.Order;
import info.ProductHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import locale.MessageHandler;
import locale.MessageType;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;
import shopping.product.SingleProduct;

/**
 * @author aleksalr, Linn
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
    @Inject
    private ProductHandler productHandler;
    private Contract newContract;

    public ShoppingBean() {
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
        shoppingCart = new ShoppingCart();
        order = new Order();
        newContract = new Contract();
    }

    /**
     * Initiates customer object to private or corporate customer based on type
     * of user.
     *
     * @param username - Username of user related to customer object.
     */
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

    /**
     * Registers new private customer in database.
     *
     * @param username - Username related to the customer object.
     */
    public void registerPrivateCustomer(String username) {
        System.out.println("REGISTERING PRIVATE CUSTOMER");
        shoppingHandler.insertCustomer(privateCustomer, username);
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
    }

    /**
     * Registers new corporate customer in database.
     *
     * @param username - Username related to the customer object.
     */
    public void registerCorporateCustomer(String username) {
        System.out.println("REGISTERING CORPORATE CUSTOMER");
        shoppingHandler.insertCustomer(corporateCustomer, username);
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
    }

    /**
     * Resets customer and order objects when. Method used when objects have
     * been processed and no longer need to remain alive.
     */
    public void resetVars() {
        privateCustomer = new PrivateCustomer();
        corporateCustomer = new CorporateCustomer();
        order = new Order();
    }

    /*
     * Getters and setters for private customer object. 
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
    public void setPrivateZipCodeString(String zip) {
        if (privateCustomer != null) {
            this.privateCustomer.setZipCode(Integer.parseInt(zip));
        }
    }

    public String getPrivateZipCodeString() {
        if (privateCustomer != null) {
            return Integer.toString(privateCustomer.getZipCode());
        }
        return null;
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
     * Getters and setters for corporate customer object. 
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

    public void setCorporateZipCodeString(String zip) {
        if (corporateCustomer != null) {
            this.corporateCustomer.setZipCode(Integer.parseInt(zip));
        }
    }

    public String getCorporateZipCodeString() {
        if (corporateCustomer != null) {
            return Integer.toString(corporateCustomer.getZipCode());
        }
        return null;
    }

    /**
     *
     * @return the menu (includes PackageProducts if logged in user is a
     * corporateCustomer)
     */
    public ArrayList<Product> getMenu(boolean isCorporate) {
        ArrayList<Product> products = shoppingHandler.getMenu();
        ArrayList<Product> productList = new ArrayList<Product>();
        if (isCorporate) {
            return products;
        } else {
            for (Product p : products) {
                if (p instanceof SingleProduct) {
                    productList.add(p);
                }
            }
        }
        return productList;
    }

    /**
     * Calls addProduct(product) in ShoppingCart.java. Adds the product to the
     * shoppingCart.
     *
     * @return
     */
    public void addProduct(Product product) {
        shoppingCart.addProduct(product);
    }

    /**
     * Calls isEmpty() in ShoppingCart.java.
     *
     * @return if the current shoppingCart is empty.
     */
    public boolean shoppingCartIsEmpty() {
        return shoppingCart.isEmpty();
    }

    /**
     * Calls getProducts() in ShoppingCart.java
     *
     * @return products in the shoppingCart
     */
    public ArrayList<Product> getProducts() {
        return shoppingCart.getProducts();
    }

    /**
     * Calls deleteProduct(product) in ShoppingCart.java. Deletes the product
     * from the shoppingCart.
     *
     * @param product
     */
    public void deleteProduct(Product product) {
        shoppingCart.deleteProduct(product);
    }

    /**
     * Calls insertOrder() in ShoppingHandler.java. Places the order in the
     * database.
     *
     * @return String to viewId
     */
    public String placeOrder() {
        if (!getProducts().isEmpty()) {
            if (privateCustomer.getCustomerId() != -1) {
                order.setCustomerID(privateCustomer.getCustomerId());
            } else if (corporateCustomer.getCustomerId() != -1) {
                order.setCustomerID(corporateCustomer.getCustomerId());
            }
            order.setProducts(getProducts());
            java.sql.Timestamp date = new java.sql.Timestamp(new Date().getTime());
            order.setPlacedDate(date);
            if (shoppingHandler.insertOrder(order)) {
                shoppingCart = new ShoppingCart();
                order = new Order();
                String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "order_placed");
                MessageHandler.addErrorMessage(msg);
                return "to_menu";
            }
        }
        String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "order_failed_to_place");
        MessageHandler.addErrorMessage(msg);
        return "";
    }

    /**
     * @return total price of the shoppingCart
     */
    public float getTotalPrice() {
        return shoppingCart.getTotalPrice();
    }

    /**
     * Converts java.util.Date to java.sql.Timestamp, and passes it to the
     * order-object.
     *
     * @param dato
     */
    public void setDeliveryDate(Date date) {
        if (date != null) {
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
            order.setDeliveryDate(sqlDate);
        }
    }

    public Date getDeliveryDate() {
        return order.getDeliveryDate();
    }

    public boolean isDelivery() {
        return order.isDelivery();
    }

    public void setDelivery(boolean del) {
        order.setDelivery(del);
    }

    /**
     * Calls fixCustomer() in ShoppingHandler.java. Saves changes to logged in
     * customer.
     *
     * @return
     */
    public void saveChangesAccount() {
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
        } else {
            String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "edit_account_changes_not_saved");
            MessageHandler.addErrorMessage(msg);
        }
        initiateCustomer(username);
    }

    /**
     * Calls getOrderHistory(CustomerId) in ShoppingHandler.java.
     *
     * @return Order history for current customer.
     */
    public ArrayList<Order> getOrderHistory() {
        if (privateCustomer.getCustomerId() != -1) {
            return shoppingHandler.getOrderHistory(privateCustomer.getCustomerId());
        } else if (corporateCustomer.getCustomerId() != -1) {
            return shoppingHandler.getOrderHistory(corporateCustomer.getCustomerId());
        }
        return null;
    }

    /**
     * Calls findQuantity(product, getProducts()).
     *
     * @param product
     * @return quantity of the current product in the shoppingCart
     */
    public int findQuantityProduct(Product product) {
        if (product != null) {
            return productHandler.findQuantity(product, getProducts());
        }
        return 0;
    }

    /**
     * Calls getUniqueProductsList(getProducts()). Sort the products in
     * selectedOrder with only one of each product.
     *
     * @return Unique product list
     */
    public ArrayList<Product> getShoppingCartProducts() {
        return productHandler.getUniqueProductsList(getProducts());
    }

    //Getters and setters for new contract
    public boolean isNewContractDelivery() {
        return newContract.isDelivery();
    }

    public void setNewContractDelivery(boolean delivery) {
        newContract.setDelivery(delivery);
    }

    public ArrayList<Product> getNewContractProducts() {
        return newContract.getOrder().getProducts();
    }

    public void setNewContractDayOfWeek(int dayOfWeek) {
        newContract.setDayOfWeek(dayOfWeek);
    }

    public int getNewContractDayOfWeek() {
        return newContract.getDayOfWeek();
    }
    

    public int findQuantityNewContract(Product product) {
        return productHandler.findQuantity(product, getNewContractProducts());
    }

    public int[] getDaysOfWeek() {
        int days[] = new int[7];
        days[0] = Calendar.SUNDAY;
        days[1] = Calendar.MONDAY;
        days[2] = Calendar.TUESDAY;
        days[3] = Calendar.WEDNESDAY;
        days[4] = Calendar.THURSDAY;
        days[5] = Calendar.FRIDAY;
        days[6] = Calendar.SATURDAY;
        return days;
    }

    public String getDayLocalString(int day) {
        String dayString = MessageHandler.getLocalizedText(MessageType.TEKST, "day" + day);
        return dayString;
    }
}