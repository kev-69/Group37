package structures;

/**
 * Custom Stack implementation using dynamic array
 * 
 * @param <T> the type of elements stored in this stack
 */
public class MyStack<T> {
    private T[] stack;
    private int top;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyStack(int capacity) {
        this.capacity = capacity;
        this.stack = (T[]) new Object[capacity];
        this.top = -1;
    }

    public MyStack() {
        this(100); // Default capacity
    }

    /**
     * Push an element onto the stack
     */
    public boolean push(T item) {
        if (isFull()) {
            // Resize the stack if it's full
            resize();
        }

        stack[++top] = item;
        return true;
    }

    /**
     * Pop and return the top element from the stack
     */
    public T pop() {
        if (isEmpty()) {
            return null; // Stack is empty
        }

        T item = stack[top];
        stack[top] = null; // Help GC
        top--;
        return item;
    }

    /**
     * Return the top element without removing it
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return stack[top];
    }

    /**
     * Check if the stack is empty
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Check if the stack is full
     */
    public boolean isFull() {
        return top == capacity - 1;
    }

    /**
     * Get the current size of the stack
     */
    public int size() {
        return top + 1;
    }

    /**
     * Get the capacity of the stack
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Clear all elements from the stack
     */
    public void clear() {
        for (int i = 0; i <= top; i++) {
            stack[i] = null;
        }
        top = -1;
    }

    /**
     * Check if the stack contains a specific element
     */
    public boolean contains(T item) {
        for (int i = 0; i <= top; i++) {
            if ((stack[i] == null && item == null) ||
                    (stack[i] != null && stack[i].equals(item))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resize the stack when it's full
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        T[] newStack = (T[]) new Object[newCapacity];

        System.arraycopy(stack, 0, newStack, 0, capacity);
        stack = newStack;
        capacity = newCapacity;
    }

    /**
     * Convert stack to array (bottom to top order)
     */
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[size()];

        System.arraycopy(stack, 0, array, 0, size());
        return array;
    }

    /**
     * Get element at specific index (0 is bottom, top is size()-1)
     */
    public T get(int index) {
        if (index < 0 || index > top) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        return stack[index];
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Stack: []";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Stack: [");

        for (int i = 0; i <= top; i++) {
            sb.append(stack[i]);
            if (i < top) {
                sb.append(", ");
            }
        }
        sb.append("] (top)");
        return sb.toString();
    }
}
