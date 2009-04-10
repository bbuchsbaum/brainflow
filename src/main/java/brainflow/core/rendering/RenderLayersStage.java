package brainflow.core.rendering;

import brainflow.core.SliceRenderer;
import brainflow.image.rendering.RenderUtils;
import brainflow.image.space.Axis;
import brainflow.image.space.ICoordinateSpace;
import org.apache.commons.pipeline.StageException;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 8:37:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class RenderLayersStage extends ImageProcessingStage {

    private BufferedImage composite;


    public void flush() {
        composite = null;
    }

    public Object filter(Object input) throws StageException {
        if (composite != null) return composite;

        List<SliceRenderer> renderers = (List<SliceRenderer>) input;
        Rectangle2D frameBounds = getBounds(renderers);

        if (renderers.size() == 0 || allTransparent(renderers)) {
             composite = null;
        } else if (renderers.size() == 1 && renderers.get(0).getLayer().getImageLayerProperties().opacity.get() >= 1) {
            SliceRenderer renderer = renderers.get(0);
            composite = renderer.render();
        } else {

            BufferedImage sourceImage = RenderUtils.createCompatibleImage((int) frameBounds.getWidth(),
                    (int) frameBounds.getHeight());

            Graphics2D g2 = (Graphics2D) sourceImage.getGraphics();

            int i=0;
            for (SliceRenderer renderer : renderers) {
                if (renderer != null && getModel().isVisible(i))  // todo temporary hack
                    renderer.renderUnto(frameBounds, g2);
                i++;
            }

            g2.dispose();


            composite = sourceImage;

        }

        getPipeline().setFrameBounds(frameBounds);

        return composite;

    }

    public boolean allTransparent(List<SliceRenderer> rendererList) {
        for (SliceRenderer renderer : rendererList) {
            if (renderer.isVisible() || renderer.getLayer().getOpacity() == 0) return false;
        }

        return true;

    }

    public Rectangle2D getBounds(List<SliceRenderer> rendererList) {
        if (rendererList == null || rendererList.size() == 0) {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (SliceRenderer renderer : rendererList) {
            ICoordinateSpace space = renderer.getImageSpace();
            minX = Math.min(minX, space.getImageAxis(Axis.X_AXIS).getRange().getMinimum());
            minY = Math.min(minY, space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());
            maxX = Math.max(maxX, space.getImageAxis(Axis.X_AXIS).getRange().getMaximum());
            maxY = Math.max(maxY, space.getImageAxis(Axis.Y_AXIS).getRange().getMaximum());
        }

        Rectangle2D imageBounds = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        return imageBounds;

    }
}
