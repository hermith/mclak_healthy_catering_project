package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.criteria.Order;
import javax.sql.DataSource;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.Product;
import user.User;

/**
 * @author Christer Olsen
 */
@Named
@ApplicationScoped
public class DatabaseHandler {

    private static final String POOL_NAME = "jdbc/sysproj_res";
    private static final String STM_INSERT_USER = "INSERT INTO User_Table(username, password) VALUES(?, ?)";
    private static final String STM_INSERT_USER_ROLE = "INSERT INTO User_Role_Table(username, user_role) VALUES(?, ?)";
    private static final String STM_INSERT_CUSTOMER = "INSERT INTO Customer_Table(username, email, phone_number, address, zip_code, city) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String STM_INSERT_PRIVATE_CUSTOMER = "INSERT INTO Private_Customer_Table(customer_id, first_name, last_name) VALUES(?, ?, ?)";
    private static final String STM_INSERT_CORPORATE_CUSTOMER = "INSERT INTO Corporate_Customer_Table(customer_id, company_name) VALUES(?, ?)";
    private static final String STM_SELECT_USER = "SELECT username AS username, password AS password FROM User_Table WHERE username = ?";
    private static final String STM_SELECT_USER_ROLE = "SELECT user_role AS user_role FROM User_Role_Table WHERE username = ?";
    private static final String STM_SELECT_CUSTOMER = "SELECT customer_id AS customer_id, email AS email, phone_number AS phone_number, address AS adress, zip_code AS zip_code, city AS city FROM Customer_Table WHERE username = ?";
    private static final String STM_SELECT_PRIVATE_CUSTOMER = "SELECT first_name AS first_name, last_name AS last_name FROM Private_Customer_Table WHERE customer_id = ?";
    private static final String STM_SELECT_CORPORATE_CUSTOMER = "SELECT company_name FROM Corporate_Customer_Table WHERE customer_id = ?";
    private static final String STM_UPDATE_PASSWORD = "UPDATE User_Table SET password = ? WHERE username = ?";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "user_role";
    private static final String COLUMN_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_ZIP_CODE = "zip_code";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_COMPANY_NAME = "company_name";
    private DataSource dataSource;

    /**
     * Constructor.
     */
    public DatabaseHandler() {
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Creating a new database handler...");
        try {
            dataSource = (DataSource) new InitialContext().lookup(POOL_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to create a new database handler!", ex);
        }
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "New database handler successfully created!");
    }

    public void testDatabaseHandler() {
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "The database handler is up and running...");
    }

    //==========================================================================
    //=============Ferdige skisser av div. metoder finnes nedenfor.=============
    //=============Dog ikke sikker på om alle nødvendige er tatt med.===========
    //==========================================================================
    /**
     * TODO: Burde kalles når en noen (person/bedrift/ansatt/admin) skal
     * registrere seg som bruker på siden.
     *
     * @param user
     * @return
     */
    public synchronized boolean registerNewUser(User user) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_INSERT_USER);
            stm.setString(1, user.getUsername().trim());
            stm.setString(2, user.getPassword().trim());
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                stm = conn.prepareStatement(STM_INSERT_USER_ROLE);
                stm.setString(1, user.getUsername().trim());
                stm.setString(2, user.getRoleId().trim());
                numberOfRowsUpdated = stm.executeUpdate();
                if (numberOfRowsUpdated == 1) {
                    commit(conn);
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "User {0} registered.", user);
                    return true;
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "User {0} NOT registered.", user);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to register new user " + user + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * TODO: Burde kalles etter registerNewUser-metoden for å registrere
     * kundeinformasjon
     *
     * @param customer
     * @return
     */
    public synchronized boolean registerNewCustomer(Customer customer, String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        int customerId = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, username);
            stm.setString(2, customer.getEmail().trim());
            stm.setString(3, customer.getPhoneNumber().trim());
            stm.setString(4, customer.getAddress().trim());
            stm.setInt(5, customer.getZipCode());
            stm.setString(6, customer.getCity().trim());
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                stm.getGeneratedKeys().next();
                customerId = stm.getGeneratedKeys().getInt(1);
                if (customer instanceof PrivateCustomer) {
                    PrivateCustomer privateCustomer = (PrivateCustomer) customer;
                    stm = conn.prepareStatement(STM_INSERT_PRIVATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    stm.setString(2, privateCustomer.getFirstName().trim());
                    stm.setString(3, privateCustomer.getLastName().trim());
                    numberOfRowsUpdated = stm.executeUpdate();
                } else if (customer instanceof CorporateCustomer) {
                    CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
                    stm = conn.prepareStatement(STM_INSERT_CORPORATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    stm.setString(2, corporateCustomer.getCompanyName().trim());
                    numberOfRowsUpdated = stm.executeUpdate();
                }
                if (numberOfRowsUpdated == 1) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Customer {0} registered.", customer);
                    commit(conn);
                    return true;
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Customer {0} NOT registered.", customer);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to register new customer " + customer + "with username " + username + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * TODO: Burde kalles uansett når en person logger inn for å finne ut
     * hvilken rolle denne brukeren har.
     *
     * @param username
     * @return
     */
    public synchronized User getUser(String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_USER_ROLE);
            stm.setString(1, username);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                User user = new User(username, resSet.getString(COLUMN_USER_ROLE));
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "User {0} retrieved.", user);
                return user;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Faield to retrieve user with username {0}.", username);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve user with username" + username + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * TODO: Burde kalles når en person, som også er en kunde, logger inn. Evt.
     * ved andre anledninger.
     *
     * @param username
     * @return
     */
    public synchronized Customer getCustomer(String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CUSTOMER);
            stm.setString(1, username);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                int customerId = resSet.getInt(COLUMN_CUSTOMER_ID);
                String email = resSet.getString(COLUMN_EMAIL);
                String phoneNumber = resSet.getString(COLUMN_PHONE_NUMBER);
                String address = resSet.getString(COLUMN_ADDRESS);
                int zip_code = resSet.getInt(COLUMN_ZIP_CODE);
                String city = resSet.getString(COLUMN_CITY);
                if (isCustomerPrivateCustomer(conn, stm, resSet, customerId)) {
                    stm = conn.prepareStatement(STM_SELECT_PRIVATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    resSet = stm.executeQuery();
                    if (resSet.next()) {
                        String firstName = resSet.getString(COLUMN_FIRST_NAME);
                        String lastName = resSet.getString(COLUMN_LAST_NAME);
                        PrivateCustomer privateCustomer = new PrivateCustomer(customerId, email, address, phoneNumber, zip_code, city, firstName, lastName);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Customer {0} retrieved.", privateCustomer);
                        return privateCustomer;
                    }
                } else /* if (isCustomerCorporateCustomer) */ {
                    stm = conn.prepareStatement(STM_SELECT_CORPORATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    resSet = stm.executeQuery();
                    if (resSet.next()) {
                        String companyName = resSet.getString(COLUMN_COMPANY_NAME);
                        CorporateCustomer corporateCustomer = new CorporateCustomer(customerId, email, address, phoneNumber, zip_code, city, companyName);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Customer {retrieved].", corporateCustomer);
                        return corporateCustomer;
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Faield to retrieve customer with username {0}.", username);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed retrieve customer with username " + username + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    private boolean isCustomerPrivateCustomer(Connection conn, PreparedStatement stm, ResultSet resSet, int customerId) throws SQLException {
        stm = conn.prepareStatement(STM_INSERT_PRIVATE_CUSTOMER);
        stm.setInt(1, customerId);
        resSet = stm.executeQuery();
        return resSet.next();
    }

    /**
     * TODO: Kalles for å hente ut et enkelt produkt, enten om det er av typen
     * PackageProduct eller SingleProduct.
     *
     * @param productId
     * @return
     */
    public synchronized Product getProduct(int productId) {
        // TODO Hent ut informasjon om et produkt.
        return null;
    }

    /**
     * TODO: Kalles for å hente ut alle (aktive?) produkt. Kommer antagelig til
     * å bli brukt når menyen skal vises fram.
     *
     * @return
     */
    public synchronized ArrayList<Product> getProducts() {
        // TODO Hent ut informasjon om alle produkt.
        return null;
    }

    /**
     * TODO: Kalles når informasjon om en ordre skal hentes ut.
     *
     * @param orderId
     * @return
     */
    public synchronized Order getOrder(int orderId) {
        // TODO Hent ut informasjon om en ordre.
        return null;
    }

    /**
     * TODO: Kalles når informasjon om alle ordre EVER skal hentes ut.
     *
     * @return
     */
    public synchronized ArrayList<Order> getOrders() {
        // TODO Hent ut informasjon om alle ordre.
        return null;
    }

    /**
     * TODO: Kalles når informasjon om alle ordre tilknyttet en spesiell kunde
     * skal hentes ut.
     *
     * @param customerId
     * @return
     */
    public synchronized ArrayList<Order> getOrders(String customerId) {
        // TODO Hent ut informasjon om alle ordre til en spesiell kunde.
        return null;
    }

    // =======================================Metodene under kommer snart... Christer=======================================
    /**
     * Closes a ResultSet in a safe manner.
     *
     * @param resSet The result set to close.
     */
    private void closeResultSet(ResultSet resSet) {
        try {
            if (resSet != null) {
                resSet.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to close result set!", ex);
        }
    }

    /**
     * Closes a Statement in a safe manner.
     *
     * @param stm The statement to close
     */
    private void closeStatement(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to close statement!", ex);
        }
    }

    /**
     * Closes a Connection in a safe manner.
     *
     * @param conn The connection to close.
     */
    private void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to close connection!", ex);
        }
    }

    private void rollBack(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to roll back connection!", ex);
        }
    }

    private void setAutoCommit(Connection conn, boolean newState) {
        try {
            if (conn != null) {
                conn.setAutoCommit(newState);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to set new auto commit state on connection!", ex);
        }
    }

    private void commit(Connection conn) {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to set new auto commit state on connection!", ex);
        }
    }
}
