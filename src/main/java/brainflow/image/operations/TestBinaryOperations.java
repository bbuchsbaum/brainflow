package brainflow.image.operations;

import brainflow.image.io.BrainIO;
import brainflow.image.data.IImageData;
import brainflow.image.data.MaskedData3D;
import brainflow.image.data.IImageData3D;
import brainflow.image.data.BinaryImageData3D;
import brainflow.display.ThresholdRange;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 12:34:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBinaryOperations {

    public static void main(String[] args) {

        try {
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_temporal.hdr");
            IImageData data = BrainIO.readAnalyzeImage(url);

            MaskedData3D mask1 = new MaskedData3D((IImageData3D)data, new ThresholdRange(-50, 16000));
            System.out.println("mask1 cardinality : " + mask1.cardinality());

            BinaryImageData3D bdat = new BinaryImageData3D(mask1);
            System.out.println("bdat cardinality : " + bdat.cardinality());


            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            data = BrainIO.readAnalyzeImage(url);
            MaskedData3D mask2 = new MaskedData3D((IImageData3D)data, new ThresholdRange(-1,16000));

            System.out.println("mask2 cardinality : " + mask2.cardinality());


            BinaryImageData3D bdat3 = bdat.AND(new BinaryImageData3D(mask2));

            System.out.println("bdat3 cardinality : " + bdat3.cardinality());


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
    }


}
