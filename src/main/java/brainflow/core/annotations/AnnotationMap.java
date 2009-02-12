package brainflow.core.annotations;

import brainflow.core.IImagePlot;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 16, 2007
 * Time: 8:11:35 PM
 * To change this template use File | Settings | File Templates.
 */


public class AnnotationMap {


    private HashMap<IImagePlot, Map<String, IAnnotation>> annotationMap = new HashMap<IImagePlot, Map<String, IAnnotation>>();


    public Map<IImagePlot, Map<String, IAnnotation>> getMap() {
        return Collections.unmodifiableMap(annotationMap);
    }

    public void putAnnotation(IImagePlot plot, IAnnotation annotation) {
        Map<String, IAnnotation> map = annotationMap.get(plot);
        if (map == null) {
            map = new HashMap<String, IAnnotation>();
        }

        map.put(annotation.getIdentifier(), annotation);
        annotationMap.put(plot, map);
    }
}
