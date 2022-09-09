package com.badjed.petrescue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
/**
 * Cette classe represente notre Robot
 * @author Ismail Badaoui
 *
 */
public class PetRescueRobot {
	public static void main(final String[] args) throws FileNotFoundException, IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bonjour, je m'appelle JARVIS le stupide");
		final Player player = new Player("JARVIS le stupide");
		final Level level = new Level(null,player,0);
		System.out.println("Regarder moi entrain de jouer le mode Infinity et arriver à 10000 en score");
		level.getGrid().show();
		System.out.println();
		while (true) {
			int i = getRandomNumber(0,level.getGrid().N);
			int j = getRandomNumber(0,level.getGrid().M);
			boolean usingBomb = (getRandomNumber(0,2)==1)?true:false;
			do {
				i = getRandomNumber(0,level.getGrid().N);
				j = getRandomNumber(0,level.getGrid().M);
				usingBomb = (getRandomNumber(0,2)==1)?true:false;
			}while(!level.getGrid().notPlayable(i, j) || (usingBomb && player.getBombs()==0));
			if(usingBomb) System.out.println("Je vais utiliser la bombe pour tuer Thanos en X ="+j+" et Y ="+i);
			else System.out.println("Je vais jouer en X ="+j+" et Y ="+i);
			level.turn(i, j, usingBomb);
			level.getGrid().show();
			System.out.println("Vous avez marre de me regarder entrain de battre Thanos?(Y/N)");
			boolean quitter = (scanner.next().charAt(0) == 'Y');
			if (quitter || level.getScore()>=10000) {
				break;
			}
			else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Je vous aime!!");
		scanner.close();
	}
	public static int getRandomNumber(final int min, final int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
