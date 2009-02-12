package brainflow.application.toplevel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 5:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BrainFlowProjectListener {

    public void modelAdded(BrainFlowProjectEvent event);

    public void modelRemoved(BrainFlowProjectEvent event);

    public void intervalAdded(BrainFlowProjectEvent event);

    public void contentsChanged(BrainFlowProjectEvent event);

    public void intervalRemoved(BrainFlowProjectEvent event);
}
