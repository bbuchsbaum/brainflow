package brainflow.core.rendering;

import brainflow.core.layer.ImageLayer2D;
import brainflow.image.data.RGBAImage;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 4:44:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PipelineLayer {

    private ImageLayer2D layer;

    private RGBAImage coloredImage;

    private RGBAImage maskedColoredImage;

    private BufferedImage rawImage;

    private BufferedImage resampledImage;

    public PipelineLayer(ImageLayer2D _layer) {
        layer = _layer;
    }

    public void clear() {
        layer = null;
        coloredImage = null;
        maskedColoredImage = null;
        rawImage = null;
        resampledImage = null;
    }

    public boolean isVisible() {
        return layer.isVisible();
    }

    public ImageLayer2D getLayer() {
        return layer;
    }

    public double getOpacity() {
        return layer.getOpacity();
    }


    public RGBAImage getColoredImage() {
        return coloredImage;
    }

    public void setColoredImage(RGBAImage coloredImage) {
        this.coloredImage = coloredImage;
    }

    public BufferedImage getRawImage() {
        return rawImage;
    }

    public void setRawImage(BufferedImage rawImage) {
        this.rawImage = rawImage;
    }

    public BufferedImage getResampledImage() {
        return resampledImage;
    }

    public void setResampledImage(BufferedImage resampledImage) {
        this.resampledImage = resampledImage;
    }


    public RGBAImage getMaskedColoredImage() {
        return maskedColoredImage;
    }

    public void setMaskedColoredImage(RGBAImage maskedColoredImage) {
        this.maskedColoredImage = maskedColoredImage;
    }
}
