package brainflow.app.toplevel;

import brainflow.app.BrainFlowProject;
import brainflow.image.io.IImageDataSource;
import brainflow.image.data.IImageData3D;
import brainflow.app.services.ImageDisplayModelEvent;
import brainflow.app.services.DataSourceStatusEvent;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerList;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

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

    public static ProjectManager get() {
        return (ProjectManager) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.ProjectManager");
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

    public ImageViewModel createDisplayModel(ImageLayer3D layer, boolean addToActiveProject) {
        registerDataSource(layer.getDataSource());

        LayerList<ImageLayer3D> layers = new LayerList<ImageLayer3D>();
        layers.add(layer);
        ImageViewModel displayModel = new ImageViewModel("model #" + (activeProject.size() + 1), layers);


        if (addToActiveProject) {
            activeProject.addModel(displayModel);
        }


        return displayModel;

    }


    public ImageViewModel createDisplayModel(IImageDataSource dataSource, boolean addToActiveProject) {
        //todo maybe this isn't the right place for this?
        
        registerDataSource(dataSource);

        ImageViewModel displayModel = ImageViewFactory.createModel("model #" + (activeProject.size() + 1), dataSource);

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
        Iterator<ImageViewModel> iter = activeProject.iterator();
        List<ImageViewModel> purged = new ArrayList<ImageViewModel>();

        while (iter.hasNext()) {
            ImageViewModel dmodel = iter.next();
            //todo hack cast
            //todo should retrieve layers not indices here ..
            List<Integer> idx = dmodel.indexOf((IImageData3D)dataSource.getData());
            List<ImageView> views = DisplayManager.getInstance().getImageViews(dmodel);

            if (idx.size() > 0) {

                List<ImageLayer3D> removables = new ArrayList<ImageLayer3D>();

                for (int i : idx) {
                    removables.add(dmodel.get(i));
                }

                if (removables.size() == dmodel.size()) {
                    //removing all layers from model, which invalidates the model.
                     for (ImageView view : views) {
                        DisplayManager.getInstance().removeView(view);
                    }

                    purged.add(dmodel);
                    
                } else {



                    List<ImageLayer3D> list = dmodel.cloneList();
                    list.removeAll(removables);
                    ImageViewModel model = new ImageViewModel(dmodel.getName(), new LayerList<ImageLayer3D>(list));
                    
                    for (ImageView view : views) {
                        view.setModel(model);
                    }


                }
            }
        }

        for (ImageViewModel  model : purged) {
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
