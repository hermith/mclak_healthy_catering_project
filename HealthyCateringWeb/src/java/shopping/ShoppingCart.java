package shopping;

import java.util.ArrayList;
import shopping.product.Product;

/**
 * @author colsen91
 */
public class ShoppingCart {
    private ArrayList<Product> products;
    
    public ShoppingCart() {
        products = new ArrayList<Product>();
    }
    
    public boolean addProduct(Product newProduct){
        if(newProduct!=null){
            return products.add(newProduct);
        }else{
            return false;
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
    
    public boolean isEmpty() {
        return products.isEmpty();
    }
    
    public boolean deleteProduct(Product product) {
        if(product!=null){
            return products.remove(product);
        }else{
            return false;
        }
    }
    
    public float getTotalPrice() {
        float sum = 0.0f;
        for(Product product : products){
            sum+=product.getPrice();
        }
        return sum;
    }
}