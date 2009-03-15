package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.ImageAxis;
import brainflow.image.space.Axis;

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

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private MontageSliceController sliceController = null;


    public MontagePlotLayout(ImageView view, Anatomy3D displayAnatomy, int nrows, int ncols) {
        super(view);
        this.displayAnatomy = displayAnatomy;
        this.nrows = nrows;
        this.ncols = ncols;
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

    public Dimension getPreferredSize() {
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
        getView().getContentPane().setLayout(new GridLayout(nrows, ncols, 3, 3));
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

    class MontageSliceController extends SimpleSliceController {


        private AnatomicalPoint3D sentinel;

        private double sliceGap = 3;



        MontageSliceController(ImageView imageView) {
            super(imageView);
            sliceGap = getView().getModel().getImageSpace().getImageAxis(displayAnatomy.ZAXIS, true).getSpacing();
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

        protected void initCursorListener() {
            BeanContainer.get().addListener(getView().cursorPos, new PropertyListener() {
                public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                    AnatomicalPoint3D oldval = (AnatomicalPoint3D) oldValue;
                    AnatomicalPoint3D newval = (AnatomicalPoint3D) newValue;

                    if (!oldval.equals(newval)) {
                        AnatomicalPoint1D zsentinel = sentinel.getValue(zaxis().getAnatomicalAxis());
                        AnatomicalPoint1D zselected = newval.getValue(zaxis().getAnatomicalAxis());


                        int nplots = getNcols() * getNrows();
                        if ( (zselected.getValue() > (zsentinel.getValue() + sliceGap * nplots)) || (zselected.getValue() < zsentinel.getValue())) {
                            //System.out.println("slice out of range");
                            setSlice(newval);
                        } else {
                            IImagePlot nearPlot = nearestPlot(zselected);
                            AnatomicalPoint1D nearz = nearPlot.getSlice().getValue(zaxis().getAnatomicalAxis());

                            int i = getPlots().indexOf(nearPlot);
                            if (i > 0) {
                                System.out.println("nearz greater than newval");
                                setSlice(sentinel.replace(new AnatomicalPoint1D(nearz.getAnatomy(), nearz.getValue() - sliceGap*i)));
                                //todo forced repaint is a hack to repaint cross hair when slice doesn't change
                                getView().getSelectedPlot().getComponent().repaint();
                            } else {
                                setSlice(sentinel.replace(new AnatomicalPoint1D(nearz.getAnatomy(), nearz.getValue())));
                                getView().getSelectedPlot().getComponent().repaint();
                                //selectedPlot.getComponent().repaint();
                            }



                        }

                    }
                }
            });
        }


        private ImageAxis zaxis() {
            //IImagePlot plot = getPlots().get(0);
            Axis axis = getView().getViewport().getBounds().findAxis(displayAnatomy.ZAXIS);
            return getView().getModel().getImageAxis(axis);

        }

        private void updateSlices() {
            List<IImagePlot> plotList = getPlots();

            int i = 0;
            for (IImagePlot plot : plotList) {
                AnatomicalPoint3D slice = getSliceForPlot(i);
                plot.setSlice(slice);
                i++;
            }

        }


        public void setSlice(AnatomicalPoint3D slice) {
            slice = slice.snapToBounds();

            if (!slice.equals(sentinel)) {
                sentinel = slice;
                updateSlices();
            }

        }

        public void nextSlice() {
            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            setSlice(sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + sliceGap)));
        }

        public void previousSlice() {
            AnatomicalPoint1D pt = sentinel.getValue(zaxis().getAnatomicalAxis());
            setSlice(sentinel.replace(new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() - sliceGap)));

        }

        public void pageBack() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void pageForward() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}



