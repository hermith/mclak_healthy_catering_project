package database;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.criteria.Order;
import javax.sql.DataSource;

/**
 * @author Christer Olsen
 */
@Named
@ApplicationScoped
public class DatabaseHandler {

    private static final String POOL_NAME = "jdbc/sysproj_res";
    private static final String STM_ADD_USER = "INSERT INTO Bruker(brukernavn, passord) VALUES(?, ?)";
    private static final String STM_ADD_USER_ROLE = "INSERT INTO Rolle(brukernavn, rolle) VALUES(?, ?)";
    private static final String STM_GET_USER = "SELECT username, password FROM User_Table WHERE username = ?";
    private static final String STM_CHANGE_PASSWORD = "UPDATE User_Table SET password = ? WHERE username = ?";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "user_role";
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
        // TODO Registrer bruker og rolle i User_Table og User_Role_Table.
        return false;
    }

    /**
     * TODO: Burde kalles etter registerNewUser-metoden for å registrere
     * kundeinformasjon
     *
     * @param customer
     * @return
     */
    public synchronized boolean registerNewCustomer(Customer customer) {
        // TODO Registrer en ny kunde i Customer_Table. OBS! Husk å skille mellom PrivateCustomer og CorporateCustomer.
        return false;
    }

    /**
     * TODO: Burde kalles uansett når en person logger inn for å finne ut
     * hvilken rolle denne brukeren har.
     *
     * @param username
     * @return
     */
    public synchronized User getUser(String username) {
        // TODO Hent ut informasjon om en bruker. Altså brukernavn og rolle.
        return null;
    }

    /**
     * TODO: Burde kalles når en person, som også er en kunde, logger inn. Evt.
     * ved andre anledninger.
     *
     * @param username
     * @return
     */
    public synchronized Customer getCustomer(String username) {
        // TODO Hent ut informasjon om en kunde. OBS! Husk å skille mellom PrivateCustomer og CorporateCustomer.
        return null;
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
    public static void closeResultSet(ResultSet resSet) {
    }

    private static void closeStatement(PreparedStatement stm) {
    }

    private void rollBack(Connection conn) {
    }

    private void setAutoCommit(Connection conn, boolean newState) {
    }
}