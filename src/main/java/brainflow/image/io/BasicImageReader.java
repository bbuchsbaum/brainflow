package brainflow.image.io;

import brainflow.app.BrainFlowException;
import brainflow.image.data.*;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.ImageSpace3D;
import brainflow.utils.DataType;
import brainflow.utils.NumberUtils;
import brainflow.utils.ProgressListener;
import brainflow.utils.ProgressAdapter;
import org.apache.commons.vfs.FileObject;

import java.io.BufferedInputStream;
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

public class BasicImageReader implements ImageReader {

    final static Logger log = Logger.getLogger(BasicImageReader.class.getCanonicalName());

    private static final int NUM_CHUNKS = 20;

    private ImageInfo info;

    private int fileDimensionality = 2;

    private DataType datatype;

    private IImageSpace imageSpace;

    //todo this is not set when no arg constructor is used
    private FileObject inputFile;

    private ByteOrder byteOrder = java.nio.ByteOrder.BIG_ENDIAN;

    public ByteBuffer buffer;


    private int skipUnits = 0;

    public BasicImageReader() {
    }


    public BasicImageReader(ImageInfo info) {
        setImageInfo(info);

    }

    public void setImageInfo(ImageInfo _info) {
        info = _info;

        setByteOrder(info.getEndian());
        setByteOffset(info.getDataOffset(info.getImageIndex()));

        setFileDimensionality(info.getDimensionality());
        setDataType(info.getDataType());
        setImageSpace(info.createImageSpace());
        inputFile = info.getDataFile();

    }

    public int getByteOffset() {
        return skipUnits;
    }

    public void setByteOffset(int skipUnits) {
        this.skipUnits = skipUnits;
    }


    public void setByteOrder(ByteOrder _byteOrder) {
        byteOrder = _byteOrder;
    }

    public void setFileDimensionality(int dim) {
        fileDimensionality = dim;
    }

    public void setDataType(DataType _dtype) {
        datatype = _dtype;
    }

    public void setImageSpace(IImageSpace _iSpace) {
        imageSpace = _iSpace;
    }

    public IImageSpace getImageSpace() {
        return imageSpace;
    }


    protected IImageData getOutput(ProgressListener listener) throws BrainFlowException {

        InputStream istream = null;

        try {


            int numBytes = imageSpace.getNumSamples() * datatype.getBytesPerUnit();
            int chunkSize = numBytes / NUM_CHUNKS;
            int lastChunk = numBytes % NUM_CHUNKS;
            listener.setMinimum(0);
            listener.setMaximum(numBytes);
            listener.setString("Opening Image Stream ...");

            //log.info("Input file system type: " + inputFile.getFileSystem());
            istream = new BufferedInputStream(inputFile.getContent().getInputStream());
            log.info("Getting input Stream ...");
            log.info("Input Stream is " + istream.getClass().getCanonicalName());
            istream.read(new byte[getByteOffset()]);

            //rac = inputFile.getContent().getRandomAccessContent(RandomAccessMode.READ);
            //rac.seek(getDataOffset());


            listener.setString("Allocating memory ...");
            ByteBuffer wholeBuffer = ByteBuffer.allocate(numBytes);

            wholeBuffer.order(byteOrder);
            listener.setValue(0);
            listener.setString("Reading Image Data ...");

            byte[] tmpdata = new byte[chunkSize];

            for (int i = 0; i < NUM_CHUNKS; i++) {
                istream.read(tmpdata);
                //rac.readFully(tmpdata);
                wholeBuffer.put(tmpdata);
                listener.setValue(i * chunkSize);

            }

            byte[] lastData = new byte[lastChunk];
            istream.read(lastData);
            //rac.readFully(lastData);

            wholeBuffer.put(lastData);
            listener.setValue(numBytes);
            listener.setString("Finished Reading Data");


            wholeBuffer.rewind();
            listener.setString("Converting Bytes To Array of Type: " + datatype);

            Object data;

            double sf = info.getScaleFactor();
            boolean scaleRequired = true;
            if (NumberUtils.equals(sf, 1, .0000001) || NumberUtils.equals(sf, 0, .0000001)) {
                scaleRequired = false;
            }

            if (datatype == DataType.BYTE) {
                data = new byte[numBytes];
                wholeBuffer.get((byte[]) data);
            } else if (datatype == DataType.UBYTE) {
                data = new byte[imageSpace.getNumSamples()];
                wholeBuffer.get((byte[]) data);
            } else if (datatype == DataType.SHORT) {
                data = new short[imageSpace.getNumSamples()];
                wholeBuffer.asShortBuffer().get((short[]) data);
            } else if (datatype == DataType.FLOAT) {
                data = new float[imageSpace.getNumSamples()];
                wholeBuffer.asFloatBuffer().get((float[]) data);
            } else if (datatype == DataType.DOUBLE) {
                data = new double[imageSpace.getNumSamples()];
                wholeBuffer.asDoubleBuffer().get((double[]) data);
            } else if (datatype == DataType.INTEGER) {
                data = new int[imageSpace.getNumSamples()];
                wholeBuffer.asIntBuffer().get((int[]) data);

            } else {
                throw new RuntimeException("BasicImageReader.getOutput(): Illegal Data Type: " + datatype.toString());
            }

            //if (scaleRequired) {
            //    data = ArrayUtils.scale(data, sf);
            //}

            if (fileDimensionality == 2) {
                BasicImageData dat = new BasicImageData2D((ImageSpace2D) imageSpace, data, info.getDataFile().getName().getBaseName());

                //dat.setImageLabel(info.getDataFile().getName().getBaseName());
                listener.finished();
                return dat;
            } else if (fileDimensionality == 3) {

                IImageData3D dat = new BasicImageData3D((ImageSpace3D) imageSpace, data, info.getDataFile().getName().getBaseName());
                if (scaleRequired) {
                    dat = ImageData.createScaledData(dat, sf);
                }
                listener.finished();
                return dat;
            } else
                throw new RuntimeException("BasicImageReader.getOutput(): Dimensionality of: " + fileDimensionality + " not supported!");

        } catch (FileNotFoundException e1) {
            throw new BrainFlowException(e1);
        } catch (IOException e2) {
            throw new BrainFlowException(e2);
        } finally {
            //if (rac != null)
            //rac.close();
            if (istream != null) {
                try {
                    istream.close();
                } catch(IOException e3) {}
            }
        }


    }


    // implement progress mechanism
    /*protected IImageData getOutput() throws IOException {

        FileChannel inChannel = null;

        try {
            int numBytes = imageSpace.getNumSamples() * datatype.getBytesPerUnit();


            byte[] bdata = FileUtil.getContent(inputFile);

            if ((bdata.length - getByteOffset()) != numBytes) {
                throw new IOException("Wrong number of bytes read in image file " + inputFile + " expected: " + numBytes + ", got: " + bdata.length);
            }

            //FileInputStream istream = new FileInputStream(ifile);
            //inChannel = istream.getChannel();
            //inChannel.position(skipUnits * datatype.getBytesPerUnit());

            buffer = ByteBuffer.allocate(numBytes);

            buffer.put(bdata, getByteOffset(), numBytes);
            buffer.order(byteOrder);
            //inChannel.read(buffer);

            buffer.rewind();

            Object dataArray = null;

            boolean scaleRequired = true;
            if (NumberUtils.equals(info.getScaleFactor(), 1, .000001) || NumberUtils.equals(info.getScaleFactor(), 0, .0000001)) {
                scaleRequired = false;
            }
            if (datatype == DataType.BYTE) {
                dataArray = bdata;
            } else if (datatype == DataType.SHORT) {
                dataArray = new short[imageSpace.getNumSamples()];
                buffer.asShortBuffer().get((short[]) dataArray);
            } else if (datatype == DataType.FLOAT) {
                dataArray = new float[imageSpace.getNumSamples()];
                buffer.asFloatBuffer().get((float[]) dataArray);
            } else if (datatype == DataType.DOUBLE) {
                dataArray = new double[imageSpace.getNumSamples()];
                buffer.asDoubleBuffer().get((double[]) dataArray);
            } else if (datatype == DataType.INTEGER) {
                dataArray = new int[imageSpace.getNumSamples()];
                buffer.asIntBuffer().get((int[]) dataArray);

            } else {
                throw new RuntimeException("BasicImageReader.getOutput(): Illegal Data Type: " + datatype.toString());
            }


            if (fileDimensionality == 2) {
                IImageData2D data2d = new BasicImageData2D((ImageSpace2D) imageSpace, dataArray, info.getDataFile().getName().getBaseName());
                //data2d.setImageLabel(info.getDataFile().getName().getBaseName());
                return data2d;
            } else if (fileDimensionality == 3) {
                IImageData3D data3d = new BasicImageData3D((ImageSpace3D) imageSpace, dataArray, info.getDataFile().getName().getBaseName());
                //data3d.setImageLabel(info.getDataFile().getName().getBaseName());
                return data3d;
            } else {
                throw new RuntimeException("BasicImageReader.getOutput(): Dimensionality of: " + fileDimensionality + " not supported!");
            }

        } catch (FileNotFoundException e1) {
            throw e1;
        } catch (IOException e2) {
            throw e2;
        } finally {
            if (inChannel != null)
                inChannel.close();
        }


    }   */


    public IImageData readImage(ImageInfo info) throws BrainFlowException {
        setImageInfo(info);


        IImageData data = null;

        try {
            data = getOutput(new ProgressAdapter());
        } catch (BrainFlowException e) {            
            throw e;
        }

        return data;

    }

    public IImageData readImage(ImageInfo info, ProgressListener plistener) throws BrainFlowException {

        setImageInfo(info);
        IImageData data = null;

        try {
            data = getOutput(plistener);

        } catch (BrainFlowException e) {
            throw e;
        } 

        return data;
    }
}