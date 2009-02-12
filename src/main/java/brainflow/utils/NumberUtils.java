package brainflow.utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class NumberUtils {
    
    

    
    public NumberUtils() {
    }
    
    public static int ubyte(byte b) {
        return b & 0xff;
    }
    
    public static boolean equals(double a, double b, double eps) {
        return Math.abs(a-b) < eps;
    }
    
    public static boolean equals(float a, float b, float eps) {
        return Math.abs(a-b) < eps;
    }
    

}