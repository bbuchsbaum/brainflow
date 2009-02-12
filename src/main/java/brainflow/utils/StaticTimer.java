/*
 * StaticTimer.java
 *
 * Created on February 19, 2003, 5:54 PM
 */

package brainflow.utils;

/**
 *
 * @author  Bradley
 */
public class StaticTimer {
    
    static long curTime;
    
    /** Creates a new instance of StaticTimer */
    private StaticTimer() {
    }
    
    public static void start() {
        curTime = System.currentTimeMillis();
    }
    
    public static void report(String message) {
        long last = curTime;
        curTime = System.currentTimeMillis();
        System.err.println(message + " : increment = " + (curTime - last));
    }
    
    public static void end(String message) {
        curTime = System.currentTimeMillis();
        System.err.println(message + " : end time = " + curTime);
    }
        
        
}
