package brainflow.core.rendering;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

import brainflow.core.layer.ImageLayer2D;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.Axis;
import brainflow.image.rendering.RenderUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 2:50:45 PM
 * To change this template use File | Settings | File Templates.
 */


public class ComposeImagesStage extends ImageProcessingStage {

    private BufferedImage composite = null;

    private static Logger log = Logger.getLogger(ComposeImagesStage.class.getName());


    public void flush() {
        composite = null;
    }

    public Object filter(Object input) throws StageException {
        List<BufferedImage> resImages = (List<BufferedImage>) input;
        List<ImageLayer2D> layers = (List<ImageLayer2D>) getPipeline().getEnv(ImagePlotPipeline.IMAGE_LAYER_DATA_KEY);
        if (composite == null) {
            if (resImages.size() == 0) {
                composite = null;
            } else if (resImages.size() == 1) {
                if (getModel().getLayer(0).isVisible() && getModel().getLayer(0).getImageLayerProperties().opacity.get().doubleValue() >= 1) {
                    composite = resImages.get(0);
                } else if (getModel().getLayer(0).isVisible() && getModel().getLayer(0).getImageLayerProperties().opacity.get().doubleValue() < 1) {
                    composite = compose(layers, resImages);
                }
            } else {
                composite = compose(layers, resImages);
            }
        }

        return composite;
    }

    private BufferedImage compose(List<ImageLayer2D> layers, List<BufferedImage> resImages) {

        Rectangle2D frameBounds = ImagePlotPipeline.getBounds(layers);

        BufferedImage sourceImage = RenderUtils.createCompatibleImage((int) frameBounds.getWidth(),
                (int) frameBounds.getHeight());

        Graphics2D g2 = sourceImage.createGraphics();

        for (int i = 0; i < layers.size(); i++) {
            ImageLayer2D layer2d = layers.get(i);

            if (layer2d.isVisible()) {
                RenderedImage rim = resImages.get(i);
                IImageSpace space = layer2d.getImageData().getImageSpace();
                double minx = space.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
                double miny = space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();

                double transx = (minx - frameBounds.getMinX()); //+ (-frameBounds.getMinX());
                double transy = (miny - frameBounds.getMinY()); //+ (-frameBounds.getMinY());
                log.info("opacity : " + layer2d.getOpacity());
                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) layer2d.getOpacity());
                g2.setComposite(composite);
                g2.drawRenderedImage(rim, AffineTransform.getTranslateInstance(transx, transy));

            }
        }

        g2.dispose();


        return sourceImage;
    }

   
}
