import java.util.Scanner;

class Main {

	/**
	* Asks an integer in the range [from, to]
	*/
	public static int askIntBtw(int from, int to) {
		Scanner scan = new Scanner(System.in);
		int answer = -1;
		while (answer == -1) {
			System.out.print("Votre reponse : ");

			try {
				answer = scan.nextInt();
				if (answer < from || answer > to) {
					System.out.println("Veuillez entrer un nombre entre " + from + " et " + to + ".");
					answer = -1;
				}

			} catch (RuntimeException e) {
				System.out.println("Veuillez entrer un nombre.");
				scan.nextLine();		// Makes the buffer empty
				answer = -1;
			}
		}
		System.out.println();		// Break line before results
		return answer;
	}

	/**
	* Displays menu and asks for an action
	*/
	public static int menuMetro() {
		System.out.println("-------------------------------------------------");
		System.out.println("Menu :");
		System.out.println("\t0- Quitter.");
		System.out.println("\t1- Trouver les 10 elements les plus connectes.");
		System.out.println("\t2- Trouver le plus court chemin d'un element a un autre.");
		System.out.println("\t3- Trouver le plus court chemin d'un element a un autre en passant par tous les groupes/composantes.");
		System.out.println("Que voulez-vous faire ?");

		return Main.askIntBtw(0, 3);
	}

	public static int menuPerson() {
		System.out.println("-------------------------------------------------");
		System.out.println("Menu :");
		System.out.println("\t0- Quitter.");
		System.out.println("\t1- Trouver les 10 elements les plus connectes.");
		System.out.println("\t2- Trouver le plus court chemin d'un element a un autre.");
		System.out.println("\t3- Verifier les six degrees de separation.");
		System.out.println("Que voulez-vous faire ?");

		return Main.askIntBtw(0, 3);
	}

	/**
	* Ask the user to enter a node name.
	*/
	public static <T extends Linkable> String askName(Network<T> net) {
		Scanner scan = new Scanner(System.in);
		String answer = null;
		Linkable elementFound = null;

		while (elementFound == null) {
			System.out.print("Entrer un nom : ");
			answer = scan.nextLine().trim();
			elementFound = net.get(answer);
		}
		System.out.println("La reponse \"" + answer + "\" a ete enregistree.");
		return answer;
	}

	/**
	* Showcase for metro
	*/
	public static void showMetroCase(String fileMetro) {
		boolean wantToContinue = true;
		String startName;
		String endName;
		Node itinerary;

		// Loading the network with the given file
		Network<Station> net = new Network<>(new Station());
		if (!net.fill(fileMetro))	return;
		System.out.println("Les donnees sont chargees.");

		// Interaction with the user
		while (wantToContinue) {
			int choice = Main.menuMetro();

			switch(choice) {
				case 0:		// Quit
					wantToContinue = false;
					break;

				case 1:		// Showing the 10 most connected Linkables
					net.showTop10();
					break;

				case 2:		// Ask for two names to find the shortest path
					startName = Main.<Station>askName(net);
					endName = Main.<Station>askName(net);
					System.out.println(net.getItinerary(startName, endName));
					break;

				case 3:		// Ask for two names to find the shortest path
					startName = Main.<Station>askName(net);
					endName = Main.<Station>askName(net);
					itinerary = net.getBestItineraryWithAllComponents(startName, endName);
					System.out.println(itinerary + "\nLongueur : " + itinerary.size());
					break;
				}
		}
	}

	/**
	* Showcase for social network
	*/
	public static void showSocialNetCase(String filePerson) {
		boolean wantToContinue = true;
		String startName;
		String endName;
		Node itinerary;

		// Loading the network with the given file
		Network<Person> net = new Network<>(new Person());
		if (!net.fill(filePerson))	return;
		System.out.println("Les donnees sont chargees.");

		// Interaction with the user
		while (wantToContinue) {
			int choice = Main.menuPerson();

			switch(choice) {
				case 0:		// Quit
					wantToContinue = false;
					break;

				case 1:		// Showing the 10 most connected Linkables
					net.showTop10();
					break;

				case 2:		// Ask for two names to find the shortest path
					startName = Main.<Person>askName(net);
					endName = Main.<Person>askName(net);
					System.out.println(net.getItinerary(startName, endName));
					break;

				case 3:		// Ask for two names to find the shortest path
					net.checkSeparationDegree();
					break;
				}

		}
	}

    public static void main(String [] args) {
		System.out.println("Selectionnez :");
		System.out.println("\t0- metro-wl.txt");
		System.out.println("\t1- sonet-200.txt");
		System.out.println("\t2- sonet-1000.txt");

		int mode = Main.askIntBtw(0, 2);

		switch (mode) {
			case 0:
				Main.showMetroCase("metro-wl.txt");
				break;

			case 1:
				Main.showSocialNetCase("sonet-200.txt");
				break;

			case 2:
				Main.showSocialNetCase("sonet-1000.txt");	// Can change the filename for social network case
				break;
		}


    }
}
