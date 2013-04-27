package shopping.product;

import java.text.DecimalFormat;

/**
 * @author colsen91
 */
public class SingleProduct extends Product {

    private float price;
    private int kcal;

    public SingleProduct() {
        super();
        this.price = 0.0f;
        this.kcal = 0;
    }

    public SingleProduct(int id, String nameNo, String nameEn, String descriptionNo, String descriptionEn, float price, int kcal) {
        super(id, nameNo, nameEn, descriptionNo, descriptionEn);
        this.price = price;
        this.kcal = kcal;
    }

    /**
     * toString-method.
     * @return a readable text representation of this object
     */
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