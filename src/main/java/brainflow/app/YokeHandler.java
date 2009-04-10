package brainflow.app;

import brainflow.core.ImageDisplayModel;
import brainflow.core.ImageView;
import brainflow.core.SimpleImageView;
import brainflow.core.ImageViewModel;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2007
 * Time: 2:48:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class YokeHandler  {

    private static final Logger log = Logger.getLogger(YokeHandler.class.getName());

    private ImageView target;

    private Map<ImageView, Long> sources = new WeakHashMap<ImageView, Long>();

    private CrossHandler crossHandler;

    public YokeHandler(ImageView _target) {
        target = _target;
        crossHandler = new CrossHandler(this);

    }


    public void setTarget(ImageView _target) {
        target = _target;
    }

    public ImageView getTarget() {
        return target;
    }

    public void clearSources() {
        for (ImageView view : sources.keySet()) {
            BeanContainer.get().removeListener(view.cursorPos, crossHandler);

        }
        sources.clear();

    }

    public Set<ImageView> getSources() {
        return sources.keySet();

    }

    public void removeSource(ImageView view) {
        if (sources.containsKey(view)) {
            sources.remove(view);
            BeanContainer.get().removeListener(view.cursorPos, crossHandler);
        } else {
            log.warning("Failed removal request: YokeHandler does not contain the view " + view);
        }


    }

    public void addSource(ImageView view) {
        if (view == target) {
            throw new IllegalArgumentException("Source cannot be same ImageView as target!");
        }
        if (sources.containsKey(view)) {
            log.warning("YokeHandler already contains view argument : " + view);
        } else {
            log.fine("yoking view " + view + " to " + target);
            sources.put(view, System.currentTimeMillis());
            BeanContainer.get().addListener(view.cursorPos, crossHandler);
        }
    }


    public boolean containsSource(ImageView view) {
        return sources.containsKey((view));
    }

    public void setTargetLocation(AnatomicalPoint3D point) {

        target.cursorPos.set(point);
      
    }

    public AnatomicalPoint3D getTargetLocation() {
        return target.getCursorPos();
    }




    public static class CrossHandler implements PropertyListener {
        private WeakReference<YokeHandler> handler;

        public CrossHandler(YokeHandler handler) {
            this.handler = new WeakReference<YokeHandler>(handler);
        }

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            AnatomicalPoint3D ap = (AnatomicalPoint3D) newValue;

            if (handler.get() != null) {
                if (!ap.equals(handler.get().getTargetLocation())) {
                    handler.get().setTargetLocation(ap);
                }
            }
        }
    }


    public static void main(String[] args) {
        ImageView target = new SimpleImageView(new ImageViewModel("1"), Anatomy3D.getCanonicalAxial());
        ImageView s1 = new SimpleImageView(new ImageViewModel("s1"),Anatomy3D.getCanonicalAxial());
        ImageView s2 = new SimpleImageView(new ImageViewModel("s2"), Anatomy3D.getCanonicalAxial());

        YokeHandler handler = new YokeHandler(target);
        handler.addSource(s1);
        handler.addSource(s2);

     

    }


}
