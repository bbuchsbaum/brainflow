package brainflow.utils;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */



public class DisplayJAI extends JPanel
                        implements MouseListener,
                                   MouseMotionListener {

    /** operations to display */
    protected RenderedImage source = null;

    /** operations origin relative to panel origin */
    protected int originX = 0;
    protected int originY = 0;


    /** default constructor */
    public DisplayJAI() {
        super();
        setLayout(null);
    }

    /** constructor with given operations */
    public DisplayJAI(RenderedImage image) {
        super();
        setLayout(null);
        source = image;

        // Swing geometry
        int w = source.getWidth();
        int h = source.getHeight();
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right,
                                      h + insets.top + insets.bottom);


        setPreferredSize(dim);
    }

    /** move operations within it's container */
    public void setOrigin(int x, int y) {
        originX = x;
        originY = y;
        repaint();
    }

    /** get the operations origin */
    public Point getOrigin() {
        return new Point(originX, originY);
    }

    /** use to display a new operations */
    public void set(RenderedImage im) {
        source = im;

        // Swing geometry
        int w = source.getWidth();
        int h = source.getHeight();
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right,
                                      h + insets.top + insets.bottom);


        setPreferredSize(dim);
        revalidate();
        repaint();
    }

    /** use to display a new operations, with origins */
    public void set(RenderedImage im, int x, int y) {
        source = im;

        // Swing geometry
        int w = source.getWidth();
        int h = source.getHeight();
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right,
                                      h + insets.top + insets.bottom);

        setPreferredSize(dim);

        originX = x;
        originY = y;

        revalidate();
        repaint();
    }

    /** paint routine */
    public synchronized void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        // empty component (no operations)
        if ( source == null ) {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        // account for borders
        Insets insets = getInsets();
        int tx = insets.left + originX;
        int ty = insets.top  + originY;

        // clear damaged component area
        Rectangle clipBounds = g2d.getClipBounds();
        g2d.setColor(getBackground());
        g2d.fillRect(clipBounds.x,
                     clipBounds.y,
                     clipBounds.width,
                     clipBounds.height);

        /**
            Translation moves the entire operations within the container
        */
        try {
            g2d.drawRenderedImage(source,
                                  AffineTransform.getTranslateInstance(tx, ty));
        } catch( OutOfMemoryError e ) {
        }
    }

    /** mouse event handlers */
    public void mousePressed(MouseEvent e)  { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseMoved(MouseEvent e)    { }
    public void mouseDragged(MouseEvent e)  { }
    public void mouseEntered(MouseEvent e)  { }
    public void mouseExited(MouseEvent e)   { }
    public void mouseClicked(MouseEvent e)  { }
}

