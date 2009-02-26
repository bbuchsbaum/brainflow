package brainflow.application.presentation;

import brainflow.core.ImageView;
import brainflow.core.ImageDisplayModel;
import brainflow.core.IImageDisplayModel;
import brainflow.core.SimpleImageView;
import brainflow.core.layer.*;
import brainflow.display.InterpolationType;
import brainflow.application.presentation.binding.CoordinateToIndexConverter2;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.anatomy.Anatomy3D;

import javax.swing.*;
import java.awt.*;

import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.container.BeanContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 26, 2008
 * Time: 2:28:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskPresenter extends ImageViewPresenter {

    private Box mainPanel;


    private ImageView maskView;

    private JSlider sliceSlider;


    public MaskPresenter() {

        buildGUI();

    }


    private void buildGUI() {
        mainPanel = new Box(BoxLayout.Y_AXIS);

        maskView = new SimpleImageView(new ImageDisplayModel("empty"), Anatomy3D.getCanonicalAxial());
        maskView.setScreenInterpolation(InterpolationType.NEAREST_NEIGHBOR);

        mainPanel.add(maskView);

        sliceSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 0);
        mainPanel.add(sliceSlider);

        mainPanel.setPreferredSize(new Dimension(256, 256));


    }

    private void bind() {

        Anatomy3D displayAnatomy = maskView.getPlots().get(0).getDisplayAnatomy();
        IImageSpace3D space = (IImageSpace3D) maskView.getModel().getImageSpace();
        Axis zaxis = space.findAxis(displayAnatomy.ZAXIS);

        CoordinateToIndexConverter2 conv = new CoordinateToIndexConverter2(maskView.worldCursorPos, (IImageSpace3D) maskView.getModel().getImageSpace(), zaxis);

        int nslices = space.getDimension(displayAnatomy.ZAXIS);
        sliceSlider.setMaximum(nslices);
        SwingBind.get().bind(conv, sliceSlider);
    }

    @Override
    public void viewDeselected(ImageView view) {
        BeanContainer.get().removeListener(view.getSelectedLayer().getImageLayerProperties().thresholdRange, thresholdListener);
        BeanContainer.get().removeListener(((ImageLayer3D) view.getSelectedLayer()).maskProperty, thresholdListener);


    }

    public void viewSelected(ImageView view) {
        IImageDisplayModel model = createMaskModel();
        maskView.setModel(model);
        BeanContainer.get().addListener(getSelectedLayer().getImageLayerProperties().thresholdRange, thresholdListener);
        BeanContainer.get().addListener(((ImageLayer3D) view.getSelectedLayer()).maskProperty, thresholdListener);
        bind();

    }

    @Override
    protected void layerSelected(ImageLayer3D layer) {
        IImageDisplayModel model = createMaskModel();
        maskView.setModel(model);
        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange, thresholdListener);
       
        BeanContainer.get().addListener((layer).maskProperty, thresholdListener);
    }

    @Override
    protected void layerDeselected(ImageLayer3D layer) {
        BeanContainer.get().removeListener(layer.getImageLayerProperties().thresholdRange, thresholdListener);
        BeanContainer.get().removeListener(( layer).maskProperty, thresholdListener);

    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private ThresholdListener thresholdListener = new ThresholdListener();


    public JComponent getComponent() {
        return mainPanel;
    }


    private IImageDisplayModel createMaskModel() {
        IImageDisplayModel model = new ImageDisplayModel("mask_model");
        ImageLayer3D layer = (ImageLayer3D) getSelectedLayer();
        MaskLayer3D maskLayer = new MaskLayer3D(layer.getMaskProperty().buildMask());

        //model.addLayer(layer);
        model.addLayer(maskLayer);

        return model;

    }

    /*class MaskListener implements PropertyListener {
        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            IImageDisplayModel model = new ImageDisplayModel("mask_model");
            ImageLayer3D layer = (ImageLayer3D)getSelectedLayer();
            MaskLayer3D maskLayer = new MaskLayer3D(layer.getMaskProperty().buildMask());

            //model.addLayer(layer);
            model.addLayer(maskLayer);
            maskView.setModel(model);

        }
    }   */


    class ThresholdListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            IImageDisplayModel model = new ImageDisplayModel("mask_model");
            ImageLayer3D layer = (ImageLayer3D) getSelectedLayer();
            MaskLayer3D maskLayer = new MaskLayer3D(layer.getMaskProperty().buildMask());

            //model.addLayer(layer);
            model.addLayer(maskLayer);
            maskView.setModel(model);

        }


    }
}
