package brainflow.image.io;

import brainflow.app.BrainFlowException;


import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.AffineMapping3D;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.utils.Point3D;
import brainflow.utils.IDimension;
import brainflow.math.Vector3f;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.util.RandomAccessMode;

import java.io.*;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 1, 2006
 * Time: 3:41:00 PM
 * To change this template use File | Settings | File Templates.
 */


public class NiftiInfoReader implements ImageInfoReader {

    private static final Logger log = Logger.getLogger(NiftiInfoReader.class.getName());




    public static boolean isHeaderFile(String name) {
        if (name.endsWith(".nii") || name.endsWith(".nii.gz") || name.endsWith(".hdr") ) {
            return true;
        }

        return false;
    }

    public static boolean isImageFile(String name) {
        if (name.endsWith(".nii") || name.endsWith(".nii.gz") || name.endsWith(".img") ) {
            return true;
        }

        return false;
    }



    public static String getHeaderName(String name) {
        if (name.endsWith(".img")) {
            name = name.substring(0, name.length() - 3);
            return name + "hdr";
        }
        if (name.endsWith(".nii")) {
            return name;
        }
        if (name.endsWith(".nii.gz")) {
            return name;
        }

        return name + ".nii";
    }

    public static String getImageName(String name) {
        if (name.endsWith(".hdr")) {
            name = name.substring(0, name.length() - 3);
            return name + "img";
        }
        if (name.endsWith(".nii")) {
            return name;
        }

        if (name.endsWith(".nii.gz")) {
            return name;
        }

        return name + ".nii";
    }


    private InputStream getInputStream(FileObject obj) throws IOException {
        try {

            log.info("getting input stream for file object : " + obj.getName());
            if (obj.getName().getBaseName().endsWith(".gz")) {
                String uri = obj.getName().getURI();

                log.info("resolving zipped file : " + "gz:" + uri);
                FileObject gzfile = VFS.getManager().resolveFile("gz:" + uri);
                FileObject[] children = gzfile.getChildren();
                if (children == null || children.length == 0) {
                    throw new IOException("Error reading gzipped file object, URI : " + uri);
                }

                if (!children[0].exists()) {
                    throw new FileNotFoundException("Error, file " + children[0].getName() + " does not exist.");
                }
                return children[0].getContent().getInputStream();


            } else {
                return obj.getContent().getRandomAccessContent(RandomAccessMode.READ).getInputStream();
            }

        } catch (FileSystemException e) {
            throw new IOException(e);
        }

    }

    private InputStream getInputStream(File f) throws IOException {
        if (f.getName().endsWith(".gz")) {
            return new BufferedInputStream(new GZIPInputStream(new FileInputStream(f), 2048));
        } else {
            return new BufferedInputStream(new FileInputStream(f), 2048);
        }

    }

    public List<ImageInfo> readInfo(URL url) throws BrainFlowException {
        try {
            FileObject fobj = VFS.getManager().resolveFile(url.toString());
            return readInfo(fobj);
        } catch(FileSystemException e) {
            throw new BrainFlowException(e);
        }

    }

    public List<ImageInfo> readInfo(InputStream istream) throws BrainFlowException {
        List<ImageInfo> ret = null;
        // todo check to see if valid nifti file extension
        // todo provide dummy header name?
        try {

            throw new UnsupportedOperationException();
            //ret = readHeader("", istream);

        } catch (Exception e) {
            log.warning("Exception caught in NiftiInfoReader.readInfo ");
            throw new BrainFlowException(e);
        }


        //return ret;


    }


    public List<ImageInfo> readInfo(File f) throws BrainFlowException {
        List<ImageInfo> ret;
        // todo check to see if valid nifti file extension
        try {

            String headerName = NiftiInfoReader.getHeaderName(f.getName());
            FileObject dataFile = VFS.getManager().resolveFile(f.getParentFile(), NiftiInfoReader.getImageName(f.getName()));
            FileObject headerFile = VFS.getManager().resolveFile(f.getParentFile(), NiftiInfoReader.getHeaderName(f.getName()));

            ret = readHeader(headerName, headerFile, getInputStream(f));
            for (ImageInfo ii : ret) {
                ii.setDataFile(dataFile);
                ii.setHeaderFile(headerFile);
            }


        } catch (Exception e) {
            //log.warning("Exception caught in AnalyzeInfoReader.readInfo ");
            throw new BrainFlowException(e);
        }


        return ret;

    }

   /*private List<ImageInfo> readHeader(InputStream istream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));


        HeaderAttribute ret = null;
        skipNewLines(reader);

        // parse the header
        do {
            ret = parseElement(reader);
            if (ret != null) {
                putAttribute(ret.getKey(), ret);
            }

        } while (ret != null);

        // determine how many sub-bricks
        HeaderAttribute attr = attributeMap.get(AFNIAttributeKey.DATASET_RANK);
        int numImages = (Integer) attr.getData().get(1);
        List<ImageInfo> infoList = new ArrayList<ImageInfo>(numImages);

        //create instances
        for (int i = 0; i < numImages; i++) {
            AFNIImageInfo info = new AFNIImageInfo(attributeMap);
            infoList.add(info);
        }

        // fill instances with data. Yes, this is all very ugly.
        Iterator<AFNIAttributeKey> iter = attributeMap.keySet().iterator();
        while (iter.hasNext()) {
            AFNIAttributeKey key = iter.next();
            processAttribute(key, attributeMap.get(key), infoList);
        }

        processByteOffsets(infoList);

        return infoList;

    } */


    private FileObject resolveFileObject(String f) throws FileNotFoundException {
        try {
            if (f.endsWith(".gz")) {
                FileObject gzfile = VFS.getManager().resolveFile("gz:" + f);
                FileObject[] children = gzfile.getChildren();
                if (children == null || children.length == 0) {
                    throw new FileNotFoundException("Error finding gzipped file : " + f);
                }

                log.fine("resolved gzipped file : " + children[0]);
                log.fine("file content type : " + children[0].getFileSystem());
                return children[0];
            } else {
                return VFS.getManager().resolveFile(f);
            }
        } catch (FileSystemException e) {
            throw new FileNotFoundException(e.getMessage());
        }

    }

    private FileObject resolveFileObject(File f) throws FileNotFoundException {
        return resolveFileObject(f.getAbsolutePath());
    }

    public List<ImageInfo> readInfo(FileObject fobj) throws BrainFlowException {
        List<ImageInfo> ret;

        try {

            String headerName = NiftiInfoReader.getHeaderName(fobj.getName().getBaseName());
            ret = readHeader(headerName, fobj, getInputStream(fobj));
            FileObject dataFile = VFS.getManager().resolveFile(fobj.getParent(), NiftiInfoReader.getImageName(fobj.getName().getBaseName()));
            FileObject headerFile = VFS.getManager().resolveFile(fobj.getParent(), NiftiInfoReader.getHeaderName(fobj.getName().getBaseName()));
            for (ImageInfo ii : ret) {
                ii.setDataFile(dataFile);
                ii.setHeaderFile(headerFile);
            }


        } catch (IOException e) {
            throw new BrainFlowException(e);
        }

        return ret;


    }


    private void fillPixDim(float[] pixdim, NiftiImageInfo info) {
        info.qfac = (int) pixdim[0];
        info.setSpacing(new Dimension3D<Double>((double) pixdim[1], (double) pixdim[2], (double) pixdim[3]));

    }


    private void fillDataType(short datatype, NiftiImageInfo info) throws BrainFlowException {

        DataType dtype = null;
        switch (datatype) {
            case Nifti1Dataset.NIFTI_TYPE_UINT8:
                dtype = DataType.UBYTE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT8:
                dtype = DataType.BYTE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT16:
                dtype = DataType.SHORT;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT32:
                dtype = DataType.INTEGER;
                break;
            case Nifti1Dataset.NIFTI_TYPE_FLOAT32:
                dtype = DataType.FLOAT;
                break;
            case Nifti1Dataset.NIFTI_TYPE_FLOAT64:
                dtype = DataType.DOUBLE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT64:
                dtype = DataType.LONG;
                break;
            case Nifti1Dataset.NIFTI_TYPE_UINT16:
                throw new BrainFlowException("Do not support NIFTI_TYPE_UINT16 datatype");
            case Nifti1Dataset.NIFTI_TYPE_UINT32:
                throw new BrainFlowException("Do not support NIFTI_TYPE_UINT32 datatype");
            case Nifti1Dataset.NIFTI_TYPE_UINT64:
                throw new BrainFlowException("Do not support NIFTI_TYPE_UINT64 datatype");
            case Nifti1Dataset.NIFTI_TYPE_RGB24:
                throw new BrainFlowException("Do not support NIFTI_TYPE_RGB24 datatype");
            default:
                throw new BrainFlowException("Do not support NIFTI_TYPE " + datatype);

        }

        info.setDataType(dtype);


    }

    private void fillImageDim(short[] dim, NiftiImageInfo info) throws BrainFlowException {
        int numDims = dim[0];
        if (dim[0] > 2 && dim[0] < 5) {
            info.setDimensionality(dim[0]);
        } else {
            throw new BrainFlowException("Nifti images with fewer than 3 or more than 4 dimensions are not supported." + dim[0]);
        }

        info.setArrayDim(new Dimension3D<Integer>((int) dim[1], (int) dim[2], (int) dim[3]));
        info.setDimensionality(numDims);
        if (numDims > 3) {
            info.setNumImages(dim[4]);
        }


    }

    private NiftiImageInfo fillInfo(Nifti1Dataset nifti) throws BrainFlowException {
        NiftiImageInfo info = new NiftiImageInfo();
        short[] dim = nifti.dim;

        Arrays.toString(dim);
        fillImageDim(dim, info);
        fillDataType(nifti.datatype, info);
        float[] pixdim = nifti.pixdim;
        fillPixDim(pixdim, info);


        info.calculateRealDim();




        if (nifti.scl_slope == 0 && nifti.scl_slope == 1) {
            info.setScaleFactor(0);
        } else {
            info.setScaleFactor(nifti.scl_slope);
        }


        info.setIntercept(nifti.scl_inter);
        info.setByteOffset((int) nifti.vox_offset);
        info.setOrigin(new Point3D(nifti.qoffset[0], nifti.qoffset[1], nifti.qoffset[2]));

        if (nifti.big_endian) {
            info.setEndian(ByteOrder.BIG_ENDIAN);
        } else {
            info.setEndian(ByteOrder.LITTLE_ENDIAN);
        }

        info.quaternion = new Vector3f(nifti.quatern[0], nifti.quatern[1], nifti.quatern[2]);
        info.qfac = nifti.qfac;
        info.qoffset = new Vector3f(nifti.qoffset[0], nifti.qoffset[1], nifti.qoffset[2]);

        Vector3f q = info.quaternion;
        Vector3f qo = info.qoffset;


        info.qform = NiftiImageInfo.quaternionToMatrix(q.get(0), q.get(1),q.get(2),
                qo.get(0), qo.get(1), qo.get(2),
                pixdim[1], pixdim[2],pixdim[3],
                info.qfac);

        
        Anatomy3D anat = NiftiImageInfo.nearestAnatomy(info.qform);
        info.setAnatomy(anat);

        AffineMapping3D mapping = new AffineMapping3D(info.qform);
        info.setMapping(mapping);


        return info;

    }


    private List<ImageInfo> readHeader(String name, FileObject file, InputStream stream) throws IOException, BrainFlowException {
        ImageInfo info;
        FileObject dataFile = VFS.getManager().resolveFile(file.getParent(), NiftiInfoReader.getImageName(file.getName().getBaseName()));
        FileObject headerFile = VFS.getManager().resolveFile(file.getParent(), NiftiInfoReader.getHeaderName(file.getName().getBaseName()));

        try {
            Nifti1Dataset nifti = new Nifti1Dataset(name);
            nifti.readHeader(stream);

            //for (int i = 0; i < numImages; i++) {
            //AFNIImageInfo info = new AFNIImageInfo(attributeMap);
            //infoList.add(info);

            info = fillInfo(nifti);
            info.setDataFile(dataFile);
            info.setHeaderFile(headerFile);


        } catch (IOException e) {
            throw e;
        } catch (BrainFlowException e) {
            throw e;
        }

        int nimages = info.getNumImages();
        if (nimages == 1) {
            return Arrays.asList(info);
        } else {

            List<ImageInfo> ret = new ArrayList<ImageInfo>();
            for (int i=0; i<nimages; i++) {
                NiftiImageInfo ni = (NiftiImageInfo)info.selectInfo(i);
                ret.add(ni);
            }

            return ret;
        }



    }


    private List<ImageInfo> readHeader(String name) throws IOException, BrainFlowException {
        ImageInfo info;

        try {
            Nifti1Dataset nifti = new Nifti1Dataset(name);
            nifti.readHeader();
            info = fillInfo(nifti);
        } catch (IOException e) {
            throw e;
        } catch (BrainFlowException e) {
            throw e;
        }

        return Arrays.asList(info);
    }


    public static void main(String[] args) {
        try {

            NiftiInfoReader reader = new NiftiInfoReader();
           
            URL url = null; //TestUtils.getDataURL("BRB-20071214-09-t1_mprage-001.nii");
            ImageInfo info = reader.readInfo(VFS.getManager().resolveFile(url.toString())).get(0);


            IDimension dim = info.getArrayDim();
        
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
