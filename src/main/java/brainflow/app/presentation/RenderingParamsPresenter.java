/*
 * OpacityPresenter.java
 *
 * Created on July 12, 2006, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.presentation;

import brainflow.core.binding.Bindable;
import brainflow.core.binding.PercentageRangeConverter;
import brainflow.app.presentation.controls.RenderingParamsForm;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;

import javax.swing.*;

import net.java.dev.properties.binding.swing.adapters.SwingBind;


/**
 * @author buchs
 */
public class RenderingParamsPresenter extends BrainFlowPresenter implements Bindable {


    private RenderingParamsForm form;



    /**
     * Creates a new instance of OpacityPresenter
     */
    public RenderingParamsPresenter() {
        form = new RenderingParamsForm();
        if (getSelectedView() != null) {
            bind();
        }


    }


    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        viewSelected(view);
    }

    public void viewSelected(ImageView view) {
        bind();
        //form.getInterpolationLabel().setEnabled(true);
        //form.getInterpolationChoices().setEnabled(true);
       
    }

    public void allViewsDeselected() {
        form.setEnabled(false);
        //form.getInterpolationChoices().setEnabled(false);
       // form.getInterpolationLabel().setEnabled(false);
    }

    @Override
    protected void layerSelected(ImageLayer3D layer) {
        bind();

    }

    public JComponent getComponent() {
        return form;
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        SwingBind.get().bind(new PercentageRangeConverter(layer.getLayerProps().opacity, 0, 1, 100), form.getOpacitySlider());
        SwingBind.get().bind(new PercentageRangeConverter(layer.getLayerProps().smoothingRadius, 0, 15, 100), form.getSmoothingSlider());
        //SwingBind.get().bindContent(layer.getLayerProps().interpolationSet, form.getInterpolationChoices());
        //SwingBind.get().bindIndex(layer.getLayerProps().interpolationSelection, form.getInterpolationChoices());
    }

    public void unbind() {
        //todo unbind
    }

    


}