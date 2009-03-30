package brainflow.app.presentation;

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 1:40:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateControls {


    private CollapsiblePanes cpanes;

    private WorldCoordinatePresenter2 worldCoordinatePresenter;

    private IndexCoordinatePresenter2 indexCoordinatePresenter;

    //private ImageViewportPresenter imageViewportPresenter;

    private LinkedViewsPresenter linkedViewsPresenter;

    public CoordinateControls() {
        init();
    }

    public JComponent getComponent() {
        return cpanes;
    }

    private void init() {
        cpanes = new CollapsiblePanes();

        linkedViewsPresenter = new LinkedViewsPresenter();
        worldCoordinatePresenter = new WorldCoordinatePresenter2();
        indexCoordinatePresenter = new IndexCoordinatePresenter2();
        //imageViewportPresenter = new ImageViewportPresenter();
        //imageExtentPresenter = new ImageExtentPresenter(activeView.getViewport());

        CollapsiblePane p1 = new CollapsiblePane();
        p1.setContentPane(linkedViewsPresenter.getComponent());
        p1.setTitle("Linked Views");
        p1.setEmphasized(true);
        p1.setOpaque(false);
        cpanes.add(p1);

        p1 = new CollapsiblePane();
        p1.setContentPane(worldCoordinatePresenter.getComponent());
        p1.setTitle("Crosshair (World)");
        p1.setEmphasized(true);
        p1.setOpaque(false);
        cpanes.add(p1);

        CollapsiblePane p2 = new CollapsiblePane();
        p2.setContentPane(indexCoordinatePresenter.getComponent());
        p2.setTitle("Crosshair (Voxel)");
        p2.setEmphasized(true);
        p2.setOpaque(false);
        cpanes.add(p2);

        //CollapsiblePane p3 = new CollapsiblePane();
        //p3.setContentPane(imageViewportPresenter.getComponent());
        //p3.setTitle("Plot Viewport");
        //p3.setEmphasized(true);
        //p3.setOpaque(false);
        //cpanes.add(p3);

        //CollapsiblePane p4 = new CollapsiblePane();
        //p4.setContentPane(imageExtentPresenter.getComponent());
        //p4.setTitle("Image Extent");
        //p4.setEmphasized(true);
        //p4.setOpaque(false);
        //cpanes.add(p4);

        cpanes.addExpansion();


    }
}
