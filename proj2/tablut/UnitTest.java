package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test as a placeholder for real ones. */

    @Test
    public void checkUndo(){
        Board b = new Board();
        b.makeMove(Square.sq(7, 4), Square.sq(7, 5));
        assertFalse(b.encodedBoard().equals(new Board().encodedBoard()));
        b.undo();
        assertTrue(b.encodedBoard().equals(new Board().encodedBoard()));
        b.makeMove(Square.sq(8,5), Square.sq(6 ,5));
        b.makeMove(Square.sq(2 ,4), Square.sq(2, 5));
        String temp = b.encodedBoard();
        b.makeMove(Square.sq(8, 3), Square.sq(6, 3));
        System.out.println(b);
        b.undo();
        System.out.println(b);
        assertTrue(temp.equals(b.encodedBoard()));

    }

}


