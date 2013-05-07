/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class StatusWindow extends JDialog {
    private JProgressBar progressUI;
    private JLabel statusLabel;
    
    public StatusWindow(JFrame parent){
        JPanel panel = new JPanel(new MigLayout("wrap 1"));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.setOpaque(false);
        progressUI = new JProgressBar();
        progressUI.setPreferredSize(new Dimension(300, 20));
        progressUI.setIndeterminate(true);
        //progressUI.setString("Downloading...");
        //progressUI.setStringPainted(true);
        statusLabel = new JLabel("status");
        
        
        panel.add(progressUI);
        panel.add(statusLabel);
        
        
        add(panel);
        setUndecorated(true);
        setSize(320,70);
        setLocationRelativeTo(parent);
        setModal(true);
        //setAlwaysOnTop(true);
        
        setVisible(true);
        setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
    }
    public String getStatus(){
        return statusLabel.getText();
    }
    
    public void setStatus(String status){
        statusLabel.setText(status);
    }
    
}
