package brainflow.display;

import cern.jet.random.Normal;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 5:18:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmoothingOp {

    public static final String OP_NAME = "smoothing_op";

    float[] kernel = new float[]{.11f, .11f, .11f, .11f, .11f, .11f, .11f, .11f, .11f};
    float radius = 1;
    Normal normal;


    public SmoothingOp(float radius) {
        this.radius = radius;
    }


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public BufferedImage filter(BufferedImage input) {
        BufferedImage bimg = null;

        if (!(input instanceof BufferedImage)) {
            bimg = new BufferedImage(input.getWidth(),
                    input.getHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);

            bimg.createGraphics().drawRenderedImage(input,
                    AffineTransform.getTranslateInstance(-input.getMinX(), -input.getMinY()));
        } else {

            bimg = input;
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage destImage = gf.createCompatibleImage((int) input.getWidth(), (int) input.getHeight());


        ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, kernel));
        return cop.filter(bimg, destImage);
    }


    public float[] makeKernel(double radius) {
        radius += 1;
        int size = (int) radius * 2 + 1;
        float[] kernel = new float[size];

        for (int i = 0; i < size; i++)
            kernel[i] = (float) Math.exp(-0.5 * (Math.sqrt((i - radius) / (radius * 2))) / Math.sqrt(0.2));
        float[] kernel2 = new float[size - 2];
        for (int i = 0; i < size - 2; i++)
            kernel2[i] = kernel[i + 1];
        if (kernel2.length == 1)
            kernel2[0] = 1f;
        
        return kernel2;
    }


    public static void main(String[] args) {

    }
}
