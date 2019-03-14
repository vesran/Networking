
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class Person implements Linkable{

    private String name;
    private List<Linkable> next;
    private Set<String> components;

    public Person() {
        this.next = new LinkedList<>();
        this.components = new HashSet<>();
    }

    public Person(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSizeNext() {
        return this.next.size();
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
            Person p = (Person)another;
            this.next.add(p);
        }
    }

    @Override
    public void linkBtw(Linkable another) {
        Person p = (Person)another;
        if (!this.getListNext().contains(another)) {
            this.linkTo(p);
        }
        if (!another.getListNext().contains(this)) {
            p.linkTo(this);
        }
    }

    @Override
    public void addComponent(String component) {
        this.components.add(component);
    }

    @Override
    public List<Linkable> getListNext(){
    	return this.next;
    }

    @Override
    public Set<String> getComponents() {
        return this.components;
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
