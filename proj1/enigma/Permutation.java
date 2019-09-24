package enigma;

import java.util.ArrayList;

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
        String[] cyclic = cycles.split(" ", 0);
        for(String cycle: cyclic){
            addCycle(cycle.substring(1, cycle.length()-1));
        }

    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycles.add(cycle);
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
        return _alphabet.size(); // FIXED
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char ch = alphabet().toChar(p);
        return alphabet().toInt(permute(ch));  // FIXME
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char ch = alphabet().toChar(c);
        return alphabet().toInt(invert(ch));// FIXME
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for(String cycle: cycles){
            for(int i = 0; i < cycle.length(); i++){
                char[] temp = cycle.toCharArray();
                if(p == temp[i]){
                    if(i == cycle.length()-1){
                        return temp[0];
                    }
                    return temp[i+1];
                }

            }
        }
        if (alphabet().contains(p)){
            return p;
        }
        return p;   // FIXME
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for(String cycle: cycles){
            for(int i = 0; i < cycle.length(); i++){
                char[] temp = cycle.toCharArray();
                if(c == temp[i]){
                    if(i == 0){
                        return temp[cycle.length()-1];
                    }
                    return temp[i-1];
                }

            }

        }
        if (alphabet().contains(c)){
            return c;
        }
        return c;   // FIXME
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int a = 0;
        for (String cycle: cycles){
            a += cycle.length();
        }
        return a == alphabet().size();  // FIXME
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    void print(){
        for(String cycle: cycles){
            System.out.println(cycle);
        }
    }
    private ArrayList<String> cycles = new ArrayList<String>();
    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
