import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Station implements Linkable {
    private String name;
    private List<Linkable> next;
    private int nbPrevious;
    private Set<String> components;

    public Station() {
        this.next = new LinkedList<>();
        this.components = new HashSet<>();
    }

    public Station(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSizeNext() {
        return this.next.size() + this.nbPrevious;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Station copy() {
        Station another = new Station(this.name);
        return another;
    }

    @Override
    public void linkTo(Linkable another) {
        if (!this.getListNext().contains(another)) {
            Station s = (Station)another;
            this.next.add(s);
            s.incNbPrevious();
        }
    }

    @Override
    public void linkBtw(Linkable another) {
        Station s = (Station)another;
        if (!this.getListNext().contains(another)) {
            this.next.add(s);
        }
        if (!another.getListNext().contains(this)) {
            s.getListNext().add(this);
        }
    }

    @Override
    public void addComponent(String line) {
        this.components.add(line);
    }

    @Override
    public List<Linkable> getListNext(){
    	return this.next;
    }

    @Override
    public Set<String> getComponents() {
        return this.components;
    }

    public void incNbPrevious() {
        this.nbPrevious++;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object another){
        if (!this.getClass().equals(another.getClass())) {
            return false;
        }
        return this.name.equals(another.toString());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
