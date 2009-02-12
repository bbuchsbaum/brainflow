package brainflow.application.actions;

import brainflow.image.io.IImageDataSource;
import brainflow.application.toplevel.ImageIOManager;
import brainflow.application.toplevel.BrainFlow;
import com.pietschy.command.file.AbstractFileOpenCommand;

import javax.swing.filechooser.FileFilter;
import java.io.File;

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
        IImageDataSource[] dsource = ImageIOManager.getInstance().findLoadableImages(files);
        for (int i=0; i<dsource.length; i++) {
            BrainFlow.get().loadAndDisplay(dsource[i]);
            //DataSourceManager.getInstance().register(dsource[i]);
        }

    }
}
