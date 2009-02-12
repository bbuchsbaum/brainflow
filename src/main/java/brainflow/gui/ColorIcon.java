package brainflow.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 16, 2007
 * Time: 12:25:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorIcon {


    public static ImageIcon createIcon(Color clr, int width, int height) {
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2 = bimg.createGraphics();
        g2.setColor(clr);
        g2.fillRect(0, 0, width, height);
        return new ImageIcon(bimg);

    }


}
