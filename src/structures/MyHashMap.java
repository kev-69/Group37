package structures;

/**
 * Custom HashMap implementation using separate chaining for collision
 * resolution
 * 
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private int size;
    private int capacity;

    /**
     * Node class for storing key-value pairs in linked list
     */
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Node[capacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity) {
        this.capacity = capacity;
        this.buckets = new Node[capacity];
        this.size = 0;
    }

    /**
     * Hash function to determine bucket index
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Put a key-value pair into the map
     */
    public V put(K key, V value) {
        int index = hash(key);
        Node<K, V> head = buckets[index];

        // If bucket is empty, create new node
        if (head == null) {
            buckets[index] = new Node<>(key, value);
            size++;
        } else {
            // Search for existing key
            Node<K, V> current = head;
            while (current != null) {
                if ((current.key == null && key == null) ||
                        (current.key != null && current.key.equals(key))) {
                    V oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            // Add new node at the end
            current.next = new Node<>(key, value);
            size++;
        }

        // Resize if load factor exceeded
        if ((double) size / capacity > LOAD_FACTOR) {
            resize();
        }

        return null;
    }

    /**
     * Get value by key
     */
    public V get(K key) {
        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Remove a key-value pair
     */
    public V remove(K key) {
        int index = hash(key);
        Node<K, V> head = buckets[index];

        if (head == null) {
            return null;
        }

        // If head node contains the key
        if ((head.key == null && key == null) ||
                (head.key != null && head.key.equals(key))) {
            buckets[index] = head.next;
            size--;
            return head.value;
        }

        // Search in the rest of the chain
        Node<K, V> current = head;
        while (current.next != null) {
            if ((current.next.key == null && key == null) ||
                    (current.next.key != null && current.next.key.equals(key))) {
                V value = current.next.value;
                current.next = current.next.next;
                size--;
                return value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Check if map contains a key
     */
    public boolean containsKey(K key) {
        return get(key) != null || (get(key) == null && keyExists(key));
    }

    /**
     * Helper method to check if a key exists (for null values)
     */
    private boolean keyExists(K key) {
        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Check if map is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get the size of the map
     */
    public int size() {
        return size;
    }

    /**
     * Clear all elements from the map
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    /**
     * Get all keys in the map
     */
    public Object[] keySet() {
        Object[] keys = new Object[size];
        int index = 0;

        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                keys[index++] = current.key;
                current = current.next;
            }
        }
        return keys;
    }

    /**
     * Get all values in the map
     */
    public Object[] values() {
        Object[] values = new Object[size];
        int index = 0;

        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                values[index++] = current.value;
                current = current.next;
            }
        }
        return values;
    }

    /**
     * Resize the hash table when load factor is exceeded
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        int oldCapacity = capacity;

        capacity *= 2;
        buckets = new Node[capacity];
        size = 0;

        // Rehash all existing elements
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> current = oldBuckets[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;

        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
