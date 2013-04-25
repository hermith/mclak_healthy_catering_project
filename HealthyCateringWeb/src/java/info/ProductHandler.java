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

    /**
     * Finds quantity of singleProduct in productList
     *
     * @param singleProduct
     * @param productList
     * @return int quantity
     */
    public int findQuantity(SingleProduct singleProduct, ArrayList<SingleProduct> productList) {
        int counter = 0;
        for (SingleProduct sp : productList) {
            if (sp.getId() == singleProduct.getId()) {
                counter++;
            }
        }
        return counter;
    }

    public int findQuantity(Product product, ArrayList<Product> products) {
        int counter = 0;
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Used to sort SingleProducts by quantity.
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
     * Used to sort Products by quantity.
     *
     * @param productList
     * @return an array with only one of each Product in productList.
     */
    public ArrayList<Product> getUniqueProductsList(ArrayList<Product> productList) {
        if (productList != null) {
            ArrayList<Product> products = new ArrayList<Product>();
            for (Product sp : productList) {
                if (!products.contains(sp)) {
                    products.add(sp);
                }
            }
            return products;
        }
        return null;
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

    /**
     * Adds (quantity * )singleProduct to the packageProduct
     *
     * @param singleProduct
     * @param packageProduct
     * @param quantity
     * @return
     */
    public boolean addProductToPackage(SingleProduct singleProduct, PackageProduct packageProduct, int quantity) {
        if ((singleProduct != null) && (packageProduct != null)) {
            for (int i = quantity; i > 0; i--) {
                packageProduct.addProduct(singleProduct);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Calls deleteProduct(productId) in DatabaseHandler.
     * Deletes the current product in the database.
     * @param productId
     * @return 
     */
    public boolean deleteProductDb(int productId) {
        return databaseHandler.deleteProduct(productId);
    }
}
