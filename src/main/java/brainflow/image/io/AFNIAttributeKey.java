package brainflow.image.io;

import java.util.*;


/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 9:41:58 AM
 */
public enum AFNIAttributeKey implements HeaderKey {

    AD_HOC,
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

    static Set<String> nameSet;

    public static boolean hasKey(String name) {
        if (nameSet == null) {
            AFNIAttributeKey[] keys = AFNIAttributeKey.values();
            List<String> names = new ArrayList<String>();
            for (AFNIAttributeKey key : keys) {
                names.add(key.name());
            }
            nameSet = new HashSet<String>(names);
        }

        return nameSet.contains(name);
    }



    public static void main(String[] args) {
        AFNIAttributeKey[] keys = values();

        for (int i = 0; i < keys.length; i++) {
            System.out.println("" + keys[i].name());
        }
    }


}
