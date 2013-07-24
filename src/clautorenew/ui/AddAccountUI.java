/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.ui;

import clautorenew.ad.AccountUpdate;
import clautorenew.ad.AccountUtil;
import clautorenew.ad.AdsStore;
import clautorenew.conf.Account;
import clautorenew.conf.Configuration;
import clautorenew.conf.LoginProcessor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class AddAccountUI extends JDialog {
    private static final long serialVersionUID = 125295725L;
    private JTextField nametxt, emailtxt;
    private JPasswordField passtxt;
    private JLabel errorlbl;
    private JButton addbtn;
    private MainFrame parent;
    private Account account = null;
    public AddAccountUI(MainFrame parent){
        this.parent = parent;
        setLayout(new MigLayout("wrap 2"));
        
        add(new JLabel("Account name"));
        nametxt = new JTextField(15);
        nametxt.addActionListener(new AddAction());
        add(nametxt);
        
        add(new JLabel("Email"));
        emailtxt = new JTextField(15);
        emailtxt.addActionListener(new AddAction());
        add(emailtxt);
        
        add(new JLabel("password"));
        passtxt = new JPasswordField(15);
        passtxt.addActionListener(new AddAction());
        add(passtxt);
        
        errorlbl = new JLabel("");
        errorlbl.setForeground(Color.red);
        add(errorlbl, "span 2");
        
        add(new JLabel(""));
        addbtn = new JButton("Add Account");
        addbtn.addActionListener(new AddAction());
        add(addbtn);
        
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    class AddAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            final String name = nametxt.getText();
            final String email = emailtxt.getText();
            final String password = new String(passtxt.getPassword());
            
            if(isBlank(password, email, name)){
                errorlbl.setText("Fields cannot be empty");
            }else{
                
                SwingWorker<Object, Void> accountWorker = new SwingWorker<Object, Void>() {
                    StatusWindow busywin = new StatusWindow(AddAccountUI.this);
                    @Override
                    protected Object doInBackground() throws Exception {
                        errorlbl.setText("");
                        busywin.setStatus("Verifying account information...");
                        AdsStore store = new AdsStore();
                        boolean doLogin = LoginProcessor.doLogin(email, password, store);
                        busywin.setStatus("Verifying account information...");
                        if(doLogin){
                            busywin.setStatus("Account Verrified!");
                            account = new Account(email, password, name, true);
                            account.setStore(store);
                            
                            AccountUtil acutil = new AccountUtil();
                            acutil.fetchAds(account);
                            
                            //persist new account on file system
                            Configuration config = Configuration.getInstance();
                            config.addAccount(account);
                            
                            parent.addAccount(account);
                            AccountUpdate updates = AccountUpdate.getInstance();
                            updates.schedule(account);
                            
                            AddAccountUI.this.dispose();
                        }else{
                            errorlbl.setText("Invlaid Login details");
                        }
                        
                        return null;
                    }

                    @Override
                    protected void done() {
                        busywin.dispose();
                    }
                    
                };
                
                accountWorker.execute();
            }
        }
        
        public boolean isBlank(String ...text){
            boolean isblank = false;
            for(String s:text){
                if(s.isEmpty()){
                    isblank=true;
                    break;
                }
            }
            
            return isblank;
        }
        
        
    }
    public static void main(String args[]){
        new AddAccountUI(null);
    }
}
