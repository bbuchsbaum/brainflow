package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 9:26:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMask {


    public static byte[] testMultiply(byte[] in, byte[] mask) {
        for (int i=0; i<in.length; i++) {
            in[i] = (byte)(in[i] * mask[i]);
        }

        return in;

    }

    public static byte[] testAnd(byte[] in, byte[] mask) {
        for (int i=0; i<in.length; i++) {
            in[i] = (byte)(in[i] & mask[i]);
        }

        return in;

    }

    public static byte[] testIf(byte[] in, byte[] mask) {
        for (int i=0; i<in.length; i++) {
            if (mask[i] == 0) {
                in[i] = 0;
            }
        }

        return in;

    }


    public static void main(String[] args) {

        int n = 256*256*256*2;
        byte[] in = new byte[n];
        byte[] mask = new byte[n];
        for (int i=0; i<in.length; i++) {
            in[i] = (byte)(Math.random() * 255);
            mask[i] = (byte)Math.round(Math.random());
        }

        long time = System.currentTimeMillis();
        long lastTime = time;
        long curTime = 0;

        for (int i=0; i<100; i++) {
            byte[] b = testAnd(in, mask);
            curTime = System.currentTimeMillis();
            lastTime = curTime;
        }

        long avg = (curTime - time)/100;
      

    }

}
