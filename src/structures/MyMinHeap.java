package structures;

import java.util.Comparator;

/**
 * Custom Min Heap implementation using array-based binary heap
 * 
 * @param <T> the type of elements stored in this heap
 */
public class MyMinHeap<T> {
    private T[] heap;
    private int size;
    private int capacity;
    private Comparator<T> comparator;

    @SuppressWarnings("unchecked")
    public MyMinHeap(int capacity, Comparator<T> comparator) {
        this.capacity = capacity;
        this.heap = (T[]) new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public MyMinHeap(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Object[capacity];
        this.size = 0;
        this.comparator = null; // Will require Comparable elements
    }

    public MyMinHeap() {
        this(100); // Default capacity
    }

    /**
     * Insert an element into the heap
     */
    public boolean insert(T item) {
        if (size >= capacity) {
            resize();
        }

        heap[size] = item;
        heapifyUp(size);
        size++;
        return true;
    }

    /**
     * Remove and return the minimum element (root)
     */
    public T extractMin() {
        if (isEmpty()) {
            return null;
        }

        T min = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) {
            heapifyDown(0);
        }

        return min;
    }

    /**
     * Return the minimum element without removing it
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    /**
     * Remove a specific element from the heap
     */
    public boolean remove(T item) {
        int index = findIndex(item);
        if (index == -1) {
            return false;
        }

        // Replace with last element
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0 && index < size) {
            // Try heapify up first, then down
            int parent = (index - 1) / 2;
            if (index > 0 && compare(heap[index], heap[parent]) < 0) {
                heapifyUp(index);
            } else {
                heapifyDown(index);
            }
        }

        return true;
    }

    /**
     * Check if heap is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get current size of heap
     */
    public int size() {
        return size;
    }

    /**
     * Clear all elements from heap
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Check if heap contains an element
     */
    public boolean contains(T item) {
        return findIndex(item) != -1;
    }

    /**
     * Convert heap to array (not in sorted order)
     */
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[size];
        System.arraycopy(heap, 0, array, 0, size);
        return array;
    }

    /**
     * Heapify up from given index
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    /**
     * Heapify down from given index
     */
    private void heapifyDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size && compare(heap[leftChild], heap[smallest]) < 0) {
                smallest = leftChild;
            }

            if (rightChild < size && compare(heap[rightChild], heap[smallest]) < 0) {
                smallest = rightChild;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * Swap elements at two indices
     */
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Compare two elements using comparator or natural ordering
     */
    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<T>) a).compareTo(b);
        }
    }

    /**
     * Find index of an element in the heap
     */
    private int findIndex(T item) {
        for (int i = 0; i < size; i++) {
            if ((heap[i] == null && item == null) ||
                    (heap[i] != null && heap[i].equals(item))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Resize the heap when capacity is exceeded
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        T[] newHeap = (T[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, capacity);
        heap = newHeap;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "MinHeap: []";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("MinHeap: [");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
