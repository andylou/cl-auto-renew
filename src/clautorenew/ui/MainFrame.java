
package clautorenew.ui;


import clautorenew.conf.Account;
import clautorenew.ad.Ad;
import clautorenew.ad.AdsStore;
import clautorenew.conf.Configuration;
import clautorenew.conf.LoginProcessor;
import clautorenew.ad.MonitorAds;
import clautorenew.ui.StatusWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import net.miginfocom.swing.MigLayout;
import java.util.Timer;
import javax.swing.border.TitledBorder;
/**
 *
 * @author Hermoine
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel c_panel;
    private DefaultListModel<Ad> adModel;
    private AdsStore store;
    private JList listview;
    private JButton renewbtn;
    private JButton deletebtn;
    private JLabel errlbl;
    private JMenuItem logoutmenu;
    private JMenuItem renewallmenu;
    private JMenuItem autorenewmenu;
    private boolean didRenew;
    private boolean didDelete;
    private boolean didRenewall;
    public MainFrame(){
        initUI();
    }
    public void initUI(){
        setJMenuBar(createMenuBar());
        add(createBannerPanel(),"North");
        c_panel = new JPanel();
        c_panel.setLayout(new BorderLayout());
        
        c_panel.add(showListings());
        
        //add(c_panel, BorderLayout.CENTER);
        
        
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createSidePane(), c_panel);
        add(splitpane);
        setTitle("CraigsList Auto Renew");
        setSize(520,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    protected JPanel createSidePane(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new TitledBorder("Accounts"));
        
        JList<Account> accountlist = new JList<>();
        
        Configuration conf= Configuration.getInstance();
        
        //ArrayList<Account> accounts = conf.getAccounts();
        
        //accountlist.setListData((Account[]) accounts.toArray());
        
        JPanel s_panel = new JPanel();
        s_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton addbtn = new JButton(" + ");
        addbtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                
            }
        });
        JButton rembtn = new JButton(" - ");
        
        s_panel.add(addbtn);
        s_panel.add(rembtn);
        
        panel.add(new JScrollPane(accountlist));
        panel.add(s_panel, BorderLayout.SOUTH);
        
        return panel;
        
    }
    public JPanel showListings(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        //store = AdsStore.getInstance();
//        adModel = store.getAdModel();
        
        
        listview = new JList();
        
        
        listview.setSelectionBackground(Color.red);
        listview.setFixedCellHeight(30);
        listview.setCellRenderer(new AdListRenderer());
        listview.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                Ad ad = adModel.get(listview.getSelectedIndex());
                if(ad.getStatus().equalsIgnoreCase("inactive")){
                    renewbtn.setEnabled(true);
                }else{
                    renewbtn.setEnabled(false);
                }
                                
            }
        });
        
        listview.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    try {
                        openAd((Ad)listview.getSelectedValue());
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });
        
        
        panel.add(new JScrollPane(listview));
        
        return panel;
    }
    public void openAd(Ad ad) throws URISyntaxException, IOException{
        
        String url = store.getPublicUrl(ad.getUrl());
       
        
        Desktop.getDesktop().browse(new URL(url).toURI());
        
    }
    public JMenuBar createMenuBar(){
        JMenuBar mbar = new JMenuBar();
        
        JMenu actionmenu = new JMenu("Actions");
        renewallmenu = new JMenuItem("Renew All");
        renewallmenu.setEnabled(false);
        
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
                AutoRenewUI autoRenewUI = AutoRenewUI.getInstance(MainFrame.this);
                autoRenewUI.setVisible(true);
            }
        });
        autorenewmenu.setEnabled(false);
        
        logoutmenu = new JMenuItem("Logout");
        logoutmenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                logout();
            }
        });
        logoutmenu.setEnabled(false);
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
        
        actionmenu.add(logoutmenu);
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
                doRenew((Ad)listview.getSelectedValue());
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
                    doDelete((Ad)listview.getSelectedValue());
                }else{
                    JOptionPane.showMessageDialog(MainFrame.this, "You select an ad first");
                }
            }

        });
        
        
        opanel.add(renewbtn);
        opanel.add(renewallbtn);
        opanel.add(deletebtn);
        
        
        
        return opanel;
    }
    
    public JPanel showLogin(){
        JPanel panel = new JPanel();
        
        panel.setLayout(new MigLayout("wrap 2"));
        
        JLabel emaillabel = new JLabel("Email");
        final JTextField emailfield = new JTextField(20);
        
        JLabel passlabel = new JLabel("Password");
        final JPasswordField passfield = new JPasswordField(20);
        errlbl = new JLabel("");
        errlbl.setForeground(Color.red);
        
        JButton loginbtn = new JButton("Login");
        loginbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction(emailfield.getText(),new String(passfield.getPassword()));
            }
        
        });
        emailfield.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction(emailfield.getText(),new String(passfield.getPassword()));
            }
        
        });
        passfield.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction(emailfield.getText(),new String(passfield.getPassword()));
            }
        
        });
        //loginbtn.setPreferredSize(new Dimension(300,70));
        panel.add(emaillabel);
        panel.add(emailfield);
        panel.add(passlabel);
        panel.add(passfield);
        panel.add(errlbl, "span 2");
        panel.add(new JLabel(""),"wrap");
        panel.add(new JLabel(""),"wrap");
        panel.add(new JLabel(""),"wrap");
        panel.add(new JLabel(""),"wrap");
        panel.add(loginbtn, "span 2");
        
        return panel;
    }
    private void loginAction(final String email, final String password){
        SwingWorker<Object,Void> loginworker = new SwingWorker(){
            StatusWindow busywindow = new StatusWindow(MainFrame.this);
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    busywindow.setStatus("please wait...");
                    if(LoginProcessor.doLogin(email, password)){


                        c_panel.removeAll();
                        c_panel.invalidate();
                        renewallmenu.setEnabled(true);
                        autorenewmenu.setEnabled(true);
                        logoutmenu.setEnabled(true);
                        c_panel.add(showListings());
                        c_panel.add(createButtonPanel(),"South");
                        
                        c_panel.validate();


                        MainFrame.this.repaint();
                        
                        //start checking for updates
                        Timer checkForUpdates = new Timer(true);
                        checkForUpdates.schedule(new MonitorAds(),300, 30*1000);
                        
                        //start automatic renewal if enabled
                        /*AutoRenewUI auto_renewUI = AutoRenewUI.getInstance(MainFrame.this);
                        Properties props = new Properties();
                        props.load(new FileReader(new File("conf.properties")));
                        AutoRenewUI.enableAction(props.getProperty("autorenew").equals("true")?true:false,
                                Integer.parseInt(props.getProperty("interval")));*/
                        
                    }else{
                        errlbl.setText("Invalid Username or password");
                    }
                    busywindow.dispose();
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return null;
            }

            @Override
            protected void done() {
               busywindow.dispose();
               
            }
            
            
        };
        MainFrame.this.repaint(10);
        loginworker.execute();
    }
    public void logout(){
        c_panel.removeAll();
        renewallmenu.setEnabled(false);
        autorenewmenu.setEnabled(false);
        logoutmenu.setEnabled(false);
        //AdsStore.reset();
        c_panel.invalidate();
        c_panel.add(showLogin());
        c_panel.validate();
        MainFrame.this.repaint(10);
    }
    
    private void doDelete(Ad ad) {
        int reply = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to delete this ad", "Delete Ad - "+ad.getTitle(), JOptionPane.YES_NO_OPTION);
        if(reply == JOptionPane.YES_OPTION){
            SwingWorker worker = new AdManager(ad, "delete");
            worker.execute();
        }
    }
    
    public void doRenewAll(){
        SwingWorker worker = new AdManager();
        worker.execute();
    }
    
    public void doRenew(Ad ad){
        SwingWorker worker = new AdManager(ad, "renew");
        worker.execute();
    }
    
    class ShadowPane extends JComponent{
        private static final long serialVersionUID = 1L;
        
    }
    
    class AdManager extends SwingWorker<Object, Void>{
            
            private Ad ad;
            private String action;
            private StatusWindow busywindow = new StatusWindow(MainFrame.this);
            public AdManager(){
                this(null,null);
            }
            public AdManager(Ad ad, String action){
                this.ad = ad;
                this.action = action;
                didDelete = false;
                didRenew = false;
                didRenewall = false;
            }
            @Override
            protected Object doInBackground() {
                busywindow.setStatus("Please wait...");
                try {
                        
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
                        if(!store.hasRenewable())
                            JOptionPane.showMessageDialog(MainFrame.this, "No ads exists to renew.");
                        else{
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
    
    public static void main(String args[]){
        MainFrame mainFrame = new MainFrame();
    }
}
