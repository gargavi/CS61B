package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Avi Garg
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        File gitlet = new File(".gitlet/gitlet");
        Gitlet repo;
        if (gitlet.exists()){
            repo = Utils.readObject(gitlet, Gitlet.class);
        } else {
            repo = new Gitlet();
        }
        if (args.length == 2){
            if (args[0] == "add"){
                repo.add(args[1]);
            }
            if (args[0] == "rm"){
                repo.remove(args[1]);
            }
            if (args[0] == "commit") {
                repo.commit(args[1]);
            }
        } else if (args.length == 1){
            if (args[0].equals("init")){
                repo.init();
            }
        }
        Utils.writeObject(gitlet, repo);
    }

}
