package brainflow.utils;

import java.io.Serializable;

/**
 * Title:        Parvenu Mri Imaging Application
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Laboratory for Cognitive Brain Research
 *
 * @author Brad Buchsbaum
 * @version 1.0
 */

public enum DataType implements Serializable {

    UBYTE("UByte", 8, 0),
    BOOLEAN("Boolean", 1, 1),
    BYTE("Byte", 1, 2),
    SHORT("Short", 2, 4),
    INTEGER("Integer", 4, 8),
    FLOAT("Float", 4, 16),
    DOUBLE("Double", 8, 32),
    LONG("Long", 8, 64),
    COMPLEX("Complex", 16, 128);


    private final String idString;

    private final int bytes;

    private final int datacode;


    private DataType(String _idString, int _bytes, int _datacode) {
        idString = _idString;
        bytes = _bytes;
        datacode = _datacode;

    }


    public String toString() {
        return idString;
    }

    public static DataType getType(String s) {
        DataType[] dt = DataType.values();
        for (int i = 0; i < dt.length; i++) {
            if (s.equalsIgnoreCase(dt[i].toString())) {
                return dt[i];
            }

        }

        return null;
    }

    public int getDataCode() {
        return datacode;
    }

    public int getBytesPerUnit() {
        return bytes;
    }

    public int getBitsPerUnit() {
        return bytes * 8;
    }


}