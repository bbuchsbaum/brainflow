package brainflow.app.mapping;

import brainflow.image.io.IImageDataSource;
import brainflow.image.io.SoftImageDataSource;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.display.InterpolationMethod;
import brainflow.display.ThresholdRange;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.provider.local.LocalFile;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 4:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class XStreamUtils {


    public static XStream createDefaultXStream() {
        XStream xstream = new XStream(new DomDriver());
        xstream.setMarshallingStrategy(new com.thoughtworks.xstream.core.TreeMarshallingStrategy());
        xstream.alias("image-data", IImageDataSource.class);
        xstream.alias("image-data", SoftImageDataSource.class);
        xstream.alias("file-reference", FileObject.class);
        xstream.alias("file-reference", LocalFile.class);
        xstream.alias("image-layer", ImageLayer.class);
        xstream.alias("image-layer", ImageLayer3D.class);
        xstream.alias("threshold-range", ThresholdRange.class);
        xstream.alias("interpolation-method", InterpolationMethod.class);
        //xstream.alias("opacity", Opacity.class);
        xstream.registerConverter(new IImageDataSourceConverter());
        xstream.registerConverter(new FileObjectConverter());
        xstream.registerConverter(new ImageLayerConverter());
        xstream.registerConverter(new ThresholdRangeConverter());
        xstream.registerConverter(new InterpolationMethodConverter());
    //    xstream.useAttributeFor(InterpolationType.class);
        return xstream;

    }
}
