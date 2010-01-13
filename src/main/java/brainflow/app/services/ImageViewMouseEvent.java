package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.image.anatomy.VoxelLoc3D;
import org.bushe.swing.event.AbstractEventServiceEvent;

import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 19, 2006
 * Time: 10:47:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewMouseEvent extends AbstractEventServiceEvent {


    private MouseEvent event;

    private ImageView iview;

    public ImageViewMouseEvent(ImageView view, MouseEvent _event) {
        super(_event.getSource());
        event = _event;
        iview = view;
    }

    public MouseEvent getEvent() {
        return event;
    }

    public ImageView getImageView() {
        return iview;
    }

    public VoxelLoc3D getLocation() {
        throw new UnsupportedOperationException();
    }


}
