/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
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
    private Product selectedProduct;
    private boolean editProduct;
    private boolean showAddProduct;

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
     *
     * @param singleProduct
     */
    public void deleteSingleProductPackage(SingleProduct singleProduct) {
        productHandler.deleteProductFromPackage(singleProduct, (PackageProduct) newProduct);
    }

    /**
     * Calls getAllSingleProducts() in ProductHandler.java.
     *
     * @return All SingleProducts from the database
     */
    public ArrayList<Product> getAllSingleProducts() {
        return productHandler.getAllSingleProducts();
    }

    /**
     * Calls getAllProducts() in ProductHandler.java.
     *
     * @return All products from the database
     */
    public ArrayList<Product> getAllProductsDB() {
        return productHandler.getAllProducts();
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
     * Finds quantity of the current singleProduct in the SingleProduct list of
     * the selectedProduct-object
     *
     * @param product
     * @return quantity
     */
    public int findQuantitySelected(SingleProduct product) {
        return productHandler.findQuantity(product, getPackageProducts());
    }

    /**
     *
     * @return newProduct.getProducts() with only one of each product
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

    public void closeEditProduct() {
        selectedProduct = null;
        editProduct = false;
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context
                .getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    public void closeAddProduct() {
        showAddProduct = false;
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context
                .getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    //GETTERs AND SETTERs
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        if (selectedProduct != null) {
            this.selectedProduct = selectedProduct;
            editProduct = true;
        }
    }

    public boolean isEditProduct() {
        return editProduct;
    }

    public void setEditProduct(boolean editProduct) {
        this.editProduct = editProduct;
    }

    public boolean isShowAddProduct() {
        return showAddProduct;
    }

    public void setShowAddProduct(boolean showAddProduct) {
        this.showAddProduct = showAddProduct;
    }

    // SETTERs AND GETTERs FOR selectedProduct
    public String getProductName() {
        if (selectedProduct != null) {
            return selectedProduct.getName();
        }
        return null;
    }

    public void setProductName(String name) {
        if (selectedProduct != null && (!name.equals(""))) {
            selectedProduct.setName(name);
        }
    }

    public String getProductDescription() {
        if (selectedProduct != null) {
            return selectedProduct.getDescription();
        }
        return null;
    }

    public void setProductDescription(String desc) {
        if (selectedProduct != null && (!desc.equals(""))) {
            selectedProduct.setDescription(desc);
        }
    }

    public int getProductKCAL() {
        if (selectedProduct != null && (selectedProduct instanceof SingleProduct)) {
            SingleProduct singleProduct = (SingleProduct) selectedProduct;
            return singleProduct.getKcal();
        }
        return 0;
    }

    public void setProductKCAL(int KCAL) {
        if (selectedProduct != null && (selectedProduct instanceof SingleProduct)) {
            SingleProduct singleProduct = (SingleProduct) selectedProduct;
            singleProduct.setKcal(KCAL);
        }
    }

    public float getProductPrice() {
        if (selectedProduct != null) {
            return selectedProduct.getPrice();
        }
        return 0;
    }

    public void setProductPrice(float price) {
        if (selectedProduct != null && (selectedProduct instanceof SingleProduct)) {
            SingleProduct singleProduct = (SingleProduct) selectedProduct;
            singleProduct.setPrice(price);
        }
    }

    public int getProductID() {
        if (selectedProduct != null) {
            return selectedProduct.getId();
        }
        return 0;
    }

    public int getProductDiscount() {
        if (selectedProduct != null && (selectedProduct instanceof PackageProduct)) {
            PackageProduct packageProduct = (PackageProduct) selectedProduct;
            return packageProduct.getDiscount();
        }
        return 0;
    }

    public void setProductDiscount(int discount) {
        if (selectedProduct != null && (selectedProduct instanceof PackageProduct)) {
            PackageProduct packageProduct = (PackageProduct) selectedProduct;
            packageProduct.setDiscount(discount);
        }
    }
    
    public ArrayList<SingleProduct> getPackageProducts() {
        if (selectedProduct != null && (selectedProduct instanceof PackageProduct)) {
            PackageProduct packageProduct = (PackageProduct) selectedProduct;
            return packageProduct.getProducts();
        }
        return null;
    }
    
    
    
    public boolean selectedIsSingleProduct() {
        return selectedProduct instanceof SingleProduct;
    }
    
    /**
     * Calls getUniqueProducts(getPackageProducts()).
     * @return an array with onlye one of each SingleProduct
     */
    public ArrayList<SingleProduct> getSingleProductsPackage() {
        return productHandler.getUniqueProducts(getPackageProducts());
    }
    
    public void deleteProductPackage(SingleProduct product) {
        productHandler.deleteProductFromPackage(product, (PackageProduct)selectedProduct);
    }
    
    public void addProductPackageEdit() {
        SingleProduct selectedSingleProduct = null;
        for (Product sp : getAllSingleProducts()) {
            if (sp.getId() == addedSingleProductID) {
                selectedSingleProduct = (SingleProduct) sp;
                break;
            }
        }
        if (selectedSingleProduct != null) {
            if (selectedProduct != null && (selectedProduct instanceof PackageProduct)) {
                PackageProduct packageProduct = (PackageProduct) selectedProduct;
                for (int i = addedSingleProductQuantity; i > 0; i--) {
                    packageProduct.addProduct(selectedSingleProduct);
                }
                addedSingleProductQuantity = 1;
            }
        }
    }
    
    /**
     * Save changes to a product
     */
    public void saveChangesProduct() {
        if (selectedProduct != null) {
            if(selectedProduct instanceof PackageProduct){
                PackageProduct pr = (PackageProduct)selectedProduct;
            }
            if (productHandler.updateProduct(selectedProduct)) {
                String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "product_changes_saved");
                MessageHandler.addErrorMessage(msg);
            } else {
                String msg = MessageHandler.getLocalizedText(MessageType.TEKST, "product_changes_not_saved");
                MessageHandler.addErrorMessage(msg);
            }
        }
    }
}
