/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clautorenew;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Hermoine
 */
public class AdListRenderer implements ListCellRenderer<Ad>{
    protected static Border noFocusBorder = new EmptyBorder(15, 1, 1, 1);

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    @Override
    public Component getListCellRendererComponent(JList<? extends Ad> list, final Ad value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
        isSelected, cellHasFocus);
        StringBuffer sb = new StringBuffer();
        sb.append("<html><div><b>")
                .append(value.getTitle())
                .append("</b><br/>")
                .append("<small>")
                .append(value.getStatus())
                .append("</small>")
                .append("</div><hr></html>");
        renderer.setText(sb.toString());

        return renderer;
    }
}
