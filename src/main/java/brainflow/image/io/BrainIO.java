package brainflow.image.io;


import brainflow.app.BrainFlowException;
import brainflow.image.data.BasicImageData;
import brainflow.image.data.IImageData;
import brainflow.image.data.IImageData3D;
import brainflow.image.data.AbstractImageData;

import brainflow.utils.DataType;
import brainflow.utils.IDimension;
import brainflow.utils.ProgressAdapter;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.logging.Logger;
import java.util.List;

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


    public static IImageData3D loadVolume(String fileName) throws BrainFlowException {
        ImageInfoReader reader = createInfoReader(fileName);
        List<? extends ImageInfo> info = reader.readInfo();


        BasicImageReader ireader = new BasicImageReader(info.get(0));
        return (IImageData3D) ireader.readImage();


    }


    public static IImageData3D loadVolume(URL url) throws BrainFlowException {
        ImageInfoReader reader = createInfoReader(url);

        List<? extends ImageInfo> info = reader.readInfo();
        BasicImageReader ireader = new BasicImageReader(info.get(0));
        return (IImageData3D) ireader.readImage();


    }

    public static boolean isSupportedFile(String fileName) {
        if (NiftiInfoReader.isHeaderFile(fileName) || NiftiInfoReader.isImageFile(fileName)) {
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

        if (NiftiInfoReader.isHeaderFile(fileName) || NiftiInfoReader.isImageFile(fileName)) {
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


    public static ImageInfoReader createInfoReader(String fileName) {
        if (NiftiInfoReader.isHeaderFile(fileName) || NiftiInfoReader.isImageFile(fileName)) {
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


    public static IImageData readNiftiImage(URL header) throws BrainFlowException {

        NiftiInfoReader reader = new NiftiInfoReader(header.getPath());
        List<ImageInfo> info = reader.readInfo();

        BasicImageReader ireader = new BasicImageReader(info.get(0));
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
        BasicImageReader ireader = new BasicImageReader(info.get(0));
        return ireader.readImage(new ProgressAdapter());

    }


    public static IImageData readAnalyzeImage(String fname) throws BrainFlowException {
        AnalyzeInfoReader reader = new AnalyzeInfoReader(fname);

        List<ImageInfo> info = reader.readInfo();

        BasicImageReader ireader = new BasicImageReader(info.get(0));
        return ireader.readImage(new ProgressAdapter());

    }


    public static void writeAnalyzeImage(String fname, AbstractImageData data) throws BrainFlowException {
        if (true) throw new UnsupportedOperationException();
        FileImageOutputStream ostream = null;

        try {
            AnalyzeInfoWriter writer = new AnalyzeInfoWriter();
            ImageInfo info = new ImageInfo(data);
            String hdrName = AnalyzeInfoReader.getHeaderName(fname);
            writer.writeInfo(new File(hdrName), info);


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