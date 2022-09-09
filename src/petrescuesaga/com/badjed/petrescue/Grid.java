package com.badjed.petrescue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.skanderj.lucidityengine.Application;

public class Grid implements Mechanics {
	public final int N = 6;
	public final int M = 6;
	private int animals = 0;
	private int saved = 0;
	private int maxMoves = -1;
	public Application app;
	public int popped = 0;
	private final Box[][] boxes = new Box[this.N][this.M];

	public Grid(final Application app, final int level) throws FileNotFoundException, IOException {
		this.app = app;
		this.init(level);
	}
	/**
	 * Initialisation de la grid
	 * @param level le level qu'on va jouer
	 * @throws IOException 
	 * @throws FileNotFoundException si le fichier du level n'est pas trouver
	 */
	public void init(final int level) throws IOException, FileNotFoundException {
		this.animals = 0;
		if (level == 0) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					int rand = this.getRandomNumber(0, 4);
					this.boxes[i][j] = new ColorBox(ColorBox.EnumColors.VALUES.get(rand), this.app);
				}
			}
		} else {
			final BufferedReader sc = new BufferedReader(new FileReader("./res/levels/level" + level + ".txt"));
			String l;
			final List<String[]> blocs = new ArrayList<>();
			while ((l = sc.readLine()) != null) {
				blocs.add(l.split(" "));
			}
			final String[] p = blocs.get(0);
			this.maxMoves = Integer.parseInt(p[0]);
			blocs.remove(0);
			int i = 0, j = 0;
			for (final String[] s : blocs) {
				for (final String bloc : s) {
					switch (bloc) {
					case "Red":
						this.boxes[i][j] = new ColorBox(ColorBox.EnumColors.RED, this.app);
						break;
					case "Green":
						this.boxes[i][j] = new ColorBox(ColorBox.EnumColors.GREEN, this.app);
						break;
					case "Blue":
						this.boxes[i][j] = new ColorBox(ColorBox.EnumColors.BLUE, this.app);
						break;
					case "Yellow":
						this.boxes[i][j] = new ColorBox(ColorBox.EnumColors.YELLOW, this.app);
						break;
					case "Wood":
						this.boxes[i][j] = new WoodBox(this.app);
						break;
					case "Pig":
						this.boxes[i][j] = new AnimalBox(AnimalBox.EnumAnimalTypes.PIG, this.app);
						this.animals++;
						break;
					case "Panda":
						this.boxes[i][j] = new AnimalBox(AnimalBox.EnumAnimalTypes.PANDA, this.app);
						this.animals++;
						break;
					case "Cat":
						this.boxes[i][j] = new AnimalBox(AnimalBox.EnumAnimalTypes.CAT, this.app);
						this.animals++;
						break;
					case "Empty":
						this.boxes[i][j] = new EmptyBox("Empty", this.app);
						break;
					default:
						break;
					}
					j++;
				}
				i++;
				j = 0;
			}
			sc.close();
		}
	}
	/**
	 * Getter des blocs
	 * @return le tableau des blocs
	 */
	public Box[][] getBoxes() {
		return this.boxes;
	}
	/**
	 * Checker si la position est ok
	 */
	@Override
	public boolean isPositionOk(final int x, final int y) {
		return ((0 <= x) && (0 <= y) && (x < this.N) && (y < this.M));
	}
	/**
	 * L'utilisation du DFS pour avoir les voisins de la même couleur
	 */
	@Override
	public void popColors(final int x, final int y, boolean[][] visited) {
		if (visited[x][y]) {
			return;
		}
		if (boxes[x][y].isColorBox()) {
			this.popped++;
		}
		visited[x][y] = true;
		for (int i = 0; i < 4; i++) {
			final int x1 = x + Mechanics.col[i], y1 = y + Mechanics.row[i];
			final String nameBox = this.boxes[x][y].toString();
			if (this.isPositionOk(x1, y1) && ((this.boxes[x][y].isColorBox() && nameBox.equals(this.boxes[x1][y1].toString())) || ((this.boxes[x1][y1].isWoodenBox()) && !this.boxes[x][y].isWoodenBox()))) {
				this.popColors(x1, y1, visited);
			}
		}
		return;
	}
	/**
	 * Regarder si on peut exploser des blocs
	 * @param x la coordonnée x du bloc
	 * @param y la coordonnée y du bloc
	 */
	public void popColorsInit(final int x, final int y) {
		if (!this.isPositionOk(x, y)) {
			return;
		}
		boolean[][] visited = new boolean[this.N][this.M];
		this.popped = 0;
		if (!this.boxes[x][y].isColorBox()) {
			return;
		}
		this.popColors(x, y, visited);
		if (this.popped > 1) {
			this.newGrid(visited);
		}
		return;
	}
	/**
	 * Regarder s'il y a des mouvements qu'on peut faire
	 * @return true si on peut jouer sinon false
	 */
	@Override
	public boolean canPlay() {
		for (int x = 1; x < this.N; x++) {
			for (int y = 0; y < this.M; y++) {
				final boolean[][] visited = new boolean[this.N][this.M];
				this.popped = 0;
				this.popColors(x, y, visited);
				if (this.popped > 1) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Creation de la nouvelle grid après l'explosion des blocs
	 */
	@Override
	public void newGrid(final boolean[][] visited) {
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.M; j++) {
				if (visited[i][j] && !this.boxes[i][j].isWoodenBox()) {
					this.boxes[i][j] = new EmptyBox("Empty", this.app);
				} else if (visited[i][j] && this.boxes[i][j].isWoodenBox()) {
					this.boxes[i][j].strength -= 1;
					final int rand = this.getRandomNumber(0, 4);
					if (this.boxes[i][j].strength == 0) {
						this.boxes[i][j] = new ColorBox(ColorBox.EnumColors.VALUES.get(rand), this.app);
					}
				}
			}
		}
		this.applyGravity();
		this.checkAnimals();
		this.applyHorizontalGravity();
		this.applyGravity();
		this.checkAnimals();
	}
	/**
	 * Gravité verticale pour les blocs afin qu'ils tombent
	 */
	@Override
	public void applyGravity() {
		for (int t = 0; t < this.N; t++) {
			for (int i = 0; i < this.N - 1; i++) {
				for (int j = 0; j < this.M; j++) {
					if (!this.boxes[i][j].isEmpty() && this.boxes[i + 1][j].isEmpty()) {
						final Box tmp = this.boxes[i][j];
						this.boxes[i][j] = this.boxes[i + 1][j];
						this.boxes[i + 1][j] = tmp;
					}
				}
			}
		}
	}
	/**
	 * Avoir un nombre random entre min et max-1
	 * @param min 
	 * @param max
	 * @return un nombre random entre min et max-1
	 */
	public int getRandomNumber(final int min, final int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	/**
	 * Check si le joueur a gagné
	 */
	@Override
	public boolean hasWon() {
		if (this.maxMoves == 0) {
			return this.animals == 0;
		} else if (this.animals == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Check le nombre d'animaux sauver
	 */
	@Override
	public void checkAnimals() {
		for (int i = 0; i < this.M; i++) {
			if (this.boxes[this.N - 1][i].isAnimal()) {
				this.boxes[this.N - 1][i] = new EmptyBox("Empty", this.app);
				this.animals--;
				saved++;
			}
		}
	}
	/**
	 * L'utilisation d'une bombe en coordonnées (y,x)
	 */
	@Override
	public void useBomb(final int x, final int y) {
		for (int i = 0; i < 9; i++) {
			final int x1 = x + Mechanics.col[i], y1 = y + Mechanics.row[i];
			if (this.isPositionOk(x1, y1)) {
				if (this.boxes[x1][y1].isColorBox()) {
					this.boxes[x1][y1] = new EmptyBox("Empty", this.app);
				} else if (this.boxes[x1][y1].isWoodenBox()) {
					this.boxes[x1][y1].strength -= 1;
					final int rand = this.getRandomNumber(0, 3);
					if (this.boxes[x1][y1].strength == 0) {
						this.boxes[x1][y1] = new ColorBox(ColorBox.EnumColors.VALUES.get(rand), this.app);
					}
				}
			}
		}
		this.applyGravity();
		this.checkAnimals();
		this.applyHorizontalGravity();
		this.applyGravity();
		this.checkAnimals();
	}
	/**
	 * Check s'il y a un box egale à b
	 * @param b un box
	 * @return les coordonnées du box b dans la grid
	 */
	public int[] get(final Box b) {
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.M; j++) {
				if (this.boxes[i][j].equals(b)) {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}
	/**
	 * Affichage de la grid
	 */
	public void show() {
		System.out.println("  0 1 2 3 4 5");
		for (int i = 0; i < this.N; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < this.M; j++) {
				System.out.print(this.boxes[i][j] + " ");
			}
			System.out.println();
		}
	}
	/**
	 * Appliquer la gravité horizontal
	 */
	@Override
	public void applyHorizontalGravity() {
		for (int doing = 0; doing < this.M; doing++) {
			for (int k = 0; k < (this.M - 1); k++) {
				if (this.boxes[this.N - 1][k].isEmpty() && !this.boxes[this.N - 1][k + 1].isEmpty()) {
					for (int i = this.N - 1; i >= 1; i--) {
						final Box tmp = this.boxes[i][k];
						this.boxes[i][k] = this.boxes[i][k + 1];
						this.boxes[i][k + 1] = tmp;
					}
				}
			}
		}
	}
	/**
	 * 
	 * @return le nombre d'animaux sauvé
	 */
	public int getSaved() {
		int tmp = saved;
		saved = 0;
		return tmp;
	}
	/**
	 * 
	 * @return nombre maximales de mouvements
	 */
	public int getMaxMoves() {
		return maxMoves;
	}
	/**
	 * Setter de saved
	 * @param saved saved
	 */
	public void setSaved(int saved) {
		this.saved = saved;
	}
	/**
	 * 
	 * @return nombre d'animaux
	 */
	public int getAnimals() {
		return animals;
	}
	/**
	 * Check si le box de coordonnées (y,x) est inutile
	 * @param x
	 * @param y
	 * @return true si on peut le jouer sinon false
	 */
	public boolean notPlayable(int x, int y) {
		if (!this.isPositionOk(x, y)) {
			return false;
		}
		boolean[][] visited = new boolean[this.N][this.M];
		this.popped = 0;
		if (!this.boxes[x][y].isColorBox()) {
			return false;
		}
		this.popColors(x, y, visited);
		if (this.popped > 1) {
			return true;
		}
		return false;
	}
}
