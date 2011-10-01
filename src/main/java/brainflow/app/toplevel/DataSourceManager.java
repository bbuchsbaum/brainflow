/*
 * DataSourceManager.java
 *
 * Created on May 29, 2003, 9:09 AM
 */

package brainflow.app.toplevel;

import brainflow.image.io.*;
import brainflow.app.services.DataSourceStatusEvent;
import org.bushe.swing.event.EventBus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author Bradley
 */
public class DataSourceManager {

    /**
     * Creates a new instance of DataSourceManager
     */

    private LinkedHashMap<Integer, IImageSource> imageMap = new LinkedHashMap<Integer, IImageSource>();

    private Logger log = Logger.getLogger(DataSourceManager.class.getName());


    protected DataSourceManager() {
        // Exists only to thwart instantiation.
    }

    public static DataSourceManager get() {
        return (DataSourceManager) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.DataSourceManager");
    }


    public boolean requestRemoval(IImageSource limg) {
        //todo need to rethink mechanism for image removal/unloading

        if (imageMap.containsKey(limg.getUniqueID())) {
            imageMap.remove(limg.getUniqueID());
            EventBus.publish(new DataSourceStatusEvent(limg, DataSourceStatusEvent.EventID.IMAGE_REMOVED));
            limg.releaseData();
        }

        return true;


    }

    public boolean isRegistered(IImageSource limg) {
        return imageMap.containsKey(limg.getUniqueID());
    }

    public void register(IImageSource limg) {
        int uid = limg.getUniqueID();
        if (imageMap.containsKey(uid)) {

            log.warning("Attempt to createSource image already in memory: " + limg.getHeaderFile());
            throw new IllegalArgumentException("IImageDataSource " + limg.getStem() + " with uinique ID " + uid + " is already registered.");
        }

        //limg.getDataFile().getFileSystem().addListener();

        imageMap.put(uid, limg);
        log.fine("registering image ..." + limg.getImageInfo().getImageLabel());
        EventBus.publish(new DataSourceStatusEvent(limg, DataSourceStatusEvent.EventID.IMAGE_REGISTERED));
    }

    public IImageSource createDataSource(IImageFileDescriptor descriptor, ImageInfo info, boolean register) {

        IImageSource source = new ImageSource3D(descriptor, info);
        if (register) {
            register(source);
        }

        return source;
    }

    public IImageSource createDataSource(IImageFileDescriptor descriptor, List<ImageInfo> infoList, int index, boolean register) {

        IImageSource source = new ImageSource3D(descriptor, infoList, index);
        if (register) {
            if (!isRegistered(source))
                register(source);
        }

        return source;
    }


    public int getNumLoadableImages() {
        return imageMap.size();
    }

    public IImageSource lookup(int uid) {
        return imageMap.get(uid);
    }


    



}
