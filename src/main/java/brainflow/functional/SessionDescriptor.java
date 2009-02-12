package brainflow.functional;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 8, 2004
 * Time: 6:34:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessionDescriptor {


    private int numReps = 0;
    private double TR = 2;
    private int skipBegin = 0;
    private int skipEnd = 0;

    private String identifier;
    private int sessionNumber = -1;
    //ImageFormat imageFormat = ImageFormat.ANALYZE75;

    public SessionDescriptor(int _numReps, double _TR, String _identifier, int _skipBegin, int _skipEnd, int _sessionNumber) {
        assert numReps > 0;
        assert _TR > 0;

        numReps = _numReps;
        TR = _TR;
        identifier = _identifier;
        skipBegin = _skipBegin;
        skipEnd = _skipEnd;
        sessionNumber = _sessionNumber;

    }

    public Integer getSessionNumber() {
        return sessionNumber;
    }

    public Integer getNumReps() {
        return numReps;
    }

    public Double getTR() {
        return TR;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getSkipBegin() {
        return skipBegin;
    }

    public Integer getSkipEnd() {
        return skipEnd;
    }







}
