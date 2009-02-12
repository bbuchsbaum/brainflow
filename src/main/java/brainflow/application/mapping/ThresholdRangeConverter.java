package brainflow.application.mapping;

import brainflow.display.ThresholdRange;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 8:31:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdRangeConverter implements Converter {
    
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        ThresholdRange trange = (ThresholdRange)o;
        //layer.getData().getImageInfo().
        //writer.startNode("header");

        //writer.startNode("threshold-range");
        writer.addAttribute("min", "" + trange.getMin());
        writer.addAttribute("max", "" + trange.getMax());
        //writer.endNode();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String min = reader.getAttribute("min");
        String max = reader.getAttribute("max");

        ThresholdRange trange = new ThresholdRange(Double.parseDouble(min), Double.parseDouble(max));
        return trange;
    }

    public boolean canConvert(Class aClass) {
        if (aClass.equals(ThresholdRange.class)) {
            return true;
        } else {
            return false;
        }
    }
}
