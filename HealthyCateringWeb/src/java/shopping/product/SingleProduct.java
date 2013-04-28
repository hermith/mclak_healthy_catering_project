package shopping.product;

import java.text.DecimalFormat;

/**
 * This class is used for storing information about a single product. This class
 * extends the shopping.product.Product class.
 *
 * @author Christer Olsen
 */
public class SingleProduct extends Product {

    private float price;
    private int kcal;

    /**
     * Initiates an empty single product.
     */
    public SingleProduct() {
        super();
        this.price = 0.0f;
        this.kcal = 0;
    }

    /**
     * Initiates a single product with given parameters.
     *
     * @param id the product id.
     * @param nameNo The name in Norwegian.
     * @param nameEn The name in English
     * @param descriptionNo The description in Norwegian.
     * @param descriptionEn The description in English.
     * @param price The product price.
     * @param kcal The amounts of kcal.
     */
    public SingleProduct(int id, String nameNo, String nameEn, String descriptionNo, String descriptionEn, float price, int kcal) {
        super(id, nameNo, nameEn, descriptionNo, descriptionEn);
        this.price = price;
        this.kcal = kcal;
    }

    @Override
    public String toString() {
        return "SingleProduct{" + super.toString() + ", price=" + price + ", kcal=" + kcal + '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    //Getters and setters
    @Override
    public float getPrice() {
        return Float.parseFloat(new DecimalFormat("#.##").format(price).replace(",", "."));
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }
}