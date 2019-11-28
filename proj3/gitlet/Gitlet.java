package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/** This will help us manage the git repository and all the commmands we'll be using
 *
 */

public class Gitlet implements Serializable {

    private ArrayList<String> untracked;

    private HashMap<String, String> tracked;

    private ArrayList<String> branches;

    private ArrayList<String> commits;

    private String head;

    private boolean inited;

    public Gitlet(){
        inited = false;
        untracked = new ArrayList<String>();
        tracked = new HashMap<String, String>();
        commits = new ArrayList<String>();

    }

    /** Init should create a gitlet repository if one doesn't exist
     * And should create a new commit to put in it
     */
    public void init() throws IOException{
        if (!inited){
            throw Utils.error("A Gitlet version-control system already exists in the current directory.");
        } else {
            inited = true;
            Files.createDirectory(Paths.get(".gitlet"));
            Files.createDirectory(Paths.get(".gitlet/commits"));
            Files.createDirectory(Paths.get(".gitlet/staging"));
            Files.createDirectory(Paths.get(".gitlet/repo"));
            Commit initial = new Commit();
            String initName = initial.gethash();
            commits.add(initName);
            File loc = new File(".gitlet/commits/" + initName);
            Utils.writeObject(loc, initial);
            branches.add("master");
            head = initName;
        }

    }

    public void add(String file) {
        if (inited) {
            try {
                File relevant = new File(file);
            } catch (Exception expr){
                throw Utils.error("File does not exist");
            }
            Blob blob = new Blob(file);
            

        } else {
            throw Utils.error("Git Repository hasn't been initialized")
        }



    }


}
