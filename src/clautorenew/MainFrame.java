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
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class MainFrame extends JFrame {
    private JPanel c_panel;
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
        
        JList listview = new JList(new String[]{"Listing 1","Listing 2","Listing 3"});
        listview.setSelectionBackground(Color.red);
        listview.setFixedCellHeight(30);
        
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
        JButton deletebtn = new JButton("Delete");
        
        opanel.add(repostbtn);
        opanel.add(renewbtn);
        opanel.add(deletebtn);
        
        
        return opanel;
    }
    
    public JPanel showLogin(){
        JPanel panel = new JPanel();
        
        panel.setLayout(new MigLayout("wrap 2"));
        
        JLabel emaillabel = new JLabel("Email");
        JTextField emailfield = new JTextField(20);
            
        JLabel passlabel = new JLabel("Password");
        JPasswordField passfield = new JPasswordField(20);
        
        JLabel errlbl = new JLabel("Error");
        errlbl.setForeground(Color.red);
        
        JButton loginbtn = new JButton("Login");
        loginbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                c_panel.removeAll();
                c_panel.validate();
                
                c_panel.add(showListings());
                c_panel.add(createButtonPanel(),"South");
                
                c_panel.validate();
                
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
    public JPanel showButtonPanel(){
        JPanel panel = new JPanel();
        //panel.set
        return panel;
    }
    
    
    public static void main(String args[]){
        new MainFrame();
    }
}
