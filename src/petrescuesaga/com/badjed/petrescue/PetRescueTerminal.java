package com.badjed.petrescue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * Cette classe represente le mode de jeu en terminal
 * @author Ismail Badaoui
 *
 */
public class PetRescueTerminal {

	public static void main(final String[] args) throws FileNotFoundException, IOException {
		final Scanner scanner = new Scanner(System.in);
		System.out.println("Bonjour, quel est votre nom?");
		final String name = scanner.next();
		final Player player = new Player(name);
		while (true) {
			System.out.println("Quel niveau voulez vous jouer?");
			for (int i = 0; i <= player.getCurrentLevel(); i++) {
				if(i==0) System.out.print("0(Infinity)");
				else System.out.print(" " + (i));
			}
			final int level = scanner.nextInt();
			System.out.println();
			final Level jeu = new Level(null, player, level);
			jeu.stage(scanner,level==0);
			System.out.println("Voulez vous quitter? (Y/N)");
			final boolean quitter = (scanner.next().charAt(0) == 'Y');
			if (quitter) {
				break;
			}
		}
		scanner.close();
	}
}
