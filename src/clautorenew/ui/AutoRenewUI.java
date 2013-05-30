/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.ui;

import clautorenew.conf.AutoRenew;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class AutoRenewUI extends JDialog {
    private static final long serialVersionUID = 1L;
    private static JComboBox<Integer> frequencyBox;
    private static JCheckBox enableRenew;
    private static Timer renewTimer = null;
    private static Properties props = new Properties();
    private static File propfile = new File("conf.properties");
    private static AutoRenewUI autoRenewUI;
    private AutoRenewUI(JFrame parent){
        
        setLayout(new MigLayout("wrap 3"));
        try {
            props.load(new FileReader(propfile));
        } catch (IOException ex) {
            Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        enableRenew = new JCheckBox("Enable Auto Renew");
        //load state from property file
        enableRenew.setSelected(props.getProperty("autorenew").equals("true")?true:false);
        enableRenew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                enableAction(enableRenew.isSelected(),
                        Integer.parseInt(props.getProperty("interval")));
            }
           
        });
        add(enableRenew,"span 3");
        
        frequencyBox = new JComboBox<>(new Integer[]{1,2,3,4,5});
        frequencyBox.setSelectedItem(new Integer(Integer.parseInt(props.getProperty("interval"))));
        add(new JLabel("Frequency"));
        add(frequencyBox);
        add(new JLabel("hour(s)"));
        
        setSize(300,150);
        setLocationRelativeTo(parent);
        setVisible(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    public static AutoRenewUI getInstance(JFrame parent){
        if(autoRenewUI==null){
            autoRenewUI = new AutoRenewUI(parent);
        }
        return autoRenewUI;
    }
    public static void main(String args[]){
        //AutoRenewUI.display(null);
    }
    public static void enableAction(boolean selected, int interval){
        if(selected){
            if(renewTimer!=null){
                System.out.println("Cancelled current running...");
                renewTimer.cancel();
            }
            renewTimer = new Timer();
            System.out.println("Scheduling new one ...");
            renewTimer.schedule(new AutoRenew(), 3000, 1000);
            props.setProperty("autorenew", "true");
            props.setProperty("interval", ""+interval);
            
        }else{
            if(renewTimer!=null){
                renewTimer.cancel();
                System.out.println("Cancelled current running");
            }
            renewTimer = null;
            props.setProperty("autorenew", "false");
            frequencyBox.setEnabled(false);
        }
        //save state
        try {
            props.store(new FileWriter(propfile), null);
        } catch (IOException ex) {
            Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
