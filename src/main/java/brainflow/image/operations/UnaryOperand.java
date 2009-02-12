package brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 6, 2008
 * Time: 8:58:48 PM
 * To change this template use File | Settings | File Templates.
 */
public enum UnaryOperand {

    NEGATE("-"),
    ADD("+");



    private String id;

    UnaryOperand(String _id) {
        id = _id;
    }

    public String getID() {
        return id;
    }


    @Override
    public String toString() {
        return getID();
    }
}
