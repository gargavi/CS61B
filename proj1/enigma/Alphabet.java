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

    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alpha.length(); // FIXME
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
       for (int i = 0; i < size(); i ++){
           if (alpha.charAt(i) == ch){
               return true;
           }
       }
        return false;
        // return 'A' <= ch && ch <= 'Z'; // FIXME
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return alpha.charAt(index); // FIXME
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        for(int i = 0; i < size(); i ++ ){
            if(alpha.charAt(i)== ch){
                return i;
            }
        }; // FIXME
        return 0;
    }

    public String alpha;
}
