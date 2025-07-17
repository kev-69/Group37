package structures;

/**
 * Custom Queue implementation using a circular array
 * 
 * @param <T> the type of elements stored in this queue
 */
public class MyQueue<T> {
    private T[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public MyQueue() {
        this(100); // Default capacity
    }

    /**
     * Add an element to the rear of the queue
     */
    public boolean enqueue(T item) {
        if (isFull()) {
            return false; // Queue is full
        }

        rear = (rear + 1) % capacity;
        queue[rear] = item;
        size++;
        return true;
    }

    /**
     * Remove and return the element at the front of the queue
     */
    public T dequeue() {
        if (isEmpty()) {
            return null; // Queue is empty
        }

        T item = queue[front];
        queue[front] = null; // Help GC
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    /**
     * Return the element at the front without removing it
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[front];
    }

    /**
     * Return the element at the rear without removing it
     */
    public T peekRear() {
        if (isEmpty()) {
            return null;
        }
        return queue[rear];
    }

    /**
     * Check if the queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Check if the queue is full
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * Get the current size of the queue
     */
    public int size() {
        return size;
    }

    /**
     * Get the capacity of the queue
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Clear all elements from the queue
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            queue[i] = null;
        }
        front = 0;
        rear = -1;
        size = 0;
    }

    /**
     * Check if the queue contains a specific element
     */
    public boolean contains(T item) {
        if (isEmpty()) {
            return false;
        }

        int current = front;
        for (int i = 0; i < size; i++) {
            if ((queue[current] == null && item == null) ||
                    (queue[current] != null && queue[current].equals(item))) {
                return true;
            }
            current = (current + 1) % capacity;
        }
        return false;
    }

    /**
     * Convert queue to array (front to rear order)
     */
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[size];

        int current = front;
        for (int i = 0; i < size; i++) {
            array[i] = queue[current];
            current = (current + 1) % capacity;
        }
        return array;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Queue: []";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Queue: [");

        int current = front;
        for (int i = 0; i < size; i++) {
            sb.append(queue[current]);
            if (i < size - 1) {
                sb.append(", ");
            }
            current = (current + 1) % capacity;
        }
        sb.append("]");
        return sb.toString();
    }
}
