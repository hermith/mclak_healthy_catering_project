package shopping.product;

import java.util.ArrayList;

/**
 * @author colsen91
 */
public class PackageProduct extends Product {

    private int discount;
    private ArrayList<SingleProduct> products;

    public PackageProduct() {
        super();
        this.discount = -1;
        this.products = null;
    }

    public PackageProduct(int id, String name, String description, int discount, ArrayList<SingleProduct> products) {
        super(id, name, description);
        this.discount = discount;
        this.products = products;
    }

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

    public void addProduct(SingleProduct product) {
        this.products.add(product);
    }

    public void removeProduct(SingleProduct product) {
        this.products.remove(product);
    }

    @Override
    public String toString() {
        return "PackageProduct{" + "discount=" + discount + ", products=" + products + ", " + super.toString() + '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}