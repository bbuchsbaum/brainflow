package brainflow.functional;

import brainflow.image.io.ImageInfo;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.util.RandomAccessMode;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 10, 2004
 * Time: 4:52:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicFunctionalImageReader implements FunctionalImageReader {

    private ImageInfo info;
    private FileObject fobj;

    private int volLength;
    private int planeLength;
    private int planeWidth;

    public BasicFunctionalImageReader(ImageInfo _info, FileObject _data) {
        fobj = _data;
        info = _info;

        try {
            assert fobj.exists();
        } catch (FileSystemException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "File: " + fobj.getName() + " not found.");
        }


        Dimension3D d3d = new Dimension3D<Integer>(info.getArrayDim().getDim(0).intValue(),
                info.getArrayDim().getDim(1).intValue(),
                info.getArrayDim().getDim(2).intValue());


        volLength = d3d.zero.intValue() * d3d.one.intValue() * d3d.two.intValue();
        planeLength = d3d.zero.intValue() * d3d.one.intValue();
        planeWidth = d3d.zero.intValue();


    }


    private final int getOffset(int i, int j, int k, int t) {
        int offset = (volLength * t) + (k * planeLength) + (j * planeWidth + i);
        return offset;
    }

    private final double[] readShortSeries(RandomAccessContent rac, int idx3d, int tmin, int tmax) throws IOException {
        double[] data = new double[tmax - tmin + 1];
        for (int t = tmin, i = 0; t < tmax; t++, i++) {
            int offset = (idx3d + (t * volLength)) * 2;
            rac.seek(offset);
            data[i] = rac.readShort();
        }

        return data;

    }

    private final double[] readDoubleSeries(RandomAccessContent rac, int idx3d, int tmin, int tmax) throws IOException {
        double[] data = new double[tmax - tmin + 1];
        for (int t = tmin, i = 0; t < tmax; t++, i++) {
            int offset = (idx3d + (t * volLength)) * 8;
            rac.seek(offset);
            data[i] = rac.readDouble();
        }

        return data;
    }

    private final double[] readFloatSeries(RandomAccessContent rac, int idx3d, int tmin, int tmax) throws IOException {
        double[] data = new double[tmax - tmin + 1];
        for (int t = tmin, i = 0; t < tmax; t++, i++) {
            int offset = (idx3d + (t * volLength)) * 4;
            rac.seek(offset);
            data[i] = rac.readFloat();
        }

        return data;
    }

    private final double[] readIntegerSeries(RandomAccessContent rac, int idx3d, int tmin, int tmax) throws IOException {
        double[] data = new double[tmax - tmin + 1];
        for (int t = tmin, i = 0; t < tmax; t++, i++) {
            int offset = (idx3d + (t * volLength)) * 4;
            rac.seek(offset);
            data[i] = rac.readInt();
        }

        return data;
    }

    private final double[] readByteSeries(RandomAccessContent rac, int idx3d, int tmin, int tmax) throws IOException {
        double[] data = new double[tmax - tmin + 1];
        for (int t = tmin, i = 0; t < tmax; t++, i++) {
            int offset = (idx3d + (t * volLength));
            rac.seek(offset);
            data[i] = rac.readByte();
        }

        return data;
    }


    public double[] readSeries(int i, int j, int k) throws IOException {

        int tmax = info.getNumImages();
        int offset = getOffset(i, j, k, 0);

        RandomAccessContent rac = fobj.getContent().getRandomAccessContent(RandomAccessMode.READ);
        double[] ret = readSeries(rac, offset, 0, tmax);

        rac.close();

        return ret;

    }

    private double[] readSeries(RandomAccessContent rac, int idx, int tbegin, int tend) throws IOException {
        DataType dtype = info.getDataType();
        double[] ret = null;
        switch (dtype) {
            case SHORT:
                ret = readShortSeries(rac, idx, tbegin, tend);
            case FLOAT:
                ret = readFloatSeries(rac, idx, tbegin, tend);
            case DOUBLE:
                ret = readDoubleSeries(rac, idx, tbegin, tend);
            case BYTE:
                ret = readByteSeries(rac, idx, tbegin, tend);
            case INTEGER:
                ret = readIntegerSeries(rac, idx, tbegin, tend);
        }

        assert ret != null;
        return ret;

    }

    public double[] readSeries(int idx) throws IOException {
        int tmax = info.getNumImages();

        RandomAccessContent rac = fobj.getContent().getRandomAccessContent(RandomAccessMode.READ);
        double[] ret = readSeries(rac, idx, 0, tmax);
        rac.close();

        return ret;
    }

    public double[] readSeries(int i, int j, int k, int tbegin, int tend) throws IOException {

        int offset = getOffset(i, j, k, 0);
        RandomAccessContent rac = fobj.getContent().getRandomAccessContent(RandomAccessMode.READ);
        double[] ret = readSeries(rac, offset, tbegin, tend);

        rac.close();

        return ret;

    }

    public double[][] readMultipleSeries(int[] indices) throws IOException {
        return new double[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double[][] readMultipleSeries(int[] indices, int tbegin, int tend) throws IOException {
        return new double[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double[][] readMultipleSeries(int ibegin, int iend, int jbegin, int jend, int kbegin, int kend) {
        return new double[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double readValue(int idx, int t) throws IOException {
        return 0;

    }

    public double readValue(int i, int j, int k, int t) throws IOException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
