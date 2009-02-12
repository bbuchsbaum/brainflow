package brainflow.image.operations;

import brainflow.utils.NumberUtils;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 11:29:58 AM
 * To change this template use File | Settings | File Templates.
 */
public final class Operations {

    public static final BooleanOperation AND = new _AND_();

    public static final BooleanOperation OR = new _OR_();

    public static final BinaryOperation GT = new _GT_();

    public static final BinaryOperation GT_EQ = new _GTEQ_();

    public static final BinaryOperation LT = new _LT_();

    public static final BinaryOperation LT_EQ = new _LTEQ_();

    public static final BinaryOperation EQ = new _EQ_();



    public static BinaryOperation lookup(BinaryOperand op) {
        switch (op) {
            case AND:
                return AND;
            case EQ:
                return EQ;
            case GT:
                return GT;
            case GT_EQ:
                return GT_EQ;
            case LT:
                return LT;
            case LT_EQ:
                return LT_EQ;     
            case OR:
                return OR;
            default:
                throw new IllegalArgumentException("unsupported operand " + op);
        }
    }

    private static class _AND_ implements BooleanOperation {
        


        public final boolean isTrue(int left, int right) {
            return (left > 0 && right > 0);
        }

        public final boolean isTrue(double left, double right) {
            return (left > 0 && right > 0);
        }

        public boolean isTrue(boolean left, boolean right) {
            return left && right;
        }

        public BinaryOperand operand() {
            return BinaryOperand.AND;
        }

        public String toString() {
            return BinaryOperand.AND.toString();
        }
    }

    private static class _OR_ implements BooleanOperation {


        public boolean isTrue(boolean left, boolean right) {
            return left || right;
        }

        public boolean isTrue(double left, double right) {
            return (left > 0 || right > 0);
        }

        public boolean isTrue(int left, int right) {
            return (left > 0 || right > 0);
        }

        public BinaryOperand operand() {
            return BinaryOperand.OR;
        }

        public String toString() {
            return operand().toString();
        }
    }

    private static class _GT_ implements BinaryOperation {

        public boolean isTrue(double left, double right) {
            return (left - right) > 0;
        }

        public boolean isTrue(int left, int right) {
            return (left - right) > 0;
        }

        public BinaryOperand operand() {
            return BinaryOperand.GT;
        }


        public String toString() {
            return operand().toString();
        }

    }

    private static class _GTEQ_ implements BinaryOperation {
        public boolean isTrue(double left, double right) {
            return (left - right) >= 0;
        }

        public boolean isTrue(int left, int right) {
            return (left - right) >= 0;
        }

        public BinaryOperand operand() {
            return BinaryOperand.GT_EQ;
        }



        public String toString() {
            return operand().toString();
        }

    }

    private static class _LTEQ_ implements BinaryOperation {
        public boolean isTrue(double left, double right) {
            return (left - right) <= 0;
        }

        public boolean isTrue(int left, int right) {
            return (left - right) <= 0;
        }

        public BinaryOperand operand() {
            return BinaryOperand.LT_EQ;
        }



        public String toString() {
            return operand().toString();
        }

    }

    private static class _LT_ implements BinaryOperation {
        public boolean isTrue(double left, double right) {
             return (left - right) < 0;
        }

        public boolean isTrue(int left, int right) {
            return (left - right) < 0;
        }

        public BinaryOperand operand() {
            return BinaryOperand.LT;
        }


        public String toString() {
            return operand().toString();
        }

    }

    private static class _EQ_ implements BinaryOperation {

        public boolean isTrue(double left, double right) {
            return NumberUtils.equals(left, right, .0001);
        }

        public boolean isTrue(int left, int right) {
            return left == right;
        }

        public BinaryOperand operand() {
            return BinaryOperand.EQ;
        }



        public String toString() {
            return operand().toString();
        }

    }





}
