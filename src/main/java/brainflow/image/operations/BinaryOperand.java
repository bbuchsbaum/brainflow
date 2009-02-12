package brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 6, 2007
 * Time: 11:25:06 PM
 * To change this template use File | Settings | File Templates.
 */
public enum BinaryOperand {

    AND("and"),
    OR("or"),
    GT(">"),
    LT("<"),
    EQ("=="),
    GT_EQ(">="),
    LT_EQ("<=");

    private String id;

    BinaryOperand(String _id) {
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
