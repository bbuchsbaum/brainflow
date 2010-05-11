package brainflow.app.presentation;

import brainflow.app.actions.RotateLayersCommand;
import brainflow.core.binding.ExtBind;
import brainflow.core.binding.WrappedImageViewModel;
import brainflow.app.toplevel.BrainFlow;
import brainflow.app.toplevel.DataSourceManager;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.gui.ToggleBar;
import brainflow.image.io.ImageInfo;
import brainflow.image.io.IImageSource;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.factory.ButtonFactory;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.action.CommandBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.*;
import java.awt.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 7:36:06 AM
 */
public class CanvasBar extends BrainFlowPresenter {


    private CommandBar canvasBar;

    private ToggleBar toggleBar;


    //private TransferHandler transferHandler = new CanvasBarTransferHandler();

    private MouseAdapter dragListener = new DragListener();

    private ActionCommand rotateCommand = new RotateLayersCommand();

    private JSpinner imageSpinner = new JSpinner();

    private JLabel imageSpinnerLabel = new JLabel("::");

    private WrappedImageViewModel wrappedModel;


    public CanvasBar() {
        super();

        buildGUI();


    }

    private void buildGUI() {
        canvasBar = new CommandBar();
        imageSpinner.setEnabled(false);
        imageSpinnerLabel.setEnabled(false);

        Dimension d = imageSpinner.getPreferredSize();
        d.setSize(200, d.getHeight());

        imageSpinner.setPreferredSize(d);

        //JideBoxLayout layout = new JideBoxLayout(canvasBar, BoxLayout.X_AXIS);
        //canvasBar.setLayout(layout);

        AbstractButton rotateButton = rotateCommand.createButton(new ButtonFactory() {
            @Override
            public JButton createButton() {
                return new JideButton();
            }

            @Override
            public AbstractButton createToggleButton() {
                return new JideToggleButton();
            }

            @Override
            public JCheckBox createCheckBox() {
                return new JCheckBox();
            }

            @Override
            public JRadioButton createRadioButton() {
                return new JRadioButton();
            }
        });


        ((JideButton)rotateButton).setButtonStyle(JideButton.TOOLBAR_STYLE);

        canvasBar.add(imageSpinnerLabel);
        canvasBar.add(imageSpinner);
        canvasBar.addSeparator();
        canvasBar.add(rotateButton);

        toggleBar = new ToggleBar(Arrays.asList("Tabula Rasa"));
        toggleBar.setBorder(new EmptyBorder(0,0,0,0));
        canvasBar.add(toggleBar);

        initSpinnerListener();
    }


    private void initSpinnerListener() {
        imageSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                String label = (String) imageSpinner.getValue();

                final ImageLayer3D layer = getSelectedLayer();
                IImageSource dsource = layer.getDataSource();

                //todo List<ImageInfo> might be Map<String, ImageInfo> (or something?)
                List<String> labels = extractLabels(dsource.getImageInfoList());
                int index = labels.indexOf(label);
         
                assert index >= 0;

                final IImageSource dsource2 = DataSourceManager.get().createDataSource(dsource.getDescriptor(), dsource.getImageInfoList(), index, true);
                //todo progress mechanism needed here

                SwingWorker worker = new SwingWorker() {
                    protected Object doInBackground() throws Exception {
                        Object ret = dsource2.getData();
                        ImageLayer3D newlayer = new ImageLayer3D(dsource2, layer.getLayerProps());
                        BrainFlow.get().replaceLayer(layer, newlayer, getSelectedView());
                        return ret;
                    }

                    @Override
                    protected void done() {
                        imageSpinner.setEnabled(true);
                    }
                };

                imageSpinner.setEnabled(false);
                worker.execute();

            }
        });

    }

    private void unbind() {
        ExtBind.get().unbind(toggleBar);
    }

    private List<String> extractLabels(List<ImageInfo> list) {

        List<String> labels = new ArrayList<String>();
        for (ImageInfo info : list) {
            labels.add(info.getImageLabel());

        }

        return labels;

    }

    private SpinnerListModel createSpinnerModel(List<String> labels) {
        return new SpinnerListModel(labels);
    }

    private void updateImageSpinner() {
        ImageLayer layer = getSelectedLayer();
        if (layer.getDataSource().getImageInfoList().size() > 1) {
            imageSpinner.setEnabled(true);
            imageSpinnerLabel.setEnabled(true);

            List<String> labels = extractLabels(layer.getDataSource().getImageInfoList());
            SpinnerListModel model = createSpinnerModel(labels);
            model.setValue(layer.getName());

            imageSpinner.setModel(model);

        } else {
            imageSpinner.setModel(new SpinnerNumberModel());
            imageSpinner.setEnabled(false);
            imageSpinnerLabel.setEnabled(false);


        }

    }


    private void bind() {
        updateImageSpinner();
        ExtBind.get().bindContent(wrappedModel.listModel, toggleBar);
        ExtBind.get().bindToggleBar(wrappedModel.layerSelection(), toggleBar);


    }

    @Override
    public void viewSelected(ImageView view) {
        wrappedModel = new WrappedImageViewModel(getSelectedView().getModel());

        bind();
    }

    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        ExtBind.get().unbind(toggleBar);
        wrappedModel = new WrappedImageViewModel(getSelectedView().getModel());
        viewSelected(view);
    }

    @Override
    protected void layerChangeNotification() {
        //bind();

    }

    public void viewDeselected(ImageView view) {
        ExtBind.get().unbind(toggleBar);
    }

    public JComponent getComponent() {
        return canvasBar;

    }

    public void allViewsDeselected() {
        unbind();
    }

    class DragListener extends MouseAdapter {


        public void mouseDragged(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler th = c.getTransferHandler();
            th.exportAsDrag(c, e, TransferHandler.MOVE);
        }


    }


}