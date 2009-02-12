package brainflow.core;

import brainflow.image.space.IImageSpace;
import brainflow.core.IImageDisplayModel;

import javax.swing.event.ChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 17, 2007
 * Time: 9:44:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpaceChangedEvent extends ChangeEvent {

    private IImageDisplayModel displayModel;

    public ImageSpaceChangedEvent(IImageDisplayModel dmodel) {
        super(dmodel);
        displayModel = dmodel;
    }

    public IImageSpace getImageSpace() {
        return displayModel.getImageSpace();
    }


    public IImageDisplayModel getSource() {
        return displayModel;
    }
}
