package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.core.BrainFlowException;

import java.io.OutputStream;
import java.io.IOException;

import org.apache.commons.vfs.FileSystemException;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 5, 2009
 * Time: 8:27:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiImageWriter implements ImageWriter<NiftiImageInfo> {


    @Override
    public void writeImage(NiftiImageInfo info, IImageData data) throws BrainFlowException {
        NiftiInfoWriter writer = new NiftiInfoWriter();
        long streampos = writer.writeInfo(info);
        System.out.println("stream pos after write = " + streampos);
        MemoryCacheImageOutputStream ostream = null;

        try {
            System.out.println("file size after header write: " + info.getHeaderFile().getContent().getSize());
            OutputStream tmp = info.getHeaderFile().getContent().getOutputStream(true);

            ostream = new MemoryCacheImageOutputStream(tmp);
            //todo fix me extensions ...
            //if (ostream.getStreamPosition() < info.getDataOffset()) {
            //    ostream.seek(info.getDataOffset() - ostream.getStreamPosition());
            //}

            //ostream.seek(352);
            //ostream.getStreamPosition();
            System.out.println("stream position before data write is " + ostream.getStreamPosition());
            BrainIO.writeToStream(info.getDataType(), data, ostream);
            System.out.println("stream after data write is " + ostream.getStreamPosition());
            System.out.println("data size in bytes " + info.getDataType().getBytesPerUnit() * data.getImageSpace().getNumSamples());
        } catch (IOException e) {
            throw new BrainFlowException(e);
        } finally {
            try {
                if (ostream != null) {
                    ostream.close();
                    info.getDataFile().close();
                    System.out.println("file size after data write: " + info.getHeaderFile().getContent().getSize());
                }
            } catch (IOException e2) {
                throw new BrainFlowException(e2);
            }
        }
    }
}




