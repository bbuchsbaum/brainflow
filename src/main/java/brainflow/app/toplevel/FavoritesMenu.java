package brainflow.app.toplevel;

import com.pietschy.command.group.CommandGroup;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.face.Face;
import brainflow.app.services.DataSourceStatusEvent;
import brainflow.core.BrainFlowException;
import brainflow.image.io.IImageDataSource;

import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.DateFormat;
import java.text.ParseException;
import java.net.URI;
import java.net.URISyntaxException;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 3, 2008
 * Time: 7:58:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FavoritesMenu {

    private static Preferences userPrefs = Preferences.userNodeForPackage(FavoritesMenu.class);

    private SortedMap<String, Favorite> favMap = new TreeMap<String, Favorite>();

    private CommandGroup commandGroup;

    public FavoritesMenu() {

        EventBus.subscribeStrongly(DataSourceStatusEvent.class, new EventSubscriber() {
            public void onEvent(Object event) {
                DataSourceStatusEvent e = (DataSourceStatusEvent) event;
                if (e.getEventID() == DataSourceStatusEvent.EventID.IMAGE_REGISTERED) {
                    IImageDataSource dsource = e.getLoadableImage();
                    String uri = dsource.getImageInfo().getHeaderFile().getName().getURI();
                    Favorite fav = favMap.get(uri);
                    if (fav != null) {
                        fav.increment();
                    } else {
                        favMap.put(uri, new Favorite(uri));
                    }

                    updatePrefs();
                    updateMenu();

                }
            }
        });


        initFavorites();
        createGroup();

    }

    public CommandGroup getCommandGroup() {
        return commandGroup;
    }

    private List<Favorite> sortFavorites() {
        Favorite[] favarray = new Favorite[favMap.size()];
        favMap.values().toArray(favarray);
        List<Favorite> sortedFavs = Arrays.asList(favarray);
        Collections.sort(sortedFavs);
        return sortedFavs;

    }

    private void createGroup() {
        commandGroup = new CommandGroup();
        commandGroup.getDefaultFace(true).setText("Favorites");

        GroupBuilder builder = commandGroup.getBuilder();


        List<Favorite> sortedFavs = sortFavorites();
        Iterator<Favorite> iter = sortedFavs.iterator();

        while (iter.hasNext()) {

            Favorite fav = iter.next();
            String uri = fav.uri;
            int lastIndex = uri.lastIndexOf("/");
            String name = uri.substring(lastIndex+1, uri.length());
            ActionCommand command = new FavoriteCommand(name, uri);
            //ActionCommand command = new FavoriteCommand(name, uri);


            builder.add(command);
        }

        builder.applyChanges();


    }

    private void initFavorites() {
        String def = System.getProperty("user.dir");


        for (int i = 0; i < 10; i++) {
            String curstr = userPrefs.get("favorite-" + (i + 1), def);
            if (curstr != null) {
                try {
                    Favorite fav = Favorite.createFavorite(curstr);
                    favMap.put(fav.uri, fav);

                    //updateMenu();
                    //updatePrefs();


                } catch (ParseException e) {
                    System.out.println("could not load favorite " + "favorite-" + (i + 1));
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
                }
            }


        }

    }

    private void updatePrefs() {
        Iterator<String> iter = favMap.keySet().iterator();
        int i = 0;

        while (iter.hasNext()) {
            String key = iter.next();
            Favorite fav = favMap.get(key);
            if (key != null) {
                userPrefs.put("favorite-" + (i + 1), fav.toString());
                i++;
            }
        }

        try {
            userPrefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Failure updating backing store for user preferences");
        }

    }


    private void updateMenu() {
        GroupBuilder builder = commandGroup.getBuilder();
        builder.clear();

        List<Favorite> sortedFavs = sortFavorites();
        Iterator<Favorite> iter = sortedFavs.iterator();


        while (iter.hasNext()) {
            Favorite fav = iter.next();
            String uri = fav.uri;
            int lastIndex = uri.lastIndexOf("/");
            String name = uri.substring(lastIndex+1, uri.length());
            ActionCommand command = new FavoriteCommand(name + " " + fav.timesAccessed, uri);

            builder.add(command);
        }

        builder.applyChanges();

    }


    public static void main(String[] args) {
        Date date = new Date();
        System.out.println("date is " + DateFormat.getDateInstance().format(date));
        try {
            Date recon = DateFormat.getDateInstance().parse(DateFormat.getDateInstance().format(date));
            System.out.println("reconstitited dat is " + recon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Favorite fav = new Favorite("File32", new Date(), 33);
        System.out.println("favorite : " + fav);

        try {
            Favorite fav2 = Favorite.createFavorite(fav.toString());
            System.out.println("reconstituted favorite " + fav2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class FavoriteCommand extends ActionCommand {

        String URI;

        String name;

        FavoriteCommand(String name, String URI) {
            super();
            this.URI = URI;
            this.name = name;
            getFace(Face.MENU, true).setText(name);

        }

        public String getURI() {
            return URI;
        }

        public String getName() {
            return name;
        }

        protected void handleExecute() {
            System.out.println("want to load " + URI);
            System.out.println("name is " + name);

            try {
                IImageDataSource dsource = BrainFlow.get().createDataSource(URI);
                BrainFlow.get().loadAndDisplay(dsource);
            } catch(BrainFlowException e) {
                throw new RuntimeException(e);
            } 


        }
    }


    static class Favorite implements Comparable {

        String uri;

        Date lastAccessed;

        Integer timesAccessed;

        public Favorite(String uri) {
            this.uri = uri;
            lastAccessed = new Date();
            timesAccessed = 1;
        }

        public Favorite(String _uri, Date _lastAccessed, int _timesAccessed) {
            uri = _uri;
            lastAccessed = _lastAccessed;
            timesAccessed = _timesAccessed;
        }


        public static Favorite createFavorite(String str) throws ParseException {
            String[] ret = str.split("::");
            if (ret.length != 3) {
                throw new ParseException(String.format("could not parse entry %s", str), 0);
            }

            String uri = ret[0].trim();
            Date date = DateFormat.getDateInstance().parse(ret[1].trim());
            int times = Integer.parseInt(ret[2].trim());

            return new Favorite(uri, date, times);

        }

        public void increment() {
            timesAccessed++;
            lastAccessed = new Date();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Favorite favorite = (Favorite) o;

            if (uri != null ? !uri.equals(favorite.uri) : favorite.uri != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return uri != null ? uri.hashCode() : 0;
        }

        public int compareTo(Object o) {
            Favorite fav = (Favorite) o;
            if (fav.timesAccessed > timesAccessed) return 1;
            if (fav.timesAccessed < timesAccessed) return -1;
            return 0;
        }

        public String toString() {
            return uri + " :: " + DateFormat.getDateInstance().format(lastAccessed) + " :: " + timesAccessed;
        }
    }
}
