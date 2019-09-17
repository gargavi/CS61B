package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Avi Garg
 */
class Arrays {
    /* C. */

    /**
     * Returns a new array consisting of the elements of A followed by the
     * the elements of B.
     */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int[] total = new int[A.length + B.length];
        System.arraycopy(A, 0, total, 0, A.length);
        System.arraycopy(B, 0, total, A.length, B.length);
        return total;
    }

    /**
     * Returns the array formed by removing LEN items from A,
     * beginning with item #START.
     */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] B = new int[A.length - len];
        int k = 0;
        while (k < start) {
            B[k] = A[k];
            k++;
        }
        while (k < B.length) {
            B[k] = A[k + len];
            k++;
        }
        return B;

    }

    /* E. */

    /**
     * Returns the array of arrays formed by breaking up A into
     * maximal ascending lists, without reordering.
     * For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     * returns the three-element array
     * {{1, 3, 7}, {5}, {4, 6, 9, 10}}.
     */



    static int[][] naturalRuns(int[] A) {

        //Utils.subarray()
        int high = 0;
        int k = 0;
        while(k < A.length && A[k] > high){
            high = A[k];
            k +=1;
        }
        int[] first = Utils.subarray(A, 0, k);
        int[] remain = Utils.subarray(A, k, A.length - k );

        if (remain.length != 0){
            int[][] partial = naturalRuns(remain);
            int[][] total = new int[partial.length +1][];
            total[0] = first;
            System.arraycopy(partial, 0, total, 1, partial.length);
            return total;
        } else {
            int[][] partial = new int[1][];
            partial[0] = first;
            return partial;
        }


    }
}

