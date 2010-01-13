package brainflow.app.presentation;

import brainflow.core.*;
import brainflow.image.axis.AxisRange;
import brainflow.utils.NumberUtils;
import brainflow.app.services.ViewBoundsChangedEvent;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 31, 2009
 * Time: 2:51:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewZoomer extends BrainFlowPresenter {


    private static double scaleFactor = 50.0 / Math.log(100);


    //private int minZoom = (int) (Math.log(10) * scaleFactor);

    private int minZoom = (int) (Math.log(100) * scaleFactor);

    private int startZoom = (int) (Math.log(100) * scaleFactor);

    private int maxZoom = (int) (Math.log(10000) * scaleFactor);

    private JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, minZoom, maxZoom, startZoom);

    private ViewBounds lastViewBounds;

    public ImageViewZoomer() {
        super();

        initSlider();

       

    }

    private double convertToProportion(int sliderValue) {
        return Math.pow(Math.E, (double) sliderValue / scaleFactor) / 100;
    }

    private int convertToSlider(double prop) {
        return (int)(Math.log(prop*100) * scaleFactor);
    }

    private ViewBounds getViewBounds() {
        IImagePlot selPlot = getSelectedView().getSelectedPlot();
        return selPlot.getViewBounds();
    }

    private void shrinkView(ImageView view, double prop) {
        ViewBounds viewBounds = getViewBounds();
        AxisRange xrefRange = view.getModel().getImageAxis(viewBounds.getDisplayAnatomy().XAXIS).getRange();
        AxisRange yrefRange = view.getModel().getImageAxis(viewBounds.getDisplayAnatomy().YAXIS).getRange();

        ViewBounds vb = new ViewBounds(view.getModel().getImageSpace(),
                viewBounds.getDisplayAnatomy(),
                xrefRange.shrink(1 - 1.0 / prop),
                yrefRange.shrink(1 - 1.0 / prop));

        ViewBounds adjusted = vb.snapToGrid();
        lastViewBounds = adjusted;
        getSelectedView().getSelectedPlot().setViewBounds(adjusted);


    }

    private void growView(ImageView view, double prop) {
        ViewBounds viewBounds = getViewBounds();
        AxisRange xrefRange = view.getModel().getImageAxis(viewBounds.getDisplayAnatomy().XAXIS).getRange();
        AxisRange yrefRange = view.getModel().getImageAxis(viewBounds.getDisplayAnatomy().YAXIS).getRange();
        ViewBounds vb = new ViewBounds(view.getModel().getImageSpace(),
                viewBounds.getDisplayAnatomy(),
                xrefRange.grow(1.0 - prop),
                yrefRange.grow(1.0 - prop));

        ViewBounds adjusted = vb.snapToGrid();
        lastViewBounds = adjusted;
        getSelectedView().getSelectedPlot().setViewBounds(adjusted);

    }

    private void resetView() {
        ImageView view = getSelectedView();
        ViewBounds viewBounds = view.getSelectedPlot().getViewBounds();

        AxisRange xrefRange = view.getModel().getImageAxis(viewBounds.getDisplayAnatomy().XAXIS).getRange();
        AxisRange yrefRange = view.getModel().getImageAxis(viewBounds.getDisplayAnatomy().YAXIS).getRange();
        ViewBounds vb = new ViewBounds(view.getModel().getImageSpace(),
                viewBounds.getDisplayAnatomy(),
                xrefRange,
                yrefRange);

        lastViewBounds = vb.snapToGrid();
        view.getSelectedPlot().setViewBounds(lastViewBounds);


    }

    private void initSlider() {
        zoomSlider.setEnabled(false);
        zoomSlider.setPaintLabels(false);
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                ImageView view = getSelectedView();
                double prop = convertToProportion(zoomSlider.getValue());
                double curProp = 1/view.getSelectedPlot().getViewBounds().getXProportion();

              

                if (view != null) {

                    if (NumberUtils.equals(prop, 1, .02)) {
                        resetView();
                    } else {
                        if (prop > 1) {
                            shrinkView(view, prop);
                        } else {
                            growView(view, prop);

                        }
                    }


                }
            }
        });

    }

    @Override
    protected void plotSelected(IImagePlot plot) {
        //todo conversion from slider to X Proportion and back is not consistent  ...
        int slider = convertToSlider(1/plot.getViewBounds().getXProportion());
        zoomSlider.setValue(slider);
        zoomSlider.setEnabled(true);
    }

    @Override
    protected void viewBoundsChanged(ViewBoundsChangedEvent event) {

        if (lastViewBounds != null && !lastViewBounds.equals(event.getViewBounds())) {
            int slider = convertToSlider(1/event.getViewBounds().getXProportion());
            zoomSlider.setValue(slider);
        }
    }

    @Override
    public void viewSelected(ImageView view) {
        //todo conversion from slider to X Proportion and back is not consistent  ...
        int slider = convertToSlider(1/view.getSelectedPlot().getViewBounds().getXProportion());
        zoomSlider.setValue(slider);
        zoomSlider.setEnabled(true);
    }

    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    

    @Override
    public void allViewsDeselected() {
        zoomSlider.setEnabled(false);
    }

    @Override
    public JComponent getComponent() {
        return zoomSlider;
    }
}
