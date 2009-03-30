package brainflow.app.presentation;

import brainflow.app.presentation.controls.ColorBarAnnotationForm;
import brainflow.core.annotations.ColorBarAnnotation;
import brainflow.gui.AbstractPresenter;


import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 12, 2007
 * Time: 9:36:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorBarAnnotationPresenter extends AbstractPresenter {

    private ColorBarAnnotation annotation;
    private ColorBarAnnotationForm form;

    public ColorBarAnnotationPresenter(ColorBarAnnotation _annotation) {
        annotation = _annotation;
        form = new ColorBarAnnotationForm();
        initBindings();
    }


    public ColorBarAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ColorBarAnnotation annotation) {
        this.annotation = annotation;
    }

    private void initBindings() {

        // bind lbar length property to JSpinner
        JSpinner barLengthSpinner = form.getBarLengthSpinner();

        /*ValueModel barLength = new PropertyAdapter(annotation, ColorBarAnnotation.BAR_LENGTH_PROPERTY, true);
        SpinnerModel spinnerModel = new SpinnerNumberModel(annotation.getBarLength(), .1, 1, .1);
        SpinnerAdapterFactory.connect(spinnerModel, barLength, 1);
        barLengthSpinner.setModel(spinnerModel);

        // bind visible property to JCheckBox
        Bindings.bind(form.getVisibleBox(), new PropertyAdapter(annotation, ColorBarAnnotation.VISIBLE_PROPERTY));

        // bind bar size to spinnerer
        JSpinner barSizeSpinner = form.getBarSizeSpinner();
        ValueModel barSizeModel = new PropertyAdapter(annotation, ColorBarAnnotation.BAR_SIZE_PROPERTY, true);
        spinnerModel = new SpinnerNumberModel(annotation.getBarSize(), 10, 200, 1);
        SpinnerAdapterFactory.connect(spinnerModel, barSizeModel, annotation.getBarSize());
        barSizeSpinner.setModel(spinnerModel);

        // bind margin to spinner
        JSpinner marginSpinner = form.getMarginSpinner();
        ValueModel marginModel = new PropertyAdapter(annotation, ColorBarAnnotation.MARGIN_PROPERTY, true);
        spinnerModel = new SpinnerNumberModel(annotation.getMargin(), 1, 80, 1);
        SpinnerAdapterFactory.connect(spinnerModel, marginModel, annotation.getMargin());
        marginSpinner.setModel(spinnerModel);    */

        // bind lineLength to JSpinner
        //JSpinner lengthSpinner = form.getLineLengthSpinner();
        //ValueModel lengthModel = new PropertyAdapter(annotation, CrosshairAnnotation.LINE_LENGTH_PROPERTY, true);
        //spinnerModel = new SpinnerNumberModel(annotation.getLineLength(), .1, 1, .1);
        //SpinnerAdapterFactory.connect(spinnerModel, lengthModel, 1);
        //lengthSpinner.setModel(spinnerModel);
        //form.getCancelButton().addActionListener();


    }


    public JComponent getComponent() {
        return form;

    }
}
