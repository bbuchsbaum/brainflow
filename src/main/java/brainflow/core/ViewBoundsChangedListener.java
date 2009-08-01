package brainflow.core;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 1, 2009
 * Time: 1:45:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ViewBoundsChangedListener extends EventListener {

    public void viewBoundsChanged(IImagePlot source, ViewBounds oldViewBounds, ViewBounds newViewBounds);
}
