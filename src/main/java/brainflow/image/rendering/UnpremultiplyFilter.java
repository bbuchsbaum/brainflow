package brainflow.image.rendering;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 27, 2007
 * Time: 9:09:08 PM
 */


/**
 * A filter which unpremultiplies an image's alpha.
 * Note: this does not change the image type of the BufferedImage
 */

public class UnpremultiplyFilter extends PointFilter {

    public UnpremultiplyFilter() {
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = (rgb >> 24) & 0xff;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        if (a == 0 || a == 255)
            return rgb;

        float f = 255.0f / a;
        r *= f;
        g *= f;
        b *= f;
        if (r > 255)
            r = 255;
        if (g > 255)
            g = 255;
        if (b > 255)
            b = 255;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public String toString() {
        return "Alpha/Unpremultiply";
    }
}


