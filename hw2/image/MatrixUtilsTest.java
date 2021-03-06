package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the MatrixUtils methods
 *  @author Avi Garg
 */

public class MatrixUtilsTest {
    /**
     *
     */
    @Test
    public void testAccumulateVertical(){
        double[][] testing = new double[][]{
                {1000000, 1000000, 1000000, 1000000}, {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000}, {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000}, {1000000, 1000000, 1000000, 1000000}
        };
        double [][] answer = new double[][]{
                {1000000, 1000000, 1000000 , 1000000 }, {2000000, 1075990, 1030003, 2000000 },
                {2075990, 1060005, 1133049, 2030003}, {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278}, {2162923, 2124919, 2124919, 2124919}
        };

        double [][] b = MatrixUtils.accumulateVertical(testing);
        System.out.println(MatrixUtils.matrixToString(b));
        System.out.println(MatrixUtils.matrixToString(answer));
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j<4; j++){
                assertEquals(b[i][j], answer[i][j], .01);
            }
        }
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
