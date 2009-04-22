package brainflow.colormap;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2008
 * Time: 4:49:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorBrewer {



    private static final int[] YellowRedHex = { 0XFFFFB2,  0XFED976, 0XFEB24C, 0XFD8D3C, 0XFC4E2A, 0XE31A1C, 0XB10026 };
    private static final int[] RedYellowBlueHex = { 0xD73027, 0xFC8D59, 0xFEE090, 0xFFFFBF, 0xE0F3F8, 0x91BFDB, 0x4575B4 };
    private static final int[] YellowGreenBlueHex = { 0xFFFFCC, 0xC7E9B4, 0x7FCDBB, 0x41B6C4, 0x1D91C0, 0x225EA8, 0x0C2C84 };
    private static final int[] PurplesHex = { 0xF2F0F7, 0xDADAEB, 0xBCBDDC, 0x9E9AC8, 0x807DBA, 0x6A51A3, 0x4A1486 };
    private static final int[] PurpleBlueHex = { 0xF1EEF6, 0xD0D1E6, 0xA6BDDB, 0x74A9CF, 0x3690C0, 0x0570B0, 0x034E7B };
    private static final int[] RedsHex = { 0xFEE5D9, 0xFCBBA1, 0xFC9272, 0xFB6A4A, 0xEF3B2C, 0xCB181D, 0x99000D };
    private static final int[] GreensHex = { 0xEDF8E9, 0xC7E9C0, 0xA1D99B, 0x74C476, 0x41AB5D, 0x238B45, 0x005A32 };
    private static final int[] BrBGHex = { 0x8C510A, 0xD8B365, 0xF6E8C3, 0xF5F5F5, 0xC7EAE5, 0x5AB4AC, 0x01665E };

    public static final IndexColorModel YellowRed = ColorTable.resampleMap(createColorModel(YellowRedHex), 14);
    public static final IndexColorModel RedYellowBlue = ColorTable.resampleMap(createColorModel(RedYellowBlueHex), 14);
    public static final IndexColorModel YellowGreenBlue = ColorTable.resampleMap(createColorModel(YellowGreenBlueHex), 14);
    public static final IndexColorModel Purples = ColorTable.resampleMap(createColorModel(PurplesHex), 14);
    public static final IndexColorModel PurpleBlue = ColorTable.resampleMap(createColorModel(PurpleBlueHex), 14);
    public static final IndexColorModel Reds = ColorTable.resampleMap(createColorModel(RedsHex), 14);
    public static final IndexColorModel Greens = ColorTable.resampleMap(createColorModel(GreensHex), 14);
    public static final IndexColorModel BrownBlueGreen = ColorTable.resampleMap(createColorModel(BrBGHex), 14);

    public static final Map<String, IndexColorModel> maps = new HashMap<String, IndexColorModel>();

    



    public static IndexColorModel createColorModel(int[] hexvals) {
        byte[] red = new byte[hexvals.length];
        byte[] green = new byte[hexvals.length];
        byte[] blue = new byte[hexvals.length];

        for (int i=0; i<hexvals.length; i++) {
            Color c = new Color(hexvals[i]);
            red[i] = (byte)c.getRed();
            green[i] = (byte)c.getGreen();
            blue[i] = (byte)c.getBlue();
        }



        return new IndexColorModel(8, hexvals.length, red, green, blue);
    }
    
}


    

