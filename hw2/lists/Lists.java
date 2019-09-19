package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */

        IntListList a = new IntListList();
        IntList current = L;
        IntListList b = a;
        int high = L.head;

        b.head = L;

        while(current.tail != null){
            int test = current.tail.head;
            if(test > high){
                current = current.tail;
                high = current.head;

            } else{
                b.tail = new IntListList(current.tail, null);
                current.tail = null;
                current = b.tail.head;
                b = b.tail;
                high = current.head;
            }


        }
        return a;


    }
}
