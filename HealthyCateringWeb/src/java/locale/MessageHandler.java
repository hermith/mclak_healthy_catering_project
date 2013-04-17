/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author linnk
 */
public class MessageHandler {

    public static void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(message));
    }

    public static String getLocalizedText(MessageType messageType, String text_id) {
        FacesContext context = FacesContext.getCurrentInstance();
        String type = "";
        switch(messageType){
            case TEKST:
                type = "#{texts.";
                break;
            case ERROR:
                type = "#{error.";
                break;
        }
        return context.getApplication().evaluateExpressionGet(context, type + text_id + "}", String.class);
    }
}