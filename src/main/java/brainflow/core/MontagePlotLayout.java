package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.ImageAxis;
import brainflow.image.space.Axis;
import brainflow.image.space.Space;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 1, 2009
 * Time: 7:36:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MontagePlotLayout extends ImagePlotLayout {

    private int ncols = 3;

    private int nrows = 3;

    private float sliceGap = 3;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private MontageSliceController sliceController = null;

    public MontagePlotLayout(ImageView view, Anatomy3D displayAnatomy, int nrows, int ncols, float sliceGap) {
        super(view);
        this.displayAnatomy = displayAnatomy;
        this.nrows = nrows;
        this.ncols = ncols;
        this.sliceGap = sliceGap;
    }

    public MontagePlotLayout(ImageView view, Anatomy3D displayAnatomy, int nrows, int ncols) {
        super(view);
        this.displayAnatomy = displayAnatomy;
        this.nrows = nrows;
        this.ncols = ncols;
        sliceGap = (float) view.getModel().getImageSpace().getImageAxis(displayAnatomy.ZAXIS, true).getSpacing();
    }

    public MontageSliceController createSliceController() {
        if (sliceController == null) {
            sliceController = new MontageSliceController(getView());
        }

        return sliceController;
    }

    public int getNcols() {
        return ncols;
    }

    public int getNrows() {
        return nrows;
    }

    public float getSliceGap() {
        return sliceGap;
    }

    public void setSliceGap(float sliceGap) {
        this.sliceGap = sliceGap;
        sliceController.updateSlices();
    }

    public int getNumPlots() {
        return getNrows() * getNcols();
    }

    public Dimension getPreferredSize() {
        //todo is this used?
        return new Dimension(150 * getNrows(), 150 * getNcols());
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
    }

    public IImagePlot whichPlot(Point p) {
        //todo duplicate code should be abstracted
        for (IImagePlot plot : plots) {
            Point point = SwingUtilities.convertPoint(getView(), p, plot.getComponent());

            boolean inplot = !((point.x < 0) || (point.y < 0) || (point.x > plot.getComponent().getWidth()) || (point.y > plot.getComponent().getHeight()));

            if (inplot) {
                return plot;
            }
        }

        //todo perhaps this should throw an exception?
        return null;
    }

    public List<IImagePlot> layoutPlots() {
        plots = createPlots();
        getView().getContentPane().removeAll();
        getView().getContentPane().setLayout(new GridLayout(nrows, ncols, 1, 1));
        for (int i = 0; i < getNrows(); i++) {
            for (int j = 0; j < getNcols(); j++) {
                IImagePlot iplot = plots.get(i * getNcols() + j);
                getView().getContentPane().add(iplot.getComponent());
            }
        }

        return plots;
    }

    protected IImagePlot configPlot(IImagePlot plot, int index, int row, int column) {
        MontageSliceController controller = this.createSliceController();
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString() + row + ", " + column);
        AnatomicalPoint3D nextSlice = controller.getSliceForPlot(index);
        plot.setSlice(nextSlice);

        return plot;

    }

    @Override
    protected IImagePlot createPlot(Anatomy3D displayAnatomy) {
        IImagePlot plot = ImagePlotFactory.createComponentPlot(getView().getModel(), displayAnatomy);

        plot.setScreenInterpolation(getView().getScreenInterpolation());
        return plot;
    }

    protected List<IImagePlot> createPlots() {
        int nr = getNrows();
        int nc = getNcols();
        List<IImagePlot> plots = new ArrayList<IImagePlot>(nr * nc);
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                IImagePlot iplot = configPlot(createPlot(displayAnatomy), i * nc + j, i, j);
                plots.add(iplot);
            }
        }

        return plots;


    }

    protected void repaintPlots() {
        for (IImagePlot plot : getPlots()) {
            plot.getComponent().repaint();
        }
    }

    class MontageSliceController extends SimpleSliceController {


        private AnatomicalPoint3D sentinel;


        MontageSliceController(ImageView imageView) {
            super(imageView);
            sentinel = getView().getCursorPos();
        }

        private AnatomicalPoint3D getSliceForPlot(int i) {

            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            return sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + (i * sliceGap)));

        }

        private IImagePlot nearestPlot(AnatomicalPoint1D zslice) {
            IImagePlot nearest = null;
            double mindist = Double.MAX_VALUE;

            for (IImagePlot plot : getPlots()) {
                AnatomicalPoint1D curz = plot.getSlice().getValue(zaxis().getAnatomicalAxis());
                double dist = Math.abs(curz.getValue() - zslice.getValue());
                if (dist < mindist) {
                    nearest = plot;
                    mindist = dist;
                }
            }

            return nearest;
        }

        private boolean outOfRange(AnatomicalPoint1D zselected) {
            int nplots = getNumPlots();
            AnatomicalPoint1D zsentinel = sentinel.getValue(zaxis().getAnatomicalAxis());
            return (zselected.getValue() > (zsentinel.getValue() + sliceGap * nplots)) || (zselected.getValue() < zsentinel.getValue());

        }

        protected void initCursorListener() {
            BeanContainer.get().addListener(getView().cursorPos, new PropertyListener() {
                public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                    AnatomicalPoint3D oldval = (AnatomicalPoint3D) oldValue;
                    AnatomicalPoint3D newval = (AnatomicalPoint3D) newValue;

                    if (!oldval.equals(newval)) {
                        AnatomicalPoint1D zselected = newval.getValue(zaxis().getAnatomicalAxis());
                        if (outOfRange(zselected)) {
                            setSlice(newval);
                        } else {
                            IImagePlot nearPlot = nearestPlot(zselected);
                            AnatomicalPoint1D nearz = nearPlot.getSlice().getValue(zaxis().getAnatomicalAxis());

                            int i = getPlots().indexOf(nearPlot);
                            if (i > 0) {
                                setSlice(sentinel.replace(new AnatomicalPoint1D(nearz.getAnatomy(), nearz.getValue() - sliceGap * i)));
                            } else {
                                setSlice(sentinel.replace(new AnatomicalPoint1D(nearz.getAnatomy(), nearz.getValue())));
                            }

                            repaintPlots();


                        }

                    }
                }
            });
        }


        private ImageAxis zaxis() {

            Axis axis = getView().getViewport().getBounds().findAxis(displayAnatomy.ZAXIS);
            return getView().getModel().getImageAxis(axis);

        }

        private void updateSlices() {
            List<IImagePlot> plotList = getPlots();

            int i = 0;
            for (IImagePlot plot : plotList) {
                AnatomicalPoint3D slice = getSliceForPlot(i);
                if (Space.containsPoint(getView().getModel().getImageSpace(), slice)) {
                    plot.setSlice(slice);
                } else {
                    System.out.println("should be clearing slice " + slice);
                    // plot.clear()

                }

                i++;
            }

        }


        public void setSlice(AnatomicalPoint3D slice) {
            if (!slice.equals(sentinel)) {
                sentinel = slice;
                updateSlices();
            }

        }

        public void nextSlice() {
            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            setSlice(sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + sliceGap)).snapToBounds());
        }

        public void previousSlice() {
            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            setSlice(sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() - sliceGap)).snapToBounds());

        }

        public void pageBack() {
            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            setSlice(sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() - (sliceGap * getPlots().size()))).snapToBounds());


        }

        public void pageForward() {

            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            setSlice(sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + (sliceGap * getPlots().size()))).snapToBounds());


        }
    }
}


