package brainflow.core.mask;

import brainflow.image.data.IMaskedData3D;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 6:44:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdFunction implements IFunction<IMaskedData3D> {

    public int getNumArguments() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Class> getArgumentTypes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IMaskedData3D evaluate(List<INode> arguments) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IMaskedData3D evaluate() {
        return null;
    }

    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
