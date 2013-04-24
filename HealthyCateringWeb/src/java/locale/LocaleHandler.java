package locale;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * @author Karl
 */
@Named
@SessionScoped
public class LocaleHandler implements Serializable {

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public String setEnglish() {
        locale = new Locale("en");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return null;
        
    }

    public String setNorwegian() {
        locale = new Locale("no");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return null;
    }

    public Locale getLang() {
        return locale;
    }

    public void setLang(Locale spraak) {
        this.locale = spraak;
    }
}