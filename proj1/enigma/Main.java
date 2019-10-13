package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Avi Garg & CS61b Staff
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        String temp = _input.next();
        if (!temp.equals("*")) {
            throw new EnigmaException("Doesn't start with a setting");
        }
        setUp(M, _input.nextLine());

        while (_input.hasNext()) {
            String a = _input.nextLine();
            if (a.indexOf("*") != -1) {
                setUp(M, a.substring(1));
            } else {
                Scanner unusual = new Scanner(a);
                String here = "";
                while (unusual.hasNext()) {
                    here = here + unusual.next();
                }
                printMessageLine(M.convert(here));
            }

        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alpha = _config.next();
            _alphabet = new Alphabet(alpha);
            int numrotors = Integer.parseInt(_config.next());
            int numpawls = Integer.parseInt(_config.next());
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                Rotor temp = readRotor();
                allRotors.add(temp);
            }
            return new Machine(_alphabet, numrotors, numpawls, allRotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String configurer = _config.next();
            String cycles = "";
            int totalletters = 0;
            while (totalletters != _alphabet.size()) {
                String a = _config.next();
                if (!a.contains("(") || !a.contains(")")) {
                    System.out.println(a);
                    throw new EnigmaException("Needs all letters to in cycles");
                }
                String b = a.substring(1, a.length() - 1);

                for (char character: b.toCharArray()) {
                    if (_alphabet.contains(character)) {
                        totalletters += 1;
                    } else {
                        throw new EnigmaException("bad configuration");
                    }
                }

                cycles = cycles + " " + a;
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (configurer.charAt(0) == "M".charAt(0)) {
                return new MovingRotor(name, perm, configurer.substring(1));
            } else if (configurer.charAt(0) == "N".charAt(0)) {
                return new FixedRotor(name, perm);
            } else if (configurer.charAt(0) == "R".charAt(0)) {
                return new Reflector(name, perm);
            } else {
                throw new EnigmaException("Not a valid configuration");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner setter = new Scanner(settings);
        int rotors = 0;
        String[] names = new String[M.numRotors()];
        while (rotors < M.numRotors()) {
            if (!setter.hasNext()) {
                throw new EnigmaException("Not enough arguments");
            }
            String a = setter.next();
            names[rotors] = a;
            rotors++;
        }
        M.insertRotors(names);
        if (setter.hasNext()) {
            M.setRotors(setter.next());
        } else {
            throw new EnigmaException("Not enough arguments");
        }
        String perm = "";
        while (setter.hasNext()) {
            String temporary = setter.next();
            if (temporary.indexOf("(") == -1){
                M.setRotorRotation(temporary);
            } else {
                perm = perm + " " + temporary;
            }
        }
        M.setPlugboard(new Permutation(perm, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String newmsg = "";
        while (msg.length() > 5) {
            String temp = msg.substring(0, 5);
            newmsg += temp + " ";
            msg = msg.substring(5);
        }
        newmsg += msg;
        _output.println(newmsg);

    }
    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
