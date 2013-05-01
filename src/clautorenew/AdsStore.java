/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private AdsStore(){
        
        listings = new ArrayList<>();
    }
    
    public static AdsStore getInstance(){
        if(inst==null)
            inst = new AdsStore();
        return inst;
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
                ad.setActions(e.select("form"));
                
                listings.add(ad);
            }
            i++;    
             
        }
        
        
        
        System.out.println(children.size());
        
    }

    public ArrayList<Ad> getListings() {
        return listings;
    }
    
    
}
