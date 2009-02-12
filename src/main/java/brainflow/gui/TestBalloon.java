package brainflow.gui;

import com.jidesoft.tooltip.BalloonTip;
import com.jidesoft.tooltip.shapes.RoundedRectangularBalloonShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 22, 2008
 * Time: 2:25:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBalloon {


    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        //To change body of implemented methods use File | Settings | File Templates.
                        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
                        JFrame frame = new JFrame();
                        JPanel jp = new JPanel();
                        final JButton butt = new JButton("press me");
                        jp.add(butt);

                        butt.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JPanel panel = new JPanel(new BorderLayout());
                                panel.setBorder(BorderFactory.createEmptyBorder(2, 10, 1, 10));

                                JLabel lab = new JLabel("I am a God!");
                                lab.setForeground(Color.WHITE);
                                panel.add(lab);
                                panel.setOpaque(false);

                                RoundedRectangularBalloonShape shape = new com.jidesoft.tooltip.shapes.RoundedRectangularBalloonShape();
                                shape.setCornerSize(12);
                                //shape.setBalloonSizeRatio(2);
                                BalloonTip tip = new BalloonTip(panel) {
                                    public void paintBalloonBackground(Graphics2D graphics2D, Shape shape) {
                                        GradientPaint p = new GradientPaint(0,0, Color.BLACK, 0, 100, Color.GRAY);
                                        graphics2D.setPaint(p);
                                        graphics2D.fill(shape);
                                    }

                                    public void paintBalloonForeground(Graphics2D graphics2D, Shape shape) {
                                        //super.paintBalloonForeground(graphics2D, shape);    //To change body of overridden methods use File | Settings | File Templates.
                                    }
                                };


                                tip.setBackground(Color.DARK_GRAY);
                                tip.setForeground(Color.DARK_GRAY);

                                tip.setBalloonShape(shape);
                                tip.setPreferredSize(new Dimension(600, 600));
                                tip.show(butt, 0, 0);

                            }
                        });
                        frame.add(jp, BorderLayout.SOUTH);



                        frame.setSize(600,600);
                        frame.setVisible(true);
                    }

                });
    }
}

