import java.io.Reader;
import java.io.IOException;
import java.util.HashMap;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author your name here
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        _reader = str;
        _from = from.toCharArray();
        _to = to.toCharArray();
        // TODO: YOUR CODE HERE
    }

    private Reader _reader;
    private char[] _from;

    private char[] _to;

    public int read (char[] characters, int x, int y) throws IOException {
        int a = _reader.read(characters, x, y);
        if (a == -1) {
            return -1;
        }
        for(int j = 0; j < characters.length; j ++ ){
            for (int i =0; i < _from.length; i++ ){
                if (_from[i] == characters[j]){
                    characters[j] = _to[i];
                    break;
                }
            }

        }
        return a;
    }

    public int read(char[] characters) throws IOException {
        return read(characters, 0, characters.length);
    }

    public void close() throws IOException {
        _reader.close();
    }
    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
}
