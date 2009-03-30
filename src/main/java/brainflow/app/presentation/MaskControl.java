package brainflow.app.presentation;

import brainflow.gui.AbstractPresenter;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.pane.CollapsiblePane;

import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 26, 2008
 * Time: 2:43:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskControl extends AbstractPresenter {


    private CollapsiblePanes cpanes;

    private SelectedLayerPresenter selectedLayerPresenter;

    private MaskPresenter maskPresenter;

    private MaskExpressionPresenter expressionPresenter;

    public MaskControl() {
        init();
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

        maskPresenter = new MaskPresenter();
        addCollapsiblePane(maskPresenter, "Mask View", false);

        expressionPresenter = new MaskExpressionPresenter();
        addCollapsiblePane(expressionPresenter, "Define Mask", false);

        //interpolationPresenter = new InterpolationPresenter();
        //addCollapsiblePane(interpolationPresenter, "Resample Method", true);

        cpanes.addExpansion();


    }



    public JComponent getComponent() {
        return cpanes;
    }
}
