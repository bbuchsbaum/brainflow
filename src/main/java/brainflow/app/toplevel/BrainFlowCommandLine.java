package brainflow.app.toplevel;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.List;

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

    public static void main(String[] args) {

        BrainFlowCommandLine cmd = new BrainFlowCommandLine();

        CmdLineParser parser = new CmdLineParser(cmd);
        try {
            parser.parseArgument(args);


            System.out.println(cmd.fileNames);

        } catch (
                CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }
}
