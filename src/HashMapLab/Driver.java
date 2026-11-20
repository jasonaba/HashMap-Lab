package HashMapLab;

import javax.swing.JFrame;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame frame = new JFrame("National Pokedex");
		PokePanel panel = new PokePanel();
		frame.add(panel);
		frame.pack();//fits to panel size
		frame.setLocationRelativeTo(null);//centers
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
	}

}