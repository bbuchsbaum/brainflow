package brainflow.image.io;


import brainflow.core.BrainFlowException;
import brainflow.image.data.IImageData;
import brainflow.image.data.IImageData3D;
import brainflow.image.data.AbstractImageData;
import brainflow.image.iterators.ValueIterator;

import brainflow.utils.DataType;
import brainflow.utils.FileObjectMatcher;
import brainflow.utils.ProgressAdapter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BrainIO {

    private static Logger log = Logger.getLogger(BrainIO.class.getCanonicalName());

    public static final IImageFileDescriptor NIFTI = new AbstractImageFileDescriptor("nii", "nii", "NIFTI") {
        @Override
        public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {
            return new ImageDataSource(this, headerFile, dataFile);
        }

        @Override
        public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {
            return new NiftiInfoReader(headerFile, dataFile);
        }


    };

    public static final IImageFileDescriptor NIFTI_GZ = new AbstractImageFileDescriptor("nii", "nii", "NIFTI", BinaryEncoding.GZIP, BinaryEncoding.GZIP) {
        @Override
        public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {
            return new ImageDataSource(this, headerFile, dataFile);
        }

        @Override
        public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {
            return new NiftiInfoReader(headerFile, dataFile);
        }


    };

    public static final IImageFileDescriptor AFNI = new AbstractImageFileDescriptor("HEAD", "BRIK", "AFNI") {
        @Override
        public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {

            return new ImageDataSource(this, headerFile, dataFile);
        }

        @Override
        public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {

            return new AFNIInfoReader(headerFile, dataFile);
        }
    };

    public static final IImageFileDescriptor AFNI_GZ = new AbstractImageFileDescriptor("HEAD", "BRIK", "AFNI", BinaryEncoding.RAW, BinaryEncoding.GZIP) {
        @Override
        public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {

            return new ImageDataSource(this, headerFile, dataFile);
        }

        @Override
        public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {
           
            return new AFNIInfoReader(headerFile, dataFile);
        }
    };



    /*public static final IImageFileDescriptor ANALYZE = new AbstractImageFileDescriptor("hdr", "img", "ANALYZE7.5") {
       @Override
       public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {
           return new ImageDataSource(this, headerFile, dataFile);
       }

       @Override
       public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {
           return new AnalyzeInfoReader(headerFile, dataFile);
       }
   }; */


    public static final IImageFileDescriptor NIFTI_PAIR = new AbstractImageFileDescriptor("hdr", "img", "NIFTI") {
        @Override
        public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {
            return new ImageDataSource(this, headerFile, dataFile);
        }

        private boolean isNifti(FileObject headerFile) {
            try {
                InputStream istream = headerFile.getContent().getInputStream();
                istream.skip(344);
                byte[] bb = new byte[4];
                istream.read(bb);
                istream.close();
                String magic = new StringBuffer(new String(bb)).toString().trim();
                return magic.equals("ni1") || magic.equals("n+1");
            } catch (FileSystemException e) {
                throw new RuntimeException(e);
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }


        }

        @Override
        public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {
            if (!isNifti(headerFile)) {
                return new AnalyzeInfoReader(headerFile, dataFile);
            } else {
                return new NiftiInfoReader(headerFile, dataFile);
            }
        }


    };


    public static final IImageFileDescriptor ANALYZE_GZ = new AbstractImageFileDescriptor("hdr", "img", "ANALYZE7.5", BinaryEncoding.GZIP, BinaryEncoding.GZIP) {
        @Override
        public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile) {
            return new ImageDataSource(this, headerFile, dataFile);
        }

        @Override
        public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile) {
            return new AnalyzeInfoReader(headerFile, dataFile);
        }
    };


    public static final List<IImageFileDescriptor> supportedImageFormats = Arrays.asList(NIFTI, NIFTI_GZ, AFNI, AFNI_GZ, NIFTI_PAIR);


    private BrainIO() {
    }

    public static void main(String[] args) {
        try {
            String name = "c:/javacode/googlecode/brainflow/src/main/groovy/testdata/207_anat_alepi.nii";
            FileObject fobj = VFS.getManager().resolveFile(name);
            InputStream is = fobj.getContent().getInputStream();
            is.read();

            is.close();

            fobj.close();


        } catch (FileSystemException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    public static IImageData3D loadVolume(String fileName) throws BrainFlowException {
        ImageInfoReader reader = createInfoReader(fileName);
        List<? extends ImageInfo> info = reader.readInfo();


        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage();
    }

    public static IImageData3D loadVolume(FileObject fileObject) throws BrainFlowException {
        ImageInfoReader reader = createInfoReader(fileObject);
        List<? extends ImageInfo> info = reader.readInfo();
        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage();
    }


    public static IImageData3D loadVolume(URL url) throws BrainFlowException {
        ImageInfoReader reader = createInfoReader(url);
        List<? extends ImageInfo> info = reader.readInfo();
        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage();
    }


    public static List<IImageData3D> loadVolumeList(String... fileNames) throws BrainFlowException {
        List<IImageData3D> dlist = new ArrayList<IImageData3D>();
        for (String fn : fileNames) {
            dlist.add(loadVolume(fn));
        }
        return dlist;

    }


    public static List<IImageData3D> loadVolumeList(List<FileObject> flist) throws BrainFlowException {
        List<IImageData3D> dlist = new ArrayList<IImageData3D>();
        for (FileObject fo : flist) {
            dlist.add(loadVolume(fo));
        }

        return dlist;

    }

    public static List<IImageData3D> loadVolumeList(File root, String regex, boolean recursive) throws BrainFlowException {
        if (root.isFile()) {
            root = root.getParentFile();
        }
        try {
            FileObject fobj = VFS.getManager().resolveFile(root.getAbsolutePath());
            List<FileObject> ret;
            if (recursive) {
                ret = new FileObjectMatcher(fobj, regex, 100).matchFiles();
            } else {
                ret = new FileObjectMatcher(fobj, regex, 1).matchFiles();
            }

            return loadVolumeList(ret);

        } catch (FileSystemException e) {
            throw new BrainFlowException(e);
        }
    }

    public static IImageDataSource loadDataSource(FileObject fobj) throws BrainFlowException {
        IImageDataSource ret;

        if (BrainIO.isSupportedImageHeaderFile(fobj)) {
            IImageFileDescriptor desc = BrainIO.getImageFileDescriptor(fobj);
            try {
                ret = desc.createDataSource(fobj, desc.resolveDataFileObject(fobj));
            } catch (IOException e) {
                throw new BrainFlowException(e);
            }
        } else if (BrainIO.isSupportedImageDataFile(fobj)) {
            IImageFileDescriptor desc = BrainIO.getImageFileDescriptor(fobj);
            try {
                ret = desc.createDataSource(fobj, desc.resolveDataFileObject(fobj));
            } catch (IOException e) {
                throw new BrainFlowException(e);
            }
        } else {
            throw new BrainFlowException("file object " + fobj + " cannot be loaded ");
        }

        return ret;

    }

    public static List<IImageDataSource> loadDataSources(FileObject[] fobjs) {
        List<IImageDataSource> sources = new ArrayList<IImageDataSource>();

        try {
            for (FileObject fobj : fobjs) {

                if (BrainIO.isSupportedImageHeaderFile(fobj) || BrainIO.isSupportedImageDataFile(fobj)) {
                    IImageFileDescriptor desc = BrainIO.getImageFileDescriptor(fobj);
                    try {
                        sources.add(loadDataSource(fobj));
                    } catch (BrainFlowException e) {
                        log.warning("could not resolve data file for header: " + fobj.getName().getPath());
                    }
                }

            }
        } catch (BrainFlowException e) {
            throw new RuntimeException(e);
        }

        return sources;
    }



    public static List<IImageDataSource> loadDataSources(File[] files) {
        FileObject[] fobjs = new FileObject[files.length];

        try {
            for (int i=0; i<files.length; i++) {
                fobjs[i] = (VFS.getManager().resolveFile(files[i].getAbsolutePath()));
            }
        } catch (FileSystemException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return loadDataSources(fobjs);

    }

    public static boolean isSupportedImageHeaderFile(FileObject headerFile) {
        for (IImageFileDescriptor desc : supportedImageFormats) {
            if (desc.isHeaderMatch(headerFile.getName().getBaseName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSupportedImageDataFile(FileObject dataFile) {
        for (IImageFileDescriptor desc : supportedImageFormats) {
            if (desc.isDataMatch(dataFile.getName().getBaseName())) {
                return true;
            }
        }

        return false;


    }

    public static boolean isSupportedImageFile(String fileName) {
        for (IImageFileDescriptor desc : supportedImageFormats) {
            if (desc.isDataMatch(fileName) || desc.isHeaderMatch(fileName)) {
                return true;
            }
        }

        return false;


    }

    public static IImageFileDescriptor getImageFileDescriptor(FileObject fobj) throws BrainFlowException {
        for (IImageFileDescriptor desc : supportedImageFormats) {
            if (desc.canResolve(fobj)) {
                return desc;
            }
        }

        throw new BrainFlowException("Could not find ImageIODescriptor for supplied File " + fobj);

    }


    //public boolean isLoadableImage(String file) {
    //    try {
    //        FileObject fobj = VFS.getManager().resolveFile(file);
    //        return isLoadableImage(fobj);
    //    } catch (FileSystemException e) {
    //        log.warning("could not resolve file " + file);
    //        return false;
    //    }

    // }

    public static boolean isSupportedFile(String fileName) {
        if (NiftiImageInfo.isHeaderFile(fileName) || NiftiImageInfo.isImageFile(fileName)) {
            return true;
        }

        if (AnalyzeInfoReader.isHeaderFile(fileName) || AnalyzeInfoReader.isImageFile(fileName)) {
            return true;
        }

        if (AFNIInfoReader.isHeaderFile(fileName) || AFNIInfoReader.isImageFile(fileName)) {
            return true;
        }


        return false;


    }


    public static ImageInfoReader createInfoReader(URL url) {

        String fileName = url.toString();

        if (NiftiImageInfo.isHeaderFile(fileName) || NiftiImageInfo.isImageFile(fileName)) {
            return new NiftiInfoReader(fileName);
        }

        if (AnalyzeInfoReader.isHeaderFile(fileName) || AnalyzeInfoReader.isImageFile(fileName)) {
            return new AnalyzeInfoReader(fileName);
        }

        if (AFNIInfoReader.isHeaderFile(fileName) || AFNIInfoReader.isImageFile(fileName)) {
            return new AFNIInfoReader(fileName);
        }


        throw new IllegalArgumentException("could not find info read for file " + fileName);


    }

    public static ImageInfoReader createInfoReader(FileObject fobj) {
        String fileName = fobj.getName().getBaseName();
        if (NiftiImageInfo.isHeaderFile(fileName) || NiftiImageInfo.isImageFile(fileName)) {
            //todo fix
            return new NiftiInfoReader(fobj, fobj);
        }

        if (AnalyzeInfoReader.isHeaderFile(fileName) || AnalyzeInfoReader.isImageFile(fileName)) {
            //todo fix
            return new AnalyzeInfoReader(fobj, fobj);
        }

        if (AFNIInfoReader.isHeaderFile(fileName) || AFNIInfoReader.isImageFile(fileName)) {
            //todo fix
            return new AFNIInfoReader(fobj, fobj);
        }


        throw new IllegalArgumentException("could not find info read for file " + fileName);


    }

    public static ImageInfoReader createInfoReader(String fileName) {
        if (NiftiImageInfo.isHeaderFile(fileName) || NiftiImageInfo.isImageFile(fileName)) {
            return new NiftiInfoReader(fileName);
        }

        if (AnalyzeInfoReader.isHeaderFile(fileName) || AnalyzeInfoReader.isImageFile(fileName)) {
            return new AnalyzeInfoReader(fileName);
        }

        if (AFNIInfoReader.isHeaderFile(fileName) || AFNIInfoReader.isImageFile(fileName)) {
            return new AFNIInfoReader(fileName);
        }


        throw new IllegalArgumentException("could not find info read for file " + fileName);


    }

    public static IImageData readNiftiImage(FileObject fobj) throws BrainFlowException {
        NiftiInfoReader reader = new NiftiInfoReader(fobj, fobj);
        List<ImageInfo> info = reader.readInfo();

        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage(new ProgressAdapter());
    }

    public static IImageData readNiftiImage(URL header) throws BrainFlowException {

        NiftiInfoReader reader = new NiftiInfoReader(header.getPath());
        List<ImageInfo> info = reader.readInfo();

        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage(new ProgressAdapter());

    }


    public static IImageData readNiftiImage(String fname) throws BrainFlowException {

        try {
            URL url = new File(fname).toURI().toURL();
            return readNiftiImage(url);
        } catch (MalformedURLException e) {
            throw new BrainFlowException(e);
        }


    }


    public static IImageData readAnalyzeImage(URL header) throws BrainFlowException {
        AnalyzeInfoReader reader = new AnalyzeInfoReader(header.toString());
        List<? extends ImageInfo> info = reader.readInfo();
        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage(new ProgressAdapter());

    }


    public static IImageData readAnalyzeImage(String fname) throws BrainFlowException {
        AnalyzeInfoReader reader = new AnalyzeInfoReader(fname);

        List<ImageInfo> info = reader.readInfo();

        BasicImageReader3D ireader = new BasicImageReader3D(info.get(0));
        return ireader.readImage(new ProgressAdapter());


    }

    public static void writeToStream(DataType dtype, IImageData data, ImageOutputStream ostream) throws IOException {
        switch (dtype) {

            case BOOLEAN:
                writeAsBytes(data, ostream);
                break;
            case BYTE:
                writeAsBytes(data, ostream);
                break;
            case DOUBLE:
                writeAsDoubles(data, ostream);
                break;
            case FLOAT:
                writeAsFloats(data, ostream);
                break;
            case INTEGER:
                writeAsInts(data, ostream);
                break;
            case LONG:
                writeAsLongs(data, ostream);
                break;
            case SHORT:
                writeAsShorts(data, ostream);
                break;
            case UBYTE:
                writeAsBytes(data, ostream);
                break;
            default:
                throw new UnsupportedOperationException("output of " + dtype + " data type not supported");

        }

    }

    public static void writeAsBytes(IImageData data, ImageOutputStream ostream) throws IOException {
        ValueIterator iter = data.valueIterator();
        while (iter.hasNext()) {
            ostream.writeByte((byte) iter.next());
        }

    }

    public static void writeAsShorts(IImageData data, ImageOutputStream ostream) throws IOException {
        ValueIterator iter = data.valueIterator();
        while (iter.hasNext()) {
            ostream.writeShort((short) iter.next());
        }

    }

    public static void writeAsInts(IImageData data, ImageOutputStream ostream) throws IOException {
        ValueIterator iter = data.valueIterator();
        while (iter.hasNext()) {
            ostream.writeInt((int) iter.next());
        }

    }

    public static void writeAsLongs(IImageData data, ImageOutputStream ostream) throws IOException {
        ValueIterator iter = data.valueIterator();
        while (iter.hasNext()) {
            ostream.writeLong((long) iter.next());
        }

    }

    public static void writeAsFloats(IImageData data, ImageOutputStream ostream) throws IOException {
        ValueIterator iter = data.valueIterator();
        while (iter.hasNext()) {
            ostream.writeFloat((float) iter.next());
        }


    }

    public static void writeAsDoubles(IImageData data, ImageOutputStream ostream) throws IOException {
        ValueIterator iter = data.valueIterator();
        while (iter.hasNext()) {
            ostream.writeDouble((double) iter.next());
        }

    }


    public static void writeAnalyzeImage(String fname, AbstractImageData data) throws BrainFlowException {
        if (true) throw new UnsupportedOperationException();
        FileImageOutputStream ostream = null;

        try {
            AnalyzeInfoWriter writer = new AnalyzeInfoWriter();
            ImageInfo info = new ImageInfo(data);
            String hdrName = AnalyzeInfoReader.getHeaderName(fname);
            writer.writeInfo(info);


            String imgName = AnalyzeInfoReader.getImageName(fname);

            File opfile = new File(imgName);


            ostream = new FileImageOutputStream(opfile);

            assert ostream != null;


            ostream.setByteOrder(info.getEndian());

            DataType dtype = data.getDataType();
            Object storage = null;


            //todo fixme
            //Object storage = data.getStorage();


            if (dtype == DataType.BYTE) {
                ostream.write((byte[]) storage, 0, ((byte[]) storage).length);
            } else if (dtype == DataType.SHORT) {
                ostream.writeShorts((short[]) storage, 0, ((short[]) storage).length);
            } else if (dtype == DataType.INTEGER) {
                ostream.writeInts((int[]) storage, 0, ((int[]) storage).length);
            } else if (dtype == DataType.FLOAT) {
                ostream.writeFloats((float[]) storage, 0, ((float[]) storage).length);
            } else if (dtype == DataType.DOUBLE) {
                ostream.writeDoubles((double[]) storage, 0, ((double[]) storage).length);
            } else
                throw new IllegalArgumentException("Data Type : " + dtype + " not supported");

        } catch (FileNotFoundException e1) {
            throw new BrainFlowException(e1);
        } catch (IOException e2) {
            throw new BrainFlowException(e2);
        } finally {
            try {
                ostream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}