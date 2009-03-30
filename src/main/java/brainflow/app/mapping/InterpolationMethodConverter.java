package brainflow.app.mapping;

import brainflow.display.InterpolationMethod;
import brainflow.display.InterpolationType;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 9:07:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterpolationMethodConverter implements Converter {
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        InterpolationMethod imeth = (InterpolationMethod)o;

        writer.addAttribute("value", "" + imeth.getInterpolation().toString());

    }

    public boolean canConvert(Class aClass) {
        if (InterpolationMethod.class.equals(aClass)) {
            return true;
        }   else {
            return false;
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String value = reader.getAttribute("value");
        return new InterpolationMethod(InterpolationType.valueOf(value));     
    }
}
