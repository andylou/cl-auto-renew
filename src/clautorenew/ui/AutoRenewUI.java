/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew.ui;

import clautorenew.ad.AccountUpdate;
import clautorenew.conf.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class AutoRenewUI extends JDialog {
    private static final long serialVersionUID = 11200L;
    public AutoRenewUI(JFrame parent){
        final Configuration config = Configuration.getInstance();
        try {
            config.loadInterval();
        } catch(EOFException e){
            //do nothing for end of file exceptions
        } catch (IOException ex ) {
            Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLayout(new MigLayout("wrap 2"));
        
        add(new JLabel("Select interval to auto renew ads: "),"span 2");
        final JTextField timetxt = new JTextField(15);
        timetxt.setText(""+config.getTime());
        
        final JComboBox<String> timeunitcbox = new JComboBox<>(new String[]{"Seconds","Minutes","Hours"});
        timeunitcbox.setSelectedItem(config.getTimeunit());
        
        final JCheckBox renewbox = new JCheckBox("Enable Auto-renew");
        renewbox.setSelected(config.isAutorenew());
        if(config.isAutorenew()){
            timetxt.setEnabled(true);
            timeunitcbox.setEnabled(true);
        }else{
            timetxt.setEnabled(false);
            timeunitcbox.setEnabled(false);
        }
        renewbox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(renewbox.isSelected()){
                    timetxt.setEnabled(true);
                    timeunitcbox.setEnabled(true);
                }else{
                    timetxt.setEnabled(false);
                    timeunitcbox.setEnabled(false);
                }
            }
        });
        
        renewbox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(config.getAccounts().size()==0){
                    JOptionPane.showMessageDialog(AutoRenewUI.this, "There are no are accounts to auto-renew. please add an account first");
                    renewbox.setSelected(false);
                    timetxt.setEnabled(false);
                    timeunitcbox.setEnabled(false);
                }
            }
        
        });
        
        
        add(renewbox,"wrap");
        add(timetxt);
        add(timeunitcbox);
        
        JButton savebtn = new JButton("Save");
        savebtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                boolean isrenew = renewbox.isSelected();
                if(isrenew){
                    config.setAutorenew(true);
                    config.setTime(Integer.parseInt(timetxt.getText()));
                    config.setTimeunit((String) timeunitcbox.getSelectedItem());
                    try {
                        config.saveInterval();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AccountUpdate updates = AccountUpdate.getInstance();
                    updates.beginAutoRenew(Integer.parseInt(timetxt.getText()), (String) timeunitcbox.getSelectedItem());
                    dispose();
                }else{
                    try {
                        config.setAutorenew(false);
                        config.saveInterval();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AutoRenewUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AccountUpdate updates = AccountUpdate.getInstance();
                    updates.cancelAutorenew();
                    dispose();
                }
            }
        });
        add(savebtn);
        
        
        
        setSize(300,150);
        setLocationRelativeTo(parent);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    
    public static void main(String args[]){
        new AutoRenewUI(null);
    }
}
