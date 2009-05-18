package brainflow.core.annotations;

import brainflow.core.IImagePlot;
import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.space.IImageSpace3D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 24, 2007
 * Time: 9:25:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SliceAnnotation extends AbstractAnnotation {


    public static final String ID = "slice label";

    private Color fontColor = Color.WHITE;

    private Font font = new Font("helvetica", Font.BOLD, 13);


    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;

        g2d.setColor(fontColor);
        g2d.setFont(font);



        FontMetrics fmetric = g2d.getFontMetrics(font);

        GridPoint3D pt = plot.getSlice();
        
        String label = "" + (int)Math.round(pt.toWorld().getZ());
        Rectangle2D strBounds = fmetric.getStringBounds(label, g2d);
        g2d.drawString(label, (int)(plotArea.getX() + (plotArea.getWidth()/2) - strBounds.getWidth()/2), (int)(strBounds.getHeight() + 4));



    }

    public IAnnotation safeCopy() {
        return this;
    }

    public String getIdentifier() {
        return ID;
    }
}
