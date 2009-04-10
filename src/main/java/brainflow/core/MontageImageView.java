package brainflow.core;

import brainflow.core.annotations.CrosshairAnnotation;
import brainflow.core.annotations.SelectedPlotAnnotation;
import brainflow.core.annotations.SliceAnnotation;
import brainflow.core.annotations.IAnnotation;
import brainflow.display.ICrosshair;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.axis.AxisRange;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.NumberFormat;

import com.jidesoft.swing.JideBoxLayout;

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

    private float sliceGap;

    private MontagePlotLayout plotLayout;

    //private MontageControlPanel controlPanel;

    public MontageImageView(ImageViewModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;

        init();
        initControlPanel();

    }

    public MontageImageView(ImageViewModel imodel, Anatomy3D displayAnatomy, int nrows, int ncols, float sliceGap) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;
        this.nrows= nrows;
        this.ncols = ncols;
        this.sliceGap = sliceGap;

        init();
        initControlPanel();
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

    public float getSliceGap() {
        return sliceGap;
    }

    public void setSliceGap(float sliceGap) {
        this.sliceGap = sliceGap;
        plotLayout.setSliceGap(sliceGap);
    }

    protected MontagePlotLayout createPlotLayout(Anatomy3D displayAnatomy) {
        /*if (plotLayout != null) {
            List<IImagePlot> plots = getPlots();
            if (plots.size() > 0) {
                Map<String, IAnnotation> annotationMap =  plots.get(0).getAnnotations();
        }*/


        plotLayout = new MontagePlotLayout(this, displayAnatomy, nrows, ncols);
        return plotLayout;
    }



    private void initControlPanel() {
       MontageControlPanel  controlPanel = new MontageControlPanel();
       add(controlPanel, BorderLayout.SOUTH);

    }

    protected void layoutPlots() {
        Map<String, IAnnotation> annotationMap = new HashMap<String, IAnnotation>();

        if (plotLayout != null) {
            List<IImagePlot> plots = getPlots();
            if (plots.size() > 0) {
                annotationMap =  plots.get(0).getAnnotations();
            }
        }

        plotLayout = createPlotLayout(displayAnatomy);
        resetPlotLayout(plotLayout, annotationMap);
    }

    public MontagePlotLayout getPlotLayout() {
        return plotLayout;
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }


    public Dimension getPreferredSize() {
        int width = (int)getModel().getImageAxis(displayAnatomy.XAXIS).getExtent();
        int height = (int)getModel().getImageAxis(displayAnatomy.YAXIS).getExtent();
        return new Dimension(width * getNcols(), height * getNrows());
    }



    class MontageControlPanel extends JPanel {

        private JSpinner rowSpinner;

        private JSpinner columnSpinner;

        private JFormattedTextField sliceGapField;

        public MontageControlPanel() {
            super();

            SpinnerNumberModel rowModel = new SpinnerNumberModel(MontageImageView.this.getNrows(), 1, 5, 1);
            rowSpinner = new JSpinner(rowModel);

            SpinnerNumberModel columnModel = new SpinnerNumberModel(MontageImageView.this.getNcols(), 1, 5, 1);
            columnSpinner = new JSpinner(columnModel);

            sliceGapField = new JFormattedTextField(NumberFormat.getNumberInstance());
            sliceGapField.setValue(getSliceGap());
            sliceGapField.setColumns(10);

            JideBoxLayout layout = new JideBoxLayout(this, JideBoxLayout.X_AXIS);
            layout.setGap(8);
            setLayout(layout);



            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            add(new JLabel("Rows "), JideBoxLayout.FIX);
            add(rowSpinner, JideBoxLayout.FIX);

            add(new JLabel("Cols "), JideBoxLayout.FIX);
            add(columnSpinner, JideBoxLayout.FIX);

            add(sliceGapField, JideBoxLayout.FIX);

            rowSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    nrows = ((Number)rowSpinner.getValue()).intValue();
                    layoutPlots();
                }
            });

            columnSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    ncols = ((Number)columnSpinner.getValue()).intValue();
                    layoutPlots();
                }
            });

            sliceGapField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Number num = (Number)sliceGapField.getValue();
                    setSliceGap(num.floatValue());
                }
            });

        }
    }
}
