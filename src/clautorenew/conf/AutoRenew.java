/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.conf;

import clautorenew.ad.AdsStore;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hermoine
 */
public class AutoRenew extends TimerTask {
    
    private void autoRenew(){
        AdsStore store = new AdsStore();
        try {
            store.renewAll();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AutoRenew.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(AutoRenew.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
        System.out.println("Renewing now");
        //autoRenew();
    }
    
}
