package brainflow.image.space;

import brainflow.image.space.IImageSpace3D;
import brainflow.math.Index3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 13, 2010
 * Time: 8:32:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class VoxelSet3D {

    private IImageSpace3D refSpace;
       
    int[][] voxels;

    public VoxelSet3D(IImageSpace3D refSpace, int[] indices) {
        this.refSpace = refSpace;
        voxels = new int[indices.length][3];

        for (int i=0; i<indices.length; i++) {
            Index3D index = refSpace.indexToGrid(i);
            voxels[i][0] = index.i1();
            voxels[i][1] = index.i2();
            voxels[i][2] = index.i3();
        }
    }

    
}
