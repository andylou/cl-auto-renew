
package clautorenew.ad;

import clautorenew.ad.Ad;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.http.HttpEntity;
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

public class AdsStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private String html;
    private static AdsStore inst;

    private CookieStore cookiestore;
    DefaultListModel<Ad> adModel;
    public AdsStore(){
        adModel = new DefaultListModel();
    }

    public static void setInst(AdsStore inst) {
        AdsStore.inst = inst;
    }

    public CookieStore getCookiestore() {
        return cookiestore;
    }

    public void setCookiestore(CookieStore cookiestore) {
        this.cookiestore = cookiestore;
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
    //parse html crawled from listings page
    private void processStream(String html) throws IOException{
        Document doc = Jsoup.parse(html);
        
        //Elements m_body = doc.select("table[summary='postings']");
        Elements m_body = doc.getElementsByAttributeValue("summary", "postings");
        
        Elements children = m_body.get(0).children().get(0).children();
        
        int i =0;
        for (Iterator<Element> it = children.iterator(); it.hasNext();) {
            Element e = it.next();
            if(i!=0){
                String status = e.select("td[class=status]").text();
                if((!status.contains("Deleted")) && (!status.contains("Expired"))){
                    Ad ad = new Ad();
                    ad.setStatus(status);
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

//                    if(!listings.contains(ad)){
//                        listings.add(0,ad);
//                    }
                    
                    if(!adModel.contains(ad)){
                        adModel.add(0, ad);
                    }
                }
            }
            i++;    
             
        }
        
    }
    
    public synchronized DefaultListModel<Ad> fetchUpdate(InputStream is) throws IOException{
        
        DefaultListModel<Ad> model = new DefaultListModel<>();
        
        Document doc = Jsoup.parse(is,"iso-8859-1","https://accounts.craigslist.org" );
        
        //Elements m_body = doc.select("table[summary='postings']");
        Elements m_body = doc.getElementsByAttributeValue("summary", "postings");
        
        Elements children = m_body.get(0).children().get(0).children();
        
        int i =0;
        for (Iterator<Element> it = children.iterator(); it.hasNext();) {
            Element e = it.next();
            if(i!=0){
                String status = e.select("td[class=status]").text();
                if((!status.contains("Deleted")) && (!status.contains("Expired"))){
                    Ad ad = new Ad();
                    ad.setStatus(status);
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
                        model.add(0, ad);
                }
            }
            i++;    
             
        }
        return model;
    }

    public DefaultListModel<Ad> getAdModel() {
        return adModel;
    }
    public boolean hasRenewable(){
        boolean renewable = false;
        for(Ad ad: (Ad[])adModel.toArray()){
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
    /*public ArrayList<Ad> getListings() {
     * return listings;
     * }*/
    
    public void renewAll() throws UnsupportedEncodingException, IOException, URISyntaxException {
        
        for(Ad ad: (Ad[])adModel.toArray()){
            ArrayList<Form> forms = ad.getActions();
            
            for(Form f: forms){
                if(f.getActionType().equals("renew")){
                    DefaultHttpClient httpclient = this.getHttpClientInstance();
                    
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
                            
                         adModel.get(adModel.indexOf(ad)).setStatus("Active");
                         //listings.get(listings.indexOf(ad)).setStatus("Active");
                            
                    }
                    httpclient.getConnectionManager().shutdown();
                        
                }
            }
            
        }
    }
    public String getPublicUrl(String url) throws IOException{
        
        DefaultHttpClient httpclient = this.getHttpClientInstance();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        
        HttpEntity entity = response.getEntity();
        
        Scanner in = new Scanner(entity.getContent());
        StringBuffer sb = new StringBuffer();
        
        while(in.hasNextLine()){
            sb.append(in.nextLine());
        }
        Document doc = Jsoup.parse(sb.toString());
        
        String public_url = doc.select("table[summary=status]").get(0).children().get(0).select("a[target=_blank]").attr("href");
        
        return public_url;
    }
    public void renew(Ad ad) throws UnsupportedEncodingException, IOException, URISyntaxException {
        
        ArrayList<Form> forms = ad.getActions();

        for(Form f: forms){
            if(f.getActionType().equals("renew")){
                DefaultHttpClient httpclient = this.getHttpClientInstance();

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
                    adModel.get(adModel.indexOf(ad)).setStatus("Active");
                    //listings.get(listings.indexOf(ad)).setStatus("Active");
                        
                }
                httpclient.getConnectionManager().shutdown();

            }
        }
    }
    
    public void delete(Ad ad) throws UnsupportedEncodingException, IOException, URISyntaxException {
        
        ArrayList<Form> forms = ad.getActions();

        for(Form f: forms){
            if(f.getActionType().equals("delete")){
                DefaultHttpClient httpclient = this.getHttpClientInstance();
                
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
                        adModel.removeElement(ad);

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
        httpclient.setCookieStore(this.getCookiestore());
        return httpclient;
    }
}
