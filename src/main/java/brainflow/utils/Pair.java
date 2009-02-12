/*
 * Pair.java
 *
 * Created on January 29, 2003, 1:46 PM
 */

package brainflow.utils;

/**
 *
 * @author  Bradley
 */

public class Pair<T, E> {
    
    
    private T left;
    
    private E right;
    
    
    public Pair( T left, E right ) {
        this.left = left;
        this.right = right;
    }
    
    
    public T left() {
        return left;
    }
    
    
    public E right() {
        return right;
    }
    
    /// Equality test.
    public boolean equals( Object o ) {
        if ( o != null && o instanceof Pair ) {
            Pair p = (Pair) o;
            return this.left.equals(p.left()) && this.right.equals(p.right());
        }
        return false;
    }
    
    /// Compute a hash code for the pair.
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }
    
    
    
}





