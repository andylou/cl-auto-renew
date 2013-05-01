/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

/**
 *
 * @author Hermoine
 */
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


public final class ConnectionMain {
    private List<Cookie> cookies;
    
    public ConnectionMain() {
        try {
            doPost();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void doGet() throws URISyntaxException, IOException{
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // httpclient.getParams().setParameter(AllClientPNames.USER_AGENT, "")
         /*httpclient.setRedirectStrategy(new RedirectStrategy(){

             @Override
             public boolean isRedirected(HttpRequest hr, HttpResponse hr1, HttpContext hc) throws ProtocolException {
                 
             }

             @Override
             public HttpUriRequest getRedirect(HttpRequest hr, HttpResponse hr1, HttpContext hc) throws ProtocolException {
                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             }
             
             
         });*/
         //httpclient.getParams().setParameter(AllClientPNames.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        try {
//            URIBuilder builder = new URIBuilder();
//            builder.setScheme("https").setHost("accounts.craigslist.org").setPath("/login")
//                .setParameter("inputEmailHandle", "farrismccall@gmail.com")
//                .setParameter("inputPassword", "fibber1953");
//            URI uri = builder.build();
//            HttpGet httpget = new HttpGet(uri);
//            //HttpPost httpget = new HttpPost(uri);
//            
//            System.out.println(httpget.getURI());
            HttpGet httpget = new HttpGet("https://accounts.craigslist.org/login");

            HttpResponse response = httpclient.execute(httpget);

            System.out.println("Initial set of cookies:");
            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }
    public void doPost() throws IOException{
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
        nvps.add(new BasicNameValuePair("inputEmailHandle", "farrismccall@gmail.com"));
        nvps.add(new BasicNameValuePair("inputPassword", "fibber1953"));
        nvps.add(new BasicNameValuePair("rt", ""));
        nvps.add(new BasicNameValuePair("rp", ""));

        httpost.setEntity(new UrlEncodedFormEntity(nvps));
        HttpContext localContext = new BasicHttpContext();
        HttpResponse response = httpclient.execute(httpost, localContext);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        
        Scanner in = new Scanner(entity.getContent());
        
        while(in.hasNextLine()){
            System.out.println(in.nextLine());
        }
        
        EntityUtils.consume(entity);

        System.out.println("Done");
        //System.out.println("Post logon cookies:");
        
        
//        cookies = httpclient.getCookieStore().getCookies();
//        if (cookies.isEmpty()) {
//            System.out.println("None");
//        } else {
//            for (int i = 0; i < cookies.size(); i++) {
//                System.out.println("- " + cookies.get(i).toString());
//            }
//        }
//        
//        Header locationHeader = response.getFirstHeader("Location");
//        System.out.println("header: "+ locationHeader);
//        HttpHost target = (HttpHost) localContext.getAttribute(
//        ExecutionContext.HTTP_TARGET_HOST);
//        HttpUriRequest req = (HttpUriRequest) localContext.getAttribute(
//        ExecutionContext.HTTP_REQUEST);
//
//        System.out.println("Final request URI: " + req.getURI()); // relative URI (no proxy used)
//        System.out.println("Final request method: " + req.getMethod());
//        httpost.releaseConnection();
//        System.out.println("Final target: " + target);
        
        
    }
    
    
}
