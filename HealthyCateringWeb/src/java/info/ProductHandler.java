/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.product.Product;

/**
 *
 * @author linnk
 */
@ApplicationScoped
public class ProductHandler {
    @Inject 
    private DatabaseHandler databaseHandler;
    
    public ArrayList<Product> getAllProducts() {
        return databaseHandler.selectProducts();
    }
    
    public boolean updateProduct(Product product) {
        return databaseHandler.updateProduct(product);
    }
}
