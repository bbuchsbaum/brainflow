package brainflow.application.actions;


import java.awt.event.ActionEvent;

import brainflow.core.*;
import brainflow.core.annotations.CrosshairAnnotation;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 28, 2007
 * Time: 11:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleCrosshairAction extends BasicAction {

     public ToggleCrosshairAction() {
        putValue(Action.NAME, "Toggle Cross");
        //putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
    }


    protected void contextChanged() {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        if (view != null) {
            setEnabled(true);
            CrosshairAnnotation annot = (CrosshairAnnotation)view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
            if (annot != null) {
                boolean selected = annot.isVisible();
                setSelected(selected);
            }
        } else {
            setEnabled(false);
        }
    }

    protected void execute(ActionEvent evt) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            CrosshairAnnotation annot = (CrosshairAnnotation)view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
            if (annot != null) {

                if (isSelected()) {
                    annot.setVisible(true);
                }
                else {
                    annot.setVisible(false);

                }

            }
        }


    }
}
