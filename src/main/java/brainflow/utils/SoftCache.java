package brainflow.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;
import java.lang.ref.SoftReference;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 2, 2008
 * Time: 4:49:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SoftCache<K, V> {

    private Map<K, SoftReference<V>> cache;

    private int maxElements = 50;

    public SoftCache() {
        cache =  new LinkedHashMap<K, SoftReference<V>>() {
            protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {
               return (size() > maxElements);

            }
        };

        cache = Collections.synchronizedMap(cache);
    }

    public void put(K key, V value) {
        SoftReference<V> ref = new SoftReference<V>(value);

        cache.put(key, ref);

    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public V get(K key) {
        SoftReference<V> ref = cache.get(key);
        if (ref == null) { return null; }


        V ret = ref.get();

        if (ret == null) {
            cache.remove(key);
            return null;
        }

    
        return ref.get();


    }



}
