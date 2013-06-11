
package clautorenew.ui;

import clautorenew.conf.Account;
import java.awt.Component;
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
public class AccountListRenderer implements ListCellRenderer<Account>{
    protected static Border noFocusBorder = new EmptyBorder(15, 1, 1, 1);

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    @Override
    public Component getListCellRendererComponent(JList<? extends Account> list, final Account value, int index,
            boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
            isSelected, cellHasFocus);
        StringBuffer sb = new StringBuffer();
        sb.append("<html><div><b>")
                .append(value.getAccountName())
                .append("</b><br/>")
                .append("<small>")
                .append(value.getEmail())
                .append("</small>")
                .append("</div></html>");
        renderer.setText(sb.toString());

        return renderer;
    }
}
