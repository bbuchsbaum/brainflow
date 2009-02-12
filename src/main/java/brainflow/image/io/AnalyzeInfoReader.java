/*
 * AnalyzeInfoReader.java
 *
 * Created on February 5, 2003, 3:49 PM
 */

package brainflow.image.io;

import brainflow.application.BrainFlowException;
import brainflow.image.io.ImageInfo;
import brainflow.image.io.ImageInfoReader;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.utils.Point3D;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;
import java.net.URL;


/**
 * @author Bradley
 */
public class AnalyzeInfoReader implements ImageInfoReader {

    private boolean verbose = false;

    private static final int HEADER_SIZE = 348;

    private static final int SWAPPED_HEADER_SIZE = 1543569408;


    final static Logger log = Logger.getLogger(AnalyzeInfoReader.class.getCanonicalName());

    /**
     * Creates a new instance of AnalyzeInfoReader
     */

    public AnalyzeInfoReader() {

    }


    public static boolean isHeaderFile(String name) {
        if (name.endsWith(".hdr")) {
            return true;
        }

        return false;
    }

    public static boolean isImageFile(String name) {
        if (name.endsWith(".img") ) {
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

    public static String getStem(String name) {
        //todo check if valid header name
        return name.substring(0, name.length() - 4);

    }


    public List<ImageInfo> readInfo(File f) throws BrainFlowException {
        List<ImageInfo> ret = null;

        String headerName = AnalyzeInfoReader.getHeaderName(f.getAbsolutePath());
        f = new File(headerName);

        try {
            FileImageInputStream istream = new FileImageInputStream(f);
            ret = readHeader(istream);
            FileObject dataFile = VFS.getManager().resolveFile(f.getParentFile(), AnalyzeInfoReader.getImageName(f.getName()));
            FileObject headerFile = VFS.getManager().resolveFile(f.getParentFile(), AnalyzeInfoReader.getHeaderName(f.getName()));
            for (ImageInfo ii : ret) {
                ii.setDataFile(dataFile);
                ii.setHeaderFile(headerFile);
                ii.setImageLabel(getStem(headerFile.getName().getBaseName()));
            }


        } catch (Exception e) {
            log.warning("Exception caught in AnalyzeInfoReader.readInfo ");
            throw new BrainFlowException(e);
        }


        return ret;

    }

     public List<ImageInfo> readInfo(URL url) throws BrainFlowException {
        try {
            FileObject fobj = VFS.getManager().resolveFile(url.toString());
            return readInfo(fobj);
        } catch(FileSystemException e) {
            throw new BrainFlowException(e);
        }

    }

    public List<? extends ImageInfo> readInfo(InputStream istream) throws BrainFlowException {
        List<ImageInfo> ret = null;

        try {
            MemoryCacheImageInputStream mis = new MemoryCacheImageInputStream(istream);
            ret  = readHeader(mis);
        } catch (Exception e) {
            throw new BrainFlowException(e);
        }

        return ret;

    }

    public List<ImageInfo> readInfo(FileObject fobj) throws BrainFlowException {
        List<ImageInfo> ret = null;

        try {
            InputStream istream = fobj.getContent().getInputStream();
            MemoryCacheImageInputStream mis = new MemoryCacheImageInputStream(istream);
            ret  = readHeader(mis);
            FileObject dataFile = VFS.getManager().resolveFile(fobj.getParent(), AnalyzeInfoReader.getImageName(fobj.getName().getBaseName()));
            FileObject headerFile = VFS.getManager().resolveFile(fobj.getParent(), AnalyzeInfoReader.getHeaderName(fobj.getName().getBaseName()));
            for (ImageInfo ii : ret) {
                ii.setDataFile(dataFile);
                ii.setHeaderFile(headerFile);
                 ii.setImageLabel(getStem(headerFile.getName().getBaseName()));
            }

        } catch (Exception e) {
            throw new BrainFlowException(e);
        }

        return ret;
    }

  

    private List<ImageInfo> readHeader(ImageInputStream istream) throws BrainFlowException {
        ImageInfo info = new ImageInfo();

        readBeginning(istream);

        if (sizeof_hdr == AnalyzeInfoReader.HEADER_SIZE) {
            info.setEndian(ByteOrder.BIG_ENDIAN);
        } else if (sizeof_hdr == AnalyzeInfoReader.SWAPPED_HEADER_SIZE) {
            info.setEndian(ByteOrder.LITTLE_ENDIAN);
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


            info.setArrayDim(new Dimension3D(Math.abs(dim[1]), Math.abs(dim[2]), Math.abs(dim[3])));

            info.setNumImages(Math.abs(dim[4]));

            if (dim[4] > 1)
                info.setDimensionality(4);


            info.setVoxelOffset(new Dimension3D(dim[5], dim[6], dim[7]));


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

            //logger.log(Level.INFO, "Data Type: " + dataType);
            info.setDataType(dataType);

            bitpix = istream.readShort();                            // bitpix
            dim_un0 = istream.readShort();
            for (int i = 0; i < pixdim.length; i++) {
                pixdim[i] = istream.readFloat();
            }

            info.setSpacing(new Dimension3D<Double>((double) (Math.abs(pixdim[1])),
                    (double) (Math.abs(pixdim[2])), (double) (Math.abs(pixdim[3]))));

            info.setOrigin(new Point3D(pixdim[4], pixdim[5], pixdim[6]));

            /*info.setRealDim(new Dimension3D<Double>((double)(Math.abs(pixdim[1]) * dim[1]),
                    (double)(Math.abs(pixdim[2]) * dim[2]),
                    (double)(Math.abs(pixdim[3]) * dim[3])));  */


            vox_offset = istream.readFloat();
            spmScale = istream.readFloat();

            // need to rethink this
            if (spmScale >= 0 && spmScale < .000001) {
                spmScale = 1f;
            }
            /////////////////////////////////////


            info.setScaleFactor(spmScale);
            info.setIntercept(0);

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
        }
        catch (IOException e) {
            throw new BrainFlowException(e);
        }


        return Arrays.asList(info);

    }


    private void readBeginning(ImageInputStream istream) throws BrainFlowException {
        try {
            istream.seek(0);
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
        AnalyzeInfoReader reader = new AnalyzeInfoReader();
        try {
            List<ImageInfo> info = reader.readInfo(new File(args[0]));
            java.util.Date d = new java.util.Date();
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    float spmScale = 1f;

    int sizeof_hdr;
    byte[] data_type = new byte[10];
    byte[] db_name = new byte[18];
    int extents;
    short session_error;
    byte regular;
    byte hkey_un0;


    short[] dim = new short[8];

    byte[] vox_units = new byte[4];
    byte[] cal_units = new byte[8];
    short dtype;
    short bitpix;
    short dim_un0;
    float[] pixdim = new float[8];
    float vox_offset;
    float funused1;
    float funused2;
    float funused3;
    float cal_max;
    float cal_min;
    int compressed;
    int verified;
    int glmax, glmin;

    // data_history

    byte[] descrip = new byte[80];
    byte[] aux_file = new byte[24];
    byte orient;
    byte[] originator = new byte[10];
    byte[] generated = new byte[10];
    byte[] scannum = new byte[10];
    byte[] patient_id = new byte[10];
    byte[] exp_date = new byte[10];
    byte[] exp_time = new byte[10];
    byte[] hist_un0 = new byte[3];
    int views;
    int vols_added;
    int start_field;
    int field_skip;
    int omax;
    int omin;
    int smax;
    int smin;


}
