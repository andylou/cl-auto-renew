/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.ad;

import clautorenew.conf.Account;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Hermoine
 */
public class AccountTaskImpl implements Task {
    private Account account;
    public AccountTaskImpl(Account account){
        this.account = account;
    }
    public void fetchUpdate() throws IOException{
        try{
            String url = "https://accounts.craigslist.org/";
    
            AdsStore store = account.getStore();
            System.out.println("Updating account: "+account.getAccountName());
            DefaultHttpClient httpclient = store.getHttpClientInstance();
            HttpGet getrequest = new HttpGet(url);

            HttpResponse response = httpclient.execute(getrequest);
            HttpEntity entity = response.getEntity();
            
            store.processStream(entity.getContent(), "iso-8859-1",url);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    } 
    @Override
    public void run() {
        try {
            this.fetchUpdate();
        } catch (IOException ex) {
            Logger.getLogger(AccountTaskImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
