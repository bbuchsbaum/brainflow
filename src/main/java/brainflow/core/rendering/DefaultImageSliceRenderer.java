package brainflow.core.rendering;

import brainflow.colormap.IColorMap;
import brainflow.core.SliceRenderer;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerProps;
import brainflow.display.InterpolationType;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.GridLoc1D;
import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.data.IImageData2D;
import brainflow.image.data.RGBAImage;
import brainflow.image.data.UByteImageData2D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import brainflow.image.interpolation.TrilinearInterpolator;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.operations.ImageSlicer;
import brainflow.image.rendering.PixelUtils;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.IImageSpace2D;
import brainflow.image.space.IImageSpace3D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 7:34:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultImageSliceRenderer implements SliceRenderer {

    private static final Logger log = Logger.getLogger(DefaultImageSliceRenderer.class.getName());

    private GridPoint3D slice;

    private ImageLayer3D layer;

    private ImageSlicer slicer;

    private IImageData2D data;

    private RGBAImage rgbaImage;

    private RGBAImage thresholdedRGBAImage;

    private BufferedImage rawImage;

    private BufferedImage smoothedImage;

    private BufferedImage resampledImage;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private IImageSpace3D refSpace;

    private IColorMap lastColorMap;


    public DefaultImageSliceRenderer(IImageSpace3D refSpace, ImageLayer3D layer, GridPoint3D slice) {
        //todo not DRY
        this.slice = slice;
        this.layer = layer;
        this.refSpace = refSpace;

        if (layer.getLayerProps().getInterpolation() == InterpolationType.NEAREST_NEIGHBOR) {
            slicer = ImageSlicer.createSlicer(refSpace, layer.getData(), new NearestNeighborInterpolator());
        } else {
            slicer = ImageSlicer.createSlicer(refSpace, layer.getData(), new TrilinearInterpolator());
        }

    }


    public DefaultImageSliceRenderer(DefaultImageSliceRenderer renderer, GridPoint3D slice) {
        //todo not DRY
        this.slice = slice;
        this.layer = renderer.layer;
        this.refSpace = renderer.refSpace;
        this.displayAnatomy = renderer.displayAnatomy;
        this.lastColorMap = renderer.lastColorMap;

        if (layer.getLayerProps().getInterpolation() == InterpolationType.NEAREST_NEIGHBOR) {
            slicer = ImageSlicer.createSlicer(refSpace, layer.getData(), new NearestNeighborInterpolator());
        } else {
            slicer = ImageSlicer.createSlicer(refSpace, layer.getData(), new TrilinearInterpolator());
        }


    }


    public DefaultImageSliceRenderer(IImageSpace3D refSpace, ImageLayer3D layer, GridPoint3D slice, Anatomy3D displayAnatomy) {
        this.slice = slice;
        this.layer = layer;
        this.refSpace = refSpace;
        this.displayAnatomy = displayAnatomy;
        slicer = ImageSlicer.createSlicer(refSpace, layer.getData());


    }


    public IImageSpace3D getReferenceSpace() {
        return refSpace;
    }

    public IImageSpace2D getImageSpace() {
        return getData().getImageSpace();
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }


    private IImageData2D getData() {
        if (data != null) return data;


        int slice = getZSlice();

        //int cutPoint = (int) Math.round(zdisp.getValue());
        return slicer.getSlice(getDisplayAnatomy(), slice);


    }

    private int getZSlice() {

        GridLoc1D zdisp = slice.getValue(displayAnatomy.ZAXIS, false);
        int slice = (int) (zdisp.getValue() -.5f);

        if (slice >= refSpace.getDimension(displayAnatomy.ZAXIS)) {
            slice = refSpace.getDimension(displayAnatomy.ZAXIS) - 1;
        } else if (slice < 0) {
            slice = 0;
        }

        return slice;


    }


    private RGBAImage getRGBAImage() {
        if (rgbaImage != null) {
            //System.out.println("rgba image isn't even null");
            return rgbaImage;
        }

        IColorMap colorMap = layer.getLayerProps().colorMap.get();

        lastColorMap = colorMap;
        return colorMap.getRGBAImage(getData());

    }

    private RGBAImage getThresholdedRGBAImage() {
        if (thresholdedRGBAImage != null) return thresholdedRGBAImage;

        thresholdedRGBAImage = thresholdRGBA(getRGBAImage());
        return thresholdedRGBAImage;
    }

    private BufferedImage getRawImage() {
        if (rawImage != null) return rawImage;

        rawImage = createBufferedImage(getThresholdedRGBAImage());
        return rawImage;
    }

    private BufferedImage getSmoothedImage() {
        if (smoothedImage != null) return smoothedImage;
        smoothedImage = this.smooth(getRawImage());

        return smoothedImage;

    }

    private BufferedImage getResampledImage() {
        if (resampledImage != null) return resampledImage;

        resampledImage = this.resample(getSmoothedImage());
        //resampledImage = this.convolveAlpha(resampledImage);
        return resampledImage;

    }

    @Override
    public void setSlice(GridPoint3D slice) {
        if (!getSlice().equals(slice)) {
            this.slice = slice;
            flush();
        }

    }

    public BufferedImage render() {
        return getResampledImage();
    }

    public void renderUnto(Rectangle2D frame, Graphics2D g2) {
        IImageSpace space = getImageSpace();
        double minx = space.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
        double miny = space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();

        double transx = (minx - frame.getMinX()); //+ (-frameBounds.getMinX());
        double transy = (miny - frame.getMinY()); //+ (-frameBounds.getMinY());


        Composite oldComposite = g2.getComposite();
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) layer.getLayerProps().opacity.get().doubleValue());
        g2.setComposite(composite);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.drawRenderedImage(render(), AffineTransform.getTranslateInstance(transx, transy));
        g2.setComposite(oldComposite);

    }

    public ImageLayer getLayer() {
        return layer;
    }

    public boolean isVisible() {
        return true;
    }

    public GridPoint3D getSlice() {
        return slice;
    }

    public void flush() {
        data = null;
        rawImage = null;
        rgbaImage = null;
        thresholdedRGBAImage = null;
        smoothedImage = null;
        resampledImage = null;
    }

    private BufferedImage smooth(BufferedImage source) {
        LayerProps dprops = layer.getLayerProps();

        double radius = dprops.smoothingRadius.get();
        if (radius < .01) return source;

        IImageSpace2D ispace = getData().getImageSpace();
        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        Kernel kern = PixelUtils.makeKernel((float) radius, (float) sx, (float) sy);

        ConvolveOp cop = new ConvolveOp(kern);
        return cop.filter(source, null);

    }

    private BufferedImage resample(BufferedImage source) {

        LayerProps dprops = layer.getLayerProps();
        InterpolationType interp = dprops.getInterpolation();
        IImageSpace2D ispace = getData().getImageSpace();

        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        // if (!source.isAlphaPremultiplied()) {
        //PremultiplyFilter filter = new PremultiplyFilter();
        //source = filter.filter(source, null);
        //  }


        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.scale(sx, sy);
        AffineTransformOp aop = null;


        if (interp == InterpolationType.NEAREST_NEIGHBOR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        } else if (interp == InterpolationType.CUBIC) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        } else if (interp == InterpolationType.LINEAR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        } else {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }


        BufferedImage ret = aop.filter(source, null);

        //if (ret.isAlphaPremultiplied()) {
        //log.finest("unpremultiplying alpha prior to resize");
        //UnpremultiplyFilter ufilter = new UnpremultiplyFilter();
        //ret = ufilter.filter(ret, null);

        //}

        return ret;


    }

    private BufferedImage createBufferedImage(RGBAImage rgba) {
        BufferedImage bimg = rgba.getAsBufferedImage();
        return bimg;
    }


    protected RGBAImage thresholdRGBA(RGBAImage rgba) {
        //StopWatch watch = new StopWatch();

        if (layer.getMaskProperty().isOpaque()) {
            return rgba;
        }



        ImageSlicer slicer = ImageSlicer.createSlicer(refSpace, layer.getMaskProperty().buildMask());

        int slice = getZSlice();

        //todo what is the correct way to round zdisp  here?
        // todo check if zdisp is valid?

    
        IImageData2D maskData = slicer.getSlice(getDisplayAnatomy(), slice);
        UByteImageData2D alpha = rgba.getAlpha();
        UByteImageData2D out = new UByteImageData2D(alpha.getImageSpace());

        ValueIterator sourceIter = alpha.valueIterator();
        ValueIterator maskIter = maskData.valueIterator();

        while (sourceIter.hasNext()) {
            int index = sourceIter.index();
            double a = sourceIter.next();
            double b = maskIter.next();

            out.set(index, (byte) (a * b));
        }

        RGBAImage ret = new RGBAImage(rgba.getSource(), rgba.getRed(), rgba.getGreen(), rgba.getBlue(), out);
        return ret;

    }
}