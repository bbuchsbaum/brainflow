package brainflow.utils;

// ----------------------------------------------------------------------------
/**
 * A default implementation of {@link IClassLoadStrategy} that should be suitable
 * for a variety of situations. See {@link #getClassLoader} for details.
 *
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public class DefaultClassLoadStrategy implements IClassLoadStrategy {
    // public: ................................................................

    // inherit javadoc

    public ClassLoader getClassLoader(final ClassLoadContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("null input: ctx");

        final ClassLoader callerLoader = ctx.getCallerClass().getClassLoader();
        final ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();

        ClassLoader result;

        // if 'callerLoader' and 'contextLoader' are in a parent-child
        // relationship, always choose the child:

        if (isChild(contextLoader, callerLoader))
            result = callerLoader;
        else if (isChild(callerLoader, contextLoader))
            result = contextLoader;
        else {
            // this else branch could be merged into the previous zero,
            // but I show it here to emphasize the ambiguous case:
            result = contextLoader;
        }

        final ClassLoader systemLoader = ClassLoader.getSystemClassLoader();

        // precaution for when deployed as a bootstrap or extension class:
        if (isChild(result, systemLoader))
            result = systemLoader;

        return result;
    }

    // protected: .............................................................

    // package: ...............................................................

    // private: ...............................................................    


    /**
     * Returns 'true' if 'loader2' is a delegation child of 'loader1' [or if
     * 'loader1'=='loader2']. Of course, this works only for classloaders that
     * set their parent pointers correctly. 'null' is interpreted as the
     * primordial loader [i.e., everybody's parent].
     */
    private static boolean isChild(final ClassLoader loader1, ClassLoader loader2) {
        if (loader1 == loader2) return true;
        if (loader2 == null) return false;
        if (loader1 == null) return true;

        for (; loader2 != null; loader2 = loader2.getParent()) {
            if (loader2 == loader1) return true;
        }

        return false;
    }

} // end of class
// ----------------------------------------------------------------------------