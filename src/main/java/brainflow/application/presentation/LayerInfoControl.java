package brainflow.application.presentation;

import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.pane.CollapsiblePane;
import brainflow.gui.AbstractPresenter;

import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2008
 * Time: 9:03:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayerInfoControl {

    private CollapsiblePanes cpanes;

    private SelectedLayerPresenter selectedLayerPresenter;

    private ImageLayerInfoPresenter layerInfoPresenter;


    public LayerInfoControl() {
        init();
    }


    public JComponent getComponent() {
        return cpanes;
    }

    private void addCollapsiblePane(AbstractPresenter presenter, String title, boolean collapsed) {
        CollapsiblePane cp = new CollapsiblePane();

        //cp.setUI(new VsnetCollapsiblePaneUI());
        cp.setContentPane(presenter.getComponent());
        cp.setTitle(title);

        cp.setEmphasized(true);
        cp.setOpaque(false);
        //cp.setStyle(CollapsiblePane.PLAIN_STYLE);

        try {
            cp.setCollapsed(collapsed);
        } catch (PropertyVetoException e) {
            // not that important ...
        }

        cpanes.add(cp);


    }


    private void init() {
        cpanes = new CollapsiblePanes();

        selectedLayerPresenter = new SelectedLayerPresenter();
        addCollapsiblePane(selectedLayerPresenter, "Selection", false);

        layerInfoPresenter = new ImageLayerInfoPresenter();
        addCollapsiblePane(layerInfoPresenter, "Information", false);

        cpanes.addExpansion();


    }


}
