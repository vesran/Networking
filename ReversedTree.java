import java.util.List;
import java.util.ArrayList;

public class ReversedTree {
    private List<Node> leafs;
    private List<Node> upperLeafs;

    public ReversedTree(Node first) {
        this.leafs = new ArrayList<>();
        this.upperLeafs = new ArrayList<>();
        this.leafs.add(first);
    }

    public ReversedTree(Linkable first) {
        this(new Node(first));
    }

    /**
    * Finds the maximal length of the ReversedTree
    * @returns int that represents the maximal length
    */
    public int maxLength() {
        return this.maxLengthNode().size();
    }

    /**
    * Finds the Node with the maximal length in the tree
    * @return Node  with the maximal length in the tree
    */
    public Node maxLengthNode() {
        Node maxNode = this.leafs.get(0);

        for (Node leaf : this.leafs) {
            if (maxNode.size() < leaf.size()){
                maxNode = leaf;
            }
        }

        return maxNode;
    }

    /**
    * Makes a copy of the tree's leafs
    * @return List<Linkable> that contains the Linkable extracted from the leaf Node
    */
    public List<Linkable> copyOfLeafsElement() {
        List<Linkable> another = new ArrayList<>();
        for (int i = 0; i <this.leafs.size(); i++) {
            another.add(this.leafs.get(i).getData());
        }
        return another;
    }

    /**
    * Appends and links two Node that wrap the given Linkables into the tree
    * @param before Node not in the tree to link
    * @param after Node must be in the tree
    */
    public void add(Linkable before, Linkable after) {
        // Looking for the actual node to link
            // in leafs
        for (Node current : this.leafs) {
            if (current.getData().equals(after)) {
                this.add(new Node(before), current);
                return;
            }
        }
            // in upperLeafs
        for (Node current : this.upperLeafs) {
            if (current.getData().equals(after)) {
                this.add(new Node(before), current);
                return;
            }
        }
    }

    /**
    * Appends and links two Node into the tree
    * @param before Node not in the tree to link
    * @param after Node must be in the tree
    */
    private void add(Node before, Node after) {
        if (this.leafs.contains(after)){
            before.setNext(after);
            this.putBack(after);
            this.leafs.add(before);

        } else if (this.upperLeafs.contains(after)) {
            before.setNext(after);
            this.leafs.add(before);

        } else {
            System.out.println("Le " + after + " n'est pas dans le reversedTree. Il contient " + this);

        }
    }

    /**
    * Puts a Node from the leafs to the upper-leafs section
    * @param Node to do the operation
    */
    private void putBack(Node target) {
        if (this.leafs.contains(target)) {
            this.leafs.remove(target);
            this.upperLeafs.add(target);
            this.upperLeafs.remove(target.getNext());
        }
    }

    public Node getNode(String target){
        for(Node n : leafs){
            if(n.getData().getName() == target){
                return n;
            }
        }
        return null;
    }

	@Override
    public String toString() {
        return this.leafs.toString();
    }

}
