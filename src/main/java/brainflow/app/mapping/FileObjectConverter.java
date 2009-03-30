package brainflow.app.mapping;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;


/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 5:01:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileObjectConverter implements Converter {

    //@Testable
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            FileObject fobj = (FileObject) object;
            writer.addAttribute("uri", fobj.getName().getURI());
            writer.addAttribute("type", fobj.getType().getName());
            writer.setValue(fobj.getName().getBaseName());

            
        } catch (FileSystemException e) {
            throw new RuntimeException("error during xml serialization", e);
        }
    }

    //@Testable
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String uri = reader.getAttribute("uri");

        FileObject ret;

        try {
            ret = VFS.getManager().resolveFile(uri);
        } catch(FileSystemException e) {
            throw new RuntimeException(e);
        }


        context.put(ret.getName().getURI(), ret);

        return ret;

    }

    public boolean canConvert(Class aClass) {
        if (FileObject.class.isAssignableFrom(aClass)) {
            return true;
            
        }
        return false;

    }


}
