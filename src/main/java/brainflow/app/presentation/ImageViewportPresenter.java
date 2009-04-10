package brainflow.app.presentation;

import brainflow.app.YokeHandler;
import brainflow.app.presentation.binding.PropertyConnector;
import brainflow.core.*;
import brainflow.core.annotations.BoxAnnotation;
import brainflow.image.anatomy.AnatomicalPoint2D;
import brainflow.image.anatomy.Anatomy3D;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2007
 * Time: 10:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewportPresenter extends ImageViewPresenter {

    private JComboBox plotSelector = new JComboBox();

    private JPanel form;

    private JLabel choiceLabel;

    private YokeHandler yokeHandler;

    private SpinnerNumberModel xspinnerModel;

    private SpinnerNumberModel yspinnerModel;

  //  private BeanAdapter viewportAdapter;

  //  private BoundedRangeAdapter xfovAdapter;

  //  private BoundedRangeAdapter yfovAdapter;

    private PropertyConnector xoriginConnector;

    private PropertyConnector yoriginConnector;

    private PropertyConnector widthConnector;

    private PropertyConnector heightConnector;




    private JSlider xfovSlider;

    private JSlider yfovSlider;

    private FormLayout layout;

    private ImageView boxView;

    private BoxAnnotation boxAnnotation = new BoxAnnotation();

    private JPanel viewPanel;

    private MouseMotionListener panner = new Panner();


    public ImageViewportPresenter() {
        buildGUI();
    }

    private void buildGUI() {
        form = new JPanel();
        //layout = new FormLayout("8dlu, p:g, 6dlu, p:g, 8dlu", "8dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, min(125dlu;p):g, 1dlu, 3dlu, p, 6dlu");
        layout = new FormLayout("3dlu, p:g, 3dlu", "8dlu, min(155dlu;p):g, 3dlu");
        form.setLayout(layout);
        
        CellConstraints cc = new CellConstraints();

        //choiceLabel = new JLabel("Selected Plot: ");
        //form.add(choiceLabel, cc.xy(2, 2));
        //form.add(plotSelector, cc.xywh(2, 4, 3, 1));

        //xspinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
        //yspinnerModel = new SpinnerNumberModel(0, 0, 100, 1);

        //xorigin = new JSpinner();
        //xorigin.setModel(xspinnerModel);
        //xorigin.setEditor(new JSpinner.NumberEditor(xorigin, "###.#");

        //yorigin = new JSpinner();
        //yorigin.setModel(yspinnerModel);
        //yorigin.setEditor(yspinnerModel);

        //form.add(new JLabel("Origin: "), cc.xy(2, 6));

        //JPanel xpan = new JPanel();
        //xpan.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //xpan.setLayout(new BoxLayout(xpan, BoxLayout.X_AXIS));
        //xpan.add(new JLabel("X: "));
        //xpan.add(xorigin);

        //JPanel ypan = new JPanel();
        //ypan.setLayout(new BoxLayout(ypan, BoxLayout.X_AXIS));
        //ypan.add(new JLabel("Y: "));
        //ypan.add(yorigin);

        //form.add(xpan, cc.xy(2, 2));
        //form.add(ypan, cc.xy(4, 2));

        viewPanel = new JPanel();
        boxView = new SimpleImageView(new ImageViewModel(), Anatomy3D.getCanonicalAxial());
        //boxView.pixelsPerUnit.set(.7);
        boxView.clearAnnotations();
        boxView.getSelectedPlot().setPlotInsets(new Insets(2, 2, 2, 2));
        boxAnnotation.setVisible(false);
        boxView.setAnnotation(boxView.getSelectedPlot(), BoxAnnotation.ID, boxAnnotation);

        viewPanel.setLayout(new JideBorderLayout());
        viewPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //viewPanel.setBackground(Color.BLACK);
        viewPanel.add(boxView, BorderLayout.CENTER);

        xfovSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        yfovSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        xfovSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateTooltips(getSelectedView());
            }
        });

        yfovSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateTooltips(getSelectedView());
            }
        });


        viewPanel.setBorder(BorderFactory.createEmptyBorder());
        viewPanel.add(xfovSlider, BorderLayout.SOUTH);
        viewPanel.add(yfovSlider, BorderLayout.WEST);

        form.add(viewPanel, cc.xy(2, 2));

        //form.add(fovSlider, cc.xywh(2,13,3,1));

        setEnabled(false);

    }

    private int convertXRangeToInteger(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        double interval = view.getModel().getImageSpace().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getInterval();
        double viewRange = plot.getXAxisRange().getInterval();

        return (int) ((viewRange / interval) * 100);

    }

    private int convertYRangeToInteger(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        double interval = view.getModel().getImageSpace().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getInterval();
        double viewRange = plot.getYAxisRange().getInterval();

        return (int) ((viewRange / interval) * 100);

    }

    private double getBoxWidth(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport();
        return viewport.getRange(plot.getXAxisRange().getAnatomicalAxis()).getInterval();

    }

    private double getBoxMinX(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport();
        return viewport.getRange(plot.getXAxisRange().getAnatomicalAxis()).getMinimum();

    }

    private double getBoxMinY(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport();
        return viewport.getRange(plot.getYAxisRange().getAnatomicalAxis()).getMinimum();

    }


    private double getBoxHeight(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport();
        return viewport.getRange(plot.getYAxisRange().getAnatomicalAxis()).getInterval();


    }


    private void updateTooltips(ImageView view) {

        xfovSlider.setToolTipText(view.getSelectedPlot().getXAxisRange().getAnatomicalAxis().toString() + " : " +
                view.getSelectedPlot().getXAxisRange().getInterval());

        yfovSlider.setToolTipText(view.getSelectedPlot().getYAxisRange().getAnatomicalAxis().toString() + " : " +
                view.getSelectedPlot().getYAxisRange().getInterval());

    }

    private void updateView(ImageView view) {

        //BeanContainer.get().addListener(view.plotSelection, new PropertyListener() {
        //    public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
        //        plotSelector.repaint();
        //    }
        //});

        viewPanel.remove(boxView);

        updateTooltips(view);



        //if (boxView == null) {
            boxView = new SimpleImageView(view.getModel(), Anatomy3D.getCanonicalAxial());
       //     boxView.identifier.set("Viewport Editor");
             boxView.clearAnnotations();
            boxView.getSelectedPlot().setPlotInsets(new Insets(2, 2, 2, 2));
            boxAnnotation.setVisible(true);

        // initialize box with correct origin for current plot viewport
        boxAnnotation.setXmin(getBoxMinX(view));
        boxAnnotation.setYmin(getBoxMinY(view));

        // initialize box with correct height and width for current plot viewport
        boxAnnotation.setWidth(getBoxWidth(view));
        boxAnnotation.setHeight(getBoxHeight(view));

        boxView.setAnnotation(boxView.getSelectedPlot(), BoxAnnotation.ID, boxAnnotation);

        viewPanel.add(boxView);
        boxView.getSelectedPlot().getComponent().addMouseMotionListener(panner);
        boxView.getSelectedPlot().getComponent().addMouseListener((MouseListener) panner);

        if (yokeHandler == null) {
            yokeHandler = new YokeHandler(boxView);
            yokeHandler.addSource(view);
        } else {

            yokeHandler.clearSources();
            yokeHandler.setTarget(boxView);
            yokeHandler.addSource(view);

        }

        /// what about unyoking old views?
        //DisplayManager.get().yoke(view, boxView);

    }



    public void viewSelected(ImageView view) {

        bindComponents();
        updateView(view);
        setEnabled(true);
        form.revalidate();

    }

    @Override
    public void viewModelChanged(ImageView view) {
        viewSelected(view);
    }

    private void intializeAdapters() {
        ImageView view = getSelectedView();
        Viewport3D viewport = view.getViewport();
        IImagePlot plot = view.getSelectedPlot();
        
       // if (viewportAdapter == null) {
       //     viewportAdapter = new BeanAdapter(viewport, true);
       // } else {
       //     viewportAdapter.setBean(viewport);
       // }

        //ValueModel xextent = viewportAdapter.getValueModel(viewport.getExtentProperty(plot.getXAxisRange().getAnatomicalAxis()));
        //ValueModel yextent = viewportAdapter.getValueModel(viewport.getExtentProperty(plot.getYAxisRange().getAnatomicalAxis()));


        //xfovAdapter = new BoundedRangeAdapter(new PercentageConverter(xextent,
        //        new ValueHolder(10),
        //        new ValueHolder(viewport.getBounds().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getInterval()), 100), 0, 0, 100);
        //xfovSlider.setModel(xfovAdapter);

        //yfovAdapter = new BoundedRangeAdapter(new PercentageConverter(yextent,
        //        new ValueHolder(10),
        //        new ValueHolder(viewport.getBounds().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getInterval()), 100), 0, 0, 100);
        //yfovSlider.setModel(yfovAdapter);

        // this line links zero and zero extent so that changing zero changes the other
        //PropertyConnector.connect(xextent, "value", yextent, "value");

    }

    private void connectBox(Viewport3D viewport, IImagePlot plot) {

        if (xoriginConnector != null) {
            xoriginConnector.disconnect();
            yoriginConnector.disconnect();
            widthConnector.disconnect();
            heightConnector.disconnect();
        }

        xoriginConnector = new PropertyConnector<Double>(viewport.getMinProperty(plot.getXAxisRange().getAnatomicalAxis()), boxAnnotation.xmin);
        yoriginConnector = new PropertyConnector<Double>(viewport.getMinProperty(plot.getYAxisRange().getAnatomicalAxis()), boxAnnotation.ymin);
        widthConnector = new PropertyConnector<Double>(viewport.getExtentProperty(plot.getXAxisRange().getAnatomicalAxis()), boxAnnotation.width);
        heightConnector = new PropertyConnector<Double>(viewport.getExtentProperty(plot.getYAxisRange().getAnatomicalAxis()), boxAnnotation.height);

        xoriginConnector.updateProperty();
        yoriginConnector.updateProperty();
        widthConnector.updateProperty();
        heightConnector.updateProperty();

    }

    private void bindComponents() {
        // assumes view not null;

        ImageView view = getSelectedView();


        //SwingBind.get().bindContent(view.getPlots(), plotSelector);
        //SwingBind.get().bindIndex(view.plotSelection, plotSelector);

        connectBox(view.getViewport(), view.getSelectedPlot());

        intializeAdapters();







        //xspinnerModel = new SpinnerNumberModel((Number) xval.value(), getXLowerBound(view), getXUpperBound(view), 1);
        //yspinnerModel = new SpinnerNumberModel((Number) yval.value(), getYLowerBound(view), getYUpperBound(view), 1);


        //SpinnerAdapterFactory.connect(xspinnerModel, xval, xval.value());
        //SpinnerAdapterFactory.connect(yspinnerModel, yval, yval.value());


    }

    private void setEnabled(boolean b) {
        form.setEnabled(b);
        //xorigin.setEnabled(b);
        //yorigin.setEnabled(b);
        //xfovSlider.setEnabled(b);
        //yfovSlider.setEnabled(b);


    }

    public void allViewsDeselected() {
        boxView.removeMouseMotionListener(panner);
        boxView.removeMouseListener((MouseListener) panner);
        setEnabled(false);
    }

    public JComponent getComponent() {
        return form;
    }


    class Panner extends MouseAdapter implements MouseMotionListener {

        private AnatomicalPoint2D lastPoint = null;


        public void mousePressed(MouseEvent e) {
            lastPoint = boxAnnotation.translateFromJava2D(boxView.getSelectedPlot(), e.getPoint());
        }

        public void mouseReleased(MouseEvent e) {
            lastPoint = null;
        }

        public void mouseMoved(MouseEvent e) {

        }


        public void mouseDragged(MouseEvent e) {
            if (lastPoint == null) return;

            IImagePlot plot = boxView.getSelectedPlot();

            if (boxAnnotation.containsPoint(plot, e.getPoint())) {
                AnatomicalPoint2D next = boxAnnotation.translateFromJava2D(plot, e.getPoint());


                Number xold = boxAnnotation.getXmin();
                Number yold = boxAnnotation.getYmin();


                double newx = xold.doubleValue() + (next.getX() - lastPoint.getX());
                double newy = yold.doubleValue() + (next.getY() - lastPoint.getY());

                if (newx < plot.getXAxisRange().getMinimum()) {
                    newx = plot.getXAxisRange().getMinimum();
                } else if (newy < plot.getYAxisRange().getMinimum()) {
                    newy = plot.getYAxisRange().getMinimum();
                }




                lastPoint = next;
            }
        }
    }


}
