package validators;

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
        if (newPassword.length() < 6) {
            FacesMessage message = MessageHandler.getMessage("password_not_min_length", MessageType.ERROR);
            throw new ValidatorException(message);
        }
        if (!isValid(newPassword)) {
            FacesMessage message = MessageHandler.getMessage("password_invalid", MessageType.ERROR);
            throw new ValidatorException(message);
        }
    }

    /**
     * Checks if newPassword contains a special character and a number.
     *
     * @param newPassword
     * @param newPassword1
     * @return if the password is approved
     */
    public boolean isValid(String newPassword) {
        final String SPESIALTEGN = "§!?=+*@()/";
        final String NUMBERS = "1234567890";
        boolean okNumbers = false;
        boolean okSpecial = false;
        for (int i = 0; i < newPassword.length(); i++) {
            for (int j = 0; j < 10; j++) {
                if (newPassword.charAt(i) == SPESIALTEGN.charAt(j)) {
                    okSpecial = true;
                }
                if (newPassword.charAt(i) == NUMBERS.charAt(j)) {
                    okNumbers = true;
                }
            }
        }
        if (okSpecial && okNumbers) {
            return true;
        }
        return false;
    }
}
