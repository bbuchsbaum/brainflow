package brainflow.app.mapping;

import brainflow.core.BrainFlowException;
import brainflow.image.io.IImageDataSource;
import brainflow.image.io.ImageIODescriptor;
import brainflow.app.toplevel.ImageIOManager;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 4:18:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class IImageDataSourceConverter implements Converter {

    private static final String INDEX_KEY = "data_index";


    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        Integer dataIndex = (Integer)context.get(INDEX_KEY);

        if (dataIndex != null) {
            dataIndex = dataIndex+1;         
            context.put(INDEX_KEY, dataIndex+1);
        } else {
            dataIndex=1;
            context.put(INDEX_KEY, dataIndex);
        }


        
        IImageDataSource dataSource = (IImageDataSource)object;
        writer.addAttribute("refid", "dataSource"+dataIndex);
        
        writer.startNode("header");
        context.convertAnother(dataSource.getHeaderFile());
        writer.endNode();

        

        writer.startNode("data");
        context.convertAnother(dataSource.getDataFile());
        writer.endNode();

        writer.startNode("data-format");
        context.convertAnother(dataSource.getFileFormat());
        writer.endNode();


    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

        reader.moveDown();
        FileObject header = (FileObject)context.convertAnother(new Object(), FileObject.class);
        reader.moveUp();
        reader.moveDown();
        FileObject data = (FileObject)context.convertAnother(new Object(), FileObject.class);
        reader.moveUp();
        

        String format =  (String)context.convertAnother(new Object(), String.class);

        try {
            ImageIODescriptor descriptor = ImageIOManager.getInstance().getDescriptor(header);
            IImageDataSource dataSource = descriptor.createLoadableImage(header, data);
            return dataSource;
        } catch(BrainFlowException e) {
            throw new RuntimeException(e);
        }



    }

    public boolean canConvert(Class aClass) {
       
        if (IImageDataSource.class.isAssignableFrom(aClass)) {
            return true;
        }

        return false;

    }
}
