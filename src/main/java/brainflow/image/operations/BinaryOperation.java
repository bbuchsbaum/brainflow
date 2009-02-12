package brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 13, 2008
 * Time: 9:15:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BinaryOperation {

    public boolean isTrue(double left, double right);

    public boolean isTrue(int left, int right);

    public BinaryOperand operand();

}
