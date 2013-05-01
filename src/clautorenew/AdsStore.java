/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author Hermoine
 */
public class AdsStore {
    private InputStream page;
    private static AdsStore inst;
    private ArrayList<Ad> listings;
    
    private AdsStore(){
        inst = new AdsStore();
        listings = new ArrayList<>();
    }
    
    public static AdsStore getInstance(){
        if(inst==null)
            inst = new AdsStore();
        return inst;
    }
    
    public InputStream getPage(){
        return null;
    }
    
    public void setPage(InputStream page){
        this.page = page;
        
        if(page != null){
           processStream(page);
        }
    }
    private void processStream(InputStream is){
        
    }
    
    
}
