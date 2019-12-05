package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/** The purpose of this class is to store the stage elements used by Gitlet
 * Should contain the names of all files that are staged and have methods to add to
 * stage and remove from the stage
 * @author aviga 
 */

public class Stage implements Serializable {

    /**A mapping from the file name to the hash of that file*/
    public HashMap<String, String> files;

    public Stage(){
        files = new HashMap<String, String>();
    }

    public void add(String file){
        File named = new File(file);
        Blob blob = new Blob(file);
        String blobh = blob.gethash();
        if (named.exists()) {
            if (files.containsKey(file)){
                String old = files.get(file);
                if (old != blobh){
                    File temp = new File(".gitlet/" + old);
                    temp.delete();
                    File loc = new File(".gitlet/" + blobh);
                    Utils.writeObject(loc, blob);
                }
            } else {
                files.put(file, blobh);
                File loc = new File(".gitlet/" + blobh);
                Utils.writeObject(loc, blob);
            }
        } else {
            throw Utils.error("File does not Exist");
        }
    }

    public HashMap<String, String> getContents() {
        return files;
    }

    public void remove(String file){
        if (files.containsKey(file)){
            String b = files.get(file);
            File cur = new File(".gitlet/" + b);
            cur.delete();
            files.remove(file);
        }
    }
    public boolean contains(String file){
        if (files.containsKey(file)){
            return true;
        }
        return false;
    }
    public void clear() {
        files.clear();
    }


}
