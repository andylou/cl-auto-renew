
package clautorenew.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
/**
 *
 * @author Hermoine
 */
public class GlassPaneDemo extends JFrame {
    public GlassPaneDemo(){
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.add(new JLabel("here"));
        add(panel, BorderLayout.NORTH);
        setTitle("CraigsList Auto Renew");
        setSize(320,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.setGlassPane(new MyGlassPane(new Point(this.getContentPane().getX(),this.getContentPane().getY())));
        this.getGlassPane().setVisible(true);
       // StatusWindow statusWindow = new StatusWindow(this);
       // statusWindow.setStatus("Just testint...");
        //set
        //repaint();
    }
    class MyGlassPane extends JComponent{
        Point point;
        public MyGlassPane(Point point){
            this.point = point;
            
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setColor(Color.BLACK);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,8* 0.1f));
            g2d.fillRect(point.x, point.y, GlassPaneDemo.this.getWidth(), GlassPaneDemo.this.getHeight());
            
            
        }
        //paint
        
    }
    public static void main(String args[]){
        new GlassPaneDemo();
    }
}
