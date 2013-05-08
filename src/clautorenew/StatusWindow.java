
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
import javax.swing.JWindow;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Hermoine
 */
public class StatusWindow extends JWindow {
    private static final long serialVersionUID = 1L;
    private JProgressBar progressUI;
    private JLabel statusLabel;
    private StatusWindow inst;
    public StatusWindow(JFrame parent){
        JPanel panel = new JPanel(new MigLayout("wrap 1"));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.setOpaque(false);
        progressUI = new JProgressBar();
        progressUI.setPreferredSize(new Dimension(300, 20));
        progressUI.setIndeterminate(true);
        
        statusLabel = new JLabel("status");
        
        panel.add(progressUI);
        panel.add(statusLabel);
        
        add(panel);
        
        setSize(320,70);
        setLocationRelativeTo(parent);
        
        //setAlwaysOnTop(true);
        setVisible(true);
        
    }
    public String getStatus(){
        return statusLabel.getText();
    }
    
    public void setStatus(String status){
        statusLabel.setText(status);
    }
    
    public static void main(String[] args) {
        new StatusWindow(null);
    }
    
}
