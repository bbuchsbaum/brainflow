package brainflow.app.actions;

import brainflow.app.presentation.ColorBandChartPresenter;
import brainflow.colormap.IColorMap;
import brainflow.core.layer.ImageLayer;
import brainflow.core.ImageView;


import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 24, 2006
 * Time: 3:12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignColorMapCommand extends BrainFlowCommand {


    public DesignColorMapCommand() {
        super();

    }

    protected void handleExecute() {
        //To change body of implemented methods use File | Settings | File Templates.

        ImageView view = getSelectedView();
        
        if (view != null) {

            //int layer = view.getModel().getSelectedLayerIndex();
            ImageLayer layer = view.getModel().getSelectedLayer();
            IColorMap oldMap = layer.getImageLayerProperties().getColorMap();

            //if (oldMap instanceof LinearColorMapDeprecated) {
            // todo fix me
            //IColorMap copyMap = oldMap.copy();

            ColorBandChartPresenter presenter = new ColorBandChartPresenter(layer.
                    getImageLayerProperties().getColorMap());
            int ret = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(presenter.getComponent()), presenter.getComponent(), "Design Color Map", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, null, null);

            if (ret != JOptionPane.OK_OPTION) {
                layer.getImageLayerProperties().colorMap.set(oldMap);

            }

            // }
        }


    }
}