package brainflow.utils;

import javax.swing.event.EventListenerList;
import java.util.EventListener;
import java.lang.reflect.Array;
import java.lang.ref.WeakReference;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 26, 2008
 * Time: 12:10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeakEventListenerList extends EventListenerList {

    private int listenerSize;

    /**
     *
     */
    public synchronized Object[] getListenerList() {
        int tgtInd = 0;
        Object[] ret = new Object[listenerSize];
        for (int i = 1; i < listenerSize; i += 2) {
            Object l = ((WeakReference) listenerList[i]).get();
            if (l != null) {
                ret[tgtInd++] = listenerList[i - 1];
                ret[tgtInd++] = l;
            } else { // listener was garbage collected
                /* Remove the listener and its class. This could be done
                * in a more efficient but much less readable way batching
                * the successive removes into one.
                */

                System.arraycopy(listenerList, i + 1, listenerList, i - 1,
                        listenerSize - i - 1);
                listenerSize -= 2;
                i -= 2;
            }
        }

        if (ret.length != tgtInd) {
            Object[] tmp = new Object[tgtInd];
            System.arraycopy(ret, 0, tmp, 0, tgtInd);
            ret = tmp;
        }

        return ret;
    }

    public synchronized <T extends EventListener> T[] getListeners(Class<T> t) {
        int tgtInd = 0;
        T[] ret = (T[]) Array.newInstance(t, listenerSize);
        for (int i = 0; i < listenerSize; i++) {
            if (listenerList[i++] == t) {
                T l = (T) ((WeakReference) listenerList[i]).get();
                if (l != null) {
                    ret[tgtInd++] = l;
                } else { // listener was garbage collected
                    /* Remove the listener and its class. This could be done
                    * in a more efficient but much less readable way batching
                    * the successive removes into one.
                    */
                    System.arraycopy(listenerList, i + 1, listenerList, i - 1,
                            listenerSize - i - 1);
                    listenerSize -= 2;
                    i -= 2;
                }
            }
        }

        if (ret.length != tgtInd) {
            T[] tmp = (T[]) Array.newInstance(t, tgtInd);
            System.arraycopy(ret, 0, tmp, 0, tgtInd);
            ret = tmp;
        }

        return ret;
    }


    /**
     * Adds the listener as a listener of the specified type.
     *
     * @param t the type of the listener to be added
     * @param l the listener to be added
     */
    public synchronized void add(Class t, EventListener l) {
        if (l == null ) {
            // In an ideal world, we would do an assertion here
            // to help developers know they are probably doing
            // something wrong
            return;
        }

        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener " + l + // NOI18N
                    " is not of type " + t); // NOI18N
        }

        if (listenerSize == 0) {
            listenerList = new Object[]{t, new WeakReference(l)};
            listenerSize = 2;
        } else {
            if (listenerSize == listenerList.length) { // reallocate
                Object[] tmp = new Object[listenerSize * 2];
                System.arraycopy(listenerList, 0, tmp, 0, listenerSize);
                listenerList = tmp;
            }

            listenerList[listenerSize++] = t;
            listenerList[listenerSize++] = new WeakReference(l);
        }
    }

    /**
     * Removes the listener as a listener of the specified type.
     *
     * @param t the type of the listener to be removed
     * @param l the listener to be removed
     */
    public synchronized void remove(Class t, EventListener l) {
        if (l == null) {
            // In an ideal world, we would do an assertion here
            // to help developers know they are probably doing
            // something wrong
            return;
        }

        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener " + l + // NOI18N
                    " is not of type " + t); // NOI18N
        }

        // Is l on the list?
        int index = -1;
        for (int i = listenerSize - 2; i >= 0; i -= 2) {
            if ((listenerList[i] == t)
                    && l.equals(((WeakReference) listenerList[i + 1]).get())
                    ) {
                index = i;
                break;
            }
        }

        // If so,  remove it
        if (index >= 0) {
            System.arraycopy(listenerList, index + 2, listenerList, index,
                    listenerSize - index - 2);
            listenerSize -= 2;
        }
    }

    public synchronized boolean contains(Class t, EventListener l) {
        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener " + l + // NOI18N
                    " is not of type " + t); // NOI18N
        }

        // Is l on the list?
        int index = -1;
        for (int i = listenerSize - 2; i >= 0; i -= 2) {
            if ((listenerList[i] == t) && l.equals(((WeakReference) listenerList[i + 1]).get())) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            return true;
        } else {
            return false;
        }




    }

    private synchronized void writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();

        // Save the non-null event listeners:
        for (int i = 0; i < listenerSize; i += 2) {
            Class t = (Class) listenerList[i];
            EventListener l = (EventListener) ((WeakReference) listenerList[i + 1]).get();
            if ((l != null) && (l instanceof Serializable)) {
                os.writeObject(t.getName());
                os.writeObject(l);
            }
        }

        os.writeObject(null);
    }

    private void readObject(ObjectInputStream is)
            throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        Object listenerTypeOrNull;

        while (null != (listenerTypeOrNull = is.readObject())) {
            EventListener l = (EventListener) is.readObject();
            add(Class.forName((String) listenerTypeOrNull), l);
        }
    }

    public synchronized String toString() {
        StringBuffer sb = new StringBuffer("WeakEventListenerList: "); // NOI18N
        sb.append(listenerSize / 2);
        sb.append(" listeners:\n"); // NOI18N
        for (int i = 0; i < listenerSize; i += 2) {
            sb.append(" type " + ((Class) listenerList[i]).getName()); // NOI18N
            sb.append(" listener " + ((WeakReference) listenerList[i + 1]).get()); // NOI18N
        }
        return sb.toString();
    }

}

