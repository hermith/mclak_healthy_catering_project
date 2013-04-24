/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import database.DatabaseHandler;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import shopping.product.PackageProduct;
import shopping.product.Product;
import shopping.product.SingleProduct;

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
    
    public boolean insertProduct(Product product) {
        return databaseHandler.insertProduct(product);
    }
    
    
    public int findQuantity(SingleProduct singleProduct, ArrayList<SingleProduct> productList) {
        int counter = 0;
        for (SingleProduct sp : productList) {
            if (sp.getId() == singleProduct.getId()) {
                counter++;
            }
        }
        return counter;
    }
    
    public ArrayList<SingleProduct> getUniqueProducts(ArrayList<SingleProduct> productList) {
        ArrayList<SingleProduct> products = new ArrayList<SingleProduct>();
        for (SingleProduct sp : productList) {
            if (!products.contains(sp)) {
                products.add(sp);
            }
        }
        return products;
    }
    
    public ArrayList<Product> getAllSingleProducts() {
        ArrayList<Product> singleProducts = new ArrayList<Product>();
        for (Product p : getAllProducts()) {
            if (p instanceof SingleProduct) {
                singleProducts.add(p);
            }
        }
        return singleProducts;
    }
    
    public Product findProductOnIdList(int productID, ArrayList<Product> list) {
        Product product = null;
        for (Product sp : list) {
            if (sp.getId() == productID) {
                product = sp;
            }
        }return product;
    }
    
    public void deleteProductFromPackage(SingleProduct singleProduct, PackageProduct packageProduct) {
        if ((singleProduct != null) && (packageProduct!= null)) {
            for (int i = findQuantity(singleProduct,packageProduct.getProducts()); i > 0; i--) {
                packageProduct.removeProduct(singleProduct);
            }
        }
    }
}
