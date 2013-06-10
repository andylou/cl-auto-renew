/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.ad;

import clautorenew.conf.Account;
import clautorenew.conf.Configuration;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultListModel;

/**
 *
 * @author Hermoine
 */
public class AccountUpdate implements Serializable{
    private static ScheduledThreadPoolExecutor pool;
    private boolean isStarted=false;
    private Map<Account, ScheduledFuture<?>> scheduledAccounts;
    private Map<Account, ScheduledFuture<?>> scheduledAd;
    private final static int DELAY= 0;
    private final static int PERIOD=1;
    
    private static AccountUpdate inst;
    
    private AccountUpdate(){
        scheduledAccounts = new HashMap<>();
        scheduledAd = new HashMap<>();
        pool = new ScheduledThreadPoolExecutor(10);
        
    }
    public static AccountUpdate getInstance(){
        if(inst == null)
            inst = new AccountUpdate();
        return inst;
    }
    public void startUpdate(){
        Configuration config = Configuration.getInstance();
        DefaultListModel<Account> accounts = config.getAccounts();
        
        for(int i=0;i<accounts.size();i++){
            int ad_period = 0;//get it from config here
            //int ad_unit = 2;
            TimeUnit ad_unit = TimeUnit.DAYS;
            ScheduledFuture<?> ac_future = pool.scheduleAtFixedRate(new AccountTaskImpl(accounts.get(i)), DELAY, PERIOD, TimeUnit.MINUTES);
            ScheduledFuture<?> ad_future = pool.scheduleAtFixedRate(new AdTaskImpl(accounts.get(i)), DELAY, ad_period, ad_unit);
            scheduledAccounts.put(accounts.get(i), ac_future);
            scheduledAd.put(accounts.get(i), ad_future);
        }
    }
    public void schedule(Account account){
        ScheduledFuture<?> future = pool.scheduleAtFixedRate(new AccountTaskImpl(account), DELAY, PERIOD, TimeUnit.MINUTES);
        scheduledAccounts.put(account, future);
    }
    
    public void remove(Account account){
        ScheduledFuture<?> ac_future = scheduledAccounts.get(account);
        ScheduledFuture<?> ad_future = scheduledAccounts.get(account);
        ac_future.cancel(true);
        ad_future.cancel(true);
        scheduledAccounts.remove(account);
        scheduledAd.remove(account);
    }
    
}
