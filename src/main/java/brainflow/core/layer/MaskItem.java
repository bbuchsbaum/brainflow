package brainflow.core.layer;

import brainflow.image.operations.Operations;
import brainflow.image.operations.BinaryOperation;
import brainflow.image.data.MaskPredicate;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.IMaskItem;
import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 10:45:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskItem extends Model implements IMaskItem {

    private ImageLayer source;

    private MaskPredicate predicate;

    private boolean active = true;

    private int group = 1;

    private BinaryOperation operation;


    public MaskItem(ImageLayer source, MaskPredicate predicate, int group) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        operation = Operations.AND;
    }

    public MaskItem(ImageLayer source, MaskPredicate predicate, int group, BinaryOperation operation) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        this.operation = operation;
    }

    public MaskItem(ImageLayer source, MaskPredicate predicate, int group, BinaryOperation operation, boolean active) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        this.operation = operation;
        this.active = active;
    }


    public BinaryOperation getOperation() {
        return operation;
    }

    public void setOperation(BinaryOperation operation) {
        BinaryOperation old = getOperation();
        this.operation = operation;
        firePropertyChange(IMaskItem.BINARY_OPERATION_PROPERTY, old, getOperation());
    }

    public ImageLayer getSource() {
        return source;
    }

    public void setSource(ImageLayer source) {
        AbstractLayer old = getSource();
        this.source = source;
        firePropertyChange(IMaskItem.SOURCE_IMAGE_PROPERTY, old, getSource());

    }

    public MaskPredicate getPredicate() {
        return predicate;
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean old = isActive();
        this.active = active;
        firePropertyChange(IMaskItem.ACTIVE_PROPERTY, old, isActive());
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        int old = getGroup();
        this.group = group;
        firePropertyChange(IMaskItem.GROUP_PROPERTY, old, getGroup());
    }
}
