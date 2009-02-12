package brainflow.core;

import brainflow.core.annotations.CrosshairAnnotation;
import brainflow.core.annotations.SelectedPlotAnnotation;
import brainflow.core.annotations.SliceAnnotation;
import brainflow.display.ICrosshair;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.axis.AxisRange;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 2, 2007
 * Time: 2:19:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class MontageImageView extends AbstractGriddedImageView {

    private Anatomy3D displayAnatomy;

    private MontageSliceController sliceController;

    //private MontageControlPanel controlPanel;

    public MontageImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;

        sliceController = new MontageSliceController(getCursorPos().getValue(displayAnatomy.ZAXIS));

        layoutGrid();
        initLocal();
        initControlPanel();


    }

    public MontageImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy, int nrows, int ncols, double sliceGap) {
        super(imodel, nrows, ncols);
        this.displayAnatomy = displayAnatomy;

        sliceController = new MontageSliceController(getCursorPos().getValue(displayAnatomy.ZAXIS), sliceGap);
        sliceController.sliceGap = sliceGap;
     
        layoutGrid();
        initLocal();
        //initControlPanel();
    }

    private void initControlPanel() {
        //controlPanel = new MontageControlPanel();
        // add(controlPanel, BorderLayout.SOUTH);

    }


    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }


    private void initLocal() {

        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(cursorPos);

        SelectedPlotAnnotation plotAnnotation = new SelectedPlotAnnotation(this);

        SliceAnnotation sliceAnnotation = new SliceAnnotation();

        for (IImagePlot plot : getPlots()) {
            setAnnotation(plot, SelectedPlotAnnotation.ID, plotAnnotation);
            setAnnotation(plot, CrosshairAnnotation.ID, crosshairAnnotation);
            setAnnotation(plot, SliceAnnotation.ID, sliceAnnotation);

        }


    }

    public SliceController getSliceController() {
        return sliceController;
    }

    private void updateSlices() {
        List<IImagePlot> plotList = getPlots();

        int i = 0;
        for (IImagePlot plot : plotList) {
            AnatomicalPoint3D slice = sliceController.getSliceForPlot(i);
            plot.setSlice(slice);
            i++;
        }

    }

    //public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
    //    this.displayAnatomy = displayAnatomy;
    //}

    @Override
    protected IImagePlot makePlot(int index, int row, int column) {
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();


        IImagePlot plot = new ComponentImagePlot(getModel(), new ViewBounds(displayAnatomy, xrange, yrange));
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString() + row + ", " + column);

        CompositeImageProducer producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        plot.setImageProducer(producer);


        AnatomicalPoint3D nextSlice = sliceController.getSliceForPlot(index);

        plot.setSlice(nextSlice);

        return plot;

    }


    public Dimension getPreferredSize() {
        return new Dimension(150 * getNRows(), 150 * getNCols());
    }

    class CrosshairHandler implements PropertyChangeListener {


        public void propertyChange(PropertyChangeEvent evt) {
            ICrosshair cross = (ICrosshair) evt.getSource();
            AnatomicalPoint1D slice = cross.getLocation().getValue(MontageImageView.this.getSelectedPlot().getDisplayAnatomy().ZAXIS);
            double val = sliceController.nearestSlice(slice);
            if (val < .1) {
                //int index = sliceController.whichPlot(slice, .11);
                //getPlotSelection().setSelectionIndex(index);
                for (IImagePlot plot : getPlots()) {
                    // can we just call repaint rather than looping?
                    plot.getComponent().repaint();
                }
            } else {
                sliceController.setSlice(slice);
            }

        }
    }

    class MontageSliceController implements SliceController {

        private AnatomicalPoint1D sentinel;

        private double sliceGap = 4;

        private AxisRange sliceRange;


        public MontageSliceController(AnatomicalPoint1D sentinel) {
            this.sentinel = sentinel;
            sliceGap = getModel().getImageSpace().getImageAxis(displayAnatomy.ZAXIS, true).getSpacing();
            sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getValue(), sentinel.getValue() + getNumPlots() * sliceGap);
        }


        public MontageSliceController(AnatomicalPoint1D sentinel, double sliceGap) {
            this.sentinel = sentinel;
            this.sliceGap = sliceGap;
            sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getValue(), sentinel.getValue() + getNumPlots() * sliceGap);
        }

        public void setSlice(AnatomicalPoint3D slice) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public double getSliceGap() {
            return sliceGap;
        }


        public AnatomicalPoint3D getSlice() {
            return null;
            //return sentinel;
        }


        public AxisRange getSliceRange() {
            return sliceRange;
        }

        public AnatomicalPoint3D getSliceForPlot(int plotIndex) {
            return  null;
        }

        public double nearestSlice(AnatomicalPoint1D slice) {
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < getNumPlots(); i++) {
                //AnatomicalPoint1D pt = getSliceForPlot(i);
                //minDist = Math.min(Math.abs(pt.evaluate() - slice.evaluate()), minDist);
            }

            return minDist;
        }

        public int whichPlot(AnatomicalPoint1D slice, double tolerance) {
            int index = -1;
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < getNumPlots(); i++) {
                AnatomicalPoint3D pt = getSliceForPlot(i);
                double dist = Math.abs(pt.getX() - slice.getValue());
                if (dist < minDist) {
                    index = i;
                    minDist = dist;
                }
            }

            if (minDist < tolerance) {
                return index;
            } else {
                return -1;
            }


        }


        public void setSlice(AnatomicalPoint1D slice) {

            AxisRange range = getViewport().getRange(getDisplayAnatomy().ZAXIS);
            if (slice.getAnatomy() != range.getAnatomicalAxis()) {
                throw new IllegalArgumentException("illegal axis for slice argument : " + slice.getAnatomy() +
                        " -- axis should be : " + range.getAnatomicalAxis());
            }

            sentinel = slice;
            sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getValue(), sentinel.getValue() + getNumPlots() * sliceGap);
            updateSlices();
        }

        public void nextSlice() {
            setSlice(new AnatomicalPoint1D(sentinel.getAnatomy(), sentinel.getValue() + sliceGap));
        }

        public void previousSlice() {
            setSlice(new AnatomicalPoint1D(sentinel.getAnatomy(), sentinel.getValue() - sliceGap));
        }

        public void pageBack() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void pageForward() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    /*class MontageControlPanel extends JPanel {

        private JSpinner rowSpinner;

        private JSpinner columnSpinner;



        public MontageControlPanel() {
            super();

            SpinnerNumberModel rowModel = new SpinnerNumberModel(MontageImageView.this.getNRows(), 1, 5, 1);
            rowSpinner = new JSpinner(rowModel);

            SpinnerNumberModel columnModel = new SpinnerNumberModel(MontageImageView.this.getNCols(), 1, 5, 1);
            columnSpinner = new JSpinner(columnModel);

            JideBoxLayout layout = new JideBoxLayout(this, JideBoxLayout.X_AXIS);
            layout.setGap(8);
            setLayout(layout);



            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(new JLabel("Rows "), JideBoxLayout.FIX);
            add(rowSpinner, JideBoxLayout.FIX);

            add(new JLabel("Cols "), JideBoxLayout.FIX);
            add(columnSpinner, JideBoxLayout.FIX);

            rowSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    layoutGrid();
                }
            });

        }
    }  */
}
