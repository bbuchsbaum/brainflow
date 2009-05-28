/*
 * Class.java
 *
 * Created on March 21, 2003, 1:28 PM
 */

package brainflow.image.io;

import brainflow.core.BrainFlowException;
import brainflow.image.io.ImageInfo;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Bradley
 */
public class AnalyzeInfoWriter implements brainflow.image.io.ImageInfoWriter {

    /**
     * Creates a new instance of Class
     */
    public AnalyzeInfoWriter() {
    }

    /**
     * Creates a new instance of ImageInfoWriter
     */
    public void writeInfo(File file, ImageInfo info) throws BrainFlowException {
        FileImageOutputStream ostream = null;
        try {
            String opname = AnalyzeInfoReader.getHeaderName(file.getAbsolutePath());
            ostream = new FileImageOutputStream(new File(opname));
            ostream.setByteOrder(info.getEndian());

            int sizeof_hdr = 348;

            ostream.writeInt(sizeof_hdr);
            ostream.write(data_type);
            ostream.write(db_name);

            ostream.writeInt(extents);
            ostream.writeShort(session_error);
            ostream.writeByte(regular);
            ostream.writeByte(hkey_un0);


            IImageSpace space = info.createImageSpace();

            int[] _dim = new int[] { space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS) };
            
            dim[0] = (short) _dim.length;
            dim[1] = (short) _dim[0];
            dim[2] = (short) _dim[1];
            dim[3] = (short) _dim[2];
            dim[4] = (short) info.getNumImages();
            dim[5] = (short) (_dim[0] / 2);
            dim[6] = (short) (_dim[1] / 2);
            dim[7] = (short) (_dim[2] / 2);

            ostream.writeShort(dim[0]);
            ostream.writeShort(dim[1]);
            ostream.writeShort(dim[2]);
            ostream.writeShort(dim[3]);
            ostream.writeShort(dim[4]);
            ostream.writeShort(dim[5]);
            ostream.writeShort(dim[6]);
            ostream.writeShort(dim[7]);

            ostream.write(vox_units);
            ostream.write(cal_units);
            ostream.writeShort(0);
            ostream.writeShort((short) info.getDataType().getDataCode());
            ostream.writeShort((short) info.getDataType().getBitsPerUnit());                            // bitpix
            ostream.writeShort(dim_un0);


            double[] _pixdim = new double[]{space.getSpacing(Axis.X_AXIS),
                    space.getSpacing(Axis.Y_AXIS),
                    space.getSpacing(Axis.Z_AXIS)};


            pixdim[1] = (float) _pixdim[0];
            pixdim[2] = (float) _pixdim[1];
            pixdim[3] = (float) _pixdim[2];


            double[] _extents = new double[]{space.getExtent(Axis.X_AXIS),
                    space.getExtent(Axis.Y_AXIS),
                    space.getExtent(Axis.Z_AXIS)};

            pixdim[4] = (float) _extents[0];
            pixdim[5] = (float) _extents[1];
            pixdim[6] = (float) _extents[2];

            for (int i = 0; i < pixdim.length; i++)
                ostream.writeFloat(pixdim[i]);

            ostream.writeFloat(vox_offset);

            ostream.writeFloat((float) info.getScaleFactor());
            ostream.writeFloat(funused2);
            ostream.writeFloat(funused3);
            ostream.writeFloat(cal_max);
            ostream.writeFloat(cal_min);
            ostream.writeInt(compressed);
            ostream.writeInt(verified);
            ostream.writeInt(glmax);
            ostream.writeInt(glmin);

            // data_history

            ostream.write(descrip);
            ostream.write(aux_file);
            //orient = (byte)vspace.getOrientation().getCode();
            ostream.writeByte(0);


            ostream.write(originator);
            ostream.write(generated);

            ostream.write(scannum);
            ostream.write(patient_id);
            ostream.write(exp_date);
            ostream.write(exp_time);
            ostream.write(hist_un0);

            ostream.writeInt(views);
            ostream.writeInt(vols_added);
            ostream.writeInt(start_field);
            ostream.writeInt(field_skip);
            ostream.writeInt(omax);
            ostream.writeInt(omin);
            ostream.writeInt(smax);
            ostream.writeInt(smin);
        }
        catch (FileNotFoundException e) {
            throw new BrainFlowException(e);
        }
        catch (IOException e2) {
            throw new BrainFlowException(e2);
        }

        finally {
            try {
                if (ostream != null) {
                    ostream.close();
                }
            } catch (IOException ex3) {
                throw new BrainFlowException(ex3);
            }
        }

    }

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
