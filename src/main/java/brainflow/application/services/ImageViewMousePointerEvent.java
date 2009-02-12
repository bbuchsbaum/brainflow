package brainflow.application.services;

import brainflow.core.ImageView;
import brainflow.image.anatomy.AnatomicalPoint;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;

import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 23, 2006
 * Time: 12:40:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewMousePointerEvent extends ImageViewMouseEvent {

    private AnatomicalPoint3D ap;

    public ImageViewMousePointerEvent(ImageView view, MouseEvent _event) {
        super(view, _event);

    }

    public AnatomicalPoint3D getLocation() {

        if (ap == null) {
            MouseEvent event = getEvent();
            AnatomicalPoint3D cursorPos = getImageView().getCursorPos();
            AnatomicalPoint3D tmp = getImageView().getAnatomicalLocation(event.getComponent(), event.getPoint());
            ap = tmp.convertTo(cursorPos.getSpace());


            IImageSpace3D space = (IImageSpace3D)getImageView().getModel().getImageSpace();
            float[] gridpos = space.worldToGrid((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
            double x1 = space.getImageAxis(Axis.X_AXIS).gridToReal(gridpos[0]);
            double y1 = space.getImageAxis(Axis.Y_AXIS).gridToReal(gridpos[1]);
            double z1 = space.getImageAxis(Axis.Z_AXIS).gridToReal(gridpos[2]);

            ap = new AnatomicalPoint3D(space, x1, y1, z1);



        }

        return ap;

    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Cursor at : ");
        AnatomicalPoint tmp = getLocation();
        if (tmp == null) return "cursorPos : --";

        builder.append("zero: " + (int) getLocation().getX());
        builder.append("one: " + (int) getLocation().getY());
        builder.append("two: " + (int) getLocation().getZ());

        return builder.toString();
    }
}
