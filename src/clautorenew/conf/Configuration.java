/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.conf;

import clautorenew.conf.Account;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
    private Configuration(){
        try {
            load();
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
        
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("wfkwebfewf"));
        os.writeObject(accounts);
        os.flush();
        os.close();    
        
    }
    public void load() throws IOException, ClassNotFoundException{
        accounts = new DefaultListModel<>();
        File f = new File("wfkwebfewf");
        if(!f.exists()){
            f.createNewFile();
        }
        
        ObjectInputStream is = new ObjectInputStream(new FileInputStream("wfkwebfewf"));
        accounts = (DefaultListModel<Account>) is.readObject();
        
        is.close();
        
    }

    public DefaultListModel<Account> getAccounts() {
        return accounts;
    }
    
    public static int loadRenewInterval(){
        return 0;
    }
    
}
