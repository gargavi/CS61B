package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** This class serves as the commit object for the Gitlet structure. Essentially it contains
 * hash references to blobs, and itself contains some information (data, message) and hash code
 * Commits can also have merges, and have parent id's and such.
 *
 */

public class Commit implements Serializable {


    public Commit(String tmessage, HashMap<String, String> files, String parent, String branch){
        message = tmessage;
        contents = files;
        _parent = parent;
        merge = false;
        _mergeparent = null;
        _branch = branch;
        Date temp = new Date();
        SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        time = format.format(temp);
    }
    public Commit(String firstp, String secondp, HashMap<String, String> files, String branch1) {
        message  = "Merged " + secondp + " into " + firstp;
        _parent = firstp;
        _mergeparent = secondp;
        merge = true;
        _branch = branch1;
        Date temp = new Date();
        SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        time = format.format(temp);
    }

    public Commit(Commit old){
        _parent = old.hashval();
        merge = false;
        _mergeparent = null;
        contents = new HashMap<String, String>();
        _branch = old.getbranch();
        Date temp = new Date();
        time = temp.toString();
        HashMap<String, String> prev = old.getContents();
        for (String b: prev.keySet()){
            contents.put(b, prev.get(b));
        }
    }
    /** Put an element in the contents */
    public void feed(String name, String hash) {
        contents.put(name, hash);
    }

    /** Remove an element from the contents */
    public void rem (String name) {
        contents.remove(name);
    }

    /** Used to create the inital commit */
    public Commit(){
        _parent = null;
        merge = false;
        _mergeparent = null;
        contents = new HashMap<String, String>();
        _branch = "master";
        Date temp = new Date(0);
        SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        time = format.format(temp);
        message = "initial commit";
    }

    private String hashval(){
        List<Object> total = new ArrayList<Object>();
        total.add(message);
        total.add(time);
        total.add(_branch);
        total.add("Commit");
        if (_parent == null){
            return Utils.sha1(total);
        } else if (merge){
            total.add(_mergeparent);
            for (String b: contents.values()){
                total.add(b);
            }
            return Utils.sha1(total);
        } else {
            for (String b: contents.values()){
                total.add(b);
            }
            return Utils.sha1(total);
        }
    }
    /** Return the hash value */
    String gethash(){
        return hashval();
    }
    /** Return the branch this commit belongs too. */
    String getbranch(){
        return _branch;
    }
    /** Get the message of the commit*/
    public String getMessage(){
        return message;
    }
    /** Get the time of the commit*/
    public String getTime(){
        return time;
    }
    /** Retrieves name of the parent */
    public String getParent(){
        return _parent;
    }
    /** Retrieves name of the second parent */
    public String getSParent(){
        return _mergeparent;
    }
    /** Retrieves the hashes of the blobs*/
    public Collection<String> getBlobs(){
        return contents.values();
    }
    /** Retrieves the contents of the commit */
    public HashMap<String, String> getContents(){
        return contents;
    }


    /** Whether it was merged.*/
    private Boolean merge;
    /** Name of the branch. */
    private String _branch;
    /** Name of merge parent*/
    private String _mergeparent;
    /** Parent id via hash value */
    private String _parent;
    /** Represents the time of the commit */
    private String time;
    /** Represents a mapping from file name to the hash code of the file */
    private HashMap<String, String> contents;
    /** Represents the message that is associated with the commit */
    private String message;
}
