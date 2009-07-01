package brainflow.app.actions;

import brainflow.core.ImageView;
import brainflow.app.toplevel.ImageViewFactory;
import brainflow.app.toplevel.DisplayManager;
import brainflow.app.toplevel.BrainFlow;
import brainflow.core.IBrainCanvas;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.ImageAxis;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateMontageViewCommand extends BrainFlowCommand {


    public CreateMontageViewCommand() {
        super("create-montage");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {
            InputPanel ip = new InputPanel(view);
            IBrainCanvas canvas = getSelectedCanvas();

            JOptionPane.showMessageDialog(canvas.getComponent(), ip);
            ImageView sview = ImageViewFactory.createMontageView(view.getModel(), ip.getRows(), ip.getColumns(), (float) ip.getSliceGap());

            BrainFlow.get().displayView(sview);

        }

    }


    class InputPanel extends JPanel {

        FormLayout layout;

        JSpinner rowSpinner;
        JSpinner colSpinner;
        JSpinner gapSpinner;

        public InputPanel(ImageView view) {

            layout = new FormLayout("6dlu, l:p, 4dlu, 1dlu, l:45dlu, 6dlu", "8dlu, p, 6dlu, p, 6dlu, p, 8dlu");
            layout.addGroupedColumn(2);
            layout.addGroupedColumn(4);
            CellConstraints cc = new CellConstraints();
            setLayout(layout);

            rowSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
            colSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));


            Anatomy3D anatomy = view.getSelectedPlot().getDisplayAnatomy();
            ImageAxis iaxis = view.getModel().getImageSpace().getImageAxis(anatomy.ZAXIS, true);
            gapSpinner = new JSpinner(new SpinnerNumberModel(iaxis.getSpacing(), iaxis.getSpacing(), 20, 1));

            add(rowSpinner, cc.xyw(4, 2, 2));
            add(colSpinner, cc.xyw(4, 4, 2));
            add(gapSpinner, cc.xyw(4, 6, 2));

            add(new JLabel("Rows:"), cc.xy(2, 2));
            add(new JLabel("Columns:"), cc.xy(2, 4));
            add(new JLabel("Slice Gap:"), cc.xy(2, 6));


        }

        public int getRows() {
            return ((Number) rowSpinner.getValue()).intValue();
        }

        public int getColumns() {
            return ((Number) colSpinner.getValue()).intValue();
        }

        public double getSliceGap() {
            return ((Number) gapSpinner.getValue()).doubleValue();
        }
    }


}