package brainflow.image.io;

import brainflow.core.BrainFlowException;

import java.io.*;
import java.util.Collection;

import static brainflow.image.io.Nifti1Dataset.*;
import brainflow.utils.IDimension;
import brainflow.math.Vector3f;
import brainflow.math.Matrix4f;

import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 4, 2009
 * Time: 2:25:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiInfoWriter implements ImageInfoWriter<NiftiImageInfo> {

    public static void main(String[] args) {
        try {
           // Collection ret = VFS.getManager().getProviderCapabilities("tmp");
            FileObject fobj = VFS.getManager().createVirtualFileSystem(VFS.getManager().resolveFile("tmp://"));
            FileObject tmpheader = fobj.getFileSystem().resolveFile("charlie.nii");
            FileObject tmpdata = fobj.getFileSystem().resolveFile("charlie.nii");

            NiftiInfoReader reader = new NiftiInfoReader("src/main/groovy/testdata/207_anat_alepi.nii");
            NiftiImageInfo info = (NiftiImageInfo)(reader.readInfo().get(0));

            NiftiImageInfo cinfo = info.copy(tmpheader, tmpdata);
            System.out.println("cinfo : " + cinfo.getHeaderFile());
            NiftiInfoWriter writer = new NiftiInfoWriter();
            writer.writeInfo(cinfo);

            FileObject[] children = fobj.getFileSystem().getRoot().getChildren();
            System.out.println("children length " + children.length);

            reader = new NiftiInfoReader(cinfo.getHeaderFile(), cinfo.getDataFile());
            NiftiImageInfo info2 = (NiftiImageInfo)(reader.readInfo().get(0));




        } catch(IOException e1) {
            e1.printStackTrace();
        } catch(BrainFlowException e2) {
            e2.printStackTrace();
        }


    }

    private byte[] setStringSize(StringBuffer s, int n) {

        byte b[];
        int i, slen;

        slen = s.length();

        if (slen >= n)
            return (s.toString().substring(0, n).getBytes());

        b = new byte[n];
        for (i = 0; i < slen; i++)
            b[i] = (byte) s.charAt(i);
        for (i = slen; i < n; i++)
            b[i] = 0;

        return (b);
    }


    @Override
    public long writeInfo(NiftiImageInfo info) throws BrainFlowException {

        MemoryCacheImageOutputStream ostream = null;
        long streamPos = -1;
        OutputStream tmp = null;

        try {
            tmp = info.getHeaderFile().getContent().getOutputStream();
            ostream = new MemoryCacheImageOutputStream(tmp);



            //does not handle extensions
            ostream.setByteOrder(info.getEndian());

            //ecs.writeIntCorrect(sizeof_hdr);
            ostream.writeInt(348);
            //data_type_string ... punting for now
            ostream.write(new byte[10]);
            // db_name
            ostream.write(new byte[18]);
            //extents
            ostream.writeInt(0);
            // session_error
            ostream.writeShort(0);
            //regular
            ostream.writeByte(0);

            IDimension<Integer> dims = info.getArrayDim();
            // need to check nifti spec for freq, phase, slice. for now, use 0, 1, 2.
            byte b = packDimInfo((short) 0, (short) 1, (short) 2);
            ostream.writeByte(b);

            // number of dimensions
            ostream.writeShort(info.getArrayDim().numDim());
            for (int i = 0; i < dims.numDim(); i++) {
                ostream.writeShort(dims.getDim(i));
            }

            for (int i=(dims.numDim()+1); i<8; i++) {
                ostream.writeShort(0);
            }

            //todo check spec for meaning of intent array
            for (int i = 0; i < 3; i++) {
                //ecs.writeFloatCorrect(intent[i]);
                ostream.writeFloat(0f);
            }

            ostream.writeShort(Nifti1Dataset.NIFTI_INTENT_NONE);
            short dcode = NiftiImageInfo.getDataTypeCode(info.getDataType());
            ostream.writeShort(dcode);
            ostream.writeShort(info.getDataType().getBitsPerUnit());

            //slice_start : always zero for now
            ostream.writeShort(0);

            float[] pixdim = new float[]{(float) info.getQfac(), info.getSpacing().getDim(0).floatValue(),
                    info.getSpacing().getDim(1).floatValue(), info.getSpacing().getDim(2).floatValue(),
                    0f, 0f, 0f, 0f};

            ostream.writeFloats(pixdim, 0, pixdim.length);

            //vox_offset
            //todo this does not handle extensions
            //for now we hard code 352
            ostream.writeFloat(info.getDataOffset());
            //ostream.writeFloat(352);

            //slope
            ostream.writeFloat((float) info.getScaleFactor());
            ostream.writeFloat((float) info.getIntercept());

            //todo slice_end ... check spec
            ostream.writeShort(dims.getDim(2));

            //todo punting on this
            ostream.writeByte((int) 0);

            // todo fixed to MM and UNKNOWN
            ostream.writeByte((int) packUnits(Nifti1Dataset.NIFTI_UNITS_MM, Nifti1Dataset.NIFTI_UNITS_UNKNOWN));

            //punting on cal_min, cal_max
            ostream.writeFloat(0);
            ostream.writeFloat(0);

            //punting on slice duration ...
            ostream.writeFloat(1);

            //punting on slice duration
            ostream.writeFloat(0);

            //punting on global_max
            ostream.writeInt(0);

            //punting on global_min
            ostream.writeInt(0);

            //punting descrip
            ostream.write(new byte[80]);
            //punting aux_file
            ostream.write(new byte[24]);

            ostream.writeShort(Nifti1Dataset.NIFTI_XFORM_UNKNOWN);
            ostream.writeShort(Nifti1Dataset.NIFTI_XFORM_UNKNOWN);

            Vector3f quat = info.getQuaternion();
            ostream.writeFloat(quat.get(0));
            ostream.writeFloat(quat.get(1));
            ostream.writeFloat(quat.get(2));
            Vector3f qoffset = info.getQoffset();
            ostream.writeFloat(qoffset.get(0));
            ostream.writeFloat(qoffset.get(1));
            ostream.writeFloat(qoffset.get(2));

            Matrix4f sform = info.getSform();


            for (int i = 0; i < 4; i++)
                ostream.writeFloat(sform.get(0, i));
            for (int i = 0; i < 4; i++)
                ostream.writeFloat(sform.get(1, i));
            for (int i = 0; i < 4; i++)
                ostream.writeFloat(sform.get(2, i));

            // punt on intent_name
            ostream.write(new byte[16]);
            ostream.write(setStringSize(new StringBuffer("n+1"), 4));

            if (info.hasExtensions()) {
                //punt on extensions
                ostream.writeByte(1);
                ostream.write(new byte[3]);

                for (NiftiImageInfo.Extension ext : info.getExtensionList()) {
                    ostream.writeInt(ext.esize);
                    ostream.writeInt(ext.ecode);
                    ostream.write(ext.blob);
                }
            } else {
                ostream.write(new byte[4]);
            }



            streamPos = ostream.getStreamPosition();
            System.out.println("stream pos after header write" + streamPos);
            //ostream.flush();


        }
        catch (IOException ex) {
            throw new BrainFlowException("Error: unable to write header file " + info.getHeaderFile() + ": " + ex.getMessage());
        } finally {
            try {
                if (ostream != null) {

                    ostream.close();
                    info.getHeaderFile().close();
                }
            } catch (IOException ex3) {
                throw new BrainFlowException(ex3);
            }
        }



        return streamPos;



    }


}
