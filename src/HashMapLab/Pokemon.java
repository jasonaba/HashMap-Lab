package HashMapLab;

public class Pokemon {
	private String name;
	private int hp;
	private String type; // ex: {grass, poison}

	public Pokemon(String name, int hp, String type) {
		this.name = name;
		this.hp = hp;
		this.type = type;
	}

	// Getters & Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// toString
	public String toString() {
		return name + " | HP: " + hp + " | Type: " + type;
	}
}