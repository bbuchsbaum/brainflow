package brainflow.image.operations;

import cern.colt.list.IntArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 16, 2009
 * Time: 5:56:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnionFindArray {

    private IntArrayList P;

    public UnionFindArray(int size) {
        P = new IntArrayList(new int[size]);
        
    }

    public void add(int label) {
        P.add(label);
    }

    public void set(int i, int label) {
        P.setQuick(i, label);
    }
    
    public void add(int[] labels) {
        for (int i=0; i<labels.length; i++) {
            P.add(labels[i]);
        }
    }





    public int findRoot(int i) {
        int root = i;
        while (P.getQuick(root) < root) {
            root = P.getQuick(root);
        }

        return root;
    }

    public void setRoot(int i, int root) {
        int j;

        while (P.getQuick(i) < i) {
            j = P.getQuick(i);
            P.setQuick(i, root);
            i = j;
        }

        P.setQuick(i, root);
    }

    public int find(int i) {

        int root = findRoot(i);
        setRoot(i, root);
        return root;
    }

    public int union(int i, int j) {
        int root = findRoot(i);
        int rootj;
        if (i != j) {
            rootj = findRoot(j);
            if (root > rootj) {
                root = rootj;
            }
            setRoot(j,root);
        }

        setRoot(i,root);

        return root;
    }

    public void flattenL() {
        int k=1;
        for (int i= 1; i<P.size(); i++) {
            if (P.getQuick(i) < i ) {
                P.setQuick(i, P.getQuick(i));
            } else {
                P.setQuick(i,k);
                k = k + 1;
            }
        }
    }

    @Override
    public String toString() {
        return "UnionFindArray{" +
                "P=" + P +
                '}';
    }

    public static void main(String[] args) {
        UnionFindArray ufa = new UnionFindArray(5);
        ufa.add(new int[] { 0, 1, 1, 3, 3,  6, 6, 6, 6 });
        int f = ufa.findRoot(6);
        System.out.println("" + f);
        System.out.println(ufa);

    }
}
