package brainflow.core.rendering;

import org.apache.commons.pipeline.StageException;
import brainflow.core.IImagePlot;

import java.awt.image.BufferedImage;

import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 7:58:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CropImageStage extends ImageProcessingStage {


    private BufferedImage croppedImage;

    private static Logger log = Logger.getLogger(CropImageStage.class.getName());


    public void flush() {
        croppedImage = null;
    }

    public Object filter(Object input) throws StageException {
        BufferedImage compositeImage = (BufferedImage) input;

        if (croppedImage == null && compositeImage != null) {
            ImagePlotPipeline pipe = getPipeline();
            IImagePlot plot = pipe.getPlot();

            Rectangle2D bounds = pipe.getFrameBounds();
           
            Rectangle2D region = new Rectangle2D.Double(plot.getXAxisRange().getMinimum(),
                    plot.getYAxisRange().getMinimum(),
                    plot.getXAxisRange().getInterval(),
                    plot.getYAxisRange().getInterval());


            croppedImage = crop(bounds, region, compositeImage);

        }

        return croppedImage;
    }

    private BufferedImage crop(Rectangle2D bounds, Rectangle2D region, BufferedImage image) {
        if (bounds.equals(region)) {
            return image;
        }

        int xmin = (int) Math.max(Math.round(region.getX() - bounds.getX()), 0);
        int ymin = (int) Math.max(Math.round(region.getY() - bounds.getY()), 0);
        int width = (int) Math.min(bounds.getWidth() - xmin, region.getWidth());
        int height = (int) Math.min(bounds.getHeight() - ymin, region.getHeight());

        width = Math.min(width, image.getWidth());
        height = Math.min(height, image.getHeight());

        BufferedImage ret = null;

        try {
            ret = image.getSubimage(xmin, ymin, width, height);
            
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ret;

    }
}
