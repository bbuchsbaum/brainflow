package brainflow.image.space;

import brainflow.math.Index3D;
import brainflow.utils.Bounds3D;
import brainflow.utils.Dimension3D;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 15, 2010
 * Time: 8:00:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexSet3D {


    private int[][] indices;

    private IImageSpace3D space;

    private Bounds3D<Integer> bounds;

    public IndexSet3D(IImageSpace3D space, int[] _indices) {
        if (_indices.length == 0) throw new IllegalArgumentException("cannot have zero length IndexSet3D");
        this.space = space;
        indices = new int[_indices.length][3];
        for (int i = 0; i < _indices.length; i++) {
            Index3D vox = space.indexToGrid(_indices[i]);
            putIndex(i, vox, indices);
        }
    }

    public IndexSet3D(IImageSpace3D space, List<Index3D> _indices) {
        this.space = space;
        indices = new int[_indices.size()][3];
        for (int i = 0; i < _indices.size(); i++) {
            Index3D vox = _indices.get(i);
            putIndex(i, vox, indices);
        }
    }

    private void putIndex(int i, Index3D vox, int[][] indices) {
        indices[i][0] = vox.i1();
        indices[i][1] = vox.i2();
        indices[i][2] = vox.i3();       
    }

    public Bounds3D<Integer> getBounds() {
        int xmin=space.getDimension(Axis.X_AXIS);
        int xmax = -1;

        int ymin=space.getDimension(Axis.Y_AXIS);
        int ymax = -1;

        int zmin=space.getDimension(Axis.Z_AXIS);
        int zmax = -1;


        for (int i=0; i<indices.length; i++) {
            xmin = Math.min(indices[i][0], xmin);
            ymin = Math.min(indices[i][1], ymin);
            zmin = Math.min(indices[i][2], zmin);

            xmax = Math.max(indices[i][0], xmax);
            ymax = Math.max(indices[i][1], ymax);
            zmax = Math.max(indices[i][2], zmax);

        }

        return new Bounds3D<Integer>(xmin,xmax,ymin,ymax,zmin,zmax);
    }
}
