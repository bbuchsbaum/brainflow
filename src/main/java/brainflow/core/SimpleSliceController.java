package brainflow.core;

import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.anatomy.SpatialLoc1D;
import brainflow.image.anatomy.GridLoc1D;
import brainflow.image.space.Axis;
import brainflow.image.axis.ImageAxis;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 7, 2007
 * Time: 2:03:58 PM
 * To change this template use File | Settings | File Templates.
 */
class SimpleSliceController implements SliceController {

    private double pageStep = .12;

    private ImageView imageView;

    public SimpleSliceController(ImageView imageView) {
        this.imageView = imageView;
        initCursorListener();
    }

    
    protected void initCursorListener() {
        BeanContainer.get().addListener(imageView.cursorPos, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                GridPoint3D oldval = (GridPoint3D)oldValue;
                GridPoint3D newval = (GridPoint3D)newValue;

         
                if (!oldval.equals(newval)) {
                    imageView.getSelectedPlot().setSlice(newval);
                    IImagePlot selectedPlot = imageView.getSelectedPlot();
                    selectedPlot.getComponent().repaint();

                }
            }
        });
    }


    public GridPoint3D getSlice() {
        return imageView.getCursorPos();
    }

    

    public void setSlice(GridPoint3D slice) {
        if (!slice.equals(imageView.cursorPos.get())) {
            imageView.cursorPos.set(slice);
        }

        
    }

    protected ImageView getView() {
        return imageView;

    }

    protected GridPoint3D incrementSlice(double incr) {
        GridPoint3D slice = getSlice();
        ImageAxis iaxis = zaxis();
        GridLoc1D pt = slice.getValue(iaxis.getAnatomicalAxis(), false);
        double z = pt.toReal().getValue() + incr;
        return slice.replace(new SpatialLoc1D(iaxis.getAnatomicalAxis(), z));

    }

    private ImageAxis zaxis() {
        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        return imageView.getModel().getImageAxis(axis);

    }

    public void nextSlice() {
        imageView.cursorPos.set(incrementSlice(zaxis().getSpacing()));
    }

    public void previousSlice() {
        imageView.cursorPos.set(incrementSlice(-zaxis().getSpacing()));
    }

    public void pageBack() {
        imageView.cursorPos.set(incrementSlice(-(zaxis().getExtent() * pageStep)));
    }

    public void pageForward() {
        imageView.cursorPos.set(incrementSlice(zaxis().getExtent() * pageStep));

    }
}
