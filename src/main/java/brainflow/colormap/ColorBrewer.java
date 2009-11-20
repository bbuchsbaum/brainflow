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

    private static final int[] MagentaGreenHex = {0x8E0152, 0xC151B7D,0xDE77AE, 0xF1B6DA,0xF7F7F7,0xE6F5D0,0xB8E186,0x7FBC41,0x4D9221,0x276419};
    private static final int[] PurpleGreenHex = { 0x40004B, 0x762A83,0x9970AB,0xC2A5CF,0xE7D4E8,0xF7F7F7,0xD9F0D3,0xA6DBA0,0x5AAE61,0x1B7837,0x00441B};
    private static final int[] OrangePurpleHex = { 0X7F3B08, 0XB35806, 0XE08214, 0XFDB863, 0XFEE0B6,0XF7F7F7, 0XD8DAEB,0XB2AB2D, 0X8073AC,0X542788,0X2D004B};
    private static final int[] RedBlueHex = { 0X67001F, 0XB2182B, 0XD6604D,0XF4A582,0XFDDBC7,0XF7F7F7,0XD1E550,0X92C5DE,0X4393C3,0X2166AC,0X053061};
    private static final int[] RedYellowGreenHex = { 0XA50026, 0XD73027, 0XF46D43,0XFDAE61,0XFEE08B,0XFFFFBF,0XD9EF8B,0XA6D96A,0X66BD63,0X1A9850,0X006837};

    public static final IndexColorModel YellowRed = ColorTable.resampleMap(createColorModel(YellowRedHex), 14);
    public static final IndexColorModel RedYellowBlue = ColorTable.resampleMap(createColorModel(RedYellowBlueHex), 14);
    public static final IndexColorModel YellowGreenBlue = ColorTable.resampleMap(createColorModel(YellowGreenBlueHex), 14);
    public static final IndexColorModel Purples = ColorTable.resampleMap(createColorModel(PurplesHex), 14);
    public static final IndexColorModel PurpleBlue = ColorTable.resampleMap(createColorModel(PurpleBlueHex), 14);
    public static final IndexColorModel Reds = ColorTable.resampleMap(createColorModel(RedsHex), 14);
    public static final IndexColorModel Greens = ColorTable.resampleMap(createColorModel(GreensHex), 14);
    public static final IndexColorModel BrownBlueGreen = ColorTable.resampleMap(createColorModel(BrBGHex), 14);
    public static final IndexColorModel MagentaGreen = ColorTable.resampleMap(createColorModel(MagentaGreenHex), 14);
    public static final IndexColorModel PurpleGreen = ColorTable.resampleMap(createColorModel(PurpleGreenHex), 14);
    public static final IndexColorModel OrangePurple = ColorTable.resampleMap(createColorModel(OrangePurpleHex), 14);
    public static final IndexColorModel RedBlue = ColorTable.resampleMap(createColorModel(RedBlueHex), 14);
    public static final IndexColorModel RedYellowGreen = ColorTable.resampleMap(createColorModel(RedYellowGreenHex), 14);

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


    

