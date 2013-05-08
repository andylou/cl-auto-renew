
package clautorenew;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel c_panel;
    DefaultListModel<Ad> adModel = new DefaultListModel();
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
        
        c_panel.add(showLogin());
        
        add(c_panel, BorderLayout.CENTER);
        
       
        setTitle("CraigsList Auto Renew");
        setSize(320,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public JPanel showListings(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        store = AdsStore.getInstance();
        
        for(Ad ad: store.getListings()){
            adModel.addElement(ad);
        }
        
        listview = new JList(adModel);
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
                    JOptionPane.showMessageDialog(null, listview.getSelectedValue());
                }
            }

        });
        
        
        panel.add(new JScrollPane(listview));
        
        return panel;
    }
    public void openAd(Ad ad) throws IOException{
        Runtime r = Runtime.getRuntime();
        r.exec(ad.getUrl());
        
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
        AdsStore.reset();
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
                            
                            store.delete(ad, adModel);
                            didDelete=true;
                        }
                        if(action.equals("renew")){
                            if(!ad.getStatus().equalsIgnoreCase("inactive"))
                                JOptionPane.showMessageDialog(MainFrame.this, "This ad is currently active");
                            else{
                                store.renew(ad, adModel);
                                didRenew = true;
                            }
                            
                        }

                    }else{
                        if(!store.hasRenewable())
                            JOptionPane.showMessageDialog(MainFrame.this, "No ads exists to renew.");
                        else{
                            store.renewAll(adModel);
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
