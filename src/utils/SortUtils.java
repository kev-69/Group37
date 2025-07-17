package utils;

import java.util.Comparator;

/**
 * Utility class containing custom sorting algorithms
 */
public class SortUtils {

    /**
     * Insertion Sort implementation
     * Time Complexity: O(n²) worst case, O(n) best case
     * Space Complexity: O(1)
     */
    public static <T> void insertionSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }

        for (int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;

            // Move elements that are greater than key one position ahead
            while (j >= 0 && comparator.compare(array[j], key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    /**
     * Insertion Sort for Comparable objects
     */
    public static <T extends Comparable<T>> void insertionSort(T[] array) {
        insertionSort(array, (a, b) -> a.compareTo(b));
    }

    /**
     * Merge Sort implementation
     * Time Complexity: O(n log n) all cases
     * Space Complexity: O(n)
     */
    public static <T> void mergeSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }

        @SuppressWarnings("unchecked")
        T[] temp = (T[]) new Object[array.length];
        mergeSortHelper(array, temp, 0, array.length - 1, comparator);
    }

    private static <T> void mergeSortHelper(T[] array, T[] temp, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSortHelper(array, temp, left, mid, comparator);
            mergeSortHelper(array, temp, mid + 1, right, comparator);
            merge(array, temp, left, mid, right, comparator);
        }
    }

    private static <T> void merge(T[] array, T[] temp, int left, int mid, int right, Comparator<T> comparator) {
        // Copy elements to temp array
        System.arraycopy(array, left, temp, left, right - left + 1);

        int i = left, j = mid + 1, k = left;

        // Merge the two halves
        while (i <= mid && j <= right) {
            if (comparator.compare(temp[i], temp[j]) <= 0) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
            }
        }

        // Copy remaining elements
        while (i <= mid) {
            array[k++] = temp[i++];
        }
        while (j <= right) {
            array[k++] = temp[j++];
        }
    }

    /**
     * Merge Sort for Comparable objects
     */
    public static <T extends Comparable<T>> void mergeSort(T[] array) {
        mergeSort(array, (a, b) -> a.compareTo(b));
    }

    /**
     * Quick Sort implementation
     * Time Complexity: O(n log n) average, O(n²) worst case
     * Space Complexity: O(log n) average
     */
    public static <T> void quickSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSortHelper(array, 0, array.length - 1, comparator);
    }

    private static <T> void quickSortHelper(T[] array, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSortHelper(array, low, pivotIndex - 1, comparator);
            quickSortHelper(array, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] array, int low, int high, Comparator<T> comparator) {
        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    /**
     * Quick Sort for Comparable objects
     */
    public static <T extends Comparable<T>> void quickSort(T[] array) {
        quickSort(array, (a, b) -> a.compareTo(b));
    }

    /**
     * Selection Sort implementation
     * Time Complexity: O(n²) all cases
     * Space Complexity: O(1)
     */
    public static <T> void selectionSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }

        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < array.length; j++) {
                if (comparator.compare(array[j], array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                swap(array, i, minIndex);
            }
        }
    }

    /**
     * Selection Sort for Comparable objects
     */
    public static <T extends Comparable<T>> void selectionSort(T[] array) {
        selectionSort(array, (a, b) -> a.compareTo(b));
    }

    /**
     * Bubble Sort implementation
     * Time Complexity: O(n²) worst case, O(n) best case
     * Space Complexity: O(1)
     */
    public static <T> void bubbleSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }

        boolean swapped;
        for (int i = 0; i < array.length - 1; i++) {
            swapped = false;

            for (int j = 0; j < array.length - 1 - i; j++) {
                if (comparator.compare(array[j], array[j + 1]) > 0) {
                    swap(array, j, j + 1);
                    swapped = true;
                }
            }

            // If no swapping occurred, array is sorted
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Bubble Sort for Comparable objects
     */
    public static <T extends Comparable<T>> void bubbleSort(T[] array) {
        bubbleSort(array, (a, b) -> a.compareTo(b));
    }

    /**
     * Binary Search implementation
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     * Note: Array must be sorted
     */
    public static <T> int binarySearch(T[] array, T target, Comparator<T> comparator) {
        if (array == null || array.length == 0) {
            return -1;
        }

        int left = 0, right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = comparator.compare(array[mid], target);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Not found
    }

    /**
     * Binary Search for Comparable objects
     */
    public static <T extends Comparable<T>> int binarySearch(T[] array, T target) {
        return binarySearch(array, target, (a, b) -> a.compareTo(b));
    }

    /**
     * Linear Search implementation
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static <T> int linearSearch(T[] array, T target) {
        if (array == null) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if ((array[i] == null && target == null) ||
                    (array[i] != null && array[i].equals(target))) {
                return i;
            }
        }
        return -1; // Not found
    }

    /**
     * Utility method to swap two elements in an array
     */
    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Check if array is sorted
     */
    public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) {
            return true;
        }

        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i - 1], array[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if array is sorted (for Comparable objects)
     */
    public static <T extends Comparable<T>> boolean isSorted(T[] array) {
        return isSorted(array, (a, b) -> a.compareTo(b));
    }
}
