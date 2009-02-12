package brainflow.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

// ----------------------------------------------------------------------------
/**
 * A static API that can be used as a drop-in replacement for
 * java.lang.ClassLoader API (the class/resource loading part). This
 * implementation is merely a wrapper around ClassLoaderResolverget.ClassLoader()
 * method.
 *
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public
abstract class ResourceLoader {
    // public: ................................................................

    /**
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    public static Class loadClass(final String name)
            throws ClassNotFoundException {
        final ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

        return Class.forName(name, false, loader);
    }

    /**
     * @see java.lang.ClassLoader#getResource(java.lang.String)
     */
    public static URL getResource(final String name) {
        final ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

        if (loader != null)
            return loader.getResource(name);
        else
            return ClassLoader.getSystemResource(name);
    }

    /**
     * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
     */
    public static InputStream getResourceAsStream(final String name) {
        final ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

        if (loader != null)
            return loader.getResourceAsStream(name);
        else
            return ClassLoader.getSystemResourceAsStream(name);
    }

    /**
     * @see java.lang.ClassLoader#getResources(java.lang.String)
     */
    public static Enumeration getResources(final String name)
            throws IOException {
        final ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

        if (loader != null)
            return loader.getResources(name);
        else
            return ClassLoader.getSystemResources(name);
    }

    // protected: .............................................................

    // package: ...............................................................

    // private: ...............................................................


    private ResourceLoader() {
    } // prevent subclassing

} // end of class
// ----------------------------------------------------------------------------