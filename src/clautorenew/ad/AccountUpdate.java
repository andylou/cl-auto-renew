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
    private static final long serialVersionUID = 90L;
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
    public void startAccountUpdate(){
        Configuration config = Configuration.getInstance();
        DefaultListModel<Account> accounts = config.getAccounts();
        
        for(int i=0;i<accounts.size();i++){
            ScheduledFuture<?> ac_future = pool.scheduleAtFixedRate(new AccountTaskImpl(accounts.get(i)), DELAY, PERIOD, TimeUnit.MINUTES);
            scheduledAccounts.put(accounts.get(i), ac_future);
        }
    }
    public void beginAutoRenew(int period, String timeunit){
        if(!scheduledAd.isEmpty()){
            cancelAutorenew();
        }
        Configuration config = Configuration.getInstance();
        DefaultListModel<Account> accounts = config.getAccounts();
        TimeUnit time_unit = TimeUnit.SECONDS;
        switch(timeunit){
            case "Seconds":
                time_unit = TimeUnit.SECONDS;
                break;
            case "Minutes":
                time_unit = TimeUnit.MINUTES;
                break;
            case "Hours":
                time_unit = TimeUnit.HOURS;
                break;
            default:
                time_unit = TimeUnit.MINUTES;
        }
        for(int i=0;i<accounts.size();i++){
            ScheduledFuture<?> ad_future = pool.scheduleAtFixedRate(new AdTaskImpl(accounts.get(i)), DELAY, period, time_unit);
            scheduledAd.put(accounts.get(i), ad_future);
        }
    }
    public void cancelAutorenew(){
        for(ScheduledFuture<?> f: scheduledAd.values()){
            if(f!=null){
                f.cancel(true);
            }
        }
        scheduledAd.clear();
    }
    public void schedule(Account account){
        ScheduledFuture<?> future = pool.scheduleAtFixedRate(new AccountTaskImpl(account), DELAY, PERIOD, TimeUnit.MINUTES);
        scheduledAccounts.put(account, future);
    }
    
    public void remove(Account account){
        ScheduledFuture<?> ac_future = scheduledAccounts.get(account);
        ScheduledFuture<?> ad_future = scheduledAccounts.get(account);
        if(ac_future!=null){
            ac_future.cancel(true);
        }
        if(ad_future!=null){
            ad_future.cancel(true);
        }
        scheduledAccounts.remove(account);
        scheduledAd.remove(account);
    }
    
}
