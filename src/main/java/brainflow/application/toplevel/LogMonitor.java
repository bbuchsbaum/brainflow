package brainflow.application.toplevel;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 21, 2007
 * Time: 7:25:11 AM
 */
public class LogMonitor extends StreamHandler {

    private JTextArea output = new JTextArea();

    private LogStream outStream;

    public LogMonitor() {
        output.setColumns(110);
        outStream = new LogStream();
        setOutputStream(outStream);

    }

    @Override
    public synchronized void publish(LogRecord record) {
        super.publish(record);
        flush();
        output.setCaretPosition(output.getDocument().getLength() - 1);

    }

    public JComponent getComponent() {
        return output;
    }


    class LogStream extends OutputStream {


        public void write(int b) throws IOException {
            output.append("" + (char) b);
        }

        public void write(byte b[], int off, int len) throws IOException {
            output.append(new String(b, off, len));
        }

        public void write(byte b[]) throws IOException {
            output.append(new String(b));
        }
    }


}
