package brainflow.application.presentation.wizards;

import brainflow.gui.AbstractPresenter;
import brainflow.image.space.ICoordinateSpace;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 4:32:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateTableSetupPresenter extends AbstractPresenter {

    private CoordinateTableSetupForm form = new CoordinateTableSetupForm();

    private CoordinateTableSetupInfo info = new CoordinateTableSetupInfo();

    private BeanAdapter adapter;

    public CoordinateTableSetupPresenter() {
        initBindings();

    }

    public void setCoordinateSpace(ICoordinateSpace space) {
        info.setCoordinateSpace(space);
    }

    public JComponent getComponent() {
        return form;
    }

    public CoordinateTableSetupInfo getInfo() {
        return info;
    }

    private void initBindings() {
        adapter = new BeanAdapter(info);
        JSpinner opacitySpinner = form.getOpacitySpinner();

        ValueModel opacity = adapter.getValueModel(CoordinateTableSetupInfo.DEFAULT_OPACITY_PROPERTY);
        SpinnerModel spinnerModel = new SpinnerNumberModel(info.getDefaultOpacity(), 0, 1, .1);
        SpinnerAdapterFactory.connect(spinnerModel, opacity, info.getDefaultOpacity());
        opacitySpinner.setModel(spinnerModel);

        ValueModel defaultValue = adapter.getValueModel(CoordinateTableSetupInfo.DEFAULT_VALUE_PROPERTY);
        spinnerModel = new SpinnerNumberModel(info.getDefaultValue(), 0, 1, .1);
        SpinnerAdapterFactory.connect(spinnerModel, defaultValue, info.getDefaultValue());
        //opacitySpinner.setModel(spinnerModel);

        ValueModel tableSize = adapter.getValueModel(CoordinateTableSetupInfo.TABLE_SIZE_PROPERTY);
        spinnerModel = new SpinnerNumberModel(info.getTableSize(), 1, 1000, 1);
        SpinnerAdapterFactory.connect(spinnerModel, tableSize, info.getTableSize());
        form.getTableSizeSpinner().setModel(spinnerModel);

        ValueModel defaultColor = adapter.getValueModel(CoordinateTableSetupInfo.DEFAULT_COLOR_PROPERTY);
        PropertyConnector connector = PropertyConnector.connect(defaultColor, "value", form.getColorChooser(), "selectedColor");
        connector.updateProperty2();
        

    }
}
