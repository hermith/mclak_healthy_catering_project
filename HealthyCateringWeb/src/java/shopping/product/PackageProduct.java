package shopping.product;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class is used for storing information about a package product. This
 * class extends the class shopping.product.Product.
 *
 * @author Christer Olsen
 */
public class PackageProduct extends Product {

    private int discount;
    private ArrayList<SingleProduct> products;

    /**
     * Initiates an empty package product.
     */
    public PackageProduct() {
        super();
        this.discount = 0;
        this.products = new ArrayList<SingleProduct>();
    }

    /**
     * Initiates a package product with given parameters.
     *
     * @param id The product id.
     * @param nameNo The name in Norwegian.
     * @param nameEn The name in English
     * @param descriptionNo The description in Norwegian.
     * @param descriptionEn The description in English.
     * @param discount The discount in NOK.
     * @param products The single products that this package product consists
     * of.
     */
    public PackageProduct(int id, String nameNo, String nameEn, String descriptionNo, String descriptionEn, int discount, ArrayList<SingleProduct> products) {
        super(id, nameNo, nameEn, descriptionNo, descriptionEn);
        this.discount = discount;
        this.products = products;
    }

    /**
     * Add product to the list products.
     *
     * @param product The product to add.
     */
    public void addProduct(SingleProduct product) {
        this.products.add(product);
    }

    /**
     * Remove product from the list products.
     *
     * @param product The product to remove.
     */
    public void removeProduct(SingleProduct product) {
        this.products.remove(product);
    }

    @Override
    public String toString() {
        return "PackageProduct{" + super.toString() + "discount=" + discount + ", products=" + products + '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Finds the price of the PackageProduct.
     *
     * @return The total price.
     */
    @Override
    public float getPrice() {
        float sum = 0.0f;
        for (Product product : products) {
            sum += product.getPrice();
        }
        return Float.parseFloat(new DecimalFormat("#.##").format(sum - discount).replace(",", "."));

    }

    // Getters and setters
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public ArrayList<SingleProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<SingleProduct> products) {
        this.products = products;
    }
}