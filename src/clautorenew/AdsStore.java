/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Hermoine
 */

public class AdsStore {
    private String html;
    private static AdsStore inst;
    private ArrayList<Ad> listings;
    private CookieStore cookeiestore;
    private DefaultListModel<Ad> listView;
    private AdsStore(){
        listView = new DefaultListModel();
        listings = new ArrayList<>();
    }
    
    public static AdsStore getInstance(){
        if(inst==null)
            inst = new AdsStore();
        return inst;
    }

    public static AdsStore getInst() {
        return inst;
    }

    public static void setInst(AdsStore inst) {
        AdsStore.inst = inst;
    }

    public DefaultListModel<Ad> getListView() {
        return listView;
    }

    public void setListView(DefaultListModel<Ad> listView) {
        this.listView = listView;
    }
    
    public CookieStore getCookeiestore() {
        return cookeiestore;
    }

    public void setCookeiestore(CookieStore cookeiestore) {
        this.cookeiestore = cookeiestore;
    }
    
    public String getHtml(){
        return html;
    }
    
    public void setHtml(String html ){
        this.html = html;
        
        if(html != null){
            try {
                processStream(html);
            } catch (IOException ex) {
                Logger.getLogger(AdsStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void processStream(String html) throws IOException{
        Document doc = Jsoup.parse(html);
        
        //Elements m_body = doc.select("table[summary='postings']");
        Elements m_body = doc.getElementsByAttributeValue("summary", "postings");
        
        Elements children = m_body.get(0).children().get(0).children();
        
        int i =0;
        for (Iterator<Element> it = children.iterator(); it.hasNext();) {
            Element e = it.next();
            if(i!=0){
                Ad ad = new Ad();
                ad.setStatus(e.select("td[class=status]").text());
                ad.setTitle(e.select("td[class=title]").text());
                ad.setUrl(e.select("td[class=title]").select("a").attr("href"));

                Elements actions = e.select("form");
                
                //split actions to its different types
                if(actions != null && actions.size()>0){
                    //process button actions on ads
                    
                    ArrayList<Form> buttons = new ArrayList();
                    for(Element formElement: actions){
                        Form form = new Form();
                        form.setAction(formElement.attr("action"));
                        form.setMethod(formElement.attr("method"));
                        
                        Elements formChildren = formElement.children();
                        //loop through input elements
                        for(Element input :formChildren){
                            FormInput formInput = new FormInput();
                            
                            //parse the input elements used as buttons/ actions on CL;
                            String name = input.attr("name");
                            String type = input.attr("type");
                            String value = input.attr("value");        
                                    
                            formInput.setName(name);
                            formInput.setType(type);
                            formInput.setValue(value);
                            
                            //set the form type
                            if(type.equals("submit")){
                                form.setActionType(formInput.getValue());
                            }
                            
                            if(value.contains("renew")){
                                ad.setStatus("Inactive");
                            }
                            
                            form.addInputElement(formInput);
                        }
                        
                        buttons.add(form);
                        
                    }
                    ad.setActions(buttons);
                    
                }

                listings.add(ad);
            }
            i++;    
             
        }
        
        
        
        System.out.println(children.size());
        
    }
    public boolean hasRenewable(){
        boolean renewable = false;
        for(Ad ad: listings){
            for(Form form: ad.getActions()){
                if(form.toString().contains("renew")){
                    renewable = form.toString().contains("renew");
                    break;
                }
            }
            if(renewable)
                break;
        }
        
        return renewable;
    }
    public ArrayList<Ad> getListings() {
        return listings;
    }
    
    public void renewAll() throws UnsupportedEncodingException, IOException, URISyntaxException {
        
        for(Ad ad: listings){
            ArrayList<Form> forms = ad.getActions();
            
            for(Form f: forms){
                if(f.getActionType().equals("renew")){
                    DefaultHttpClient httpclient = (DefaultHttpClient) getHttpClientInstance();
                    httpclient.setCookieStore(this.getCookeiestore());
                    HttpContext localContext = new BasicHttpContext();
                    String action = f.getAction();
                    String method = f.getMethod();
                    int statusCode = 404;
                    if(method.equalsIgnoreCase("post")){
                        
                        HttpPost httpost = new HttpPost(action);
                        List <NameValuePair> nvps = new ArrayList <>();
                        
                        for(FormInput finput: f.getInputsElements()){
                            if(!finput.getType().equals("submit"))
                                nvps.add(new BasicNameValuePair(finput.getName(), finput.getValue()));
                        }
                        
                        httpost.setEntity(new UrlEncodedFormEntity(nvps));
                        HttpResponse response = httpclient.execute(httpost, localContext);
                        statusCode = response.getStatusLine().getStatusCode();
                        //System.out.println("POST: " +response.getStatusLine());

                    }else{
                        URIBuilder uriBuilder = new URIBuilder(action);
                        for(FormInput finput: f.getInputsElements()){
                            uriBuilder.addParameter(finput.getName(), finput.getValue());
                        }
                        
                        HttpGet httpget = new HttpGet(uriBuilder.build());
                        HttpResponse response = httpclient.execute(httpget, localContext);
                        statusCode = response.getStatusLine().getStatusCode();
                       // System.out.println("GET: " +response.getStatusLine());
                    }
                    if(statusCode == 200){
                            int index = listings.indexOf(ad);
                            listings.remove(ad);
                            listView.removeElement(ad);
                            
                            ad.setStatus("Active");
                            listings.add(index,ad);
                            listView.add(index,ad);
                            
                    }
                    httpclient.getConnectionManager().shutdown();
                        
                }
            }
            
        }
    }
    
    public void renew(Ad ad) throws UnsupportedEncodingException, IOException, URISyntaxException {
        
        ArrayList<Form> forms = ad.getActions();

        for(Form f: forms){
            if(f.getActionType().equals("renew")){
                DefaultHttpClient httpclient = (DefaultHttpClient) getHttpClientInstance();
                httpclient.setCookieStore(this.getCookeiestore());
                HttpContext localContext = new BasicHttpContext();
                String action = f.getAction();
                String method = f.getMethod();
                int statusCode = 404;
                if(method.equalsIgnoreCase("post")){

                    HttpPost httpost = new HttpPost(action);
                    List <NameValuePair> nvps = new ArrayList <>();

                    for(FormInput finput: f.getInputsElements()){
                        if(!finput.getType().equals("submit"))
                            nvps.add(new BasicNameValuePair(finput.getName(), finput.getValue()));
                    }

                    httpost.setEntity(new UrlEncodedFormEntity(nvps));
                    HttpResponse response = httpclient.execute(httpost, localContext);
                    statusCode = response.getStatusLine().getStatusCode();
                    //System.out.println("POST: " +response.getStatusLine());

                }else{
                    URIBuilder uriBuilder = new URIBuilder(action);
                    for(FormInput finput: f.getInputsElements()){
                        uriBuilder.addParameter(finput.getName(), finput.getValue());
                    }

                    HttpGet httpget = new HttpGet(uriBuilder.build());
                    HttpResponse response = httpclient.execute(httpget, localContext);
                    statusCode = response.getStatusLine().getStatusCode();
                   // System.out.println("GET: " +response.getStatusLine());
                }
                if(statusCode == 200){
                        int index = listings.indexOf(ad);
                        listings.remove(ad);
                        listView.removeElement(ad);

                        ad.setStatus("Active");
                        listings.add(index,ad);
                        listView.add(index,ad);

                }
                httpclient.getConnectionManager().shutdown();

            }
        }
    }
    
    public DefaultHttpClient getHttpClientInstance(){
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
        
        return httpclient;
    }
}
