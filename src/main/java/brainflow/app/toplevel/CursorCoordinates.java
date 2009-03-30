package brainflow.app.toplevel;

import brainflow.app.services.ImageViewMousePointerEvent;
import brainflow.image.anatomy.AnatomicalPoint3D;
import com.jidesoft.status.LabelStatusBarItem;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import java.awt.*;
import java.text.MessageFormat;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 11:25:29 AM
 */
public class CursorCoordinates implements EventSubscriber {

    private LabelStatusBarItem xaxisLabel = new LabelStatusBarItem();

    private LabelStatusBarItem yaxisLabel = new LabelStatusBarItem();

    private LabelStatusBarItem zaxisLabel = new LabelStatusBarItem();


    private MessageFormat format = new MessageFormat("{0}: {1, number, ##0.0}");

    public CursorCoordinates() {
        EventBus.subscribeExactlyStrongly(ImageViewMousePointerEvent.class, this);
        xaxisLabel.setText("0.0");
        yaxisLabel.setText("0.0");
        zaxisLabel.setText("0.0");
        xaxisLabel.setMinimumSize(new Dimension(60, 0));
        yaxisLabel.setMinimumSize(new Dimension(60, 0));
        zaxisLabel.setMinimumSize(new Dimension(60, 0));
    }


    public LabelStatusBarItem getXaxisLabel() {
        return xaxisLabel;
    }

    public LabelStatusBarItem getYaxisLabel() {
        return yaxisLabel;
    }

    public LabelStatusBarItem getZaxisLabel() {
        return zaxisLabel;
    }

    public void onEvent(Object evt) {

        ImageViewMousePointerEvent event = (ImageViewMousePointerEvent) evt;
        AnatomicalPoint3D gpoint = event.getLocation();
        if (gpoint != null) {
            xaxisLabel.setText(format.format(new Object[]{
                    gpoint.getAnatomy().XAXIS.toString().substring(0, 1),
                    gpoint.getValue(gpoint.getAnatomy().XAXIS).getValue()}
            ));

            yaxisLabel.setText(format.format(new Object[]{
                    gpoint.getAnatomy().YAXIS.toString().substring(0, 1),
                    gpoint.getValue(gpoint.getAnatomy().YAXIS).getValue()}
            ));

            zaxisLabel.setText(format.format(new Object[]{
                    gpoint.getAnatomy().ZAXIS.toString().substring(0, 1),
                    gpoint.getValue(gpoint.getAnatomy().ZAXIS).getValue()}
            ));


        }
    }
}
