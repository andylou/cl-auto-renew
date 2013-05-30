
package clautorenew.conf;

import clautorenew.ad.AdsStore;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Hermoine
 */
public class LoginProcessor {
    private static boolean wasRedirected = false;
    public static boolean doLogin(String email, String password) throws UnsupportedEncodingException, IOException{
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
        
        HttpEntity entity = response.getEntity();

        System.out.println(email +" Logged in at "+new Date());
        
        Scanner input = new Scanner(entity.getContent());
        StringBuffer sb = new StringBuffer();
        
        while(input.hasNextLine()){
            sb.append(input.nextLine());
        }
        
        AdsStore store = new AdsStore();
        store.setHtml(sb.toString());
        
        store.setCookiestore(httpclient.getCookieStore());
        
        EntityUtils.consume(entity);
        
        httpclient.getConnectionManager().shutdown();
        return true;
        
    }
}
