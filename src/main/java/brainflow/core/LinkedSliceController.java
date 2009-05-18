package brainflow.core;

import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.GridPoint3D;

import java.util.Iterator;

import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 8, 2007
 * Time: 1:03:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkedSliceController extends SimpleSliceController {

    public LinkedSliceController(ImageView imageView) {
        super(imageView);
    }

    protected void initCursorListener() {
        BeanContainer.get().addListener(getView().cursorPos, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

                GridPoint3D oldval = (GridPoint3D)oldValue;
                GridPoint3D newval = (GridPoint3D)newValue;

                if (!oldval.equals(newval)) {
                    Iterator<IImagePlot> iter = getView().plotIterator();
                    //IImagePlot selPlot = getView().getSelectedPlot();
                    while (iter.hasNext()) {
                        IImagePlot plot = iter.next();

                        GridPoint3D crossSlice = getView().getCursorPos();

                        if (!plot.getSlice().equals(crossSlice))
                            plot.setSlice(crossSlice);
                        
                    }


                }
            }
        });
    }



    
}
