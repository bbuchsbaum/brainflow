/*
 * ToStringGenerator.java
 *
 * Created on February 11, 2003, 10:28 AM
 */

package brainflow.utils;

import java.lang.reflect.Field;


/**
 * @author Bradley
 */
public class ToStringGenerator {

    /**
     * Creates a new instance of ToStringGenerator
     */
    public ToStringGenerator() {
    }


    private int calculateDimensions(Class c) {
        int dims = 0;
        while (c.isArray()) {
            dims++;
            c = c.getComponentType();
        }
        return dims;
    }

    public String generateToString(Object parent) {
        Field[] fields = parent.getClass().getDeclaredFields();
        Class cSuper = parent.getClass().getSuperclass();
        if (fields.length == 0 && cSuper == Object.class)
            return "The class has no attributes";
        StringBuffer buffer = new StringBuffer();
        buffer.append(parent.getClass().getName());
        buffer.append(" [");
        buffer.append(cSuper.getName());
        buffer.append("]" + System.getProperty("line.separator"));
        if (cSuper != Object.class)
            buffer.append(super.toString()); // Apending the toString of the super class

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (fields[i].getType().isPrimitive()) {
                buffer.append(System.getProperty("line.separator") + getPrimitiveFieldData(fields[i], parent));
            } else {
                if (fields[i].getType().isArray()) {
                    int dimensions = calculateDimensions(fields[i].getType());
                    if (dimensions > 1) {
                        buffer.append(System.getProperty("line.separator") + "The Attribute namely '" + fields[i].getName() + "' has more than zero dimension that is not supported by this code!");
                    } else if (fields[i].getType().getComponentType().isPrimitive()) {
                        buffer.append(System.getProperty("line.separator") + getPrimitiveArrayFieldData(fields[i], parent));
                    } else {
                        buffer.append(System.getProperty("line.separator") + getNonPrimitiveArrayFieldData(fields[i], parent));
                    }
                } else {
                    buffer.append(System.getProperty("line.separator") + getNonPrimitiveFieldData(fields[i], parent));
                }
            }
        } // End of For Loop
        return buffer.toString();
    }

    private String getNonPrimitiveArrayFieldData(Field field, Object parent) {
        StringBuffer output = new StringBuffer();
        output.append(field.getName() + "[] :: Length = ");
        try {
            Object value = field.get(parent);
            Object[] values = null;
            values = (Object[]) value;

            if (values == null) {
                output.append("none");
            } else {
                output.append(values.length);
                for (int i = 0; i < values.length; i++) {
                    output.append(System.getProperty("line.separator"));
                    output.append(field.getName() + "[" + i + "] :- " + values[i].toString());
                }
            }
        }
        catch (IllegalAccessException exception) {
            output.append("*Access denied*");
        }
        return output.toString();
    }

    private String getNonPrimitiveFieldData(Field field, Object parent) {
        StringBuffer output = new StringBuffer();
        output.append(field.getName() + " :- ");
        try {
            Object value = field.get(parent);
            if (value != null) {
                output.append(value.toString());
            } else {
                output.append("null");
            }

        }
        catch (IllegalAccessException exception) {
            output.append("*Access denied*");
        }
        return output.toString();
    }

    private String getPrimitiveArrayFieldData(Field field, Object parent) {
        StringBuffer output = new StringBuffer();
        output.append(field.getName() + "[] :: Length = ");
        try {
            Object value = field.get(parent);
            int aLength = 0;
            Object[] values = null;
            char primitiveTypeIdentifier = field.getType().getComponentType().toString().charAt(0);
            char extraPrimitiveTypeIdentifier = field.getType().getComponentType().toString().charAt(1);
            switch (primitiveTypeIdentifier) {
                case 'f':
                    float[] fValues = (float[]) value;
                    aLength = fValues.length;
                    values = new Object[aLength];
                    for (int i = 0; i < fValues.length; i++)
                        values[i] = new Float(fValues[i]);
                    break;
                case 'i':
                    int[] iValues = (int[]) value;
                    aLength = iValues.length;
                    values = new Object[aLength];
                    for (int i = 0; i < iValues.length; i++)
                        values[i] = new Integer(iValues[i]);
                    break;
                case 'd':
                    double[] dValues = (double[]) value;
                    aLength = dValues.length;
                    values = new Object[aLength];
                    for (int i = 0; i < dValues.length; i++)
                        values[i] = new Double(dValues[i]);
                    break;
                case 's':
                    short[] sValues = (short[]) value;
                    aLength = sValues.length;
                    values = new Object[aLength];
                    for (int i = 0; i < sValues.length; i++)
                        values[i] = new Short(sValues[i]);
                    break;
                case 'b':
                    if (extraPrimitiveTypeIdentifier == 'o') {
                        boolean[] bValues = (boolean[]) value;
                        aLength = bValues.length;
                        values = new Object[aLength];
                        for (int i = 0; i < bValues.length; i++)
                            values[i] = new Boolean(bValues[i]);
                    } else {
                        byte[] bValues = (byte[]) value;
                        aLength = bValues.length;
                        values = new Object[aLength];
                        for (int i = 0; i < bValues.length; i++)
                            values[i] = new Byte(bValues[i]);
                    }
                    break;
                case 'l':
                    long[] lValues = (long[]) value;
                    aLength = lValues.length;
                    values = new Object[aLength];
                    for (int i = 0; i < lValues.length; i++)
                        values[i] = new Long(lValues[i]);
                    break;
                case 'c':
                    char[] cValues = (char[]) value;
                    aLength = cValues.length;
                    values = new Object[aLength];
                    for (int i = 0; i < cValues.length; i++)
                        values[i] = new Character(cValues[i]);
                    break;
                default:
                    output.append("*Invalid primitive type*");
                    break;
            } // End of Switch

            output.append(aLength);
            for (int i = 0; i < values.length; i++) {
                output.append(System.getProperty("line.separator"));
                output.append(field.getName() + "[" + i + "] = " + values[i].toString());
            }
        }
        catch (IllegalAccessException exception) {
            output.append("*Access denied*");
        }
        return output.toString();
    }

    private String getPrimitiveFieldData(Field field, Object parent) {
        StringBuffer output = new StringBuffer();
        output.append(field.getName() + " = ");
        try {

            Object value = field.get(parent);
            output.append(value.toString());
        }
        catch (IllegalAccessException exception) {
            output.append("*Access denied*");
        }
        return output.toString();
    }
}


