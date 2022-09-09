package com.badjed.petrescue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.skanderj.lucidityengine.Application;

public class Game {
	private final Grid grid;
	private final Player player;
	public static final int MAXLEVEL = 6;
	public int level;
	private int score = 0;
	private int diff = 0;

	public Game(final Application app, final Player player, final int level) throws FileNotFoundException, IOException {
		this.grid = new Grid(app, level);
		this.level = level;
		this.player = player;
	}
	
	public void setName(String s) {
		player.setName(s);
	}
	public void turn(final int x, final int y, final boolean usingBomb) {
		if (!usingBomb) {
			this.grid.popColorsInit(x, y);
			int pop = this.grid.popped;
			this.score += 5 *(pop-1) *(pop-1);
		
			pop = this.grid.getSaved();
			this.grid.setSaved(0);
			this.score += 1000 * pop;
		} else if (this.player.useBomb()) {
			this.grid.useBomb(x, y);
			this.score += 500;
		}
		if (this.grid.hasWon()) {
			if(this.player.getCurrentLevel()==level) this.player.nextLevel();
			this.player.bestScores[this.level] = Math.max(this.score, this.player.bestScores[this.level]);
			try {
				player.save();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void stage() {
		do {
			// turn(x,y,usingBomb);
		} while (!this.grid.hasWon() && this.grid.canPlay());
		if (this.grid.hasWon()) {
			this.player.nextLevel();
		}
		this.player.bestScores[this.level] = Math.max(this.score, this.player.bestScores[this.level]);
	}

	public void stage(final Scanner s) {
		this.grid.show();
		boolean usingBomb;
		int x, y;
		do {
			System.out.println("Vous avez " + this.player.getBombs() + " bombes");
			if (this.player.getBombs() > 0) {
				System.out.println("Voulez-vous utiliser une bombe?(Y/N)");
				usingBomb = (s.next().charAt(0) == 'Y');
			} else {
				usingBomb = false;
			}
			System.out.println("Choisir le X-Axis");
			x = s.nextInt();
			System.out.println("Choisir le Y-Axis");
			y = s.nextInt();
			this.turn(y, x, usingBomb);
			this.grid.show();
		} while (!this.grid.hasWon() && this.grid.canPlay());
		if (this.grid.hasWon()) {
			this.player.nextLevel();
			System.out.println("Bravo vous avez gagné et votre score est: " + this.score);
		} else {
			System.out.println("Desolé mais c'est perdu");
		}
		this.player.bestScores[this.level] = Math.max(this.score, this.player.bestScores[this.level]);
	}

	public void update() {
		if ((this.level == 0) && ((this.score / 1000) > this.diff)) {
			this.diff = this.score / 1000;
			this.player.addBombs(3);
		}
	}

	public Grid getGrid() {
		return grid;
	}
}
