package brainflow.app.services;

import brainflow.core.ImageView;

import javax.swing.event.ListDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 30, 2009
 * Time: 9:17:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewListDataEvent extends ImageViewEvent {

    private ListDataEvent event;

    public ImageViewListDataEvent(ImageView view, ListDataEvent event) {
        super(view);
        this.event = event;
    }

    public ListDataEvent getListDataEvent() {
        return event;
    }
}
