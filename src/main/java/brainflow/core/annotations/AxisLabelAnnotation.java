package brainflow.core.annotations;

import brainflow.core.IImagePlot;
import brainflow.image.anatomy.AnatomicalAxis;
import org.jfree.text.TextUtilities;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 13, 2007
 * Time: 7:13:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AxisLabelAnnotation extends AbstractAnnotation {

    private Paint textPaint = Color.WHITE;

    private int axisLineXStart = 5;
    private int axisLineYStart = 5;

    private int xFontStart = axisLineXStart;
    private int yFontStart = axisLineYStart;

    public static final String ID = "axis label";

    private Font font = new Font("helvetica", Font.ITALIC | Font.BOLD, 14);


    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) {
            return;
        }

        AnatomicalAxis xaxis = plot.getXAxisRange().getAnatomicalAxis();
        AnatomicalAxis yaxis = plot.getYAxisRange().getAnatomicalAxis();


        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fmetric = g2d.getFontMetrics(font);


        String xlabel = xaxis.getMinDirection().toString().subSequence(0,1) + "  to  " + xaxis.getMaxDirection().toString().subSequence(0,1);
        String ylabel = yaxis.getMinDirection().toString().subSequence(0,1) + "  to  " + yaxis.getMaxDirection().toString().subSequence(0,1);

        Rectangle2D xbounds = fmetric.getStringBounds(xlabel, g2d);
        Rectangle2D ybounds = fmetric.getStringBounds(ylabel, g2d);
        int stringLen = (int) Math.max(xbounds.getWidth(), ybounds.getWidth());
        int fontHeight = (int) Math.max(xbounds.getHeight(), ybounds.getHeight());

        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(textPaint);
        g2d.drawString(xlabel, xFontStart + fontHeight, fontHeight);


        int lineLength = 0;//(int) (Math.min(plotArea.getWidth(), plotArea.getHeight()) / 1.5);
        lineLength = Math.min(stringLen, 200);
        lineLength = Math.max(lineLength, 75);

        drawArrow(g2d, fontHeight + axisLineXStart, fontHeight + axisLineYStart, fontHeight + axisLineXStart + lineLength, fontHeight + axisLineYStart, 8);
        drawArrow(g2d, fontHeight + axisLineXStart, fontHeight + axisLineYStart, fontHeight + axisLineXStart, fontHeight + axisLineYStart + lineLength, 8);


        TextUtilities.drawRotatedString(ylabel, g2d, Math.PI / 2, yFontStart, axisLineYStart + fontHeight);


        g2d.setPaint(oldPaint);

    }


    public final String getIdentifier() {
        return AxisLabelAnnotation.ID;
    }

    public AxisLabelAnnotation safeCopy() {
        AxisLabelAnnotation annot = new AxisLabelAnnotation();
        annot.axisLineXStart = axisLineXStart;
        annot.axisLineYStart = axisLineYStart;
        annot.font = new Font(font.getName(), font.getStyle(), font.getSize());
        return annot;

    }

    /**
     * Draws an arrow on the given Graphics2D context
     *
     * @param g  The Graphics2D context to draw on
     * @param x  The zero location of the "tail" of the arrow
     * @param y  The zero location of the "tail" of the arrow
     * @param xx The zero location of the "head" of the arrow
     * @param yy The zero location of the "head" of the arrow
     */
    private void drawArrow(Graphics2D g, int x, int y, int xx, int yy, float arrowWidth) {

        float theta = 0.423f;
        float[] xPoints = new float[3];
        float[] yPoints = new float[3];
        float[] vecLine = new float[2];
        float[] vecLeft = new float[2];
        float fLength;
        float th;
        float ta;
        float baseX, baseY;

        xPoints[0] = xx;
        yPoints[0] = yy;

        // build the line vector
        vecLine[0] = (float) xPoints[0] - x;
        vecLine[1] = (float) yPoints[0] - y;

        // build the arrow base vector - normal to the line
        vecLeft[0] = -vecLine[1];
        vecLeft[1] = vecLine[0];

        // setup length parameters
        fLength = (float) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1]);
        th = arrowWidth / (2.0f * fLength);
        ta = arrowWidth / (2.0f * ((float) Math.tan(theta) / 2.0f) * fLength);

        // find the base of the arrow
        baseX = ((float) xPoints[0] - ta * vecLine[0]);
        baseY = ((float) yPoints[0] - ta * vecLine[1]);

        // build the points on the sides of the arrow
        xPoints[1] = (int) (baseX + th * vecLeft[0]);
        yPoints[1] = (int) (baseY + th * vecLeft[1]);
        xPoints[2] = (int) (baseX - th * vecLeft[0]);
        yPoints[2] = (int) (baseY - th * vecLeft[1]);

        Line2D line = new Line2D.Double(x, y, baseX, baseY);
        g.draw(line);

        GeneralPath path = new java.awt.geom.GeneralPath();
        path.moveTo(xPoints[0], yPoints[0]);
        path.lineTo(xPoints[1], yPoints[1]);
        path.lineTo(xPoints[2], yPoints[2]);

        g.fill(path);
    }
}
