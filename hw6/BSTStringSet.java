import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        Node temp = new Node(s);
        Node a = _root;
        if (a == null){
            _root = temp;
        }
        else{
            while ((a.left != null && s.compareTo(a.s) < 0) || (a.right != null && s.compareTo(a.s) > 0)){
                if (s.compareTo(a.s) < 0){
                    a = a.left;
                } else {
                    a = a.right;
                }
            }
            if (a.left == null && s.compareTo(a.s) < 0){
                a.left = temp;
            } else {
                a.right = temp;
            }

        }

    }

    @Override
    public boolean contains(String s) {
        Node a = _root;
        while (a != null){
            if (a.s.compareTo(s) == 0){
                return true;
            } else if (s.compareTo(a.s) < 0){
                a = a.left;
            } else {
                a = a.right;
            }
        }
        return false;
    }
    public void addto(Node n, List temp){
        if (n.left != null){
            addto(n.left, temp);
        }
        temp.add(n.s);
        if (n.right != null){
            addto(n.right, temp);
        }

    }
    @Override
    public List<String> asList() {
        ArrayList<String> temp = new ArrayList<String>();
        Node a = _root;
        addto(_root, temp);
        return temp;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }


        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }
    private static class BSTIterator1 implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();
        private String lower;
        private String higher;

        /** A new iterator over the labels in NODE. */
        BSTIterator1(Node node, String low, String high) {
            lower = low;
            higher = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            if ((higher.compareTo(node.right.s) > 0)){
                addTree(node.right);
            }
            return node.s;
        }


        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null && lower.compareTo(node.s) < 0) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }


    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTIterator1(_root, low, high);

    }



    /** Root node of the tree. */
    private Node _root;
}
