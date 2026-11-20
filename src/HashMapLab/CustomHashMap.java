package HashMapLab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class CustomHashMap {
	private Node[] pokedex;// the list that pokemon are being held in
	private int size;
	private static final double MAX_LOADFACTOR = .75;
	private static final double MIN_LOADFACTOR = .1;

	public CustomHashMap(int list_cap) {
		this.pokedex = new Node[list_cap];
		size = 0;
	}

	public Node[] getPokedex() {
		return pokedex;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	// loadfile() adds entrues to the map and displays only the keys on the screen

	/**
	 * put the pokemon in this specific spot in the list to demonstrate collisions
	 * 
	 * @param e - the entry of pokemon and the key
	 * @return the previous pokemon in that spot
	 */
	public Pokemon put(int index, Entry e) {
		if (index >= pokedex.length || index < 0) {
			PokePanel.getDisplay().append("\"" + index + "\" isn't a valid index! \n");
			;
			return null;
		}

		if (e == null) {// we do not accept them putting a null pokemon into the pokedex
			PokePanel.getDisplay().append(
					"Cannot add a \"null\" to the pokedex! If you want to remove an index, use the \"Remove Pokemon\" button instead!\n");
			return null;
		}
		Pokemon output = null;
		if (pokedex[index] == null) {// if there isn't a pokemon in the desired slot
			pokedex[index] = new Node(e);
			PokePanel.getDisplay().append("Added the Pokemon " + e + " to the Pokedex! No chaining was necessary!\n");
			size++;// increment size since we aren't replacing a Pokemon
		} else {
			Node runner = pokedex[index];
			while (runner.getNext() != null && !runner.getData().getKey().equals(e.getKey())) {
				// traverse linked list of nodes until we can place this node at the end or if
				// we find the same key
				runner = runner.getNext();
			}
			if (!runner.getData().getKey().equals(e.getKey())) {// if we didn't land on an entry with the same key as e
				runner.setNext(new Node(e));// chain (showing collisions)
				size++;// increment size since we aren't replacing a Pokemon
				// don't get anything so it will output null

				PokePanel.getDisplay()
						.append("Added the Pokemon " + e + " to the Pokedex! It was chained due to collisons!\n");
			}
			// replace the current node with this one if the key is the same
			else {
				output = runner.getData().getValue();// get the pokemon that will point to e
				runner.setData(e);// no chaining because it replaces the same key
				PokePanel.getDisplay().append("Added the Pokemon " + e + " to the Pokedex! It replaced " + output
						+ " due to having the same key!\n");
			}
		}
		rehash();// Check if the size change makes a rehash necessary
		return output;
	}

	/**
	 * Places an entry where it should be in the map (according to it's key)
	 * 
	 * @param e - The "Entry" object being placed
	 * @return the value of an "Entry" object that was replaced due to it sharing
	 *         the same key as "e" (will return null if no Entry object was
	 *         replaced)
	 */
	public Pokemon put(Entry e) {
		if (e == null) {
			return null;
		}
		Pokemon output = null;
		int index = Math.abs(e.getKey().hashCode() % pokedex.length);
		if (pokedex[index] == null) {
			pokedex[index] = new Node(e);
			PokePanel.getDisplay().append("Added the Pokemon " + e + " to the Pokedex! No chaining was necessary!\n");
			size++;// increment size since we aren't replacing a Pokemon
		} else {
			Node runner = pokedex[index];
			while (runner.getNext() != null && !runner.getData().getKey().equals(e.getKey())) {
				// traverse linked list of nodes until we can place this node at the end or if
				// we find the same key
				runner = runner.getNext();
			}
			if (!runner.getData().getKey().equals(e.getKey())) {// if we didn't land on an entry with the same key as e
				runner.setNext(new Node(e));
				PokePanel.getDisplay()
						.append("Added the Pokemon " + e + " to the Pokedex! It was chained due to collisons!\n");
				size++;// increment size since we aren't replacing a Pokemon
				// don't get anything so it will output null
			}
			// replace the node in the map with this one if the key is the same
			else {
				output = runner.getData().getValue();// get the pokemon we replaced
				runner.setData(e);// we don't increment size because we replaced Node w/ same key
				PokePanel.getDisplay().append("Added the Pokemon " + e + " to the Pokedex! It replaced " + output
						+ " due to having the same key!\n");
			}
		}
		rehash();// Check if size change makes resizing the list required
		return output;
	}

	/**
	 * Returns the Pokemon with this key in the map
	 * 
	 * @param k - The specified Key object
	 * @returns the Pokemon
	 */
	public Pokemon get(PokeKey k) {
		if (k == null)
			return null;
		int index = Math.abs(k.hashCode() % pokedex.length);// index of where it should be
		Node runner = pokedex[index];
		while (runner != null && !runner.getData().getKey().equals(k)) {
			// while the runner's key is not the same as the desired key and it isn't null
			runner = runner.getNext();
		}
		if (runner == null) {
			PokePanel.getDisplay().append("Pokemon not found.");
			return null;
		}
		PokePanel.getDisplay().append(runner.getData().toString());
		return runner.getData().getValue();// only occurs when we find the entry with the same key

	}

	/**
	 * Removes the Pokemon with this key in the map
	 * 
	 * @param k - The specified Key object
	 * @returns the Pokemon being removed
	 */
	public Pokemon remove(PokeKey k) {
		if (k == null)
			return null;
		int index = Math.abs(k.hashCode() % pokedex.length);// index of where it should be
		Node runner = pokedex[index];
		Node previous = null;
		while (runner != null && !runner.getData().getKey().equals(k)) {
			// while the runner's key is not the same as the desired key and it isn't null
			previous = runner;
			runner = runner.getNext();
		}
		if (runner == null) {
			PokePanel.getDisplay().append("Cannot remove: Pokemon not found.");
			return null;// Pokemon with specified key isn't found
		}
		if (previous == null) {// if it is the head of the linked list
			pokedex[index] = runner.getNext();// make it's next node (or null) the new head (skipping over the removed
												// entry entirely)
		} else {
			previous.setNext(runner.getNext());// Make the previous one skip over the removed entry
		}
		runner.setNext(null);// so the list can't be accessed by this value anymore
		size--;// decrease size if we are removing a Pokemon
		rehash();// Check if the size change requires us to rehash
		PokePanel.getDisplay().append(runner.getData().toString());
		return runner.getData().getValue();// returns the node we removed
	}

	/**
	 * Updates the HashMap's capacity to be not too large or small (based on the
	 * loadfactors)
	 */
	public void rehash() {
		if (pokedex.length != 100 && loadFactor() < MIN_LOADFACTOR) {// if the size gets too small
			// create a new list that is either 1/2x the previous list, or 100 (whichever is
			// bigger)
			CustomHashMap new_map = new CustomHashMap((int) (Math.max(pokedex.length * .5, 100)));
			for (int i = 0; i < pokedex.length; i++) {// iterate through the entirety of the old hashmap
				if (pokedex[i] == null)// skip the empty slots in the map
					continue;
				Node runner = pokedex[i];
				while (runner != null) {// while there is still nodes left in the individual linked lists
					new_map.putWithoutRehashing(runner.getData());// put these into the new map
					runner = runner.getNext();
				}
			}
			pokedex = new_map.getPokedex();// replace the old map with this new, rehashed, one!
		} else if (loadFactor() > MAX_LOADFACTOR) {// If the size gets too large
			// create a new map 2x the capacity of the previous map
			CustomHashMap new_map = new CustomHashMap(pokedex.length * 2);
			for (int i = 0; i < pokedex.length; i++) {// iterates through entire hashmap
				if (pokedex[i] == null)
					continue;
				Node runner = pokedex[i];
				while (runner != null) {// while there is still nodes left in the individual linked lists
					new_map.putWithoutRehashing(runner.getData());// does the hashing itself
					runner = runner.getNext();
				}
			}
			pokedex = new_map.getPokedex();
			size = new_map.getSize();
		}
	}

	/**
	 * Used for the rehashing method to prevent infinite loops between the put() and
	 * rehash() methods when rehashing
	 * 
	 * @param e - The Entry being placed into the new map
	 */
	private void putWithoutRehashing(Entry e) {// CONTINUE HERE!!! FIX SO IT DOESN'T RETURN ANYTHING AND ONLY DOES IT'S
												// JOB!
		if (e == null) {
			return;
		}
		int index = Math.abs(e.getKey().hashCode() % pokedex.length);
		if (pokedex[index] == null) {
			pokedex[index] = new Node(e);
			size++;
		} else {
			Node runner = pokedex[index];
			while (runner.getNext() != null && !runner.getData().getKey().equals(e.getKey())) {
				// traverse linked list of nodes until we can place this node at the end or if
				// we find the same key
				runner = runner.getNext();
			}
			if (!runner.getData().getKey().equals(e.getKey())) {// if we didn't land on an entry with the same key as e
				runner.setNext(new Node(e));
				size++;
			} else {// if we fell on a matching key
				runner.setData(e);
			}
		}
		return;
	}

	/**
	 * 
	 * @returns the current load factor of the map
	 */
	public double loadFactor() {
		return (double) (size) / pokedex.length;
	}

	/**
	 * Loads the file of data the computer will parse
	 * 
	 * @throws FileNotFoundException if the file is not located in the place where
	 *                               the user specified it to be
	 */
	public void loadFromFile() throws FileNotFoundException {
		PokePanel.getDisplay().setText("Loading... \n");
		JFileChooser file = new JFileChooser(new File(System.getProperty("user.dir")));
		int result = file.showOpenDialog(null);
		if (result != JFileChooser.APPROVE_OPTION) {
			PokePanel.getDisplay().append("File selection cancelled.\n");
			return;
		}

		// go line by line through csv and get data
		Scanner scanner = new Scanner(file.getSelectedFile());
		scanner.nextLine();// skip first line (header)

		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			ArrayList<String> dataList = new ArrayList<String>();
			StringBuilder currentData = new StringBuilder();
			boolean inQuotes = false;

			for (char c : line.toCharArray()) {
				if (c == '"') {// if it is a quote
					inQuotes = !inQuotes;
				} else if (c == ',' && !inQuotes) {// if it is a comma separated element
					dataList.add(currentData.toString().trim());// add that element to the list
					currentData.setLength(0);// resets the data we're looking at
				} else {
					currentData.append(c);// add that character to current data
				}
			}
			dataList.add(currentData.toString().trim());// adds the final one (since there is no comma at the end)
			try {

				PokeKey p_k = new PokeKey(Integer.parseInt(dataList.get(0)), Integer.parseInt(dataList.get(4)));
				Pokemon p = new Pokemon(dataList.get(1), Integer.parseInt(dataList.get(2)), dataList.get(3));
				Entry e = new Entry(p_k, p);
				put(e);
				PokePanel.getDisplay().append(e.getKey() + "\n");
			} catch (NumberFormatException ex) {
				PokePanel.getDisplay().append("Data Formatting Error: " + dataList);
			}
		}
		PokePanel.getDisplay().append("Loading Complete!\n\n\n");
		scanner.close();

		/*
		 * testing rehashing with put and remove!
		 * System.out.println(entries.getPokedex().length);//test to see the length
		 * for(int i = 0; i<entries.getPokedex().length*2; i++) {//goes through map
		 * twice and removes one of each slot (until we get to 159!) idrk how the math
		 * works but i got there!
		 * if(entries.getPokedex()[i%entries.getPokedex().length]== null ||
		 * entries.getPokedex()[i%entries.getPokedex().length].getData()==null)
		 * continue;
		 * entries.remove(entries.getPokedex()[i%entries.getPokedex().length].getData().
		 * getKey()); System.out.println(i); }
		 * System.out.println(entries.getSize());//test to see the length
		 * System.out.println(entries.getPokedex().length);
		 */

	}

	public void exportToFile(File file) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.println("id,name,hp,type,evo_set");

			for (int i = 0; i < pokedex.length; i++) {
				Node runner = pokedex[i];
				while (runner != null) {
					Entry e = runner.getData();
					writer.println(e.getKey().getID() + "," + e.getValue().getName() + "," + e.getValue().getHp() + ","
							+ e.getValue().getType() + "," + e.getKey().getEVO_SET() + "\n");
					runner = runner.getNext();
				}
			}

		}
	}

	/**
	 * Prints all the entries in the map and the load factor of the entire map
	 */
	public String toString() {
		String output = "";
		Node runner = null;
		for (int i = 0; i < pokedex.length; i++) {
			runner = pokedex[i];
			if (runner == null)// if you hit an empty slot, skip it
				continue;
			while (runner != null) {// get string of every single pokemon
				output = output + runner.getData().getKey().toString() + "\n";//to only show the keya
				runner = runner.getNext();// to look at all the collided nodes
			}

		}

		output = output + "Load Factor: " + loadFactor() + "\n";// add the load factor to the end

		return output;
	}
}