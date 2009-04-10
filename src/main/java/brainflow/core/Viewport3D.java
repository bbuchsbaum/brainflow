package brainflow.core;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.ImageAxis;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.ImageSpace3D;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.BeanContainer;

import javax.swing.event.ListDataEvent;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 3:16:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Viewport3D  {

    public static IImageSpace3D EMPTY_SPACE = new ImageSpace3D(new ImageAxis(0, 100, Anatomy3D.getCanonicalAxial().XAXIS, 100),
            new ImageAxis(0, 100, Anatomy3D.getCanonicalAxial().YAXIS, 100),
            new ImageAxis(0, 100, Anatomy3D.getCanonicalAxial().ZAXIS, 100));

    private ImageViewModel viewModel;
    
    private IImageSpace3D bounds;

    public final Property<Double> XAxisMin = new ObservableProperty<Double>(0.0){
        public void set(Double aDouble) {
            super.set(aDouble);
            XAxisMax.set(get() + XAxisExtent.get());
        }
    };

    public final Property<Double> YAxisMin = new ObservableProperty<Double>(0.0){
        public void set(Double aDouble) {
            super.set(aDouble);
            YAxisMax.set(get() + YAxisExtent.get());
        }
    };

    public final Property<Double> ZAxisMin = new ObservableProperty<Double>(0.0){
        public void set(Double aDouble) {
            super.set(aDouble);
            ZAxisMax.set(get() + ZAxisExtent.get());
        }
    };

    public final Property<Double> XAxisExtent = new ObservableProperty<Double>(0.0){
            public void set(Double aDouble) {
                super.set(aDouble);
                XAxisMax.set(get() + XAxisMin.get());
            }
        };

        public final Property<Double> YAxisExtent = new ObservableProperty<Double>(0.0){
            public void set(Double aDouble) {
                super.set(aDouble);
                YAxisMax.set(get() + YAxisMin.get());
            }
        };

        public final Property<Double> ZAxisExtent = new ObservableProperty<Double>(0.0){
            public void set(Double aDouble) {
                super.set(aDouble);
                ZAxisMax.set(get() + ZAxisMin.get());
            }
        };




    public final Property<Double> XAxisMax = ObservableProperty.create(0.0);

    public final Property<Double> YAxisMax = ObservableProperty.create(0.0);

    public final Property<Double> ZAxisMax = ObservableProperty.create(0.0);


    public Viewport3D(ImageViewModel _displayModel) {
        BeanContainer.bind(this);

        viewModel = _displayModel;

        if (viewModel.size() != 0) {
            bounds = _displayModel.getImageSpace();

        } else {
            bounds = EMPTY_SPACE;
        }

        init();

    }


    public IImageSpace3D getBounds() {
        return bounds;
    }

    public boolean inBounds(AnatomicalPoint3D pt) {
        if (pt.getAnatomy() != bounds.getAnatomy()) {
            throw new IllegalArgumentException("supplied point must have same Anatomy as Viewport for inBounds test." +
                    "arg : " + pt.getAnatomy() + " viewport : " + bounds.getAnatomy());
        }
        
        if (!bounds.getImageAxis(pt.getAnatomy().XAXIS, true).getRange().contains(pt.getX())) return false;
        if (!bounds.getImageAxis(pt.getAnatomy().YAXIS, true).getRange().contains(pt.getY())) return false;
        if (!bounds.getImageAxis(pt.getAnatomy().ZAXIS, true).getRange().contains(pt.getZ())) return false;

        return true;
    }

    public boolean inBounds(AnatomicalAxis axis, double val) {
        return bounds.getImageAxis(axis, true).getRange().contains(val);
    }

    public double getXAxisMin() {
        return XAxisMin.get();
    }

    private void init() {
        setXAxisMin(bounds.getImageAxis(Axis.X_AXIS).getMinimum());
        setYAxisMin(bounds.getImageAxis(Axis.Y_AXIS).getMinimum());
        setZAxisMin(bounds.getImageAxis(Axis.Z_AXIS).getMinimum());
        setXAxisExtent(bounds.getExtent(Axis.X_AXIS));
        setYAxisExtent(bounds.getExtent(Axis.Y_AXIS));
        setZAxisExtent(bounds.getExtent(Axis.Z_AXIS));

    }


    public AnatomicalAxis getXAxis() {
        return bounds.getAnatomicalAxis(Axis.X_AXIS);
    }

    public AnatomicalAxis getYAxis() {
        return bounds.getAnatomicalAxis(Axis.Y_AXIS);
    }

    public AnatomicalAxis getZAxis() {
        return bounds.getAnatomicalAxis(Axis.Z_AXIS);
    }

    public Property<Double> getMinProperty(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return XAxisMin;
        } else if (axis.sameAxis(getYAxis())) {
            return YAxisMin;
        } else if (axis.sameAxis(getZAxis())) {
            return ZAxisMin;
        }

        throw new IllegalArgumentException("Invalid axis " + axis + " for viewport");

    }

    public Property<Double> getMaxProperty(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return XAxisMax;
        } else if (axis.sameAxis(getYAxis())) {
            return YAxisMax;
        } else if (axis.sameAxis(getZAxis())) {
            return ZAxisMax;
        }

        throw new IllegalArgumentException("Invalid axis " + axis + " for viewport");

    }

    public Property<Double> getExtentProperty(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return XAxisExtent;
        } else if (axis.sameAxis(getYAxis())) {
            return YAxisExtent;
        } else if (axis.sameAxis(getZAxis())) {
            return ZAxisExtent;
        }

        throw new IllegalArgumentException("Invalid axis " + axis + " for viewport");

    }


    public AxisRange getRange(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return new AxisRange(axis, getXAxisMin(), getXAxisMin() + getXAxisExtent());
        } else if (axis.sameAxis(getYAxis())) {
            return new AxisRange(axis, getYAxisMin(), getYAxisMin() + getYAxisExtent() );
        } else if (axis.sameAxis(getZAxis())) {
            return new AxisRange(axis, getZAxisMin(), getZAxisMin() + getZAxisExtent());
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }
    }

    public void setAxesRange(AnatomicalAxis axis1, double min1, double max1,
                             AnatomicalAxis axis2, double min2, double max2) {
        setAxisRange(axis1, min1, max1 - min1);
        setAxisRange(axis2, min2, max2 - min2);


    }



    public void setAxisRange(AnatomicalAxis axis, double min, double extent) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisMin(min);
            setXAxisExtent(extent);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisMin(min);
            setYAxisExtent(extent);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisMin(min);
            setZAxisExtent(extent);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }
    }

    public void setAxisMin(AnatomicalAxis axis, double min) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisMin(min);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisMin(min);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisMin(min);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }


    }

    public void setAxisExtent(AnatomicalAxis axis, double extent) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisExtent(extent);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisExtent(extent);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisExtent(extent);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }


    }


    public void setXAxisMin(double xAxisMin) {
        if (xAxisMin < bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + xAxisMin + "outside of image bounds");

        }

       XAxisMin.set(xAxisMin);

    }

    public double getYAxisMin() {
        return YAxisMin.get();
    }

    public void setYAxisMin(double yAxisMin) {
        if (yAxisMin < bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + yAxisMin + " is outside of image bounds");
        }

        YAxisMin.set(yAxisMin);
    }

    public double getZAxisMin() {
        return ZAxisMin.get();
    }


    public void setZAxisMin(double zAxisMin) {
          if (zAxisMin < bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + zAxisMin + " is outside of image bounds");
        }
        ZAxisMin.set(zAxisMin);
    }


    public double getXAxisMax() {
        return XAxisMax.get();
    }

    public double getYAxisMax() {
        return YAxisMax.get();
    }

    public double getZAxisMax() {
        return ZAxisMax.get();
    }

    public double getXAxisExtent() {
        return XAxisExtent.get();
    }

    public void setXAxisExtent(double xAxisExtent) {
       XAxisExtent.set(xAxisExtent);
    }


    public double getYAxisExtent() {
        return YAxisExtent.get();
    }

    public void setYAxisExtent(double yAxisExtent) {
        YAxisExtent.set(yAxisExtent);
    }



    public double getZAxisExtent() {
        return ZAxisExtent.get();
    }

    public void setZAxisExtent(double zAxisExtent) {
        ZAxisExtent.set(zAxisExtent);
    }

    

}
