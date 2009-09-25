package org.boxwood.array;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 3, 2009
 * Time: 9:08:21 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class IndexSlice implements Iterable<IndexSlice.Slice> {

  
    protected List<Slice> slices = new ArrayList<Slice>();


    public int dim() {
        return slices.size();
    }

    public Slice slice(int i) {
        return slices.get(i);
    }

    public abstract IndexSlice add(int from, int to);

    public abstract IndexSlice along(int dim);

    @Override
    public Iterator<Slice> iterator() {
        return slices.iterator();
    }

    public static void main(String[] args) {
        IndexSlice3D slice = SliceBuilder.add(0,22).add(0,44).add(33,44);
               
    }




    public static class SliceBuilder {

        public static IndexSlice1D add(int from, int to) {
            return new IndexSlice1D(from, to);
        }

    }

    public static class IndexSlice1D extends IndexSlice {

        public IndexSlice1D(int from, int to) {
            slices.add(new Slice(from, to));
        }

        public IndexSlice1D(Slice slice) {
            slices.add(slice);
        }

        public Slice one() {
            return slices.get(0);
        }

        public IndexSlice2D add(int from, int to) {
            return new IndexSlice2D(slices.get(0), new Slice(from, to));
        }

        public IndexSlice2D along(int dim) {
            return new IndexSlice2D(slices.get(0), new Slice(0,dim-1));
        }
    }

    public static class IndexSlice2D extends IndexSlice1D {

        public IndexSlice2D(Slice one, Slice two) {
            super(one);
            slices.add(two);
        }

        public Slice two() {
            return slices.get(1);
        }

        public IndexSlice3D add(int from, int to) {
            return new IndexSlice3D(slices.get(0), slices.get(1), new Slice(from, to));
        }

        public IndexSlice3D along(int dim) {
            return new IndexSlice3D(slices.get(0), slices.get(1), new Slice(0,dim-1));
        }

    }

    public static class IndexSlice3D extends IndexSlice2D {

        public IndexSlice3D(Slice one, Slice two, Slice three) {
            super(one, two);
            slices.add(three);

        }

        public IndexSlice4D add(int from, int to) {
            return new IndexSlice4D(slices.get(0), slices.get(1), slices.get(2), new Slice(from, to));
        }

        public IndexSlice4D along(int dim) {
            return new IndexSlice4D(slices.get(0), slices.get(1), slices.get(2), new Slice(0,dim-1));
        }

        public Slice three() {
            return slices.get(2);
        }


    }

    public static class IndexSlice4D extends IndexSlice3D {

        public IndexSlice4D(Slice one, Slice two, Slice three, Slice four) {
            super(one,two,three);
            slices.add(four);
        }

        public Slice four() {
            return slices.get(3);
        }


    }



    public static class Slice {

        private int from;

        private int to;

        public Slice(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public int from() {
            return from;                                                                                                           }

        public int to() {
            return to;
        }
    }



}
