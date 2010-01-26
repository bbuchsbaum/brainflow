package brainflow.core;

import brainflow.image.anatomy.GridLoc3D;
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
                GridLoc3D oldval = (GridLoc3D)oldValue;
                GridLoc3D newval = (GridLoc3D)newValue;

                System.out.println("new grid loc = " + newval);
         
                if (!oldval.equals(newval)) {
                    imageView.getSelectedPlot().setSlice(newval);
                    IImagePlot selectedPlot = imageView.getSelectedPlot();
                    selectedPlot.getComponent().repaint();

                }
            }
        });
    }


    public GridLoc3D getSlice() {
        return imageView.getCursorPos();
    }

    

    public void setSlice(GridLoc3D slice) {
        if (!slice.equals(imageView.cursorPos.get())) {
            imageView.cursorPos.set(slice);
            System.out.println("setting slice to gridloc " + slice);
        }

        
    }

    protected ImageView getView() {
        return imageView;

    }

    protected GridLoc3D incrementSlice(double incr) {
        GridLoc3D slice = getSlice();
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
