package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.core.annotations.IAnnotation;
import brainflow.image.axis.AxisRange;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import com.pietschy.command.toggle.ToggleGroup;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 8, 2009
 * Time: 11:31:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrthoImageView extends ImageView {

    private OrthoPlotLayout plotLayout;

    private Anatomy3D leadAnatomy = Anatomy3D.getCanonicalAxial();

    private ORIENTATION orientation;

    private CommandContainer commandContainer = new CommandContainer();

    private List<Slider> sliderList;

    private Slider slider1;

    private Slider slider2;

    private Slider slider3;


    protected class SwitchSagittal extends ToggleCommand {

        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isSagittal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalSagittal());
            }
        }

    }



    protected class SwitchAxial extends ToggleCommand {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isAxial()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalAxial());
            }
        }

    }



    protected class SwitchCoronal extends ToggleCommand {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isCoronal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalCoronal());
            }
        }


    }


    private ToggleCommand switchAxial = new SwitchAxial();

    private ToggleCommand switchCoronal = new SwitchCoronal();

    private ToggleCommand switchSagittal = new SwitchSagittal();


    public OrthoImageView(ImageViewModel imodel, ORIENTATION orientation) {
        super(imodel);    //To change body of overridden methods use File | Settings | File Templates.
        this.orientation = orientation;
        init();
    }

    public OrthoImageView(ImageViewModel imodel, Anatomy3D leadAnatomy, ORIENTATION orientation) {
        super(imodel);    //To change body of overridden methods use File | Settings | File Templates.
        this.leadAnatomy = leadAnatomy;
        this.orientation = orientation;
        init();
    }

    private void init() {
        slider1 = new Slider(this, leadAnatomy);
        slider2 = new Slider(this, leadAnatomy.getCanonicalOrthogonal()[1]);
        slider3 = new Slider(this, leadAnatomy.getCanonicalOrthogonal()[2]);

        sliderList = Arrays.asList(slider1,slider2,slider3);
       

        layoutPlots();
        initDisplayAnatomy(leadAnatomy);

        initToolBar();
        layoutPlots();


        commandContainer.bind(this);



    }

    protected void layoutPlots() {
        plotLayout = createPlotLayout(leadAnatomy);
        initPlotLayout(plotLayout, new HashMap<String, IAnnotation>());

    }

    public OrthoPlotLayout getPlotLayout() {
        return plotLayout;
    }

    protected OrthoPlotLayout createPlotLayout(Anatomy3D displayAnatomy) {
        plotLayout = new OrthoPlotLayout(this, displayAnatomy, orientation);
        return plotLayout;
    }

    protected void initDisplayAnatomy(Anatomy3D displayAnatomy) {
        if (displayAnatomy.isAxial()) {
            switchAxial.setSelected(true);
        } else if (displayAnatomy.isCoronal()) {
            switchCoronal.setSelected(true);
        } else if (displayAnatomy.isSagittal()) {
            switchSagittal.setSelected(true);
        } else {
            Logger.getAnonymousLogger().severe("DisplayAnatomy not one of : [Axial, Coronal, Sagittal] -- this should never happen");
        }

    }

    

    protected void initCommand(ActionCommand command, String text, String iconpath) {
        command.getDefaultFace(true).setText(text);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconpath));
        command.getDefaultFace().setIcon(icon);
        command.bind(commandContainer);

    }


    protected void initToolBar() {
        ToggleCommand switchAxial = new SwitchAxial();
        ToggleCommand switchCoronal = new SwitchCoronal();
        ToggleCommand switchSagittal = new SwitchSagittal();

        initCommand(switchAxial, "", "icons/axial_16.png");
        initCommand(switchCoronal, "", "icons/coronal_16.png");
        initCommand(switchSagittal, "", "icons/sagit_16.png");

        ToggleGroup switchGroup = new ToggleGroup();

        switchGroup.getDefaultFace(true);
        switchGroup.setEmptySelectionAllowed(false);
        switchGroup.setExclusive(true);

        GroupBuilder builder = switchGroup.getBuilder();

        builder.add(switchAxial);
        builder.add(switchCoronal);
        builder.add(switchSagittal);

        builder.applyChanges();
        switchGroup.bind(commandContainer);
        add(switchGroup.createToolBar(), BorderLayout.NORTH);


    }

    public enum ORIENTATION {
            HORIZONTAL,
            VERTICAL,
            TRIANGULAR
        }


    /**
     * Created by IntelliJ IDEA.
     * User: Brad Buchsbaum
     * Date: Sep 7, 2007
     * Time: 4:15:20 PM
     * To change this template use File | Settings | File Templates.
     */
    class OrthoPlotLayout extends ImagePlotLayout {



        private ORIENTATION orientation = ORIENTATION.HORIZONTAL;

        private Anatomy3D leadAnatomy = Anatomy3D.getCanonicalAxial();

        private LinkedSliceController sliceController = null;

        public OrthoPlotLayout(ImageView view) {
            super(view);
        }

        public OrthoPlotLayout(ImageView view, ORIENTATION orientation) {
            super(view);
            this.orientation = orientation;
        }

        public OrthoPlotLayout(ImageView view, Anatomy3D leadAnatomy, ORIENTATION orientation) {
            super(view);
            this.orientation = orientation;
            this.leadAnatomy = leadAnatomy;
        }





        protected java.util.List<IImagePlot> createPlots() {
            Anatomy3D[] anat = leadAnatomy.getCanonicalOrthogonal();
            java.util.List<IImagePlot> plots = new ArrayList<IImagePlot>();
            plots.add(super.createPlot(leadAnatomy));
            plots.add(super.createPlot(anat[1]));
            plots.add(super.createPlot(anat[2]));
            return plots;


        }

        public LinkedSliceController createSliceController() {
            if (sliceController == null) {
                sliceController = new LinkedSliceController(getView());
            }

            return sliceController;

        }

        public Dimension getPreferredSize() {
            switch (orientation) {
                case HORIZONTAL:
                    return new Dimension(3 * DEFAULT_WIDTH, DEFAULT_HEIGHT);
                case TRIANGULAR:
                    return new Dimension(2 * DEFAULT_WIDTH, 2 * DEFAULT_HEIGHT);
                case VERTICAL:
                    return new Dimension(DEFAULT_WIDTH, 3 * DEFAULT_HEIGHT);
                default:
                    return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }


        }

        private JPanel wrapPlot(IImagePlot plot, JSlider slider) {
             JPanel panel = new JPanel();
                        panel.setLayout(new BorderLayout());
                        panel.add(plot.getComponent(), BorderLayout.CENTER);
                        panel.add(slider, BorderLayout.SOUTH);
            return panel;

        }

        public java.util.List<IImagePlot> layoutPlots() {
            plots = createPlots();
            getView().getContentPane().removeAll();
            switch (orientation) {
                case HORIZONTAL:
                    BoxLayout layout1 = new BoxLayout(getView().getContentPane(), BoxLayout.X_AXIS);
                    getView().getContentPane().setLayout(layout1);
                    getView().getContentPane().add(wrapPlot(plots.get(0), slider1.getSlider()));
                    getView().getContentPane().add(wrapPlot(plots.get(1), slider2.getSlider()));
                    getView().getContentPane().add(wrapPlot(plots.get(2), slider3.getSlider()));


                    break;

                case TRIANGULAR:
                    FormLayout layout2a = new FormLayout("p:grow(.85), p:grow(.15)", "p:g, 1dlu:g, p:g, 1dlu:g");
                    layout2a.setColumnGroups(new int[][]{{1, 2}});
                    getView().getContentPane().setLayout(layout2a);
                    CellConstraints cc = new CellConstraints();
                    getView().getContentPane().add(plots.get(0).getComponent(), cc.xywh(1, 1, 1, 4));
                    getView().getContentPane().add(plots.get(1).getComponent(), cc.xywh(2, 1, 1, 2));
                    getView().getContentPane().add(plots.get(2).getComponent(), cc.xywh(2, 3, 1, 2));
                    break;

                case VERTICAL:
                    BoxLayout layout3 = new BoxLayout(getView().getContentPane(), BoxLayout.Y_AXIS);
                    getView().getContentPane().setLayout(layout3);                      
                    getView().getContentPane().add(wrapPlot(plots.get(0), slider1.getSlider()));
                    getView().getContentPane().add(wrapPlot(plots.get(1), slider2.getSlider()));
                    getView().getContentPane().add(wrapPlot(plots.get(2), slider3.getSlider()));

                    break;


                default:
                    throw new AssertionError();
            }

            return plots;

        }

        public IImagePlot whichPlot(Point p) {

            for (IImagePlot plot : plots) {
                Point point = SwingUtilities.convertPoint(getView(), p, plot.getComponent());

                boolean inplot = !((point.x < 0) || (point.y < 0) || (point.x > plot.getComponent().getWidth()) || (point.y > plot.getComponent().getHeight()));

                if (inplot) {
                    return plot;
                }
            }

            return null;


        }

        public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
            Anatomy3D[] anatomy = displayAnatomy.getCanonicalOrthogonal();
            for (int i=0; i<anatomy.length; i++) {
                Anatomy3D danat = anatomy[i];
                AxisRange xrange = getView().getModel().getImageAxis(danat.XAXIS).getRange();
                AxisRange yrange = getView().getModel().getImageAxis(danat.YAXIS).getRange();
                ViewBounds vb = new ViewBounds(getView().getModel().getImageSpace(), danat, xrange, yrange);
                sliderList.get(i).setDisplayAnatomy(danat);
                plots.get(i).setViewBounds(vb);

            }
        }

        public Anatomy3D getDisplayAnatomy() {
            return plots.get(0).getDisplayAnatomy();
        }
    }
}
