package shopping;

import java.util.ArrayList;
import shopping.product.Product;

/**
 * @author colsen91
 */
public class ShoppingCart {
    private ArrayList<Product> products;
    
    public ShoppingCart() {
        
    }
    
    public boolean addProduct(Product newProduct){
        if(newProduct!=null){
            return products.add(newProduct);
        }else{
            return false;
        }
    }
}