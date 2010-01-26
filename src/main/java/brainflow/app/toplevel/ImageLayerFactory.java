package brainflow.app.toplevel;

import brainflow.colormap.LinearColorMap;
import brainflow.image.io.IImageDataSource;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerProps;
import brainflow.utils.Range;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 5, 2009
 * Time: 11:18:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayerFactory {


    public static ImageLayer3D createImageLayer(IImageDataSource dataSource) {
        //double[] sortedVals = ImageData.sort(dataSource.getData());

        //double highclip = sortedVals[(int) (.98 * sortedVals.length)];
        //double lowclip = sortedVals[(int) (.02 * sortedVals.length)];

        LayerProps params = new LayerProps(
                new Range(dataSource.getData().minValue(),
                        dataSource.getData().maxValue()));

        params.colorMap.set(new LinearColorMap(dataSource.getData().minValue(),
                dataSource.getData().maxValue(),
                ResourceManager.getInstance().getDefaultColorMap()));

        //IClipRange clip = params.clipRange.get().newClipRange(dataSource.getData().minValue(), dataSource.getData().maxValue(), lowclip, highclip);
        //params.clipRange.set(clip);
        return new ImageLayer3D(dataSource, params);


    }
}
