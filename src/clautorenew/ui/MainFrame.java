
package clautorenew.ui;


import clautorenew.ad.AccountUpdate;
import clautorenew.conf.Account;
import clautorenew.ad.Ad;
import clautorenew.ad.AdsStore;
import clautorenew.conf.Configuration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.border.TitledBorder;
/**
 *
 * @author Hermoine
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel c_panel;
    private JList<Ad> listview;
    private JButton renewbtn;
    private JButton deletebtn;
    private JLabel errlbl;
    private JMenuItem renewallmenu;
    private JMenuItem autorenewmenu;
    private boolean didRenew;
    private boolean didDelete;
    private boolean didRenewall;
    private JList<Account> accountlist;
    private static JLabel statuslbl;
    protected DefaultListModel<Account> accountmodel;
    private static MainFrame mf;
    
    private MainFrame(){
        initUI();
    }
    public static MainFrame getInstance(){
        if(mf == null){
            mf = new MainFrame();
        }
        
        return mf;
    }
    public void initUI(){
        accountmodel = new DefaultListModel<>();
        setJMenuBar(createMenuBar());
        add(createBannerPanel(),"North");
        c_panel = new JPanel();
        c_panel.setLayout(new BorderLayout());
        
        c_panel.add(showListings());
        
        //load configurations from file
        final Configuration config = Configuration.getInstance();
        if(config.getAccounts()!=null){
            accountmodel = config.getAccounts();
        }
        
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createSidePane(), c_panel);
        add(splitpane);
        splitpane.setDividerLocation(250);
        splitpane.setDividerSize(5);
        
        add(statusPanel(),BorderLayout.SOUTH);
        setTitle("CraigsList Auto Renew");
        setSize(620,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    config.save();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        AccountUpdate updates = AccountUpdate.getInstance();
        updates.startAccountUpdate();
        try {
            config.loadInterval();
            if(config.isAutorenew()){
                updates.beginAutoRenew(config.getTime(), config.getTimeunit());
            }
        }catch(EOFException e){
            //do nothing for end of file exceptions
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public JPanel statusPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statuslbl = new JLabel("Status");
        panel.add(statuslbl);
        
        return panel;
    }
    
    public static void setStatus(String msg){
        statuslbl.setText(msg);
    }
    protected JPanel createSidePane(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new TitledBorder("Accounts"));
        accountlist = new JList<>();
        accountlist.setCellRenderer(new AccountListRenderer());
        accountlist.setModel(accountmodel);
        accountlist.setFixedCellHeight(40);
        if(!accountmodel.isEmpty()){
            accountlist.setSelectedIndex(0);
            
            listview.setModel(accountlist.getSelectedValue().getStore().getAdModel());
            
        }
        accountlist.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    Account account = accountlist.getSelectedValue();
                    listview.setModel(account.getStore().getAdModel());
                }
            }
        });
        
        JPanel s_panel = new JPanel();
        s_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton addbtn = new JButton("+");
        addbtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                new AddAccountUI(MainFrame.this);
            }
        });
        JButton rembtn = new JButton("-");
        //remove accoount btn action
        rembtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent a){
                //confirm remove
                Account acc = accountlist.getSelectedValue();
                int reply = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to delete Account: "+
                        acc.getEmail()+"?", "Delete Account", JOptionPane.YES_NO_OPTION);
                
                if(reply == JOptionPane.YES_OPTION){
                    //remove account from configuration
                    Configuration config = Configuration.getInstance();
                    config.deleteAccount(acc);
                    
                    //cancel auto update for account
                    AccountUpdate updates = AccountUpdate.getInstance();
                    updates.remove(acc);
                    
                    //remove account from UI
                    listview.setModel(new DefaultListModel<Ad>());
                    //accountmodel.removeElement(acc); //no need for this because the the ui is using the same model object with configuration
                }
                
                
            }
        });
        s_panel.add(addbtn);
        s_panel.add(rembtn);
        
        panel.add(new JScrollPane(accountlist));
        panel.add(s_panel, BorderLayout.SOUTH);
        
        return panel;
        
    }
    //used bu AddAccountUI to display new accounts on the UI
    public void addAccount(Account account){
        //accountmodel.addElement(account);//no need for this because the the ui is using the same model object with configuration
        accountlist.clearSelection();
        accountlist.setSelectedValue(account, true);
        listview.setModel(account.getStore().getAdModel());
    }
    
    //the listings ui
    public JPanel showListings(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
         
        listview = new JList();  
        
        listview.setSelectionBackground(Color.red);
        listview.setFixedCellHeight(30);
        listview.setCellRenderer(new AdListRenderer());
        listview.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    Account account = accountlist.getSelectedValue();
                    Ad ad = account.getStore().getAdModel().get(listview.getSelectedIndex());
                    if(ad.getStatus().equalsIgnoreCase("inactive")){
                        renewbtn.setEnabled(true);
                    }else{
                        renewbtn.setEnabled(false);
                    }
                }
                
                                
            }
        });
        
        listview.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    try {
                        openAd(listview.getSelectedValue());
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });
        
        
        panel.add(new JScrollPane(listview));
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        return panel;
    }
    
    //displays an ad in the default system browser when clicked
    public void openAd(Ad ad) throws URISyntaxException, IOException{
        Account account = accountlist.getSelectedValue();
        String url = account.getStore().getPublicUrl(ad.getUrl());
       
        
        Desktop.getDesktop().browse(new URL(url).toURI());
        
    }
    public JMenuBar createMenuBar(){
        JMenuBar mbar = new JMenuBar();
        
        JMenu actionmenu = new JMenu("Actions");
        renewallmenu = new JMenuItem("Renew All");
        //renewallmenu.setEnabled(false);
        
        renewallmenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                doRenewAll();
            }
        });
        autorenewmenu = new JMenuItem("Setup Automatic Renewal");
        autorenewmenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
               new AutoRenewUI(MainFrame.this);
            }
        });
        //autorenewmenu.setEnabled(false);
        
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                System.exit(0);
            }
        });
        
        actionmenu.add(renewallmenu);
        actionmenu.add(autorenewmenu);
        
        actionmenu.addSeparator();
        
        actionmenu.add(exit);
        
        mbar.add(actionmenu);
        
        return mbar;
    }
    
    public JPanel createBannerPanel(){
        JPanel panel = new JPanel();
        
        JLabel label = new JLabel("<html><div style='padding:5px;'><h1>CL Auto Renew"
                + "</h1>"
                + ""
                + "</div><html>");
        
        panel.add(label);
        panel.setPreferredSize(new Dimension(200,100));
        panel.setBackground(Color.white);
        return panel;
        
    }
    public JPanel createButtonPanel(){
        JPanel opanel = new JPanel();
        
        opanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        renewbtn = new JButton("Renew");
        renewbtn.setEnabled(false);
        renewbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                doRenew(listview.getSelectedValue());
            }

        });
        
        JButton renewallbtn = new JButton("Renew All");
        renewallbtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doRenewAll();
            }
        });
        deletebtn = new JButton("Delete");
        deletebtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(listview.getSelectedValue()!=null){
                    doDelete(listview.getSelectedValue());
                }else{
                    JOptionPane.showMessageDialog(MainFrame.this, "You have to select an ad first");
                }
            }

        });
        
        
        opanel.add(renewbtn);
        opanel.add(renewallbtn);
        opanel.add(deletebtn);
        
        
        
        return opanel;
    }
    
    private void doDelete(Ad ad) {
        int reply = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to delete this ad", "Delete Ad - "+ad.getTitle(), JOptionPane.YES_NO_OPTION);
        if(reply == JOptionPane.YES_OPTION){
            SwingWorker<Object, Void> worker = new AdManager(ad, "delete", accountlist.getSelectedValue());
            worker.execute();
        }
    }
    
    public void doRenewAll(){
        SwingWorker<Object, Void> worker = new AdManager(accountlist.getSelectedValue());
        worker.execute();
    }
    
    public void doRenew(Ad ad){
        SwingWorker<Object, Void> worker = new AdManager(ad, "renew", accountlist.getSelectedValue());
        worker.execute();
    }
    
    class ShadowPane extends JComponent{
        private static final long serialVersionUID = 1L;
        
    }
    
    class AdManager extends SwingWorker<Object, Void>{
            
            private Ad ad;
            private String action;
            private StatusWindow busywindow = new StatusWindow(MainFrame.this);
            private Account account;
            public AdManager(Account account){
                this(null,null,account);
            }
            public AdManager(Ad ad, String action, Account account){
                this.ad = ad;
                this.action = action;
                this.account = account;
                didDelete = false;
                didRenew = false;
                didRenewall = false;
            }
            @Override
            protected Object doInBackground() {
                busywindow.setStatus("Please wait...");
                try {
                    AdsStore store = account.getStore();    
                    if(ad != null){
                        
                        if(action.equals("delete")){
                            
                            store.delete(ad);
                            didDelete=true;
                        }
                        if(action.equals("renew")){
                            if(!ad.getStatus().equalsIgnoreCase("inactive"))
                                JOptionPane.showMessageDialog(MainFrame.this, "This ad is currently active");
                            else{
                                store.renew(ad);
                                didRenew = true;
                            }
                            
                        }

                    }else{
                        if(!store.hasRenewable()){
                            busywindow.dispose();
                            JOptionPane.showMessageDialog(MainFrame.this, "No ads exists to renew.");
                        }else{
                            store.renewAll();
                            didRenewall = true;
                        }
                        
                    }

                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                    
                
                return null;
            }

            @Override
            protected void done() {
                busywindow.setStatus("Done");
                busywindow.dispose();
                
                if(didRenew==true){
                    //display message "Ad renewed"
                    JOptionPane.showMessageDialog(MainFrame.this, "Done renewing ad");
                }
                
                if(didDelete==true){
                    JOptionPane.showMessageDialog(MainFrame.this, "Ad deleted");
                }
                
                if(didRenewall==true){
                    
                    JOptionPane.showMessageDialog(MainFrame.this, "All ads have now been renewed");
                }
                
            }
            
        }
    public void refresh(){
        this.repaint();
        this.revalidate();
    }
    public static void main(String args[]){
        MainFrame mainFrame = new MainFrame();
    }
}
