package HashMapLab;


public class Entry {//pokemon key, pokemon
	private PokeKey key;
	private Pokemon value;

	//Constructor
	public Entry(PokeKey k, Pokemon v) {
		key = k;
		value = v;
	}

	//Getters
	public PokeKey getKey() {
		return key;
	}

	public Pokemon getValue() {
		return value;
	}
	
	//toString
	@Override
	public String toString() {
		return key.toString() + " | " + value.toString();
	}

	
	
	
}