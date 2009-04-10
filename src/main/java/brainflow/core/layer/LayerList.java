package brainflow.core.layer;

import brainflow.image.space.ICoordinateSpace;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.data.IImageData;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 8, 2009
 * Time: 7:51:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayerList<T extends AbstractLayer> implements List<T> {

        
    private List<T> layers = new ArrayList<T>();

    public LayerList(List<T> layers) {
        this.layers = layers;
    }

    public LayerList(T ... layers) {
        this.layers = Arrays.asList(layers);

    }

    @Override
    public int size() {
        return layers.size();
    }

    @Override
    public boolean isEmpty() {
        return layers.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return layers.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return layers.iterator();
    }

    @Override
    public Object[] toArray() {
        return layers.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return layers.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return layers.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return layers.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return layers.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return layers.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return layers.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return layers.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return layers.retainAll(c);
    }

    @Override
    public void clear() {
        layers.clear();
    }

    @Override
    public T get(int index) {
        return layers.get(index);
    }

    @Override
    public T set(int index, T element) {
        return layers.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        layers.add(index, element);
    }

    @Override
    public T remove(int index) {
        return layers.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return layers.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return layers.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return layers.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return layers.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return layers.subList(fromIndex, toIndex);
    }

    public ICoordinateSpace getSpace() {
        if (layers.isEmpty()) throw new IllegalStateException("an empty list does not have a coordinate space");
        return layers.get(0).getCoordinateSpace();
    }

    




}
