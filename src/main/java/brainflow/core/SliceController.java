package brainflow.core;

import brainflow.image.anatomy.GridPoint3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 7, 2007
 * Time: 2:31:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SliceController {


    public GridPoint3D getSlice();

    public void setSlice(GridPoint3D slice);

    public void nextSlice();

    public void previousSlice();

    public void pageBack();

    public void pageForward();


}
