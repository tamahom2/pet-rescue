package com.badjed.petrescue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import com.skanderj.lucidityengine.logging.Logger;
import com.skanderj.lucidityengine.logging.Logger.LogLevel;


/**
 * Représente un joueur
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public class Player {
	private String name;
	private int currentLevel = 1;
	private int bombs = 2;
	public int[] bestScores = new int[Level.MAX_LEVEL + 1];

	public Player(final String name) {
		this.name = name;
		try {
			this.load(name);
		} catch (final FileNotFoundException fileNotFoundException) {
			Logger.log(Level.class, LogLevel.SEVERE, "An exception occurred while loading data: %s", fileNotFoundException.getMessage());
		}
	}

	public void setName(final String name) {
		this.name = name;
		try {
			this.load(name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Sauvegarde des données du joueur 
	 * @throws FileNotFoundException
	 */
	public void save() throws FileNotFoundException {
		final File file = new File("./res/data/" + this.name + ".txt");
		try {
			if (!file.createNewFile()) {
				file.delete();
				file.createNewFile();
			}
		} catch (final IOException exception) {
			Logger.log(Level.class, LogLevel.SEVERE, "An exception occurred while saving data: %s", exception.getMessage());
		}
		final PrintStream printStream = new PrintStream(file);
		printStream.println(this.currentLevel);
		printStream.println(this.bombs);
		for (final int bestScore : this.bestScores) {
			printStream.println(bestScore);
		}
		printStream.flush();
		printStream.close();
	}
	/**
	 * Load les données du joueur nommé name
	 * @param name nom du joueur
	 * @throws FileNotFoundException
	 */
	public void load(final String name) throws FileNotFoundException {
		final File file = new File("./res/data/" + name + ".txt");
		if (!file.exists()) {
			this.save();
			return;
		}
		final BufferedReader bufferedReader = new BufferedReader(new FileReader("./res/data/" + name + ".txt"));
		try {
			this.currentLevel = Integer.parseInt(bufferedReader.readLine());
			this.bombs = Integer.parseInt(bufferedReader.readLine());
			for (int i = 0; i < this.bestScores.length; i++) {
				this.bestScores[i] = Integer.parseInt(bufferedReader.readLine());
			}
			bufferedReader.close();
		} catch (final NumberFormatException | IOException exception) {
			Logger.log(Level.class, LogLevel.SEVERE, "An exception occurred while loading data: %s", exception.getMessage());
		}
	}

	public int getBombs() {
		return this.bombs;
	}

	public void addBombs(int add) {
		this.bombs += add;
	}
	/**
	 * Utilisation des bombes
	 * @return true si on peut utilise une bombe sinon false
	 */
	public boolean useBomb() {
		if (this.bombs > 0) {
			this.bombs--;
			return true;
		}
		return false;
	}

	public String getName() {
		return this.name;
	}

	public int getCurrentLevel() {
		return this.currentLevel;
	}
	/**
	 * Si On gagne à un niveau on peut jouer le suivant
	 */
	public void nextLevel() {
		if (this.currentLevel < Level.MAX_LEVEL) {
			this.currentLevel++;
			this.bombs += 2;
		}
	}
}
