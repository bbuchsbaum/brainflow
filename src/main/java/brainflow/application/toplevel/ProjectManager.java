package brainflow.application.toplevel;

import brainflow.application.BrainFlowProject;
import brainflow.image.io.IImageDataSource;
import brainflow.application.services.ImageDisplayModelEvent;
import brainflow.application.services.DataSourceStatusEvent;
import brainflow.colormap.LinearColorMap2;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.utils.Range;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 2, 2006
 * Time: 1:39:29 PM
 * To change this template use File | Settings | File Templates.
 */


public class ProjectManager implements EventSubscriber, BrainFlowProjectListener {

    private List<BrainFlowProject> projects = new ArrayList<BrainFlowProject>();

    private BrainFlowProject activeProject = new BrainFlowProject();

    protected ProjectManager() {
        // Exists only to thwart instantiation.
        EventBus.subscribe(DataSourceStatusEvent.class, this);
        activeProject.addListDataListener(this);
        activeProject.setName("Project-1");
    }

    public static ProjectManager getInstance() {
        return (ProjectManager) SingletonRegistry.REGISTRY.getInstance("brainflow.application.toplevel.ProjectManager");
    }

    public BrainFlowProject getActiveProject() {
        return activeProject;
    }

    public IImageDisplayModel addToActiveProject(ImageLayer layer) {

        boolean registered = DataSourceManager.getInstance().isRegistered(layer.getDataSource());

        if (!registered) {
            DataSourceManager.getInstance().register(layer.getDataSource());
        }

        //todo give sensible name
        IImageDisplayModel displayModel = new ImageDisplayModel("model #" + (activeProject.size() + 1));

        //todo hack cast
        displayModel.addLayer((ImageLayer3D)layer);
        activeProject.addModel(displayModel);


        return displayModel;

    }


    public IImageDisplayModel addToActiveProject(IImageDataSource limg) {

        boolean registered = DataSourceManager.getInstance().isRegistered(limg);

        if (!registered) {
            DataSourceManager.getInstance().register(limg);
        }

        //todo give sensible name
        IImageDisplayModel displayModel = new ImageDisplayModel("model #" + (activeProject.size() + 1));



        ImageLayerProperties params = new ImageLayerProperties(
                new Range(limg.getData().minValue(),
                          limg.getData().maxValue()));

        params.colorMap.set(new LinearColorMap2(limg.getData().minValue(),
                limg.getData().maxValue(),
                ResourceManager.getInstance().getDefaultColorMap()));

        ImageLayer3D layer = new ImageLayer3D(limg, params);
        displayModel.addLayer(layer);

        activeProject.addModel(displayModel);


        return displayModel;

    }


    public void onEvent(Object evt) {
        DataSourceStatusEvent event = (DataSourceStatusEvent) evt;
        switch (event.getEventID()) {
            case IMAGE_LOADED:
                break;
            case IMAGE_REGISTERED:
                break;
            case IMAGE_REMOVED:
                //todo if only zero image is in model, then this is effectively removing the view.
                clearLoadableImage(event.getLoadableImage());
            case IMAGE_UNLOADED:
                break;
        }
    }

    protected void clearLoadableImage(IImageDataSource limg) {
        Iterator<IImageDisplayModel> iter = activeProject.iterator();
        while (iter.hasNext()) {
            IImageDisplayModel dmodel = iter.next();
            List<Integer> idx = dmodel.indexOf(limg.getData());

            if (idx.size() > 0) {

                List<ImageLayer> removables = new ArrayList<ImageLayer>();

                for (int i : idx) {
                    removables.add(dmodel.getLayer(i));
                }

                for (ImageLayer layer : removables) {
                    //todo hack cast
                    dmodel.removeLayer((ImageLayer3D)layer);
                }
            }
        }
    }


    public void modelAdded(BrainFlowProjectEvent event) {
        EventBus.publish(new ImageDisplayModelEvent(event, ImageDisplayModelEvent.TYPE.LAYER_ADDED));

    }

    public void modelRemoved(BrainFlowProjectEvent event) {
        EventBus.publish(new ImageDisplayModelEvent(event, ImageDisplayModelEvent.TYPE.LAYER_REMOVED));
    }

    public void intervalAdded(BrainFlowProjectEvent e) {
        EventBus.publish(new ImageDisplayModelEvent(e, ImageDisplayModelEvent.TYPE.LAYER_INTERVAL_ADDED));
    }

    public void intervalRemoved(BrainFlowProjectEvent e) {
        EventBus.publish(new ImageDisplayModelEvent(e, ImageDisplayModelEvent.TYPE.LAYER_INTERVAL_REMOVED));
    }

    public void contentsChanged(BrainFlowProjectEvent e) {
        EventBus.publish(new ImageDisplayModelEvent(e, ImageDisplayModelEvent.TYPE.LAYER_CHANGED));
    }
}
