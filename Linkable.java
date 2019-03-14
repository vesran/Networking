import java.util.List;
import java.util.Set;

public interface Linkable {
    public void linkTo(Linkable another);
    public Linkable copy();
    public String getName();
    public void setName(String name);
    public int getSizeNext();
    public List<Linkable> getListNext();
    public void linkBtw(Linkable another);
    public void addComponent(String component);
    public Set<String> getComponents();
}
