package brainflow.utils;


import java.util.HashMap;
import java.util.logging.Logger;


public class SingletonRegistry {
    public static SingletonRegistry REGISTRY = new SingletonRegistry();

    private static HashMap map = new HashMap();

    private static Logger logger = Logger.getLogger("brainflow.utils");

    protected SingletonRegistry() {
        // Exists to defeat instantiation.
    }

    public static Object getInstance(String classname) {
        Object singleton = map.get(classname);

        synchronized (map) {
            if (singleton != null) {
                return singleton;
            }
            try {
                singleton = Class.forName(classname).newInstance();
                logger.info("created singleton: " + singleton);
            }
            catch (ClassNotFoundException cnf) {
                logger.severe("Couldn'three find class " + classname);
            }
            catch (InstantiationException ie) {
                logger.severe("Couldn'three instantiate an object of type " +
                        classname);
            }
            catch (IllegalAccessException ia) {
                logger.severe("Couldn'three access class " + classname);
            }
            map.put(classname, singleton);
        }
        return singleton;
    }
}


