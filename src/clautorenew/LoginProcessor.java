/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
    public static boolean doLogin(String email, String password) throws UnsupportedEncodingException, IOException{
        DefaultHttpClient  httpclient = new DefaultHttpClient();
        httpclient.setRedirectStrategy(new DefaultRedirectStrategy() {                
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                boolean isRedirect=false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
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
        
        int status = response.getStatusLine().getStatusCode();
        Header redirectHeader = response.getFirstHeader("Location");
        
        if(redirectHeader ==null || status !=200){
            return false;
        }
        
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        
        Scanner input = new Scanner(entity.getContent());
        StringBuffer sb = new StringBuffer();
        
        while(input.hasNextLine()){
            sb.append(input.nextLine());
        }
        
        AdsStore store = AdsStore.getInstance();
        store.setHtml(sb.toString());
        
        EntityUtils.consume(entity);
        
        System.out.println("Done");
        return false;
        
    }
}
