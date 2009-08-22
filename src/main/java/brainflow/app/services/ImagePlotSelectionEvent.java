package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.core.IImagePlot;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 22, 2009
 * Time: 10:57:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePlotSelectionEvent extends ImageViewEvent {

    
    private IImagePlot selectedPlot;

    private IImagePlot oldSelectedPlot;

    public ImagePlotSelectionEvent(ImageView view, IImagePlot oldSelectedPlot, IImagePlot newSelectedPlot) {
        super(view);
        this.selectedPlot = newSelectedPlot;
        this.oldSelectedPlot = oldSelectedPlot;
    }

    public IImagePlot getSelectedPlot() {
        return selectedPlot;
    }

    public IImagePlot getOldSelectedPlot() {
        return oldSelectedPlot;
    }
}
