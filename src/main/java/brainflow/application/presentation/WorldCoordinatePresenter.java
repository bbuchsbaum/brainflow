package brainflow.application.presentation;

import brainflow.application.presentation.binding.DoubleToStringConverter;
import brainflow.application.presentation.binding.PercentageRangeConverter;
import brainflow.application.presentation.controls.TripleSliderForm;
import brainflow.core.ImageView;

import brainflow.image.axis.ImageAxis;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 2:02:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldCoordinatePresenter extends ImageViewPresenter {

    private TripleSliderForm form;


    public WorldCoordinatePresenter() {
        buildGUI();

        if (getSelectedView() != null)
            bind();

    }


    private void buildGUI() {
        form = new TripleSliderForm();

    }

    private void bind() {
        ImageView view = getSelectedView();
        IImageSpace ispace = view.getModel().getImageSpace();

        ImageAxis xaxis = ispace.getImageAxis(Axis.X_AXIS);
        ImageAxis yaxis = ispace.getImageAxis(Axis.Y_AXIS);
        ImageAxis zaxis = ispace.getImageAxis(Axis.Z_AXIS);

        // bind cursorPos values to JSliders using double --> integer converter wrapper
        SwingBind.get().bind(new PercentageRangeConverter(view.cursorX, xaxis.getMinimum(), xaxis.getMaximum(), 100), form.getSlider1());
        SwingBind.get().bind(new PercentageRangeConverter(view.cursorY, yaxis.getMinimum(), yaxis.getMaximum(), 100), form.getSlider2());
        SwingBind.get().bind(new PercentageRangeConverter(view.cursorZ, zaxis.getMinimum(), zaxis.getMaximum(), 100), form.getSlider3());

        SwingBind.get().bind(new DoubleToStringConverter(view.cursorX), form.getValueLabel1());
        SwingBind.get().bind(new DoubleToStringConverter(view.cursorY), form.getValueLabel2());
        SwingBind.get().bind(new DoubleToStringConverter(view.cursorZ), form.getValueLabel3());

        form.getSliderLabel1().setText("X: " + "(" + xaxis.getAnatomicalAxis() + ")");
        form.getSliderLabel2().setText("Y: " + "(" + yaxis.getAnatomicalAxis() + ")");
        form.getSliderLabel3().setText("Z: " + "(" + zaxis.getAnatomicalAxis() + ")");
    }


    public JComponent getComponent() {
        return form;
    }

    public void viewSelected(ImageView view) {
        bind();
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
