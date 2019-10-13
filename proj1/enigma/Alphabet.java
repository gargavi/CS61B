package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Avi Garg
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        this.alpha = chars;
        for (int i = 0; i < chars.length(); i++) {
            for (int j = 0; j < chars.length(); j++) {
                if (i != j && chars.charAt(i) == chars.charAt(j)) {
                    throw new EnigmaException("can't have duplicates");
                }
            }
        }


    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alpha.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < size(); i++) {
            if (alpha.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return alpha.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("Character not in the alphabet " + ch);
        }
        for (int i = 0; i < size(); i++) {
            if (alpha.charAt((i + rotation) % size()) == ch) {
                return i;
            }
        }
        return 0;
    }

    /**Sets the rotation of the alphabet. */
    /**@param sett represents rotation */
    void rotating(int sett) {
        rotation = sett;
    }

    /** retrieves the alphabet.
     * @return String Alpha*/
    public String getAlpha() {
        return alpha;
    }

    /** Represents a rotation in the Alphabet ring.*/
    private int rotation = 0;
    /** Represents the alphabet character. */
    private String alpha;
}
