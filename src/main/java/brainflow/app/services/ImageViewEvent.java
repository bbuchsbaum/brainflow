/*
 * ImageViewEvent.java
 *
 * Created on July 5, 2006, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.services;

import brainflow.core.ImageView;
import org.bushe.swing.event.AbstractEventServiceEvent;

/**
 *
 * @author buchs
 */
public class ImageViewEvent extends AbstractEventServiceEvent {
    
    private ImageView view;
    
    /** Creates a new instance of ImageViewEvent */
    public ImageViewEvent(ImageView view) {
        super(view);
        this.view = view;
    }
    
    public ImageView getImageView() {
        return view;
    }
    
}
