package brainflow.image.data;

import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.iterators.ValueIterator;
import brainflow.math.Index3D;
import cern.colt.list.IntArrayList;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 12, 2010
 * Time: 9:54:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterSet implements Iterable<ClusterSet.Cluster>{


    private HashMap<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();

    private IImageData3D labels;

    private IImageData3D data;

    public ClusterSet() {
    }

    public ClusterSet(IImageData3D labels, IImageData3D data) {
        if (!data.getImageSpace().equals(labels.getImageSpace())) {
            throw new IllegalArgumentException("labels and data arguments must have same IImageSpace");
        }
        this.labels = labels;
        this.data = data;
        build();
    }


    public int size() {
        return clusters.size();
    }

    public boolean isEmpty() {
        return true;
    }

    private void build() {
        ValueIterator imageIterator = labels.valueIterator();
        //Map<Integer, Integer> sizes = new HashMap<Integer, Integer>();

        while (imageIterator.hasNext()) {
            int label = (int) imageIterator.next();
            if (label > 0) {
                Cluster cluster = clusters.get(label);
                if (cluster == null) {
                    cluster = new Cluster(label);
                    cluster.addIndex(imageIterator.index());
                    clusters.put(label, cluster);
                } else {
                    cluster.addIndex(imageIterator.index());
                }
            }

        }

    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Set<Integer> iset = clusters.keySet();
        for (int i : iset) {
            sb.append(clusters.get(i).toString());
            sb.append("\n");

        }

        return sb.toString();
    }

    @Override
    public Iterator<Cluster> iterator() {
        return getClusters().iterator();
    }

    public class Cluster implements Comparable<Cluster> {

        //VoxelSet should be a set of voxels (which can be mapped to coordinates (CoordinateSet)
        // and can be searched, and have a bounding box, and distance functuions, etc.
        public IntArrayList indices;

        public int label;

        private double maxValue = -Double.MAX_VALUE;

        private double minValue = Double.MAX_VALUE;

        private int maxIndex = 0;

        private int minIndex = 0;

        private SpatialLoc3D worldCentroid;

        public Cluster(int label) {
            indices = new IntArrayList();
            this.label = label;
        }

        private void addIndex(int idx) {
            indices.add(idx);
            double val = data.value(idx);


            if (val > maxValue) {
                maxValue = val;
                maxIndex = idx;
            }
            if (val < minValue) {
                minValue = val;
                minIndex = idx;
            }

        }

        public int getSize() {
            return indices.size();
        }

        public int[] getIndices() {
            return indices.elements().clone();
        }

        public double getArea() {
            return indices.size() * labels.getImageSpace().getSpacing().product().doubleValue();
        }

        public SpatialLoc3D getWorldCentroid() {
            if (worldCentroid != null) return worldCentroid;

            double x = 0;
            double y = 0;
            double z = 0;
            for (int i = 0; i < indices.size(); i++) {
                Index3D index = labels.getImageSpace().indexToGrid(indices.get(i));

                x = index.i1() + x;
                y = index.i2() + y;
                z = index.i3() + z;
            }

            float[] centroid = labels.getImageSpace().indexToWorld((int)(x/indices.size()), (int)(y/indices.size()), (int)(z/indices.size()));
            worldCentroid = new SpatialLoc3D(labels.getImageSpace().getMapping().getWorldAnatomy(),centroid[0], centroid[1], centroid[2]);
            return worldCentroid;
        }

        public double getExtremeValue() {
            if (Math.abs(getMaxValue()) >= Math.abs(getMinValue())) {
                return getMaxValue();
            } else {
                return getMinValue();
            }
        }

        public Index3D getExtremeVoxel() {
            if (Math.abs(getMaxValue()) >= Math.abs(getMinValue())) {
                return getMaxVoxel();
            } else {
                return getMinVoxel();
            }

        }

        public Index3D getMaxVoxel() {

            Index3D vox = data.getImageSpace().indexToGrid(maxIndex);
        
            return data.getImageSpace().indexToGrid(maxIndex);
        }

        public Index3D getMinVoxel() {
            return data.getImageSpace().indexToGrid(minIndex);
        }

        public double getMaxValue() {
            return maxValue;
        }

        public double getMinValue() {
            return minValue;
        }

        @Override
        public int compareTo(Cluster o) {
            if (o == this) return 0;
            if (this.getSize() < o.getSize()) return 1;
            if (this.getSize() > o.getSize()) return -1;

            return 0;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cluster cluster = (Cluster) o;

            if (label != cluster.label) return false;
            if (indices != null ? !indices.equals(cluster.indices) : cluster.indices != null) return false;

            return true;
        }


        @Override
        public int hashCode() {
            int result = indices != null ? indices.hashCode() : 0;
            result = 31 * result + label;
            return result;
        }

        @Override
        public String toString() {
            return "Cluster{" +
                    ", label=" + label +
                    ", max file=" + maxValue +
                    ", size=" + getSize() +
                    '}';
        }


    }


    public Collection<Cluster> getClusters() {
        return clusters.values();
    }

    public SortedSet<Cluster> getSortedClustersBySize() {
        return new TreeSet<Cluster>(clusters.values());
    }


}
