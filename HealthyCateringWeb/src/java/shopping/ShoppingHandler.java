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
        SingleProduct sp = new SingleProduct(1, "Pizza", "Uten tomatsaus", 33, 300);
        SingleProduct sp1 = new SingleProduct(2, "Burger", "Med ost og bacon", 44, 300);
        SingleProduct sp2 = new SingleProduct(3, "Baguette", "Med egg og bacon", 65, 300);
        SingleProduct sp3 = new SingleProduct(3, "Calzone", "Nam", 35, 300);
        SingleProduct sp4 = new SingleProduct(3, "Brødskive", "Med ost og skinke", 45, 300);
        SingleProduct sp5 = new SingleProduct(3, "Salat", "Frisk, deilig salat..", 55, 300);
        SingleProduct sp6 = new SingleProduct(3, "Kyllingsalat", "Salat med kylling", 55, 300);
        SingleProduct sp7 = new SingleProduct(3, "Kiwi", "Nam", 75, 300);
        pro.add(sp);
        pro.add(sp1);
        pro.add(sp2);
        pro.add(sp3);
        pro.add(sp4);
        pro.add(sp5);
        pro.add(sp6);
        pro.add(sp7);
        //--test--
        return database.selectProducts();
    }
}