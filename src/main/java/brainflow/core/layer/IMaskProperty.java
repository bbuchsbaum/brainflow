package brainflow.core.layer;

import brainflow.image.data.IMaskedData;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 14, 2008
 * Time: 10:36:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskProperty<T extends IMaskedData> {

    public enum MASK_KEY {
        PRIMARY_MASK,
        EXPRESSION_MASK,
        CLUSTER_MASK
    }


    public IMaskProperty<T> setMask(IMaskProperty.MASK_KEY key, T mask );

    public T getMask(IMaskProperty.MASK_KEY key, T mask );

    public T buildMask();

    public void reduce();
    
    public boolean isOpaque();


}
