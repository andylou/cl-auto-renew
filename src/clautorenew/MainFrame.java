/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class MainFrame extends JFrame {
    private JPanel c_panel;
    DefaultListModel<Ad> adModel = new DefaultListModel();
    private AdsStore store;
    public MainFrame(){
        
        
        
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
        store.setListView(adModel);

        JList listview = new JList(adModel);
        listview.setSelectionBackground(Color.red);
        listview.setFixedCellHeight(30);
        listview.setCellRenderer(new AdListRenderer());
        panel.add(new JScrollPane(listview));
        
        return panel;
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
        
        JButton repostbtn = new JButton("Repost");
        JButton renewbtn = new JButton("Renew");
        JButton renewallbtn = new JButton("Renew All");
        renewallbtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doRenew();
            }
        });
        JButton deletebtn = new JButton("Delete");
        
        opanel.add(repostbtn);
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
        
        final JLabel errlbl = new JLabel("");
        errlbl.setForeground(Color.red);
        
        JButton loginbtn = new JButton("Login");
        loginbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email = emailfield.getText();
                    String password = new String(passfield.getPassword());
                    if(LoginProcessor.doLogin(email, password)){
                        //System.out.println("got here");
                        c_panel.removeAll();
                        c_panel.validate();

                        c_panel.add(showListings());
                        c_panel.add(createButtonPanel(),"South");

                        c_panel.validate();
                        MainFrame.this.repaint();
                    }else{
                        errlbl.setText("Invalid Username or password");
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
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
    
    public void doRenew(){
        SwingWorker worker = new SwingWorker(){
            private boolean didRenew = false;
            @Override
            protected Object doInBackground() {
                if(!store.hasRenewable())
                    JOptionPane.showMessageDialog(MainFrame.this, "No ads exists to renew.");
                else{
                    try {
                        store.renewAll();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    didRenew = true;
                }
                    
                
                return null;
            }

            @Override
            protected void done() {
                if(didRenew){
                    //update the model
                    JOptionPane.showMessageDialog(MainFrame.this, "All ads have now been renewed");
                }
                
            }
            
            
        };
        
        worker.execute();
    }
    class ShadowPane extends JComponent{
        
    }
    
    
    public static void main(String args[]){
        new MainFrame();
    }
}
