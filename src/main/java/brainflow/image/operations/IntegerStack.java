package brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 18, 2007
 * Time: 10:24:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerStack {


    private int[] stack;

    private int pushPos = 0;

    public IntegerStack(int maxSize) {
        stack = new int[maxSize];
    }

    public void push(int i) {
        System.out.println("pushing " + i);
        stack[pushPos] = i;
        pushPos++;

    }

    public int pop() {
        pushPos--;
        return stack[pushPos];
    }

    public int size() {
        return pushPos;
    }

    public boolean isEmpty() {
        return pushPos == 0;
    }


    public static void main(String[] args) {
        IntegerStack stack = new IntegerStack(100);
        stack.push(33);
        stack.push(44);
        System.out.println(stack.pop());
        stack.push(12);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());



    }

}
