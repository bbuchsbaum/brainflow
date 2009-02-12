package brainflow.image.rendering;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 27, 2007
 * Time: 9:08:26 PM
 */

/**
 * A filter which premultiplies an image's alpha.
 * Note: this does not change the image type of the BufferedImage
 */
public class PremultiplyFilter extends PointFilter {

    float multiplier = 1.0f / 255.0f;

    public PremultiplyFilter() {
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = (rgb >> 24) & 0xff;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        float f = a * multiplier;
        r *= f;
        g *= f;
        b *= f;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public String toString() {
        return "Alpha/Premultiply";
    }
}


