package brainflow.core;

import brainflow.image.space.IImageSpace;

import javax.swing.event.ListDataListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 1, 2007
 * Time: 5:42:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageDisplayModelListener extends ListDataListener {

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space);



    
}
