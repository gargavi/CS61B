package capers;

import java.io.File;

public class Main {
    static final File MAIN_FOLDER = new File(".capers");

    /**
     * Runs one of three comands:
     * story [text] -- writes "text" to a story file in the .capers directory
     * dog [name] [breed] [age] -- creates a dog with the specified parameters
     * birthday [name] -- advances a dog's age and prints out a celebratory message
     * 
     * All persistent data should be stored in a ".capers" 
     * directory in the current working directory.
     * 
     * Recommended structure (you do not have to follow):
     * .capers/ -- top level folder for all persistent data
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     * 
     * Useful Util functions (as a start, you will likely need to use more):
     * writeContents - writes out strings/byte arrays to a file
     * readContentsAsString - reads in a file as a string
     * readContents - reads ina a file as a byte array
     * writeObject - writes a serializable object to a file
     * readObject - reads in a serializable object from a file
     * join - joins together strings or files into a path 
     *     (eg. Utils.join(".capers", "dogs") would give you a File object 
     *      with the path of ".capers/dogs")
     * @param args arguments from the command line
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            exitWithError("Must have at least one argument");
        }

        setupPersistence();

        switch (args[0]) {
            case "story":
                writeStory(args);
                break;
            case "dog":
                makeDog(args);
                break;
            case "birthday":
                celebrateBirthday(args);
                break;
            default:
                exitWithError(String.format("Unknown command: %s", args[0]));

        }
        return;
    }

    /**
     * Does required filesystem operations to allow for persistence
     * (creates any necessary folders or files)
     */
    public static void setupPersistence() {
  
    }


    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(-1);
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     */
    public static void writeStory(String[] args) {
    
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String[] args) {
 
    }

    /**
     * Advances a dogs age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses which dog to advance based on the first non-command argument of args
     */
    public static void celebrateBirthday(String[] args) {
    
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match
     * 
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
