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
        _notc = new int[_notches.length()];
        String temp = "";
        for (int i = 0; i < _notches.length(); i++) {
            _notc[i] = alphabet().toInt(_notches.charAt(i));
            temp += Integer.toString(_notc[i]);
        }
        System.out.println(name + temp);

    }
    @Override
    String get_notches(){
        return _notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int t: _notc) {
            if (t == this.setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        this.set(this.setting() + 1);
    }

    /** A string to contain the notches. */
    private String _notches;
    /** All the notches for this rotor. */
    private int[] _notc;
    /** The permutation. */
    private Permutation _perm;
}
