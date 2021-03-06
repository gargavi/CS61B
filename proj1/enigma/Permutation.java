package enigma;

import java.util.ArrayList;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Avi Garg
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        Scanner cycler = new Scanner(cycles);
        while (cycler.hasNext()) {
            String cycle = cycler.next();
            cycle = cycle.substring(1, cycle.length() - 1);
            for (int i = 0; i < cycle.length(); i++) {
                if (!_alphabet.contains(cycle.charAt(i))) {
                    throw new EnigmaException("values not in alphabet");
                }
            }
            addCycle(cycle);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (char a: cycle.toCharArray()) {
            if (gathered.indexOf(a) != -1) {
                throw new EnigmaException("cycle contains repeated values");
            } else {
                gathered = gathered + a;
            }
        }
        cyclical.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char ch = alphabet().toChar(p);
        return wrap(alphabet().toInt(permute(ch)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char ch = alphabet().toChar(c);
        return wrap(alphabet().toInt(invert(ch)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (String cycle: cyclical) {
            for (int i = 0; i < cycle.length(); i++) {
                char[] temp = cycle.toCharArray();
                if (p == temp[i]) {
                    if (i == cycle.length() - 1) {
                        return temp[0];
                    }
                    return temp[i + 1];
                }

            }
        }
        if (alphabet().contains(p)) {
            return p;
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (String cycle: cyclical) {
            for (int i = 0; i < cycle.length(); i++) {
                char[] temp = cycle.toCharArray();
                if (c == temp[i]) {
                    if (i == 0) {
                        return temp[cycle.length() - 1];
                    }
                    return temp[i - 1];
                }

            }

        }
        if (alphabet().contains(c)) {
            return c;
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int a = 0;
        for (String cycle: cyclical) {
            a += cycle.length();
        }
        return a == alphabet().size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Contains all the cycles. */
    private ArrayList<String> cyclical = new ArrayList<String>();
    /** A string to help us analyze the used cycles. */
    private String gathered = "";

}
