package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 7, 2007
 * Time: 1:41:13 PM
 * To change this template use File | Settings | File Templates.
 */


public class SimplePlotLayout extends ImagePlotLayout {


    private Anatomy3D displayAnatomy;

    private SliceController sliceController;

    public SimplePlotLayout(ImageView view, Anatomy3D displayAnatomy) {
        super(view);
        this.displayAnatomy = displayAnatomy;
    }

    

    protected List<IImagePlot> createPlots() {
        List<IImagePlot> plots = new ArrayList<IImagePlot>();
        IImagePlot plot = createPlot(displayAnatomy);
        plots.add(plot);
        

        return plots;


    }

    public List<IImagePlot> layoutPlots() {
       plots = createPlots();

       getView().getContentPane().setLayout(new BorderLayout());
       getView().getContentPane().removeAll();
       getView().getContentPane().add(plots.get(0).getComponent(), BorderLayout.CENTER);
       getView().revalidate();

       return plots;
    }

    public SliceController createSliceController() {
        if (sliceController == null) {
            sliceController = new SimpleSliceController(getView());
        }

        return sliceController;
    }

    public IImagePlot whichPlot(Point p) {
       Point point = SwingUtilities.convertPoint(getView(), p, plots.get(0).getComponent());

        boolean inplot = !((point.x < 0) || (point.y < 0) || (point.x > getView().getWidth()) || (point.y > getView().getHeight()));
        if (inplot) {
            return plots.get(0);
        } else {
            return null;
        }
       
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
        AxisRange xrange = getView().getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getView().getModel().getImageAxis(displayAnatomy.YAXIS).getRange();
        ViewBounds vb = new ViewBounds(displayAnatomy, xrange, yrange);

        plots.get(0).setViewBounds(vb);
       
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public Dimension getPreferredSize() {
        return plots.get(0).getPreferredSize();
    }
}
