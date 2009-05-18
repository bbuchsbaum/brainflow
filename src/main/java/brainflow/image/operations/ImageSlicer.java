package brainflow.image.operations;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.data.*;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import brainflow.image.interpolation.InterpolationFunction3D;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageSlicer {


    private DataGrid3D image;


    public static ImageSlicer createSlicer(IImageSpace3D refSpace, IImageData3D data) {
        if (refSpace.equals(data.getImageSpace())) {
            return new ImageSlicer(data);
        } else {
            return createSlicer(refSpace, data, new NearestNeighborInterpolator());
        }

    }

    public static ImageSlicer createSlicer(IImageSpace3D refSpace, IImageData3D data, InterpolationFunction3D interpolator) {
        if (refSpace.equals(data.getImageSpace())) {
            return new ImageSlicer(data);
        } else {
            return new ImageSlicer(new MappedDataAcessor3D(refSpace, data, interpolator));
        }

    }


    public ImageSlicer(DataGrid3D _image) {
        image = _image;

    }

    public DataGrid3D getImage() {
        return image;
    }

    public BasicImageData2D getSlice(Anatomy3D displayAnatomy, int fixedSlice) {
     
        ImageFiller filler = new ImageFiller();
        return filler.fillImage(image, displayAnatomy, fixedSlice);



    }



}


