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

    /**
     * Calls selectProducts() in DatabaseHandler.java.
     *
     * @return All products in the database
     */
    public ArrayList<Product> getAllProducts() {
        return databaseHandler.selectProducts();
    }

    /**
     * Calls updateProduct(product) in DatabaseHandler.java. Updates current
     * product in the database.
     *
     * @param product
     * @return the product was updated (true/false)
     */
    public boolean updateProduct(Product product) {
        return databaseHandler.updateProduct(product);
    }

    /**
     * Calls insertProduct(product) in DatabaseHandler.java. Inserts current
     * product in the database.
     *
     * @param product
     * @return the product was inserted (true/false)
     */
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

    /**
     * Used to sort Products by quantity.
     *
     * @param productList
     * @return an array with only one of each SingleProduct in productList.
     */
    public ArrayList<SingleProduct> getUniqueProducts(ArrayList<SingleProduct> productList) {
        ArrayList<SingleProduct> products = new ArrayList<SingleProduct>();
        for (SingleProduct sp : productList) {
            if (!products.contains(sp)) {
                products.add(sp);
            }
        }
        return products;
    }

    /**
     * Finds all the SingleProducts in the database
     *
     * @return All SingleProducts in the database
     */
    public ArrayList<Product> getAllSingleProducts() {
        ArrayList<Product> singleProducts = new ArrayList<Product>();
        for (Product p : getAllProducts()) {
            if (p instanceof SingleProduct) {
                singleProducts.add(p);
            }
        }
        return singleProducts;
    }

    /**
     * Finds the Product in list with id=productID
     *
     * @param productID
     * @param list
     * @return Product product
     */
    public Product findProductOnIdList(int productID, ArrayList<Product> list) {
        Product product = null;
        for (Product sp : list) {
            if (sp.getId() == productID) {
                product = sp;
            }
        }
        return product;
    }

    /**
     * Deletes all singleProduct from packageProduct
     *
     * @param singleProduct
     * @param packageProduct
     */
    public void deleteProductFromPackage(SingleProduct singleProduct, PackageProduct packageProduct) {
        if ((singleProduct != null) && (packageProduct != null)) {
            for (int i = findQuantity(singleProduct, packageProduct.getProducts()); i > 0; i--) {
                packageProduct.removeProduct(singleProduct);
            }
        }
    }
}
