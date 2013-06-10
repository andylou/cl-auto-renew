
package clautorenew.conf;

import clautorenew.ad.AdsStore;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author Hermoine
 */
public class LoginProcessor {
    private static boolean wasRedirected = false;
    public static boolean doLogin(String email, String password, AdsStore store) throws UnsupportedEncodingException, IOException{
        DefaultHttpClient  httpclient = new DefaultHttpClient();
        httpclient.setRedirectStrategy(new DefaultRedirectStrategy() {                
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                boolean isRedirect=false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                    
                } catch (ProtocolException e) {
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        wasRedirected = true;
                        return true;
                    }
                }
                return isRedirect;
            }
        });
        
        HttpPost httpost = new HttpPost("https://accounts.craigslist.org/login");
        List <NameValuePair> nvps = new ArrayList <>();
        nvps.add(new BasicNameValuePair("inputEmailHandle", email));
        nvps.add(new BasicNameValuePair("inputPassword", password));
        nvps.add(new BasicNameValuePair("rt", ""));
        nvps.add(new BasicNameValuePair("rp", ""));

        httpost.setEntity(new UrlEncodedFormEntity(nvps));
        HttpContext localContext = new BasicHttpContext();
        HttpResponse response = httpclient.execute(httpost, localContext);
        
        if(!wasRedirected){
            return false;
        }
        
        System.out.println(email +" Logged in at " + new Date());
        
        store.setCookiestore(httpclient.getCookieStore());
        httpclient.getConnectionManager().shutdown();
        return true;
        
    }
}
