package shopping;

import database.DatabaseHandler;
import javax.inject.Inject;

/**
 * @author colsen91
 */
public class ShoppingHandler {

    @Inject
    private DatabaseHandler database;

    public ShoppingHandler() {
    }

    public boolean insertOrder() { //tar inn ordre....
        //return database.insertOrder(order);
        return false;
    }
}