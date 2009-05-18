package brainflow.image.io;

import brainflow.image.io.ImageInfo;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.vfs.FileObject;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 10:04:11 AM
 */
public class AFNIImageInfo extends ImageInfo {

    private static final Logger log = Logger.getLogger(AFNIImageInfo.class.getName());

    private Map<AFNIAttributeKey, HeaderAttribute> attributeMap;

   

    public AFNIImageInfo(FileObject headerFile, FileObject dataFile, Map<AFNIAttributeKey, HeaderAttribute> attributeMap) {
        super();
        setHeaderFile(headerFile);
        setDataFile(dataFile);
        this.attributeMap = attributeMap;
    }

    



}
