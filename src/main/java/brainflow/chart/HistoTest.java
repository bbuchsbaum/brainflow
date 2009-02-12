package brainflow.chart;

import brainflow.image.Histogram;
import brainflow.image.io.IImageDataSource;
import brainflow.display.ColoredHistogram;
import test.TestUtils;
import brainflow.colormap.LinearColorMap2;
import brainflow.colormap.ColorTable;

import javax.swing.*;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 8, 2008
 * Time: 6:22:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistoTest extends JPanel {

    Histogram histogram;

    public HistoTest(Histogram histo) {
        histogram = histo;
        buildDataSet();
    }

    private void buildDataSet() {


        //HistogramDatasetX dataset = new HistogramDatasetX(histogram);
        ColoredHistogram chist = new ColoredHistogram(histogram);

        LinearColorMap2 lmap = new LinearColorMap2(histogram.getMinValue(), histogram.getMaxValue(), ColorTable.SPECTRUM);
        lmap = lmap.newClipRange(0,4000);
        chist.setColorModel(lmap);
        add(chist, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        IImageDataSource dataSource = TestUtils.quickDataSource("resources/data/global_mean+orig.HEAD");
        Histogram histo = new Histogram(dataSource.getData(),256);

        JFrame jf = new JFrame();


        jf.add(new HistoTest(histo));
        jf.pack();
        jf.setVisible(true);
    }
}
