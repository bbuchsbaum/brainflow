package brainflow.core;

import brainflow.utils.IRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 1, 2008
 * Time: 9:52:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IClipRange extends IRange {

    public boolean contains(double val);

    public double getLowClip();

    public double getHighClip();
  
    public IRange getInnerRange();


    public IClipRange newClipRange(double min, double max, double lowclip, double highclip);
}
