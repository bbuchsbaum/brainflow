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
public class ToggleCrossHairCommandX extends BrainFlowToggleCommand {

    public ToggleCrossHairCommandX() {
        super("toggle-crosshair");
        //putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
    }

    @Override
    public void viewSelected(ImageView view) {
        CrosshairAnnotation annot = (CrosshairAnnotation) view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
        if (annot != null) {
            super.setSelected(annot.isVisible());
        }
        
    }

    protected void handleSelection(boolean b) {

        ImageView view = getSelectedView();

        if (view != null) {
            CrosshairAnnotation annot = (CrosshairAnnotation) view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
            if (annot != null) {
                annot.setVisible(b);
            }
        }


    }
}
