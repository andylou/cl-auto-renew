/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.ad;

import clautorenew.ad.AdsStore;
import clautorenew.ad.Ad;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Hermoine
 */
public class MonitorAds extends TimerTask {
    private void fetchAds() throws IOException{
        AdsStore store = new AdsStore();
        DefaultHttpClient httpclient = store.getHttpClientInstance();
        System.out.println(httpclient.getCookieStore());
        CookieStore cookies = httpclient.getCookieStore();
        for(Cookie cookie: cookies.getCookies()){
            System.out.println(cookie.getName() +" : "+ cookie.getValue());
        }
         
         HttpGet httpget = new HttpGet("https://accounts.craigslist.org/");
         
         HttpResponse response = httpclient.execute(httpget);
         
         HttpEntity entity = response.getEntity();
         
         Scanner input = new Scanner(entity.getContent());
         StringBuffer sb = new StringBuffer();
          
         /*while(input.hasNextLine()){
            System.out.println(input.nextLine());
            sb.append(input.nextLine());
         }*/
         //give the thread enough time to finish
         
         DefaultListModel<Ad> model = store.fetchUpdate(entity.getContent());
         
         EntityUtils.consume(entity);
         httpclient.getConnectionManager().shutdown();
         
         for(int i=0; i<model.size();i++){
             Ad update_ad = model.get(i);
             boolean found = false;
             for(int j=0;j<store.getAdModel().size();j++){
                 Ad current_ad = store.getAdModel().get(j);
                 
                 if(update_ad.getUrl().equals(current_ad.getUrl())){
                     store.getAdModel().get(j).setStatus(update_ad.getStatus());
                     store.getAdModel().get(j).setActions(update_ad.getActions());
                     store.getAdModel().get(j).setTitle(update_ad.getTitle());
                     
                     found = true;
                 }
             }
             if(!found){
                store.getAdModel().add(0, update_ad);
            }
         }
        
    }
    @Override
    public void run() {
        try {
            fetchAds();
        } catch (IOException ex) {
            Logger.getLogger(MonitorAds.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
