package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author avigarg
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void add_test() throws IOException {
        try {
            Main.main("init");
        } catch (Exception expr) {
            System.out.println(expr);
        }
        Main.main("add", "something2");
        Main.main("commit", "this be the way");
        Main.main("add", "something1");
        Main.main("branch", "nayan");
        Main.main("log");
        Main.main("status");
        List<String> working_dir = Utils.plainFilenamesIn(".");
        Main.main("status");

    }
    @Test
    public void try_this() {
        HashMap<String, String> temp = new HashMap<String, String>();
        HashMap<String, String> temp1 = new HashMap<String, String>();
        temp.put("hello", "aditya");
        temp.put("hi", "avi");
        temp1.put("hello", "adi");
        temp.put("fuck", "avi");
        for (String b: temp.keySet()) {
            if (temp.get(b).equals(temp1.get(b))){
                System.out.println("nice");
            }
        }
    }


}


