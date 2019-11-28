package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The goal of this is to serve as a container for the files that we have, with ability
 * to compare two different blobs to each other to make the commit class easier.
 * A blob should contain the name of the file and the contents of a file
 * @author aviga
 */


public class Blob implements Serializable {


    public Blob(String filename){
        name = filename;
        File file = new File(filename);
        byteme = Utils.readContents(file);
        word = Utils.readContentsAsString(file);
        List<Object> temp = new ArrayList<Object>();
        temp.add(byteme);
        temp.add(name);
        temp.add(word);
        temp.add("Blob");
        hash = Utils.sha1(temp);
    }

    /** Retrieve the name of the file. */
    String getName(){
        return name;
    }
    /** Return the string of the file contents. */
    String getSContents(){
        return word;
    }
    /** Return the byte array of the file contents.*/
    byte[] getBContents(){
        return byteme;
    }
    /** Return the hash value */
    String gethash(){
        return hash;
    }

    /** The name of the file. */
    private String name;
    /** contents of the file as a byte array. */
    private byte[] byteme;
    /** Contents of the file as a string. */
    private String word;
    /** Hash Value of the Blob. */
    private String hash;




}
