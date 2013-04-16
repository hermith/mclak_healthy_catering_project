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
import shopping.product.PackageProduct;
import shopping.product.Product;
import shopping.product.SingleProduct;
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
    private static final String STM_SELECT_USER_ROLE = "SELECT user_role AS user_role FROM User_Role_Table WHERE username = ?";
    private static final String STM_SELECT_PASSWORD = "SELECT password AS password FROM User_Table WHERE username = ?";
    private static final String STM_UPDATE_PASSWORD = "UPDATE User_Table SET password = ? WHERE username = ?";
    private static final String STM_SELECT_CUSTOMER_1 = "SELECT customer_id AS customer_id, email AS email, phone_number AS phone_number, address AS adress, zip_code AS zip_code, city AS city FROM Customer_Table WHERE username = ?";
    private static final String STM_SELECT_CUSTOMER_2 = "SELECT email AS email, phone_number AS phone_number, address AS adress, zip_code AS zip_code, city AS city FROM Customer_Table WHERE customer_id = ?";
    private static final String STM_SELECT_PRIVATE_CUSTOMER = "SELECT first_name AS first_name, last_name AS last_name FROM Private_Customer_Table WHERE customer_id = ?";
    private static final String STM_SELECT_CORPORATE_CUSTOMER = "SELECT company_name FROM Corporate_Customer_Table WHERE customer_id = ?";
    private static final String STM_INSERT_CUSTOMER = "INSERT INTO Customer_Table(username, email, phone_number, address, zip_code, city) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String STM_INSERT_PRIVATE_CUSTOMER = "INSERT INTO Private_Customer_Table(customer_id, first_name, last_name) VALUES(?, ?, ?)";
    private static final String STM_INSERT_CORPORATE_CUSTOMER = "INSERT INTO Corporate_Customer_Table(customer_id, company_name) VALUES(?, ?)";
    private static final String STM_UPDATE_CUSTOMER = "UPDATE Customer_Table SET email = ?, phone_number = ?, address = ?, zip_code = ?, city = ? WHERE customer_id = ?";
    private static final String STM_UPDATE_PRIVATE_CUSTOMER = "UPDATE Private_Customer_Table SET first_name = ?, last_name = ? WHERE customer_id = ?";
    private static final String STM_UPDATE_CORPORATE_CUSTOMER = "UPDATE Corporate_Customer_Table SET company_name = ? WHERE customer_id = ?";
    private static final String STM_SELECT_PRODUCT = "SELECT product_name AS product_name, product_description AS product_description FROM Product_Table WHERE product_id = ?";
    private static final String STM_SELECT_PRODUCT_IDS = "SELECT product_id AS product_id FROM Product_Table";
    private static final String STM_SELECT_SINGLE_PRODUCT = "SELECT product_price AS product_price, product_kcal AS product_kcal FROM Single_Product_Table WHERE product_id = ?";
    private static final String STM_SELECT_PACKAGE_PRODUCT = "SELECT product_discount AS product_discount FROM Package_Product_Table WHERE product_id = ?";
    private static final String STM_SELECT_PACKAGE_SINGLE_PRODUCT_CONNECTION = "SELECT single_product_id AS single_product_id, quantity AS quantity FROM Package_Single_Product_Table WHERE package_product_id = ?";
    private static final String STM_INSERT_PRODUCT = "INSERT INTO Product_Table(product_name, product_description) VALUES(?, ?)";
    private static final String STM_INSERT_SINGLE_PRODUCT = "INSERT INTO Single_Product_Table(product_id, product_price, product_kcal) VALUES(?, ?, ?)";
    private static final String STM_INSERT_PACKAGE_PRODUCT = "INSERT INTO Package_Product_Table(product_id, product_discount) VALUES(?, ?)";
    private static final String STM_INSERT_PACKAGE_SINGLE_PRODUCT_CONNECTION = "INSERT INTO Package_Single_Product_Table(package_product_id, single_product_id, quantity) VALUES(?, ?, ?)";
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
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_KCAL = "product_kcal";
    private static final String COLUMN_PRODUCT_DISCOUNT = "product_discount";
    private static final String COLUMN_SINGLE_PRODUCT_ID = "single_product_id";
    private static final String COLUMN_SINGLE_PRODUCT_QUANTITY = "quantity";
    private DataSource dataSource;

    /**
     * Constructor.
     */
    public DatabaseHandler() {
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Attempting to creating a new database handler...");
        try {
            dataSource = (DataSource) new InitialContext().lookup(POOL_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to create a new database handler!", ex);
        }
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "New database handler successfully created!");
    }

    public synchronized User selectUser(String username) {
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
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "User {0} retrieved.", user);
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
            closeStatement(stm);
            closeConnection(conn);
        }
    }

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
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Password for user {0} NOT retrieved.", username);
                return true;
            } else {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Password for user {0} NOT retrieved.", username);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve password for user " + username + ".", ex);
            return false;
        } finally {
            closeStatement(stm);
            closeConnection(conn);
        }
    }

    public synchronized Customer selectCustomer(String username) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CUSTOMER_1);
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

    public synchronized Customer selectCustomer(int customerId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_CUSTOMER_2);
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
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Customer {0} retrieved.", corporateCustomer);
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

    public synchronized boolean insertCustomer(Customer customer, String username) {
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

    public synchronized boolean updateCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
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
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Customer {0} NOT updated.", customer);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to update customer " + customer + ".", ex);
            return false;
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }

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
                String name = resSet1.getString(COLUMN_PRODUCT_NAME);
                String description = resSet1.getString(COLUMN_PRODUCT_DESCRIPTION);
                if (isProductASingleProduct(conn, stm1, resSet1, productId)) {
                    stm1 = conn.prepareStatement(STM_SELECT_SINGLE_PRODUCT);
                    stm1.setInt(1, productId);
                    resSet1 = stm1.executeQuery();
                    if (resSet1.next()) {
                        float price = resSet1.getFloat(COLUMN_PRODUCT_PRICE);
                        int kcal = resSet1.getInt(COLUMN_PRODUCT_KCAL);
                        SingleProduct singleProduct = new SingleProduct(productId, name, description, price, kcal);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Single product {0} retrieved.", singleProduct);
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
                        stm1 = conn.prepareStatement(STM_SELECT_PRODUCT);
                        stm2 = conn.prepareStatement(STM_SELECT_SINGLE_PRODUCT);;
                        resSet2 = null;
                        while (resSet1.next()) {
                            int singleProductId = resSet1.getInt(COLUMN_SINGLE_PRODUCT_ID);
                            int quantity = resSet1.getInt(COLUMN_SINGLE_PRODUCT_QUANTITY);
                            stm1.setInt(1, singleProductId);
                            resSet2 = stm1.executeQuery();
                            if (resSet2.next()) {
                                String singleProductName = resSet2.getString(COLUMN_PRODUCT_NAME);
                                String singleProductDescription = resSet2.getString(COLUMN_PRODUCT_DESCRIPTION);
                                stm2.setInt(1, singleProductId);
                                resSet2 = stm1.executeQuery();
                                if (resSet2.next()) {
                                    float singleProductPrice = resSet2.getFloat(COLUMN_PRODUCT_PRICE);
                                    int singleProductKcal = resSet2.getInt(COLUMN_PRODUCT_KCAL);
                                    for (int i = 0; i < quantity; i++) {
                                        products.add(new SingleProduct(singleProductId, singleProductName, singleProductDescription, singleProductPrice, singleProductKcal));
                                    }
                                }
                            }
                        }
                        PackageProduct packageProduct = new PackageProduct(productId, name, description, discount, products);
                        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "Package product {0} retrieved.", packageProduct);
                    }
                }
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.WARNING, "Product with product id {0] NOT retrieved.", productId);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to retrieve product with product id " + productId + ".", ex);
            return null;
        } finally {
            closeResultSet(resSet1);
            closeResultSet(resSet2);
            closeStatement(stm1);
            closeStatement(stm2);
            closeConnection(conn);
        }
    }

    public synchronized ArrayList<Product> selectProducts() {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet resSet = null;
        try {
            conn = dataSource.getConnection();
            stm = conn.prepareStatement(STM_SELECT_PRODUCT_IDS);
            resSet = stm.executeQuery();
            ArrayList<Product> products = new ArrayList<Product>();
            while (resSet.next()) {
                int productId = resSet.getInt(COLUMN_PRODUCT_ID);
                products.add(selectProduct(productId));
            }
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "ALL products retrieved successfully.");
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

    /*
    public synchronized boolean insertProduct(Product product) {
        Connection conn = null;
        PreparedStatement stm = null;
        int numberOfRowsUpdated = -1;
        int productId = -1;
        try {
            conn = dataSource.getConnection();
            setAutoCommit(conn, false);
            stm = conn.prepareStatement(STM_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, product.getName());
            stm.setString(2, product.getDescription());
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
                } else if (product instanceof PackageProduct) {
                    PackageProduct packageProduct = (PackageProduct) product;
                    stm = conn.prepareStatement(STM_INSERT_PACKAGE_PRODUCT);
                    stm.setInt(1, productId);
                    stm.setInt(2, packageProduct.getDiscount());
                    numberOfRowsUpdated = stm.executeUpdate();
                    if (numberOfRowsUpdated == 1) {
                        numberOfRowsUpdated = -1;
                        // TODO sjekk om single product allerede er reg. hvis ikke; reg. Deretter sett opp kobling.
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Failed to insert product " + product + ".", ex);
        } finally {
            setAutoCommit(conn, true);
            closeStatement(stm);
            closeConnection(conn);
        }
    }
    */

    public synchronized boolean insertProducts(ArrayList<Product> products) {
        return true;
    }

    public synchronized boolean updateProduct(Product product) {
        return true;
    }

    public synchronized boolean updateProducts(ArrayList<Product> products) {
        return true;
    }

    public synchronized Order selectOrder(int orderId) {
        return null;
    }

    public synchronized ArrayList<Order> selectOrders() {
        return null;
    }

    public synchronized ArrayList<Order> selectOrders(String customerId) {
        return null;
    }

    public synchronized boolean insertOrder(Order order) {
        return true;
    }

    public synchronized boolean insertOrders(ArrayList<Order> orders) {
        return true;
    }

    public synchronized boolean updateOrder(Order order) {
        return true;
    }

    public synchronized boolean updateOrdes(ArrayList<Order> orders) {
        return true;
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

    private boolean isProductASingleProduct(Connection conn, PreparedStatement stm, ResultSet resSet, int productId) throws SQLException {
        stm = conn.prepareStatement(STM_SELECT_SINGLE_PRODUCT);
        stm.setInt(1, productId);
        resSet = stm.executeQuery();
        return resSet.next();
    }
}