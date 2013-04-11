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

    private Locale spraak = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public String setEnglish() {
        spraak = new Locale("en");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(spraak);
        return null;
        
    }

    public String setNorwegian() {
        spraak = new Locale("no");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(spraak);
        return null;
    }

    public Locale getLang() {
        return spraak;
    }

    public void setLang(Locale spraak) {
        this.spraak = spraak;
    }
}