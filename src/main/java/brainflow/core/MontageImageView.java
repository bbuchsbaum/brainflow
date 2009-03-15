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
public class MontageImageView extends ImageView {

    private Anatomy3D displayAnatomy;

    private int nrows = 3;

    private int ncols = 3;


    private MontagePlotLayout plotLayout;

    //private MontageControlPanel controlPanel;

    public MontageImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;
        //initControlPanel();
        init();

    }

    public MontageImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy, int nrows, int ncols, double sliceGap) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;
        this.nrows= nrows;
        this.ncols = ncols;

        init();
        //initControlPanel();
    }

     private void init() {
        layoutPlots();
    }

    public int getNrows() {
        return nrows;
    }

    public int getNcols() {
        return ncols;
    }

    protected MontagePlotLayout createPlotLayout(Anatomy3D displayAnatomy) {
        plotLayout = new MontagePlotLayout(this, displayAnatomy, nrows, ncols);
        return plotLayout;
    }



    private void initControlPanel() {
        //controlPanel = new MontageControlPanel();
        // add(controlPanel, BorderLayout.SOUTH);

    }

    protected void layoutPlots() {
        plotLayout = createPlotLayout(displayAnatomy);
        resetPlotLayout(plotLayout);
    }

    public MontagePlotLayout getPlotLayout() {
        return plotLayout;
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }



    


    public Dimension getPreferredSize() {
        return new Dimension(150 * getNrows(), 150 * getNcols());
    }

    /*class CrosshairHandler implements PropertyChangeListener {


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
    }*/


        //public double nearestSlice(AnatomicalPoint1D slice) {
        //    double minDist = Double.MAX_VALUE;
            //for (int i = 0; i < getNumPlots(); i++) {
                //AnatomicalPoint1D pt = getSliceForPlot(i);
                //minDist = Math.min(Math.abs(pt.evaluate() - slice.evaluate()), minDist);
           // }

        //    return minDist;
        //}

        //public int whichPlot(AnatomicalPoint1D slice, double tolerance) {
        //    int index = -1;
        //    double minDist = Double.MAX_VALUE;
            //for (int i = 0; i < getNumPlots(); i++) {
           //     AnatomicalPoint3D pt = getSliceForPlot(i);
            //    double dist = Math.abs(pt.getX() - slice.getValue());
            //    if (dist < minDist) {
            //        index = i;
            //        minDist = dist;
            //    }
           // }

          //  if (minDist < tolerance) {
          //      return index;
           // } else {
           //     return -1;
           // }


       // }


        /*public void setSlice(AnatomicalPoint1D slice) {

            AxisRange range = getViewport().getRange(getDisplayAnatomy().ZAXIS);
            if (slice.getAnatomy() != range.getAnatomicalAxis()) {
                throw new IllegalArgumentException("illegal axis for slice argument : " + slice.getAnatomy() +
                        " -- axis should be : " + range.getAnatomicalAxis());
            }

            sentinel = slice;
           // sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getValue(), sentinel.getValue() + getNumPlots() * sliceGap);
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
    } */

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
