package brainflow.core;

import brainflow.modes.ImageViewInteractor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 4:18:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IBrainCanvas  {

    public JComponent getComponent();

    public BrainCanvasModel getImageCanvasModel();

    public void addInteractor(ImageViewInteractor interactor);

    public void removeInteractor(ImageViewInteractor interactor);

    public ImageView whichSelectedView(Point p);

    public ImageView whichView(Component source, Point p);

    public Component whichComponent(Point p);

    public IImagePlot whichPlot(Point p);

    public java.util.List<IImagePlot> getPlotList();

    public boolean isSelectedView(ImageView view);

    public java.util.List<ImageView> getViews(ImageViewModel model);

    public List<ImageView> getViews();

    public void setSelectedView(ImageView view);

    public ImageView getSelectedView();

    public void removeImageView(ImageView view);

    public void addImageView(ImageView view);

    public void moveToFront(ImageView view);

    public int getNumViews();
}
