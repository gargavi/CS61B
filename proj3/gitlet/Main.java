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
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        try {
            if (args[0].equals("add")) {
                if (args.length == 2) {
                    repo.add(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("rm")) {
                if (args.length == 2) {
                    repo.remove(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("commit")) {
                if (args.length == 2) {
                    repo.commit(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("init")) {
                if (args.length == 1) {
                    repo.init();
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("log")) {
                if (args.length == 1) {
                    repo.log();
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("global-log")) {
                if (args.length == 1) {
                    repo.globall();
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("checkout")) {
                if (args[1].equals("--")) {
                    if (args.length == 3) {
                        repo.checkoutn(args[2]);
                    } else {
                        throw Utils.error("Incorrect Operands");
                    }

                } else if (args[1].equals("--")) {
                    if (args.length == 4) {
                        repo.checkout(args[3], args[1]);
                    } else {
                        throw Utils.error("Incorrect Operands");
                    }
                } else if (args.length == 2) {
                    repo.checkoutb(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }

            } else if (args[0].equals("status")) {
                if (args.length == 1) {
                    repo.status();
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("branch")) {
                if (args.length == 2) {
                    repo.branch(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("rm-branch")) {
                if (args.length == 2) {
                    repo.removebranch(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("reset")) {
                if (args.length == 2) {
                    repo.reset(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("find")) {
                if (args.length == 2) {
                    repo.find(args[1]);
                } else {
                    throw Utils.error("Incorrect Operands");
                }
            } else if (args[0].equals("list")) {
                System.out.println(Utils.plainFilenamesIn("."));
            }

            else {
                throw Utils.error("No command with that name exists");
            }
        } catch (GitletException exp)  {
            System.out.println(exp);
            System.exit(0);
        }
        Utils.writeObject(gitlet, repo);
    }

}
