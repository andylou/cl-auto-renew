/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Hermoine
 */
public class HttpClientDemo {
    public HttpClientDemo(){
        
        //CReate a httpClient
        DefaultHttpClient httpClient = new DefaultHttpClient();
        DefaultHttpClient httpClient2 = new DefaultHttpClient();
        
        
        //Using GET
        HttpGet httpGet = new HttpGet("https://accounts.craigslist.org/");
        try {
            HttpResponse resp = httpClient.execute(httpGet);
            System.out.println(resp.getStatusLine());
            
             // do something useful with the response body
            // and ensure it is fully consumed
            HttpEntity entity = resp.getEntity();
            //Scanner in = new Scanner(entity.getContent());
            
            //while(in.hasNextLine()){
                //System.out.println(in.nextLine());
            //}
            
            EntityUtils.consume(entity);
            
        } catch (IOException ex) {
            Logger.getLogger(HttpClientDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            httpGet.releaseConnection();
        }
        
        HttpContext localContext = new BasicHttpContext();
        
        //USING POST
        HttpPost httpPost = new HttpPost("https://accounts.craigslist.org/login");
        List <NameValuePair> nvps = new ArrayList();
        nvps.add(new BasicNameValuePair("inputEmailHandle", "farrismccall@gmail.com"));
        nvps.add(new BasicNameValuePair("inputPassword", "fibber1953"));
        

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));//set request enity
            HttpResponse response2 = httpClient.execute(httpPost, localContext);
            System.out.println(response2.getStatusLine());
            System.out.println(response2.getFirstHeader("Location"));
            
            HttpHost target = (HttpHost) localContext.getAttribute(
                ExecutionContext.HTTP_TARGET_HOST);
            HttpUriRequest req = (HttpUriRequest) localContext.getAttribute(
            ExecutionContext.HTTP_REQUEST);
            

            System.out.println("Final request URI: " + req.getURI()); // relative URI (no proxy used)
            System.out.println("Final request method: " + req.getMethod());
            httpPost.releaseConnection();
            System.out.println("Final target: " + target);
            
            
            HttpGet request2 = new HttpGet(target.toURI());
            HttpResponse respons = httpClient.execute(request2);
            
            
            HttpEntity entity2 = response2.getEntity();//response entity
            
            
            // do something useful with the response body
            // and ensure it is fully consumed
            Scanner in = new Scanner(entity2.getContent());
            
            while(in.hasNextLine()){
                System.out.println(in.nextLine());
            }
            
            
            EntityUtils.consume(entity2);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpClientDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpClientDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            httpPost.releaseConnection();
            httpClient.getConnectionManager().shutdown();
        }
        
    }
    
    
}
