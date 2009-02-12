package brainflow.gui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 26, 2008
 * Time: 8:21:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrettySlider extends JPanel {


    private int trackHeight = 18;

    private int trackWidth = 150;

    private int arcSize = (int)Math.round(.13 * (double)trackWidth);

    public AdjustArc arcPanel = new AdjustArc();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.DARK_GRAY.brighter());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        int arcSize = (int)Math.round(.13 * (double)trackWidth);

        RoundRectangle2D r = new RoundRectangle2D.Double(2, 2, trackWidth, trackHeight, arcSize, arcSize);
        Color lg = new Color(233, 233, 233);
        Color bg = new Color(200,200, 200);


        GradientPaint gpaint = new GradientPaint(0, trackHeight, lg, 0, 0, bg, true);
        g2.setPaint(gpaint);
        g2.fill(r);


        //RoundRectangle2D track = new RoundRectangle2D.Double(2,2, trackWidthSlider, trackHeightSlider, 20, 5);


        //g2.fill(track);

        Ellipse2D ball = new Ellipse2D.Double((double)trackWidth/2.0, 2, trackHeight, trackHeight);
        //g2.setColor(Color.WHITE);
        //g2.draw(ball);


        gpaint = new GradientPaint((float)ball.getX(), 0, new Color(88,88,88) , (float)(ball.getX() + ball.getWidth()), 0, Color.BLACK, true);
        g2.setPaint(gpaint);
        g2.fill(ball);


        

    }

    public AdjustArc getArcPanel() {
        return arcPanel;
    }



    public int getTrackHeight() {
        return trackHeight;
    }

    public void setTrackHeight(int trackHeight) {
        this.trackHeight = trackHeight;
        repaint();
    }

    public int getTrackWidth() {
        return trackWidth;
    }

    public void setTrackWidth(int trackWidth) {
        this.trackWidth = trackWidth;
        repaint();
    }



    class AdjustArc extends JPanel {

        JSlider trackWidthSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, trackWidth);
        JSlider trackHeightSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, trackHeight);


        AdjustArc() {

            Box box1 = new Box(BoxLayout.X_AXIS);
            Box box2 = new Box(BoxLayout.X_AXIS);

            box1.add(new JLabel("Arc Width: "));
            box1.add(trackWidthSlider);

            box2.add(new JLabel("Arc Height: "));
            box2.add(trackHeightSlider);


            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

            setLayout(layout);

            add(box1);
            add(box2);

            trackWidthSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    System.out.println("change event");
                    PrettySlider.this.setTrackWidth(trackWidthSlider.getValue());
                }
            });

            trackHeightSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    System.out.println("change event");
                    PrettySlider.this.setTrackHeight(trackHeightSlider.getValue());
                }
            });

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 JFrame jf = new JFrame();
                PrettySlider slider = new PrettySlider();

                slider.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                jf.add(slider, BorderLayout.CENTER);
                jf.add(slider.getArcPanel(), BorderLayout.SOUTH);
                jf.setSize(300,300);
                jf.setVisible(true);
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }
}
