package brainflow.gui;

import brainflow.utils.Range;
import brainflow.utils.RangeModel;
import brainflow.utils.IRange;
import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.combobox.PopupPanel;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 12, 2006
 * Time: 5:00:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangeComboBox extends AbstractComboBox {

    private RangeModel rangeModel = new RangeModel(new Range(0,0));

    private RangeEditorComponent editorComponent = new RangeEditorComponent(IRange.class);

    private RangePopupPanel popup;

    public RangeComboBox() {
        super(DROPDOWN);
        setEditable(false);
        setBorder(BorderFactory.createEmptyBorder());
        initComponent();
        setRange(rangeModel);

        
        
    }

    public RangeComboBox(RangeModel _range) {
        super(DROPDOWN);
        setEditable(false);
        setBorder(BorderFactory.createEmptyBorder());
        initComponent();
        setRange(_range);
    }


    public RangeModel getRange() {
        return rangeModel;
    }

    public void setRange(RangeModel range) {
        this.rangeModel = range;
        editorComponent.setItem(range);
        setSelectedItem(range);
       

        if (popup != null)
            popup.setRangeModel(rangeModel);

    }

    
    @Override
    public EditorComponent createEditorComponent() {
        return editorComponent;

    }

    public void addTextFieldActionListener(ActionListener listener) {
        if (popup == null) {
            popup = new RangePopupPanel(rangeModel);

        }
        popup.getMinField().addActionListener(listener);
        popup.getMaxField().addActionListener(listener);

    }

    public void removeTextFieldActionListener(ActionListener listener) {
        popup.getMinField().removeActionListener(listener);
        popup.getMaxField().removeActionListener(listener);

    }

    public PopupPanel createPopupComponent() {
        if (popup == null) {
            popup = new RangePopupPanel(rangeModel);


        }
        return popup;

    }

    class RangeEditorComponent extends DefaultTextFieldEditorComponent {

        public RangeEditorComponent(Class clazz) {
            super(clazz);

        }



        public String getText() {
            return rangeModel.toString();

        }


        public void setItem(Object value) {
            if (value == null) return;

            super.setItem(value);

            if (value instanceof RangeModel) {

                RangeModel r = (RangeModel) value;

                setText(r.toString());
                _textField.setText(r.toString());
            } else {
                
                _textField.setText(value.toString());
            }
        }

    }


}
