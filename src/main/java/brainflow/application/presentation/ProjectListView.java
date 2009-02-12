package brainflow.application.presentation;

import brainflow.application.BrainFlowProject;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayer;
import brainflow.core.IImageDisplayModel;
import brainflow.core.ImageView;
import brainflow.image.axis.CoordinateAxis;
import brainflow.image.space.Axis;
import brainflow.image.space.ICoordinateSpace;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.MultilineLabel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.NumberFormat;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 22, 2007
 * Time: 9:47:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectListView extends ImageViewPresenter {

    private BrainFlowProject project;

    private JPanel form;

    private JList modelList;

    private JList layerList;

    private MultilineLabel infoLabel;

    private FormLayout layout;

    private IImageDisplayModel selectedModel;

    public ProjectListView(BrainFlowProject _project) {
        project = _project;
        buildGUI();

    }

     public void bind() {
        SwingBind.get().bindContent(getSelectedView().getModel().getListModel(), layerList);
        SwingBind.get().bindSelectionIndex(getSelectedView().getModel().getListSelection(), layerList);



    }

    private void buildGUI() {

        layout = new FormLayout("6dlu, l:max(p;65dlu), 1dlu, 12dlu, l:max(p;100dlu):g, 1dlu, 4dlu", "8dlu, max(p;50dlu), 6dlu, max(p;30dlu), 4dlu");

        modelList = new JList(new ArrayListModel(project.getModelList()));


        if (project.size() > 0) {
            selectedModel = project.getModel(0);
            //layerList = new JList(selectedModel.getLayerSelection());
            layerList = new JList();
            modelList.setSelectedIndex(0);
        } else {
            layerList = new JList();
        }


        form = new JPanel();
        CellConstraints cc = new CellConstraints();
        form.setLayout(layout);

        JideTitledBorder border1 = new JideTitledBorder("Models");
        JideTitledBorder border2 = new JideTitledBorder("Layers");
        modelList.setBorder(border1);
        layerList.setBorder(border2);


        form.add(new JScrollPane(modelList), cc.xyw(2, 2, 2));
        form.add(new JScrollPane(layerList), cc.xyw(5, 2, 2));

        modelList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int i = modelList.getSelectedIndex();
                selectedModel = project.getModel(i);
                bind();
                //layerList.setModel(selectedModel.getLayerSelection());
            }
        });

        layerList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int i = layerList.getSelectedIndex();

                if (i >= 0) {
                    AbstractLayer layer = selectedModel.getLayer(i);
                    ICoordinateSpace space = layer.getCoordinateSpace();
                    StringBuffer sb = new StringBuffer();
                    sb.append("Orientation : " + space.getAnatomy().toString());
                    sb.append("\n");
                    CoordinateAxis xaxis = space.getImageAxis(Axis.X_AXIS);
                    CoordinateAxis yaxis = space.getImageAxis(Axis.Y_AXIS);
                    CoordinateAxis zaxis = space.getImageAxis(Axis.Z_AXIS);

                    sb.append("Extent : " + getExtentString(xaxis) + " " + getExtentString(yaxis) + " " + getExtentString(zaxis));
                    infoLabel.setText(sb.toString());
                }
            }
        });

        //JPanel infoPanel = new JPanel();
        infoLabel = new MultilineLabel();
        infoLabel.setText("    " + "\n" + "     " + "\n");
        infoLabel.setBorder(new JideTitledBorder("Coordinate Space"));
        //infoPanel.add(infoLabel);
        //infoPanel.setBorder(new JideTitledBorder("Coordinate Space"));
        form.add(infoLabel, cc.xyw(2, 4, 4));


    }

    private String getExtentString(CoordinateAxis axis) {
        NumberFormat f = NumberFormat.getNumberInstance();
        f.setMaximumFractionDigits(1);
        return "[" + f.format(axis.getRange().getMinimum()) + ", " + f.format(axis.getRange().getMaximum()) + "]";

    }

    public ImageLayer getSelectedLayer() {
        return (ImageLayer) layerList.getSelectedValue();
    }

    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return form;
    }


}
