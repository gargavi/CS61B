package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {

    @Test
    public void testCatenate(){
        int[] A = new int[]{1, 2, 3, 4};
        int[] B = new int[]{5,6,7,8,};
        int[] C= new int[]{1, 2,3,4,5,6,7,8};
        System.out.println(Utils.equals(Arrays.catenate(A, B), C));

    }
    @Test
    public void testRemove(){
        int[] A = new int[]{1, 2, 3, 4};
        int[] B = new int[]{5,6,7,8,};
        int[] C= new int[]{1, 2,3,4,5,6,7,8};
        assertArrayEquals(A, Arrays.remove(C, 4, 4));
        assertArrayEquals(B, Arrays.remove(C, 0, 4));
    }
    @Test
    public void testNaturalRuns(){
        int[] A =  new int[]{1, 3, 7, 5, 4, 6, 9, 10};
        int[][] B = new int[][]{{1,3,7}, {5}, {4,6,9,10}};
        assertArrayEquals(Arrays.naturalRuns(A)[0], B[0]);
        assertArrayEquals(Arrays.naturalRuns(A)[1], B[1]);
        assertArrayEquals(Arrays.naturalRuns(A)[2], B[2]);

    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
