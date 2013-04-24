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
import locale.MessageType;
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
        String msgSuccess = MessageHandler.getLocalizedText(MessageType.TEKST, "product_add_success");
        String msgError = MessageHandler.getLocalizedText(MessageType.ERROR, "product_add_error");
        if (newProduct != null) {
            if (newProduct instanceof PackageProduct) {
                if ((!getNewProductName().trim().equals("")) && (!getNewProductDescription().trim().equals("")) && (!getNewProductProducts().isEmpty())) {
                    if (productHandler.insertProduct(newProduct)) {
                        newProduct = newProductIsSingle == true ? new SingleProduct() : new PackageProduct();
                        MessageHandler.addErrorMessage(msgSuccess);
                    } else {
                        MessageHandler.addErrorMessage(msgError);
                    }
                } else {
                    String msg = MessageHandler.getLocalizedText(MessageType.ERROR, "product_please_fill_form");
                    MessageHandler.addErrorMessage(msg);
                }
            } else {
                if (productHandler.insertProduct(newProduct)) {
                    newProduct = newProductIsSingle == true ? new SingleProduct() : new PackageProduct();
                    MessageHandler.addErrorMessage(msgSuccess);
                } else {
                    MessageHandler.addErrorMessage(msgError);
                }
            }
        }
    }

    /**
     * Deletes the chosen singleProduct from (PackageProduct) newProduct
     * @param singleProduct 
     */
    public void deleteSingleProductPackage(SingleProduct singleProduct) {
        productHandler.deleteProductFromPackage(singleProduct, (PackageProduct)newProduct);
    }
    
    /**
     * 
     * @return All SingleProducts from the database
     */
    public ArrayList<Product> getAllSingleProducts() {
        return productHandler.getAllSingleProducts();
    }

    /**
     * Add a SingleProduct to (PackageProduct) newProduct.
     */
    public void addProductPackage() {
        SingleProduct selectedSingleProduct = (SingleProduct) productHandler.findProductOnIdList(addedSingleProductID, getAllSingleProducts());
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

    /**
     * Finds quantity of the current singleProduct in the SingleProduct list of
     * the newProduct-object
     *
     * @param singleProduct
     * @return quantity
     */
    public int findQuantity(SingleProduct singleProduct) {
        return productHandler.findQuantity(singleProduct, getNewProductProducts());
    }

    /**
     *
     * @return newProduct.getProducts() with only one of each
     * product
     */
    public ArrayList<SingleProduct> getNewProductProductsQuantity() {
        return productHandler.getUniqueProducts(getNewProductProducts());
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
        if (newProduct != null && (newProduct instanceof PackageProduct)) {
            PackageProduct packageProduct = (PackageProduct) newProduct;
            return packageProduct.getProducts();
        }
        return null;
    }
}
