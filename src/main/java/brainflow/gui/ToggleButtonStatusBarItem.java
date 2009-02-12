package brainflow.gui;

import com.jidesoft.status.StatusBarItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 23, 2004
 * Time: 3:22:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleButtonStatusBarItem extends StatusBarItem {

    private JToggleButton toggleButton;
    private String name;

    public ToggleButtonStatusBarItem(String _name, boolean selected) {
        name = _name;
        toggleButton = new JToggleButton(name, selected);
        setLayout(new BorderLayout());
        add(toggleButton, BorderLayout.CENTER);
        

    }

    public ToggleButtonStatusBarItem(Action a) {
        name = (String)a.getValue(Action.NAME);
        toggleButton = new JToggleButton(a);
        setLayout(new BorderLayout());
        add(toggleButton, BorderLayout.CENTER);
    }


    public String getItemName() {
        return name;
    }

    public void setIcon(Icon icon) {
        toggleButton.setIcon(icon);
    }

    public int getPreferredWidth() {
        return 16;
    }
}
