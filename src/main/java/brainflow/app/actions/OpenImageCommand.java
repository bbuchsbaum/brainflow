package brainflow.app.actions;

import brainflow.image.io.BrainIO;
import brainflow.image.io.IImageSource;
import brainflow.app.toplevel.BrainFlow;
import com.pietschy.command.file.AbstractFileOpenCommand;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 20, 2007
 * Time: 9:14:06 PM
 */
public class OpenImageCommand extends AbstractFileOpenCommand {

    public OpenImageCommand() {
        super("open-image");
    }

    public OpenImageCommand(FileFilter... fileFilters) {
        super("open-image", fileFilters);
    }

    protected void performOpen(File[] files) {
        List<IImageSource> dsources = BrainIO.loadDataSources(files);
        for (IImageSource ds : dsources) {
            BrainFlow.get().loadAndDisplay(ds);
            //DataSourceManager.get().register(dsource[i]);
        }

    }
}
