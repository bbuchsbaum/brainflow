package brainflow.image.io;

import brainflow.core.BrainFlowException;


import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.AffineMapping3D;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.utils.Point3D;
import brainflow.math.Vector3f;
import brainflow.math.Matrix4f;
import org.apache.commons.vfs.FileObject;

import java.io.*;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 1, 2006
 * Time: 3:41:00 PM
 * To change this template use File | Settings | File Templates.
 */


public class NiftiInfoReader extends AbstractInfoReader {

    private static final Logger log = Logger.getLogger(NiftiInfoReader.class.getName());


    protected NiftiInfoReader() {
        super();
    }

    public NiftiInfoReader(FileObject headerFile, FileObject dataFile) {
        super(headerFile, dataFile);
    }

    public NiftiInfoReader(File headerFile, File dataFile) {
        super(headerFile, dataFile);
    }

    public NiftiInfoReader(String name) {
        super(new File(NiftiImageInfo.getHeaderName(name, ".nii")), new File(NiftiImageInfo.getImageName(name, ".nii")));
    }

    @Override
    public NiftiInfoReader create(FileObject headerFile, FileObject dataFile) {
        return new NiftiInfoReader(headerFile, dataFile);
    }





    @Override
    public List<ImageInfo> readInfoList() throws BrainFlowException {

        try {
            String headerName = NiftiImageInfo.getHeaderName(getHeaderFile().getName().getBaseName(), ".nii");
            return readHeader(headerName, getHeaderFile(), getDataFile());
        } catch (IOException e) {
            throw new BrainFlowException(e);
        }

    }

   


    private void fillPixDim(float[] pixdim, NiftiImageInfo.Builder builder) {
        builder.qfac((int) pixdim[0]);
        builder.spacing(new Dimension3D<Double>((double) pixdim[1], (double) pixdim[2], (double) pixdim[3]));

    }


    private void fillDataType(short datatype, NiftiImageInfo.Builder builder) throws BrainFlowException {
        DataType dtype = NiftiImageInfo.getDataType(datatype);
        builder.dataType(dtype);

    }

    private void fillImageDim(short[] dim, NiftiImageInfo.Builder builder) throws BrainFlowException {
        int numDims = dim[0];
        if (dim[0] > 2 && dim[0] < 5) {
            builder.dimensionality(numDims);
        } else {
            throw new BrainFlowException("Nifti images with fewer than 3 or more than 4 dimensions are not supported." + numDims);
        }

        builder.volumeDim(new Dimension3D<Integer>((int) dim[1], (int) dim[2], (int) dim[3]));
       
        if (numDims == 4) {
            builder.numVolumes(dim[4]);
        }


    }

    private NiftiImageInfo fillInfo(Nifti1Dataset nifti, FileObject headerFile, FileObject dataFile) throws BrainFlowException {
       NiftiImageInfo.Builder builder = new NiftiImageInfo.Builder();
        builder.dataFile(dataFile);
        builder.headerFile(headerFile);
        
        short[] dim = nifti.dim;

        Arrays.toString(dim);
        fillImageDim(dim, builder);
        fillDataType(nifti.datatype, builder);
        float[] pixdim = nifti.pixdim;
        fillPixDim(pixdim, builder);

        



        if (nifti.scl_slope == 0 && nifti.scl_slope == 1) {
            builder.scaleFactor(0);
        } else {
            builder.scaleFactor(nifti.scl_slope);
        }


        builder.intercept(nifti.scl_inter);
        builder.byteOffset((int) nifti.vox_offset);
        builder.origin(new Point3D(nifti.qoffset[0], nifti.qoffset[1], nifti.qoffset[2]));

        if (nifti.big_endian) {
            builder.endian(ByteOrder.BIG_ENDIAN);
        } else {
            builder.endian(ByteOrder.LITTLE_ENDIAN);
        }

        Vector3f q = new Vector3f(nifti.quatern[0], nifti.quatern[1], nifti.quatern[2]);
        Vector3f qo = new Vector3f(nifti.qoffset[0], nifti.qoffset[1], nifti.qoffset[2]);
        builder.quaternion(q);
        builder.qfac(nifti.qfac);
        builder.qoffset(qo);




        Matrix4f qform = NiftiImageInfo.quaternionToMatrix(q.get(0), q.get(1), q.get(2),
                qo.get(0), qo.get(1), qo.get(2),
                pixdim[1], pixdim[2], pixdim[3],
                nifti.qfac);
        builder.qform(qform);

        Anatomy3D anat = NiftiImageInfo.nearestAnatomy(qform);
        builder.anatomy(anat);

        AffineMapping3D mapping = new AffineMapping3D(qform, anat, Anatomy3D.REFERENCE_ANATOMY);
        builder.mapping(mapping);

        Vector extVec = nifti.extensions_list;
        Vector extBlobs = nifti.extension_blobs;

        if (extBlobs.size() != extVec.size()) {
            throw new RuntimeException("error reading extension data in NIFTI header: length extension code vector does not equal length of extension byte list");
        }

        if (extVec.size() > 0) {
            List<NiftiImageInfo.Extension> extlist = new ArrayList<NiftiImageInfo.Extension>();
            for (int i=0; i<extVec.size(); i++) {
                int[] ecodes = (int[])extVec.get(i);
                byte[] blob = (byte[])extBlobs.get(i);
                extlist.add(new NiftiImageInfo.Extension(ecodes[0], ecodes[1], blob));
            }
            builder.extensions(extlist);
        }


        return builder.build();

    }

    @Override
    public ImageInfo readInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private List<ImageInfo> readHeader(String name, FileObject headerFile, FileObject dataFile) throws IOException, BrainFlowException {
        ImageInfo info;

        try {
            //if (!headerFile.exists()) {
                //throw new IllegalArgumentException("header " + headerFile + " does not exist.");
            //}

            Nifti1Dataset nifti = new Nifti1Dataset(name);
            nifti.readHeader(headerFile.getContent().getInputStream());

            info = fillInfo(nifti, headerFile, dataFile);

        } catch (IOException e) {
            throw e;
        } catch (BrainFlowException e) {
            throw e;
        }

        int nimages = info.getNumVolumes();
        
        if (nimages == 1) {
            return Arrays.asList(info);
        } else {

            List<ImageInfo> ret = new ArrayList<ImageInfo>();
            for (int i = 0; i < nimages; i++) {
                NiftiImageInfo ni = (NiftiImageInfo) info.selectInfo(i);
                ret.add(ni);
            }

            return ret;
        }


    }



    
}
