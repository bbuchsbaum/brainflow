package brainflow.core;

import brainflow.app.services.ImageViewLayerSelectionEvent;
import brainflow.core.annotations.IAnnotation;
import brainflow.core.layer.ImageLayer3D;
import brainflow.display.InterpolationType;
import brainflow.image.anatomy.*;
import brainflow.image.axis.ImageAxis;
import brainflow.image.space.Axis;
import brainflow.image.space.ICoordinateSpace;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ICoordinateSpace3D;

import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.events.PropertyListener;
import org.bushe.swing.event.EventBus;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 17, 2004
 * Time: 11:55:26 AM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageView extends JPanel implements ListDataListener, ImageDisplayModelListener {




    private static final Logger log = Logger.getLogger(ImageView.class.getName());




    private List<IImagePlot> plotList = new ArrayList<IImagePlot>();

    private int selectedPlotIndex = -1;

    private String identifier = "view";

    private double pixelsPerUnit = 1.5;

    public final Property<ImageViewModel> viewModel = ObservableProperty.create();
   
    public final Property<Boolean> preserveAspect = new ObservableProperty<Boolean>(false) {
        public void set(Boolean aBoolean) {
            super.set(aBoolean);
            Iterator<IImagePlot> iter = getPlots().iterator();
            while (iter.hasNext()) {
                IImagePlot plot = iter.next();
                plot.setPreserveAspectRatio(aBoolean);
            }

        }
    };


    public final Property<AnatomicalPoint3D> cursorPos = new ObservableProperty<AnatomicalPoint3D>() {

        public void set(AnatomicalPoint3D ap) {

            ap = ap.snapToBounds();
            if (!ap.equals(get())) {
                super.set(ap);
            }

        }
    };

    public final Property<AnatomicalPoint3D> worldCursorPos = new ObservableWrapper.ReadWrite<AnatomicalPoint3D>(cursorPos) {

        public void set(AnatomicalPoint3D ap) {
            if (ap.getAnatomy() != Anatomy3D.REFERENCE_ANATOMY) {
                throw new IllegalArgumentException("world point " + ap + " must have Anatomy " + Anatomy3D.REFERENCE_ANATOMY);
            }

            super.set(AnatomicalPoint3D.convertFromWorld(ap, getModel().getImageSpace()));
        }

        public AnatomicalPoint3D get() {
            RProperty<AnatomicalPoint3D> cpos = (RProperty<AnatomicalPoint3D>) getProperties()[0];
            return AnatomicalPoint3D.convertToWorld(cpos.get(), getModel().getImageSpace());
        }
    };


    public final Property<Double> cursorX = new ObservableProperty<Double>() {
        public void set(Double aDouble) {
            super.set(aDouble);
            cursorPos.set(new AnatomicalPoint3D(getModel().getImageSpace(), aDouble, cursorPos.get().getY(), cursorPos.get().getZ()));
        }

        public Double get() {
            return cursorPos.get().getX();
        }
    };

    public final Property<Double> cursorY = new ObservableProperty<Double>() {
        public void set(Double aDouble) {
            super.set(aDouble);
            cursorPos.set(new AnatomicalPoint3D(getModel().getImageSpace(), cursorPos.get().getX(), aDouble, cursorPos.get().getZ()));
        }

        public Double get() {
            return cursorPos.get().getY();
        }
    };

    public final Property<Double> cursorZ = new ObservableProperty<Double>() {
        public void set(Double aDouble) {
            super.set(aDouble);
            cursorPos.set(new AnatomicalPoint3D( getModel().getImageSpace(), cursorPos.get().getX(), cursorPos.get().getY(), aDouble));
        }

        public Double get() {
            return cursorPos.get().getZ();
        }
    };

    private InterpolationType screenInterpolation = InterpolationType.NEAREST_NEIGHBOR;

    private PlotSelectionHandler plotSelectionHandler = new PlotSelectionHandler();

    private ImageLayerSelectionListener layerSelectionListener = new ImageLayerSelectionListener();

    private JPanel contentPane = new JPanel();

    private SliceController sliceController;

    protected Viewport3D viewport;


    public ImageView(ImageViewModel imodel) {
        BeanContainer.bind(this);

        viewModel.set(imodel);

        setLayout(new BorderLayout());

        add(contentPane, BorderLayout.CENTER);
        initView(imodel);
    }



    public abstract ImagePlotLayout getPlotLayout();

    protected void resetPlotLayout(ImagePlotLayout plotLayout, Map<String, IAnnotation> annotationMap) {
        plotList = plotLayout.layoutPlots();

        if (!plotList.isEmpty()) {
            selectedPlotIndex = 0;
        } else {
            selectedPlotIndex = -1;
        }

        for (String key : annotationMap.keySet() ) {
            setAnnotation(key, annotationMap.get(key));
        }


        sliceController = plotLayout.createSliceController();
        revalidate();
        repaint();
    }



    protected abstract void layoutPlots();

    private void clearListeners(ImageViewModel oldModel) {
        BeanContainer.get().removeListener(oldModel.layerSelection, layerSelectionListener);
        removeMouseListener(plotSelectionHandler);

    }

    public void clearListeners() {
        clearListeners(viewModel.get());

    }


    protected void registerListeners(ImageViewModel model) {
        BeanContainer.get().addListener(model.layerSelection, layerSelectionListener);
        addMouseListener(plotSelectionHandler);



    }

    private void updateView(ImageViewModel oldModel, ImageViewModel newModel) {
        clearListeners(oldModel);
        initView(newModel);

        //one reason this is necesary is because IImagePlots do not have a setModel method. This means that new plots have to be created.
        

        //todo this transfer annotations from first plot only, which is wrong if differet plots can have different annotations
        IImagePlot oldPlot = plotList.get(0);
        Map<String, IAnnotation> amap = oldPlot.getAnnotations();



        ImagePlotLayout layout = getPlotLayout();
        plotList = layout.layoutPlots();
        

        for (String key : amap.keySet() ) {
            setAnnotation(key, amap.get(key));
        }
        //todo end


        sliceController = layout.createSliceController();
        revalidate();
        repaint();



    }


    protected void initViewport(ImageViewModel model) {
        viewport = new Viewport3D(model);
        if (cursorPos.get() == null || !viewport.inBounds(cursorPos.get().convertTo(viewport.getBounds()))) {
            AnatomicalPoint3D centroid = model.getImageSpace().getCentroid();
            cursorPos.set(new CursorPosition(model.getImageSpace(), centroid.getX(), centroid.getY(), centroid.getZ()));
        }

    }

    protected void initView(ImageViewModel model) {
        initViewport(model);
        registerListeners(model);
       
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    protected JPanel getContentPane() {
        return contentPane;
    }


    public SliceController getSliceController() {
        return sliceController;
    }


    public boolean isPreserveAspect() {
        return preserveAspect.get();
    }

    public void setPreserveAspect(boolean _preserveAspect) {
        preserveAspect.set(_preserveAspect);
    }


    public InterpolationType getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationType screenInterpolation) {
        this.screenInterpolation = screenInterpolation;
        Iterator<IImagePlot> iter = getPlots().iterator();
        while (iter.hasNext()) {
            IImagePlot plot = iter.next();
            plot.setScreenInterpolation(screenInterpolation);
        }


    }

    public void setSelectedPlot(IImagePlot plot) {
        int idx = plotList.indexOf(plot);
        if (idx >= 0) {
            selectedPlotIndex = idx;
        } else {
            throw new IllegalArgumentException("plot argument not contained in this view");
        }

    }

    public IImagePlot getSelectedPlot() {
        int idx = selectedPlotIndex;
        if (idx >= 0) {
            return plotList.get(idx);
        }

        return null;

    }

    public ImageLayer3D getSelectedLayer() {
        return viewModel.get().get(getSelectedLayerIndex());
    }

    public int getSelectedLayerIndex() {
        return viewModel.get().layerSelection.get();
    }

    public void setSelectedLayerIndex(int selectedIndex) {
        viewModel.get().layerSelection.set(selectedIndex);
    }

    public void setModel(ImageViewModel model) {
        ImageViewModel oldModel = viewModel.get();
        viewModel.set(model);
        updateView(oldModel, model);

    }

    public ImageViewModel getModel() {
        return viewModel.get();
    }


    public AnatomicalPoint3D getCursorPos() {
        return cursorPos.get();
    }

    public Viewport3D getViewport() {
        return viewport;
    }

    public ImageAxis getImageAxis(AnatomicalAxis axis) {
        ImageAxis iaxis = getModel().get(0).getCoordinateSpace().getImageAxis(axis, true);
        ImageAxis retAxis = iaxis.matchAxis(axis);
        return retAxis;

    }

    public ImageAxis getImageAxis(Axis axis) {
        return getModel().get(0).getCoordinateSpace().getImageAxis(axis);
    }


    public AnatomicalPoint3D getCentroid() {
        ICoordinateSpace3D compositeSpace = getModel().get(0).getCoordinateSpace();
        return compositeSpace.getCentroid();
    }

    public void clearAnnotations() {
        for (IImagePlot plot : getPlots()) {
            plot.clearAnnotations();
        }
    }

    public IAnnotation getAnnotation(IImagePlot plot, String name) {

        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        return plot.getAnnotation(name);
    }

    public void removeAnnotation(IImagePlot plot, String name) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.removeAnnotation(name);

    }

    public void setAnnotation(String name, IAnnotation annotation) {
        Iterator<IImagePlot> iter = getPlotLayout().iterator();
        while (iter.hasNext()) {
            setAnnotation(iter.next(), name, annotation);
        }
    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        if (!getPlotLayout().containsPlot(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.setAnnotation(name, annotation);


    }

    /*public Point getCrosshairLocation(IImagePlot plot) {
        if (!getPlotLayout().containsPlot(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        AnatomicalPoint3D ap = cursorPos.get();
        //todo fix me, what on earth is going on here?


        AnatomicalPoint3D dispAP = null;

        double xpt = dispAP.getValue(Axis.X_AXIS.getId());
        double ypt = dispAP.getValue(Axis.Y_AXIS.getId());

        double percentX = (xpt - plot.getXAxisRange().getBeginning().getValue()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt - plot.getYAxisRange().getBeginning().getValue()) / plot.getYAxisRange().getInterval();

        Rectangle2D plotArea = plot.getPlotArea();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        Point location = new Point((int) Math.round(screenX), (int) Math.round(screenY));
        return SwingUtilities.convertPoint(plot.getComponent(), location, this);

    }*/

    public boolean pointInPlot(Component source, Point p) {
        Point viewPoint = SwingUtilities.convertPoint(source, p, this);
        return !(whichPlot(viewPoint) == null);

    }

    //todo this method is just really, really bad. fix it.
    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point p) {

        Point viewPoint = SwingUtilities.convertPoint(source, p, this);
        IImagePlot plot = whichPlot(viewPoint);

        if (plot == null) {
            throw new IllegalArgumentException("Point p: " + p +  " is not within bounds of component");

        }

        //System.out.println("plot loc " + plot.getSlice());

        Point plotPoint = SwingUtilities.convertPoint(this, viewPoint, plot.getComponent());

        AnatomicalPoint2D apoint = plot.translateScreenToAnat(plotPoint);

        Anatomy3D displayAnatomy = plot.getDisplayAnatomy();

        AnatomicalPoint3D ap3d = new AnatomicalPoint3D(
                Anatomy3D.matchAnatomy(
                        plot.getXAxisRange().getAnatomicalAxis(),
                        plot.getYAxisRange().getAnatomicalAxis(),
                        plot.getDisplayAnatomy().ZAXIS),
                apoint.getX(), apoint.getY(),
                plot.getSlice().getValue(displayAnatomy.ZAXIS).getValue());

                //getCursorPos().getValue(displayAnatomy.ZAXIS).getValue());

        //System.out.println("ap3d " + ap3d);


        return ap3d;
    }


    public IImagePlot whichPlot(Point p) {
        return getPlotLayout().whichPlot(p);
    }

    public List<IImagePlot> getPlots() {
        return getPlotLayout().getPlots();
    }

    public ListIterator<IImagePlot> plotIterator() {
        return getPlotLayout().iterator();
    }

    public RenderedImage captureImage() {
        BufferedImage img = new BufferedImage(getContentPane().getWidth(),
                getContentPane().getHeight(), BufferedImage.TYPE_INT_ARGB);
        getContentPane().paint(img.createGraphics());
        return img;


    }


    public void intervalAdded(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }

    }

    public void intervalRemoved(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }
    }

    public void contentsChanged(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {

            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }
    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
        Viewport3D viewport = getViewport();
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.setViewBounds(new ViewBounds(plot.getDisplayAnatomy(),
                    viewport.getRange(plot.getDisplayAnatomy().XAXIS),
                    viewport.getRange(plot.getDisplayAnatomy().YAXIS)));

        }

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = getPlotLayout().getPreferredSize();

        d.setSize(d.getWidth() * pixelsPerUnit, d.getHeight() * pixelsPerUnit);
        return d;
    }

    public String toString() {
        return identifier;
    }

    class ImageLayerSelectionListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

            int selectionIndex = (Integer) newValue;
            int oldIndex = (Integer) oldValue;

            if (selectionIndex >= 0) {

                ImageLayer3D selectedLayer = getModel().get(selectionIndex);
                ImageLayer3D deselectedLayer = getModel().get(oldIndex);

                if (selectionIndex >= 0) {
                    EventBus.publish(new ImageViewLayerSelectionEvent(ImageView.this, deselectedLayer, selectedLayer));
                }
            }

        }


    }


    class ViewportHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            List<IImagePlot> plots = ImageView.this.getPlots();
            for (IImagePlot plot : plots) {
                plot.setViewBounds(new ViewBounds(plot.getDisplayAnatomy(),
                        viewport.getRange(plot.getDisplayAnatomy().XAXIS),
                        viewport.getRange(plot.getDisplayAnatomy().YAXIS)));

            }


        }
    }

    class PlotSelectionHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            IImagePlot plot = whichPlot(e.getPoint());
            if (plot != null && plot != getSelectedPlot()) {
                IImagePlot oldPlot = getSelectedPlot();

                setSelectedPlot(plot);

                plot.getComponent().repaint();

                if (oldPlot != null)
                    oldPlot.getComponent().repaint();
            }
        }

    }
    
}