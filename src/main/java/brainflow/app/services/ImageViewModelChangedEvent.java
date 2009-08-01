package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 9, 2009
 * Time: 6:12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewModelChangedEvent extends ImageViewEvent {

    private ImageViewModel oldModel;

    private ImageViewModel newModel;


    public ImageViewModelChangedEvent(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        super(view);
        this.oldModel = oldModel;
        this.newModel = newModel;
    }

    public ImageViewModel getOldModel() {
        return oldModel;
    }

    public ImageViewModel getNewModel() {
        return newModel;
    }
}
