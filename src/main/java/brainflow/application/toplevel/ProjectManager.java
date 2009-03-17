package brainflow.application.toplevel;

import brainflow.application.BrainFlowProject;
import brainflow.image.io.IImageDataSource;
import brainflow.application.services.ImageDisplayModelEvent;
import brainflow.application.services.DataSourceStatusEvent;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
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

    private void registerDataSource(IImageDataSource dsource) {
        boolean registered = DataSourceManager.get().isRegistered(dsource);

        if (!registered) {
            DataSourceManager.get().register(dsource);
        }

    }

    public IImageDisplayModel createDisplayModel(ImageLayer3D layer, boolean addToActiveProject) {
        registerDataSource(layer.getDataSource());

        IImageDisplayModel displayModel = new ImageDisplayModel("model #" + (activeProject.size() + 1));

        displayModel.addLayer(layer);

        if (addToActiveProject) {
            activeProject.addModel(displayModel);
        }


        return displayModel;

    }


    public IImageDisplayModel createDisplayModel(IImageDataSource dataSource, boolean addToActiveProject) {
        //todo maybe this isn't the right place for this?
        
        registerDataSource(dataSource);

        IImageDisplayModel displayModel = ImageViewFactory.createModel("model #" + (activeProject.size() + 1), dataSource);

        if (addToActiveProject) {
            activeProject.addModel(displayModel);
        }

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
                clearDataSource(event.getLoadableImage());
            case IMAGE_UNLOADED:
                break;
        }
    }

    protected void clearDataSource(IImageDataSource dataSource) {
        Iterator<IImageDisplayModel> iter = activeProject.iterator();
        List<IImageDisplayModel> purged = new ArrayList<IImageDisplayModel>();

        while (iter.hasNext()) {
            IImageDisplayModel dmodel = iter.next();
            List<Integer> idx = dmodel.indexOf(dataSource.getData());

            if (idx.size() > 0) {

                List<ImageLayer3D> removables = new ArrayList<ImageLayer3D>();

                for (int i : idx) {
                    removables.add(dmodel.getLayer(i));
                }

                if (removables.size() == dmodel.getNumLayers()) {
                    //removing all layers from model, which invalidates the model.
                    List<ImageView> views = DisplayManager.getInstance().getImageViews(dmodel);
                    for (ImageView view : views) {
                        DisplayManager.getInstance().removeView(view);
                    }
                    purged.add(dmodel);
                    
                } else {

                    for (ImageLayer3D layer : removables) {
                        dmodel.removeLayer(layer);
                    }
                }
            }
        }

        for (IImageDisplayModel  model : purged) {
            activeProject.removeModel(model);
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
