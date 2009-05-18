package brainflow.image.data;

import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.rendering.RenderUtils;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 12:21:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class RGBAImage {


    private final UByteImageData2D red;

    private final UByteImageData2D green;

    private final UByteImageData2D blue;

    private final UByteImageData2D alpha;

    private final IImageData2D source;

    public RGBAImage(IImageData2D source, UByteImageData2D red, UByteImageData2D green, UByteImageData2D blue, UByteImageData2D alpha) {
        if (!checkSpace(red.getImageSpace(),
                green.getImageSpace(),
                blue.getImageSpace(),
                alpha.getImageSpace())) {
            throw new IllegalArgumentException("All image bands mut have equivalent image spaces");
        }

        this.source = source;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /*public RGBAImage(ImageSpace2D space) {
        this.red = new UByteImageData2D(space);
        this.green = new UByteImageData2D(space);
        this.blue = new UByteImageData2D(space);
        this.alpha = new UByteImageData2D(space);
    }*/


    public IImageData2D getSource() {
        return source;
    }

    private boolean checkSpace(IImageSpace... spaces) {
        for (int i = 1; i < spaces.length; i++) {
            if (!spaces[i].equals(spaces[i - 1])) {
                return false;
            }
        }

        return true;

    }

    public int getWidth() {
        return red.getImageSpace().getDimension(Axis.X_AXIS);
    }

    public int getHeight() {
        return red.getImageSpace().getDimension(Axis.Y_AXIS);
    }



    public final byte getRed(int x, int y) {
        return red.get(x, y);
    }

    public final byte getGreen(int x, int y) {
        return green.get(x, y);
    }

    public final byte getBlue(int x, int y) {
        return blue.get(x, y);
    }

    public final byte getAlpha(int x, int y) {
        return alpha.get(x, y);
    }

    public BufferedImage getAsBufferedImage() {
        byte[] br = getRed().getByteArray();
        byte[] bg = getGreen().getByteArray();
        byte[] bb = getBlue().getByteArray();
        byte[] ba = getAlpha().getByteArray();

        byte[][] ball = new byte[4][];
        ball[0] = br;
        ball[1] = bg;
        ball[2] = bb;
        ball[3] = ba;
        return RenderUtils.createInterleavedBufferedImage(ball, getWidth(), getHeight(), false);

    }

    public UByteImageData2D getRed() {
        return red;
    }

    public UByteImageData2D getGreen() {
        return green;
    }

    public UByteImageData2D getBlue() {
        return blue;
    }

    public UByteImageData2D getAlpha() {
        return alpha;
    }

    

}
