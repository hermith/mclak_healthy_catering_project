/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Static methods used to simplify getting localized messages directly form
 * Java. Used to get both normal localized text and localized error messages.
 *
 * @author linnk
 */
public class MessageHandler {

    /**
     * "Adds" a new error message to the h:messages xhtml object.
     *
     * @param message Message to be shown in h:message/h:messages
     */
    public static void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(message));
    }

    /**
     * Returns a localized string based on the keyboard for the text.
     *
     * @param msg Message string id
     * @param messageType Enum of what type, Error og Tekst
     * @return Localized text
     */
    public static FacesMessage getMessage(String msg, MessageType messageType) {
        String text = getLocalizedText(messageType, msg);
        return new FacesMessage(text);
    }

    /**
     * Additional logic for getMessage method.
     *
     * @param messageType Enum of what type, Error og Tekst
     * @param text_id Message string id
     * @return Localized text
     */
    private static String getLocalizedText(MessageType messageType, String text_id) {
        FacesContext context = FacesContext.getCurrentInstance();
        String type = "";
        switch (messageType) {
            case TEKST:
                type = "#{texts.";
                break;
            case ERROR:
                type = "#{error.";
                break;
        }
        return context.getApplication().evaluateExpressionGet(context, type
                + text_id + "}", String.class);
    }
}