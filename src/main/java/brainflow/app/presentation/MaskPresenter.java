package brainflow.app.presentation;

import brainflow.core.*;
import brainflow.core.layer.*;
import brainflow.display.InterpolationType;
import brainflow.core.binding.CoordinateToIndexConverter2;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.anatomy.Anatomy3D;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

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
public class MaskPresenter extends BrainFlowPresenter {



    private Box mainPanel;

    private ImageView maskView;

    private JSlider sliceSlider;


    public MaskPresenter() {

        buildGUI();

    }


    private void buildGUI() {
        mainPanel = new Box(BoxLayout.Y_AXIS);

        maskView = new SimpleImageView(new ImageViewModel(), Anatomy3D.getCanonicalAxial());
        maskView.setScreenInterpolation(InterpolationType.NEAREST_NEIGHBOR);

        mainPanel.add(maskView);

        sliceSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 0);
        mainPanel.add(sliceSlider);

        mainPanel.setPreferredSize(new Dimension(256, 256));


    }

    private void bind() {

        Anatomy3D displayAnatomy = maskView.getPlots().get(0).getDisplayAnatomy();
        IImageSpace3D space = maskView.getModel().getImageSpace();
        Axis zaxis = space.findAxis(displayAnatomy.ZAXIS);


        CoordinateToIndexConverter2 conv = new CoordinateToIndexConverter2(maskView.worldCursorPos, maskView.getModel().getImageSpace(), zaxis);

        int nslices = space.getDimension(displayAnatomy.ZAXIS);
        sliceSlider.setMaximum(nslices);
        SwingBind.get().bind(conv, sliceSlider);
    }

    private void unbind() {
        SwingBind.get().unbind(sliceSlider);

    }

    @Override
    public void viewDeselected(ImageView view) {
        BeanContainer.get().removeListener(view.getSelectedLayer().getLayerProps().thresholdRange, thresholdListener);
        BeanContainer.get().removeListener((view.getSelectedLayer()).maskProperty, thresholdListener);
        unbind();

    }


    @Override
    public void viewSelected(final ImageView view) {


        SwingWorker<ImageViewModel, Void> worker = new SwingWorker<ImageViewModel, Void>() {
            protected ImageViewModel doInBackground() throws Exception {
                return createMaskModel();

            }



            @Override
            protected void done() {

                try {
                    BeanContainer.get().addListener(view.getSelectedLayer().getLayerProps().thresholdRange, thresholdListener);
                    BeanContainer.get().addListener(view.getSelectedLayer().maskProperty, thresholdListener);

                    ImageViewModel model = get();
                    maskView.setModel(model);
                    bind();
                } catch(ExecutionException e) {

                } catch (InterruptedException e2) {

                }
            }
        };

        worker.execute();

    }


    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        if (!(oldModel.getSelectedLayer() == newModel.getSelectedLayer())) {
            BeanContainer.get().removeListener(oldModel.getSelectedLayer().getLayerProps().thresholdRange, thresholdListener);
            BeanContainer.get().removeListener(oldModel.getSelectedLayer().maskProperty, thresholdListener);
        }

        viewSelected(view);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void layerSelected(ImageLayer3D layer) {

        ImageViewModel model = createMaskModel();
        maskView.setModel(model);
        BeanContainer.get().addListener(layer.getLayerProps().thresholdRange, thresholdListener);
        BeanContainer.get().addListener((layer).maskProperty, thresholdListener);
    }

    @Override
    protected void layerDeselected(ImageLayer3D layer) {
        BeanContainer.get().removeListener(layer.getLayerProps().thresholdRange, thresholdListener);
        BeanContainer.get().removeListener(layer.maskProperty, thresholdListener);

    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private ThresholdListener thresholdListener = new ThresholdListener();


    public JComponent getComponent() {
        return mainPanel;
    }


    private ImageViewModel createMaskModel() {
        ImageLayer3D layer = getSelectedLayer();
        MaskLayer3D maskLayer = new MaskLayer3D(layer.getMaskProperty().buildMask());
        return new ImageViewModel("mask_model", new LayerList<ImageLayer3D>(maskLayer));


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

            ImageLayer3D layer = getSelectedLayer();
            MaskLayer3D maskLayer = new MaskLayer3D(layer.getMaskProperty().buildMask());

            ImageViewModel model = new ImageViewModel("mask_model", new LayerList<ImageLayer3D>(maskLayer));
            maskView.setModel(model);

        }


    }
}
