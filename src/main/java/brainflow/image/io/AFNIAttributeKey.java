package brainflow.image.io;


/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 9:41:58 AM
 */
public enum AFNIAttributeKey implements HeaderKey {

    //todo come up with mechanism for adhoc attributes
    FDRCURVE_000043,
    FDRCURVE_000042,
    FDRCURVE_000040,
    FDRCURVE_000039,
    FDRCURVE_000037,
    FDRCURVE_000036,
    FDRCURVE_000034,
    FDRCURVE_000032,
    FDRCURVE_000030,
    FDRCURVE_000028,
    FDRCURVE_000026,
    FDRCURVE_000024,
    FDRCURVE_000023,
    FDRCURVE_000021,
    FDRCURVE_000020,
    FDRCURVE_000018,
    FDRCURVE_000016,
    FDRCURVE_000014,
    FDRCURVE_000013,
    FDRCURVE_000011,
    FDRCURVE_000009,
    FDRCURVE_000007,
    FDRCURVE_000005,
    FDRCURVE_000003,
    FDRCURVE_000001,
    MARKS_FLAGS,
    MARKS_HELP,
    MARKS_LAB,
    MARKS_XYZ,
    BRICK_STATSYM,
    BRICK_STATAUX,
    STAT_AUX,
    IJK_TO_DICOM_REAL,
    HISTORY_NOTE,
    TYPESTRING,
    IDCODE_STRING,
    IDCODE_DATE,
    SCENE_DATA,
    LABEL_1,
    LABEL_2,
    DATASET_NAME,
    ORIENT_SPECIFIC,
    ORIGIN,
    DELTA,
    IJK_TO_DICOM,
    BRICK_STATS,
    DATASET_RANK,
    DATASET_DIMENSIONS,
    BRICK_TYPES,
    BRICK_FLOAT_FACS,
    BRICK_LABS,
    BRICK_KEYWORDS,
    BYTEORDER_STRING,
    TAXIS_NUMS,
    TAXIS_FLOATS,
    WARPDRIVE_MATVEC_FOR_000000,
    WARPDRIVE_MATVEC_INV_000000;


    public static void main(String[] args) {
        AFNIAttributeKey[] keys = values();

        for (int i = 0; i < keys.length; i++) {
            System.out.println("" + keys[i].name());
        }

        
    }


}
