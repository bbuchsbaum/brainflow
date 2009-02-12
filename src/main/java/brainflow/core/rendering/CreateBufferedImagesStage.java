package brainflow.core.rendering;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import brainflow.image.data.RGBAImage;
import brainflow.image.rendering.RenderUtils;
import brainflow.core.layer.AbstractLayer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 2:05:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateBufferedImagesStage extends ImageProcessingStage {

    private List<BufferedImage> images;

    private static Logger log = Logger.getLogger(CreateBufferedImagesStage.class.getName());


    public void flush() {
        images = null;
    }

    public Object filter(Object input) throws StageException {
        List<RGBAImage> rgbaImages = (List<RGBAImage>)input;

        if (images == null || images.size() != getModel().getNumLayers()) {
            images = new ArrayList<BufferedImage>();
            for (int i=0; i<getModel().getNumLayers(); i++) {
                RGBAImage rgba = rgbaImages.get(i);
                AbstractLayer layer = getModel().getLayer(i);
                if (layer.isVisible() && rgba != null) {
                    images.add(createBufferedImage(rgba));
                } else {
                    images.add(null);
                }

            }
        }

        return images;

    }

    private BufferedImage createBufferedImage(RGBAImage rgba) {
        byte[] br = rgba.getRed().getByteArray();
        byte[] bg = rgba.getGreen().getByteArray();
        byte[] bb = rgba.getBlue().getByteArray();
        byte[] ba = rgba.getAlpha().getByteArray();

        byte[][] ball = new byte[4][];
        ball[0] = br;
        ball[1] = bg;
        ball[2] = bb;
        ball[3] = ba;
        BufferedImage bimg = RenderUtils.createBufferedImage(ball, rgba.getWidth(), rgba.getHeight());

        // code snippet is required because of bug in Java ImagingLib.
        // It cannot deal with component sample models... so we convert first.
        BufferedImage ret = RenderUtils.createCompatibleImage(bimg.getWidth(), bimg.getHeight());
        Graphics2D g2 = ret.createGraphics();
        g2.drawRenderedImage(bimg, AffineTransform.getTranslateInstance(0, 0));
        return ret;
    }
}
