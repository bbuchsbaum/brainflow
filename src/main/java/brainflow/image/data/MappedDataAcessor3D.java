package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.interpolation.TrilinearInterpolator;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.Iterator3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2008
 * Time: 5:55:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MappedDataAcessor3D implements DataGrid3D {

    private final IImageSpace3D refSpace;

    private final IImageData3D data;

    private InterpolationFunction3D interp = new TrilinearInterpolator();

    public MappedDataAcessor3D(IImageSpace3D refSpace, IImageData3D data) {
        this.refSpace = refSpace;
        this.data = data;
    }

    public MappedDataAcessor3D(IImageSpace3D refSpace, IImageData3D data, InterpolationFunction3D interp) {
        this.refSpace = refSpace;
        this.data = data;
        this.interp = interp;
    }

    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        x = refSpace.gridToWorldX(x, y, z);
        y = refSpace.gridToWorldY(x, y, z);
        z = refSpace.gridToWorldZ(x, y, z);

        return data.worldValue(x,y,z, interp);

    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        return data.worldValue(realx, realy, realz, interp);
    }

    public double value(int x, int y, int z) {
        float xf = refSpace.gridToWorldX(x, y, z);
        float yf = refSpace.gridToWorldY(x, y, z);
        float zf = refSpace.gridToWorldZ(x, y, z);

        return data.worldValue(xf, yf, zf, interp);

    }

    public IImageSpace3D getImageSpace() {
        return refSpace;
    }

    public double value(int index) {
        int x = refSpace.indexToGridX(index);
        int y = refSpace.indexToGridY(index);
        int z = refSpace.indexToGridZ(index);

        return data.value(x,y,z);
    }

    public int numElements() {
        return refSpace.getNumSamples();
    }

    public ImageIterator iterator() {
        return new Iterator3D(this);
    }
}
