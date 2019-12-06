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

    /** Keeps track of untracked files. */
    private ArrayList<String> untracked;
    /** Keeps track of the tracked files at any given point in time. */
    private Set<String> tracked;
    /** The staging area */
    private Stage staged;
    /** The names of all branches in the file. */
    private ArrayList<String> branches;
    /** .*/
    private ArrayList<String> commits;

    private HashMap<String, String> branchHeads;

    private String currentbranch;

    private String head;

    private boolean inited;

    private int lowest;

    private String globalances;

    public Gitlet(){
        inited = false;
        untracked = new ArrayList<String>();
        tracked = new HashSet<String>();
        commits = new ArrayList<String>();
        branchHeads = new HashMap<String, String>();
        branches = new ArrayList<String>();
        staged = new Stage();
        lowest = Integer.MAX_VALUE;
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
            head = initName;
            globalances = head;
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
            File prev = new File(".gitlet/" + head);
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
                untracked.add(file);
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
            File parent = new File(".gitlet/" + head);
            Commit old = Utils.readObject(parent, Commit.class);
            HashMap<String, String> prev = old.getContents();
            HashMap<String, String> staging = staged.getContents();
            HashMap<String, String> cur = new HashMap<String, String>();
            for (String name: tracked){
                if (staging.containsKey(name)){
                    cur.put(name, staging.get(name));
                } else if (!untracked.contains(name) && prev.containsKey(name)){
                    cur.put(name, prev.get(name));
                }
            }
            if (prev.equals(cur)){
                throw Utils.error("No Change Added to Commit");
            } else {
                staged = new Stage();
                Commit current = new Commit(message, cur, head, currentbranch );
                String name = current.gethash();
                branchHeads.put(currentbranch, name);
                commits.add(name);
                head = name;
                File loc = new File(".gitlet/" + name);
                Utils.writeObject(loc, current);
                untracked.clear();
            }
        }
    }
    /** Displays the commits from current head to the inital commit. */
    public void log() {
        File cur = new File(".gitlet/" + head);
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
            if (b.equals(currentbranch)) {
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
        checkout(name, head);
    }

    public void checkoutb (String branchName) {
        if (branchHeads.keySet().contains(branchName)) {
            if (branchName == currentbranch) {
                throw Utils.error("No need to checkout the current branch.");
            } else {
                File cur = new File(".gitlet/"  + branchHeads.get(branchName));
                Commit branch = Utils.readObject(cur, Commit.class);
                HashMap<String, String> files = branch.getContents();
                if (!staged.getContents().isEmpty()) {
                    throw Utils.error("You have uncommitted changes.");
                }
                List<String> working_dir = Utils.plainFilenamesIn(".");
                for (String ter: working_dir) {
                    if (!tracked.contains(ter) && files.keySet().contains(ter)) {
                        throw Utils.error("There is an untracked file in the way; delete it or add it first.");
                    }
                }
                for (String c: tracked) {
                    if (!files.containsKey(c)){
                        Utils.restrictedDelete(c);
                    }
                }
                for (String b: files.keySet()) {
                    String hash = files.get(b);
                    Blob c = Utils.readObject(new File(".gitlet/" + hash), Blob.class);
                    Utils.writeContents(new File(b), c.getBContents());
                }

                currentbranch = branchName;
                head = branchHeads.get(branchName);
                staged.clear();
            }
        } else {
            throw Utils.error("No such branch exists.");
        }
    }
    public void branch(String bname) {
        if (branches.contains(bname)) {
            throw Utils.error("branch with that name already exists.");
        } else {
            branchHeads.put(bname, head);
            branches.add(bname);
        }
    }
    public void removebranch(String bname) {
        if (!branches.contains(bname)) {
            throw Utils.error("A branch with that name does not exist.");
        } else if (bname == currentbranch){
            throw Utils.error("Cannot remove the current branch.");
        } else {
            branchHeads.remove(bname);
            branches.remove(bname);
        }
    }
    public void reset(String commmit) {
        if (commits.contains(commmit)){
            Commit temp = Utils.readObject(new File(".gitlet/" + commmit), Commit.class);
            HashMap<String, String> files = temp.getContents();
            List<String> working_dir = Utils.plainFilenamesIn(".");
            for (String ter: working_dir) {
                if (!tracked.contains(ter) && files.keySet().contains(ter)) {
                    throw Utils.error("There is an untracked file in the way; delete it or add it first.");
                }
            }
            for (String b: files.keySet()) {
                checkout(b, commmit);
            }
            branchHeads.put(currentbranch, commmit);

        } else {
            throw Utils.error("No commit with that id exists.");
        }
    }
    public void reaffirm(String first, String branch, int total) {
        Commit temp = Utils.readObject(Utils.join(".gitlet/", first), Commit.class);
        if (temp.getbranch().equals(branch)) {
            if (total < lowest) {
                lowest = total;
                globalances = first;
            }
        } else if (temp.getParent() != null) {
            if (temp.getSParent() != null) {
                reaffirm(temp.getSParent(), branch, total + 1);
            }
            reaffirm(temp.getParent(), branch, total + 1);
        }
    }

    public String ancestorlocater(String first, String second, String branch1, String branch2) {
        reaffirm(first, branch2, 0);
        reaffirm(second, branch1, 0);
        return globalances;
    }


    public void merge(String bname) {
        String givenhash = branchHeads.get(bname);
        String currenthash = branchHeads.get(currentbranch);
        String ances = ancestorlocater(givenhash, currenthash, bname, currentbranch);
        Commit ancestor = Utils.readObject(Utils.join(".gitlet/",ances ), Commit.class);
        Commit given = Utils.readObject(Utils.join(".gitlet/", givenhash ), Commit.class);
        Commit current = Utils.readObject(Utils.join(".gitlet/", currenthash), Commit.class);
        if (ancestor.gethash() == givenhash) {
            throw Utils.error(" Given branch is an ancestor of the current branch");
        } else if (ancestor.gethash() == currenthash) {
            branchHeads.put(currentbranch, givenhash);
        }
        lowest = Integer.MAX_VALUE;
        globalances = head;
        HashSet<String> allfiles = new HashSet<String>();
        for (String b: ancestor.getContents().keySet()){
            allfiles.add(b);
        }
        for (String b: given.getContents().keySet()) {
            allfiles.add(b);
        }
        for (String b: current.getContents().keySet()) {
            allfiles.add(b);
        }
        boolean conflict = false;
        HashMap<String, String> givenval = given.getContents();
        HashMap<String, String> currentval = current.getContents();
        HashMap<String, String> ancestorval = ancestor.getContents();
//        if (bname.equals("B")){
//            System.out.println(givenval);
//            System.out.println(currentval);
//        }
        for (String c: allfiles) {
            if (givenval.containsKey(c)) {
                if (ancestorval.get(c) != null && !givenval.get(c).equals(ancestorval.get(c))){
                    if (currentval.get(c).equals(ancestorval.get(c))) {
                        checkout(c, given.gethash());
                        staged.add(c);
                    }
                } else if (!ancestorval.containsKey(c) && !currentval.containsKey(c)) {
                    checkout(c, given.gethash());
                    staged.add(c);
                } else if (currentval.containsKey(c)) {
                    if (!currentval.get(c).equals(givenval.get(c))) {
                        Blob fir = Utils.readObject(Utils.join(".gitlet/", currentval.get(c)), Blob.class);
                        Blob sec = Utils.readObject(Utils.join(".gitlet/", givenval.get(c)), Blob.class);
                        String concat = "<<<<<<< HEAD" + fir.getSContents() + " in " + currentbranch
                                + "=======" + sec.getSContents() + " in " + bname + ">>>>>>>";
                        Utils.writeContents(new File(c), concat);
                        add(c);
                        conflict = true;
                    }
                } else if (!currentval.containsKey(c) && !givenval.get(c).equals(ancestorval.get(c))){
                    Blob sec = Utils.readObject(Utils.join(".gitlet/", givenval.get(c)), Blob.class);
                    String concat = "<<<<<<< HEAD in " + currentbranch
                            + "=======" + sec.getSContents() + " in " + bname + ">>>>>>>";
                    Utils.writeContents(new File(c), concat);
                    add(c);
                    conflict = true;
                }

            } else {
                if (ancestorval.containsKey(c)) {
                    if (currentval.containsKey(c) && currentval.get(c).equals(ancestorval.get(c))) {
                        new File(c).delete();
                        untracked.add(c);
                    } else if (currentval.containsKey(c) && !currentval.get(c).equals(ancestorval.get(c))) {
                        Blob fir = Utils.readObject(Utils.join(".gitlet/", currentval.get(c)), Blob.class);
                        String concat = "<<<<<<< HEAD" + fir.getSContents() + " in " + currentbranch
                                + "======= in " + bname + ">>>>>>>";
                        Utils.writeContents(new File(c), concat);
                        add(c);
                        conflict = true;
                    }
                }
            }
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict");
        }
        HashMap<String, String> staging = staged.getContents();
        HashMap<String, String> curr = new HashMap<String, String>();
        for (String name: allfiles){
            if (staging.containsKey(name)){
                curr.put(name, staging.get(name));
            } else if (!untracked.contains(name) && currentval.containsKey(name)){
                curr.put(name, currentval.get(name));
            }
        }
        Commit merged = new Commit(current.gethash(), given.gethash(), curr, currentbranch);
        staged = new Stage();
        String name = merged.gethash();
        branchHeads.put(currentbranch, name);
        branchHeads.put(bname, name);
        commits.add(name);
        head = name;
        File loc = new File(".gitlet/" + name);
        Utils.writeObject(loc, merged);
        untracked.clear();
    }
}
