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
        this.discount = 0;
        this.products = new ArrayList<SingleProduct>();
    }

    public PackageProduct(int id, String nameNo, String nameEn, String descriptionNo, String descriptionEn, int discount, ArrayList<SingleProduct> products) {
        super(id, nameNo, nameEn, descriptionNo, descriptionEn);
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

    @Override
    public float getPrice() {
        float sum = 0.0f;
        for (Product product : products) {
            sum += product.getPrice();
        }
        return sum - discount;
    }
}