/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.conf;

import clautorenew.ad.AdsStore;

/**
 *
 * @author Hermoine
 */
public class Account {
    
    private String email;
    private String password;
    private String accountName;
    private boolean autorenewAll;
    private int interval =60* 1000;
    private AdsStore store = new AdsStore();
    public Account(String email, String password, String accountName, boolean autorenewAll) {
        this.email = email;
        this.password = password;
        this.accountName = accountName;
        this.autorenewAll = autorenewAll;
        
    }

    public AdsStore getStore() {
        return store;
    }

    public void setStore(AdsStore store) {
        this.store = store;
    }
    
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isAutorenewAll() {
        return autorenewAll;
    }

    public void setAutorenewAll(boolean autorenewAll) {
        this.autorenewAll = autorenewAll;
    }

    @Override
    public String toString() {
        return this.getEmail();
    }
    
    
}
