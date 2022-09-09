package com.badjed.petrescue;

import com.skanderj.lucidityengine.Application;

/**
 * 
 * Représente une case vide du jeu.
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public class EmptyBox extends Box {
	public EmptyBox(final String name, final Application app) {
		super(name, app);
	}

	@Override
	public boolean isAnimal() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean isWoodenBox() {
		return false;
	}

	@Override
	public boolean isColorBox() {
		return false;
	}

	@Override
	public String toString() {
		return "E";
	}

	@Override
	public void draw(final int x, final int y, final int size) {
		return;
	}
}
