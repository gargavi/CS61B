public class Test {

    public static void main (String[] args) {
        System.out.println("this compiled and passed? ")

    }

    public static int max(int[] a ) {
        int max;

        for (int num: a) {
            if (num > max) {
                max = num;
            }
        }
        return max;

    }

    public static boolean threeSumDistinct(int[] a) {
        /* How to do anything in this class. */
        return helper_three(a, 0, 0);


    }
    public static boolean threeSum(int[] a ){
        return helper_three_distinct(a, 0, 0)
    }

    public static boolean helper_three_distinct(int[] array int count int sum) {
        if (count != 3 ){
            return false;
        }
        else if (sum = 0) {
            return true;
        }
        else if (array.length == 0) {
            return false;
        }
        else {
            int new_sum = sum + array[0];
            int new_count = count + 1
            return helper_three(array, new_count, new_sum) || helper_three(array, count, sum)
        }
    }


    public static boolean helper_three(int[] array int count int sum) {
        if (count != 3 ){
            return false;
        }
        else if (sum = 0) {
            return true;
        }
        else if (array.length == 0) {
            return false;
        }
        else {
            int[] new_array = array.slice(1, array.length());
            int new_sum = sum + array[0];
            int new_count = count + 1
            return helper_three(new_array, new_count, new_sum) || helper_three(new_array, count, sum)
        }



    }

}