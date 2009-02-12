package brainflow.application.toplevel;

import brainflow.gui.AbstractPresenter;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.*;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 2:38:35 PM
 */
public class EventBusMonitor extends AbstractPresenter implements EventSubscriber {

    private static final int MAX_LINES = 1000;

    private JTextArea area = new JTextArea();


    public EventBusMonitor() {
        EventBus.subscribeStrongly(EventServiceEvent.class, this);
        //area.getDocument().
        //area.setRows(MAX_LINES);
        area.setColumns(110);
        // area.setPreferredSize(new Dimension(1000, 150));

    }

    public JComponent getComponent() {
        return area;
    }


    public void onEvent(Object event) {

        area.append(event.toString());
        area.append("\n");

        if (area.getDocument().getLength() > MAX_LINES) {
            try {
                area.getDocument().remove(0, 500);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        area.setCaretPosition(area.getDocument().getLength() - 1);
    }


}
