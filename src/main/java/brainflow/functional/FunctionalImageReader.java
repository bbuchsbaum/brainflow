package brainflow.functional;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 10, 2004
 * Time: 3:21:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FunctionalImageReader {

    public double[] readSeries(int i, int j, int k) throws IOException;
    public double[] readSeries(int idx) throws IOException;
    public double[] readSeries(int i, int j, int k, int tbegin, int tend) throws IOException;
    public double[][] readMultipleSeries(int[] indices) throws IOException;
    public double[][] readMultipleSeries(int[] indices, int tbegin, int tend) throws IOException;
    public double[][] readMultipleSeries(int ibegin, int iend, int jbegin, int jend, int kbegin, int kend);
    public double readValue(int idx, int t) throws IOException;
    public double readValue(int i, int j, int k, int t) throws IOException;




}
