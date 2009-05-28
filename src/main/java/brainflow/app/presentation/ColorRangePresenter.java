package brainflow.app.presentation;

import brainflow.core.binding.Bindable;
import brainflow.core.binding.ExtBind;

import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.gui.BiSlider;
import brainflow.gui.NumberRangeModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author buchs
 */
public class ColorRangePresenter extends ImageViewPresenter implements Bindable {



    private JPanel form;

    private BiSlider bislider;


    /**
     * Creates a new instance of ColorRangePanel
     */
    public ColorRangePresenter() {

        form = new JPanel();
        initGUI();

        if (getSelectedView() != null) {
            bind();
        }

    }


    private void initGUI() {

        bislider = new BiSlider(new NumberRangeModel(0,100,0,100));
        form.setLayout(new BorderLayout());
        form.setBorder(new EmptyBorder(5,1,5,1));
        form.add(bislider, BorderLayout.CENTER);

    }

    public JComponent getComponent() {
        return form;
    }

    public void allViewsDeselected() {
        unbind();
    }

    public void viewSelected(ImageView view) {
        bind();
    }



    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        unbind();
        viewSelected(view);
    }

    @Override
    protected void layerSelected(ImageLayer3D layer) {
        bind();
    }

    public void unbind() {
        ExtBind.get().unbind(bislider);
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();

        ExtBind.get().bindBiSlider(layer.getImageLayerProperties().clipRange, bislider);

       

    }



    public static void main(String[] args) {


    }


}