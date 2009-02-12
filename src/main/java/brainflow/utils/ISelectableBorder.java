package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 2, 2006
 * Time: 11:58:58 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ISelectableBorder {


    public void setSelected(boolean b);

    public void setPreSelected(boolean b);

    public boolean isPreSelected();

    public boolean isSelected();

}
