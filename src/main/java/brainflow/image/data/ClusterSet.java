package brainflow.image.data;

import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.IImageSpace3D;
import cern.colt.list.IntArrayList;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 12, 2010
 * Time: 9:54:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterSet {


    private HashMap<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();

    private IImageData3D labels;

    public ClusterSet(IImageData3D labels) {
        this.labels = labels;
        build();
    }


    public int size() {
        return clusters.size();
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

    public class Cluster implements Comparable<Cluster> {

        //VoxelSet should be a set of voxels (which can be mapped to coordinates (CoordinateSet)
        // and can be searched, and have a bounding box, and distance functuions, etc.
        public IntArrayList indices;

        public int label;

        public Cluster(int label) {
           indices = new IntArrayList();
           this.label = label;
        }

        private void addIndex(int idx) {
            indices.add(idx);
        }

        public int getSize() {
            return indices.size();
        }

        public double getArea() {
           return indices.size() * labels.getImageSpace().getSpacing().product().doubleValue();
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
