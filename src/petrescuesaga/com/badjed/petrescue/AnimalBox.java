package com.badjed.petrescue;

import java.awt.image.BufferedImage;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.resources.Images;

/**
 * 
 * Représente une case animal du jeu.
 * 
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public class AnimalBox extends Box {
	private static boolean creepy = false;
	public enum EnumAnimalTypes {
		PANDA, CAT, PIG;
	}
	
	public AnimalBox(final EnumAnimalTypes name, final Application app) {
		super(name.toString(), app);
	}
	public static void setCreepy() {
		creepy = true;
	}
	@Override
	public boolean isAnimal() {
		return true;
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
		return false;
	}

	@Override
	public String toString() {
		return String.valueOf(this.name.charAt(0));
	}

	@Override
	public void draw(final int x, final int y, final int size) {
		final BufferedImage image;
		if(!creepy) {
		image = this.name.equals(EnumAnimalTypes.PANDA.toString()) ? Images.get("panda")
				: this.name.equals(EnumAnimalTypes.CAT.toString()) ? Images.get("cat") : Images.get("pig");
		}
		else {
			image = this.name.equals(EnumAnimalTypes.CAT.toString())?Images.get("Easter Creepy Cat")
					:this.name.equals(EnumAnimalTypes.PANDA.toString()) ? Images.get("Easter Creepy Dog") : Images.get("Easter Creepy Pig");
		}
		this.application.screen().drawImage(image, x, y, size, size);
	}
}
