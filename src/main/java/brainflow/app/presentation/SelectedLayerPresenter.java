package brainflow.app.presentation;

import brainflow.core.*;
import brainflow.core.binding.ExtBind;
import brainflow.core.binding.WrappedImageViewModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 1, 2006
 * Time: 10:27:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectedLayerPresenter extends ImageViewPresenter {


    private FormLayout layout;

    private JLabel layerLabel;


    private CheckBoxList layerSelector;

    private JPanel form;

    private JScrollPane formPane;

    private WrappedImageViewModel wrappedModel;


    public SelectedLayerPresenter() {
        buildGUI();

    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("6dlu, 70dlu:g, 1dlu, 12dlu", "8dlu, p, 3dlu, 1dlu, max(p;55dlu), 5dlu");
        form.setLayout(layout);

        CellConstraints cc = new CellConstraints();

        layerLabel = new JLabel("Selected Layer: ");
        //layerSelector = new CheckBoxList();
        layerSelector = new CheckBoxList();

        formPane = new JScrollPane(layerSelector);
        form.add(layerLabel, cc.xy(2, 2));
        form.add(formPane, cc.xywh(2, 4, 2, 2));
        layout.addGroupedColumn(2);

        if (getSelectedView() != null) {
            bind();
        }

    }

    private void bind() {

        wrappedModel = new WrappedImageViewModel(getSelectedView().getModel());

        ExtBind.get().bindContent(wrappedModel.listModel, layerSelector);

        ExtBind.get().bindSelectionIndex(getSelectedView().getModel().layerSelection, layerSelector);

        ExtBind.get().bindCheckedIndices(wrappedModel.visibleSelection, layerSelector);

    }

    public void viewDeselected(ImageView view) {
        ExtBind.get().unbind(layerSelector);


    }


    public void allViewsDeselected() {
        layerSelector.setEnabled(false);

    }


    public void viewSelected(ImageView view) {
        bind();
        layerSelector.setEnabled(true);

    }

   
    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        ExtBind.get().unbind(layerSelector);
        viewSelected(view);
    }

    public JComponent getComponent() {
        return form;
    }




}
