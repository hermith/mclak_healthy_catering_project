package shopping;

import java.util.ArrayList;
import shopping.product.Product;

/**
 * @author Linn K
 */
public class ShoppingCart {

    private ArrayList<Product> products;

    public ShoppingCart() {
        products = new ArrayList<Product>();
    }

    /**
     * Add newProduct to shoppingCart products.
     *
     * @param newProduct
     * @return if newProduct was added to products
     */
    public boolean addProduct(Product newProduct) {
        if (newProduct != null) {
            return products.add(newProduct);
        } else {
            return false;
        }
    }

    /**
     *
     * @return if the shoppingCart products is empty.
     */
    public boolean isEmpty() {
        return products.isEmpty();
    }

    /**
     * Deletes a product from the shoppingCart products.
     *
     * @param product
     * @return if it went ok
     */
    public boolean deleteProduct(Product product) {
        if (product != null) {
            return products.remove(product);
        } else {
            return false;
        }
    }

    /**
     * @return the total price of the products in the shoppingCart-object.
     */
    public float getTotalPrice() {
        float sum = 0.0f;
        for (Product product : products) {
            sum += product.getPrice();
        }
        return sum;
    }

    //GETTER & SETTER
    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}