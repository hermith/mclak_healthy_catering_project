package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import locale.MessageHandler;
import locale.MessageType;

/**
 *
 * @author linnk
 */
@FacesValidator("PasswordValidator")
public class PasswordValidator implements Validator {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    private Pattern pattern;
    private Matcher matcher;

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        UIInput fieldNewPassword = (UIInput) component.findComponent("newPassword");
        UIInput fieldNewPassword1 = (UIInput) component.findComponent("newPassword1");

        String newPassword = (String) fieldNewPassword.getLocalValue();
        String newPassword1 = (String) fieldNewPassword1.getLocalValue();

        if (newPassword == null || newPassword1 == null) {
            return;
        }

        if (!newPassword.equals(newPassword1)) {
            FacesMessage message = MessageHandler.getMessage("password_not_match", MessageType.ERROR);
            throw new ValidatorException(message);
        }
        if (newPassword.length() < 8) {
            FacesMessage message = MessageHandler.getMessage("password_not_min_length", MessageType.ERROR);
            throw new ValidatorException(message);
        }
        matcher = pattern.matcher(newPassword);
        if (!matcher.matches()) {
            FacesMessage message = MessageHandler.getMessage("password_invalid", MessageType.ERROR);
            throw new ValidatorException(message);
        }
    }
}
