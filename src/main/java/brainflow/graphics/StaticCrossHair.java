package brainflow.graphics;



import java.awt.Graphics;
import java.awt.*;
import java.awt.geom.*;

/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class StaticCrossHair implements GraphicsVisitor {

  float radius = 75;
  float gap = 6;

  Point point;
  float alpha = 1f;
  float thickness = 2.5f;
  Color lineColor = Color.red;
  Rectangle2D bounds = null;

  boolean enabled=true;

  Component component;

  public StaticCrossHair(Component c, float _radius) {
    radius = _radius;
    lineColor = lineColor.brighter().brighter().brighter().brighter().brighter().brighter();
    component = c;
  }

  public StaticCrossHair(Component c) {
    component = c;
  }

  public void setCentroid(Point xy) {
    point = xy;
  }

  public void setAlpha(float a) {
    alpha = a;
  }

  public void setRadius(float r) {
    radius = r;
  }

  public void setBounds(Rectangle2D rect) {
    bounds = rect;
  }

  public void setEnabled(boolean b) {
    enabled = b;
  }


  public void drawObject(Graphics g) {
    if (!enabled)
      return;
    if (point == null)
      return;

    Graphics2D g2 = (Graphics2D)g;
    bounds = component.getBounds();
    if (bounds != null) {
     g2.clip(bounds);
    }



    g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
   // AlphaComposite comp = AlphaComposite.get(AlphaComposite.SRC_OVER, alpha);
  //  g2.setComposite(comp);

    Line2D bvert = new Line2D.Double(point.getX(), point.getY()+gap, point.getX(), point.getY()+radius);
    Line2D tvert = new Line2D.Double(point.getX(), point.getY()-radius, point.getX(), point.getY()-gap);

    Line2D lhorz = new Line2D.Double(point.getX()-radius, point.getY(), point.getX()-gap, point.getY());
    Line2D rhorz = new Line2D.Double(point.getX()+gap, point.getY(), point.getX()+radius, point.getY());

    Color c1 = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 255);
    Color c2 = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 0);

    g2.setPaint(lineColor);

    GradientPaint gp = new GradientPaint((float)point.getX(), (float)point.getY()+gap, c1, (float)point.getX(), (float)point.getY()+radius, c2, false);
    //g2.setPaint(gp);
    g2.draw(bvert);
    gp = new GradientPaint((float)point.getX(), (float)point.getY()-radius, c2, (float)point.getX(), (float)point.getY()-gap, c1, false);
    //g2.setPaint(gp);
    g2.draw(tvert);
    gp = new GradientPaint((float)point.getX()-radius, (float)point.getY(), c2, (float)point.getX()-gap, (float)point.getY(), c1, false);
    //g2.setPaint(gp);
    g2.draw(lhorz);
    gp = new GradientPaint((float)point.getX()+gap, (float)point.getY(), c1, (float)point.getX()+radius, (float)point.getY(), c2, false);
   // g2.setPaint(gp);
    g2.draw(rhorz);


  }

  public void flush() {

  }
}