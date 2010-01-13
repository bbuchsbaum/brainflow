package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.image.anatomy.VoxelLoc3D;

import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 23, 2006
 * Time: 12:40:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewMousePointerEvent extends ImageViewMouseEvent {

    private VoxelLoc3D ap;

    public ImageViewMousePointerEvent(ImageView view, MouseEvent _event) {
        super(view, _event);

    }

    public VoxelLoc3D getLocation() {

        if (ap == null) {
            MouseEvent event = getEvent();

            VoxelLoc3D cursorPos = getImageView().getCursorPos();
            if (!getImageView().pointInPlot(event.getComponent(), event.getPoint())) {
                ap = new VoxelLoc3D(0,0,0, cursorPos.getSpace());
            } else {
                ap = getImageView().getAnatomicalLocation(event.getComponent(), event.getPoint());
            }

        }

        return ap;

    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Cursor at : ");
        VoxelLoc3D tmp = getLocation();
        if (tmp == null) return "cursorPos : --";

        builder.append("zero: " + (int) getLocation().getX().getValue());
        builder.append("one: " + (int) getLocation().getY().getValue());
        builder.append("two: " + (int) getLocation().getZ().getValue());

        return builder.toString();
    }
}
