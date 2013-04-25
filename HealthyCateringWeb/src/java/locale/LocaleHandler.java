package locale;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * LocaleHandler.java is used to set the current language for the user. This
 * has the ability to switch between Norwegian and English. these methods are
 * called directly from xhtml buttoms.
 * 
 * @author Karl
 */
@Named
@SessionScoped
public class LocaleHandler implements Serializable {

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    /**
     * Changes to English language.
     * 
     * @return 
     */
    public String setEnglish() {
        locale = new Locale("en");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return null;
        
    }

    /**
     * Changes to Norwegian language
     * 
     * @return 
     */
    public String setNorwegian() {
        locale = new Locale("no");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return null;
    }

    /**
     * @return Returns the current locale as a Locale object
     */
    public Locale getLang() {
        return locale;
    }

    /**
     * Sets the current locale from a Locale object
     * 
     * @param spraak Language to change to
     */
    public void setLang(Locale spraak) {
        this.locale = spraak;
    }
}