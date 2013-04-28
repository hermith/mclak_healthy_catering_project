package image;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * This file adds images into a gallery with diffrent titles in different
 * language
 *
 * @author Maria
 */
@Named
public class GalleriaBean {

    private List<GalleriaImages> images_no;
    private List<GalleriaImages> images_en;
    private String[] foodnames_no = {null, "Biff", "Laks", "Gresk salat", "Ferske råvarer", "Kyllingbryst"};
    private String[] foodnames_en = {null, "Steak", "Salmon", "Greek salad", "Fresh ingredients", "Chicken breast"};

    public GalleriaBean() {
        images_no = new ArrayList<GalleriaImages>();
        for (int i = 1; i <= 5; i++) {
            images_no.add(new GalleriaImages(foodnames_no[i], "food" + i + ".jpg"));
        }

        images_en = new ArrayList<GalleriaImages>();
        for (int i = 1; i <= 5; i++) {
            images_en.add(new GalleriaImages(foodnames_en[i], "food" + i + ".jpg"));

        }
    }

    /**
     * Get images in different language
     *
     * @return images
     */
    public List<GalleriaImages> getImages_no() {
        return images_no;
    }

    public List<GalleriaImages> getImages_en() {
        return images_en;
    }

    /**
     * Check for current language
     *
     * @return current language
     */
    public boolean isNo() {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        return locale.getLanguage().equals("no");
    }
}
