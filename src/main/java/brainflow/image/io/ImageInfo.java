package brainflow.image.io;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.data.IImageData;
import brainflow.image.space.*;
import brainflow.utils.*;
import org.apache.commons.vfs.FileObject;

import java.nio.ByteOrder;
import java.util.Arrays;


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



    private IDimension<Integer> dimensions;

    private IDimension<Integer> volumeDim = new Dimension3D<Integer>(0, 0, 0);

    private IDimension<Double> spacing = new Dimension3D<Double>(0.0, 0.0, 0.0);

    private IDimension<Integer> voxelOffset = new Dimension3D<Integer>(0, 0, 0);

    private ImageMapping3D mapping;

    private Point3D origin = new Point3D();

    private int numVolumes = 1;

    private int imageIndex = 0;

    private int byteOffset = 0;

    private int dimensionality;

    private Anatomy3D anatomy = Anatomy3D.AXIAL_LPI;

    private DataType dataType = DataType.BYTE;

    private ByteOrder endian = ByteOrder.nativeOrder();

    private float scaleFactor = 1;

    private float intercept = 0;
    
    // List<String> subLabels ....

    private String imageLabel = null;

    private FileObject dataFile;

    private FileObject headerFile;

    protected ImageInfo() {
    }

    private ImageInfo(FileObject headerFile, FileObject dataFile) {
        this.headerFile = headerFile;
        this.dataFile = dataFile;
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
        numVolumes = info.numVolumes;
        origin = new Point3D(info.origin);
        imageIndex = info.imageIndex;
        voxelOffset = info.voxelOffset;
        imageLabel = info.imageLabel;
        spacing = info.spacing;
        volumeDim = info.volumeDim;
        imageLabel = info.imageLabel;
        mapping = info.mapping;
        dimensions =info.dimensions;
    }

    protected ImageInfo(ImageInfo info, String _imageLabel, int index) {
        dataFile = info.dataFile;
        headerFile = info.headerFile;
        endian = info.endian;
        scaleFactor = info.scaleFactor;
        intercept = info.intercept;
        dataType = info.dataType;
        anatomy = info.anatomy;
        dimensionality = info.dimensionality;
        byteOffset = info.byteOffset;
        numVolumes = info.numVolumes;
        origin = new Point3D(info.origin);
        imageIndex = index;
        voxelOffset = info.voxelOffset;
        spacing = info.spacing;
        volumeDim = info.volumeDim;
        imageLabel = _imageLabel;
        mapping = info.mapping;
        dimensions =info.dimensions;
    }


    public ImageInfo(IImageData data) {
        IImageSpace space = data.getImageSpace();

        IDimension<Integer> dim = space.getDimension();

        int[] dimensions = new int[]{dim.getDim(0), dim.getDim(1), dim.getDim(2)};


        setAnatomy((Anatomy3D) space.getAnatomy());

        setVolumeDim(new Dimension3D<Integer>(dimensions[0], dimensions[1], dimensions[2]));
        setDataType(data.getDataType());
        setDimensionality(space.getNumDimensions());


        setSpacing(new Dimension3D<Double>(space.getSpacing(Axis.X_AXIS),
                space.getSpacing(Axis.Y_AXIS),
                space.getSpacing(Axis.Z_AXIS)));

        imageLabel = data.getImageLabel();

        if (imageLabel.equals("") || imageLabel == null) {
            imageLabel = "unititled_" + System.currentTimeMillis();
        }


    }

    public static boolean isValidExtension(String fileName, String[] validExtensions) {
        for (int i = 0; i < validExtensions.length; i++) {
            if (fileName.endsWith(validExtensions[i])) {
                return true;
            }

        }

        return false;


    }


    public static class Builder extends AbstractBuilder {

        protected ImageInfo info;

        public Builder() {
            info = new ImageInfo();

        }

        protected Builder(ImageInfo info) {
            this.info = info;
        }

        public Builder(FileObject headerFile, FileObject dataFile) {
            info = new ImageInfo(headerFile, dataFile);
        }

        public Builder headerFile(FileObject headerFile) {
            info.headerFile = headerFile;
            return this;
        }

        public Builder dataFile(FileObject dataFile) {
            info.dataFile = dataFile;
            return this;
        }

        public Builder volumeDim(IDimension<Integer> dim) {
            info.volumeDim = dim;
            return this;
        }
        
        public Builder dimensions(IDimension<Integer> dim) {
            info.dimensions = dim;
            return this;
        }

        public Builder anatomy(Anatomy3D anatomy) {
            info.anatomy = anatomy;
            return this;
        }

        public Builder dataType(DataType dataType) {
            info.dataType = dataType;
            return this;
        }

        public Builder dimensionality(int dimensionality) {
            info.dimensionality = dimensionality;
            return this;
        }

        public Builder endian(ByteOrder order) {
            info.endian = order;
            return this;
        }

        public Builder spacing(IDimension<Double> spacing) {
            info.spacing = spacing;
            return this;
        }

        public Builder imageLabel(String label) {
            info.imageLabel = label;
            return this;
        }

        public Builder byteOffset(int offset) {
            info.byteOffset = offset;
            return this;
        }

        public Builder numVolumes(int num) {
            info.numVolumes = num;
            return this;
        }

        public Builder origin(Point3D origin) {
            info.origin = origin;
            return this;
        }

        public Builder imageIndex(int index) {
            info.imageIndex = index;
            return this;
        }

        public Builder intercept(double intercept) {
            info.intercept = (float) intercept;
            return this;
        }

        public Builder scaleFactor(double scaleFactor) {
            info.scaleFactor = (float) scaleFactor;
            return this;
        }

        public Builder voxelOffset(IDimension<Integer> offset) {
            info.voxelOffset = offset;
            return this;
        }

        public Builder mapping(ImageMapping3D mapping) {
            info.mapping = mapping;
            return this;
        }

        @Override
        public void checkBuilt() {
            if (info.dimensions == null || info.dimensions.numDim() < 3) {
                throw new IllegalStateException("invalid dimensions " + info.dimensions);
            }
        }

        public ImageInfo build() {
            this.checkBuilt();
            this.isBuilt = true;
            return this.info;
        }
    }


    public ImageInfo selectInfo(int index) {
        if (index < 0 || index >= getNumVolumes()) {
            throw new IllegalArgumentException("illegal selection index for image info with " + getNumVolumes() + " sub images");
        }

        ImageInfo.Builder builder = new ImageInfo.Builder(this);
        builder.dimensionality(getDimensionality() - 1);
        builder.imageIndex(index);
        builder.imageLabel(getHeaderFile().getName().getBaseName() + ":" + index);
        return builder.build();

    }

    public IImageSpace createImageSpace() {
        ImageAxis[] iaxes = new ImageAxis[3];
        AnatomicalAxis[] aaxes = anatomy.getAnatomicalAxes();

        IDimension<Double> realDim = ImageInfo.calculateRealDim(volumeDim, spacing);
        for (int i = 0; i < iaxes.length; i++) {
            iaxes[i] = new ImageAxis(-realDim.getDim(i) / 2, realDim.getDim(i) / 2,
                    aaxes[i], volumeDim.getDim(i));
        }

        return new ImageSpace3D(iaxes[0], iaxes[1], iaxes[2], mapping);

    }

    public ImageReader createImageReader() {
        return new BasicImageReader3D(this);
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


    public IDimension<Integer> getDimensions() {
        return dimensions;
    }


    public FileObject getHeaderFile() {
        return headerFile;
    }

    public ByteOrder getEndian() {
        return endian;
    }

    public IDimension<Integer> getVolumeDim() {
        return volumeDim;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getDataOffset() {
        return byteOffset;
    }

    public int getDataOffset(int index) {
        return (getVolumeDim().product().intValue() * getDataType().getBytesPerUnit() * index) + getDataOffset();

    }

    public static IDimension<Double> calculateRealDim(IDimension<Integer> arrayDim, IDimension<? extends Number> spacing) {
        if (arrayDim.numDim() != spacing.numDim()) {
            throw new IllegalArgumentException("dimensions do not match: volumeDim = " + arrayDim.numDim() + "  spacing = " + spacing.numDim());
        }
        Double[] realVals = new Double[arrayDim.numDim()];
        for (int i = 0; i < realVals.length; i++) {
            realVals[i] = spacing.getDim(i).doubleValue() * arrayDim.getDim(i).doubleValue();

        }
        return DimensionFactory.create(realVals);
    }


    public Anatomy getAnatomy() {
        return anatomy;
    }

    public ImageMapping3D getMapping() {
        return mapping;
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


    void setNumVolumes(int _numVolumes) {
        numVolumes = _numVolumes;
    }

    public int getNumVolumes() {
        return numVolumes;
    }


    public IDimension<Integer> getVoxelOffset() {
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


    // public because this can be reset after loading when information is incorrect...
    public void setAnatomy(Anatomy3D _anatomy) {
        anatomy = _anatomy;
    }

    void setVolumeDim(IDimension volumeDim) {
        this.volumeDim = volumeDim;
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

    public IDimension<Double> getSpacing() {
        return spacing;
    }

    void setSpacing(Dimension3D<Double> spacing) {
        this.spacing = spacing;
    }

    public void setEndian(ByteOrder _endian) {
        endian = _endian;
    }


}