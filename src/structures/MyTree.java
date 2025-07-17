package structures;

import java.util.Comparator;

/**
 * Custom Binary Search Tree implementation
 * 
 * @param <T> the type of elements stored in this tree
 */
public class MyTree<T> {
    private TreeNode<T> root;
    private int size;
    private Comparator<T> comparator;

    /**
     * Node class for the binary search tree
     */
    private static class TreeNode<T> {
        T data;
        TreeNode<T> left;
        TreeNode<T> right;

        TreeNode(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public MyTree(Comparator<T> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;
    }

    public MyTree() {
        this.root = null;
        this.size = 0;
        this.comparator = null; // Will require Comparable elements
    }

    /**
     * Insert an element into the tree
     */
    public boolean insert(T item) {
        if (root == null) {
            root = new TreeNode<>(item);
            size++;
            return true;
        }

        return insertRecursive(root, item);
    }

    private boolean insertRecursive(TreeNode<T> node, T item) {
        int comparison = compare(item, node.data);

        if (comparison < 0) {
            if (node.left == null) {
                node.left = new TreeNode<>(item);
                size++;
                return true;
            } else {
                return insertRecursive(node.left, item);
            }
        } else if (comparison > 0) {
            if (node.right == null) {
                node.right = new TreeNode<>(item);
                size++;
                return true;
            } else {
                return insertRecursive(node.right, item);
            }
        } else {
            // Duplicate values not allowed
            return false;
        }
    }

    /**
     * Search for an element in the tree
     */
    public boolean contains(T item) {
        return search(root, item) != null;
    }

    /**
     * Find and return an element from the tree
     */
    public T find(T item) {
        TreeNode<T> node = search(root, item);
        return node != null ? node.data : null;
    }

    private TreeNode<T> search(TreeNode<T> node, T item) {
        if (node == null) {
            return null;
        }

        int comparison = compare(item, node.data);

        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return search(node.left, item);
        } else {
            return search(node.right, item);
        }
    }

    /**
     * Remove an element from the tree
     */
    public boolean remove(T item) {
        if (root == null) {
            return false;
        }

        TreeNode<T> parent = null;
        TreeNode<T> current = root;

        // Find the node to remove and its parent
        while (current != null) {
            int comparison = compare(item, current.data);

            if (comparison == 0) {
                break; // Found the node
            } else if (comparison < 0) {
                parent = current;
                current = current.left;
            } else {
                parent = current;
                current = current.right;
            }
        }

        if (current == null) {
            return false; // Node not found
        }

        // Case 1: Node has no children (leaf node)
        if (current.left == null && current.right == null) {
            if (parent == null) {
                root = null; // Removing root
            } else if (parent.left == current) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }
        // Case 2: Node has one child
        else if (current.left == null) {
            if (parent == null) {
                root = current.right;
            } else if (parent.left == current) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        } else if (current.right == null) {
            if (parent == null) {
                root = current.left;
            } else if (parent.left == current) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        }
        // Case 3: Node has two children
        else {
            // Find inorder successor (smallest node in right subtree)
            TreeNode<T> successor = findMin(current.right);
            T successorData = successor.data;

            // Remove the successor (which has at most one child)
            remove(successorData);

            // Replace current node's data with successor's data
            current.data = successorData;
            return true; // Early return to avoid decrementing size twice
        }

        size--;
        return true;
    }

    /**
     * Find the minimum element in the tree
     */
    public T findMin() {
        if (root == null) {
            return null;
        }
        return findMin(root).data;
    }

    private TreeNode<T> findMin(TreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Find the maximum element in the tree
     */
    public T findMax() {
        if (root == null) {
            return null;
        }
        return findMax(root).data;
    }

    private TreeNode<T> findMax(TreeNode<T> node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    /**
     * Get the size of the tree
     */
    public int size() {
        return size;
    }

    /**
     * Check if tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Clear all elements from the tree
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Get inorder traversal of the tree (sorted order)
     */
    public T[] inorderTraversal() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[size];
        inorderRecursive(root, result, new int[] { 0 });
        return result;
    }

    private void inorderRecursive(TreeNode<T> node, T[] result, int[] index) {
        if (node != null) {
            inorderRecursive(node.left, result, index);
            result[index[0]++] = node.data;
            inorderRecursive(node.right, result, index);
        }
    }

    /**
     * Get preorder traversal of the tree
     */
    public T[] preorderTraversal() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[size];
        preorderRecursive(root, result, new int[] { 0 });
        return result;
    }

    private void preorderRecursive(TreeNode<T> node, T[] result, int[] index) {
        if (node != null) {
            result[index[0]++] = node.data;
            preorderRecursive(node.left, result, index);
            preorderRecursive(node.right, result, index);
        }
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
     * Get the height of the tree
     */
    public int height() {
        return height(root);
    }

    private int height(TreeNode<T> node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Tree: []";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Tree (inorder): [");
        T[] inorder = inorderTraversal();
        for (int i = 0; i < inorder.length; i++) {
            sb.append(inorder[i]);
            if (i < inorder.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
