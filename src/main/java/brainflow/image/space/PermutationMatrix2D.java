package brainflow.image.space;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2009
 * Time: 9:27:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PermutationMatrix2D {

    final int x0, x1, xoffset, y0, y1, yoffset;


    public PermutationMatrix2D(int _x0, int _x1, int _xoffset, int _y0, int _y1, int _yoffset) {
        x0 = _x0;
        x1 = _x1;
        y0 = _y0;
        y1 = _y1;
        xoffset = _xoffset;
        yoffset = _yoffset;

    }

    public int[] permute1(int x, int y) {
        int a1 = x0 * x + x1 * y + xoffset;
        int a2 = y0 * x + y1 * y + yoffset;

        return new int[] { a1, a2 };
    }

    public static void main(String[] args) {
        PermutationMatrix2D pmat = new PermutationMatrix2D(0, 1, 0, -1, 0, 255);
        System.out.println(Arrays.toString(pmat.permute1(0,255)));
    }


}
