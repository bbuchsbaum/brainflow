package brainflow.app.presentation;

import brainflow.app.presentation.controls.CrosshairForm;
import brainflow.core.annotations.CrosshairAnnotation;
import brainflow.gui.AbstractPresenter;

import com.jidesoft.combobox.ColorComboBox;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 12, 2007
 * Time: 9:36:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairPresenter extends AbstractPresenter {

    private CrosshairAnnotation annotation;

    private CrosshairForm form;

    public CrosshairPresenter(CrosshairAnnotation _annotation) {
        annotation = _annotation;
        form = new CrosshairForm();
        initBindings();
    }


    public CrosshairAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(CrosshairAnnotation annotation) {
        
        this.annotation = annotation;
    }

    private void initBindings() {

        // bind line width property to JSpinner
        JSpinner lineWidthSpinner = form.getLineWidthSpinner();

        SpinnerModel spinnerModel = new SpinnerNumberModel(annotation.getLineWidth(), 1.0, 10.0, .3);
        lineWidthSpinner.setModel(spinnerModel);

         form.getColorChooser().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == ColorComboBox.PROPERTY_SELECTED_ITEM) {
                    Color clr = (Color)evt.getNewValue();
                    annotation.setLinePaint(clr);
                }
            }
        });

        // todo cast here assumes Color class.
        form.getColorChooser().setSelectedColor((Color)annotation.getLinePaint());

        JSpinner gapSpinner = form.getGapSpinner();
        spinnerModel = new SpinnerNumberModel(annotation.getGap(), 0, 20, 1);
        gapSpinner.setModel(spinnerModel);

        JSpinner lengthSpinner = form.getLineLengthSpinner();
        spinnerModel = new SpinnerNumberModel(annotation.getLineLength(), .1, 1, .1);
        lengthSpinner.setModel(spinnerModel);
        //form.getCancelButton().addActionListener();


    }


    public JComponent getComponent() {
        return form;

    }
}
