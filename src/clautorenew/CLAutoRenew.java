
package clautorenew;

import clautorenew.ui.MainFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;



/**
 *
 * @author Hermoine
 */
final public class CLAutoRenew {

    /**22
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run(){
                        try{
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                        }catch(Exception ex){
                            try {
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            } catch (Exception ex1) {
                                Logger.getLogger(CLAutoRenew.class.getName()).log(Level.SEVERE, null, ex1);
                            } 
                        }
                        MainFrame.getInstance();
                        
                    }
                });
        
        
        
        
    }
}
