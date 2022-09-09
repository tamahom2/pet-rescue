package com.badjed.petrescue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.resources.Images;

/**
 * 
 * Représente une case couleur du jeu.
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public class ColorBox extends Box {
	enum EnumColors {
		RED, YELLOW, BLUE, GREEN;

		public static final List<EnumColors> VALUES = Collections.unmodifiableList(Arrays.asList(EnumColors.values()));
	}

	public ColorBox(final EnumColors name, final Application app) {
		super(name.toString(), app);
	}

	@Override
	public boolean isAnimal() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isWoodenBox() {
		return false;
	}

	@Override
	public boolean isColorBox() {
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(this.name.toString().charAt(0));
	}

	@Override
	public void draw(final int x, final int y, final int size) {
		this.application.screen().drawImage(Images.get(this.name), x, y, size, size);
	}
}
