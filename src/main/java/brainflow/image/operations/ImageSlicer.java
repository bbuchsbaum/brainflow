package brainflow.image.operations;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.data.*;
import brainflow.image.space.IImageSpace3D;


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

    private Anatomy3D displayAnatomy = Anatomy3D.AXIAL_LAI;


    public static ImageSlicer createSlicer(IImageSpace3D refSpace, IImageData3D data) {
        if (refSpace.equals(data.getImageSpace())) {
            return new ImageSlicer(data);
        } else {
            System.out.println("creating mapped data accesor");
            return new ImageSlicer(new MappedDataAcessor3D(refSpace, data));
        }

    }


    public ImageSlicer(DataGrid3D _image) {
        image = _image;

    }


    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
    }


    public BasicImageData2D getSlice(Anatomy3D displayAnatomy, int fixedSlice) {
     
        ImageFiller filler = new ImageFiller();
        return filler.fillImage(image, displayAnatomy, fixedSlice);



    }



}


