package brainflow.utils;

// ----------------------------------------------------------------------------
/**
 * Information context for {@link IClassLoadStrategy#getClassLoader(ClassLoadContext)}.
 *
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public class ClassLoadContext {
    // public: ................................................................

    /**
     * Returns the class representing the caller of {@link ClassLoaderResolver}
     * API. Can be used to retrieve the caller's classloader etc (which may be
     * different from the ClassLoaderResolver's own classloader).
     */
    public final Class getCallerClass() {
        return m_caller;
    }

    // protected: .............................................................

    // package: ...............................................................

    /**
     * This constructor is package-private to restrict instantiation to
     * {@link ClassLoaderResolver} only.
     */
    ClassLoadContext(final Class caller) {
        m_caller = caller;
    }

    // private: ...............................................................


    private final Class m_caller;

} // end of class
// ----------------------------------------------------------------------------