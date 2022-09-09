package com.badjed.petrescue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bienvenue dans Pet Rescue Saga (par Jeddi Skander et Badaoui Ismail");
		System.out.println("Veuillez choisir votre mode de jeu :");
		System.out.println("1 pour GUI");
		System.out.println("2 pour le terminal");
		System.out.println("3 pour le robot");
		boolean quitter = false;
		while (true) {
			String s = scanner.next();
			switch (s) {
			case "1":
				PetRescue.main(args);
				quitter = true;
				break;
			case "2":
				PetRescueTerminal.main(args);
				quitter = true;
				break;
			case "3":
				PetRescueRobot.main(args);
				quitter = true;
				break;
			default:
				System.out.println("Oups, veuillez rechoisir!");
				break;
			}
			if(quitter) {
				break;
			}
		}
		scanner.close();
	}
}
