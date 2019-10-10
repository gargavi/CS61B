package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numrotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
         return _numrotors; // FIXME
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls; // FIXME
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int i = 0;
        boolean duplicate = false;
        for (String temp: rotors){
            for (Rotor total: _allRotors){
                if (total.name() == temp){
                    if (duplicate == true){
                        throw new EnigmaException("can't have multiple rotors with same name");
                    } else {
                        ROTORS[i] = total;
                        i += 1;
                        duplicate = true;
                    }
                }

            }
            if (duplicate == false){
                throw new EnigmaException("invalid configuration (passed rotor not defined)");
            } else{
                duplicate = false;
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numrotors - 1){
            throw new EnigmaException("need setting to be the right length");
        }
        for (int i = 0; i < setting.length(); i++ ){
            ROTORS[i+1].set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int i = ROTORS.length - 1;
        while(ROTORS[i].rotates()){
            if(i == ROTORS.length - 1){
                ROTORS[i].advance();
            }else if(ROTORS[i+1].atNotch()){
                ROTORS[i].advance();
                ROTORS[i+1].advance();
            }
            i --;
        }
        c = _plugboard.permute(c);
        for (int j = ROTORS.length; j > 0; j --){
            c = ROTORS[j-1].convertForward(c);
        }
        for (int j = 0; j < ROTORS.length; j--){
            c = ROTORS[j].convertBackward(c);
        }
        return _plugboard.invert(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String perm = "";
        for(int i = 0; i < msg.length(); i ++){
            perm = perm + convert(_alphabet.toInt(msg.charAt(i)));
        }
        return perm;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    private int _numrotors;
    private int _pawls;
    private Collection<Rotor> _allRotors;
    private Rotor[] ROTORS = new Rotor[_numrotors];
    private Permutation _plugboard;
    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
