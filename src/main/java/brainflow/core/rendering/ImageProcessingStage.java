package brainflow.core.rendering;

import brainflow.image.anatomy.GridPoint3D;
import org.apache.commons.pipeline.stage.BaseStage;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.StageContext;

import brainflow.core.ImageViewModel;
import brainflow.image.anatomy.Anatomy3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:53:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageProcessingStage extends BaseStage {

    private ImagePlotPipeline pipeline;

    @Override
    public void init(StageContext context) {
        super.init(context);
        pipeline = (ImagePlotPipeline)context;
    }

    public abstract void flush();

    public ImageViewModel getModel() {
        return pipeline.getModel();
    }

    public GridPoint3D getSlice() {
        return pipeline.getSlice();
    }

    public Anatomy3D getDisplayAnatomy() {
        return pipeline.getDisplayAnatomy();
    }

    protected ImagePlotPipeline getPipeline() {
        return pipeline;
    }

    public void process(Object obj) throws StageException {
        Object out = filter(obj);
        emit(out);
    }

    public abstract Object filter(Object input) throws StageException;

}
