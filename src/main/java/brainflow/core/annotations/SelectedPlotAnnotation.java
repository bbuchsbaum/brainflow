package brainflow.core.annotations;

import brainflow.core.IImagePlot;
import brainflow.core.ImageView;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 16, 2007
 * Time: 3:02:59 PM
 */
public class SelectedPlotAnnotation extends AbstractAnnotation {

    public static final String ID = "plot delimiter";

    private ImageView view;

    private Stroke stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 10f, new float[]{2f, 2f}, 0f);

    private Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f);

    public SelectedPlotAnnotation(ImageView view) {
        this.view = view;
    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
         if (plot == view.getSelectedPlot()) {

            Paint oldPaint = g2d.getPaint();
            Stroke oldStroke = g2d.getStroke();
            Composite oldComposite = g2d.getComposite();

            g2d.setPaint(Color.WHITE);
            g2d.setStroke(stroke);
            g2d.setComposite(composite);
            g2d.draw(plotArea);

            g2d.setPaint(oldPaint);
            g2d.setStroke(oldStroke);
            g2d.setComposite(oldComposite);

        }

    }


    public final String getIdentifier() {
        return ID;
    }

    

    public IAnnotation safeCopy() {
        return this;
    }
}
