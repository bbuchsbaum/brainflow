package brainflow.app;

import brainflow.app.toplevel.BrainFlowProjectEvent;
import brainflow.app.toplevel.BrainFlowProjectListener;

import brainflow.core.ImageDisplayModelListener;
import brainflow.core.ImageViewModel;
import brainflow.core.IImageDisplayModel;
import brainflow.image.space.IImageSpace;
import brainflow.image.io.IImageDataSource;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.*;
import java.util.logging.Logger;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 1:18:57 PM
 */
public class BrainFlowProject {

    private static final Logger log = Logger.getLogger(BrainFlowProject.class.getName());

    private List<ImageViewModel> modelList = new ArrayList<ImageViewModel>();

    private Set<IImageDataSource> dataSources = new LinkedHashSet<IImageDataSource>();

    private ModelListDataListener listDataListener = new ModelListDataListener();

    private List<BrainFlowProjectListener> listenerList = new ArrayList<BrainFlowProjectListener>();

    private String name = "untitled";


    public void addDataSource(IImageDataSource dataSource) {
        dataSources.add(dataSource);
    }

    public void removeDataSource(IImageDataSource dataSource) {
        dataSources.remove(dataSource);
    }

    public void addModel(ImageViewModel model) {
        if (!modelList.contains(model)) {
            modelList.add(model);
            addSources(model);
            model.addListDataListener(listDataListener);
            fireModelAdded(new BrainFlowProjectEvent(this, model, null));


        } else {
            log.warning("BrainFlowProject already contains model supplied as argument, not adding.");
        }
    }

    private void addSources(ImageViewModel model) {
        //synchronized (model) {
            int nlayers = model.size();

            for (int i = 0; i < nlayers; i++) {
                dataSources.add(model.get(i).getDataSource());
            }
        //}
    }

    public void removeSources(ImageViewModel model) {
        //synchronized (model) {
            int nlayers = model.size();

            for (int i = 0; i < nlayers; i++) {
                dataSources.remove(model.get(i).getDataSource());
            }
        //}

    }

    public void removeModel(ImageViewModel model) {
        if (modelList.contains(model)) {
            modelList.remove(model);
            if (model.size() > 0)
                removeSources(model);
            
            model.removeListDataListener(listDataListener);
            fireModelRemoved(new BrainFlowProjectEvent(this, model, null));
            
        } else {
            log.warning("BrainFlowProject does not contain model supplied as argument, cannot remove");
        }
    }

    public ImageViewModel getModel(int index) {
        return modelList.get(index);
    }


    public Iterator<ImageViewModel> iterator() {
        return modelList.iterator();
    }

    public List<ImageViewModel> getModelList() {
        return Collections.unmodifiableList(modelList);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int size() {
        return modelList.size();
    }

    public void addListDataListener(BrainFlowProjectListener listener) {
        listenerList.add(listener);
    }

    public void removeListDataListener(BrainFlowProjectListener listener) {
        listenerList.remove(listener);
    }

    

    public String toString() {
        return name;
    }

    private void fireModelAdded(BrainFlowProjectEvent event) {
        for (BrainFlowProjectListener l : listenerList) {
            l.modelAdded(event);

        }

    }

    private void fireModelRemoved(BrainFlowProjectEvent event) {
        for (BrainFlowProjectListener l : listenerList) {
            l.modelRemoved(event);

        }


    }

    private void fireIntervalAdded(ListDataEvent e) {
        for (BrainFlowProjectListener l : listenerList) {
            l.intervalAdded(new BrainFlowProjectEvent(this, (ImageViewModel) e.getSource(), e));

        }

    }

    private void fireContentsChanged(ListDataEvent e) {
        for (BrainFlowProjectListener l : listenerList) {
            l.contentsChanged(new BrainFlowProjectEvent(this, (ImageViewModel) e.getSource(), e));


        }

    }

    private void fireIntervalRemoved(ListDataEvent e) {
        for (BrainFlowProjectListener l : listenerList) {
            l.intervalRemoved(new BrainFlowProjectEvent(this, (ImageViewModel) e.getSource(), e));

        }

    }


    class ModelListDataListener implements ListDataListener {


        public void intervalAdded(ListDataEvent e) {
            fireIntervalAdded(e);
        }

        public void intervalRemoved(ListDataEvent e) {
            fireIntervalRemoved(e);

        }

        public void contentsChanged(ListDataEvent e) {
            fireContentsChanged(e);

        }


    }


}
