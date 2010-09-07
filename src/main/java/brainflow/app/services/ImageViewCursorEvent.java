package brainflow.app.services;

import brainflow.core.ImageView;

import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.anatomy.SpatialLoc3D;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 17, 2006
 * Time: 6:18:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewCursorEvent extends ImageViewEvent {
    /**
     * Creates a new instance of ImageViewEvent
     */


    private GridPoint3D cpos;

    private SpatialLoc3D spos;

    public ImageViewCursorEvent(ImageView view) {
        super(view);
        assert view != null;
        cpos = view.cursorPos.get();
        spos = view.worldCursorPos.get();
    }

    public GridPoint3D getCursor() {
        return cpos;
    }

    public SpatialLoc3D getCursorWorld() {
        return spos;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Crosshair at : ");


        builder.append("x: " + (int) cpos.getX().getValue());
        builder.append("y: " + (int) cpos.getY().getValue());
        builder.append("z: " + (int) cpos.getZ().getValue());

        return builder.toString();
    }


}
