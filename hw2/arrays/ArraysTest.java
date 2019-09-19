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
        assertArrayEquals(Arrays.naturalRuns(A), B);
        A = new int[]{1};
        B = new int[][]{{1}};
        assertArrayEquals(Arrays.naturalRuns(A), B);
        A = new int[]{1,3,5,2,4,6,6};
        B = new int[][] {{1,3,5}, {2,4,6}, {6}};
        assertArrayEquals(Arrays.naturalRuns(A), B);
        A = new int[]{0,1,2,3,4};
        B = new int[][]{{0,1,2,3,4}};
        assertArrayEquals(Arrays.naturalRuns(A), B);

    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
