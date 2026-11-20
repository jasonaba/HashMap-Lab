package HashMapLab;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import javax.swing.*;

//PROBABLY REMOVE POKEPANEL AND MAKE CUSTOMHASHMAP THE PANEL! but check first before doing that

public class PokePanel extends JPanel {

	private static JTextArea display = new JTextArea(40, 100);
	private CustomHashMap entries = new CustomHashMap(100);

	private JPanel buttons;

	public PokePanel() {
		this.setSize(1000,1000);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		display.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		this.add(new JScrollPane(display), BorderLayout.CENTER);
		display.setEditable(false);

		buttons = new JPanel(new GridLayout(0, 1, 5, 5));// (rows, columns, padding, padding)
		buttons.add(makeButton("Load File", e -> {

			try {

				entries.loadFromFile();

			} catch (FileNotFoundException e1) {

				// TODO Auto-generated catch block

				e1.printStackTrace();

			}

		}));
		buttons.add(makeButton("Get Pokemon", e -> getPokemon()));
		buttons.add(makeButton("Put Pokemon", e -> putPokemon()));
		buttons.add(makeButton("Remove Pokemon", e -> removePokemon()));
		buttons.add(makeButton("Save Pokedex", e -> exportPokemon()));
		buttons.add(makeButton("Print Pokedex", e-> display.append(entries.toString())));

		this.add(buttons, BorderLayout.EAST);
	}

	public static JTextArea getDisplay() {
		return display;
	}

	public CustomHashMap getEntries() {
		return entries;
	}

	public JButton makeButton(String name, ActionListener action) {
		JButton button = new JButton(name);
		button.addActionListener(action);// adds whatever action that button does to the button
		return button;
	}

	public void getPokemon() {
		// Text Panel
		JTextField p_id = new JTextField(15);
		JTextField p_evo_set = new JTextField(15);
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.add(new JLabel("Pokemon ID: "));
		panel.add(p_id);
		panel.add(new JLabel("Evolution Set Number: "));
		panel.add(p_evo_set);

		int result = JOptionPane.showConfirmDialog(null, panel, "Enter Pokemon Information to Get Pokemon:",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {// create a PokemonKey from their input
			PokeKey p_k = null;

			// try to parse input
			try {
				p_k = new PokeKey(Integer.parseInt(p_id.getText()), Integer.parseInt(p_evo_set.getText()));
			} catch (NumberFormatException ex) {
				display.append(
						"Invalid information. Please enter integers for both the Pokemon's ID and Evolution Set. \n");
				return;
			}
			// retrieve pokemon
			entries.get(p_k);
			
		}
	}

	public void putPokemon() {
		// Text Panel
		String name = JOptionPane.showInputDialog(this, "Pokemon Name");
		String hp = JOptionPane.showInputDialog(this, "Pokemon HP");
		String type = JOptionPane.showInputDialog(this, "Pokemon Type (format: \"{type, type, type, etc}\"");
		String id = JOptionPane.showInputDialog(this, "Pokemon ID");
		String evo_set = JOptionPane.showInputDialog(this, "Pokemon Evolution Set");
		String index = JOptionPane.showInputDialog(this, "Desired index (if none, leave empty)");

		try {// try to put a pokemon in before
			PokeKey p_k = new PokeKey(Integer.parseInt(id), Integer.parseInt(evo_set));
			Pokemon p = new Pokemon(name, Integer.parseInt(hp), type);
			Entry p_e = new Entry(p_k, p);

			Pokemon removed = null;

			if (index.trim().isEmpty()) {
				removed = entries.put(p_e);
			} else {
				removed = entries.put(Integer.parseInt(index), p_e);
			}

			if (removed == null) {
				display.append("Your Pokemon was added and no Pokemon was removed.");
			} else
				display.append("The Pokemon you added had a collision with another: Removed Pokemon: " + removed
						+ " | Added Pokemon: " + p_e);

		} catch (NumberFormatException ex) {
			display.append("Invalid information. Please enter integers for the Pokemon's parameters. \n");
			return;
		}

	}

	public void removePokemon() {
		JTextField p_id = new JTextField(15);
		JTextField p_evo_set = new JTextField(15);
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.add(new JLabel("Pokemon ID: "));
		panel.add(p_id);
		panel.add(new JLabel("Evolution Set Number: "));
		panel.add(p_evo_set);

		int result = JOptionPane.showConfirmDialog(null, panel, "Enter Pokemon Information to Remove Pokemon:",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {// create a PokemonKey from their input
			PokeKey p_k = null;
			// try to parse input
			try {
				p_k = new PokeKey(Integer.parseInt(p_id.getText()), Integer.parseInt(p_evo_set.getText()));
				Pokemon removed = entries.remove(p_k);
				if (removed != null) {// if the pokemon was successfully returned by the "remove()" function in
										// CustomHashMap
					display.append(removed + " was sucessfully removed. \n");
				} else {
					display.append("404: Pokemon not found. \n");
				}
				// prevent them from entering invalid information
			} catch (NumberFormatException ex) {
				display.append(
						"Invalid information. Please enter integers for both the Pokemon's ID and Evolution Set. \n");
				return;
			}
		}
	}

	public void exportPokemon() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save Pokedex");
		chooser.setSelectedFile(new File("pokedex-export.csv"));

		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			display.append("Successfully exported Pokedex to \"" + chooser.getSelectedFile().getPath() +"\"!");
			try {
				entries.exportToFile(selectedFile);

			} catch (IOException ex) {
				display.append("Error exporting Pokedex: " + ex.getMessage() + "\n");
			}
		}
	}

}