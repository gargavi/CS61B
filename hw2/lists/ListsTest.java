package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** Lists Testing
 *
 *  @author Avi Garg
 */

public class ListsTest {


    @Test
    public void testNaturalRuns(){
        IntList a = IntList.list(1,2,3,4,3,4,1,2);
        assertEquals(Lists.naturalRuns(a), IntListList.list(new int[][]{{1,2,3,4}, {3,4}, {1,2}}));


    }


    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
