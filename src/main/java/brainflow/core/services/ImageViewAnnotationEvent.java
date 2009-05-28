package brainflow.core.services;

import brainflow.core.ImageView;
import brainflow.core.annotations.IAnnotation;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 21, 2007
 * Time: 4:50:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewAnnotationEvent extends ImageViewEvent {


    private IAnnotation annotation;

    public ImageViewAnnotationEvent(ImageView view, IAnnotation _annotation) {
        super(view);
        annotation = _annotation;
    }

    public IAnnotation getAnnotation() {
        return annotation;
    }




}
