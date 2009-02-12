package brainflow.image.operations;

import cern.colt.list.IntArrayList;
import brainflow.image.data.*;
import brainflow.image.io.BrainIO;
import brainflow.image.space.ImageSpace3D;
import brainflow.utils.DataType;
import brainflow.utils.StaticTimer;
import brainflow.math.Index3D;
import brainflow.math.WIndex3D;

import java.util.List;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 10:12:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegionGrowingImageFilter extends AbstractImageFilter {

    private int[] seed;
    private int ndim;

    private static int[] I_INDICES = new int[26];
    private static int[] J_INDICES = new int[26];
    private static int[] K_INDICES = new int[26];

    static {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if ((i == 0) && (j == 0) && (k == 0)) {
                        continue;
                    }
                    I_INDICES[count] = i;
                    J_INDICES[count] = j;
                    K_INDICES[count] = k;
                    count++;
                }
            }
        }
    }


    public RegionGrowingImageFilter(WIndex3D _seed) {
        seed = new int[3];
        seed[0] = _seed.i1();
        seed[1] = _seed.i2();
        seed[2] = _seed.i3();

    }

    public void setSeed(Index3D _seed) {
        seed = new int[3];
        seed[0] = _seed.i1();
        seed[1] = _seed.i2();
        seed[2] = _seed.i3();
    }


    public IImageData getOutput() {
        List imageList = getSources();

        BasicImageData data = (BasicImageData) imageList.get(0);

        ndim = data.getImageSpace().getNumDimensions();

        if (ndim == 3) {
            BasicImageData3D data3d = (BasicImageData3D) data;
            SeedRegion3D sreg = new SeedRegion3D(seed, data3d);
            IntArrayList alist = sreg.growRegion();
            System.out.println("cluster is of size: " + alist.size());
            System.out.println("cluster: " + alist);

        }


        return null;


    }

    public static void main(String[] args) {
        try {
            IImageData data = BrainIO.readAnalyzeImage("c:/brains/sign_lang/zstat1");
            ThresholdImageFilter tfilter = new ThresholdImageFilter();
            tfilter.setThreshold(2);
            tfilter.addInput(data);
            data = tfilter.getOutput();

            RegionGrowingImageFilter ifilter = new RegionGrowingImageFilter(new WIndex3D(69, 81, 36));
            ifilter.addInput(data);


            StaticTimer.start();
            ifilter.getOutput();
            StaticTimer.report("finished");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    class SeedRegion3D {

        IImageData3D data;
        ImageSpace3D space;

        int[] seed;

        IntArrayList regionIndices = new IntArrayList(50);

        DataWriter3D visited = null;

        Stack<int[]> searchStack = new Stack<int[]>();

        public SeedRegion3D(int[] _seed, BasicImageData3D _data) {
            seed = _seed;
            data = _data;
            space = (ImageSpace3D) data.getImageSpace();
            visited = ((IImageData3D) BasicImageData.create(space, DataType.INTEGER)).createWriter(false);
        }

        public IntArrayList growRegion() {

            int centroidIdx = data.indexOf(seed[0], seed[1], seed[2]);

            // mark center as visited
            if (data.value(centroidIdx) > 0) {
                visited.setValue(centroidIdx, 1);
                searchStack.push(seed);
                //regionIndices.add(centroidIdx);
                //searchRegion(seed);
            } else {
                return regionIndices;
            }


            while (searchStack.size() > 0) {
                int[] centroid = searchStack.pop();
                searchRegion(centroid);
            }


            return regionIndices;
        }


        private void searchRegion(int[] centroid) {

            int centroidIdx = data.indexOf(centroid[0], centroid[1], centroid[2]);
            regionIndices.add(centroidIdx);


            int[] searchSet = getIndexSet(centroid);


            for (int i = 0; i < searchSet.length; i++) {
                int idx = searchSet[i];
                if (visited.value(idx) != 0) {
                    continue;
                }
                if (data.value(idx) > 0) {
                    
                    Index3D voxel = data.indexToGrid(idx);
                    visited.setValue(idx, 1);
                    searchStack.push(voxel.toArray());

                    //searchRegion(voxel);
                } else {
                    visited.setValue(idx, 1);
                }
            }

            return;
        }

        private int[] getIndexSet(int[] centroid) {

            int[] set = new int[26];

            for (int count = 0; count < 26; count++) {
                set[count] = data.indexOf(centroid[0] + I_INDICES[count], centroid[1] + J_INDICES[count], centroid[2] + K_INDICES[count]);
                count++;

            }

            return set;
        }


    }
}
