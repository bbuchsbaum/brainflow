package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.core.ViewBounds;
import brainflow.core.IImagePlot;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 1, 2009
 * Time: 5:12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewBoundsChangedEvent extends ImageViewEvent {

    private final ViewBounds oldViewBounds;

    private final ViewBounds viewBounds;

    private final IImagePlot plot;


    public ViewBoundsChangedEvent(ImageView view, IImagePlot plot, ViewBounds oldBounds,ViewBounds newBounds) {
        super(view);
        this.oldViewBounds = oldBounds;
        this.plot = plot;
        this.viewBounds = newBounds;
    }

    public ViewBounds getOldViewBounds() {
        return oldViewBounds;
    }

    public IImagePlot getPlot() {
        return plot;
    }

    public ViewBounds getViewBounds() {
        return viewBounds;
    }
}
