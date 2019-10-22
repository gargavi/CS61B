package enigma;


import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Avi Garg & CS61b Staff */

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
        ROTORS = new Rotor[_numrotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numrotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int i = 0;
        boolean duplicate = false;
        for (String temp: rotors) {
            for (Rotor total: _allRotors) {
                String a = total.name() + temp;
                if (total.name().equals(temp)) {
                    if (duplicate) {
                        throw new EnigmaException("can't have equal rotors");
                    } else {
                        ROTORS[i] = total;
                        i += 1;
                        duplicate = true;
                    }
                }

            }
            if (!duplicate) {
                throw new EnigmaException("invalid configuration " + temp);
            } else {
                duplicate = false;
            }
        }
        if (!ROTORS[0].reflecting()) {
            throw new EnigmaException("The first rotor has to be a reflector");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numrotors - 1) {
            throw new EnigmaException("need setting to be the right length");
        }
        for (int i = 0; i < setting.length(); i++) {
            if (_alphabet.contains(setting.charAt(i))) {
                ROTORS[i + 1].set(setting.charAt(i));
            } else {
                throw new EnigmaException("setting can't have whack letters");
            }

        }
    }
    /** sets the rotation of the rotors. */
    /**@param rotatsetting the setting of all rotation*/
    void setRotorRotation(String rotatsetting) {
        if (rotatsetting.length() != _numrotors - 1) {
            throw new EnigmaException("need setting to be the right length");
        }
        for (int i = 0; i < rotatsetting.length(); i++) {
            if (_alphabet.contains(rotatsetting.charAt(i))) {
                Alphabet hello = ROTORS[i + 1].alphabet();
                int a = hello.toInt(rotatsetting.charAt(i));
                hello.rotating(a);
            } else {
                throw new EnigmaException("setting can't have whack letters");
            }
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
        int i = numRotors() - numPawls();
        while (i < numRotors()) {
            if (i == numRotors() - 1) {
                ROTORS[i].advance();
                i++;
            } else if (ROTORS[i + 1].atNotch()) {
                ROTORS[i].advance();
                ROTORS[i + 1].advance();
                i = i + 2;
            } else {
                i++;
            }
        }
        char d = _alphabet.toChar(c);
        c = _plugboard.permute(c);
        for (int j = ROTORS.length; j > 0; j--) {
            c = ROTORS[j - 1].convertForward(c);
        }
        for (int j = 1; j < ROTORS.length; j++) {
            c = ROTORS[j].convertBackward(c);
        }
        return _plugboard.invert(c);

    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String perm = "";
        for (int i = 0; i < msg.length(); i++) {
            char a = _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
            perm = perm + a;
        }
        return perm;
    }
    /**Provide Access Rto Rotors.*/
    /**@return a collection. */
    Collection retrieve() {
        Collection<String> temp = new ArrayList<String>();
        for (Rotor a: _allRotors) {
            temp.add(a.name());
        }
        return temp;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /**The number of rotors. */
    private int _numrotors;
    /** The number of pawls. */
    private int _pawls;
    /** THe Collection of all Rotors. */
    private Collection<Rotor> _allRotors;
    /** The rotors we use. */
    private Rotor[] ROTORS;
    /**The plugboard that is used in the beginning and end. */
    private Permutation _plugboard;
}
