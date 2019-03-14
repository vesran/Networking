import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.function.Predicate;
import java.util.Scanner;

public class Network <T extends Linkable> {
    private final T reference;
    private Set<Linkable> nodes;
    private Set<String> allComponents;

    public Network(T reference) {
        this.reference = reference;
        this.nodes = new HashSet<>();
        this.allComponents = new HashSet<>();
    }

    /**
     * Load all the data found in the file
     *
     * @param filename -> String
     */
    public boolean fill(String filename) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));

            // Filling the set of Linkables
            this.fillNodes(br);

            // Linking nodes
            this.linkNodes(br);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Verifiez si le fichier est bien present ou au bon endroit.");
            return false;
        }
        return true;
    }

    /**
     * Add all the Linkable found in the file to the Set of Linkable
     *
     * @param br -> BufferedReader
     * @throws IOException
     */
    private void fillNodes(BufferedReader br) throws IOException {
        String line;

        while ((line = br.readLine()) != null && !line.startsWith("###")) {
            if (!line.startsWith("%") && !line.equals("\n")) {
                this.addNodes(line.trim());
            }
        }
    }

    /**
     * Create a new Linkable by making a copy of the reference and add it to the Set of Linkable
     *
     *  @param str -> String
     */
    private void addNodes(String str) {
        Linkable another  = this.reference.copy();   // Makes a copy of the reference
        another.setName(str);                       // Setting up the copy
        this.nodes.add(another);                    // Adding the copy to the set
    }

    /**
     * Links Nodes that are connected according to the file given
     *
     * @param br -> BufferedReader
     * @throws IOException
     */
    private void linkNodes(BufferedReader br) throws IOException {
        String line;
        String [] array;
        Linkable first;
        Linkable second;

        while ((line = br.readLine()) != null) {
            if (!line.startsWith("%")) {
                // Linking nodes
                array = line.split(":");
                if (array.length == 1) {                // is oriented
                    array = line.split(">");

                    if (array.length == 2) {
                        first = this.get(array[0]);
                        second = this.get(array[1]);
                        first.linkTo(second);
                        System.out.println("Linking : " + first + " -----> " + second);

                    } else if (array.length == 3) {
                        first = this.get(array[0]);
                        second = this.get(array[2]);
                        first.linkTo(second);
                        first.addComponent(array[1]);
                        second.addComponent(array[1]);
                        this.allComponents.add(array[1]);
                        System.out.println("Linking : " + first + " -----> " + second + " in component " + array[1]);
                    }

                } else if (array.length == 2) {                                // not oriented
                    first = this.get(array[0]);
                    second = this.get(array[1]);
                    first.linkBtw(second);
                    System.out.println("Linking : " + first + " <----> " + second);

                } else if (array.length == 3) {
                    first = this.get(array[0]);
                    second = this.get(array[2]);
                    first.linkBtw(second);
                    first.addComponent(array[1]);
                    second.addComponent(array[1]);
                    this.allComponents.add(array[1]);
                    System.out.println("Linking : " + first + " <----> " + second + " in component " + array[1]);
                }
            }
        }
    }

    /**
    *   Returns a Linkable from the set of nodes with the specified name.
    *
    *   @param name -> String
    *   @return null if the name is not found in the Set of nodes
    *   @return Linkable if the name is found in the Set of nodes
    */
    public Linkable get(String name) {
        Iterator<Linkable> it = this.nodes.iterator();
        Linkable element;

        while (it.hasNext()) {
            element = it.next();
            if (element.getName().equals(name))  return element;
        }
        System.out.println("Le nom " + name + " n'existe pas.");

        return null;
    }

    /**
    *   Display on command line the 10 first Linkable with maximum connections in order.
    *
    */
    public void showTop10() {
		System.out.println("Top 10 des noeuds les plus connectes :");
        this.getTop(10).forEach( x->System.out.println("\t" + "Nombre de connexions : "+ x.getSizeNext() + " -- " +  x) );               // Displaying in separate lines
    }

    /**
    *   Returns a sorted list with the n-first Linkable with maximum connections.
    *
    *   @param n -> int
    *   @return List<Linkable>
    */
    public List<Linkable> getTop(int n) {
        List<Linkable> sortedList = new ArrayList<>(this.nodes);        // Convert set to list (so that we can use Collection.sort)
        Collections.sort(sortedList, (first, second) ->
                {
            return (second.getSizeNext() - first.getSizeNext() == 0) ?
                        first.getName().compareTo(second.getName()) : second.getSizeNext() - first.getSizeNext();
        });  // Sorting
        return new ArrayList<>(sortedList.subList(0, n));               // Returning the n first Linkable as a list
    }

    /**
    * Returns a node that contains an itinerary between two Linkable names
    *
    * @param startName, endName -> string
    * @return Node
    */
	public Node getItinerary(String startName, String endName) {
		if (startName.equals(endName)) {
			System.out.println("Il s'agit du meme noeud.");
		}

		System.out.println("Chemin depuis \"" + startName + "\" vers \"" + endName + "\" :");
		Linkable start = this.get(startName);
		Linkable end = this.get(endName);

		if(start == null && end == null) {
			throw new InputMismatchException("\n\nLes noms " + startName + ", " + endName + " sont introuvables.\n");
		}else if(start == null) {
			throw new InputMismatchException("\n\nLe nom " + startName + " est introuvable.\n");
		}else if(end == null){
			throw new InputMismatchException("\n\nLe nom " + endName + " est introuvable.\n");
		}else {

		return this.getItinerary(start, end);
		}
	}

    /**
     * Returns a node that contains an itinerary between two Linkable
     *
     * @param start,end -> Linkable
     * @return Node
    */
    private Node getItinerary(Linkable start, Linkable end) {
		if (start.equals(end)) {
			return new Node(start);
		}
		return this.getItinerary(start, (x)->(x.equals(end)));
    }

    /**
     * Returns a node that contains an itinerary from the specified Linkable until the stop condition is true
     *
     * @param start, stopCondition -> Linkable, Predicate<Linkable>
     * @return Node
    */
    public Node getItinerary(Linkable start, Predicate<Linkable> stopCondition) {
	    Set<Linkable> collector = new HashSet<>();
        collector.add(start);
        ReversedTree tree = new ReversedTree(start);
        List<Linkable> tmp;
        Set<Linkable> collectorOld = null;

        while(!collector.equals(collectorOld)){
            tmp = tree.copyOfLeafsElement();
            collectorOld = new HashSet<>(collector);
            for(Linkable l : tmp){
                Collections.shuffle(l.getListNext());
                for(Linkable next : l.getListNext()){

                    if(!collector.contains(next)){
                        tree.add(next,l);
                        collector.add(next);
                        if(stopCondition.test(next)){
                            return tree.getNode(next.getName());
                        }
                    }

                }

            }
        }

        throw new InputMismatchException("\n\n\nPas de chemin trouvé");
    }

    /**
     * Return a tree that all itinerary who started with a specified Linkable
     *
     * @param start -> Linkable
     * @return ReversedTree
     */
    public ReversedTree getAllItinerary(Linkable start){
        Set<Linkable> collector = new HashSet<>();
        collector.add(start);
        ReversedTree tree = new ReversedTree(start);
        List<Linkable> tmp;

        Set<Linkable> collectorOld = null;

        while(!collector.equals(collectorOld)){
            tmp = tree.copyOfLeafsElement();
            collectorOld = new HashSet<>(collector);
            for(Linkable l : tmp){
                for(Linkable next : l.getListNext()){

                    if(!collector.contains(next)){
                        tree.add(next,l);
                        collector.add(next);
                    }
                }
            }
        }
        return tree;
    }

    /**
    * Return true if the six degrees of separation is satisfied
    *
    * @return boolean
    */
    public boolean checkSeparationDegree(){
        ReversedTree tree;
        int size;
        for(Linkable l : this.nodes){
            tree = this.getAllItinerary(l);
            size = tree.maxLength() - 1;        // Get separation degrees not length
           if(size > 6){
                System.out.println("On n'observe pas la theorie des six degres de separation.");
                System.out.println("On a par exemple le noeud " + l.getName() + " ayant un chemin de degre de separation minimale " + size + " vers un certain noeud.");
                System.out.println("Voir la longueur du chemin ci-dessous : ");
                System.out.println(tree.maxLengthNode());
                return false;

            }
        }
        System.out.println("On observe la theorie des six degres de séparation.");
        return true;
    }

    /**
    *   Gets an itinerary from a start Node to the first Linkable found with an unvisited component.
    *
    *   @param start, componenetsToCross -> Linkable, Set<String>
    *   @return Node
    */
    public Node findItineraryToDifferentComponent(Linkable start, Set<String> componentsToCross) {
        Predicate<Linkable> p = (x) -> (Node.intersect(x.getComponents(), componentsToCross).size() >= 1);
        Node itinerary = this.getItinerary(start, p);
        return itinerary;
    }

    /**
    *   Makes the path shorter considering all parts by replacing some of them by a shorter and equivalent one
    *
    *   @param path -> Node
    */
    public void shortenAll(Node path) {
        Node current = path;
        while (current != null) {
            this.shorten(current, path);
            current = current.getNext();
        }
    }

    /**
    *   Makes the path shorter by replacing some part of the path by a shorter and equivalent one
    *
    *   @param path, origin -> Node, Node
    */
    public void shorten(Node path, Node origin) {
        Node stop = path.getNext();
        Node shortestPath;

        while (stop != null) {
            shortestPath = this.getItinerary(stop.getData(), path.getData());

            if (path.isReplaceableInComponentsBy(shortestPath, stop, origin, this.allComponents)) {
                path.replaceUntil(stop, shortestPath);
            }
            stop = stop.getNext();
        }
    }

    /**
    *   Returns an itinerary that go through all defined components of the network
    *
    *   @param start Linkable
    *   @param end -> Linkable
    *   @return Node
    */
    public Node getItineraryWithAllComponents(Linkable start, Linkable end) {
        Linkable current = start;
        Set<String> componentsToCross = new HashSet<>(this.allComponents);
        Set<String> crossedComponents;
        Node path = new Node(start);
        Node subPath = null;
        Node nextSubPath = null;

        String [] newComponentsCrossed = new String[0];

        while (true) {
            subPath = this.getItinerary(current, end);
            if (subPath == null) {
                throw new InputMismatchException("\n\n"+current + " et " + end + " ne font pas partis de la meme composante connexe.\n");
            }
            crossedComponents = subPath.getCrossedComponents();

            if (crossedComponents.containsAll(componentsToCross)) {
                path = path.concat(subPath);
                this.shortenAll(path);
                return path;

            } else {
                nextSubPath = this.findItineraryToDifferentComponent(current, componentsToCross);

                path = path.concat(nextSubPath);        // Fusion des deux paths

                // Select only one new component crossed
                newComponentsCrossed = Node.intersect(path.whichComponentsCrossed(), componentsToCross)
                                            .toArray(newComponentsCrossed);
                if (newComponentsCrossed.length != 0) {
                    crossedComponents.add(newComponentsCrossed[0]);
                    componentsToCross.remove(newComponentsCrossed[0]);
                }

                current = path.getData();      // MAJ current to the last destination

            }
        }
    }

	/**
     * Returns best itinerary among a few attempts
     *
     * @param startName,endName -> String, String
     * @return Node
     */
	public Node getBestItineraryWithAllComponents(String startName, String endName) {
		Linkable start = this.get(startName);
		Linkable end = this.get(endName);

		if(start == null && end == null) {
			throw new InputMismatchException("\n\nLes noms " + startName + ", " + endName + " sont introuvables.\n");

		} else if(start == null) {
			throw new InputMismatchException("\n\nLe nom " + startName + " est introuvable.\n");

		} else if(end == null) {
			throw new InputMismatchException("\n\nLe nom " + endName + " est introuvable.\n");

		} else {
			return getBestItineraryWithAllComponents(start, end);
		}
	}

    /**
     * Returns best itinerary among a few attempts
     *
     * @param start,end -> Linkable, Linkable
     * @return Node
     */
    private Node getBestItineraryWithAllComponents(Linkable start, Linkable end) {
		if (start.getComponents().size() <= 0 || end.getComponents().size() <= 0) {
			System.out.println("Les entrees n'appartiennent a aucune composantes. Appuyez sur Entree pour continuer.");
			(new Scanner(System.in)).nextLine();
        }

        List<Node> allPaths = new ArrayList<>();
		int nbIterations = 20;
		DecimalFormat nbFormat = new DecimalFormat("#.00");

		// Simulating different paths
		System.out.println("\nCalcul du meilleur chemin en passant par toutes les composantes :");
        for (int i = 1; i <= nbIterations; i++) {
            allPaths.add(this.getItineraryWithAllComponents(start, end));
            System.out.println(nbFormat.format((float)i/(float)nbIterations*100) +"% (Chargement)");
        }
		// Sort to get the shortest path
        Collections.sort(allPaths, (x, y) -> x.compareTo(y));
        return allPaths.get(0);
    }

}
