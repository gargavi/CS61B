package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void permuting(){
        alpha = "abcdefghij";
        perm = new Permutation("(abc) (def) (hij)", new Alphabet(alpha));
        checkPerm("simple", "adhbeicfjg", "beicfjadhg");
        perm = new Permutation("(cba) (def) (jh)", new Alphabet(alpha));
        checkPerm("missing letter", "abcdefghij", "cabefdgjih");
        perm = new Permutation("", new Alphabet(alpha));
        checkPerm("empty cycles", "abcdefghij", "abcdefghij");

    }

    @Test
    public void testduplicates(){
        alpha = "aa";
        try{
            Alphabet a = new Alphabet(alpha);
            System.out.println("Didn't catch #1");
        } catch (EnigmaException excp) {
            System.out.println("Good Catch #1");

        }
        alpha = "aabb";
        try{
            Alphabet a = new Alphabet(alpha);
            System.out.println("Didn't catch #2");
        } catch (EnigmaException excp) {
            System.out.println("Good Catch #2");

        }

    }
    @Test
    public void testMismatch(){
        alpha = "abcde";
        try {
            perm = new Permutation("(ab) (cde) (f)", new Alphabet(alpha));
            assertEquals(0, 1);
        } catch (EnigmaException excp){
            assertEquals(excp.getMessage(), "cycle contains values that the alphabet does not");
        }
    }


    @Test
    public void testpermute(){
        alpha = "mynakeis";
        perm = new Permutation("(my) (nake) (is)", new Alphabet(alpha));
        assertEquals(perm.permute(0), 1);
        assertEquals(perm.permute(3), 4);
        assertEquals(perm.permute(5), 2);
        assertEquals(perm.permute('k'), 'e');
        assertEquals(perm.permute('s'), 'i');

        alpha = "qwerty";
        perm = new Permutation("(qw) (ert)", new Alphabet(alpha));
        assertEquals(perm.permute(5), 5);
        assertEquals(perm.permute(0), 1);
        assertEquals(perm.permute('q'), 'w');


    }
    @Test
    public void testinvert(){
        alpha = "uncopyrightable";
        perm = new Permutation("(copy) (unright) (bal)", new Alphabet(alpha));
        assertEquals(perm.invert('y'), 'p');
        assertEquals(perm.invert('p'), 'o');
        assertEquals(perm.invert('e'), 'e');
        assertEquals(perm.invert('u'), 't');

    }
    @Test
    public void testDerange(){
        alpha = "uncopyrightable";
        perm = new Permutation("(copy) (unright) (bal)", new Alphabet(alpha));
        assertEquals(perm.derangement(), false);

        alpha = "qwerty";
        perm = new Permutation("(qw) (ert)", new Alphabet(alpha));
        assertEquals(perm.derangement(), false);
        alpha = "mynakeis";
        perm = new Permutation("(my) (nake) (is)", new Alphabet(alpha));
        assertEquals(perm.derangement(), true);
        alpha = "abcdefghij";
        perm = new Permutation("(abc) (def) (hij)", new Alphabet(alpha));
        assertEquals(perm.derangement(), false);
        perm = new Permutation("(cba) (def) (jh)", new Alphabet(alpha));
        assertEquals(perm.derangement(), false);
        alpha = "";
        perm = new Permutation("", new Alphabet(alpha));
        assertEquals(perm.derangement(), true);
    }



}
