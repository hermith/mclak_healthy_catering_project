package shopping.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ProductAttributesHandler {

    private static final String NAME_SUFFIX = "_name";
    private static final String DESC_SUFFIX = "_desc";
    private File fileNo;
    private FileInputStream fisNo;
    private FileOutputStream fosNo;
    private Properties propNo;
    private File fileEn;
    private FileInputStream fisEn;
    private FileOutputStream fosEn;
    private Properties propEn;

    public ProductAttributesHandler() {
        try {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            this.fileNo = new File(ccl.getResource("locale/Product_no.properties").getFile());
            this.fisNo = new FileInputStream(fileNo);
            this.fosNo = new FileOutputStream(fileNo);
            this.propNo = new Properties();
            this.propNo.load(fisNo);

            this.fileEn = new File(ccl.getResource("locale/Product_en.properties").getFile());
            this.fisEn = new FileInputStream(fileEn);
            this.fosEn = new FileOutputStream(fileEn);
            this.propEn = new Properties();
            this.propEn.load(fisEn);
            
            Logger.getLogger(ProductAttributesHandler.class.getName()).log(Level.INFO, "File stream(s) initiated.");
        } catch (FileNotFoundException ex1) {
            Logger.getLogger(ProductAttributesHandler.class.getName()).log(Level.SEVERE, "Unable to initiate file stream(s).", ex1);
        } catch (IOException ex2) {
            Logger.getLogger(ProductAttributesHandler.class.getName()).log(Level.SEVERE, "Unable to initiate file stream(s).", ex2);
        }
    }

    public void appendToProperties(String productNameNo, String productNameEn, String productDescriptionNo, String productDescriptionEn) {
        String productNameKey = getGeneratedPropertyNameKey(productNameEn);
        String productDescKey = getGeneratedPropertyDescriptionKey(productDescriptionEn);
        append(productNameKey, productNameNo, productNameEn);
        append(productDescKey, productDescriptionNo, productDescriptionEn);
    }

    public String getGeneratedPropertyNameKey(String property) {
        return property.replaceAll("\\s", "_").toLowerCase() + NAME_SUFFIX;
    }
    
    public String getGeneratedPropertyDescriptionKey(String property) {
        return property.replaceAll("\\s", "_").toLowerCase() + DESC_SUFFIX;
    }

    private void append(String key, String valueNo, String valueEn) {
        propNo.setProperty(key, valueNo);
        propEn.setProperty(key, valueEn);
        try {
            propNo.store(fosNo, null);
            propEn.store(fosEn, null);
        } catch (IOException ex) {
            Logger.getLogger(ProductAttributesHandler.class.getName()).log(Level.SEVERE, "Failed to append propterty with key " + key + ".", ex);
        }
    }

    public String test() {
        appendToProperties("Knekkebrød", "Crips Bread", "Tørt og kjedelig knekkebrød", "Dry and boring crips bread");
        return propNo.getProperty("crips_bread_name");
    }
}