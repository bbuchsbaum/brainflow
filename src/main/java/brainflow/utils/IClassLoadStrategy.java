
package brainflow.utils;

// ----------------------------------------------------------------------------
/**
 * The interface implemented by any classloader selection Strategy used
 * with {@link ClassLoaderResolver} API.
 * 
 * @see DefaultClassLoadStrategy
 * 
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public
interface IClassLoadStrategy
{
    // public: ................................................................
    
    /**
     * Selects a classloader based on a given createSource context.
     * 
     * @see ClassLoaderResolver#getClassLoader()
     */
    ClassLoader getClassLoader (ClassLoadContext ctx);

} // end of interface
// ----------------------------------------------------------------------------