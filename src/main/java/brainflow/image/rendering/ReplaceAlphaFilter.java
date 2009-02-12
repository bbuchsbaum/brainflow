package brainflow.image.rendering;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 27, 2007
 * Time: 10:06:30 PM
 */
public class ReplaceAlphaFilter extends PointFilter {

    private BufferedImage alpha;
    private Raster alphaRaster;

    public ReplaceAlphaFilter(BufferedImage alpha) {
        this.alpha = alpha;
        alphaRaster = alpha.getRaster();
    }

    public int filterRGB(int x, int y, int rgb) {
        int salpha = alphaRaster.getSample(x, y, 0);

        int a = (rgb >> 24) & 0xff;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;

        return (salpha << 24) | (r << 16) | (g << 8) | b;

    }
}
