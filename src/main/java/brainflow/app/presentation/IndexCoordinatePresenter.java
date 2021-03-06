package brainflow.app.presentation;

import brainflow.core.binding.CoordinateToIndexConverter;
import brainflow.app.presentation.controls.CoordinateSpinner;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.image.space.Axis;
import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 9:21:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexCoordinatePresenter extends BrainFlowPresenter {

    private CoordinateSpinner form;


    public IndexCoordinatePresenter() {

        buildGUI();
        if (getSelectedView() != null)
            bind();


    }


    private void buildGUI() {
        form = new CoordinateSpinner();


    }

    public void bind() {
        ImageView view = getSelectedView();

        
        CoordinateToIndexConverter iconv = new CoordinateToIndexConverter(view.worldCursorPos, view.getModel().getImageSpace(), Axis.X_AXIS);
        CoordinateToIndexConverter jconv = new CoordinateToIndexConverter(view.worldCursorPos, view.getModel().getImageSpace(), Axis.Y_AXIS);
        CoordinateToIndexConverter kconv = new CoordinateToIndexConverter(view.worldCursorPos, view.getModel().getImageSpace(), Axis.Z_AXIS);

        // bind cursorPos values to JSliders using double --> integer converter wrapper

        SwingBind.get().bind(iconv, form.getXspinner());
        SwingBind.get().bind(jconv, form.getYspinner());
        SwingBind.get().bind(kconv, form.getZspinner());

    }

    public void unbind() {
        SwingBind.get().unbind(form.getXspinner());
        SwingBind.get().unbind(form.getYspinner());
        SwingBind.get().unbind(form.getZspinner());

    }


    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void viewSelected(ImageView view) {
        bind();
    }


    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        if (oldModel.getImageSpace() != newModel.getImageSpace()) {
            unbind();
            viewSelected(view);
        }
    }

    @Override
    public void viewDeselected(ImageView view) {
        unbind();
        
    }

    public JComponent getComponent() {
        return form;
    }
}