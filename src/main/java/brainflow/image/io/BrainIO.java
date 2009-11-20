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
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;

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


    public BrainIO() {
    }

    public static void main(String[] args) {
        try {
            String name = "c:/javacode/googlecode/brainflow/src/main/groovy/testdata/207_anat_alepi.nii";
            FileObject fobj = VFS.getManager().resolveFile(name);
            InputStream is = fobj.getContent().getInputStream();
            is.read();
            System.out.println("fobj is open?" + fobj.isContentOpen());
            is.close();
            System.out.println("fobj is open?" + fobj.isContentOpen());
            System.out.println("can open?" + fobj.getContent().getInputStream());
            fobj.close();
            System.out.println("fobj is open?" + fobj.isContentOpen());

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