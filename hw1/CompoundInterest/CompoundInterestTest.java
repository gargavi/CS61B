import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        int this_year = CompoundInterest.THIS_YEAR;
        int target_year = 2045;
        int answer = target_year - this_year;
        assertEquals(answer, CompoundInterest.numYears(target_year));
        target_year = 2067;
        answer = target_year - this_year;
        assertEquals(answer, CompoundInterest.numYears(target_year));

    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2021), tolerance);

    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(295712.28, CompoundInterest.futureValueReal(1000000, 0, 2059, 3), tolerance);

    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavingsReal(5000, 2021, 10, 0), tolerance);
        assertEquals(16220.655, CompoundInterest.totalSavingsReal(5000, 2021, 10, 1), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
