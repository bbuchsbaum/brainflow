package brainflow.utils;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 30, 2006
 * Time: 4:43:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IResizeableBorder {


    public enum HANDLE_LOCATION {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,
        SIDE_LEFT,
        SIDE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_MIDDLE,
        BOTTOM_RIGHT,
        NULL

    }


    public boolean onHandle(Point p);

    public IResizeableBorder.HANDLE_LOCATION getHandle(Point p);

}
