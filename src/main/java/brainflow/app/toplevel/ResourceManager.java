package brainflow.app.toplevel;

import brainflow.colormap.ColorTable;
import brainflow.colormap.ColorBrewer;

import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Oct 8, 2003
 * Time: 11:54:10 AM
 * To change this template use Options | File Templates.
 */
public class ResourceManager {

    private Logger log = Logger.getLogger(ResourceManager.class.getName());
    
    private HashMap<String, IndexColorModel> cachedMaps = new HashMap<String, IndexColorModel>();

    private static String defaultColorMap = "grayscale";

    protected ResourceManager() {
        cachedMaps.put("grayscale", ColorTable.GRAYSCALE);
        // Exists only to thwart instantiation.
    }


    public static ResourceManager getInstance() {
        return (ResourceManager) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.ResourceManager");
    }

    public IndexColorModel getDefaultColorMap() {
        return getColorMap(defaultColorMap);

    }

    public IndexColorModel getColorMap(String name) {
        IndexColorModel icm = cachedMaps.get(name);
        if (icm != null) return icm;

        throw new IllegalArgumentException("Color Model " + name + " not found!");
    }

    public Map<String, IndexColorModel> getColorMaps() {
        try {
            if (cachedMaps.isEmpty()) {
                loadColorMaps();
            }

            return Collections.unmodifiableMap(cachedMaps);

        } catch (IOException e) {
            log.severe("Could not load property file for color map preloading, aborting");
            throw new RuntimeException(e);
        }

    }

    //public void registerColorMap(IntervalColorModel icm) {

    //}

    private void loadColorMaps() throws IOException {

        cachedMaps.put("YellowRed", ColorBrewer.YellowRed);
        cachedMaps.put("YellowGreenBlue", ColorBrewer.YellowGreenBlue);
        cachedMaps.put("RedYellowBlue", ColorBrewer.RedYellowBlue);
        cachedMaps.put("Purples", ColorBrewer.Purples);
        cachedMaps.put("PurpleBlue", ColorBrewer.PurpleBlue);
        cachedMaps.put("Reds", ColorBrewer.Reds);
        cachedMaps.put("Greens", ColorBrewer.Greens);
        cachedMaps.put("BrownBlueGreen", ColorBrewer.BrownBlueGreen);

        InputStream istream = getClass().getClassLoader().getResourceAsStream("colormaps/colormap.properties");


        Properties props = new Properties();
        props.load(istream);

        Enumeration enumer = props.propertyNames();

        while (enumer.hasMoreElements()) {
            String name = (String) enumer.nextElement();
            String location = props.getProperty(name);
            try {

                URL url = getClass().getClassLoader().getResource(location);

                IndexColorModel icm = ColorTable.createFromXMLInputStream(url.openStream());

                cachedMaps.put(name, icm);
            } catch (Exception bfe) {
                log.severe("Failed to load colormap: " + name + ", at: " + location);
                throw new RuntimeException("Failed to load colormap: " + name + ", at: " + location, bfe);
            }
        }


    }


    public static void main(String[] args) {

    }
}
