package brainflow.application.toplevel;

import brainflow.core.*;
import brainflow.core.annotations.CrosshairAnnotation;
import brainflow.core.annotations.SelectedPlotAnnotation;
import brainflow.core.annotations.SliceAnnotation;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import brainflow.utils.StringGenerator;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:31:37 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageViewFactory {

    private static final Logger log = Logger.getLogger(ImageViewFactory.class.getCanonicalName());


    public static IImagePlot createAxialPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalAxial());
    }

    public static IImagePlot createCoronalPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalCoronal());
    }

    public static IImagePlot createSagittalPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalSagittal());
    }

    public static void addDefaultAnnotations(ImageView view) {
        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(view.cursorPos);
        view.setAnnotation(CrosshairAnnotation.ID, crosshairAnnotation);
        view.setAnnotation(SelectedPlotAnnotation.ID, new SelectedPlotAnnotation(view));
        view.setAnnotation(SliceAnnotation.ID, new SliceAnnotation());
    }


    public static IImagePlot createPlot(IImageDisplayModel displayModel, Anatomy3D displayAnatomy) {
        AxisRange xrange = displayModel.getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = displayModel.getImageAxis(displayAnatomy.YAXIS).getRange();
        ViewBounds vb = new ViewBounds(displayAnatomy, xrange, yrange);

        return new ComponentImagePlot(displayModel, vb);

    }


    public static ImageView createOrthogonalView(ImageView source, OrthoPlotLayout.ORIENTATION orientation) {
        ImageView view = new ImageView(source.getModel());
        view.initPlotLayout(new OrthoPlotLayout(view,  orientation));
        addDefaultAnnotations(view);

        return view;

    }

    public static ImageView createYokedAxialView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalAxial());
        //view.initPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalAxial()));

        addDefaultAnnotations(view);

        return view;
    }

    public static ImageView createYokedCoronalView(ImageView source) {
        ImageView view = new ImageView(source.getModel());
        view.initPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalCoronal()));
        DisplayManager.getInstance().yoke(source, view);
        return view;
    }


    public static ImageView createYokedSagittalView(ImageView source) {
        ImageView view = new ImageView(source.getModel());
        view.initPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalSagittal()));
        DisplayManager.getInstance().yoke(source, view);
        return view;

    }


    public static ImageView createAxialView(IImageDisplayModel displayModel) {
        ImageView view = new SimpleImageView(displayModel, Anatomy3D.getCanonicalAxial());
        addDefaultAnnotations(view);

        /*ActionCommand aspectCommand = new SetPreserveAspectCommand(view);
        aspectCommand.bind(view);
        CommandGroup viewGroup = new CommandGroup("image-view-menu");
        viewGroup.bind(view);
        PopupAdapter adapter = new PopupAdapter(view, viewGroup.createPopupMenu());
        */

        return view;
    }



    public static ImageView createMontageView(IImageDisplayModel displayModel, int nrows, int ncols, double sliceGap) {
        MontageImageView view = new MontageImageView(displayModel, Anatomy3D.getCanonicalAxial(), nrows, ncols, sliceGap);

        return view;
    }



    public static ImageView createSagittalView(ImageView source) {
        ImageView view = new ImageView(source.getModel());
        view.initPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalSagittal()));
        view.cursorPos.set(source.cursorPos.get());
        addDefaultAnnotations(view);
        return view;

    }

    public static ImageView createCoronalView(ImageView source) {
        ImageView view = new ImageView(source.getModel());
        view.initPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalCoronal()));
        addDefaultAnnotations(view);
        return view;


    }

    public static ImageView createAxialView(ImageView source) {
        ImageView view = new ImageView(source.getModel());
        view.initPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalAxial()));
        addDefaultAnnotations(view);
        return view;


    }


    


    public static class ImageViewTitleGenerator implements StringGenerator {

        private ImageView view;

        public ImageViewTitleGenerator(ImageView _view) {
            view = _view;

        }

        public String getString() {
            if (view != null) {
                return "[" + view.toString() + "] " + view.getModel().getName();
            }

            return null;

        }
    }


}
