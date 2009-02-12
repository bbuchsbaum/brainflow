package brainflow.core.layer;

import brainflow.image.data.MaskPredicate;
import brainflow.image.operations.BinaryOperation;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 16, 2007
 * Time: 2:52:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskItem {

    public static final String SOURCE_IMAGE_PROPERTY = "source";

    public static final String THRESHOLD_PREDICATE_PROPERTY = "predicate";

    public static final String BINARY_OPERATION_PROPERTY = "operation";

    public static final String GROUP_PROPERTY = "group";
    
    public static final String ACTIVE_PROPERTY = "active";

    public BinaryOperation getOperation();

    public void setOperation(BinaryOperation operation);

    public ImageLayer getSource();

    public void setSource(ImageLayer source);

    public MaskPredicate getPredicate();

    //public void setPredicate(ThresholdRange predicate);

    public boolean isActive();

    public void setActive(boolean active);

    public int getGroup();

    public void setGroup(int group);
}
