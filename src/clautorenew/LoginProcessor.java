/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Hermoine
 */
public class LoginProcessor {
    public static boolean doLogin(String email, String password){
        //CReate a httpClient
        DefaultHttpClient httpClient = new DefaultHttpClient();
        
        //Using GET
        HttpGet httpGet = new HttpGet("https://accounts.craigslist.org/");
        try {
            HttpResponse resp = httpClient.execute(httpGet);
            System.out.println(resp.getStatusLine());
            
            if((resp.getStatusLine()).getStatusCode() == 200)
                return true;
            
             // do something useful with the response body
            // and ensure it is fully consumed
            HttpEntity entity = resp.getEntity();
            
            EntityUtils.consume(entity);
            
        } catch (IOException ex) {
            Logger.getLogger(HttpClientDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            httpGet.releaseConnection();
        }
        
        return false;
        
    }
}
