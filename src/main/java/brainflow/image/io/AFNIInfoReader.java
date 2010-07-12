package brainflow.image.io;

import brainflow.core.BrainFlowException;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.utils.*;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import java.io.*;
import java.util.*;
import java.nio.ByteOrder;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 12, 2007
 * Time: 1:21:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class AFNIInfoReader extends AbstractInfoReader {


    private Map<AFNIAttributeKey, HeaderAttribute> attributeMap = new HashMap<AFNIAttributeKey, HeaderAttribute>();

    protected AFNIInfoReader() {
        super();
    }

    public AFNIInfoReader(FileObject headerFile, FileObject dataFile) {
        super(headerFile, dataFile);
    }

    public AFNIInfoReader(File headerFile, File dataFile) {
        super(headerFile, dataFile);
    }

    public AFNIInfoReader(String name) {
        super(new File(AFNIInfoReader.getHeaderName(name)), new File(AFNIInfoReader.getImageName(name)));
    }

    @Override
    public AFNIInfoReader create(FileObject headerFile, FileObject dataFile) {
        return new AFNIInfoReader(headerFile, dataFile);
    }

    public static boolean isHeaderFile(String name) {
        return name.endsWith(".HEAD") || name.endsWith(".HEAD.gz");

    }

    public static boolean isImageFile(String name) {
        return name.endsWith(".BRIK") || name.endsWith(".BRIK.gz");

    }

    public static String getHeaderName(String name) {
        if (name.endsWith(".BRIK")) {
            name = name.substring(0, name.length() - 4);
            return name + "HEAD";
        }

        if (name.endsWith(".HEAD")) {
            return name;
        }

        if (name.endsWith(".BRIK.gz")) {
            name = name.substring(0, name.length() - 7);
            return name + "HEAD";
        }

        return name + ".HEAD";
    }




    public static String getImageName(String name) {
        if (name.endsWith(".HEAD")) {
            name = name.substring(0, name.length() - 4);
            return name + "BRIK";
        }
        if (name.endsWith(".BRIK")) {
            return name;
        }

        if (name.endsWith(".BRIK.gz")) {
            return name;

        }

        if (name.endsWith(".HEAD.gz")) {
            name = name.substring(0, name.length() - 7);
            return name + "BRIK.gz";
        }

        return name + ".BRIK";
    }

    @Override
    public ImageInfo readInfo() throws BrainFlowException {
        return null;

    }

    @Override
    public List<ImageInfo> readInfoList() throws BrainFlowException {
        try {
            return readHeader(headerFile.getContent().getInputStream());
        } catch (FileSystemException e) {
            throw new BrainFlowException(e);
        } catch (IOException e) {
            throw new BrainFlowException(e);
        }

    }

    private List<ImageInfo> splitInfo(AFNIImageInfo info) {
        HeaderAttribute attr = attributeMap.get(AFNIAttributeKey.DATASET_RANK);
        int numImages = (Integer) attr.getData().get(1);

         List<ImageInfo> infoList = new ArrayList<ImageInfo>(numImages);
         //create instances
        for (int i = 0; i < numImages; i++) {
            AFNIImageInfo _info = new AFNIImageInfo(headerFile, dataFile, attributeMap);
            infoList.add(_info);
        }

        // fill instances with data. Yes, this is all very ugly.
        for (AFNIAttributeKey afniAttributeKey : attributeMap.keySet()) {
            processAttribute(afniAttributeKey, attributeMap.get(afniAttributeKey), infoList);
        }

        processByteOffsets(infoList);
        return infoList;


    }

    private List<ImageInfo> readHeader(InputStream istream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));


        HeaderAttribute ret;
        skipNewLines(reader);

        // parse the header
        do {
            ret = parseElement(reader);
            if (ret != null) {
                putAttribute(ret.getKey(), ret);
            }

        } while (ret != null);

        // determine how many sub-bricks
        HeaderAttribute attr = attributeMap.get(AFNIAttributeKey.DATASET_RANK);
        int numImages = (Integer) attr.getData().get(1);
        List<ImageInfo> infoList = new ArrayList<ImageInfo>(numImages);

        //create instances
        for (int i = 0; i < numImages; i++) {
            AFNIImageInfo info = new AFNIImageInfo(headerFile, dataFile, attributeMap);
            infoList.add(info);
        }

        // fill instances with data. Yes, this is all very ugly.
        for (AFNIAttributeKey afniAttributeKey : attributeMap.keySet()) {
            processAttribute(afniAttributeKey, attributeMap.get(afniAttributeKey), infoList);
        }

        processByteOffsets(infoList);

        return infoList;

    }

    // must be processed after all other attributes
    private void processByteOffsets(List<ImageInfo> infoList) {
        int offset = 0;
        for (ImageInfo anInfoList : infoList) {
            AFNIImageInfo info = (AFNIImageInfo) anInfoList;
            info.setByteOffset(offset);
            offset = offset + (info.getDataType().getBytesPerUnit() * info.getVolumeDim().product().intValue());
        }
    }

    private void skipNewLines(BufferedReader reader) throws IOException {
         boolean marked = false;
        while (reader.readLine().equals("")) {
            // marks stream for rewind
            reader.mark(500);
            marked = true;
        }


        if (marked) {
            reader.reset();
        }
    }

    private HeaderAttribute parseElement(BufferedReader reader) throws IOException {
        String typeStr = "";

        do {
            typeStr = reader.readLine();
            if (typeStr == null) return null;
        } while(typeStr.equals(""));





        String nameStr = reader.readLine();
        String countStr = reader.readLine();
        StringBuffer sb = new StringBuffer();

        String line;
        do {
            line = reader.readLine();
            if (line != null) {
                sb.append(line);
            }

        } while (line != null && !line.equals(""));



        HeaderAttribute.HEADER_ATTRIBUTE_TYPE type = HeaderAttribute.parseType(typeStr.replaceFirst("-", "_"));

        //todo should check to see whether attribute exists ..s

        HeaderAttribute attribute;

        if (AFNIAttributeKey.hasKey(HeaderAttribute.parseName(nameStr))) {
            attribute = HeaderAttribute.createAttribute(type, AFNIAttributeKey.valueOf(HeaderAttribute.parseName(nameStr)),
            HeaderAttribute.parseCount(countStr), sb.toString());
        } else {
            attribute = HeaderAttribute.createAttribute(type, AFNIAttributeKey.AD_HOC, HeaderAttribute.parseCount(countStr), sb.toString());
        }

        return attribute;

    }

    enum AFNI_TYPE {
        string_attribute,
        float_attribute,
        integer_attribute,
        double_attribute

    }

    public Map<AFNIAttributeKey, HeaderAttribute> getAttributeMap() {
        return Collections.unmodifiableMap(attributeMap);
    }


    void putAttribute(AFNIAttributeKey key, HeaderAttribute attribute) {
        attributeMap.put(key, attribute);

    }

    public HeaderAttribute getAttribute(AFNIAttributeKey key) {
        return attributeMap.get(key);

    }

    private void processOrigin(List<Float> origin, List<ImageInfo> infoList) {
        Point3D pt = new Point3D();
        pt.setX(origin.get(0));
        pt.setY(origin.get(1));
        pt.setZ(origin.get(2));

        for (ImageInfo info : infoList) {
            info.setOrigin(pt);
        }

    }

    private void processDimensions(List<Integer> dims, List<ImageInfo> infoList) {
        int i = dims.size() - 1;

        while (i > 0) {
            int dnum = dims.get(i);
            if (dnum == 0) {
                dims.remove(i);
            }

            i--;
        }

        IDimension idim = DimensionFactory.create(dims);
        for (ImageInfo info : infoList) {
            info.setVolumeDim(idim);
            //todo this is hard coded and dangerous
            info.setDimensionality(3);
        }

    }

    private void processSpacing(List<? extends Number> delta, List<ImageInfo> infoList) {
        Dimension3D<Double> spacing = new Dimension3D<Double>(Math.abs(delta.get(0).doubleValue()), Math.abs(delta.get(1).doubleValue()), Math.abs(delta.get(2).doubleValue()));
        for (ImageInfo info : infoList) {
            info.setSpacing(spacing);
        }

    }

    private void processByteOrder(List<String> orderList, List<ImageInfo> infoList) {
        String orderStr = orderList.get(0);

        ByteOrder bord;
        if (orderStr.equals("LSB_FIRST")) {
            bord = ByteOrder.LITTLE_ENDIAN;
        } else if (orderStr.equals("MSB_FIRST")) {
            bord = ByteOrder.BIG_ENDIAN;
        } else {
            throw new RuntimeException("unrecognized BYTEORDER attribute file : " + orderStr);
        }

        for (ImageInfo info : infoList) {
            info.setEndian(bord);
        }
    }

    private void processBrickTypes(List<Integer> types, List<ImageInfo> infoList) {
        //only handle first type
        int n = 0;
        for (Integer code : types) {
            ImageInfo info = infoList.get(n);
            switch (code) {
                case 0:
                    info.setDataType(DataType.BYTE);
                    break;
                case 1:
                    info.setDataType(DataType.SHORT);
                    break;
                case 3:
                    info.setDataType(DataType.FLOAT);
                    break;
                case 5:
                    throw new IllegalArgumentException("Unsupported data type " + code);
                   
                default:
                    throw new IllegalArgumentException("Illegal AFNI data code " + code);

            }

            n++;
        }
    }


    private void processDatasetRank(List<Integer> rank, List<ImageInfo> infoList) {
        int dim1 = rank.get(0);
        if (dim1 != 3) {
            throw new IllegalArgumentException("AFNI attribute DATASET_RANK[0] must be 3");
        }

        int dim2 = rank.get(1);
        assert dim2 == infoList.size();

        // is it a bucket? is it a brik? 
        for (ImageInfo info : infoList) {
            info.setNumVolumes(1);
        }

    }

    private void processOrientSpecific(List<Integer> codes, List<ImageInfo> infoList) {
        if (codes.size() != 3) {
            throw new IllegalArgumentException("AFNI attribute ORIENT_SPECIFIC must have three entries");
        }

        AnatomicalAxis[] axes = new AnatomicalAxis[3];
        for (int i = 0; i < axes.length; i++) {
            int code = codes.get(i);
            switch (code) {
                case 0:
                    axes[i] = AnatomicalAxis.RIGHT_LEFT;
                    break;
                case 1:
                    axes[i] = AnatomicalAxis.LEFT_RIGHT;
                    break;
                case 2:
                    axes[i] = AnatomicalAxis.POSTERIOR_ANTERIOR;
                    break;
                case 3:
                    axes[i] = AnatomicalAxis.ANTERIOR_POSTERIOR;
                    break;
                case 4:
                    axes[i] = AnatomicalAxis.INFERIOR_SUPERIOR;
                    break;
                case 5:
                    axes[i] = AnatomicalAxis.SUPERIOR_INFERIOR;
                    break;
                default:
                    throw new IllegalArgumentException("unrecognized code " + code + " for AFNI attribure ORIENT_SPECIFIC");
            }

        }

        Anatomy3D ret = Anatomy3D.matchAnatomy(axes[0], axes[1], axes[2]);
        if (ret == null) {
            throw new IllegalArgumentException("Illegal Axis configuration in AFNI attriute ORIENT_SPECIFIC");
        } else {
            for (ImageInfo info : infoList) {
                info.setAnatomy(ret);
            }

        }
    }

    private void processBrickFloatFacs(List<Float> facs, List<ImageInfo> infoList) {
        assert facs.size() == infoList.size();
        for (int i = 0; i < facs.size(); i++) {
            infoList.get(i).setScaleFactor(facs.get(i));
        }
    }

    private void processBrickLabels(List<String> labels, List<ImageInfo> infoList) {
        if (labels.size() != infoList.size()) {
            throw new IllegalArgumentException("number of image labels does not equal number of images -- labels: " + labels.size() + " != images: " + infoList.size());
        }
        if (labels.size() > 1) {
            for (int i = 0; i < labels.size(); i++) {
                infoList.get(i).setImageLabel(labels.get(i));
            }
        } // todo consider the impact of this decision ...
    }

    private void processAttribute(AFNIAttributeKey key, HeaderAttribute attribute, List<ImageInfo> infoList) {
        HeaderAttribute.IntegerAttribute iattr;
        HeaderAttribute.FloatAttribute fattr;
        HeaderAttribute.StringAttribute sattr;

        switch (key) {

            case BRICK_FLOAT_FACS:
                fattr = (HeaderAttribute.FloatAttribute) attribute;
                processBrickFloatFacs(fattr.getData(), infoList);
                break;
            case BRICK_KEYWORDS:
                break;
            case BRICK_LABS:
                sattr = (HeaderAttribute.StringAttribute) attribute;
                processBrickLabels(sattr.getData(), infoList);
                break;
            case BRICK_STATS:
                break;
            case BRICK_TYPES:
                iattr = (HeaderAttribute.IntegerAttribute) attribute;
                processBrickTypes(iattr.getData(), infoList);
                break;
            case BYTEORDER_STRING:
                sattr = (HeaderAttribute.StringAttribute) attribute;
                processByteOrder(sattr.getData(), infoList);
                break;
            case DATASET_DIMENSIONS:
                iattr = (HeaderAttribute.IntegerAttribute) attribute;
                processDimensions(iattr.getData(), infoList);
                break;
            case DATASET_NAME:
                break;
            case DATASET_RANK:
                iattr = (HeaderAttribute.IntegerAttribute) attribute;
                processDatasetRank(iattr.getData(), infoList);
                break;
            case DELTA:
                fattr = (HeaderAttribute.FloatAttribute) attribute;
                processSpacing(fattr.getData(), infoList);
                break;
            case HISTORY_NOTE:
                break;
            case IDCODE_DATE:
                break;
            case IDCODE_STRING:
                break;
            case IJK_TO_DICOM:
                break;
            case LABEL_1:
                break;
            case LABEL_2:
                break;
            case ORIENT_SPECIFIC:
                iattr = (HeaderAttribute.IntegerAttribute) attribute;
                processOrientSpecific(iattr.getData(), infoList);
                break;
            case ORIGIN:
                fattr = (HeaderAttribute.FloatAttribute) attribute;
                processOrigin(fattr.getData(), infoList);
            case SCENE_DATA:
                break;
            case TYPESTRING:
                break;
        }
    }


    public static void main(String[] args) {

    }
}
