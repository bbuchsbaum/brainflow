package brainflow.app.actions;




import javax.swing.*;

import brainflow.core.ImageView;
import brainflow.core.IImagePlot;
import com.jidesoft.combobox.InsetsComboBox;
import com.jidesoft.combobox.InsetsChooserPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 11, 2007
 * Time: 7:22:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdjustMarginsCommand extends BrainFlowCommand {

    private static final Logger log = Logger.getLogger(AdjustMarginsCommand.class.getName());

    private JDialog dialog;


    public AdjustMarginsCommand() {
        // icon = "/resources/icons/layout_header.png"
        //putValue(Action.NAME, "Adjust Margins");
        
    }

    protected void handleExecute() {
        final ImageView view = (ImageView) getSelectedView();

        if (view != null) {
            final IImagePlot plot = view.getPlots().get(0);
            Insets oldInsets = (Insets) plot.getPlotInsets().clone();

            InsetsComboBox ipanel = new InsetsComboBox(plot.getPlotInsets());

            final InsetsChooserPanel panel = (InsetsChooserPanel)ipanel.createPopupComponent();
            panel.setSelectedInsets(oldInsets);
            Container c = JOptionPane.getFrameForComponent(view);


            AbstractAction okAction = new AbstractAction("OK") {

                public void actionPerformed(ActionEvent e) {
                    plot.setPlotInsets(panel.getSelectedInsets());
                    view.repaint();
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            };

            AbstractAction cancelAction = new AbstractAction("Cancel") {

                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            };

            panel.setOkAction(okAction);
            panel.setCancelAction(cancelAction);




            dialog = new JDialog(JOptionPane.getFrameForComponent(view));
            dialog.setLayout(new BorderLayout());

            Point p = c.getLocation();
            JPanel mainPanel = new JPanel();
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 15, 8));
            mainPanel.setLayout(new BorderLayout());

            mainPanel.add(panel, BorderLayout.CENTER);

            dialog.add(mainPanel, BorderLayout.CENTER);
            dialog.pack();
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setLocation((int) (p.getX() + c.getWidth() / 2f), (int) (p.getY() + c.getHeight() / 2f));
            dialog.setVisible(true);

        } else {
            setEnabled(false);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }





}