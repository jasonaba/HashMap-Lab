package HashMapLab;

public class PokeKey {//pokemon key
	private final int ID;
	private final int EVO_SET;/*pichu, pikachu, raichu share evo_set = 10*/
	
	public PokeKey(int id, int evo_set) {
		this.ID = id;
		this.EVO_SET = evo_set;
	}
		

	public int getID() {
		return ID;
	}

	public int getEVO_SET() {
		return EVO_SET;
	}

	@Override
	public int hashCode() {
		return (ID * 31) + (7*EVO_SET);
		}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PokeKey other = (PokeKey) obj;
		return this.ID == other.ID && this.EVO_SET==other.EVO_SET;
	}

	@Override
	public String toString() {
		return "ID: " +  ID + " |  Evo Set: " + EVO_SET;
	}


	
	
}