package brainflow.image.space;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 14, 2009
 * Time: 8:10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class PermutationMatrix3D {

    final int x0, x1, x2, xoffset;

    final int y0, y1, y2, yoffset;

    final int z0, z1, z2, zoffset;

    public PermutationMatrix3D(int x0, int x1, int x2, int xoffset, int y0, int y1, int y2, int yoffset, int z0, int z1, int z2, int zoffset) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.xoffset = xoffset;
        this.y0 = y0;
        this.y1 = y1;
        this.y2 = y2;
        this.yoffset = yoffset;
        this.z0 = z0;
        this.z1 = z1;
        this.z2 = z2;
        this.zoffset = zoffset;
    }



    public int[] permute(int x, int y, int z) {
        int a1 = x0 * x + x1 * y + x2*z + xoffset;
        int a2 = y0 * x + y1 * y + y2*z + yoffset;
        int a3 = z0 * x + z1 * y + z2*z + zoffset;
        return new int[] { a1, a2, a3 };
    }


}
