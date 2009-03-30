package brainflow.app.presentation;

import brainflow.app.presentation.binding.CoordinateToIndexConverter2;
import brainflow.app.presentation.controls.CoordinateSpinner;
import brainflow.core.ImageView;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.IImageSpace3D;
import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 9:21:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexCoordinatePresenter2 extends ImageViewPresenter {

    private CoordinateSpinner form;


    public IndexCoordinatePresenter2() {

        buildGUI();
        if (getSelectedView() != null)
            bind();


    }


    private void buildGUI() {
        form = new CoordinateSpinner();


    }

    public void bind() {
        ImageView view = getSelectedView();
        IImageSpace ispace = view.getModel().getImageSpace();

        
        CoordinateToIndexConverter2 iconv = new CoordinateToIndexConverter2(view.worldCursorPos, (IImageSpace3D)view.getModel().getImageSpace(), Axis.X_AXIS);
        CoordinateToIndexConverter2 jconv = new CoordinateToIndexConverter2(view.worldCursorPos, (IImageSpace3D)view.getModel().getImageSpace(), Axis.Y_AXIS);
        CoordinateToIndexConverter2 kconv = new CoordinateToIndexConverter2(view.worldCursorPos, (IImageSpace3D)view.getModel().getImageSpace(), Axis.Z_AXIS);
        // bind cursorPos values to JSliders using double --> integer converter wrapper

        SwingBind.get().bind(iconv, form.getXspinner());
        SwingBind.get().bind(jconv, form.getYspinner());
        SwingBind.get().bind(kconv, form.getZspinner());

        //SwingBind.get().bind(new IntegerToStringConverter(iconv), form.getValueLabel1());
        //SwingBind.get().bind(new IntegerToStringConverter(jconv), form.getValueLabel2());
        //SwingBind.get().bind(new IntegerToStringConverter(kconv), form.getValueLabel3());

        //form.getSliderLabel1().setText("I: " + "(" + xaxis.getAnatomicalAxis() + ")");
        //form.getSliderLabel2().setText("J: " + "(" + yaxis.getAnatomicalAxis() + ")");
        //form.getSliderLabel3().setText("K: " + "(" + zaxis.getAnatomicalAxis() + ")");
    }


    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void viewSelected(ImageView view) {
        bind();
    }



    public JComponent getComponent() {
        return form;
    }
}