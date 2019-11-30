package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** This will help us manage the git repository and all the commmands we'll be using
 *
 */

public class Gitlet implements Serializable {

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
            tracked.add(file);
            Blob blob;
            try {
                File relevant = new File(file);
                blob = new Blob(file);
            } catch (Exception expr){
                throw Utils.error("File does not exist");
            }
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
            File parent = new File(".gitlet/" + branchHeads.get(currentbranch));
            Commit old = Utils.readObject(parent, Commit.class);
            Commit current = new Commit(old);
            HashMap<String, String> staging = staged.getContents();
            int stag = 1;
            for (String b : staging.keySet()) {
                current.feed(b, staging.get(b));
                stag ++;
            }
            for (String b : untracked) {
                current.rem(b);
                stag ++;
            }
            if (stag == 1){
                throw Utils.error("No Change Added to Commit");
            } else {
                staged = new Stage();
                String name = current.gethash();
                branchHeads.put(currentbranch, name);
                commits.add(name);
                File loc = new File(".gitlet/" + name);
                Utils.writeObject(loc, current);
                untracked.clear();
            }
        }
    }
}
