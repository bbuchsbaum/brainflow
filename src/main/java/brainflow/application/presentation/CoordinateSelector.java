package brainflow.application.presentation;

import brainflow.core.BrainCanvasModel;
import brainflow.core.ImageView;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 22, 2006
 * Time: 2:33:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSelector extends JComponent {


    private JLabel xLabel = new JLabel("X: ");
    private JLabel yLabel = new JLabel("Y: ");
    private JLabel zLabel = new JLabel("Z: ");

    private JSpinner xSpinner = new JSpinner();
    private JSpinner ySpinner = new JSpinner();
    private JSpinner zSpinner = new JSpinner();


    private BrainCanvasModel canvasModel;
    private ImageView selectedView;


    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);


    private AnatomicalPoint3D crosshairVoxel = new AnatomicalPoint3D(Anatomy3D.getCanonicalAxial(), 0, 0, 0);
    private BeanAdapter crosshairVoxelBean = new BeanAdapter(crosshairVoxel, true);


    public CoordinateSelector(BrainCanvasModel _canvasModel) {
        canvasModel = _canvasModel;
        selectedView = _canvasModel.getSelectedView();

        init();
    }

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }


    private void init() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        setLayout(layout);


        SpinnerNumberModel snm1 = new SpinnerNumberModel();
        snm1.setMinimum(0);
        snm1.setMaximum(300);
        xSpinner.setModel(snm1);

        snm1 = new SpinnerNumberModel();
        snm1.setMinimum(0);
        snm1.setMaximum(300);
        ySpinner.setModel(snm1);

        snm1 = new SpinnerNumberModel();
        snm1.setMinimum(0);
        snm1.setMaximum(300);
        zSpinner.setModel(snm1);


        xSpinner.setEditor(new JSpinner.NumberEditor(xSpinner, "###"));
        ValueModel xmodel = crosshairVoxelBean.getValueModel("zero");
        SpinnerAdapterFactory.connect(xSpinner.getModel(), xmodel, 0);
        //xSpinner.addChangeListener(this);

        ySpinner.setEditor(new JSpinner.NumberEditor(ySpinner, "###"));
        ValueModel ymodel = crosshairVoxelBean.getValueModel("zero");
        SpinnerAdapterFactory.connect(ySpinner.getModel(), ymodel, 0);
        //ySpinner.addChangeListener(this);

        zSpinner.setEditor(new JSpinner.NumberEditor(zSpinner, "###"));
        ValueModel zmodel = crosshairVoxelBean.getValueModel("one");
        SpinnerAdapterFactory.connect(zSpinner.getModel(), zmodel, 0);
        //zSpinner.addChangeListener(this);

        add(xLabel);
        add(xSpinner);
        add(yLabel);
        add(ySpinner);
        add(zLabel);
        add(zSpinner);

        PropertyConnector.connect(canvasModel, "selectedView", this, "selectedView");
        selectedView = canvasModel.getSelectedView();

        if (selectedView != null) {
            PropertyConnector.connect(selectedView, "crosshairVoxel", this, "crosshairVoxel");
        } else {
            disableSpinners();
        }


    }


    private void disableSpinners() {
        xSpinner.setEnabled(false);
        ySpinner.setEnabled(false);
        zSpinner.setEnabled(false);

    }

    private void enableSpinners() {
        xSpinner.setEnabled(true);
        ySpinner.setEnabled(true);
        zSpinner.setEnabled(true);

    }


    public ImageView getSelectedView() {
        return selectedView;
    }

    public void setSelectedView(ImageView _selectedView) {

        if (this.selectedView != _selectedView) {
            this.selectedView = _selectedView;
           PropertyConnector.connect(selectedView, "crosshairVoxel", this, "crosshairVoxel");
            enableSpinners();
        }


    }

    public AnatomicalPoint3D getCrosshairVoxel() {
        return crosshairVoxel;
    }

    public void setCrosshairVoxel(AnatomicalPoint3D _crosshairVoxel) {
        AnatomicalPoint3D oldVoxel = _crosshairVoxel;
        crosshairVoxel = _crosshairVoxel;
        crosshairVoxelBean.setBean(crosshairVoxel);

        changeSupport.firePropertyChange("crosshairVoxel", oldVoxel, crosshairVoxel);

    }


}


