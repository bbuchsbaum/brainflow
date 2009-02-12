package brainflow.core.rendering;

import org.apache.commons.pipeline.StageException;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.Axis;
import brainflow.core.layer.ImageLayer2D;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.display.InterpolationType;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 7:13:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResampleImagesStage extends ImageProcessingStage {

    private static final Logger log = Logger.getLogger(ResampleImagesStage.class.getName());

    private List<BufferedImage> images;


    public void flush() {
        images = null;
    }

    public Object filter(Object input) throws StageException {
        List<BufferedImage> resImages = (List<BufferedImage>)input;
        if (images == null || images.size() != getModel().getNumLayers()) {
            images = new ArrayList<BufferedImage>();
            List<ImageLayer2D> layers = (List<ImageLayer2D>)getPipeline().getEnv(ImagePlotPipeline.IMAGE_LAYER_DATA_KEY);
             for (int i=0; i<getModel().getNumLayers(); i++) {
                ImageLayer2D layer = layers.get(i);
                 BufferedImage bimg = resImages.get(i);
                if (layer.isVisible() && bimg != null) {
                    images.add(resample(layer, bimg));
                } else {
                    images.add(null);
                }

             }
        }

        return images;

    }

    private BufferedImage resample(ImageLayer2D layer, BufferedImage source) {

        ImageLayerProperties dprops = layer.getImageLayerProperties();
        InterpolationType interp = dprops.getInterpolation();
        ImageSpace2D ispace = (ImageSpace2D) layer.getImageData().getImageSpace();

        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        //AffineTransform at = AffineTransform.getTranslateInstance(ox,oy);
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.scale(sx, sy);
        AffineTransformOp aop = null;


        if (interp == InterpolationType.NEAREST_NEIGHBOR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        } else if (interp == InterpolationType.CUBIC) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        } else if (interp == InterpolationType.LINEAR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        } else {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }

        return aop.filter(source, null);


    }
}