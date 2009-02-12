package brainflow.graphics;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class GraphicalPointer {

  public static final int POINTING_DOWN = 0;

  float base;
  float height;

  GeneralPath gpath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

  Paint fillPaint = Color.GREEN;

  int curX;
  int curY;

  public GraphicalPointer(double _base, double _height) {
    base = (float)_base;
    height = (float)_height;

  }

  public void setFillPaint(Paint paint) {
    fillPaint = paint;
  }

  public void draw(Graphics2D g2, int x, int y) {
    gpath = new GeneralPath();
    gpath.moveTo(x-base/2f, (float)y);
    gpath.lineTo(x+base/2f, (float)y);
    gpath.lineTo(x, y+height);
    gpath.lineTo(x-base/2, y);

    Paint prev = g2.getPaint();
    g2.setPaint(fillPaint);

    g2.fill(gpath);

    g2.setPaint(prev);
    curX = x;
    curY = y;

  }

  public boolean inside(int x, int y) {
    if (gpath == null)
      return false;
    return gpath.contains(x,y);
  }
  
  public static void main(String[] args) {
      JFrame jf = new JFrame();
      
      JPanel jp = new JPanel() {
          public void paintComponent(Graphics g) {
              Graphics2D g2 = (Graphics2D)g;
              GraphicalPointer gp = new GraphicalPointer(25,25);
              gp.draw(g2, 50,50);
          }
      };
      
      jf.setSize(100, 100);
      jf.getContentPane().add(jp);
      jf.pack();
      jf.setVisible(true);
  }
      
      








}