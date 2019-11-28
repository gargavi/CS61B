package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Avi Garg
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

        File gitlet = new File(".gitlet/gitlet");
        if (gitlet.exists()){
            Gitlet repo = Utils.readObject(gitlet, Gitlet.class);
        } else {
            Gitlet repo = null;
        }


    }

}
