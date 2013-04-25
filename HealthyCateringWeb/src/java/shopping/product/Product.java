package shopping.product;

import java.util.Locale;
import javax.inject.Inject;
import locale.LocaleHandler;

/**
 * @author aleksalr
 */
public abstract class Product {

    private int id;
    private String nameNo;
    private String nameEn;
    private String descriptionNo;
    private String descriptionEn;
    @Inject
    private LocaleHandler localeHandler;

    public Product() {
        this.id = -1;
        this.nameNo = null;
        this.nameEn = null;
        this.descriptionNo = null;
        this.descriptionEn = null;
    }

    public Product(int id, String nameNo, String nameEn, String descriptionNo, String descriptionEn) {
        this.id = id;
        this.nameNo = nameNo;
        this.nameEn = nameEn;
        this.descriptionNo = descriptionNo;
        this.descriptionEn = descriptionEn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (localeHandler.getLang().equals(new Locale("no"))) {
            return nameNo;
        } else {
            return nameEn;
        }
    }

    public String getNameNo() {
        return nameNo;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameNo(String nameNo) {
        this.nameNo = nameNo;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescription() {
        if (localeHandler.getLang().equals(new Locale("no"))) {
            return descriptionNo;
        } else {
            return descriptionEn;
        }
    }

    public String getDescriptionNo() {
        return descriptionNo;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionNo(String descriptionNo) {
        this.descriptionNo = descriptionNo;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", nameNo=" + nameNo + ", nameEn=" + nameEn + ", descriptionNo=" + descriptionNo + ", descriptionEn=" + descriptionEn + ", localeHandler=" + localeHandler + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    /**
     * @return the price
     */
    public abstract float getPrice();
}