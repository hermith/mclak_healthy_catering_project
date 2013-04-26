package database;

import info.Contract;
import info.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import shopping.customer.CorporateCustomer;
import shopping.customer.Customer;
import shopping.customer.PrivateCustomer;
import shopping.product.PackageProduct;
import shopping.product.Product;
import shopping.product.SingleProduct;
import user.User;

/**
 * The DatabaseHandler class is the applications only connection to the
 * database. It contains methods for inserting, updating, retrieving data for
 * customers, orders, users, products, contracts and so forth. The class is not
 * named, but application scoped and injected only in other application scoped
 * classes.
 *
 * <br><br>Keep in mind that none of the methods in this class checks the
 * integrity of parameters and such. All null-checks and so forth should be done
 * before passing data to this class.
 *
 * <br><br>Also this class logs
 *
 * @author Christer Olsen
 */
@ApplicationScoped
public class DatabaseHandler {

    //Below are strings representing PreparedStatement objects used in this class.
    private static final String STM_SELECT_USER = "SELECT email AS email, user_role AS user_role FROM User_Table NATURAL JOIN User_Role_Table WHERE username = ?";
    private static final String STM_SELECT_USER_IDS = "SELECT username AS username FROM User_Table";
    private static final String STM_SELECT_EMAIL_FROM_USER_TABLE = "SELECT email AS email FROM User_Table WHERE username = ?";
    private static final String STM_SELECT_PASSWORD = "SELECT password AS password FROM User_Table WHERE username = ?";
    private static final String STM_UPDATE_PASSWORD = "UPDATE User_Table SET password = ? WHERE username = ?";
    private static final String STM_UPDATE_EMAIL_IN_USER_TABLE = "UPDATE User_Table SET email = ? WHERE username = ?";
    private static final String STM_INSERT_USER = "INSERT INTO User_Table(username, password, email) VALUES(?, ?, ?)";
    private static final String STM_INSERT_USER_ROLE = "INSERT INTO User_Role_Table(username, user_role) VALUES(?, ?)";
    private static final String STM_SELECT_CUSTOMER_BY_USERNAME = "SELECT customer_id AS customer_id, email AS email, phone_number AS phone_number, address AS address, zip_code AS zip_code, city AS city FROM Customer_Table WHERE username = ?";
    private static final String STM_SELECT_CUSTOMER_BY_CUSTOMER_ID = "SELECT email AS email, phone_number AS phone_number, address AS address, zip_code AS zip_code, city AS city FROM Customer_Table WHERE customer_id = ?";
    private static final String STM_SELECT_CUSTOMER_IDS = "SELECT customer_id AS customer_id FROM Customer_Table";
    private static final String STM_SELECT_USERNAME_FROM_CUSTOMER_TABLE = "SELECT username AS username FROM Customer_Table WHERE customer_id = ?";
    private static final String STM_SELECT_PRIVATE_CUSTOMER = "SELECT first_name AS first_name, last_name AS last_name FROM Private_Customer_Table WHERE customer_id = ?";
    private static final String STM_SELECT_CORPORATE_CUSTOMER = "SELECT company_name FROM Corporate_Customer_Table WHERE customer_id = ?";
    private static final String STM_INSERT_CUSTOMER = "INSERT INTO Customer_Table(username, email, phone_number, address, zip_code, city) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String STM_INSERT_PRIVATE_CUSTOMER = "INSERT INTO Private_Customer_Table(customer_id, first_name, last_name) VALUES(?, ?, ?)";
    private static final String STM_INSERT_CORPORATE_CUSTOMER = "INSERT INTO Corporate_Customer_Table(customer_id, company_name) VALUES(?, ?)";
    private static final String STM_UPDATE_CUSTOMER = "UPDATE Customer_Table SET email = ?, phone_number = ?, address = ?, zip_code = ?, city = ? WHERE customer_id = ?";
    private static final String STM_UPDATE_EMAIL_IN_CUSTOMER_TABLE = "UPDATE Customer_Table SET email = ? WHERE username = ?";
    private static final String STM_UPDATE_PRIVATE_CUSTOMER = "UPDATE Private_Customer_Table SET first_name = ?, last_name = ? WHERE customer_id = ?";
    private static final String STM_UPDATE_CORPORATE_CUSTOMER = "UPDATE Corporate_Customer_Table SET company_name = ? WHERE customer_id = ?";
    private static final String STM_SELECT_PRODUCT = "SELECT product_name_no AS product_name_no, product_name_en AS product_name_en, product_description_no AS product_description_no, product_description_en AS product_description_en, is_active AS is_active FROM Product_Table WHERE product_id = ?";
    private static final String STM_SELECT_PRODUCT_IDS = "SELECT product_id AS product_id FROM Product_Table";
    private static final String STM_SELECT_SINGLE_PRODUCT = "SELECT product_price AS product_price, product_kcal AS product_kcal FROM Single_Product_Table WHERE product_id = ?";
    private static final String STM_SELECT_PACKAGE_PRODUCT = "SELECT product_discount AS product_discount FROM Package_Product_Table WHERE product_id = ?";
    private static final String STM_SELECT_PACKAGE_SINGLE_PRODUCT_CONNECTION = "SELECT single_product_id AS single_product_id, quantity AS quantity FROM Package_Single_Product_Table WHERE package_product_id = ?";
    private static final String STM_INSERT_PRODUCT = "INSERT INTO Product_Table(product_name_no, product_name_en, product_description_no, product_description_en, is_active) VALUES(?, ?, ?, ?, ?)";
    private static final String STM_INSERT_SINGLE_PRODUCT = "INSERT INTO Single_Product_Table(product_id, product_price, product_kcal) VALUES(?, ?, ?)";
    private static final String STM_INSERT_PACKAGE_PRODUCT = "INSERT INTO Package_Product_Table(product_id, product_discount) VALUES(?, ?)";
    private static final String STM_INSERT_PACKAGE_SINGLE_PRODUCT = "INSERT INTO Package_Single_Product_Table(package_product_id, single_product_id, quantity) VALUES(?, ?, ?)";
    private static final String STM_UPDATE_PRODUCT = "UPDATE Product_Table SET product_name_no = ?, product_name_en = ?, product_description_no = ?, product_description_en = ?, is_active = ? WHERE product_id = ?";
    private static final String STM_UPDATE_PRODUCT_SET_IS_ACTIVE = "UPDATE Product_Table SET is_active = ? WHERE product_id = ?";
    private static final String STM_UPDATE_SINGLE_PRODUCT = "UPDATE Single_Product_Table SET product_price = ?, product_kcal = ? WHERE product_id = ?";
    private static final String STM_UPDATE_PACKAGE_PRODUCT = "UPDATE Package_Product_Table SET product_discount = ? WHERE product_id = ?";
    private static final String STM_UPDATE_PACKAGE_SINGLE_PRODUCT_INCREMENT_QUANTITY = "UPDATE Package_Single_Product_Table SET quantity = ((SELECT quantity FROM Package_Single_Product_Table WHERE package_product_id = ? AND single_product_id = ?) + 1) WHERE package_product_id = ? AND single_product_id = ?";
    private static final String STM_DELETE_PACKAGE_SINGLE_PRODUCTS = "DELETE FROM Package_Single_Product_Table WHERE package_product_id = ?";
    private static final String STM_SELECT_ORDER = "SELECT customer_id AS customer_id, date_placed AS date_placed, date_to_be_delivered AS date_to_be_delivered, date_delivered AS date_delivered, is_delivery AS is_delivery, is_active AS is_active, is_prepared AS is_prepared FROM Order_Table WHERE order_id = ?";
    private static final String STM_SELECT_ORDER_PRODUCTS = "SELECT product_id AS product_id, quantity AS quantity FROM Order_Product_Table WHERE order_id = ?";
    private static final String STM_SELECT_ORDER_IDS = "SELECT order_id AS order_id FROM Order_Table";
    private static final String STM_SELECT_ORDER_IDS_BASED_ON_CUSTOMER_ID = "SELECT order_id AS order_id FROM Order_Table WHERE customer_id = ?";
    private static final String STM_INSERT_ORDER = "INSERT INTO Order_Table(customer_id, date_placed, date_to_be_delivered, date_delivered, is_delivery, is_active, is_prepared) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String STM_INSERT_ORDER_PRODUCT = "INSERT INTO Order_Product_Table(order_id, product_id, quantity) VALUES(?, ?, ?)";
    private static final String STM_UPDATE_ORDER = "UPDATE Order_Table SET customer_id = ?, date_placed = ?, date_to_be_delivered = ?, date_delivered = ?, is_delivery = ?, is_active = ?, is_prepared = ? WHERE order_id = ?";
    private static final String STM_UPDATE_ORDER_SET_IS_PREPARED = "UPDATE Order_Table SET is_prepared = ? WHERE order_id = ?";
    private static final String STM_UPDATE_ORDER_SET_DATE_DELIVERED = "UPDATE Order_Table SET date_delivered = ? WHERE order_id = ?";
    private static final String STM_UPDATE_ORDER_SET_IS_ACTIVE = "UPDATE Order_Table SET is_active = ? WHERE order_id = ?";
    private static final String STM_DELETE_ORDER_PRODUCTS = "DELETE FROM Order_Product_Table WHERE order_id = ?";
    private static final String STM_DELETE_ORDER = "DELETE FROM Order_Table WHERE order_id = ?";
    private static final String STM_SELECT_CONTRACT = "SELECT order_id AS order_id, day_of_the_week AS day_of_the_week, is_active AS is_active WHERE contract_id = ?";
    private static final String STM_SELECT_CONTRACT_IDS = "SELECT contract_id AS contract_id FROM Contract_Table";
    private static final String STM_SELECT_CONTRACT_IDS_BASED_ON_CUSTOMER_ID = "SELECT contract_id AS contract_id FROM Contract_Table WHERE order_id = (SELECT DISTINCT order_id FROM Order_Table WHERE customer_id = ?)";
    private static final String STM_INSERT_CONTRACT = "INSERT INTO Contract_Table(order_id, day_of_the_week, time_of_delivery, is_active) VALUES(?, ?, ?, ?)";
    private static final String STM_UPDATE_CONTRACT = "UPDATE Contract_Table SET order_id = ?, day_of_the_week = ?, time_of_delivery = ?, is_active = ? WHERE contract_id = ?";
    private static final String STM_UPDATE_CONTRACT_SET_IS_ACTIVE = "UPDATE Contract_Table SET is_active = ? WHERE order_id = ?";
    // Below are column names used in this class for retrieving data from ResultSet objects.
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
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME_NO = "product_name_no";
    private static final String COLUMN_PRODUCT_NAME_EN = "product_name_en";
    private static final String COLUMN_PRODUCT_DESCRIPTION_NO = "product_description_no";
    private static final String COLUMN_PRODUCT_DESCRIPTION_EN = "product_description_en";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_KCAL = "product_kcal";
    private static final String COLUMN_PRODUCT_DISCOUNT = "product_discount";
    private static final String COLUMN_SINGLE_PRODUCT_ID = "single_product_id";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_DATE_PLACED = "date_placed";
    private static final String COLUMN_DATE_TO_BE_DELIVERED = "date_to_be_delivered";
    private static final String COLUMN_DATE_DELIVERED = "date_delivered";
    private static final String COLUMN_IS_DELIVERY = "is_delivery";
    private static final String COLUMN_IS_ACTIVE = "is_active";
    private static final String COLUMN_IS_PREPARED = "is_prepared";
    private static final String COLUMN_CONTRACT_ID = "contract_id";
    private static final String COLUMN_DAY_OF_THE_WEEK = "day_of_the_week";
    private static final String COLUMN_TIME_OF_DELIVERY = "time_of_delivery";
    /**
     * String containing the name of the connection pool used for getting
     * connections to the database.
     */
    private static final String POOL_NAME = "jdbc/sysproj_res";
    private boolean productsTableChanged;
    private DataSource dataSource;

    /**
     * This constructor initiates the object variable 'dataSource' by
     * looking up the pool name given by the POOL_NAME constant. The constructor
     * also initiates the object variable productsTableChanged to true in order
     * to force the ShoppingHandler class to retrieve the product menu from the
     * database upon initiation.
     */
    public DatabaseHandler() {
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Attempting to creating a new database handler...");
        productsTableChanged = true;
        try {
            dataSource = (DataSource) new InitialContext().lookup(POOL_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to create a new database handler!", ex);
        }
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "New database handler successfully created!");
    }

    /**
     * This method returns the productsTableChanged variable. This variable is
     * set to false if the menu has been retrieved from the database since the
     * last change in the product table, and set to true if it has not.
     *
     * @return productsTableChanged variable.
     */
    public synchronized boolean hasProductsTableChanged() {
        return productsTableChanged;
    }

    /**
     * Sets the productsTableChanged variable to a new given value. This method
     * should primarily be called by the ShoppingHandler when the menu has been
     * retrieved from the database.
     *
     * @param value the new value of the productsTableChanged.
     */
    public synchronized void setProductsTableChanged(boolean value) {
        productsTableChanged = value;
    }

    /**
     * Method used in retrieving a user from the User_Table and User_Role_Table
     * with a given username.
     *
     * @param username The username of the user to retrieve.
     * @return The user object representing the username, null if the user does
     * not exist or if an SQLException is thrown.
     */
    public synchronized User selectUser(String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_USER);
            stm.setString(1, username);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                User user = new User(username, resSet.getString(COLUMN_USER_ROLE), resSet.getString(COLUMN_EMAIL));
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "User {0} retrieved.", user);
                return user;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve user with username {0}.", username);
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
     * Method used for retrieving all users in the database. The method gets its
     * data from the User_Table and the User_Role_Table. If an error occurs
     * during the retrieval of a user the user is skipped and the process
     * continues.
     *
     * @return An ArrayList containing all User objects.
     */
    public synchronized ArrayList<User> selectUsers() {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        boolean errorEncountered = false;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_USER_IDS);
            resSet = stm.executeQuery();
            ArrayList<User> users = new ArrayList<User>();
            while (resSet.next()) {
                String username = resSet.getString(COLUMN_USERNAME);
                User user = selectUser(username);
                if (user != null) {
                    users.add(user);
                } else {
                    errorEncountered = true;
                }
            }
            if (errorEncountered) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL users.");
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL users retrieved successfully.");
            }
            return users;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL users.", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method user for inserting a new user into the database. Inserts data into
     * the User_Table and User_Role_Table. If an error occurs during insertion
     * the process is aborted and whatever data, if any, that got inserted get
     * rolled back.
     *
     * @param user The User object representing the user to insert.
     * @return True if the user was successfully inserted, false if not.
     */
    public synchronized boolean insertUser(User user) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_INSERT_USER);
            stm.setString(1, user.getUsername().trim());
            stm.setString(2, user.getPassword().trim());
            stm.setString(3, user.getEmail().trim());
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
            rollBack(conn);
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
     * Method used for retrieving the password for a specific user represented
     * by a username. This method should only be called when a user wishes to
     * change his or her password and should never be printed, not even in the
     * console or server log.
     *
     * <br><br>The method uses data from the User_Table to do this.
     *
     * @param username The username of the user which password to retrieve.
     * @return The password for the given user, null if the user is not found or
     * an SQLException is thrown.
     */
    public synchronized String selectUserPassword(String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_PASSWORD);
            stm.setString(1, username);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Password for user {0} retrieved.", username);
                return resSet.getString(COLUMN_PASSWORD);
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Password for user {0} NOT retrieved.", username);
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve password for user " + username + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for updating a given users password. The method uses and
     * updates data in the User_Table to do this.
     *
     * @param password The new password.
     * @param username The username of the user.
     * @return True if the password was changed, false if not.
     */
    public synchronized boolean updateUserPassword(String password, String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_UPDATE_PASSWORD);
            stm.setString(1, password);
            stm.setString(2, username);
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Password for user {0} changed.", username);
                return true;
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to change password for user {0}.", username);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to change password for user " + username + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for updating a users email address. The method updates data
     * in the User_Table and in the Customer_Table.
     *
     * @param email The new email.
     * @param username The username of the user.
     * @return True if the email was changes successfully in both tables, false
     * if not.
     */
    public synchronized boolean updateUserEmail(String email, String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_UPDATE_EMAIL_IN_USER_TABLE);
            stm.setString(1, email);
            stm.setString(2, username);
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                stm = conn.prepareStatement(STM_UPDATE_EMAIL_IN_CUSTOMER_TABLE);
                stm.setString(1, email);
                stm.setString(2, username);
                numberOfRowsUpdated = stm.executeUpdate();
                if (numberOfRowsUpdated == -1) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Password for user {0} changed.", username);
                    commit(conn);
                    return true;
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to change password for user {0}.", username);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to change password for user " + username + ".", ex);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for retrieving a customer from the database with a given
     * username. The method uses data in the Customer_Table and either the
     * Private_Customer_Table or the Corporate_Customer_Table to do this.
     *
     * @param username The username of the customer to retrieve.
     * @return A Customer object representing the customer.
     */
    public synchronized Customer selectCustomer(String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CUSTOMER_BY_USERNAME);
            stm.setString(1, username);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                int customerId = resSet.getInt(COLUMN_CUSTOMER_ID);
                String email = resSet.getString(COLUMN_EMAIL);
                String phoneNumber = resSet.getString(COLUMN_PHONE_NUMBER);
                String address = resSet.getString(COLUMN_ADDRESS);
                int zip_code = resSet.getInt(COLUMN_ZIP_CODE);
                String city = resSet.getString(COLUMN_CITY);
                if (isCustomerAPrivateCustomer(conn, stm, resSet, customerId)) {
                    stm = conn.prepareStatement(STM_SELECT_PRIVATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    resSet = stm.executeQuery();
                    if (resSet.next()) {
                        String firstName = resSet.getString(COLUMN_FIRST_NAME);
                        String lastName = resSet.getString(COLUMN_LAST_NAME);
                        PrivateCustomer privateCustomer = new PrivateCustomer(customerId, email, address, phoneNumber, zip_code, city, firstName, lastName);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Private customer {0} retrieved.", privateCustomer);
                        return privateCustomer;
                    }
                } else /* if (isCustomerCorporateCustomer) */ {
                    stm = conn.prepareStatement(STM_SELECT_CORPORATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    resSet = stm.executeQuery();
                    if (resSet.next()) {
                        String companyName = resSet.getString(COLUMN_COMPANY_NAME);
                        CorporateCustomer corporateCustomer = new CorporateCustomer(customerId, email, address, phoneNumber, zip_code, city, companyName);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Corporate customer {0} retrieved.", corporateCustomer);
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

    /**
     * Method used for retrieving a customer from the database with a given
     * customer id. The method uses data in the Customer_Table and either the
     * Private_Customer_Table or the Corporate_Customer_Table to do this.
     *
     * @param customerId The customer id.
     * @return A Customer object representing the customer.
     */
    public synchronized Customer selectCustomer(int customerId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CUSTOMER_BY_CUSTOMER_ID);
            stm.setInt(1, customerId);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                String email = resSet.getString(COLUMN_EMAIL);
                String phoneNumber = resSet.getString(COLUMN_PHONE_NUMBER);
                String address = resSet.getString(COLUMN_ADDRESS);
                int zip_code = resSet.getInt(COLUMN_ZIP_CODE);
                String city = resSet.getString(COLUMN_CITY);
                if (isCustomerAPrivateCustomer(conn, stm, resSet, customerId)) {
                    stm = conn.prepareStatement(STM_SELECT_PRIVATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    resSet = stm.executeQuery();
                    if (resSet.next()) {
                        String firstName = resSet.getString(COLUMN_FIRST_NAME);
                        String lastName = resSet.getString(COLUMN_LAST_NAME);
                        PrivateCustomer privateCustomer = new PrivateCustomer(customerId, email, address, phoneNumber, zip_code, city, firstName, lastName);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Private customer {0} retrieved.", privateCustomer);
                        return privateCustomer;
                    }
                } else /* if (isCustomerCorporateCustomer) */ {
                    stm = conn.prepareStatement(STM_SELECT_CORPORATE_CUSTOMER);
                    stm.setInt(1, customerId);
                    resSet = stm.executeQuery();
                    if (resSet.next()) {
                        String companyName = resSet.getString(COLUMN_COMPANY_NAME);
                        CorporateCustomer corporateCustomer = new CorporateCustomer(customerId, email, address, phoneNumber, zip_code, city, companyName);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Corporate customer {0} retrieved.", corporateCustomer);
                        return corporateCustomer;
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve customer with customerId {0}.", customerId);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed retrieve customer with customerId " + customerId + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for retrieving all customers stored in the database. The
     * method uses data in the Customer_Table and either the
     * Private_Customer_Table or the Corporate_Customer_Table to do this. Also,
     * if an error occurs while retrieving a customer, the customer is skipped
     * and the process continues. In other words: as many customers as possible
     * is retrieved.
     *
     * @return An ArrayList containing all Customer objects.
     */
    public synchronized ArrayList<Customer> selectCustomers() {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        boolean errorEncountered = false;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CUSTOMER_IDS);
            resSet = stm.executeQuery();
            ArrayList<Customer> customers = new ArrayList<Customer>();
            while (resSet.next()) {
                int customerId = resSet.getInt(COLUMN_CUSTOMER_ID);
                Customer customer = selectCustomer(customerId);
                if (customer != null) {
                    customers.add(customer);
                } else {
                    errorEncountered = true;
                }
            }
            if (errorEncountered) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL customers. See glassfish log for details.");
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL customers retrieved successfully.");
            }
            return customers;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL customers.", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for inserting a new customer into the database. The method
     * updates data in the Customer_Table and either the Private_Customer_Table
     * or the Corporate_Customer_Table. If an error occurs during insertion, the
     * process is aborted the data data, if any, that got inserted gets rolled
     * back.
     *
     * @param customer The customer to be inserted.
     * @param username The username of the user connected to this customer.
     * @return True if the customer was inserted, false if not.
     */
    public synchronized boolean insertCustomer(Customer customer, String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        int numberOfRowsUpdated = -1;
        int customerId = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_SELECT_EMAIL_FROM_USER_TABLE);
            stm.setString(1, username);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                String email = resSet.getString(COLUMN_EMAIL);
                stm = conn.prepareStatement(STM_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
                stm.setString(1, username.trim());
                stm.setString(2, email.trim());
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
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Customer {0} with username {1} registered.", new Object[]{customer, username});
                        commit(conn);
                        return true;
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Customer {0} with username {1} NOT registered.", new Object[]{customer, username});
            rollBack(conn);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Customer " + customer + " with username " + username + " NOT registered.", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Updates information about a customer in the database. The method updates
     * data in the Customer_Table and either the Private_Customer_Table or the
     * Corporate_Customer_Table. If an error occurs during insertion, the
     * process is aborted the data data, if any, that got inserted gets rolled
     *
     * @param customer The customer object containing the updated information.
     * @return True if the information was successfully updated, false if not.
     */
    public synchronized boolean updateCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_SELECT_USERNAME_FROM_CUSTOMER_TABLE);
            stm.setInt(1, customer.getCustomerId());
            resSet = stm.executeQuery();
            if (resSet.next()) {
                String username = resSet.getString(COLUMN_USERNAME);
                stm = conn.prepareStatement(STM_UPDATE_EMAIL_IN_USER_TABLE);
                stm.setString(1, customer.getEmail());
                stm.setString(2, username);
                numberOfRowsUpdated = stm.executeUpdate();
                if (numberOfRowsUpdated == 1) {
                    numberOfRowsUpdated = -1;
                    stm = conn.prepareStatement(STM_UPDATE_CUSTOMER);
                    stm.setString(1, customer.getEmail());
                    stm.setString(2, customer.getPhoneNumber());
                    stm.setString(3, customer.getAddress());
                    stm.setInt(4, customer.getZipCode());
                    stm.setString(5, customer.getCity());
                    stm.setInt(6, customer.getCustomerId());
                    numberOfRowsUpdated = stm.executeUpdate();
                    if (numberOfRowsUpdated == 1) {
                        numberOfRowsUpdated = -1;
                        if (customer instanceof PrivateCustomer) {
                            PrivateCustomer privateCustomer = (PrivateCustomer) customer;
                            stm = conn.prepareStatement(STM_UPDATE_PRIVATE_CUSTOMER);
                            stm.setString(1, privateCustomer.getFirstName());
                            stm.setString(2, privateCustomer.getLastName());
                            stm.setInt(3, privateCustomer.getCustomerId());
                            numberOfRowsUpdated = stm.executeUpdate();
                        } else if (customer instanceof CorporateCustomer) {
                            CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
                            stm = conn.prepareStatement(STM_UPDATE_CORPORATE_CUSTOMER);
                            stm.setString(1, corporateCustomer.getCompanyName());
                            stm.setInt(2, corporateCustomer.getCustomerId());
                            numberOfRowsUpdated = stm.executeUpdate();
                        }
                        if (numberOfRowsUpdated == 1) {
                            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Customer {0} updated.", customer);
                            commit(conn);
                            return true;
                        }
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Customer {0} NOT updated.", customer);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Customer " + customer + " NOT updated.", ex);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for updating multiple customers at once. This method uses the
     * updateCustomer(Customer) method to do this, and therefore it is not
     * guaranteed that all customers passed gets updated. In other words: this
     * method doesn't stop if an error occurs. It only skips the the customer
     * that caused the error continues.
     *
     * @param customers The customers to be updated.
     * @return True if ALL customers was successfully updated, false if not.
     */
    public synchronized boolean updateCustomers(ArrayList<Customer> customers) {
        int numberOfCustomersUpdated = 0;
        for (Customer customer : customers) {
            if (updateCustomer(customer)) {
                numberOfCustomersUpdated++;
            }
        }
        if (numberOfCustomersUpdated == customers.size()) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully updated customers {0}", customers);
            return true;
        } else {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed update customers! Only {0} out of {1} customers updated. See glassfish log for details.", new Object[]{numberOfCustomersUpdated, customers.size()});
            return false;
        }
    }

    /**
     * Method used for retrieving a product from the database with a given
     * product id. The Method uses data from the Product_Table, and possibly the
     * Single_Product_Table, the Package_Product_Table and the
     * Package_Single_Product_Table to do this.
     *
     * @param productId the id of the product to retrieve.
     * @return The product corresponding to the product id. null if the product
     * id is invalid or and SQLException was thrown.
     */
    public synchronized Product selectProduct(int productId) {
        Connection conn = null;
        PreparedStatement stm1 = null;
        PreparedStatement stm2 = null;
        ResultSet resSet1 = null;
        ResultSet resSet2 = null;
        try {
            conn = dataSource.getConnection();
            stm1 = conn.prepareStatement(STM_SELECT_PRODUCT);
            stm1.setInt(1, productId);
            resSet1 = stm1.executeQuery();
            if (resSet1.next()) {
                String nameNo = resSet1.getString(COLUMN_PRODUCT_NAME_NO);
                String nameEn = resSet1.getString(COLUMN_PRODUCT_NAME_EN);
                String descriptionNo = resSet1.getString(COLUMN_PRODUCT_DESCRIPTION_NO);
                String descriptionEn = resSet1.getString(COLUMN_PRODUCT_DESCRIPTION_EN);
                boolean active = resSet1.getBoolean(COLUMN_IS_ACTIVE);
                if (isProductInSingleProductTable(conn, stm1, resSet1, productId)) {
                    stm1 = conn.prepareStatement(STM_SELECT_SINGLE_PRODUCT);
                    stm1.setInt(1, productId);
                    resSet1 = stm1.executeQuery();
                    if (resSet1.next()) {
                        float price = resSet1.getFloat(COLUMN_PRODUCT_PRICE);
                        int kcal = resSet1.getInt(COLUMN_PRODUCT_KCAL);
                        SingleProduct singleProduct = new SingleProduct(productId, nameNo, nameEn, descriptionNo, descriptionEn, price, kcal);
                        singleProduct.setActive(active);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Single product {0} retrieved.", singleProduct);
                        return singleProduct;
                    }
                } else /* if (isProductAPackageProduct) */ {
                    stm1 = conn.prepareStatement(STM_SELECT_PACKAGE_PRODUCT);
                    stm1.setInt(1, productId);
                    resSet1 = stm1.executeQuery();
                    if (resSet1.next()) {
                        int discount = resSet1.getInt(COLUMN_PRODUCT_DISCOUNT);
                        ArrayList<SingleProduct> products = new ArrayList<SingleProduct>();
                        stm1 = conn.prepareStatement(STM_SELECT_PACKAGE_SINGLE_PRODUCT_CONNECTION);
                        stm1.setInt(1, productId);
                        resSet1 = stm1.executeQuery();
                        stm1 = conn.prepareStatement(STM_SELECT_PRODUCT);
                        stm2 = conn.prepareStatement(STM_SELECT_SINGLE_PRODUCT);
                        while (resSet1.next()) {
                            int singleProductId = resSet1.getInt(COLUMN_SINGLE_PRODUCT_ID);
                            int quantity = resSet1.getInt(COLUMN_QUANTITY);
                            stm1.setInt(1, singleProductId);
                            resSet2 = stm1.executeQuery();
                            if (resSet2.next()) {
                                String singleProductNameNo = resSet2.getString(COLUMN_PRODUCT_NAME_NO);
                                String singleProductNameEn = resSet2.getString(COLUMN_PRODUCT_NAME_EN);
                                String singleProductDescriptionNo = resSet2.getString(COLUMN_PRODUCT_DESCRIPTION_NO);
                                String singleProductDescriptionEn = resSet2.getString(COLUMN_PRODUCT_DESCRIPTION_EN);
                                boolean singleProductActive = resSet2.getBoolean(COLUMN_IS_ACTIVE);
                                stm2.setInt(1, singleProductId);
                                resSet2 = stm2.executeQuery();
                                if (resSet2.next()) {
                                    float singleProductPrice = resSet2.getFloat(COLUMN_PRODUCT_PRICE);
                                    int singleProductKcal = resSet2.getInt(COLUMN_PRODUCT_KCAL);
                                    for (int i = 0; i < quantity; i++) {
                                        SingleProduct sp = new SingleProduct(singleProductId, singleProductNameNo, singleProductNameEn, singleProductDescriptionNo, singleProductDescriptionEn, singleProductPrice, singleProductKcal);
                                        sp.setActive(singleProductActive);
                                        products.add(sp);
                                    }
                                }
                            }
                        }
                        PackageProduct packageProduct = new PackageProduct(productId, nameNo, nameEn, descriptionNo, descriptionEn, discount, products);
                        packageProduct.setActive(active);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Package product {0} retrieved.", packageProduct);
                        return packageProduct;
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Product with product id {0} NOT retrieved.", productId);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Product with product id " + productId + " NOT retrieved.", ex);
            return null;
        } finally {
            closeResultSet(resSet1);
            closeResultSet(resSet2);
            closeStatement(stm1);
            closeStatement(stm2);
            closeConnection(conn);
        }
    }

    /**
     * Method used for retrieving all products in the database. The method uses
     * the selectProduct(int) to do this, and therefore only as many products as
     * possible is retrieved. If and error occurs during the retrieval of a
     * product, the product is skipped and the process continues.
     *
     * @return An ArrayList containing all products in the database.
     */
    public synchronized ArrayList<Product> selectProducts() {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        boolean errorEncountered = false;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_PRODUCT_IDS);
            resSet = stm.executeQuery();
            ArrayList<Product> products = new ArrayList<Product>();
            while (resSet.next()) {
                int productId = resSet.getInt(COLUMN_PRODUCT_ID);
                Product product = selectProduct(productId);
                if (product != null) {
                    products.add(product);
                } else {
                    errorEncountered = true;
                }
            }
            if (errorEncountered) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL products. See glassfish log for details.");
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL products retrieved successfully.");
            }
            return products;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL products.", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for inserting a new product into the database. The method
     * updates data in the Product_Table, and possibly the Single_Product_Table,
     * the Package_Product_Table and the Package_Single_Product_Table. If an
     * error occurs during insertion, the process is aborted and any data
     * inserted gets rolled back. this.
     *
     * @param product The product to be inserted.
     * @return True if the product was successfully inserted, false if not.
     */
    public synchronized boolean insertProduct(Product product) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        int numberOfRowsUpdated = -1;
        int productId = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, product.getNameNo());
            stm.setString(2, product.getNameEn());
            stm.setString(3, product.getDescriptionNo());
            stm.setString(4, product.getDescriptionEn());
            stm.setInt(5, ((product.isActive()) ? 1 : 0));
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                stm.getGeneratedKeys().next();
                productId = stm.getGeneratedKeys().getInt(1);
                if (product instanceof SingleProduct) {
                    SingleProduct singleProduct = (SingleProduct) product;
                    stm = conn.prepareStatement(STM_INSERT_SINGLE_PRODUCT);
                    stm.setInt(1, productId);
                    stm.setFloat(2, singleProduct.getPrice());
                    stm.setInt(3, singleProduct.getKcal());
                    numberOfRowsUpdated = stm.executeUpdate();
                    if (numberOfRowsUpdated == 1) {
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully inserted single product {0}.", singleProduct);
                        commit(conn);
                        productsTableChanged = true;
                        return true;
                    }
                } else if (product instanceof PackageProduct) {
                    PackageProduct packageProduct = (PackageProduct) product;
                    stm = conn.prepareStatement(STM_INSERT_PACKAGE_PRODUCT);
                    stm.setInt(1, productId);
                    stm.setInt(2, packageProduct.getDiscount());
                    numberOfRowsUpdated = stm.executeUpdate();
                    if (numberOfRowsUpdated == 1) {
                        ArrayList<SingleProduct> singleProductsInserted = new ArrayList<SingleProduct>();
                        for (SingleProduct p : packageProduct.getProducts()) {
                            numberOfRowsUpdated = -1;
                            if (!isProductInSingleProductTable(conn, stm, resSet, p.getId())) {
                                stm = conn.prepareStatement(STM_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS);
                                stm.setString(1, p.getNameNo());
                                stm.setString(2, p.getNameEn());
                                stm.setString(3, p.getDescriptionNo());
                                stm.setString(4, p.getDescriptionEn());
                                stm.setInt(5, ((product.isActive()) ? 1 : 0));
                                numberOfRowsUpdated = stm.executeUpdate();
                                stm.getGeneratedKeys().next();
                                p.setId(stm.getGeneratedKeys().getInt(1));
                                if (numberOfRowsUpdated == 1) {
                                    stm = conn.prepareStatement(STM_INSERT_SINGLE_PRODUCT);
                                    stm.setInt(1, p.getId());
                                    stm.setFloat(2, p.getPrice());
                                    stm.setInt(3, p.getKcal());
                                    stm.executeUpdate();
                                }
                            }
                            if (singleProductsInserted.contains(p)) {
                                stm = conn.prepareStatement(STM_UPDATE_PACKAGE_SINGLE_PRODUCT_INCREMENT_QUANTITY);
                                stm.setInt(1, productId);
                                stm.setInt(2, p.getId());
                                stm.setInt(3, productId);
                                stm.setInt(4, p.getId());
                                stm.executeUpdate();
                            } else {
                                numberOfRowsUpdated = -1;
                                stm = conn.prepareStatement(STM_INSERT_PACKAGE_SINGLE_PRODUCT);
                                stm.setInt(1, productId);
                                stm.setInt(2, p.getId());
                                stm.setInt(3, 1);
                                numberOfRowsUpdated = stm.executeUpdate();
                                if (numberOfRowsUpdated == 1) {
                                    singleProductsInserted.add(p);
                                }
                            }
                        }
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully inserted package product {0}.", packageProduct);
                        commit(conn);
                        productsTableChanged = true;
                        return true;
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to insert product {0}.", product);
            rollBack(conn);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to insert product " + product + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method for inserting multiple products at once. The method uses the
     * insertProduct(Product) method, and therefore only inserts as many
     * products as possible. Any products that causes an error during insertion
     * will be skipped.
     *
     * @param products The products to be inserted.
     * @return true if ALL products got inserted successfully, false if not.
     */
    public synchronized boolean insertProducts(ArrayList<Product> products) {
        int numberOfProductsInserted = 0;
        for (Product product : products) {
            if (insertProduct(product)) {
                numberOfProductsInserted++;
            }
        }
        if (numberOfProductsInserted == products.size()) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully inserted products {0}", products);
            return true;
        } else {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed insert products! Only {0} out of {1} products updated. See glassfish log for details.", new Object[]{numberOfProductsInserted, products.size()});
            return false;
        }
    }

    /**
     * Method used for updating a product in the database. The method updates
     * data in the Product_Table, and possibly the Single_Product_Table, the
     * Package_Product_Table and the Package_Single_Product_Table. If an error
     * occurs during insertion, the process is aborted and any data inserted
     * gets rolled back. this.
     *
     * @param product The Product object containing the updated information
     * about the product.
     * @return True if the product was updated successfully, false if not.
     */
    public synchronized boolean updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_UPDATE_PRODUCT);
            stm.setString(1, product.getNameNo());
            stm.setString(2, product.getNameEn());
            stm.setString(3, product.getDescriptionNo());
            stm.setString(4, product.getDescriptionEn());
            stm.setInt(5, ((product.isActive()) ? 1 : 0));
            stm.setInt(6, product.getId());
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                if (product instanceof SingleProduct) {
                    SingleProduct singleProduct = (SingleProduct) product;
                    stm = conn.prepareStatement(STM_UPDATE_SINGLE_PRODUCT);
                    stm.setFloat(1, singleProduct.getPrice());
                    stm.setInt(2, singleProduct.getKcal());
                    stm.setInt(3, singleProduct.getId());
                    numberOfRowsUpdated = stm.executeUpdate();
                    if (numberOfRowsUpdated == 1) {
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully updated single product {0}.", singleProduct);
                        commit(conn);
                        productsTableChanged = true;
                        return true;
                    }
                } else if (product instanceof PackageProduct) {
                    PackageProduct packageProduct = (PackageProduct) product;
                    stm = conn.prepareStatement(STM_UPDATE_PACKAGE_PRODUCT);
                    stm.setInt(1, packageProduct.getDiscount());
                    stm.setInt(2, packageProduct.getId());
                    numberOfRowsUpdated = stm.executeUpdate();
                    if (numberOfRowsUpdated == 1) {
                        numberOfRowsUpdated = -1;
                        stm = conn.prepareStatement(STM_DELETE_PACKAGE_SINGLE_PRODUCTS);
                        stm.setInt(1, packageProduct.getId());
                        numberOfRowsUpdated = stm.executeUpdate();
                        if (numberOfRowsUpdated > 0) {
                            ArrayList<SingleProduct> productsUpdated = new ArrayList<SingleProduct>();
                            stm = conn.prepareStatement(STM_INSERT_PACKAGE_SINGLE_PRODUCT);
                            for (SingleProduct p : packageProduct.getProducts()) {
                                if (!productsUpdated.contains(p)) {
                                    numberOfRowsUpdated = -1;
                                    int quantity = 0;
                                    for (SingleProduct p2 : packageProduct.getProducts()) {
                                        if (p.equals(p2)) {
                                            quantity++;
                                        }
                                    }
                                    stm.setInt(1, packageProduct.getId());
                                    stm.setInt(2, p.getId());
                                    stm.setInt(3, quantity);
                                    numberOfRowsUpdated = stm.executeUpdate();
                                    if (numberOfRowsUpdated == 1) {
                                        productsUpdated.add(p);
                                    } else {
                                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update package product {0}.", packageProduct);
                                        rollBack(conn);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Package product {0} successfully updated.", packageProduct);
                    commit(conn);
                    productsTableChanged = true;
                    return true;
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update product {0}.", product);
            rollBack(conn);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to update product " + product + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for updating multiple products at once. The method uses the
     * updateProduct(Product) method to do this, and therefore only updates as
     * many products as possible. Any products the causes an error will be
     * skipped.
     *
     * @param products The products to be updated.
     * @return True if ALL products passed was updated successfully, false if
     * not.
     */
    public synchronized boolean updateProducts(ArrayList<Product> products) {
        int numberOfProductsUpdated = 0;
        for (Product product : products) {
            if (updateProduct(product)) {
                numberOfProductsUpdated++;
            }
        }
        if (numberOfProductsUpdated == products.size()) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully updated products {0}", products);
            return true;
        } else {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed update products! Only {0} out of {1} products updated. See glassfish log for details.", new Object[]{numberOfProductsUpdated, products.size()});
            return false;
        }
    }

    /**
     * Method used for only updating whether or not a product is active or not.
     * The method updates data in the Product_Table only.
     *
     * @param active The new status of the product.
     * @param productId The product id.
     * @return True if the new status was set successfully, false if not.
     */
    public synchronized boolean updateProductSetActive(boolean active, int productId) {
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_UPDATE_PRODUCT_SET_IS_ACTIVE);
            stm.setInt(1, ((active) ? 1 : 0));
            stm.setInt(2, productId);
            stm.executeUpdate();
            productsTableChanged = true;
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Product with product id {0} got new status set to.", new Object[]{productId, active});
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed set new status for product with product id " + productId + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    /**
     * Method used for retrieving a specific order from the database. The method
     * uses data from the Order_Table, Order_Product_Table, the Product_Table,
     * and possibly the Single_Product_Table, the Package_Product_Table and the
     * Package_Single_Product_Table to do this.
     *
     * @param orderId the id of the order to retrieve.
     * @return An Order object representing the order, or null if the order id
     * was invalid or if an SQLException was thrown.
     */
    public synchronized Order selectOrder(int orderId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_ORDER);
            stm.setInt(1, orderId);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                int customerId = resSet.getInt(COLUMN_CUSTOMER_ID);
                Timestamp datePlaced = resSet.getTimestamp(COLUMN_DATE_PLACED);
                Timestamp dateToBeDelivered = resSet.getTimestamp(COLUMN_DATE_TO_BE_DELIVERED);
                Timestamp dateDelivered = resSet.getTimestamp(COLUMN_DATE_DELIVERED);
                boolean delivery = resSet.getBoolean(COLUMN_IS_DELIVERY);
                boolean active = resSet.getBoolean(COLUMN_IS_ACTIVE);
                boolean prepared = resSet.getBoolean(COLUMN_IS_PREPARED);
                Order order = new Order(orderId, customerId, new ArrayList<Product>(), datePlaced, dateToBeDelivered, dateDelivered, delivery);
                order.setActive(active);
                order.setPrepared(prepared);
                stm = conn.prepareStatement(STM_SELECT_ORDER_PRODUCTS);
                stm.setInt(1, orderId);
                resSet = stm.executeQuery();
                while (resSet.next()) {
                    int productId = resSet.getInt(COLUMN_PRODUCT_ID);
                    int quantity = resSet.getInt(COLUMN_QUANTITY);
                    Product product = selectProduct(productId);
                    for (int i = 0; i < quantity; i++) {
                        order.getProducts().add(product);
                    }
                }
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Retrieved order {0}. successfully", order);
                return order;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed retrieve order with order id {0}.", orderId);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed retrieve order with order id " + orderId + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized ArrayList<Order> selectOrders(int customerId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_ORDER_IDS_BASED_ON_CUSTOMER_ID);
            stm.setInt(1, customerId);
            resSet = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<Order>();
            while (resSet.next()) {
                int orderId = resSet.getInt(COLUMN_ORDER_ID);
                Order order = selectOrder(orderId);
                if (order != null) {
                    orders.add(order);
                } else {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL orders with customer id {0}.", customerId);
                    return null;
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL orders with customer id {0} retrieved successfully.", customerId);
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL orders with customer id " + customerId + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized ArrayList<Order> selectOrders() {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        boolean errorEncountered = false;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_ORDER_IDS);
            resSet = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<Order>();
            while (resSet.next()) {
                int orderId = resSet.getInt(COLUMN_ORDER_ID);
                Order order = selectOrder(orderId);
                if (order != null) {
                    orders.add(order);
                } else {
                    errorEncountered = true;
                }
            }
            if (errorEncountered) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL orders. See glashfish log for details.");
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL orders retrieved successfully.");
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL orders.", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized ArrayList<Order> selectUndeliveredOrders() {
        ArrayList<Order> allOrders = selectOrders();
        if (allOrders != null) {
            ArrayList<Order> undeliveredOrders = new ArrayList<Order>();
            for (Order o : allOrders) {
                if (o.getDeliveredDate() == null) {
                    undeliveredOrders.add(o);
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL undelivered orders retrieved successfully.");
            return undeliveredOrders;
        }
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve all undelivered orders.");
        return null;
    }

    public synchronized ArrayList<Order> selectDeliveredOrders() {
        ArrayList<Order> allOrders = selectOrders();
        if (allOrders != null) {
            ArrayList<Order> deliveredOrders = new ArrayList<Order>();
            for (Order o : allOrders) {
                if (o.getDeliveredDate() != null) {
                    deliveredOrders.add(o);
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL delivered orders retrieved successfully.");
            return deliveredOrders;
        }
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve all delivered orders.");
        return null;
    }

    public synchronized boolean insertOrder(Order order) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, order.getCustomerID());
            stm.setTimestamp(2, order.getPlacedDate());
            stm.setTimestamp(3, order.getDeliveryDate());
            stm.setTimestamp(4, order.getDeliveredDate());
            stm.setInt(5, ((order.isDelivery()) ? 1 : 0));
            stm.setInt(6, ((order.isActive()) ? 1 : 0));
            stm.setInt(7, ((order.isPrepared()) ? 1 : 0));
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                stm.getGeneratedKeys().next();
                int orderId = stm.getGeneratedKeys().getInt(1);
                order.setOrderID(orderId);
                ArrayList<Product> productsAdded = new ArrayList<Product>();
                stm = conn.prepareStatement(STM_INSERT_ORDER_PRODUCT);
                for (Product p : order.getProducts()) {
                    if (!productsAdded.contains(p)) {
                        int quantity = 0;
                        for (Product p2 : order.getProducts()) {
                            if (p.equals(p2)) {
                                quantity++;
                            }
                        }
                        stm.setInt(1, orderId);
                        stm.setInt(2, p.getId());
                        stm.setInt(3, quantity);
                        numberOfRowsUpdated = stm.executeUpdate();
                        if (numberOfRowsUpdated == 1) {
                            productsAdded.add(p);
                        } else {
                            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Failed to insert order {0}.", order);
                            rollBack(conn);
                            return false;
                        }
                    }
                }
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Order {0} successfully inserted.", order);
                commit(conn);
                return true;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to insert order {0}.", order);
            rollBack(conn);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to insert order " + order + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean insertOrders(ArrayList<Order> orders) {
        int numberOfOrdersInserted = 0;
        for (Order o : orders) {
            if (insertOrder(o)) {
                numberOfOrdersInserted++;
            }
        }
        if (numberOfOrdersInserted == orders.size()) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully inserted orders {0}", orders);
            return true;
        } else {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed insert orders! Only {0} out of {1} products updated. See glassfish log for details.", new Object[]{numberOfOrdersInserted, orders.size()});
            return false;
        }
    }

    public synchronized boolean updateOrder(Order order) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_UPDATE_ORDER);
            stm.setInt(1, order.getCustomerID());
            stm.setTimestamp(2, order.getPlacedDate());
            stm.setTimestamp(3, order.getDeliveryDate());
            stm.setTimestamp(4, order.getDeliveredDate());
            stm.setInt(5, ((order.isDelivery()) ? 1 : 0));
            stm.setInt(6, ((order.isActive()) ? 1 : 0));
            stm.setInt(7, ((order.isPrepared()) ? 1 : 0));
            stm.setInt(8, order.getOrderID());
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                numberOfRowsUpdated = -1;
                stm = conn.prepareStatement(STM_DELETE_ORDER_PRODUCTS);
                stm.setInt(1, order.getOrderID());
                if (numberOfRowsUpdated > 0) {
                    ArrayList<Product> productsUpdated = new ArrayList<Product>();
                    stm = conn.prepareStatement(STM_INSERT_ORDER_PRODUCT);
                    for (Product p : order.getProducts()) {
                        numberOfRowsUpdated = -1;
                        if (!productsUpdated.contains(p)) {
                            int quantity = 0;
                            for (Product p2 : order.getProducts()) {
                                if (p.equals(p2)) {
                                    quantity++;
                                }
                            }
                            stm.setInt(1, order.getOrderID());
                            stm.setInt(2, p.getId());
                            stm.setInt(3, quantity);
                            numberOfRowsUpdated = stm.executeUpdate();
                            if (numberOfRowsUpdated == 1) {
                                productsUpdated.add(p);
                            } else {
                                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update order {0}.", order);
                                rollBack(conn);
                                return false;
                            }
                        }
                    }
                }
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Order {0} successfully updated.", order);
                commit(conn);
                return true;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update order {0}.", order);
            rollBack(conn);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to update order " + order + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean updateOrderSetDateDelivered(int orderId, Timestamp dateDelivered) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_UPDATE_ORDER_SET_DATE_DELIVERED);
            stm.setTimestamp(1, dateDelivered);
            stm.setInt(2, orderId);
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Order with order id {0} successfully updated with delivered date {1}", new Object[]{orderId, dateDelivered});
                return true;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update order with order id {0} with delivered date {1}", new Object[]{orderId, dateDelivered});
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to update order with order id " + orderId + " with delivered date " + dateDelivered + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean updateOrderSetPrepared(int orderId, boolean prepared) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_UPDATE_ORDER_SET_IS_PREPARED);
            stm.setInt(1, ((prepared) ? 1 : 0));
            stm.setInt(2, orderId);
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Order with order id {0} successfully updated with prepared status {1}", new Object[]{orderId, prepared});
                return true;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update order with order id {0} with prepared status {1}", new Object[]{orderId, prepared});
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to update order with order id " + orderId + " with prepared status " + prepared + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean updateOrders(ArrayList<Order> orders) {
        int numberOfOrdersUpdated = 0;
        for (Order o : orders) {
            if (updateOrder(o)) {
                numberOfOrdersUpdated++;
            }
        }
        if (numberOfOrdersUpdated == orders.size()) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully updated orders {0}", orders);
            return true;
        } else {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update orders! Only {0} out of {1} orders updated. See glassfish log for details.", new Object[]{numberOfOrdersUpdated, orders.size()});
            return false;
        }
    }

    public synchronized boolean updateOrderSetActive(boolean active, int orderId) {
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_UPDATE_ORDER_SET_IS_ACTIVE);
            stm.setInt(1, ((active) ? 1 : 0));
            stm.setInt(2, orderId);
            stm.executeUpdate();
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully update order with order id {0} with new status {1}.", new Object[]{orderId, active});
            commit(conn);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to set new status for order with order id " + orderId + ".", ex);
            rollBack(conn);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized Contract selectContract(int contractId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CONTRACT);
            stm.setInt(1, contractId);
            resSet = stm.executeQuery();
            if (resSet.next()) {
                Order order = selectOrder(resSet.getInt(COLUMN_ORDER_ID));
                int dayOfWeek = resSet.getInt(COLUMN_DAY_OF_THE_WEEK);
                Time time = resSet.getTime(COLUMN_TIME_OF_DELIVERY);
                boolean active = resSet.getBoolean(COLUMN_IS_ACTIVE);
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully retrieved contract with contract id {0}", contractId);
                return new Contract(contractId, order, dayOfWeek, time, active);
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Faield to retrieve contract with contract id {0}", contractId);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Faield to retrieve contract with contract id " + contractId + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized ArrayList<Contract> selectContracts() {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        boolean errorEncountered = false;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CONTRACT_IDS);
            resSet = stm.executeQuery();
            ArrayList<Contract> contracts = new ArrayList<Contract>();
            while (resSet.next()) {
                int contractId = resSet.getInt(COLUMN_ORDER_ID);
                Contract contract = selectContract(contractId);
                if (contract != null) {
                    contracts.add(contract);
                } else {
                    errorEncountered = true;
                }
            }
            if (errorEncountered) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL contracts. See glashfish log for details.");
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL contracts retrieved successfully.");
            }
            return contracts;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL contracts.", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized ArrayList<Contract> selectContracts(int customerId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        boolean errorEncountered = false;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CONTRACT_IDS_BASED_ON_CUSTOMER_ID);
            stm.setInt(1, customerId);
            resSet = stm.executeQuery();
            ArrayList<Contract> contracts = new ArrayList<Contract>();
            while (resSet.next()) {
                int contractId = resSet.getInt(COLUMN_CONTRACT_ID);
                Contract contract = selectContract(contractId);
                if (contract != null) {
                    contracts.add(contract);
                } else {
                    errorEncountered = true;
                }
            }
            if (errorEncountered) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to retrieve ALL contracts for customer with customer id {0}. See glashfish log for details.", customerId);
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL contracts retrieved successfully for customer with customer id {0}.");
            }
            return contracts;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve ALL contracts for customer with customer id " + customerId + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean insertContract(Contract contract) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            if (insertOrder(contract.getOrder())) {
                stm = conn.prepareStatement(STM_INSERT_CONTRACT);
                stm.setInt(1, contract.getOrder().getOrderID());
                stm.setInt(2, contract.getDayOfWeek());
                stm.setTime(3, contract.getTime());
                stm.setInt(4, ((contract.isActive()) ? 1 : 0));
                numberOfRowsUpdated = stm.executeUpdate();
                if (numberOfRowsUpdated == 1) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully inserted contract {0}.", contract);
                    return true;
                }
            }
            stm = conn.prepareStatement(STM_DELETE_ORDER);
            stm.setInt(1, contract.getOrder().getOrderID());
            stm.executeUpdate();
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to insert contract {0}.", contract);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to insert contract " + contract + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean updateContract(Contract contract) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            if (updateOrder(contract.getOrder())) {
                stm = conn.prepareStatement(STM_UPDATE_CONTRACT);
                stm.setInt(1, contract.getOrder().getOrderID());
                stm.setInt(2, contract.getDayOfWeek());
                stm.setTime(3, contract.getTime());
                stm.setInt(4, ((contract.isActive()) ? 1 : 0));
                stm.setInt(5, contract.getContractId());
                numberOfRowsUpdated = stm.executeUpdate();
                if (numberOfRowsUpdated == 1) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully updated contract {0}.", contract);
                    return true;
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to update contract {0}.", contract);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to update contract " + contract + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized boolean updateContractSetActive(boolean active, int contractId) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_UPDATE_CONTRACT_SET_IS_ACTIVE);
            stm.setInt(1, ((active) ? 1 : 0));
            stm.setInt(2, contractId);
            numberOfRowsUpdated = stm.executeUpdate();
            if (numberOfRowsUpdated == 1) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Successfully set new status for contract with contract id {0} to {1}.", new Object[]{contractId, active});
                return true;
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Failed to set new status for contract with contract id {0} to {1}.", new Object[]{contractId, active});
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Successfully set new status for contract with contract id" + contractId + " to " + active + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

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

    private boolean isCustomerAPrivateCustomer(Connection conn, PreparedStatement stm, ResultSet resSet, int customerId) throws SQLException {
        stm = conn.prepareStatement(STM_SELECT_PRIVATE_CUSTOMER);
        stm.setInt(1, customerId);
        resSet = stm.executeQuery();
        return resSet.next();
    }

    private boolean isProductInSingleProductTable(Connection conn, PreparedStatement stm, ResultSet resSet, int productId) throws SQLException {
        stm = conn.prepareStatement(STM_SELECT_SINGLE_PRODUCT);
        stm.setInt(1, productId);
        resSet = stm.executeQuery();
        return resSet.next();
    }
}