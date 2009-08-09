package brainflow.app.services;

import org.bushe.swing.event.AbstractEventServiceEvent;

import javax.swing.event.ListDataEvent;

import brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 2, 2009
 * Time: 9:12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainCanvasListDataEvent extends AbstractEventServiceEvent {

    private IBrainCanvas source;

    private ListDataEvent listEvent;

    public BrainCanvasListDataEvent(IBrainCanvas source, ListDataEvent listEvent) {
        super(source);
        this.source = source;
        this.listEvent = listEvent;
    }

    @Override
    public IBrainCanvas getSource() {
        return source;
    }

    public ListDataEvent getListEvent() {
        return listEvent;
    }
}
