package brainflow.app.toplevel;

import brainflow.image.io.BrainIO;
import brainflow.image.io.IImageDataSource;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 5, 2010
 * Time: 8:56:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainFlowCommandLine {

    @Argument
    List<String> fileNames;

    private void loadDataInBackground(List<String> fileNames) {
        File[] files = new File[fileNames.size()];
        for (int i = 0; i < fileNames.size(); i++) {
            files[i] = new File(fileNames.get(i));
        }

        List<IImageDataSource> dsources = BrainIO.loadDataSources(files);
        for (final IImageDataSource dsource : dsources) {
            ImageLoader loader = new ImageLoader(dsource) {
                @Override
                protected void done() {
                    System.out.println("finished loading " + dsource.getHeaderFile().getName());
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("adding to display " + dsource.getHeaderFile().getName());
                            BrainFlow.get().loadAndDisplay(dsource);
                        }
                    });
                }
            };

            loader.execute();

           
        }
    }


    public void launch() {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    System.out.println("launching brainflow");
                    BrainFlow.get().launch();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });

        if (fileNames.size() > 0) {
            System.out.println("loading command line images");
            loadDataInBackground(fileNames);
        }


    }

    public static void main(String[] args) {

        BrainFlowCommandLine cmd = new BrainFlowCommandLine();
        CmdLineParser parser = new CmdLineParser(cmd);


        try {
            parser.parseArgument(args);
            cmd.launch();

            System.out.println(cmd.fileNames);

        } catch (
                CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }
}
