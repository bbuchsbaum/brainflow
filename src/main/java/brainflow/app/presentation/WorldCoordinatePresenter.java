package brainflow.app.presentation;

import brainflow.core.binding.WorldToAxisConverter;
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
 * Time: 2:02:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldCoordinatePresenter extends BrainFlowPresenter {

    private CoordinateSpinner form;


    public WorldCoordinatePresenter() {
        buildGUI();

        if (getSelectedView() != null)
            bind();

    }


    private void buildGUI() {
        form = new CoordinateSpinner();

    }

    private void unbind() {
        SwingBind.get().unbind(form.getXspinner());
        SwingBind.get().unbind(form.getYspinner());
        SwingBind.get().unbind(form.getZspinner());
    }

    private void bind() {
        ImageView view = getSelectedView();

        form.getXspinner().setModel(new SpinnerNumberModel(0, -1000, 1000, 1.0));
        form.getYspinner().setModel(new SpinnerNumberModel(0, -1000, 1000, 1.0));
        form.getZspinner().setModel(new SpinnerNumberModel(0, -1000, 1000, 1.0));

        SwingBind.get().bind(new WorldToAxisConverter(view.worldCursorPos, Axis.X_AXIS), form.getXspinner());
        SwingBind.get().bind(new WorldToAxisConverter(view.worldCursorPos, Axis.Y_AXIS), form.getYspinner());
        SwingBind.get().bind(new WorldToAxisConverter(view.worldCursorPos, Axis.Z_AXIS), form.getZspinner());


        String header1 = view.worldCursorPos.get().getAnatomy().XAXIS.getMinDirection().toString();
        String header2 = view.worldCursorPos.get().getAnatomy().YAXIS.getMinDirection().toString();
        String header3 = view.worldCursorPos.get().getAnatomy().ZAXIS.getMinDirection().toString();

        form.getXspinnerHeader().setText(header1);
        form.getYspinnerHeader().setText(header2);
        form.getZspinnerHeader().setText(header3);

       
    }


    public JComponent getComponent() {
        return form;
    }

    public void viewSelected(ImageView view) {
        bind();
    }

    @Override
    public void viewDeselected(ImageView view) {
        unbind();
    }

    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        //do nothing?
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}