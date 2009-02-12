package brainflow;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.axis.ImageAxis;
import brainflow.image.data.BasicImageData;
import brainflow.image.data.BasicImageData3D;
import brainflow.image.data.IImageData3D;
import brainflow.image.io.BrainIO;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace3D;
import brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 24, 2007
 * Time: 1:59:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakeData {


    public static void main(String[] args) {
        int zdim = 3;
        int xdim = 3;
        int ydim = 3;

        ImageAxis axis1 = new ImageAxis(0, 100, AnatomicalAxis.RIGHT_LEFT, xdim);
        ImageAxis axis2 = new ImageAxis(0, 100, AnatomicalAxis.POSTERIOR_ANTERIOR, ydim);
        ImageAxis axis3 = new ImageAxis(0, 100, AnatomicalAxis.INFERIOR_SUPERIOR, zdim);

        ImageSpace3D space = new ImageSpace3D(axis1, axis2, axis3);
        IImageData3D data1 = new BasicImageData3D(space, DataType.INTEGER);
        IImageData3D data2 = new BasicImageData3D(space, DataType.INTEGER);
        IImageData3D data3 = new BasicImageData3D(space, DataType.INTEGER);
        IImageData3D data4 = new BasicImageData3D(space, DataType.INTEGER);


        data1 = fillHorizontal(data1);
        data2 = fillVertical(data2);
        data3 = fillHorizontal(data3);
        data3 = fillVertical(data3);
        data4 = fillDiagonal(data4);


        try {
            BrainIO.writeAnalyzeImage("c:/horizontal_stripes", (BasicImageData) data1);
            BrainIO.writeAnalyzeImage("c:/vertical_stripes", (BasicImageData) data2);
            BrainIO.writeAnalyzeImage("c:/checkered_stripes", (BasicImageData) data3);
            BrainIO.writeAnalyzeImage("c:/diagonal_stripes", (BasicImageData) data4);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static IImageData3D fillDiagonal(IImageData3D data) {
        for (int x = 0; x < data.getDimension(Axis.X_AXIS); x++) {
            for (int y = 0; y < data.getDimension(Axis.Y_AXIS); y++) {
                for (int z = 0; z < data.getDimension(Axis.Z_AXIS); z++) {
                    //if (x == y) //data.setValue(x, y, z, 100);
                }

            }
        }

        return data;
    }


    public static IImageData3D fillHorizontal(IImageData3D data) {
        for (int x = 0; x < data.getDimension(Axis.X_AXIS); x++) {
            for (int y = 0; y < data.getDimension(Axis.Y_AXIS); y++) {
                for (int z = 0; z < data.getDimension(Axis.Z_AXIS); z++) {
                    //if ((y % 2) == 1) //data.setValue(x, y, z, 100);
                }

            }
        }

        return data;
    }


    public static IImageData3D fillVertical(IImageData3D data) {
        for (int x = 0; x < data.getDimension(Axis.X_AXIS); x++) {
            for (int y = 0; y < data.getDimension(Axis.Y_AXIS); y++) {
                for (int z = 0; z < data.getDimension(Axis.Z_AXIS); z++) {
                    //if ((x % 2) == 1) //data.setValue(x, y, z, 100);
                }

            }
        }

        return data;
    }
}
