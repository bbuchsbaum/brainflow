package brainflow.image.io;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.data.IImageData;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.ImageMapping3D;
import brainflow.utils.*;
import org.apache.commons.vfs.FileObject;

import java.nio.ByteOrder;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageInfo implements java.io.Serializable {

    public static Anatomy3D DEFAULT_ANATOMY = Anatomy3D.AXIAL_LPI;

    private IDimension arrayDim = new Dimension3D<Integer>(0, 0, 0);

    private IDimension spacing = new Dimension3D<Double>(0.0, 0.0, 0.0);

    private Dimension3D voxelOffset = new Dimension3D<Integer>(0, 0, 0);


    private ImageMapping3D mapping;

    private Point3D origin = new Point3D();

    private int numImages = 1;

    private int imageIndex = 0;

    private int byteOffset = 0;

    private int dimensionality = 3;

    // SPM Default anatomy is LPI (Neurological)


    private Anatomy3D anatomy = Anatomy3D.AXIAL_LPI;

    private DataType dataType = DataType.BYTE;

    private ByteOrder endian = ByteOrder.nativeOrder();

    private float scaleFactor = 1;

    private float intercept = 0;


    private String imageLabel = null;

    private FileObject dataFile;

    private FileObject headerFile;

    public ImageInfo() {

    }

    public ImageInfo(ImageInfo info) {
        dataFile = info.dataFile;
        headerFile = info.headerFile;
        endian = info.endian;
        scaleFactor = info.scaleFactor;
        intercept = info.intercept;
        dataType = info.dataType;
        anatomy = info.anatomy;
        dimensionality = info.dimensionality;
        byteOffset = info.byteOffset;
        numImages = info.numImages;
        origin = new Point3D(info.origin);
        imageIndex = info.imageIndex;
        voxelOffset = new Dimension3D<Integer>(info.voxelOffset);
        imageLabel = info.imageLabel;
        spacing = info.spacing;
        arrayDim = info.arrayDim;
        imageLabel = info.imageLabel;
        mapping = info.mapping;
    }

    protected ImageInfo(ImageInfo info, String _imageLabel, int index) {
        dataFile = info.dataFile;
        headerFile = info.headerFile;
        endian = info.endian;
        scaleFactor = info.scaleFactor;
        intercept = info.intercept;
        dataType = info.dataType;
        anatomy = info.anatomy;

        dimensionality = 3;
        byteOffset = info.byteOffset;
        numImages = info.numImages;
        origin = new Point3D(info.origin);

        imageIndex = index;
        voxelOffset = new Dimension3D<Integer>(info.voxelOffset);
        imageLabel = info.imageLabel;
        spacing = info.spacing;
        arrayDim = info.arrayDim;
        imageLabel = _imageLabel;
        mapping = info.mapping;
    }


    public ImageInfo(IImageData data) {
        IImageSpace space = data.getImageSpace();

        IDimension<Integer> dim = space.getDimension();
        int[] dimensions = new int[]{dim.getDim(0), dim.getDim(1), dim.getDim(2)};


        setAnatomy((Anatomy3D) space.getAnatomy());

        setArrayDim(new Dimension3D<Integer>(dimensions[0], dimensions[1], dimensions[2]));
        setDataType(data.getDataType());
        setDimensionality(space.getNumDimensions());


        setSpacing(new Dimension3D<Double>(space.getSpacing(Axis.X_AXIS),
                space.getSpacing(Axis.Y_AXIS),
                space.getSpacing(Axis.Z_AXIS)));

        imageLabel = data.getImageLabel();

    }


    public ImageInfo selectInfo(int index) {
        if (index < 0 || index >= getNumImages()) {
            throw new IllegalArgumentException("illegal selection index for image info with " + getNumImages() + " sub images");
        }

        ImageInfo ret = new ImageInfo(this);
        ret.dimensionality = getDimensionality()-1;
        ret.imageIndex = index;
        ret.imageLabel = getHeaderFile().getName().getBaseName() + ":" + index;
        return ret;

    }

    public IImageSpace createImageSpace() {
        ImageAxis[] iaxes = new ImageAxis[3];
        AnatomicalAxis[] aaxes = anatomy.getAnatomicalAxes();

        IDimension realDim = calculateRealDim();
        for (int i = 0; i < iaxes.length; i++) {
            iaxes[i] = new ImageAxis(-realDim.getDim(i).doubleValue() / 2, realDim.getDim(i).doubleValue() / 2,
                    aaxes[i], (int) arrayDim.getDim(i).doubleValue());
        }

        return new ImageSpace3D(iaxes[0], iaxes[1], iaxes[2], mapping);

    }

    void setDataFile(FileObject fobj) {
        dataFile = fobj;
    }

    public FileObject getDataFile() {
        return dataFile;
    }

    void setHeaderFile(FileObject fobj) {
        headerFile = fobj;
    }


    public FileObject getHeaderFile() {
        return headerFile;
    }

    public ByteOrder getEndian() {
        return endian;
    }

    public IDimension getArrayDim() {
        return arrayDim;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getDataOffset() {
        return byteOffset;
    }

    public int getDataOffset(int index) {
        return (getArrayDim().product() * getDataType().getBytesPerUnit() * index) + getDataOffset();

    }

    public IDimension calculateRealDim() {
        Double[] realVals = new Double[arrayDim.numDim()];
        for (int i = 0; i < realVals.length; i++) {
            realVals[i] = arrayDim.getDim(i).intValue() * spacing.getDim(i).doubleValue();
        }
        return DimensionFactory.create(realVals);
    }


    public Anatomy getAnatomy() {
        return anatomy;
    }

    ImageMapping3D getMapping() {
        return mapping;
    }

    void setMapping(ImageMapping3D mapping) {
        this.mapping = mapping;
    }

    public String getImageLabel() {
        if (imageLabel == null) {
            if (getDimensionality() == 4) {
                imageLabel = getHeaderFile().getName().getBaseName() + ":" + getImageIndex();
            } else {
                //todo fix bug here NullPointerException caused by "freezing" mask
                imageLabel = getHeaderFile().getName().getBaseName();
            }
        }
                
        return imageLabel;
    }

    void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    public double getIntercept() {
        return intercept;
    }

    void setIntercept(float intercept) {
        this.intercept = intercept;
    }

    void setNumImages(int _numImages) {
        numImages = _numImages;
    }

    public int getNumImages() {
        return numImages;
    }

    void setVoxelOffset(Dimension3D _voxelOffset) {
        voxelOffset = _voxelOffset;
    }

    public Dimension3D getVoxelOffset() {
        return voxelOffset;
    }

    void setDimensionality(int _dimensionality) {
        dimensionality = _dimensionality;
    }

    public int getDimensionality() {
        return dimensionality;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    void setScaleFactor(float _scaleFactor) {
        scaleFactor = _scaleFactor;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    // public because this can be reset after loading when information is incorrect...
    public void setAnatomy(Anatomy3D _anatomy) {
        anatomy = _anatomy;
    }

    void setArrayDim(IDimension arrayDim) {
        this.arrayDim = arrayDim;
        calculateRealDim();
    }

    void setDataType(DataType dataType) {
        this.dataType = dataType;

    }

    void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }


    public Point3D getOrigin() {
        return origin;
    }

    void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    public IDimension getSpacing() {
        return spacing;
    }

    void setSpacing(Dimension3D spacing) {
        this.spacing = spacing;
    }

    public void setEndian(ByteOrder _endian) {
        endian = _endian;
    }


}