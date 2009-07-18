package brainflow.app.toplevel;

import brainflow.app.BrainFlowProject;
import brainflow.image.io.IImageDataSource;
import brainflow.image.data.IImageData3D;
import brainflow.app.toplevel.ImageViewModelEvent;
import brainflow.core.services.DataSourceStatusEvent;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerList;
import brainflow.core.layer.ImageLayer;
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

    public ImageViewModel createViewModel(ImageLayer3D layer, boolean addToActiveProject) {
        registerDataSource(layer.getDataSource());

        List<ImageLayer3D> layers = new ArrayList<ImageLayer3D>();
        layers.add(layer);
        ImageViewModel viewModel = new ImageViewModel("model #" + (activeProject.size() + 1), layers);


        if (addToActiveProject) {
            activeProject.addModel(viewModel);
        }


        return viewModel;

    }


    public ImageViewModel createViewModel(IImageDataSource dataSource, boolean addToActiveProject) {
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
        System.out.println("clearing data source " + dataSource);
        //todo just really and truly horrible
        Iterator<ImageViewModel> iter = activeProject.iterator();
        System.out.println("number of models " + activeProject.size());
        List<ImageViewModel> purged = new ArrayList<ImageViewModel>();

        while (iter.hasNext()) {
            ImageViewModel dmodel = iter.next();
            //todo hack cast
            //todo should retrieve layers not indices here ..
            List<Integer> idx = dmodel.indexOf((IImageData3D) dataSource.getData());
            List<ImageView> views = DisplayManager.get().getImageViews(dmodel);

            System.out.println("number of views " + views.size());

            if (idx.size() > 0) {

                List<ImageLayer3D> removables = new ArrayList<ImageLayer3D>();

                for (int i : idx) {
                    removables.add(dmodel.get(i));
                }

                System.out.println("number of removables " + removables.size());

                if (removables.size() == dmodel.size()) {
                    System.out.println("number of removables == size of model");
                    //removing all layers from model, which invalidates the model.
                    for (ImageView view : views) {
                        DisplayManager.get().removeView(view);
                        //still need to remove layers to notify event listeners, etc.
                        for (ImageLayer3D layer : removables) {
                            view.getModel().remove(layer);
                        }
                        //////
                    }

                    purged.add(dmodel);

                } else {
                    System.out.println("removing layers from all views");
                    for (ImageView view : views) {
                        for (ImageLayer3D layer : removables) {
                            view.getModel().remove(layer);
                        }
                    }


                }
            }
        }


        for (ImageViewModel model : purged) {
            System.out.println("removing model from project");
            activeProject.removeModel(model);
        }
    }


    public void modelAdded(BrainFlowProjectEvent event) {
        EventBus.publish(new ImageViewModelEvent(event, ImageViewModelEvent.TYPE.LAYER_ADDED));

    }

    public void modelRemoved(BrainFlowProjectEvent event) {
        EventBus.publish(new ImageViewModelEvent(event, ImageViewModelEvent.TYPE.LAYER_REMOVED));
    }

    public void intervalAdded(BrainFlowProjectEvent e) {
        EventBus.publish(new ImageViewModelEvent(e, ImageViewModelEvent.TYPE.LAYER_INTERVAL_ADDED));
    }

    public void intervalRemoved(BrainFlowProjectEvent e) {
        EventBus.publish(new ImageViewModelEvent(e, ImageViewModelEvent.TYPE.LAYER_INTERVAL_REMOVED));
    }

    public void contentsChanged(BrainFlowProjectEvent e) {
        EventBus.publish(new ImageViewModelEvent(e, ImageViewModelEvent.TYPE.LAYER_CHANGED));
    }
}
