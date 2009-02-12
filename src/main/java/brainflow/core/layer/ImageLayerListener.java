package brainflow.core.layer;

import brainflow.core.layer.ImageLayerEvent;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 3:24:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageLayerListener extends EventListener {


    public void thresholdChanged(ImageLayerEvent event);

    public void colorMapChanged(ImageLayerEvent event);

    public void opacityChanged(ImageLayerEvent event);

    public void interpolationMethodChanged(ImageLayerEvent event);

    public void visibilityChanged(ImageLayerEvent event);

    public void smoothingChanged(ImageLayerEvent event);

    public void clipRangeChanged(ImageLayerEvent event);

    public void maskChanged(ImageLayerEvent event);


}
