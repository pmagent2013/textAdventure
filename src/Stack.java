public class Stack {


    //
    // Public
    //
    public Stack() {
        init();
    }

    public void push(int item) throws Exception{
        // Check for stack overflow.
        if (topPtr > 0) {
            topPtr = topPtr - 1;
            arr[topPtr] = item;
        } else {
            Exception overflow = new Exception("Stack Overflow");
            throw overflow;
        }
    }

    public int pop() {
        int retVal = 0;
        // Check for stack underflow.
        if (topPtr < CAPACITY) {
            retVal = arr[topPtr];
            topPtr = topPtr + 1;
        } else {
            retVal = -1;
            Exception underflow = new Exception("Stack Underflow");
        }
        return retVal;
    }

    public boolean isEmpty() {
        boolean retVal = false;
        if (topPtr == CAPACITY) {
            retVal = true;
        }
        return retVal;
    }

    public boolean isEmptyMo() {
        return (topPtr == CAPACITY);
    }


    //
    // Private
    //
    private final int CAPACITY = 1000;
    private int[] arr = new int[CAPACITY];
    private int topPtr = 0;

    private void init() {
        for (int i = 0; i < CAPACITY; i++) {
            arr[i] = 0;
        }
        topPtr = CAPACITY;
    }


}
