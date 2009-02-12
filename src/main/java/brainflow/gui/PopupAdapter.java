package brainflow.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 30, 2007
 * Time: 7:35:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PopupAdapter extends MouseAdapter {

    private JPopupMenu menu;
    private JComponent invoker;

    public PopupAdapter(JComponent invoker, JPopupMenu menu) {
        this.invoker = invoker;
        this.menu = menu;

        invoker.addMouseListener(this);
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopup(e.getPoint());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopup(e.getPoint());
        }
    }

    private void showPopup(Point p) {
        menu.show(invoker, p.x, p.y);
    }

}
