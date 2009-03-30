package brainflow.app.mapping;

import brainflow.image.io.IImageDataSource;
import brainflow.app.ImageIODescriptor;
import brainflow.image.io.SoftImageDataSource;
import brainflow.app.toplevel.ImageIOManager;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 4:00:23 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestXStream {


    IImageDataSource limg1;
    IImageDataSource limg2;

    public TestXStream() {
        try {
            ImageIOManager.getInstance().initialize();


            FileObject fobj1 = VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/resources/data/icbm452_atlas_probability_white.hdr");
            FileObject fobj2 = VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/resources/data/icbm452_atlas_probability_gray.hdr");

            ImageIODescriptor descriptor1 = ImageIOManager.getInstance().getDescriptor(fobj1);
            ImageIODescriptor descriptor2 = ImageIOManager.getInstance().getDescriptor(fobj2);
            limg1 = descriptor1.createLoadableImage(fobj1, VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/resources/data/icbm452_atlas_probability_white.img"));
            limg2 = descriptor2.createLoadableImage(fobj2, VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/resources/data/icbm452_atlas_probability_gray.img"));


            XStream xstream = new XStream(new DomDriver());
            xstream.setMarshallingStrategy(new com.thoughtworks.xstream.core.TreeMarshallingStrategy());
            xstream.alias("image-data", IImageDataSource.class);
            xstream.alias("image-data", SoftImageDataSource.class);
            xstream.alias("file-reference", FileObject.class);
            xstream.alias("file-reference", LocalFile.class);
            xstream.alias("image-layer", ImageLayer.class);
            xstream.registerConverter(new IImageDataSourceConverter());
            xstream.registerConverter(new FileObjectConverter());
            xstream.registerConverter(new ImageLayerConverter());
           // xstream.setMode(XStream.ID_REFERENCES);

            
            DataHolder holder = xstream.newDataHolder();
            holder.put("dataIndex", 1);

            List<ImageLayer> sources = new ArrayList<ImageLayer>();

            ImageLayer layer1 = new ImageLayer3D(limg1);
            ImageLayer layer2 = new ImageLayer3D(limg1);

            sources.add(layer1);
            sources.add(layer2);

            String xmlString = xstream.toXML(limg1);
            xstream.fromXML(xmlString);
            


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new TestXStream();
    }


}
