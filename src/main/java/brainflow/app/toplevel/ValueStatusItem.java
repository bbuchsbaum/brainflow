package brainflow.app.toplevel;


import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.app.services.ImageViewCursorEvent;
import brainflow.app.services.ImageViewMousePointerEvent;
import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import com.jidesoft.status.LabelStatusBarItem;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 24, 2010
 * Time: 3:27:32 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class ValueStatusItem extends LabelStatusBarItem implements EventSubscriber {

    private static final Map<Color, ImageIcon> colorMap = new HashMap<Color, ImageIcon>();

    private NumberFormat format = NumberFormat.getNumberInstance();

    public ValueStatusItem() {
        super();

        setIcon(ColorTable.createImageIcon(Color.GRAY, 40, 15));
        setText("Value :");
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setMinimumSize(new Dimension(150, 20));
        initListener();
    }

    public abstract void initListener();

    protected void updateValue(ImageLayer layer, double value) {
        IColorMap cmap = layer.getLayerProps().getColorMap();

        Color c = null;
        try {
            c = cmap.getColor(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageIcon icon = colorMap.get(c);
        if (icon == null) {
            icon = ColorTable.createImageIcon(c, 40, 15);
            colorMap.put(c, icon);
            if (colorMap.size() > 256) {
                colorMap.clear();

            }
        }

        setText("Value: " + format.format(value));
        this.setIcon(icon);


    }

    public static class Mouse extends ValueStatusItem {

        public Mouse() {
            super();
        }

        @Override
        public void initListener() {
            EventBus.subscribeExactly(ImageViewMousePointerEvent.class, this);
            //To change body of implemented methods use File | Settings | File Templates.
        }

        private boolean validEvent(ImageViewMousePointerEvent event) {
            //todo only publish events when cursorPos is over valid view
            ImageView view = event.getImageView();

            if (view == null) {
                //todo this should be impossible
                return false;
            }
            if (view.getModel().getSelectedIndex() < 0) {
                // an empty view ... hmmm
                return false;
            }

            if (event.getLocation() == null) {
                // well, shouldn't really happen but ...
                return false;
            }

            return true;

        }

        public void onEvent(Object evt) {
            ImageViewMousePointerEvent event = (ImageViewMousePointerEvent) evt;

            //todo only publish events when cursorPos is over valid view
            ImageView view = event.getImageView();
            if (!validEvent(event)) return;

            GridLoc3D gpoint = event.getLocation();
            ImageLayer3D layer = view.getSelectedLayer();


            double value = layer.getValue(gpoint);
            updateValue(layer, value);


        }
    }

    public static class Crosshair extends ValueStatusItem {
        @Override
        public void initListener() {
            EventBus.subscribeExactly(ImageViewCursorEvent.class, this);
        }

        private boolean validEvent(ImageViewCursorEvent event) {
            //todo only publish events when cursorPos is over valid view
            ImageView view = event.getImageView();

            if (view == null) {
                //todo this should be impossible
                return false;
            }
            if (view.getModel().getSelectedIndex() < 0) {
                // an empty view ... hmmm
                return false;
            }

            if (event.getCursor() == null) {
                // well, shouldn't really happen but ...
                return false;
            }

            return true;

        }

        @Override
        public void onEvent(Object evt) {
            ImageViewCursorEvent event = (ImageViewCursorEvent) evt;
            ImageView view = event.getImageView();
            if (!validEvent(event)) return;
            SpatialLoc3D spoint = event.getCursorWorld();
            ImageLayer3D layer = view.getSelectedLayer();

            double value = layer.getData().worldValue((float)spoint.getX(), (float)spoint.getY(), (float)spoint.getZ(), new NearestNeighborInterpolator());
           // double file = layer.getValue(gpoint);
            updateValue(layer, value);

        }
    }


}
