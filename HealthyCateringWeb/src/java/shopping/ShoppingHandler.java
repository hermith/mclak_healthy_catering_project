package shopping;

import database.DatabaseHandler;
import info.Order;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.product.Product;
import shopping.product.SingleProduct;

/**
 * @author colsen91
 */
@ApplicationScoped
public class ShoppingHandler implements Serializable{

    @Inject
    private DatabaseHandler database;

    public ShoppingHandler() {
    }

    public boolean insertOrder(Order order) {
        //return database.insertOrder(order);
        return false;
    }

    public ArrayList<Product> getMenu() {
        //--test--
        ArrayList<Product> pro = new ArrayList<Product>();
        SingleProduct sp = new SingleProduct(1, "pizza", "nam", 33, 300);
        SingleProduct sp1 = new SingleProduct(2, "burger", "nam", 44, 300);
        SingleProduct sp2 = new SingleProduct(3, "baguette", "nam", 55, 300);
        pro.add(sp);
        pro.add(sp1);
        pro.add(sp2);
        //--test--
        return pro;
    }
}