package brainflow.gui;

import brainflow.utils.RangeModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.combobox.PopupPanel;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 12, 2006
 * Time: 6:45:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangePopupPanel extends PopupPanel {

    private RangeModel rangeModel;
    private JFormattedTextField minField;
    private JFormattedTextField maxField;
    //private BeanAdapter adapter;

    private FormLayout layout;

    public RangePopupPanel(RangeModel range) {

        rangeModel = range;
        //adapter = new BeanAdapter(rangeModel);
        init();
        setDefaultFocusComponent(minField);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    public void setRangeModel(RangeModel range) {
        rangeModel = range;
        //adapter.setBean(rangeModel);

    }

    public JTextField getMinField() {
        return minField;
    }

    public JTextField getMaxField() {
        return maxField;
    }

    private void init() {
        layout = new FormLayout("6dlu, l:p, 3dlu, l:max(30dlu;p), 3dlu, l:p, 3dlu, l:max(30dlu;p), 6dlu", "8dlu, p, 8dlu");

        setLayout(layout);

        CellConstraints cc = new CellConstraints();
        add(new JLabel("From: "), cc.xy(2, 2));

        minField = new JFormattedTextField(NumberFormat.getNumberInstance());
        minField.setValue(rangeModel.getMin());
        minField.setColumns(7);
        //Bindings.bind(minField, adapter.getValueModel(RangeModel.RANGE_MIN_PROPERTY));

        add(minField, cc.xy(4, 2));
        add(new JLabel("To:"), cc.xy(6, 2));


        maxField = new JFormattedTextField(NumberFormat.getNumberInstance());
        maxField.setValue(rangeModel.getMax());
        maxField.setColumns(7);

        //Bindings.bind(maxField, adapter.getValueModel(RangeModel.RANGE_MAX_PROPERTY));
        add(maxField, cc.xy(8, 2));


    }
}
