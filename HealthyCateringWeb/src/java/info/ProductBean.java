/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import locale.MessageHandler;
import shopping.product.PackageProduct;
import shopping.product.Product;
import shopping.product.SingleProduct;

/**
 *
 * @author linnk
 */
@Named
@SessionScoped
public class ProductBean implements Serializable {

    private Product newProduct;
    private int addedSingleProductID;
    private boolean newProductIsSingle;
    private int addedSingleProductQuantity;
    @Inject
    private ProductHandler productHandler;

    public ProductBean() {
        newProductIsSingle = true;
        newProduct = new SingleProduct();
        addedSingleProductQuantity = 1;
    }

    public void addNewProduct() {
        if(newProduct!=null && (!getNewProductName().trim().equals("")) && (!getNewProductDescription().trim().equals("")) && (!getNewProductProducts().isEmpty())){
            if (productHandler.insertProduct(newProduct)) {
                newProduct = newProductIsSingle == true ? new SingleProduct() : new PackageProduct();
                MessageHandler.addErrorMessage("DET GIKK BRA");
            }else{
                MessageHandler.addErrorMessage("DET GIKK DÅRLIG");
            }
        }
    }

    public void deleteSingleProductPackage(SingleProduct singleProduct) {
        PackageProduct packageProduct = (PackageProduct) newProduct;
        if (singleProduct != null) {
            for (int i = findQuantity(singleProduct); i > 0; i--) {
                packageProduct.removeProduct(singleProduct);
            }
        }
    }

    public ArrayList<Product> getAllSingleProducts() {
        ArrayList<Product> singleProducts = new ArrayList<Product>();
        for (Product p : productHandler.getAllProducts()) {
            if (p instanceof SingleProduct) {
                singleProducts.add(p);
            }
        }
        return singleProducts;
    }

    public void addProductPackage() {
        SingleProduct selectedSingleProduct = null;
        for (Product sp : getAllSingleProducts()) {
            if (sp.getId() == addedSingleProductID) {
                selectedSingleProduct = (SingleProduct) sp;
            }
        }
        if (selectedSingleProduct != null) {
            if (newProduct != null && (newProduct instanceof PackageProduct)) {
                PackageProduct packageProduct = (PackageProduct) newProduct;
                for (int i = addedSingleProductQuantity; i > 0; i--) {
                    packageProduct.addProduct(selectedSingleProduct);
                }
                addedSingleProductQuantity = 1;
            }
        }
    }

    //GETTERS AND SETTERS FOR newProduct
    public String getNewProductName() {
        if (newProduct != null) {
            return newProduct.getName();
        }
        return null;
    }

    public void setNewProductName(String name) {
        if (newProduct != null) {
            newProduct.setName(name);
        }
    }

    public String getNewProductDescription() {
        if (newProduct != null) {
            return newProduct.getDescription();
        }
        return null;
    }

    public void setNewProductDescription(String desc) {
        if (newProduct != null) {
            newProduct.setDescription(desc);
        }
    }

    public int getNewProductKCAL() {
        if (newProduct != null) {
            SingleProduct singleProduct = (SingleProduct) newProduct;
            return singleProduct.getKcal();
        }
        return 0;
    }

    public void setNewProductKCAL(int kcal) {
        if (newProduct != null) {
            SingleProduct singleProduct = (SingleProduct) newProduct;
            singleProduct.setKcal(kcal);
        }
    }

    public float getNewProductPrice() {
        if (newProduct != null) {
            SingleProduct singleProduct = (SingleProduct) newProduct;
            return singleProduct.getPrice();
        }
        return 0;
    }

    public void setNewProductPrice(float price) {
        if (newProduct != null) {
            SingleProduct singleProduct = (SingleProduct) newProduct;
            singleProduct.setPrice(price);
        }
    }

    public int getNewProductDiscount() {
        if (newProduct != null) {
            PackageProduct packageProduct = (PackageProduct) newProduct;
            return packageProduct.getDiscount();
        }
        return 0;
    }

    public void setNewProductDiscount(int discount) {
        if (newProduct != null) {
            PackageProduct packageProduct = (PackageProduct) newProduct;
            packageProduct.setDiscount(discount);
        }
    }

    public ArrayList<SingleProduct> getNewProductProducts() {
        if (newProduct != null) {
            PackageProduct packageProduct = (PackageProduct) newProduct;
            return packageProduct.getProducts();
        }
        return null;
    }

    public int findQuantity(SingleProduct singleProduct) {
        int counter = 0;
        for (SingleProduct sp : getNewProductProducts()) {
            if (sp.getId() == singleProduct.getId()) {
                counter++;
            }
        }
        return counter;
    }

    public ArrayList<SingleProduct> getNewProductProductsQuantity() {
        ArrayList<SingleProduct> products = new ArrayList<SingleProduct>();
        for (SingleProduct sp : getNewProductProducts()) {
            if (!products.contains(sp)) {
                products.add(sp);
            }
        }
        return products;
    }

    //GETTERS AND SETTERS
    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }

    public boolean isNewProductIsSingle() {
        return newProductIsSingle;
    }

    public void setNewProductIsSingle(boolean newProductIsSingle) {
        this.newProductIsSingle = newProductIsSingle;
        newProduct = newProductIsSingle == true ? new SingleProduct() : new PackageProduct();
    }

    public int getAddedSingleProductID() {
        return addedSingleProductID;
    }

    public void setAddedSingleProductID(int addedSingleProductID) {
        this.addedSingleProductID = addedSingleProductID;
    }

    public int getAddedSingleProductQuantity() {
        return addedSingleProductQuantity;
    }

    public void setAddedSingleProductQuantity(int addedSingleProductQuantity) {
        this.addedSingleProductQuantity = addedSingleProductQuantity;
    }
}
