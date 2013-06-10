
package clautorenew.ad;

import java.io.Serializable;
import java.util.ArrayList;
import org.jsoup.select.Elements;

/**
 *
 * @author Hermoine
 */
public class Ad implements Serializable {
    private String status;// This could be deleted, expired, or inactive
    private ArrayList<Form> actions;
    private String url;
    private String title;

    public String getStatus() {
        return status;
    }

    public ArrayList<Form> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Form> actions) {
        this.actions = actions;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Ad(String status, ArrayList<Form> actions, String url, String title) {
        this.status = status;
        this.actions = actions;
        this.url = url;
        this.title = title;
    }

    public Ad() {
        this(null,null,null,null);
    }

    @Override
    public String toString() {
        return title; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
