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

    JComponent getComponent();

    BrainCanvasModel getImageCanvasModel();

    void addInteractor(ImageViewInteractor interactor);

    void removeInteractor(ImageViewInteractor interactor);

    ImageView whichSelectedView(Point p);

    ImageView whichView(Component source, Point p);

    Component whichComponent(Point p);

    IImagePlot whichPlot(Point p);

    java.util.List<IImagePlot> getPlotList();

    boolean isSelectedView(ImageView view);

    java.util.List<ImageView> getViews(ImageViewModel model);

    List<ImageView> getViews();

    void setSelectedView(ImageView view);

    ImageView getSelectedView();

    void removeImageView(ImageView view);

    void addImageView(ImageView view);

    void moveToFront(ImageView view);
}
