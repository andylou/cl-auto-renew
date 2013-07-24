
package clautorenew.ad;

import clautorenew.conf.Account;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Hermoine
 */
public class AdTaskImpl implements Task {
    private static final long serialVersionUID = 13L;
    private Account account;
    public AdTaskImpl(Account account){
        this.account = account;
    }
    public void autoRenew() throws IOException, UnsupportedEncodingException, URISyntaxException{
        AdsStore store = account.getStore();
        store.renew(getRenewableAd());
    }
    public Ad getRenewableAd(){
        boolean renewable = false;
        Ad r_ad = new Ad();
        DefaultListModel<Ad> ads = account.getStore().getAdModel();
        for(int i=0;i<ads.size();i++ ){
            Ad ad = ads.get(i);
            for(Form form: ad.getActions()){
                if(form.toString().contains("renew")){
                    renewable = form.toString().contains("renew");
                    break;
                }
            }
            if(renewable){
                r_ad=ad;
                break;
            }
                
        }
        return r_ad;
    }
    @Override
    public void run() {
        try {
            autoRenew();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AdTaskImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdTaskImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(AdTaskImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
