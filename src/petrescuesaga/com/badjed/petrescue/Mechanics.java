package com.badjed.petrescue;

/**
 * 
 * Représente les mécaniques du jeu.
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public interface Mechanics {
	int[] row = { 1, -1, 0, 0, 1, 1, -1, -1, 0 };
	int[] col = { 0, 0, 1, -1, 1, -1, 1, -1, 0 };

	boolean isPositionOk(int x, int y);

	void popColors(int x, int y, boolean[][] visited);

	void newGrid(boolean[][] visited);

	void applyGravity();

	void applyHorizontalGravity();

	boolean hasWon();

	boolean canPlay();

	void checkAnimals();

	void useBomb(int x, int y);
}
