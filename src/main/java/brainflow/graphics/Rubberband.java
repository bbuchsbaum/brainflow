package brainflow.graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;


/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class Rubberband extends MouseAdapter implements MouseMotionListener  {

  boolean on = false;
  boolean dragging = false;
  boolean stopped = false;
  boolean first = true;
  Component component;
  Point start = new Point();
  Point end = new Point();
  Rectangle2D last;
  Rectangle2D current;
  ArrayList listeners = new ArrayList();
  
  Rectangle boundary;

  public Rubberband(Component _component) {
    component = _component;
    component.addMouseListener(this);
    component.addMouseMotionListener(this);
    boundary = component.getBounds();
  }

  public Component getComponent() {
    return component;
  }

  public void addActionListener(ActionListener listener) {
    listeners.add(listener);
  }

  private void fireActionPerformed() {
    for (int i=0; i<listeners.size(); i++) {
      ActionListener l = (ActionListener)listeners.get(i);
      l.actionPerformed(new ActionEvent(this, 0, "rubberbandAction"));
    }
  }

  public void setBoundary(Rectangle _boundary) {
      boundary = _boundary;
  }

  public void setActive(boolean active) {
    on = active;
  }

  public Rectangle2D getLastBounds() {
    return new Rectangle2D.Double(start.getX(), start.getY(), end.getX()-start.getX(), end.getY()-start.getY());
  }


  public void mouseDragged(MouseEvent e) {
    if (on) {
      if (first == true)
        first = false;
      dragging = true;
      draw(e.getPoint());
    }

  }

  public void draw(Point p) {
    Point cur = p;
    Graphics2D g2 = (Graphics2D)component.getGraphics();
    g2.setXORMode(Color.green);
    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

    if ( last != null )
      g2.draw(last);

    current = new Rectangle(start, new Dimension(cur.x-start.x, cur.y-start.y));
    //if (!boundary.contains(current)) {
    //    current = last;
    //}
    

    if (dragging && !stopped)
      g2.draw(current);
    last = current;
    g2.dispose();
  }



  public void mouseMoved(MouseEvent e) {}

  public void mouseReleased(MouseEvent e) {
    if (on) {
      dragging = false;
      end = e.getPoint();
      stopped = true;
      draw(end);
      double width = end.getX() - start.getX();
      double height = end.getY() - start.getY();
      if ( (width >= 1) && (height >= 1) )
        fireActionPerformed();
      last = null;

    }
  }


  public void mousePressed(MouseEvent e) {
    if (on && !dragging) {
      start = e.getPoint();
      stopped = false;

    }
  }



  public void flush() {
    /**@todo: Implement this mri.graphics.GraphicsVisitor method*/
    throw new java.lang.UnsupportedOperationException("Method flush() not yet implemented.");
  }
}