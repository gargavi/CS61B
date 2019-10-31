import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    private double size = 0.0;
    private List<Node> total = new ArrayList<Node>(1);

    private double load = 2.0;

    @Override
    public void put(String s) {
        size = 1.0 + size;
         

    }

    @Override
    public boolean contains(String s) {
        return false; // FIXME
    }

    @Override
    public List<String> asList() {
        return null; // FIXME
    }

    public class Node {
        String _string;
        Node _next;
        public Node(String s){
            _string = s;
            _next = null;
        }
        public void add_next(Node a){
            _next = a;
        }
    }

}
