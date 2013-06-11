/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.conf;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
/**
 *
 * @author Hermoine
 */
public class Configuration {
    private DefaultListModel<Account> accounts;
    private static Configuration instance;
    private int time;
    private String timeunit;
    private boolean autorenew=false;
    private Configuration(){
        try {
            load();
            //loadInterval();
        }catch(EOFException ex){
            //do nothing for eof exceptions
        }catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Configuration getInstance(){
        if(instance == null)
            instance = new Configuration();
        return instance;
    }
    
    public synchronized void addAccount(Account account){
        accounts.add(0,account);
        try {
            save();
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void deleteAccount(Account account){
        accounts.removeElement(account);
        try {
            save();
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void save() throws IOException{
        
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("conf.dat"));
        os.writeObject(accounts);
        os.flush();
        os.close();    
        
    }
    public void load() throws IOException, ClassNotFoundException{
        accounts = new DefaultListModel<>();
        File f = new File("conf.dat");
        if(!f.exists()){
            f.createNewFile();
        }
        
        ObjectInputStream is = new ObjectInputStream(new FileInputStream("conf.dat"));
        accounts = (DefaultListModel<Account>) is.readObject();
        
        is.close();
        
    }
    public void loadInterval() throws IOException, ClassNotFoundException{
        File f = new File("exad.dat");
        if(!f.exists()){
            f.createNewFile();
        }
        
        ObjectInputStream is = new ObjectInputStream(new FileInputStream("exad.dat"));
        autorenew = is.readBoolean();
        time = is.readInt();
        timeunit = (String)is.readObject();
        is.close();
        
    }
    public void saveInterval() throws FileNotFoundException, IOException{
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("exad.dat"));
        os.writeBoolean(autorenew);
        os.writeInt(time);
        os.writeObject(timeunit);
        os.flush();
        os.close();
    }
    public DefaultListModel<Account> getAccounts() {
        return accounts;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeunit() {
        return timeunit;
    }

    public void setTimeunit(String timeunit) {
        this.timeunit = timeunit;
    }

    public boolean isAutorenew() {
        return autorenew;
    }

    public void setAutorenew(boolean autorenew) {
        this.autorenew = autorenew;
    }
    
    
    
}
