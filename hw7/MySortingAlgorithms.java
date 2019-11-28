import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i ++){
                int j = i;
                while (j > 0 && array[j-1] > array[j]){
                    int temp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = temp;
                    j --;
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i ++){
                int lowest_index = i;
                for (int j = i; j < k; j ++){
                    if (array[j] < array[i]){
                        lowest_index = j;
                    }
                }
                int temp = array[lowest_index];
                array[lowest_index] = array[i];
                array[i] = temp;

            }

        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {



        }
        public void merge(int[] arr){
            int[] first = new int[arr.length/2];
            int[] second = new int[arr.length - arr.length/2];
            for (int i = 0; i < arr.length/2; i ++){
                first[i] = arr[i];
            }
            for (int j = arr.length/2; j < arr.length; j ++){
                second[j - arr.length] = arr[j];
            }
            // [1, 3, 4, 7, 8]
            // [2, 5, 6, 7, 9, 10]
            // [1, 2, 3, 4, 5, 6, 7, 7, 8
            int i = 0;
            int j = 0;
            int k = 0;
            while (i < first.length && j < second.length){
                if (first[i] < second[j]){
                    arr[k] = first[i];
                    i ++;
                    k ++;
                } else{
                    arr[k] = second[j];
                    j ++;
                    k ++;
                }
            }
            while (i < first.length){
                arr[k] = first[i];
                i ++;
                k ++;
            }
            while (j < second.length){
                arr[k] = first[j];
                j ++;
                k ++;
            }

        }


        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int max = array[0];
            int[] output = new int[array.length];
            for (int i = 1; i < array.length; i ++ ){
                if (array[i] > max){
                    max = array[i];
                }
            }
            int[] count = new int[max + 1];
            for (int b: array){
                count[b] = count[b] + 1;
            }
            for (int i = 1; i <= max; i ++ ){
                count[i] = count[i] + count[i-1];
            }
            for (int i = array.length-1; i>=0; i--)
            {
                output[count[array[i]]-1] = array[i];
                --count[array[i]];
            }
            int j = 0;
            for (int b: output){
                array[j] = b;
                j ++;
            }

        }

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {

            for (int i = k/2 ; i > 0; i --){
                heapify(array, k, i-1);
            }
            for (int i = k  -1; i >= 0; i --){
                int temp = array[0];
                array[0] = array[i];
                array[i] = temp;

                heapify(array, i, 0);
            }

        }

        public void heapify(int arr[], int n, int i){
            int largest = i;
            int val_l = arr[i];
            int left = 2*i;
            int right = 2*i + 1;

            if (left < n && arr[left] > val_l){
                largest = left;
            }
            if (right < n && arr[right] > largest){
                largest = right;
            }
            if (largest != i){
                int temp = arr[i];
                arr[i] = arr[largest];
                arr[largest] = temp;

                heapify(arr, n, largest);
            }

        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
