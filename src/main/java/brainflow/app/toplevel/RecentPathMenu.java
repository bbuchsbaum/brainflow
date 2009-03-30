package brainflow.app.toplevel;

import brainflow.app.FileSystemEvent;
import brainflow.app.FileSystemEventListener;

import brainflow.app.actions.MountDirectoryCommand;
import com.pietschy.command.group.CommandGroup;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.ActionCommand;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 30, 2006
 * Time: 12:28:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecentPathMenu {


    private RollingList recentDirectories = new RollingList(6);

    private static Preferences userPrefs = Preferences.userNodeForPackage(RecentPathMenu.class);


    private CommandGroup commandGroup;

    public RecentPathMenu() {
        DirectoryManager.getInstance().addFileSystemEventListener(new FileSystemEventListener() {
            public void eventOccurred(FileSystemEvent e) {
                String path = e.getFileObject().getName().getURI();
                if (recentDirectories.contains(path)) {
                    return;
                }

                recentDirectories.addItem(path);

                updateMenu();
                updatePrefs();


            }
        });

        initRecentlyMounted();
        createGroup();
    }

    //public JMenu getMenu() {
    //    return directoryMenu;
    //}

    public CommandGroup getCommandGroup() {
        return commandGroup;
    }

    private void createGroup() {
        commandGroup = new CommandGroup();
        commandGroup.getDefaultFace(true).setText("Recently Mounted");
        
        GroupBuilder builder = commandGroup.getBuilder();
        Iterator<String> iter = recentDirectories.iterator();
        while (iter.hasNext()) {
            ActionCommand command = new MountDirectoryCommand(iter.next());
            builder.add(command);
        }

        builder.applyChanges();
        

    }

    private void updateMenu() {
        GroupBuilder builder = commandGroup.getBuilder();
        builder.clear();
        Iterator<String> iter = recentDirectories.iterator();
        while (iter.hasNext()) {
            ActionCommand command = new MountDirectoryCommand(iter.next());
            builder.add(command);
        }

        builder.applyChanges();

    }


    private void initRecentlyMounted() {
        String def = System.getProperty("user.dir");


        String last = null;
        for (int i = 0; i < 6; i++) {
            String curstr = userPrefs.get("recent-dir-" + (i + 1), def);
            if (!curstr.equals(last)) {
                recentDirectories.addItem(curstr);
            }

            last = curstr;
        }


    }


    private void updatePrefs() {
        Iterator<String> iter = recentDirectories.iterator();
        int i = 0;

        while (iter.hasNext()) {
            String dirname = iter.next();
            userPrefs.put("recent-dir-" + (i + 1), dirname);
            i++;
        }

        try {
            userPrefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Failure updating backing store for user preferences");
        }

    }

    public List<String> getRecentPaths() {
        return recentDirectories.getItems();
    }

    class RollingList {

        private List<String> roll = new LinkedList<String>();

        private int size;

        public RollingList(int _size) {
            size = _size;
        }

        public boolean contains(String path) {
            return roll.contains(path);
        }

        public List<String> getItems() {
            return Collections.unmodifiableList(roll);
        }

        public Iterator<String> iterator() {
            return roll.iterator();
        }

        public void addItem(String item) {
            if (roll.contains(item)) {
                return;
            }
            if (roll.size() == size) {
                roll.remove(0);
            }
            roll.add(item);

        }

    }


}
