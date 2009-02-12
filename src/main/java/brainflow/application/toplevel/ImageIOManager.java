package brainflow.application.toplevel;


import brainflow.application.BrainFlowException;
import brainflow.image.io.IImageDataSource;
import brainflow.application.ImageIODescriptor;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 11:27:29 AM
 * To change this template use File | Settings | File Templates.
 */

public class ImageIOManager {

    private static Logger log = Logger.getLogger(ImageIOManager.class.getName());

    private static java.util.List<ImageIODescriptor> descriptorList = new java.util.ArrayList<ImageIODescriptor>();

    private boolean initialized = false;

    protected ImageIOManager() {
        // Exists only to thwart instantiation.
    }


    public static ImageIOManager getInstance() {
        return (ImageIOManager) SingletonRegistry.REGISTRY.getInstance("brainflow.application.toplevel.ImageIOManager");
    }

    public ImageIODescriptor[] descriptorArray() {
        ImageIODescriptor[] desc = new ImageIODescriptor[descriptorList.size()];
        descriptorList.toArray(desc);
        return desc;
    }

    public ImageIODescriptor getDescriptor(FileObject fobj) throws BrainFlowException {
        for (Iterator<ImageIODescriptor> iter = descriptorList.iterator(); iter.hasNext();) {
            ImageIODescriptor desc = iter.next();

            if (desc.isHeaderMatch(fobj)) return desc;
            if (desc.isDataMatch(fobj)) return desc;
        }

        throw new BrainFlowException("Could not find ImageIODescriptor for supplied File " + fobj);

    }

    public boolean isLoadableImage(FileObject header) {
        assert descriptorList.size() > 0 : "ImageIODescriptors not available";
        for (Iterator<ImageIODescriptor> iter = descriptorList.iterator(); iter.hasNext();) {
            ImageIODescriptor desc = iter.next();
            if (desc.isHeaderMatch(header)) {
                return true;
            }
            //IImageDataSource[] limg = desc.findLoadableImages(fobjs);
            //Collections.addAll(limglist, limg);
        }

        return false;

    }


    public boolean isLoadableImage(String file) {
        try {
            FileObject fobj = VFS.getManager().resolveFile(file);
            return isLoadableImage(fobj);
        } catch (FileSystemException e) {
            log.warning("could not resolve file " + file);
            return false;
        }

    }


    public IImageDataSource[] findLoadableImages(File[] files) {
        FileObject[] fobjs = new FileObject[files.length];
        try {
            for (int i = 0; i < fobjs.length; i++) {
                fobjs[i] = VFS.getManager().resolveFile(files[i].getAbsolutePath());
            }
        } catch (FileSystemException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return findLoadableImages(fobjs);

    }

    public IImageDataSource[] findLoadableImages(FileObject[] fobjs) {

        List<IImageDataSource> limglist = new ArrayList<IImageDataSource>();

        for (Iterator<ImageIODescriptor> iter = descriptorList.iterator(); iter.hasNext();) {
            ImageIODescriptor desc = iter.next();
            IImageDataSource[] limg = desc.findLoadableImages(fobjs);
            Collections.addAll(limglist, limg);
        }

        IImageDataSource[] ret = new IImageDataSource[limglist.size()];
        limglist.toArray(ret);

        return ret;

    }


    public void initialize() throws BrainFlowException {
        if (initialized) return;
        InputStream istream = getClass().getClassLoader().getResourceAsStream("resources/config/imageio-config.xml");

        try {

            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(istream);
            Element root = doc.getRootElement();
            java.util.List l1 = root.getChildren("ImageFormat");

            log.info("Number of image formats: " + l1.size());

            for (Iterator iter = l1.iterator(); iter.hasNext();) {
                try {
                    ImageIODescriptor desc = ImageIODescriptor.loadFromXML((Element) iter.next());
                    log.info("Loading image format: " + desc);
                    descriptorList.add(desc);
                } catch (BrainFlowException e) {
                    throw e;
                }
            }

            descriptorList = Collections.unmodifiableList(descriptorList);
            initialized = true;

        } catch (JDOMException e) {
            e.printStackTrace();
            throw new BrainFlowException("ImageIOManager.intialize: Error reading XML configuration file imagio/config.xml", e);
        } catch (IOException e) {
            throw new BrainFlowException("ImageIOManager.intialize: Error reading XML configuration file imagio/config.xml", e);

        }

    }


}
