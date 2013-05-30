/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.conf;

import clautorenew.conf.Account;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author Hermoine
 */
public class Configuration {
    private DefaultListModel<Account> accounts;
    private static Configuration instance;
    private Configuration(){
        load();
    }
    
    public static Configuration getInstance(){
        if(instance == null)
            instance = new Configuration();
        return instance;
    }
    
    public void addAccount(Account account){
        accounts.add(0,account);
        save();
    }
    
    public void deleteAccount(Account account){
        accounts.removeElement(account);
        save();
    }
    
    public void save(){
        
        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("wfkwebfewf")))){
            os.writeObject(accounts);
        }catch(Exception e){
        }
    }
    public void load(){
        try(ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("wfkwebfewf")))){
            accounts = (DefaultListModel<Account>) is.readObject();
        }catch(Exception e){
        }
    }

    public DefaultListModel<Account> getAccounts() {
        return accounts;
    }
    
    
}
