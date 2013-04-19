package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import locale.MessageHandler;
import locale.MessageType;

@FacesValidator("ZipCodeValidator")
public class ZipCodeValidator implements Validator {

    private static final String ZIP_PATTERN = "[0-9]+";
    private Pattern pattern;
    private Matcher matcher;

    public ZipCodeValidator() {
        pattern = Pattern.compile(ZIP_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String zipCode = value.toString().trim();
        if(zipCode.equals("")){
            return;
        }
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches() || zipCode.length()!=4) {
            FacesMessage msg = MessageHandler.getMessage("zip_code_invalid", MessageType.ERROR);
            throw new ValidatorException(msg);
        }
    }
}