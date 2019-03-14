import java.util.Set;
import java.util.HashSet;

public class Node implements Comparable<Node> {
    private static int cpt = 0;
    private final int id;
    private Linkable data;
    private Node next;

    public Node(Linkable data) {
        this.id = cpt++;
        this.data = data;
    }

    public Linkable getData() {
        return this.data;
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    /**
    * Goes through each linked Node to count how many nodes there are.
    * @return int for the number of nodes
    */
    public int size() {
        Node current = this;
        int size =  0;

        while (current != null) {
            current = current.getNext();
            size++;
        }

        return size;
    }

    /**
    * Goes to the last Node.
    * @return Node representing the last linked Node, the one that has no next.
    */
    public Node getLast() {
        Node current = this;
        while (current.next != null) {
            current = current.next;
        }
        return current;
    }

    /**
    * Makes a copy of the current Node with its following Nodes
    * @return Node that is independent is its source.
    */
    public Node copy(Node stop) {
        Node copy = new Node(null);
        Node currentOnCopy = copy;
        Node currentOnThis = this;
        int stopId = stop.id;

        while (currentOnThis != null && stopId != currentOnThis.id) {
            currentOnCopy.next = new Node(currentOnThis.getData());
            currentOnCopy = currentOnCopy.next;
            currentOnThis = currentOnThis.next;
        }
        currentOnCopy.next = new Node(currentOnThis.getData());

        return copy.next;
    }

    /**
    * Makes the concatenation of the specified Node and the current one.
    * @param another the Node to append
    * @return  Node starting with the parameter Node and followed by the current Node
    */
    public Node concat(Node another) {
        if (another != null) {
            Node toJoin = another.getLast();
            if (toJoin.getData().equals(this.getData())) {
                toJoin.next = this.next;
            } else {
                toJoin.next = this;
            }
            return another;
        } else {
            return this;
        }
    }

    /**
    * Determines which components are crossed by following this linked Node
    * @return Set<String> containing all crossed components
    */
    public Set<String> whichComponentsCrossed() {
        Set<String> crossedComponents = new HashSet<>();
        Set<String> intersection;
        Node current = this;

        while (current.next != null) {
            intersection = Node.intersect(current.getData().getComponents(),  current.next.getData().getComponents());
            if (!intersection.isEmpty()) {
                crossedComponents.addAll(intersection);
            }
            current = current.next;
        }

        return crossedComponents;
    }

    /**
    * Determine whether the given Node can replace the specified segment of Nodes
    * without changing the crossed components
    * @param another replacement
    * @param to end Node of the current linked Node
    * @param origin the entire linked Node of the current Node
    * @param all crossed components
    * @return true replaceable
    * @return false not replaceable
    */
    public boolean isReplaceableInComponentsBy(Node another, Node to, Node origin, Set<String> all) {
        Set<String> fusion;

        // Determine the components before this;
        Node saveNextThis = this.next;
        this.next = null;
        fusion = origin.whichComponentsCrossed();
        this.next = saveNextThis;

        // Adding the components of another
        fusion.addAll(another.whichComponentsCrossed());

        // Adding components after
        fusion.addAll(to.whichComponentsCrossed());

        return fusion.containsAll(all);
    }

    /**
    * Replaces a segment of the current linked Node from a start with a specified one.
    * @param stop last Node of the replaced segment
    * @param with linked Nodes replacer
    */
    public void replaceUntil(Node stop, Node with) {
        this.next = (this.equals(with)) ? with.next : with;     // Skipping the first node in case of an equality
        Node last = with.getLast();
        last.next = (last.equals(stop)) ? stop.next : stop;     // Skipping the last node of the new path in case of equality
    }

    /**
    * Intersection of 2 Sets
    * @param first Set to intersect
    * @param Set to intersect
    * @param the intersection as a Set without modifying the parameters.
    */
    public static Set<String> intersect(Set<String> first, Set<String> second) {
        Set<String> intersection = new HashSet<>(first);
        intersection.retainAll(second);
        return intersection;

    }

    /**
    * Determines which components are crossed by following this linked Node
    * @return Set<String> containing all crossed components
    */
    public Set<String> getCrossedComponents() {
        Set<String> crossedComponents = new HashSet<>();
        Set<String> intersection;
        Node current = this;

        while (current.next != null) {
            intersection = Node.intersect(current.data.getComponents(),current.next.data.getComponents());
            if (intersection.size() >= 1) {
                crossedComponents.addAll(intersection);
            }
            current = current.next;
        }

        return crossedComponents;
    }

    public boolean equals(Node another) {
        return (this.getData().equals(another.getData()));
    }

    @Override
    public boolean equals(Object another) {
        if (!this.getClass().equals(another.getClass())) {
            return false;
        }
        Node anotherNode = (Node)another;
        return (this.equals(anotherNode));
    }

    @Override
    public int hashCode() {
        return this.getData().hashCode();
    }

    @Override
    public int compareTo(Node another) {
        return (this.size() - another.size());
    }

    @Override
    public String toString() {          // Print reversed chain
        StringBuilder strb = new StringBuilder();

        if (this.next == null) {
            return ("\n\tGroupe(s) : " + this.getData().getComponents() + "\t : " + this.getData().toString() + "\n\t");

        } else {
            strb.append(this.next);
            // Groupe = Composantes connexes
            strb.append("Groupe(s) : "  + this.data.getComponents());
            strb.append("\t : " +this.getData());
            strb.append("\n\t");

            return strb.toString();
        }

    }
}
