package brainflow.core.rendering;

import brainflow.core.*;
import brainflow.core.layer.AbstractLayer;

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
        
        IImageDisplayModel model = (IImageDisplayModel)o;
        for (int i=0; i<model.getNumLayers(); i++) {
            AbstractLayer layer = model.getLayer(i);

            SliceRenderer renderer = layer.getSliceRenderer(model.getImageSpace(), getSlice(), getDisplayAnatomy());
            
            rendererList.add(renderer);
        }

        return rendererList;

    }








}