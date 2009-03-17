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
        P = new IntArrayList(size);
        
    }

    public void add(int label) {
        P.add(label);
    }
    public void add(int[] labels) {
        for (int i=0; i<labels.length; i++) {
            P.add(labels[i]);
        }
    }

    private int p(int i) {
        return P.getQuick(i-1);
    }

    private void sp(int i, int val) {
        P.setQuick(i-1, val);
    }


    public int findRoot(int i) {
        int root = i;
        while (p(root) < root) {
            root = p(root);
        }

        return root;
    }

    public void setRoot(int i, int root) {
        int j;
        while (p(i) < i) {
            j = p(i);
            sp(i, root);
            i = j;
        }

        sp(i, root);
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
