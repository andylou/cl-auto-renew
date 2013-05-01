/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import org.jsoup.select.Elements;

/**
 *
 * @author Hermoine
 */
public class Ad {
    private String status;
    private Elements actions;
    private String url;
    private String title;

    public String getStatus() {
        return status;
    }

    public Elements getActions() {
        return actions;
    }

    public void setActions(Elements actions) {
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

    public Ad(String status, Elements actions, String url, String title) {
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
