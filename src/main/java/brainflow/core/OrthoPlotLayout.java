package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 7, 2007
 * Time: 4:15:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrthoPlotLayout extends ImagePlotLayout {

    public enum ORIENTATION {
        HORIZONTAL,
        VERTICAL,
        TRIANGULAR
    }

    private ORIENTATION orientation = ORIENTATION.HORIZONTAL;

    private Anatomy3D leadAnatomy = Anatomy3D.getCanonicalAxial();

    private LinkedSliceController sliceController = null;

    public OrthoPlotLayout(ImageView view) {
        super(view);
    }

    public OrthoPlotLayout(ImageView view, ORIENTATION orientation) {
        super(view);
        this.orientation = orientation;
    }

    public OrthoPlotLayout(ImageView view, Anatomy3D leadAnatomy, ORIENTATION orientation) {
        super(view);
        this.orientation = orientation;
        this.leadAnatomy = leadAnatomy;
    }



    protected List<IImagePlot> createPlots() {
        Anatomy3D[] anat = leadAnatomy.getCanonicalOrthogonal();
        List<IImagePlot> plots = new ArrayList<IImagePlot>();
        plots.add(super.createPlot(leadAnatomy));
        plots.add(super.createPlot(anat[1]));
        plots.add(super.createPlot(anat[2]));
        return plots;


    }

    public LinkedSliceController createSliceController() {
        if (sliceController == null) {
            sliceController = new LinkedSliceController(getView());
        }

        return sliceController;

    }

    public Dimension getPreferredSize() {
        switch (orientation) {
            case HORIZONTAL:
                return new Dimension(3 * DEFAULT_WIDTH, DEFAULT_HEIGHT);
            case TRIANGULAR:
                return new Dimension(2 * DEFAULT_WIDTH, 2 * DEFAULT_HEIGHT);
            case VERTICAL:
                return new Dimension(DEFAULT_WIDTH, 3 * DEFAULT_HEIGHT);
            default:
                return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }


    }

    public List<IImagePlot> layoutPlots() {
        plots = createPlots();
        getView().getContentPane().removeAll();
        switch (orientation) {
            case HORIZONTAL:
                BoxLayout layout1 = new BoxLayout(getView().getContentPane(), BoxLayout.X_AXIS);
                getView().getContentPane().setLayout(layout1);
                for (IImagePlot plot : plots) {
                    getView().getContentPane().add(plot.getComponent());
                }

                break;

            case TRIANGULAR:
                FormLayout layout2a = new FormLayout("p:grow(.85), p:grow(.15)", "p:g, 1dlu:g, p:g, 1dlu:g");
                layout2a.setColumnGroups(new int[][]{{1, 2}});
                getView().getContentPane().setLayout(layout2a);
                CellConstraints cc = new CellConstraints();
                getView().getContentPane().add(plots.get(0).getComponent(), cc.xywh(1, 1, 1, 4));
                getView().getContentPane().add(plots.get(1).getComponent(), cc.xywh(2, 1, 1, 2));
                getView().getContentPane().add(plots.get(2).getComponent(), cc.xywh(2, 3, 1, 2));
                break;

            case VERTICAL:
                BoxLayout layout3 = new BoxLayout(getView().getContentPane(), BoxLayout.Y_AXIS);
                getView().getContentPane().setLayout(layout3);
                for (IImagePlot plot : plots) {
                    getView().getContentPane().add(plot.getComponent());
                }

                break;


            default:
                throw new AssertionError();
        }

        return plots;

    }

    public IImagePlot whichPlot(Point p) {

        for (IImagePlot plot : plots) {
            Point point = SwingUtilities.convertPoint(getView(), p, plot.getComponent());

            boolean inplot = !((point.x < 0) || (point.y < 0) || (point.x > plot.getComponent().getWidth()) || (point.y > plot.getComponent().getHeight()));

            if (inplot) {
                return plot;
            }
        }

        return null;


    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        Anatomy3D[] anatomy = displayAnatomy.getCanonicalOrthogonal();
        for (int i=0; i<anatomy.length; i++) {
            Anatomy3D danat = anatomy[i];
            AxisRange xrange = getView().getModel().getImageAxis(danat.XAXIS).getRange();
            AxisRange yrange = getView().getModel().getImageAxis(danat.YAXIS).getRange();
            ViewBounds vb = new ViewBounds(danat, xrange, yrange);

            plots.get(i).setViewBounds(vb);
        }
    }

    public Anatomy3D getDisplayAnatomy() {
        return plots.get(0).getDisplayAnatomy();
    }
}
