package brainflow.app.toplevel;

import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.annotations.CrosshairAnnotation;
import brainflow.core.annotations.SelectedPlotAnnotation;
import brainflow.core.annotations.SliceAnnotation;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import brainflow.image.io.IImageDataSource;
import brainflow.utils.StringGenerator;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:31:37 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageViewFactory {

    private static final Logger log = Logger.getLogger(ImageViewFactory.class.getCanonicalName());


    public static ImageViewModel createModel(String name, IImageDataSource dataSource) {
        List<ImageLayer3D> layers = new ArrayList<ImageLayer3D>();
        layers.add(ImageLayerFactory.createImageLayer(dataSource));
        return new ImageViewModel(name, layers);
    }


    public static IImagePlot createAxialPlot(ImageViewModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalAxial());
    }

    public static IImagePlot createCoronalPlot(ImageView view) {
        return ImageViewFactory.createPlot(view.getModel(), Anatomy3D.getCanonicalCoronal());
    }

    public static IImagePlot createSagittalPlot(ImageViewModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalSagittal());
    }

    public static void addDefaultAnnotations(ImageView view) {
        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(view.cursorPos, view);
        view.setAnnotation(CrosshairAnnotation.ID, crosshairAnnotation);

        view.setAnnotation(SliceAnnotation.ID, new SliceAnnotation());
        if (view.getPlotLayout().getNumPlots() > 1)
            view.setAnnotation(SelectedPlotAnnotation.ID, new SelectedPlotAnnotation(view));
    }


    public static IImagePlot createPlot(ImageViewModel displayModel, Anatomy3D displayAnatomy) {
        AxisRange xrange = displayModel.getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = displayModel.getImageAxis(displayAnatomy.YAXIS).getRange();
        ViewBounds vb = new ViewBounds(displayAnatomy, xrange, yrange);

        return new ComponentImagePlot(displayModel, vb);

    }


    public static ImageView createOrthogonalView(ImageView source, OrthoPlotLayout.ORIENTATION orientation) {
        ImageView view = new OrthoImageView(source.getModel(), orientation);
        addDefaultAnnotations(view);

        return view;

    }

    public static ImageView createYokedAxialView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalAxial());
        //view.resetPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalAxial()));

        addDefaultAnnotations(view);

        return view;
    }

    public static ImageView createYokedCoronalView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalCoronal());
        DisplayManager.get().yoke(source, view);
        return view;
    }


    public static ImageView createYokedSagittalView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalSagittal());
        DisplayManager.get().yoke(source, view);
        return view;

    }


    public static ImageView createAxialView(ImageViewModel displayModel) {
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



    public static ImageView createMontageView(ImageViewModel displayModel, int nrows, int ncols, float sliceGap) {
        ImageView view = new MontageImageView(displayModel, Anatomy3D.getCanonicalAxial(), nrows, ncols, sliceGap);
        addDefaultAnnotations(view);
        return view;

    }



    public static ImageView createSagittalView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalSagittal());
        view.cursorPos.set(source.cursorPos.get());
        addDefaultAnnotations(view);
        return view;

    }

    public static ImageView createCoronalView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalCoronal());
        addDefaultAnnotations(view);
        return view;


    }

    public static ImageView createAxialView(ImageView source) {
        ImageView view = new SimpleImageView(source.getModel(), Anatomy3D.getCanonicalAxial());
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
