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

/**
 * A validator that checks that phonenumbers are input correct. It checks that
 * it contains numbers, between 8 and 13 numbers.
 *
 * @author linnk
 */
@FacesValidator("PhoneNumberValidator")
public class PhoneNumberValidator implements Validator {

    //Regex pattern
    private static final String PHONE_NUMBER_PATTERN = "[0-9]+";
    private Pattern pattern;
    private Matcher matcher;

    public PhoneNumberValidator() {
        pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String phoneNumber = value.toString().trim();
        if (phoneNumber.equals("")) {
            return;
        }
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches() || !(phoneNumber.length() >= 8 && phoneNumber.length() < 13)) {
            FacesMessage msg = MessageHandler.getMessage("invalid_phone_number", MessageType.ERROR);
            throw new ValidatorException(msg);
        }
    }
}