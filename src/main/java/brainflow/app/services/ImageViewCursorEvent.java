package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.image.anatomy.AnatomicalPoint3D;

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


    private AnatomicalPoint3D cpos;

    public ImageViewCursorEvent(ImageView view) {
        super(view);
        cpos = view.worldCursorPos.get();
    }

    public AnatomicalPoint3D getCursor() {
        return cpos;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Crosshair at : ");


        builder.append("zero: " + (int) cpos.getX());
        builder.append(" zero: " + (int) cpos.getY());
        builder.append(" one: " + (int) cpos.getZ());

        return builder.toString();
    }


}
