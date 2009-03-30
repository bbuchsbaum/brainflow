package brainflow.app;

import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.axis.AxisRange;
import brainflow.image.anatomy.AnatomicalAxis;
import com.jidesoft.introspector.BeanIntrospector;
import com.jidesoft.grid.PropertyTableModel;
import com.jidesoft.grid.PropertyTable;
import com.jidesoft.grid.PropertyPane;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 22, 2007
 * Time: 5:11:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPropertyPane extends JPanel {


    IImageSpace space;
    public TestPropertyPane() {
        try {
            init();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void init() throws Exception {
        space = new ImageSpace3D(new ImageAxis(new AxisRange(AnatomicalAxis.LEFT_RIGHT, -100, 100), 2),
                                 new ImageAxis(new AxisRange(AnatomicalAxis.POSTERIOR_ANTERIOR, -100, 100), 2),
                                 new ImageAxis(new AxisRange(AnatomicalAxis.INFERIOR_SUPERIOR, -100, 100), 2));

        BeanIntrospector spect = new BeanIntrospector(IImageSpace.class);
        PropertyTableModel model = spect.createPropertyTableModel(space);
        PropertyTable table = new PropertyTable(model);
        PropertyPane pane = new PropertyPane(table);
        add(pane);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new TestPropertyPane());
        frame.pack();
        frame.setVisible(true);
    }

    }
