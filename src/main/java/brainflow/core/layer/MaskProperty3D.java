package brainflow.core.layer;

import brainflow.image.data.*;
import brainflow.core.IClipRange;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 16, 2008
 * Time: 10:21:52 PM
 * To change this template use File | Settings | File Templates.
 */


public class MaskProperty3D implements IMaskProperty<IMaskedData3D> {


    private HashMap<MASK_KEY, IMaskedData3D> maskMap = new HashMap<MASK_KEY, IMaskedData3D>();


    private ImageLayer3D layer;

    private IMaskedData3D frozenMask;

    private String maskExpression = "";


    public MaskProperty3D(ImageLayer3D _layer) {
        layer = _layer;

    }

    private MaskProperty3D(ImageLayer3D _layer, HashMap<MASK_KEY, IMaskedData3D> map) {
        layer = _layer;
        maskMap = map;
    }

    private MaskProperty3D(ImageLayer3D _layer, HashMap<MASK_KEY, IMaskedData3D> map, String expression) {
        layer = _layer;
        maskMap = map;
        maskExpression = expression;
    }

    public MaskProperty3D copyMask(MASK_KEY key, IMaskedData3D mask) {
        HashMap<MASK_KEY, IMaskedData3D> newmap = (HashMap<MASK_KEY, IMaskedData3D>) maskMap.clone();
        newmap.put(key, mask);
        return new MaskProperty3D(layer, newmap, getMaskExpression());
    }

    public IMaskedData3D getMask(MASK_KEY key, IMaskedData3D mask) {
        return maskMap.get(key);
    }

    public String getMaskExpression() {
        return maskExpression;
    }

    public void setMaskExpression(String maskExpression) {
        this.maskExpression = maskExpression;
    }

    public boolean isOpaque() {
        return false;
    }

    public void reduce() {
        if (frozenMask == null) {
            frozenMask = new BinaryImageData3D(buildMask());
        }

    }

    public IMaskedData3D buildMask() {
        if (frozenMask != null) {
            return frozenMask;
        } else {


            final IClipRange range = layer.getImageLayerProperties().thresholdRange.get();

            MaskPredicate pred = new MaskPredicate() {
                public boolean mask(double value) {
                    return !range.contains(value);

                }
            };

            IMaskedData3D retmask = (IMaskedData3D) ImageData.createMask(pred, layer.getData());

            IMaskedData3D secondaryMask = maskMap.get(MASK_KEY.EXPRESSION_MASK);

            if (secondaryMask != null) {
                retmask = new BooleanMaskNode3D(retmask, secondaryMask);
            }

            return retmask;
        }
    }
}
