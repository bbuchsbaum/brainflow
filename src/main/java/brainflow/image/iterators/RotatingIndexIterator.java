package brainflow.image.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 12, 2008
 * Time: 9:27:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class RotatingIndexIterator implements IndexIterator {

    private int end;

    protected int length;


    protected int count = -1;



    public RotatingIndexIterator(int length, int total) {
        if (total < length) {
            throw new IllegalArgumentException("total must exceed length");
        }
        this.length = length;
        end = total-1;

    }


    public boolean hasNext() {

        if (count < end) return true;

        return false;
    }

    public int get() {
        return count % length;
    }

    public int getRotation() {
        return count/length;
    }

    public int next() {
        count++;
        return get();

    }


   
    public static void main(String[] args) {
        RotatingIndexIterator iter = new RotatingIndexIterator(3, 9);
        while (iter.hasNext()) {
            System.out.println("i : " + iter.next());
        }

    }
}
