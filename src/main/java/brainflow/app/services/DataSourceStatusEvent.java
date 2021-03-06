package brainflow.app.services;

import brainflow.image.io.IImageSource;
import org.bushe.swing.event.AbstractEventServiceEvent;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 10, 2004
 * Time: 1:33:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceStatusEvent extends AbstractEventServiceEvent {


    public enum EventID {
        IMAGE_REGISTERED, IMAGE_REMOVED, IMAGE_LOADED, IMAGE_UNLOADED
    }


    private EventID id;

    private IImageSource sourceImage;

    public DataSourceStatusEvent(IImageSource source, EventID _id) {
        super(source);
        sourceImage = source;
        id = _id;
    }

    public EventID getEventID() {
        return id;
    }

    public IImageSource getLoadableImage() {
        return sourceImage;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("+++ ");
        builder.append(sourceImage.getDataFile().getName().getPath());
        builder.append(" : ");
        builder.append(id);
        return builder.toString();
    }


}
