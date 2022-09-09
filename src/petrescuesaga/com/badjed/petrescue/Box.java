package com.badjed.petrescue;

import com.skanderj.lucidityengine.Application;

/**
 * 
 * Représente une case du jeu.
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public abstract class Box {
	// On garde une référence au jeu
	protected Application application;
	
	protected String name;
	protected int strength = 2;

	public Box(final String name, final Application app) {
		this.name = name;
		this.application = app;
	}

	// Est ce un animal?
	public abstract boolean isAnimal();

	// Est ce une case vide?
	public abstract boolean isEmpty();

	// Est ce une case en bois?
	public abstract boolean isWoodenBox();

	// Est ce une case de couleur simple?
	public abstract boolean isColorBox();

	// Pour l'affiachage
	public abstract void draw(int x, int y, int size);

	@Override
	public String toString() {
		return this.name;
	}
}
