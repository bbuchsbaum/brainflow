package brainflow.core;

import brainflow.image.io.IImageSource;
import brainflow.image.io.MemoryImageSource;
import brainflow.colormap.ColorTable;
import brainflow.colormap.DiscreteColorMap;
import brainflow.colormap.LinearColorMap;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import brainflow.image.io.BrainIO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 12, 2005
 * Time: 1:01:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SnapShooter {

    private ImageViewModel viewModel;

    private IImagePlot emulator;

    private Anatomy3D displayAnatomy;


    private int width;

    private int height;


    public SnapShooter(ImageViewModel _dset, Anatomy3D _displayAnatomy) {
        viewModel = _dset;
        setDisplayAnatomy(_displayAnatomy);

    }

    public void setDisplayAnatomy(Anatomy3D _displayAnatomy) {
        displayAnatomy = _displayAnatomy;

        emulator = new ComponentImagePlot(viewModel, new ViewBounds(viewModel.getImageSpace(), displayAnatomy, viewModel.getImageAxis(displayAnatomy.XAXIS).getRange(),
                viewModel.getImageAxis(displayAnatomy.YAXIS).getRange()));


        width = (int) viewModel.getImageAxis(displayAnatomy.XAXIS).getRange().getInterval() * 2;
        height = (int) viewModel.getImageAxis(displayAnatomy.YAXIS).getRange().getInterval() * 2;

    }


    public ImageIcon shootLayer(double slice, int layer, int width, int height) {
       return null;

    }

    public RenderedImage shoot(double slice) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage img = gf.createCompatibleImage(width, height);

        //procurer.setSlice(new BrainPoint1D(displayAnatomy.ZAXIS, cutPoint));
        //emulator.paint(img.createGraphics(), new Rectangle(0, 0, width, height));
        return img;
    }

    public void shootContinuouslyAndSave(String path, String prefix, double startSlice, double endSlice, double increment) {
        RenderedImage[] rimg = shootContinuously(startSlice, endSlice, increment);
        for (int i = 0; i < rimg.length; i++) {
            String num = "";
            if (i < 10) num = "00" + i;
            else if (i < 100) num = "0" + i;
            else num = num + i;

            String fname = path + "/" + prefix + "_" + num + ".png";

            //todo fixme
            assert false;
            //JAI.create("filestore", rimg[i], fname, "png");
        }
    }

    public RenderedImage[] shootContinuously(double startSlice, double endSlice, double increment) {
        AxisRange range = viewModel.getImageAxis(displayAnatomy.ZAXIS).getRange();
        if (!range.contains(startSlice) || (!range.contains(endSlice))) {
            throw new IllegalArgumentException("Illegal cutPoint range " + startSlice + " to " + endSlice);
        }

        List<RenderedImage> list = new ArrayList<RenderedImage>();
        double curSlice = startSlice;
        while (curSlice <= endSlice) {
            list.add(shoot(curSlice));
            curSlice += increment;
        }

        RenderedImage[] rimg = new RenderedImage[list.size()];
        list.toArray(rimg);

        return rimg;

    }

    public RenderedImage[] shootSlices(double[] slices) {
        RenderedImage[] rimg = new RenderedImage[slices.length];
        for (int i = 0; i < slices.length; i++) {
            rimg[i] = shoot(slices[i]);
        }

        return rimg;
    }

    public static Color getHSB(float h, float s, float b, int alpha) {
        Color clr = Color.getHSBColor(h,s,b);
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
    }



    public static void main(String[] args) {
        try {

            ImageDisplayModel dset = new ImageDisplayModel("snapper");
            IImageSource il1 = new MemoryImageSource(BrainIO.readAnalyzeImage("c:/DTI/slopes/ch2"));
            IImageSource il2 = new MemoryImageSource(BrainIO.readAnalyzeImage("c:/DTI/slopes/tAgeXDiag"));
            IImageSource il3 = new MemoryImageSource(BrainIO.readAnalyzeImage("c:/DTI/slopes/tAge.Schiz"));
            IImageSource il4 = new MemoryImageSource(BrainIO.readAnalyzeImage("c:/DTI/slopes/tAge.Norm"));
            IImageSource il5 = new MemoryImageSource(BrainIO.readAnalyzeImage("c:/DTI/slopes/bAge.Schiz"));
            IImageSource il6 = new MemoryImageSource(BrainIO.readAnalyzeImage("c:/DTI/slopes/bAge.Norm"));


            LinearColorMap lmap = new LinearColorMap(0, 221, ColorTable.GRAYSCALE);
            DiscreteColorMap ragged = new DiscreteColorMap(lmap);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*public static void main(String[] args) {
   try {

       ImageDisplayModel dset = new ImageDisplayModel("snapper");
       IImageDataSource il1 = new MemoryImage(BrainIO.readAnalyzeImage("/r/d5/despo/buchs/MSSM_TEMPLATE"));
       IImageDataSource il2 = new MemoryImage(BrainIO.readAnalyzeImage("/r/d5/despo/buchs/TDiagnosis_G75.midbrain.Covar"));

       LinearColorMapDeprecated lmap = new LinearColorMapDeprecated(0, 221, ColorTable.GRAYSCALE);
       RaggedColorMap ragged = new RaggedColorMap();
       ragged.extendHigher(1.65, new Color(0, 0, 0, 0));
       ragged.extendHigher(2.6, new Color(102, 255, 0, 150));
       ragged.extendHigher(3.35, new Color(255, 255, 0, 200));
       ragged.extendHigher(4, new Color(255, 102, 0, 225));
       ragged.extendHigher(20, new Color(255, 0, 51, 255));

       ragged.extendLower(-1.65, new Color(0, 0, 0, 0));
       ragged.extendLower(-2.6, new Color(0, 102, 255, 150));
       ragged.extendLower(-3.35, new Color(51, 0, 255, 200));
       ragged.extendLower(-4, new Color(102, 0, 255, 225));
       ragged.extendLower(-20, new Color(204, 51, 255, 255));

       
       ImageLayer layer1 = new ImageLayer(il1.getData());
       layer1.getLayerProps().getColorMap().setProperty(lmap);
       ImageLayer layer2 = new ImageLayer(il2.getData());
       layer2.getLayerProps().getColorMap().setProperty(ragged);

       dset.addLayer(layer1);
       dset.addLayer(layer2);


       SnapShooter shooter = new SnapShooter(dset, Anatomy3D.getCanonicalAxial());
       shooter.shootContinuouslyAndSave("/r/d5/despo/buchs/", "TDiagnosis_Axial", -45, 45, 4);


   } catch (Exception e) {
       e.printStackTrace();
   }

} */



