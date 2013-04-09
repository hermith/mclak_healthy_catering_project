package database;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.INFO, "The database handler is working...");
    }
    
    public synchronized boolean registerNewUser(String username, String password, String role) {
        // TODO
        return false;
    }
}