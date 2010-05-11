/*
 * AnalyzeInfoReader.java
 *
 * Created on February 5, 2003, 3:49 PM
 */

package brainflow.image.io;

import brainflow.core.BrainFlowException;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.utils.Point3D;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;


/**
 * @author Bradley
 */
public class AnalyzeInfoReader extends AbstractInfoReader {


    private static final int HEADER_SIZE = 348;

    private static final int SWAPPED_HEADER_SIZE = 1543569408;


    private final static Logger log = Logger.getLogger(AnalyzeInfoReader.class.getCanonicalName());


    protected AnalyzeInfoReader() {
        super();
    }

    public AnalyzeInfoReader(FileObject headerFile, FileObject dataFile) {
        super(headerFile, dataFile);
    }

    public AnalyzeInfoReader(File headerFile, File dataFile) {
        super(headerFile, dataFile);
    }

    public AnalyzeInfoReader(String name) {
        super(new File(AnalyzeInfoReader.getHeaderName(name)), new File(AnalyzeInfoReader.getImageName(name)));
    }

    @Override
    public ImageInfoReader create(FileObject headerFile, FileObject dataFile) {
        return new AnalyzeInfoReader(headerFile, dataFile);
    }

    public static boolean isHeaderFile(String name) {
        if (name.endsWith(".hdr")) {
            return true;
        }

        return false;
    }

    public static boolean isImageFile(String name) {
        if (name.endsWith(".img")) {
            return true;
        }

        return false;
    }

    public static String getHeaderName(String name) {
        if (name.endsWith(".img")) {
            name = name.substring(0, name.length() - 3);
            return name + "hdr";
        }
        if (name.endsWith(".hdr")) {
            return name;
        } else
            return name + ".hdr";
    }

    public static String getImageName(String name) {
        if (name.endsWith(".hdr")) {
            name = name.substring(0, name.length() - 3);
            return name + "img";
        }
        if (name.endsWith(".img")) {
            return name;
        } else
            return name + ".img";
    }

    @Override
    public ImageInfo readInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ImageInfo> readInfoList() throws BrainFlowException {
        try {
            InputStream istream = headerFile.getContent().getInputStream();
            MemoryCacheImageInputStream mis = new MemoryCacheImageInputStream(istream);
            return readHeader(mis);
        } catch (FileSystemException e) {
            throw new BrainFlowException(e);
        }


    }

    


    private List<ImageInfo> readHeader(ImageInputStream istream) throws BrainFlowException {
        ImageInfo.Builder builder = new ImageInfo.Builder();
        builder.headerFile(headerFile);
        builder.dataFile(dataFile);

        readBeginning(istream);

        if (sizeof_hdr == AnalyzeInfoReader.HEADER_SIZE) {
            builder.endian(ByteOrder.BIG_ENDIAN);
        } else if (sizeof_hdr == AnalyzeInfoReader.SWAPPED_HEADER_SIZE) {
            builder.endian(ByteOrder.LITTLE_ENDIAN);
            istream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        } else throw new BrainFlowException("Illegal Value (" + sizeof_hdr + ") for header size, cannot proceed!");

        try {
            dim[0] = istream.readShort();
            dim[1] = istream.readShort();
            dim[2] = istream.readShort();
            dim[3] = istream.readShort();
            dim[4] = istream.readShort();
            dim[5] = istream.readShort();
            dim[6] = istream.readShort();
            dim[7] = istream.readShort();


            builder.volumeDim(new Dimension3D<Integer>(Math.abs(dim[1]), Math.abs(dim[2]), Math.abs(dim[3])));

            builder.numVolumes(Math.abs(dim[4]));


           if (dim[4] > 1) {
               builder.dimensionality(4);
           } else {
               builder.dimensionality(3);
           }

            builder.voxelOffset(new Dimension3D<Integer>((int) dim[5], (int) dim[6], (int) dim[7]));


            istream.read(vox_units);
            istream.read(cal_units);
            istream.readShort(); // unused short


            dtype = istream.readShort();
            DataType dataType;

            switch (dtype) {

                case 1:
                    dataType = DataType.BOOLEAN;
                    break;
                case 2:
                    dataType = DataType.BYTE;
                    break;
                case 4:
                    dataType = DataType.SHORT;
                    break;
                case 8:
                    dataType = DataType.INTEGER;
                    break;
                case 16:
                    dataType = DataType.FLOAT;
                    break;
                case 32:
                    dataType = DataType.DOUBLE;
                    break;
                default:
                    dataType = null;
            }

            if (dataType == null) {

                throw new BrainFlowException("Illegal Data Type: " + dataType);
            }

            builder.dataType(dataType);

            bitpix = istream.readShort();                            // bitpix
            dim_un0 = istream.readShort();
            for (int i = 0; i < pixdim.length; i++) {
                pixdim[i] = istream.readFloat();
            }

            builder.spacing(new Dimension3D<Double>((double) (Math.abs(pixdim[1])),
                    (double) (Math.abs(pixdim[2])), (double) (Math.abs(pixdim[3]))));

            builder.origin(new Point3D(pixdim[4], pixdim[5], pixdim[6]));


            vox_offset = istream.readFloat();
            spmScale = istream.readFloat();

            // need to rethink this
            if (spmScale >= 0 && spmScale < .000001) {
                spmScale = 1f;
            }
            /////////////////////////////////////


            builder.scaleFactor(spmScale);
            builder.intercept(0);

            funused2 = istream.readFloat();
            funused3 = istream.readFloat();


            cal_max = istream.readFloat();
            cal_min = istream.readFloat();
            compressed = istream.readInt();
            verified = istream.readInt();
            glmax = istream.readInt();
            glmin = istream.readInt();


            istream.read(descrip);
            istream.read(aux_file);
            orient = istream.readByte();

            istream.read(originator);
            istream.read(generated);

            istream.read(scannum);
            istream.read(patient_id);
            istream.read(exp_date);
            istream.read(exp_time);
            istream.read(hist_un0);

            views = istream.readInt();
            vols_added = istream.readInt();
            start_field = istream.readInt();
            field_skip = istream.readInt();
            omax = istream.readInt();
            omin = istream.readInt();
            smax = istream.readInt();
            smin = istream.readInt();

            istream.close();
            return Arrays.asList(builder.build());
        }
        catch (IOException e) {
            throw new BrainFlowException(e);
        }




    }


    private void readBeginning(ImageInputStream istream) throws BrainFlowException {
        try {
            //istream.seek(0);
            sizeof_hdr = istream.readInt();

            istream.readFully(data_type);
            istream.readFully(db_name);

            extents = istream.readInt();
            session_error = istream.readShort();
            regular = istream.readByte();
            hkey_un0 = istream.readByte();
        } catch (Exception e) {
            throw new BrainFlowException(e);
        }
    }

    public static void main(String[] args) {

    }


    private float spmScale = 1f;

    private int sizeof_hdr;
    private byte[] data_type = new byte[10];
    private byte[] db_name = new byte[18];
    private int extents;
    private short session_error;
    private byte regular;
    private byte hkey_un0;


    private short[] dim = new short[8];

    private byte[] vox_units = new byte[4];
    private byte[] cal_units = new byte[8];
    private short dtype;
    private short bitpix;
    private short dim_un0;
    private float[] pixdim = new float[8];
    private float vox_offset;
    private float funused1;
    private float funused2;
    private float funused3;
    private float cal_max;
    private float cal_min;
    private int compressed;
    private int verified;
    private int glmax, glmin;

    // data_history

    private byte[] descrip = new byte[80];
    private byte[] aux_file = new byte[24];
    private byte orient;
    private byte[] originator = new byte[10];
    private byte[] generated = new byte[10];
    private byte[] scannum = new byte[10];
    private byte[] patient_id = new byte[10];
    private byte[] exp_date = new byte[10];
    private byte[] exp_time = new byte[10];
    private byte[] hist_un0 = new byte[3];
    private int views;
    private int vols_added;
    private int start_field;
    private int field_skip;
    private int omax;
    private int omin;
    private int smax;
    private int smin;


}
