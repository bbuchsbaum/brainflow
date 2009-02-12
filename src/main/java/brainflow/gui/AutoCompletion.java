package brainflow.gui;

import javax.swing.*;
import javax.swing.text.*;

public class AutoCompletion extends PlainDocument {
    JComboBox comboBox;
    
    public AutoCompletion(JComboBox comboBox) {
        this.comboBox = comboBox;
    }
    
    public static void enable(JComboBox comboBox) {
        // has to be editable
        comboBox.setEditable(true);
        // get the combo boxes editor component
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        // change the editor's document
        editor.setDocument(new AutoCompletion(comboBox));
        // handle selected item
        Object selected = comboBox.getSelectedItem();
        if (selected != null) editor.setText(selected.toString());
    }
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // ignore empty insert
        if (str==null || str.length()==0) return;
        // construct the resulting string
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs, currentText.length());
        String futureText = beforeOffset + str + afterOffset;
        
        // lookup and select a matching item
        Object item = lookupItem(futureText);
        if (item != null) {
            comboBox.setSelectedItem(item);
        } else {
            // keep old item selected if there is no match
            item = comboBox.getSelectedItem();
            // do not move on the selection
            offs--;
        }
        
        // string representation of the selected item
        String itemString = item==null?"":item.toString();
        
        // remove all text and insert the string representation
        remove(0, getLength());
        super.insertString(0, itemString, a);
        
        // select all if the user selects an item via mouse        
        if (itemString.equals(str) && offs==0) {
            offs=-1;
        } else {
            // otherwise the user used the keyboard
            // => show the popup list
            comboBox.setPopupVisible(true);
        }
        // select the completed part
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editor.setSelectionStart(offs+1);
        editor.setSelectionEnd(getLength());
    }
    
    private Object lookupItem(String pattern) {
        ComboBoxModel model = comboBox.getModel();
        Object selectedItem=model.getSelectedItem();
        
        // will point to the item that matches the pattern
        Object matchingItem=null;
        // only search for a different item if the currently selected does not match
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
            matchingItem = model.getSelectedItem();
        } else {
            // iterate over all items
            for (int i=0; i<model.getSize(); i++) {
                Object currentItem = model.getElementAt(i);
                // current item starts with the pattern?
                if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
                    matchingItem=currentItem;
                    break;
                }
            }
        }
        return matchingItem;
    }
    
    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().startsWith(str2.toUpperCase());
    }
    
    public static void main(String[] args) {
        // the combo box
        JComboBox comboBox = new JComboBox(new Object[] {"Ester", "Jordi", "Jordina", "Jorge", "Sergi"});
        // enable automatic completion
        AutoCompletion.enable(comboBox);
        // create and show a window containing the combo box
        JFrame frame = new JFrame();
        frame.getContentPane().add(comboBox);
        frame.pack(); frame.show();
    }
}