package gitlet;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** This class serves as the commit object for the Gitlet structure. Essentially it contains
 * hash references to blobs, and itself contains some information (data, message) and hash code
 * Commits can also have merges, and have parent id's and such.
 *
 */

public class Commit implements Serializable {


    public Commit(String tmessage, HashMap<String, Blob> files, String parent, String branch){
        message = tmessage;
        for (String b: files.keySet()){
            Blob blob = files.get(b);
            contents.put(blob.getName(),blob.gethash());
        }
        _parent = parent;
        merge = false;
        _mergeparent = null;
        _branch = branch;
        ZonedDateTime temp = ZonedDateTime.now();
        time = temp.format(DateTimeFormatter.ofPattern
                        ("EEE MMM d HH:mm:ss yyyy xxxx"));
        hash = hashval();
    }
    public Commit(){
        _parent = null;
        merge = false;
        _mergeparent = null;
        _branch = "master";
        time = "January 1, 1970, Thursday, 00:00:00";
        message = "inital commit";
        hash = hashval();

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
        return hash;
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

    /** The hash value.*/
    private String hash;
}
