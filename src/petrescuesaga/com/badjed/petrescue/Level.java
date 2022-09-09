package com.badjed.petrescue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.logging.Logger;
import com.skanderj.lucidityengine.logging.Logger.LogLevel;

/**
 * 
 * Représente un niveau du jeu.
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public class Level {
	private final Grid grid;
	private final Player player;
	public static final int MAX_LEVEL = 6;
	private int level, score, difference,moves;

	public Level(final Application app, final Player player, int level) throws FileNotFoundException, IOException {
		this.grid = new Grid(app, level);
		this.level = level;
		this.player = player;
		this.score = 0;
		this.difference = 0;
		this.moves = grid.getMaxMoves();
	}
	/**
	 * un Setter du nom du joueur
	 * @param name nom du joueur
	 */
	public void setName(final String name) {
		this.player.setName(name);
	}
	/**
	 * Représente un tour de jeu
	 * @param x la coordonnée x du bloc
	 * @param y la coordonnée y du bloc
	 * @param usingBomb l'utilisation de la bombe
	 */
	public void turn(final int x, final int y, final boolean usingBomb) {
		if (!usingBomb) {
			this.grid.popColorsInit(x, y);
			int pop = this.grid.popped;
			this.score += 5 * (Math.max(0, pop - 1)) * (Math.max(0, pop - 1));
			pop = this.grid.getSaved();
			this.score += 1000 * pop;
		} else if (this.player.useBomb()) {
			this.grid.useBomb(x, y);
			this.score += 500;
		}
		if (level != 0 && this.grid.hasWon()) {
			if (this.player.getCurrentLevel() == this.level) {
				this.player.nextLevel();
			}
			this.player.bestScores[this.level] = Math.max(this.score, this.player.bestScores[this.level]);
			try {
				this.player.save();
			} catch (final FileNotFoundException fileNotFoundException) {
				Logger.log(Level.class, LogLevel.SEVERE, "An exception occurred while saving data: %s", fileNotFoundException.getMessage());
			}
		} else if (level == 0 && !this.grid.canPlay()) {
			try {
				this.player.bestScores[this.level] = Math.max(this.score, this.player.bestScores[this.level]);
				this.grid.init(0);
				this.player.save();
			} catch (IOException exception) {
				Logger.log(Level.class, LogLevel.SEVERE, "An exception occurred while saving data: %s", exception.getMessage());
			}
		}
	}
	/**
	 * Répresente le jeu en terminal 
	 * @param scanner L'input de l'utilisateur
	 * @param infinity Infinity mode ou mode normal
	 */
	public void stage(final Scanner scanner,boolean infinity) {
		this.grid.show();
		boolean usingBomb;
		boolean quitter = false;
		int x, y;
		if(!infinity) {
			do {
				if(this.grid.getMaxMoves()>=0) {
					System.out.println("Il vous reste "+this.moves+" Mouvements");
				}
				System.out.println("Vous avez " + this.player.getBombs() + " bombes");
				if (this.player.getBombs() > 0) {
					System.out.println("Voulez-vous utiliser une bombe?(Y/N)");
					usingBomb = (scanner.next().charAt(0) == 'Y');
				} else {
					usingBomb = false;
				}
				System.out.println("Choisir le X-Axis");
				x = scanner.nextInt();
				System.out.println("Choisir le Y-Axis");
				y = scanner.nextInt();
				this.turn(y, x, usingBomb);
				this.moves--;
				this.grid.getMaxMoves();
				this.grid.show();
			} while ((!this.grid.hasWon() && this.grid.canPlay() && this.moves!=0) || (this.player.getBombs()>0 && this.moves!=0  && !this.grid.hasWon()));
			if (this.grid.hasWon()) {
				System.out.println("Bravo vous avez gagné et votre score est: " + this.score);
			} else {
				System.out.println("Desolé, c'est perdu");
			}
		}
		else {
			do {
				System.out.println("Vous avez " + this.player.getBombs() + " bombes");
				if (this.player.getBombs() > 0) {
					System.out.println("Voulez-vous utiliser une bombe?(Y/N)");
					usingBomb = (scanner.next().charAt(0) == 'Y');
				} else {
					usingBomb = false;
				}
				System.out.println("Choisir le X-Axis");
				x = scanner.nextInt();
				System.out.println("Choisir le Y-Axis");
				y = scanner.nextInt();
				this.turn(y, x, usingBomb);
				this.grid.show();
				System.out.println("Voulez vous quitter le niveau?(Y/N)");
				quitter = (scanner.next().charAt(0) == 'Y');
			} while (!quitter);
		}
		this.player.bestScores[this.level] = Math.max(this.score, this.player.bestScores[this.level]);
	}
	/**
	 * Ajouter des bombes au joueur dans l'inifinity mode
	 */
	public void update() {
		if ((this.level == 0) && ((this.score / 1000) > this.difference)) {
			this.difference = this.score / 1000;
			this.player.addBombs(3);
		}
	}
	/**
	 * Grid getter 
	 * @return la grid du level
	 */
	public Grid getGrid() {
		return this.grid;
	}
	/**
	 * 
	 * @return le score du joueur
	 */
	public int getScore() {
		return score;
	}
	/**
	 * 
	 * @return le joueur
	 */
	public Player getPlayer() {
		return this.player;
	}

	public void setScore(int i) {
		this.score = i;
		
	}
}
