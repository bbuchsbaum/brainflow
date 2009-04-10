package brainflow.core.rendering;

import brainflow.core.*;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayer3D;

import org.apache.commons.pipeline.StageException;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:31:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class GatherRenderersStage extends ImageProcessingStage {


    private List<SliceRenderer> rendererList = null;

    private static Logger log = Logger.getLogger(GatherRenderersStage.class.getName());


    public GatherRenderersStage() {
    }


    public void flush() {
        rendererList = null;
    }

    public Object filter(Object o) throws StageException {
        if (rendererList != null) return rendererList;

        rendererList = new ArrayList<SliceRenderer>();
        
        ImageViewModel model = (ImageViewModel)o;
        Iterator<ImageLayer3D> iter = model.iterator();
        while(iter.hasNext()) {
            AbstractLayer layer = iter.next();

            SliceRenderer renderer = layer.getSliceRenderer(model.getImageSpace(), getSlice(), getDisplayAnatomy());
            
            rendererList.add(renderer);
        }

        return rendererList;

    }








}