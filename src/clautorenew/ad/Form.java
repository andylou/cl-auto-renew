
package clautorenew.ad;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Hermoine
 */
public class Form implements Serializable{
    private String method;
    private String action;
    private String actionType;
    private ArrayList<FormInput> inputsElements = new ArrayList();
    private static final long serialVersionUID = 40L;
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<FormInput> getInputsElements() {
        return inputsElements;
    }

    public void setInputsElements(ArrayList<FormInput> inputsElements) {
        this.inputsElements = inputsElements;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public void addInputElement(FormInput input){
        this.inputsElements.add(input);
    }
    
    @Override
    public String toString() {
        return this.getActionType();
    }
    
}
