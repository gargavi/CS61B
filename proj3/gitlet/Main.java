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
        if (args[0].equals("add")) {
            if (args.length == 2) {
                try {
                    repo.add(args[1]);
                } catch (GitletException exp) {
                    System.out.println(exp);
                    System.exit(0);
                }
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
        } else if (args[0].equals("rm")) {
            if (args.length == 2) {
                try {
                    repo.remove(args[1]);
                } catch (GitletException exp) {
                    System.out.println(exp);
                    System.exit(0);
                }
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
        } else if (args[0].equals("commit")) {
            if (args.length == 2) {
                try {
                    repo.commit(args[1]);
                } catch (GitletException exp) {
                    System.out.println(exp);
                    System.exit(0);
                }
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
        } else if (args[0].equals("init")) {
            if (args.length == 1) {
                try {
                    repo.init();
                } catch (GitletException exp) {
                    System.out.println(exp);
                    System.exit(0);
                }
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
        } else if (args[0].equals("log")) {
            if (args.length == 1) {
                try {
                    repo.log();
                } catch (GitletException exp) {
                    System.out.println(exp);
                    System.exit(0);
                }
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
        } else if (args[0].equals("global-log")) {
            if (args.length == 1) {
                try {
                    repo.globall();
                } catch (GitletException exp) {
                    System.out.println(exp);
                    System.exit(0);
                }
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
        } else if (args[0].equals("checkout")){
            if (args[1].equals("--")){
                if (args.length == 3){
                    repo.checkoutn(args[2]);
                } else {
                    System.out.println("Incorrect Operands");
                    System.exit(0);
                }

            } else if (args[2].equals("--")) {
                if (args.length == 4){
                    repo.checkout(args[3], args[1]);
                } else {
                    System.out.println("Incorrect Operands");
                    System.exit(0);
                }
            } else if (args.length == 2) {
                repo.checkoutb(args[2]);
            } else {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }

        } else {
            System.out.println("No command with that name exists");
            System.exit(0);
        }
        Utils.writeObject(gitlet, repo);
    }

}
