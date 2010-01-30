package brainflow.colormap;

import brainflow.image.data.RGBAImage;
import brainflow.image.data.IImageData2D;

import java.awt.*;
import java.util.ListIterator;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 16, 2009
 * Time: 8:06:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContinuousColorMap implements IColorMap {

    private MapToIndex mapper;

    @Override
    public int getMapSize() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ColorInterval getInterval(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Color getColor(double value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getHighClip() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getLowClip() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IColorMap newClipRange(double lowClip, double highClip, double min, double max) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getMaximumValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getMinimumValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListIterator<ColorInterval> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RGBAImage getRGBAImage(IImageData2D data) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AbstractColorBar createColorBar() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static abstract class BasicMapper implements MapToIndex {

        protected final IColorMap colorMap;

        protected final double range;

        protected final int size;

        public BasicMapper(IColorMap map) {
            colorMap = map;
            range = map.getHighClip() - map.getLowClip();
            size = map.getMapSize();
        }

    }

    public static class LinearMapper extends BasicMapper {

        public LinearMapper(IColorMap map) {
            super(map);
        }

        @Override
        public int map(double val) {
            int bin = (int) Math.round((((val - colorMap.getLowClip()) / range) * size));
            //todo may be unneccessary?
            if (bin < 0) bin = 0;
            if (bin >= size) {
                bin = size - 1;
            }

            return bin;

        }
    }

    public static class LogisticMapper extends BasicMapper {


        public LogisticMapper(IColorMap mapper) {
            super(mapper);
        }

        @Override
        public int map(double val) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}
