package brainflow.image.io;

import brainflow.core.BrainFlowException;
import brainflow.image.data.*;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.ImageSpace3D;
import brainflow.utils.DataType;
import brainflow.utils.NumberUtils;
import brainflow.utils.ProgressListener;
import brainflow.utils.ProgressAdapter;
import org.apache.commons.vfs.FileObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BasicImageReader3D extends AbstractImageReader {

    private static final Logger log = Logger.getLogger(BasicImageReader3D.class.getCanonicalName());

    private static final int NUM_CHUNKS = 20;

    public BasicImageReader3D(ImageInfo info) {
        super(info);

    }


    public IImageSpace3D getImageSpace() {
        return (IImageSpace3D) super.getImageSpace();
    }

    @Override
    public int getDimensionality() {
        return 3;
    }

    protected IImageData3D getOutput(ProgressListener listener) throws BrainFlowException {

        InputStream istream = null;
        IImageData3D dat = null;
        boolean scaleRequired = true;
        double sf = getImageInfo().getScaleFactor();
        if (NumberUtils.equals(sf, 1, .00000000001) || NumberUtils.equals(sf, 0, 0.0000000001)) {
            scaleRequired = false;
        }

        try {

            IImageSpace3D imageSpace = getImageSpace();
            int numBytes = imageSpace.getNumSamples() * getDatatype().getBytesPerUnit();
            int chunkSize = numBytes / NUM_CHUNKS;
            int lastChunk = numBytes % NUM_CHUNKS;


            listener.setMinimum(0);
            listener.setMaximum(numBytes);
            listener.setString("Opening Image Stream ...");


            istream = getInputFile().getContent().getInputStream();

            log.info("Getting input Stream ...");
            log.info("Input Stream is " + istream.getClass().getCanonicalName());

            long nread = istream.skip(getByteOffset());


            listener.setString("Allocating memory ...");
            ByteBuffer wholeBuffer = ByteBuffer.allocate(numBytes);

            wholeBuffer.order(getByteOrder());
            listener.setValue(0);
            listener.setString("Reading Image Data ...");

            byte[] tmpdata = new byte[chunkSize];

            for (int i = 0; i < NUM_CHUNKS; i++) {
                nread = istream.read(tmpdata);
                wholeBuffer.put(tmpdata);
                listener.setValue(i * chunkSize);

            }

            byte[] lastData = new byte[lastChunk];
            nread = istream.read(lastData);

            wholeBuffer.put(lastData);
            wholeBuffer.rewind();
            listener.setString("Converting Bytes To Array of Type: " + getDatatype());


            if (getDatatype() == DataType.BYTE) {
                byte[] data = new byte[numBytes];
                wholeBuffer.get(data);
                /// always unsigned byte ...
                dat = new BasicImageData3D.UByte(imageSpace, data);
            } else if (getDatatype() == DataType.UBYTE) {
                byte[] data = new byte[imageSpace.getNumSamples()];
                wholeBuffer.get(data);
                dat = new BasicImageData3D.UByte(imageSpace, data);
            } else if (getDatatype() == DataType.SHORT) {
                short[] data = new short[imageSpace.getNumSamples()];
                wholeBuffer.asShortBuffer().get(data);
                dat = new BasicImageData3D.Short(imageSpace, data);
            } else if (getDatatype() == DataType.FLOAT) {
                float[] data = new float[imageSpace.getNumSamples()];
                wholeBuffer.asFloatBuffer().get(data);
                dat = new BasicImageData3D.Float(imageSpace, data);
            } else if (getDatatype() == DataType.DOUBLE) {
                double[] data = new double[imageSpace.getNumSamples()];
                wholeBuffer.asDoubleBuffer().get(data);
                dat = new BasicImageData3D.Double(imageSpace, data);
            } else if (getDatatype() == DataType.INTEGER) {
                int[] data = new int[imageSpace.getNumSamples()];
                wholeBuffer.asIntBuffer().get(data);
                dat = new BasicImageData3D.Int(imageSpace, data);

            } else {
                throw new RuntimeException("BasicImageReader.getOutput(): Illegal Data Type: " + getDatatype().toString());
            }

            listener.setValue(numBytes);
            listener.setString("Finished Reading Data");


        } catch (FileNotFoundException e1) {
            throw new BrainFlowException(e1);
        } catch (IOException e2) {
            throw new BrainFlowException(e2);
        } finally {

            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException e3) {
                    throw new RuntimeException(e3);
                }
            }
        }

        listener.finished();

        if (scaleRequired) {
            dat = Data.createScaledData(dat, sf);

        }
        return dat;


    }


    @Override
    public IImageData3D readImage() throws BrainFlowException {

        return getOutput(new ProgressAdapter());


    }

    @Override
    public IImageData3D readImage(ProgressListener plistener) throws BrainFlowException {
        return getOutput(plistener);


    }
}