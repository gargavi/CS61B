package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Avi Garg
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _perm = perm;
        _notches = notches;
        //_notch_array = notches.split("");

    }


    @Override
    boolean rotates(){
        return true;
    }

    @Override
    boolean atNotch(){
        for (int i = 0; i < _notches.length(); i ++){
            if(alphabet().toInt(_notches.charAt(i)) == this.setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        this.set(this.setting() + 1);
    }

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

    private String _notches;
    private Permutation _perm;
    private char[] _notch_array;
}
