import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
  * store Comparable objects. Instead, it can store any type of object
  * (represented by type T) and an associated priority value.
  * @author CS 61BL Staff*/
public class ArrayHeap<T> {

    /* DO NOT CHANGE THESE METHODS. */

    /* An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /* A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /* Returns the number of elments in the priority queue. */
    public int size() {
        return contents.size() - 1;
    }

    /* Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the node at INDEX to N */
    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 > contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /* Returns and removes the node located at INDEX. */
    private Node removeNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.remove(index);
        }
    }

    /* Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /* Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* A Node class that stores items and their associated priorities. */
    public class Node {
        private T item;
        private double priority;

        private Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        public T item() {
            return this.item;
        }

        public double priority() {
            return this.priority;
        }

        public void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return this.item.toString() + ", " + this.priority;
        }
    }


    /* Returns the index of the left child of the node at i. */
    private int getLeftOf(int i) {
        int a = 2*i;
        if (a > size()){
            return -1;
        } else {
            return a;
        }
    }

    /* Returns the index of the right child of the node at i. */
    private int getRightOf(int i) {
        int a = 2*i + 1;
        if (a > size()){
            return -1;
        } else {
            return a;
        }
    }

    /* Returns the index of the node that is the parent of the node at i. */
    private int getParentOf(int i) {
        if (i % 2 == 1){
            return (i-1)/2;
        } else {
            return i /2;
        }

    }

    /* Adds the given node as a left child of the node at the given index. */
    private void setLeft(int index, Node n) {
        setNode(getLeftOf(index), n);
    }

    /* Adds the given node as the right child of the node at the given index. */
    private void setRight(int index, Node n) {
        setNode(getRightOf(index), n);
    }

    /** Returns the index of the node with smaller priority. Precondition: not
      * both nodes are null. */
    private int min(int index1, int index2) {
        Node a = getNode(index1);
        Node b = getNode(index2);
        if (a == null && b == null){
            throw new Error();
        }
        if (a == null || a.priority() < b.priority()){
            return index2;
        } else if ( b == null || a.priority() > b.priority()) {
            return index1;
        } else {
            return index1;
        }
    }

    /* Returns the Node with the smallest priority value, but does not remove it
     * from the heap. */
    public Node peek() {
        return getNode(1);
    }

    /* Bubbles up the node currently at the given index. */
    private void bubbleUp(int index) {
        int parent = getParentOf(index);
        while (index != 1 && getNode(index).priority() > getNode(parent).priority() ){
            swap(index, getParentOf(index));
            index = parent;
            parent = getParentOf(index);
        }
    }

    /* Bubbles down the node currently at the given index. */
    private void bubbleDown(int index) {
        int right = getRightOf(index);
        int left = getLeftOf(index);
        while ((right != -1 && getNode(index).priority() > getNode(right).priority()) || (left != -1 && getNode(index).priority() > getNode(left).priority())){
            if (getNode(index).priority() > getNode(right).priority()){
                swap(index, right);
                index = right;

            } else {
                swap(index, left);
                index = left;
            }
            right = getRightOf(index);
            left = getLeftOf(index);
        }
    }

    /* Inserts an item with the given priority value. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
        if (size() > 1) {
            setNode(size() + 1, new Node(item, priority));
            swap(1, size());
            bubbleDown(1);
            bubbleUp(size());
        } else{
            setNode(size() + 1, new Node(item, priority));
        }
    }

    /* Returns the element with the smallest priority value, and removes it from
     * the heap. Same as dequeue, or poll. */
    public T removeMin() {
        swap(1, size());
        bubbleDown(1);
        return removeNode(size()).item();

    }

    /* Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the same
     * item. Check for item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        for (int a = 0; a < size(); a ++){
            Node C = getNode(a);
            if (C.item().equals(item)){
                double b = C.priority();
                C.setPriority(priority);
                if (b > C.priority()){
                    bubbleUp(a);
                } else {
                    bubbleDown(a);
                }
            }
        }

    }

}
