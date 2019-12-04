package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** This will help us manage the git repository and all the commmands we'll be using
 * @author avigarg
 */

public class Gitlet implements Serializable {

    /** */
    private ArrayList<String> untracked;

    private Collection<String> tracked;

    private Stage staged;

    private ArrayList<String> branches;

    private ArrayList<String> commits;

    private HashMap<String, String> branchHeads;

    private String currentbranch;

    private boolean inited;

    public Gitlet(){
        inited = false;
        untracked = new ArrayList<String>();
        tracked = new HashSet<String>();
        commits = new ArrayList<String>();
        branchHeads = new HashMap<String, String>();
        branches = new ArrayList<String>();
        staged = new Stage();
    }

    /** Init should create a gitlet repository if one doesn't exist
     * And should create a new commit to put in it
     */
    public void init() throws IOException{
        if (inited){
            throw Utils.error("A Gitlet version-control system already exists in the current directory.");
        } else {
            inited = true;
            Files.createDirectory(Paths.get(".gitlet"));
            Commit initial = new Commit();
            String initName = initial.gethash();
            commits.add(initName);
            File loc = new File(".gitlet/" + initName);
            Utils.writeObject(loc, initial);
            branches.add("master");
            currentbranch = "master";
            branchHeads.put("master", initName);
        }
    }

    public void add(String file) {
        if (inited) {
            Blob blob;
            try {
                File relevant = new File(file);
                blob = new Blob(file);
            } catch (Exception expr){
                throw Utils.error("File does not exist");
            }
            tracked.add(file);
            String hashed = blob.gethash();
            File prev = new File(".gitlet/" + branchHeads.get(currentbranch));
            Commit previous = Utils.readObject(prev, Commit.class);
            if (previous.getBlobs().contains(hashed)){
                staged.remove(file);
            } else {
                staged.add(file);
            }
        } else {
            throw Utils.error("Git Repository hasn't been initialized");
        }
    }
    /** Need to unstage a file and mark it to be not tracked during the next commit.
     * Also delete the file */
    public void remove(String file) {
        if (tracked.contains(file) || staged.contains(file)) {
            if (tracked.contains(file)) {
                untracked.contains(file);
                File temp = new File(file);
                Utils.restrictedDelete(temp);
            }
            if (staged.contains(file)){
                staged.remove(file);
            }
        } else {
            throw Utils.error("No reason to remove the file");
        }

    }

    /** Make a new commit and add it to whatever the fuck */
    public void commit(String message){
        if (message == "") {
            throw Utils.error("Please enter a commit message");
        } else {
            String parental = branchHeads.get(currentbranch);
            File parent = new File(".gitlet/" + parental);
            Commit old = Utils.readObject(parent, Commit.class);
            HashMap<String, String> prev = old.getContents();
            HashMap<String, String> staging = staged.getContents();
            HashMap<String, String> cur = new HashMap<String, String>();
            for (String name: tracked){
                if (staging.containsKey(name)){
                    cur.put(name, staging.get(name));
                } else if (!untracked.contains(name)){
                    cur.put(name, prev.get(name));
                }
            }
            if (prev.equals(cur)){
                throw Utils.error("No Change Added to Commit");
            } else {
                staged = new Stage();
                Commit current = new Commit(message, cur, parental, currentbranch );
                String name = current.gethash();
                branchHeads.put(currentbranch, name);
                commits.add(name);
                File loc = new File(".gitlet/" + name);
                Utils.writeObject(loc, current);
                untracked.clear();
            }
        }
    }
    /** Displays the commits from current head to the inital commit. */
    public void log() {
        File cur = new File(".gitlet/" + branchHeads.get(currentbranch));
        Commit current = Utils.readObject(cur, Commit.class);
        while (current.getParent() != null) {
            System.out.println("===");
            System.out.println("commit " + current.gethash());
            if (current.getSParent() != null) {
                String first = current.getParent().substring(0, 6);
                String second = current.getSParent().substring(0, 6);
                System.out.println("Merge: " + first + " " + second);
            }
            System.out.println("Date: " + current.getTime());
            System.out.println(current.getMessage());
            System.out.println();
            String temp = current.getParent();
            cur = new File(".gitlet/" + temp);
            current = Utils.readObject(cur, Commit.class);
        }
        System.out.println("===");
        System.out.println("commit " + current.gethash());
        System.out.println("Date: " + current.getTime());
        System.out.println(current.getMessage());
        System.out.println();
    }

    public void globall() {
        for (String b: commits) {
            File cur = new File(".gitlet/" + b);
            Commit current = Utils.readObject(cur, Commit.class);
            System.out.println("===");
            System.out.println("Commit: " + current.gethash());
            if (current.getSParent() != null) {
                String first = current.getParent().substring(0, 6);
                String second = current.getSParent().substring(0, 6);
                System.out.println("Merge: " + first + " " + second);
            }
            System.out.println("Date: " + current.getTime());
            System.out.println(current.getMessage());
            System.out.println();
        }
    }

    public void find(String mess) {
        boolean found = false;
        for (String b: commits) {
            File cur = new File(".gitlet/" + b);
            Commit current = Utils.readObject(cur, Commit.class);
            if (mess == current.getMessage()) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            Utils.error("Found no commit with that message");
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        for (String b: branchHeads.keySet()) {
            if (b == currentbranch) {
                System.out.println("*" + b);
            } else {
                System.out.println(b);
            }
        }
        System.out.println("=== Staged Files===");
        for (String b: staged.getContents().keySet()) {
            System.out.println(b);
        }
        System.out.println("=== Removed Files===");
        for (String b: untracked) {
            System.out.println(b);
        }
        System.out.println("===Modifications Not Staged for Commit");
            // Need to add more code for the extra credit portion

    }

    public void checkout (String name, String commit) {
        if (commit.length() >= 40 ) {
            File temp = new File(".gitlet/" + commit);
            Commit head = Utils.readObject( temp, Commit.class);
            String hash = head.getContents().get(name);
            if (head.getContents().containsKey(name)){
                temp = new File(".gitlet/" + hash);
                if (!temp.exists()){
                    throw Utils.error("No commit with that id exists");
                }
                Blob blob = Utils.readObject(temp, Blob.class);
                temp = new File(name);;
                Utils.writeContents(temp, blob.getBContents());
            } else {
                throw Utils.error("File does not exist in that commit.");
            }

        } else if (commit.length() < 40){
            for (String b: commits) {
                if (commit.equals(b.substring(0, commit.length()))) {
                    checkout(name, b);
                }
            }
            throw Utils.error("No commit with that id exists.");
        }

    }
    public void checkoutn (String name) {
        String current = branchHeads.get(currentbranch);
        checkout(name, current);
    }

    public void checkoutb (String branchName) {
        if (branchHeads.keySet().contains(branchName)) {
            if (branchName == currentbranch) {
                throw Utils.error("No need to checkout the current branch.");
            } else {
                List<String> all = Utils.plainFilenamesIn(".");

            }
        } else {
            throw Utils.error("No such branch exists.");
        }
    }
}
