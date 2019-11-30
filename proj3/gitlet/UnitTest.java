package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void init_test() throws IOException {
        Gitlet temp = new Gitlet();
        temp.init();


    }
    @Test
    public void add_test() throws IOException {
        try {
            Main.main("init");
        } catch (Exception expr) {
            System.out.println(expr);
        }
        Main.main("add", "something2");
        Main.main("add", "something1");
        Main.main("commit", "this be the way");
        Main.main("commit", "this is the way");

    }
    @Test
    public void trty() {


    }


}


