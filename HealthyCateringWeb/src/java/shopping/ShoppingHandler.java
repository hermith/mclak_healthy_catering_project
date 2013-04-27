package shopping;

import database.DatabaseHandler;
import info.Contract;
import info.Order;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;
import shopping.product.SingleProduct;

/**
 * @author colsen91
 */
@ApplicationScoped
public class ShoppingHandler implements Serializable {

    @Inject
    private DatabaseHandler database;
    private ArrayList<Product> products;

    public ShoppingHandler() {
        products = new ArrayList<Product>();
    }

    /**
     * Calls insertOrder(order) in DatabaseHandler.java. Inserts a order in the
     * database.
     *
     * @param order
     * @return if the order was inserted.
     */
    public boolean insertOrder(Order order) {
        return database.insertOrder(order);
    }

    /**
     * Calls selectProducts in DatabaseHandler.java if the variable
     * productsTableHasChanged is given.
     *
     * @return a list with active products.
     */
    public ArrayList<Product> getMenu() {
        ArrayList<Product> activeProducts = new ArrayList<Product>();
        if (database.hasProductsTableChanged()) {
            products = database.selectProducts();
            database.setProductsTableChanged(false);
        }
        if (products != null) {
            for (Product p : products) {
                if (p.isActive()) {
                    activeProducts.add(p);
                }
            }
        } else {
            activeProducts.add(new SingleProduct(-1,
                    "Databaseproblemer", "Database problems", "Beklager, vi har litt problemer med databasen, prøv på nytt senere.",
                    "Sorry, we are currently experiencing some problems with the database, please try again later.",
                    -6, 3000));
        }
        return activeProducts;
    }

    /**
     * Calls insertCustomer(customer,username) in DatabaseHandler. Inserts a new
     * customer in the database.
     *
     * @param customer
     * @param username
     * @return if the customer was inserted.
     */
    public boolean insertCustomer(Customer customer, String username) {
        return (database.insertCustomer(customer, username));
    }

    /**
     * Calls selectCustomer(username) in DatabaseHandler.java.
     *
     * @param username
     * @return Customer-object with the correct username.
     */
    public Customer getCustomer(String username) {
        return database.selectCustomer(username);
    }

    /**
     * Calls updateCustomer(customer) in DatabaseHandler.java. Updates the
     * customer object in the database.
     *
     * @param customer
     * @return whether the customer was updated.
     */
    public boolean updateCustomer(Customer customer) {
        return database.updateCustomer(customer);
    }

    /**
     * Fix newCustomer object with approved values.
     *
     * @param customer
     * @param newCustomer
     * @return whether the customer was updated.
     */
    public boolean fixCustomer(Customer customer, Customer newCustomer) {
        if (customer instanceof PrivateCustomer) {
            PrivateCustomer privateCustomer = (PrivateCustomer) newCustomer;
            PrivateCustomer privateCustomerDatabase = (PrivateCustomer) customer;
            if (privateCustomer.getFirstName().equals("")) {
                privateCustomer.setFirstName(privateCustomerDatabase.getFirstName());
            }
            if (privateCustomer.getLastName().equals("")) {
                privateCustomer.setLastName(privateCustomerDatabase.getLastName());
            }
        } else if (customer instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) newCustomer;
            CorporateCustomer corporateCustomerDatabase = (CorporateCustomer) customer;
            if (corporateCustomer.getCompanyName().equals("")) {
                corporateCustomer.setCompanyName(corporateCustomerDatabase.getCompanyName());
            }
        }
        if (newCustomer.getAddress().equals("")) {
            newCustomer.setAddress(customer.getAddress());
        }
        if (newCustomer.getEmail().equals("")) {
            newCustomer.setEmail(customer.getEmail());
        }
        if (newCustomer.getPhoneNumber().equals("")) {
            newCustomer.setPhoneNumber(customer.getPhoneNumber());
        }
        if (newCustomer.getCity().equals("")) {
            newCustomer.setCity(customer.getCity());
        }
        return updateCustomer(newCustomer);
    }

    /**
     * Calls selectOrders(customerId) in DatabaseHandler.java.
     *
     * @param customerId
     * @return order history to the given customer id.
     */
    public ArrayList<Order> getOrderHistory(int customerId) {
        return database.selectOrders(customerId);
    }

    /**
     * Updates contract with id like contractId. 
     *
     * @param active
     * @param contractId
     * @return if the contract was updated.
     */
    public boolean updateContractSetActive(boolean active, int contractId) {
        return database.updateContractSetActive(active, contractId);
    }

    /**
     * Calls insertContract(contract) in DatabaseHandler.java. Inserts contract
     * in the database.
     *
     * @param contract
     * @return whether the contract was inserted.
     */
    public boolean insertContract(Contract contract) {
        return database.insertContract(contract);
    }

    /**
     *
     * @param customerId
     * @return active contracts on the given customerId
     */
    public ArrayList<Contract> selectContracts(int customerId) {
        ArrayList<Contract> contracts = new ArrayList<Contract>();
        for (Contract c : database.selectContracts(customerId)) {
            if (c.isActive()) {
                contracts.add(c);
            }
        }
        return contracts;
    }
}