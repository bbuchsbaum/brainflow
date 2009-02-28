package brainflow.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 26, 2009
 * Time: 8:50:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class XAxis {

    private double min;

    private double max;

    private Stroke lineStroke = new BasicStroke(1.2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);

    private int xoffset = 7;

    private int yoffset = 5;

    private int tickHeight = 3;

    private int nticks = 6;

    private Font font = new Font("helvetica", Font.PLAIN, 9);




    public XAxis(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getNticks() {
        return nticks;
    }

    public void setNticks(int nticks) {
        this.nticks = nticks;
    }

    public int getTickHeight() {
        return tickHeight;
    }

    public void setTickHeight(int tickHeight) {
        this.tickHeight = tickHeight;
    }

    public int getYoffset() {
        return yoffset;
    }

    public void setYoffset(int yoffset) {
        this.yoffset = yoffset;
    }

    public int getXoffset() {
        return xoffset;
    }

    public void setXoffset(int xoffset) {
        this.xoffset = xoffset;
    }

    public Stroke getLineStroke() {
        return lineStroke;
    }

    public void setLineStroke(Stroke lineStroke) {
        this.lineStroke = lineStroke;
    }

    public void draw(Graphics2D g2, Rectangle rect) {
        
        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float ystart = yoffset;
        float xstart = rect.x + xoffset;

        float width = (float)rect.getWidth() - (xoffset*2);

        Line2D line = new Line2D.Float(xstart, ystart, xstart + width, ystart);
        g2.setStroke(lineStroke);
        g2.setColor(Color.DARK_GRAY.brighter());
        g2.draw(line);

        //g2.setColor(Color.BLACK);
        float[] ticks = getTickLocations(nticks, xstart, xstart + width);
        for (int i=0; i<ticks.length; i++) {

            drawTick(g2, ticks[i], ystart, tickHeight);
        }

        String[] labels = getTickLabels(ticks, xstart, xstart + width);
        float xend = ticks[0];
        for (int i=0; i<labels.length; i++) {

            if (xend > ticks[i] && i < (ticks.length-1) ) {
                xend = ticks[i+1];

            } else {
                xend = drawLabel(g2,labels[i], i, ticks[i], ystart);
            }

        }



    }

    private int drawLabel(Graphics2D g2, String label, int tickindex, float tickloc, float ystart) {
        g2.setFont(g2.getFont().deriveFont(8));
        FontMetrics fmetric = g2.getFontMetrics();
        Rectangle2D strbounds = fmetric.getStringBounds(label, g2);
        int stringLen = (int) strbounds.getWidth();
        int fontHeight = (int) strbounds.getHeight();
        if (tickindex == 0) {
            // if first tick, draw label to left of tick
            g2.drawString(label, tickloc, ystart + tickHeight + fontHeight + 1);
        } else if (tickindex == (nticks-1)) {

            g2.drawString(label, tickloc - stringLen, ystart + tickHeight + fontHeight + 1);
        } else {
            g2.drawString(label, (float)(tickloc - stringLen/2.0), ystart + tickHeight + fontHeight + 1);
        }

        return (int)(tickloc + stringLen);
    }


    private void drawTick(Graphics2D g2, float tick_x, float tick_y, float tickheight) {
        Line2D tick = new Line2D.Float(tick_x, tick_y, tick_x, tick_y+tickheight);
        g2.draw(tick);

    }

    private String[] getTickLabels(float[] ticks, float xmin, float xmax) {
        String[] ret = new String[ticks.length];
        for (int i=0; i<ticks.length; i++) {
            float perc = (ticks[i] - xmin)/(xmax-xmin);
            int val = (int)Math.round(perc * (max-min) + min);
            ret[i] = String.valueOf(val);
        }

        return ret;
    }

    private float[] getTickLocations(int nticks, float xmin, float xmax) {
        if (nticks < 2) throw new IllegalArgumentException("must have at least 2 ticks");
        float[] res = new float[nticks];

        float gap = (xmax - xmin)/(float)(nticks-1);
        res[0] = xmin;
        res[nticks-1] = (float)Math.floor(xmax)-.5f;

        for (int i=1; i<res.length-1; i++) {
            res[i] = res[i-1] + gap;
        }

        return res;

      

    }

    public static void main(String[] args) {
        JPanel jp = new JPanel() {
            XAxis axis = new XAxis(0,32444);
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                axis.draw((Graphics2D)g, getBounds());
            }
        };

        jp.setPreferredSize(new Dimension(300,80));

        JFrame jf = new JFrame();
        jf.add(jp);
        jf.pack();
        jf.setVisible(true);

    }



}
