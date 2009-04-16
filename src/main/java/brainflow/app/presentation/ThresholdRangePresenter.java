/*
 * ThresholdRangePresenter.java
 *
 * Created on July 11, 2006, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.presentation;

import brainflow.app.presentation.binding.ExtBind;

import brainflow.core.*;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.gui.BiSlider;
import brainflow.gui.NumberRangeModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


/**
 * @author buchs
 */
public class ThresholdRangePresenter extends ImageViewPresenter {


    private JPanel form;

    private BiSlider bislider;

    private JCheckBox symmetricalCheckBox = new JCheckBox("absolute value");


    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter() {
        form = new JPanel();
        initGUI();

        if (getSelectedView() != null) {
            bind();
        }


    }

    private void initGUI() {

        bislider = new BiSlider(new NumberRangeModel(0, 100, 0, 100));
        form.setLayout(new BorderLayout());
        form.setBorder(new EmptyBorder(5, 1, 5, 1));
        form.add(bislider, BorderLayout.CENTER);


        symmetricalCheckBox.setBorder(new EmptyBorder(12,5,5,5));

        form.add(symmetricalCheckBox, BorderLayout.SOUTH);

        symmetricalCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ImageLayer layer = getSelectedLayer();
                    IClipRange oldclip = layer.getThreshold();
                    layer.getImageLayerProperties().thresholdRange.set(new AbsClipRange(oldclip.getMin(), oldclip.getMax(), oldclip.getHighClip()));
                } else {
                    ImageLayer layer = getSelectedLayer();
                    IClipRange oldclip = layer.getThreshold();
                    layer.getImageLayerProperties().thresholdRange.set(new ClipRange(oldclip.getMin(), oldclip.getMax(), oldclip.getLowClip(), oldclip.getHighClip()));

                }
            }
        });

    }


    public void viewSelected(ImageView view) {
        bind();
    }


    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        unbind();
        viewSelected(view);
    }

    @Override
    protected void layerSelected(ImageLayer3D layer) {
        bind();
    }


    public void allViewsDeselected() {
        unbind();
    }

    public JComponent getComponent() {
        return form;
    }

    public void unbind() {
        ExtBind.get().unbind(bislider);
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        ExtBind.get().bindBiSlider(layer.getImageLayerProperties().thresholdRange, bislider);

        if (layer.getImageLayerProperties().clipRange.get().getMin() >= 0) {
            symmetricalCheckBox.setEnabled(false);
        } else {
            symmetricalCheckBox.setEnabled(true); 
        }

        //todo egregious hack
        if (layer.getImageLayerProperties().thresholdRange.get() instanceof AbsClipRange) {
            symmetricalCheckBox.setSelected(true);
        } else {
            symmetricalCheckBox.setSelected(false);
        }


    }


    public static void main(String[] args) {


    }


}