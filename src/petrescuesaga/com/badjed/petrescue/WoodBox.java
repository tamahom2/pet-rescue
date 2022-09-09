package com.badjed.petrescue;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.resources.Images;

/**
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public class WoodBox extends Box {
	public WoodBox(final Application app) {
		super("Wood", app);
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
		return true;
	}

	@Override
	public boolean isColorBox() {
		return false;
	}

	@Override
	public String toString() {
		return "W";
	}

	@Override
	public void draw(final int x, final int y, final int size) {
		this.application.screen().drawImage(this.strength == 2 ? Images.get("wood") : Images.get("wood-weak"), x, y, size, size);
	}
}
