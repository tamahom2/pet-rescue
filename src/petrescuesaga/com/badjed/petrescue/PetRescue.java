package com.badjed.petrescue;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.badjed.petrescue.lucidityengine.exts.BombCheckbox;
import com.badjed.petrescue.lucidityengine.exts.ColorBackground;
import com.badjed.petrescue.lucidityengine.exts.FlickeringLabel;
import com.badjed.petrescue.lucidityengine.exts.ForegroundImage;
import com.badjed.petrescue.lucidityengine.exts.ImageBackground;
import com.badjed.petrescue.lucidityengine.exts.OneLineTextfield;
import com.badjed.petrescue.lucidityengine.exts.SimpleButton;
import com.badjed.petrescue.lucidityengine.exts.StaticLabel;
import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.ThreadWrapper.ThreadWrapperType;
import com.skanderj.lucidityengine.core.Engine;
import com.skanderj.lucidityengine.core.Scene;
import com.skanderj.lucidityengine.exts.transitions.FadeInTransition;
import com.skanderj.lucidityengine.exts.transitions.FadeOutTransition;
import com.skanderj.lucidityengine.graphics.OnScreenText;
import com.skanderj.lucidityengine.graphics.OnScreenTextProperties;
import com.skanderj.lucidityengine.graphics.OnScreenTextProperties.OnScreenTextPosition;
import com.skanderj.lucidityengine.graphics.WindowConfiguration;
import com.skanderj.lucidityengine.graphics.components.ComponentLabelPosition;
import com.skanderj.lucidityengine.graphics.components.ComponentState;
import com.skanderj.lucidityengine.graphics.components.Label;
import com.skanderj.lucidityengine.graphics.components.Textfield;
import com.skanderj.lucidityengine.input.Keyboard;
import com.skanderj.lucidityengine.input.Mouse;
import com.skanderj.lucidityengine.input.binds.Binds;
import com.skanderj.lucidityengine.logging.Logger;
import com.skanderj.lucidityengine.logging.Logger.DebuggingType;
import com.skanderj.lucidityengine.logging.Logger.LogLevel;
import com.skanderj.lucidityengine.resources.Fonts;
import com.skanderj.lucidityengine.resources.Images;
import com.skanderj.lucidityengine.resources.audio.Audios;
import com.skanderj.lucidityengine.util.Utilities;
import com.skanderj.ts4j.Task;
import com.skanderj.ts4j.TaskScheduler;
import com.skanderj.ts4j.TaskType;

/**
 * Cette classe représente l'interface graphique faites en utilisant LucidityEngine
 * @author Ismail Badaoui
 * @author Skander Jeddi
 *
 */
public final class PetRescue extends Application {
	public static final String DEMO_IDENTIFIER = "pet-rescue-saga";
	public static final String WINDOW_TITLE = "Pet Rescue Saga";
	public static final int WINDOW_WIDTH = 1200, WINDOW_HEIGHT = (int) ((PetRescue.WINDOW_WIDTH / 16.0) * 9),
			BUFFERS = 2;
	public static final double TARGET_REFRESH_RATE = 60.0;
	public static final boolean FULLSCREEN = false;

	private boolean exitingSplash = false;
	private Player player;
	
	public PetRescue() {
		super(PetRescue.DEMO_IDENTIFIER, PetRescue.TARGET_REFRESH_RATE, PetRescue.TARGET_REFRESH_RATE, Keyboard.AZERTY);
	}

	@Override
	protected void loadResources() {
		Images.load("background", "res/images/background.jpg");
		Images.load("BLUE", "res/images/BlueBox.png");
		Images.load("bomb", "res/images/Bomb.png");
		Images.load("wood", "res/images/WoodenBox.png");
		Images.load("wood-weak", "res/images/BrokenWoodenBox.png");
		Images.load("cat", "res/images/CatAnimal.png");
		Images.load("Creepy Background", "res/images/CreepyBackground.png");
		Images.load("Easter Creepy Dog", "res/images/EasterCreepyDog.png");
		Images.load("Easter Creepy Cat", "res/images/EasterCreepyCat.png");
		Images.load("bomb_ignited", "res/images/Explosion.png");
		Images.load("GREEN", "res/images/GreenBox.png");
		Images.load("panda", "res/images/PandaAnimal.png");
		Images.load("pig", "res/images/PigAnimal.png");
		Images.load("RED", "res/images/RedBox.png");
		Images.load("YELLOW", "res/images/YellowBox.png");
		Images.load("prs-logo", "res/images/logo.png");
		Images.load("Easter Creepy Pig", "res/images/EasterCreepyPig.png");

		Fonts.load("pumpkin", "res/fonts/pumpkin.ttf");
		Fonts.load("south", "res/fonts/south.ttf");

		Audios.load("main-theme", "res/audios/main-theme.wav");
		Audios.load("creepy-main-theme", "res/audios/creepy-theme.wav");

		Audios.load("pop", "res/audios/pop.wav");
		Audios.load("bomb", "res/audios/bomb.wav");
	}

	@Override
	protected void registerApplicationObjects() {
		final int w = this.windowConfiguration().getWidth(), h = this.windowConfiguration().getHeight();
		// Splashscreen
		{
			Engine.setObject("splash-bg", new ColorBackground(this, 0, 0, w, h, Color.WHITE));
			Engine.setObject("built-with",
					new StaticLabel(this,
							new OnScreenText("Créé Ã  l'aide de", new OnScreenTextProperties(Fonts.get("pumpkin", 36),
									Color.WHITE, Color.DARK_GRAY, OnScreenTextPosition.CENTERED)),
							0, 0, w, h - 100));
			Engine.setObject("engine-name",
					new FlickeringLabel(this, 0, 0, w, h + 50, "Lucidity Engine", new OnScreenTextProperties(
							Fonts.get("pumpkin", 108), Color.WHITE, Color.DARK_GRAY, OnScreenTextPosition.CENTERED)));
			Engine.setObject("credits", new StaticLabel(this,
					new OnScreenText("Par JEDDI Skander ET BADAOUI Ismail", new OnScreenTextProperties(
							Fonts.get("pumpkin", 32), Color.WHITE, Color.DARK_GRAY, OnScreenTextPosition.CENTERED)),
					0, 0, w, (2 * h) - 100));
		}
		// Background
		Engine.setObject("background", new ImageBackground(this, 0, 0, w, h, Images.get("background")));
		// Main menu objects
		{
			// Title
			Engine.setObject("title", new ForegroundImage(this, (w / 2) + 10, 30, 400, 200, Images.get("prs-logo")));
			// Play button
			{
				Engine.setObject("play-button", new SimpleButton(this, (w / 2) - 360, (h / 2) + 120, 120, 60,
						new OnScreenText("Jouer",
								new OnScreenTextProperties(Fonts.get("blueberry", 34),
										Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
										OnScreenTextPosition.CENTERED)),
						Color.WHITE, new Color(0, 0, 0, 200), 15));
				((SimpleButton) Engine.getObject("play-button")).mapActionToState(ComponentState.IDLE, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
				});
				((SimpleButton) Engine.getObject("play-button")).mapActionToState(ComponentState.HOVERED, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
				});
				((SimpleButton) Engine.getObject("play-button")).mapActionToState(ComponentState.ACTIVE, b -> {
					Engine.switchToScene("name-menu");
				});
			}
			// Credits button
			{
				Engine.setObject("credits-button", new SimpleButton(this, (w / 2) - 220, (h / 2) + 120, 120, 60,
						new OnScreenText("Crédits",
								new OnScreenTextProperties(Fonts.get("blueberry", 34),
										Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
										OnScreenTextPosition.CENTERED)),
						Color.WHITE, new Color(0, 0, 0, 200), 15));
				((SimpleButton) Engine.getObject("credits-button")).mapActionToState(ComponentState.IDLE, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
				});
				((SimpleButton) Engine.getObject("credits-button")).mapActionToState(ComponentState.HOVERED, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
				});
				((SimpleButton) Engine.getObject("credits-button")).mapActionToState(ComponentState.ACTIVE, b -> {
					Engine.switchToScene("credits-scene");
				});
			}
			// Back to main menu button
			{
				Engine.setObject("go-back-menu-button", new SimpleButton(this, (w / 2) - 150, h - 120, 300, 60,
						new OnScreenText("Retour au menu",
								new OnScreenTextProperties(Fonts.get("blueberry", 34),
										Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
										OnScreenTextPosition.CENTERED)),
						Color.WHITE, new Color(0, 0, 0, 200), 15));
				((SimpleButton) Engine.getObject("go-back-menu-button")).mapActionToState(ComponentState.IDLE, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
				});
				((SimpleButton) Engine.getObject("go-back-menu-button")).mapActionToState(ComponentState.HOVERED, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
				});
				((SimpleButton) Engine.getObject("go-back-menu-button")).mapActionToState(ComponentState.ACTIVE, b -> {
					Engine.switchToScene("main-menu");
				});
			}
			// Remerciment
			{
				Engine.setObject("thank-you-all",
						new StaticLabel(this,
								new OnScreenText("Merci d'avoir essayé notre jeu jeu",
										new OnScreenTextProperties(Fonts.get("blueberry", 144),
												Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
												OnScreenTextPosition.CENTERED)),
								0, 0, w, h));
				Engine.setObject("enjoy",
						new StaticLabel(this,
								new OnScreenText("Nous espérons que c'était amusant",
										new OnScreenTextProperties(Fonts.get("blueberry", 144),
												Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
												OnScreenTextPosition.CENTERED)),
								0, 30, w, h));
				Engine.setObject("hope-you-find-easter-egg",
						new StaticLabel(this,
								new OnScreenText("N'oublier pas nous avons caché 2 easter eggs!",
										new OnScreenTextProperties(Fonts.get("blueberry", 144),
												Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
												OnScreenTextPosition.CENTERED)),
								0, 60, w, h));
			}
			// Exit button
			{
				Engine.setObject("exit-button", new SimpleButton(this, (w / 2) - 290, (h / 2) + 200, 120, 60,
						new OnScreenText("Quitter",
								new OnScreenTextProperties(Fonts.get("blueberry", 34),
										Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
										OnScreenTextPosition.CENTERED)),
						Color.WHITE, new Color(0, 0, 0, 200), 15));
				((SimpleButton) Engine.getObject("exit-button")).mapActionToState(ComponentState.IDLE, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
				});

				((SimpleButton) Engine.getObject("exit-button")).mapActionToState(ComponentState.HOVERED, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
				});
				((SimpleButton) Engine.getObject("exit-button")).mapActionToState(ComponentState.ACTIVE, b -> {
					this.stop();
				});
			}
		}
		// Name menu objects
		{
			// Username prompt
			Engine.setObject("username-prompt", new StaticLabel(this,
					new OnScreenText("Nom d'utilisateur?",
							new OnScreenTextProperties(Fonts.get("blueberry", 32),
									Utilities.buildAgainst(Color.YELLOW, 200), Utilities.buildAgainst(Color.RED, 200),
									OnScreenTextPosition.CENTERED)),
					0, 0, w, h / 3));
			// Username field
			Engine.setObject("username-field", new OneLineTextfield(this, (w / 2) - 75, (h / 2) - 25, 150,
					Utilities.buildAgainst(Color.WHITE, 150), new OnScreenTextProperties(Fonts.get("blueberry", 44),
							Color.BLACK, Color.LIGHT_GRAY, OnScreenTextPosition.CENTERED)));
			// Next button
			Engine.setObject("next-button",
					new SimpleButton(this, (w / 2) - 70, (h / 2) + 200, 140, 70,
							new OnScreenText("Continuer",
									new OnScreenTextProperties(Fonts.get("blueberry", 34),
											Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
											OnScreenTextPosition.CENTERED)),
							Color.WHITE, new Color(0, 0, 0, 200), 15));
			((SimpleButton) Engine.getObject("next-button")).mapActionToState(ComponentState.IDLE, b -> {
				((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
			});
			((SimpleButton) Engine.getObject("next-button")).mapActionToState(ComponentState.HOVERED, b -> {
				((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
			});
			((SimpleButton) Engine.getObject("next-button")).mapActionToState(ComponentState.ACTIVE, b -> {
				if (!((OneLineTextfield) Engine.getObject("username-field")).currentLine().isEmpty()) {
					// TODO : Save Name
					this.player = new Player(((OneLineTextfield) Engine.getObject("username-field")).currentLine());
					if (this.player.getName().equals("Horror") || this.player.getName().equals("Horreur")) {
						Engine.setObject("background",
								new ImageBackground(this, 0, 0, w, h, Images.get("Creepy Background")));
						Audios.stopAll();
						Audios.loop("creepy-main-theme", -1, 0.1f);
						AnimalBox.setCreepy();
					}
					((Label) Engine.getObject("username")).setText(new OnScreenText(
							"Bienvenue, " + ((OneLineTextfield) Engine.getObject("username-field")).currentLine() + "!",
							new OnScreenTextProperties(Fonts.get("blueberry", 48),
									Utilities.buildAgainst(Color.YELLOW, 200), Utilities.buildAgainst(Color.RED, 200),
									OnScreenTextPosition.CENTERED)));
					Engine.switchToScene("levels-menu");
				}
			});
		}
		// Levels menu objects
		{
			// Username at the top of the page
			Engine.setObject("username",
					new StaticLabel(this, new OnScreenText("", new OnScreenTextProperties(Fonts.get("blueberry", 48),
							Color.WHITE, Color.LIGHT_GRAY, OnScreenTextPosition.CENTERED)), 0, 0, w, h / 4));
			// Level buttons
			for (int i = 0; i < Level.MAX_LEVEL; i += 1) {
				Engine.setObject(String.format("level-%d-button", i + 1),
						new SimpleButton(this, 80 + (w / 6) + (((w / 6) - 80) * (i)), (h / 2) - 110, 40, 40,
								new OnScreenText(String.valueOf(i + 1),
										new OnScreenTextProperties(Fonts.get("blueberry", 32),
												Utilities.buildAgainst(Color.BLUE, 255), new Color(255, 0, 0, 0),
												OnScreenTextPosition.CENTERED)),
								Utilities.buildAgainst(Color.YELLOW, 150), Color.YELLOW.darker(), 0));
				((SimpleButton) Engine.getObject(String.format("level-%d-button", i + 1)))
						.mapActionToState(ComponentState.IDLE, b -> {
							((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.YELLOW, 150));
						});
				((SimpleButton) Engine.getObject(String.format("level-%d-button", i + 1)))
						.mapActionToState(ComponentState.HOVERED, b -> {
							((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.GREEN, 150));
						});
				final int j = i;
				((SimpleButton) Engine.getObject(String.format("level-%d-button", i + 1)))
						.mapActionToState(ComponentState.ACTIVE, b -> {
							Engine.switchToScene(String.format("level-%d", j + 1));
						});
			}
			// Infinite level button
			{
				Engine.setObject("infinite-level-button",
						new SimpleButton(this, (w / 2) - 100, (h / 2) + 30, 200, 60,
								new OnScreenText("Mode infini",
										new OnScreenTextProperties(Fonts.get("blueberry", 32),
												Utilities.buildAgainst(Color.BLUE, 255), new Color(0, 0, 255, 0),
												OnScreenTextPosition.CENTERED)),
								Utilities.buildAgainst(Color.YELLOW, 150), Color.YELLOW.darker(), 0));
				((SimpleButton) Engine.getObject("infinite-level-button")).mapActionToState(ComponentState.IDLE, b -> {
					((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.YELLOW, 150));
				});
				((SimpleButton) Engine.getObject("infinite-level-button")).mapActionToState(ComponentState.HOVERED,
						b -> {
							((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.GREEN, 150));
						});
				((SimpleButton) Engine.getObject("infinite-level-button")).mapActionToState(ComponentState.ACTIVE,
						b -> {
							Engine.switchToScene("infinite-level");
						});
			}
		}
		// Levels
		{
			Engine.setObject("score",
					new StaticLabel(this,
							new OnScreenText("Score: 0              Mouvements: -1",
									new OnScreenTextProperties(Fonts.get("blueberry", 48),
											Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
											OnScreenTextPosition.CENTERED)),
							0, 0, w, h / 4));
			Engine.setObject("bombs", new BombCheckbox(this, (w / 2) - 200, h - 90, 80, 80, new OnScreenText(
					"Utiliser une bombe? ()",
					new OnScreenTextProperties(Fonts.get("blueberry", 26), Utilities.buildAgainst(Color.YELLOW, 200),
							new Color(255, 0, 0, 200), OnScreenTextPosition.CENTERED)),
					Color.YELLOW, Color.RED, Color.RED, ComponentLabelPosition.RIGHT));
		}
		// Lose Screen
		{
			Engine.setObject("lose-text",
					new StaticLabel(this,
							new OnScreenText("Vous avez perdu!",
									new OnScreenTextProperties(Fonts.get("blueberry", 144),
											Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
											OnScreenTextPosition.CENTERED)),
							0, 0, w, h));
		}
		// Win Screen
		{
			Engine.setObject("win-text",
					new StaticLabel(this,
							new OnScreenText("Vous avez gagné!",
									new OnScreenTextProperties(Fonts.get("blueberry", 144),
											Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
											OnScreenTextPosition.CENTERED)),
							0, 0, w, h));
		}
		// Back button
		{
			Engine.setObject("back-button", new SimpleButton(this, (w / 2) - 150, h - 120, 300, 60, new OnScreenText(
					"Retour en arrière",
					new OnScreenTextProperties(Fonts.get("blueberry", 34), Utilities.buildAgainst(Color.YELLOW, 200),
							new Color(255, 0, 0, 200), OnScreenTextPosition.CENTERED)),
					Color.WHITE, new Color(0, 0, 0, 200), 15));
			((SimpleButton) Engine.getObject("back-button")).mapActionToState(ComponentState.IDLE, b -> {
				((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
			});

			((SimpleButton) Engine.getObject("back-button")).mapActionToState(ComponentState.HOVERED, b -> {
				((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
			});
			((SimpleButton) Engine.getObject("back-button")).mapActionToState(ComponentState.ACTIVE, b -> {
				Engine.switchToScene("levels-menu");
			});
		}
		// final Score
		{
			player = new Player("");
			for (int i = 0; i <= Level.MAX_LEVEL; i++) {
				if (i == 0) {
					Engine.setObject("high-score-infinity", new StaticLabel(this,
							new OnScreenText("Infinity score :" + player.bestScores[0],
									new OnScreenTextProperties(Fonts.get("blueberry", 144),
											Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
											OnScreenTextPosition.CENTERED)),
							0, -300, w, h));
				} else {
					Engine.setObject("high-score-" + i, new StaticLabel(this,
							new OnScreenText("Level " + i + " Score :" + player.bestScores[i],
									new OnScreenTextProperties(Fonts.get("blueberry", 144),
											Utilities.buildAgainst(Color.YELLOW, 200), new Color(255, 0, 0, 200),
											OnScreenTextPosition.CENTERED)),
							0, -300 + 60 * i, w, h));
				}
			}
		}

		// Statistics Button
		{
			Engine.setObject("stats-button", new SimpleButton(this, (w / 2) - 150, h - 120, 300, 60, new OnScreenText(
					"Vos Statistiques",
					new OnScreenTextProperties(Fonts.get("blueberry", 34), Utilities.buildAgainst(Color.YELLOW, 200),
							new Color(255, 0, 0, 200), OnScreenTextPosition.CENTERED)),
					Color.WHITE, new Color(0, 0, 0, 200), 15));
			((SimpleButton) Engine.getObject("stats-button")).mapActionToState(ComponentState.IDLE, b -> {
				((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 0));
			});

			((SimpleButton) Engine.getObject("stats-button")).mapActionToState(ComponentState.HOVERED, b -> {
				((SimpleButton) b).setBackgroundColor(Utilities.buildAgainst(Color.WHITE, 100));
			});
			((SimpleButton) Engine.getObject("stats-button")).mapActionToState(ComponentState.ACTIVE, b -> {
				Engine.switchToScene("stats-menu");
			});
		}
	}

	@Override
	protected void registerScenes() {
		Engine.registerScene("splashscreen", new Scene(this) {
			@Override
			public void enter() {
				TaskScheduler.scheduleTask("splash-out", new Task(5, TimeUnit.SECONDS) {
					@Override
					public TaskType type() {
						return TaskType.FIXED_RATE;
					}

					@Override
					public void execute() {
						Engine.switchToScene("main-menu");
					}
				});
			}

			@Override
			public void exit() {
				Audios.loop("main-theme", -1, 0.1f);
			}
		}.addObjects(Utilities.createArray("built-with", "engine-name", "credits")));
		((Scene) Engine.getObject("splashscreen")).setInTransition(new FadeInTransition(this, 90, Color.WHITE));
		((Scene) Engine.getObject("splashscreen")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		Engine.registerScene("main-menu", new Scene(this) {
			@Override
			public void enter() {
				return;
			}

			@Override
			public void exit() {
				return;
			}
		}.addObjects(new String[] { "background", "title", "play-button", "credits-button", "exit-button" }));
		((Scene) Engine.getObject("main-menu")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("main-menu")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		Engine.registerScene("name-menu", new Scene(this) {
			@Override
			public void enter() {
				return;
			}

			@Override
			public void exit() {
				return;
			}
		}.addObjects(new String[] { "background", "username-prompt", "username-field", "next-button" }));
		((Scene) Engine.getObject("name-menu")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("name-menu")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		Engine.registerScene("levels-menu", new Scene(this) {
			@Override
			public void enter() {
				return;
			}

			@Override
			public void exit() {
				return;
			}
		}.addObjects(
				new String[] { "background", "username", "infinite-level-button", "level-1-button", "level-2-button",
						"level-3-button", "level-4-button", "level-5-button", "level-6-button", "stats-button" }));
		((Scene) Engine.getObject("levels-menu")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("levels-menu")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		// Every level from 1 to 6
		for (int i = 1; i <= Level.MAX_LEVEL; i++) {
			final int j = i;
			try {
				Engine.registerScene("level-" + j, new Scene(this) {
					private final Level level = new Level(this.application, new Player(""), j);
					private int moves = this.level.getGrid().getMaxMoves();
					private boolean done = false, won = false, shouldSwitch = true, drawGrid = false;

					@Override
					public void enter() {
						this.setAfterInTransition(scene -> this.drawGrid = true);
						this.done = false;
						this.won = false;
						this.shouldSwitch = true;
						this.level.setName(PetRescue.this.player.getName());
						this.level.setScore(0);
						try {
							this.level.getGrid().init(j);
							this.moves = this.level.getGrid().getMaxMoves();
						} catch (final IOException ioException) {
							Logger.log(PetRescue.class, LogLevel.FATAL,
									"An exception has occurred while setting up level %d: %s", j,
									ioException.getMessage());
						}
						((Label) Engine.getObject("score")).text().content = "Score: 0              Mouvements: "
								+ (this.moves >= 0 ? this.moves : "illimités");
						((BombCheckbox) Engine.getObject("bombs")).text().content = "Utiliser une bombe? ("
								+ this.level.getPlayer().getBombs() + ")";
						return;
					}

					@Override
					public void exit() {
						return;
					}

					@Override
					public void update() {
						if (this.level.getPlayer().getCurrentLevel() < j && shouldSwitch) {
							shouldSwitch = false;
							Engine.switchToScene("levels-menu");
						} else if (done && shouldSwitch) {
							// System.out.println("PetRescue.registerScenes()");
							if (won) {
								try {
									this.level.getPlayer().save();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("should switch");
								Engine.switchToScene("won-scene");
							} else {
								// System.out.println("PetRescue.registerScenes() L");
								Engine.switchToScene("lost-scene");
							}
							shouldSwitch = false;
							// System.out.println(Engine.currentScene());
						} else {
							super.update();
							boolean canPlay = this.level.getGrid().canPlay() || (this.level.getPlayer().getBombs() > 0);
							if (this.moves == 0) {
								this.done = true;
								if (this.level.getGrid().hasWon()) {
									this.won = true;
								} else {
									this.won = false;
								}
							} else {
								if (this.level.getGrid().hasWon()) {
									this.won = true;
									this.done = true;
								} else if (canPlay) {
									final int boxSize = 60;
									final int offX = (this.application.window().getConfiguration().getWidth() / 2)
											- (boxSize * 3);
									final int offY = (this.application.window().getConfiguration().getHeight() / 2)
											- (boxSize * 3);
									for (int i = 0; i < this.level.getGrid().M; i += 1) {
										for (int j = 0; j < this.level.getGrid().N; j += 1) {
											final Rectangle rect = new Rectangle(offX + (boxSize * j),
													offY + (boxSize * i), boxSize, boxSize);
											if (rect.contains(this.application.mouse().getX(),
													this.application.mouse().getY())
													&& PetRescue.this.mouse
															.isButtonDownInCurrentFrame(Mouse.BUTTON_LEFT)) {
												if (this.level.getGrid().getBoxes()[i][j].isColorBox()
														|| this.level.getGrid().getBoxes()[i][j].isWoodenBox()) {
													boolean bomb = false;
													if (((BombCheckbox) Engine.getObject("bombs")).isChecked()
															&& this.level.getPlayer().useBomb()) {
														this.level.getPlayer().addBombs(1);
														bomb = true;
													}
													if (bomb) {
														Audios.play("bomb", 0.3f);
													} else {
														Audios.play("pop", 0.3f);
													}
													this.level.turn(i, j, bomb);
													if (!bomb && (this.moves > 0)) {
														this.moves--;
													}
													((StaticLabel) Engine.getObject("score")).text().content = "Score: "
															+ this.level.getScore() + "              Moves: "
															+ (this.moves >= 0 ? this.moves : "Infinity");
													((BombCheckbox) Engine.getObject("bombs"))
															.text().content = "Utiliser une bombe? ("
																	+ this.level.getPlayer().getBombs() + ")";
													if (this.level.getPlayer().getBombs() < 0) {
														((BombCheckbox) Engine.getObject("bombs")).setChecked(false);
													}
												}
											}
										}
									}
								} else {
									this.done = true;
									this.won = this.level.getGrid().hasWon();
								}
							}
						}
					}

					@Override
					public void render() {
						super.render();
						if (!this.done && this.drawGrid) {
							final int boxSize = 60;
							final int offX = (this.application.window().getConfiguration().getWidth() / 2)
									- (boxSize * 3);
							final int offY = (this.application.window().getConfiguration().getHeight() / 2)
									- (boxSize * 3);
							for (int i = 0; i < this.level.getGrid().M; i += 1) {
								for (int j = 0; j < this.level.getGrid().N; j += 1) {
									this.level.getGrid().getBoxes()[i][j].draw(offX + (boxSize * j),
											offY + (boxSize * i), boxSize);
								}
							}
						}
					}
				}.addObjects(Utilities.createArray("background", "score", "end", "bombs")));
				((Scene) Engine.getObject("level-" + i)).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
				((Scene) Engine.getObject("level-" + i)).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
			} catch (final IOException ioException) {
				Logger.log(PetRescue.class, LogLevel.FATAL,
						"An exception occurred while loading data for level " + i + ": %s", ioException.getMessage());
			}
		}
		// Infinite level
		try {
			Engine.registerScene("infinite-level", new Scene(this) {
				private final Level level = new Level(this.application, new Player(""), 0);
				private final int moves = this.level.getGrid().getMaxMoves();

				@Override
				public void enter() {
					this.level.setName(PetRescue.this.player.getName());
					try {
						this.level.getPlayer().load(PetRescue.this.player.getName());
						this.level.getGrid().init(0);
						this.level.setScore(0);
					} catch (final IOException e) {
						e.printStackTrace();
					}
					((Label) Engine.getObject("score")).text().content = "Score: 0              Mouvements: "
							+ (this.moves >= 0 ? this.moves : "illimités");
					((BombCheckbox) Engine.getObject("bombs")).text().content = "Utiliser une bombe? ("
							+ this.level.getPlayer().getBombs() + ")";
					return;
				}

				@Override
				public void exit() {
					return;
				}

				@Override
				public void update() {
					super.update();
					final int boxSize = 60;
					final int offX = (this.application.window().getConfiguration().getWidth() / 2) - (boxSize * 3);
					final int offY = (this.application.window().getConfiguration().getHeight() / 2) - (boxSize * 3);
					for (int i = 0; i < this.level.getGrid().M; i += 1) {
						for (int j = 0; j < this.level.getGrid().N; j += 1) {
							final Rectangle rect = new Rectangle(offX + (boxSize * j), offY + (boxSize * i), boxSize,
									boxSize);
							if (rect.contains(this.application.mouse().getX(), this.application.mouse().getY())
									&& PetRescue.this.mouse.isButtonDownInCurrentFrame(Mouse.BUTTON_LEFT)) {
								if (this.level.getGrid().getBoxes()[i][j].isColorBox()
										|| this.level.getGrid().getBoxes()[i][j].isWoodenBox()) {
									boolean bomb = false;
									if (((BombCheckbox) Engine.getObject("bombs")).isChecked()
											&& this.level.getPlayer().useBomb()) {
										this.level.getPlayer().addBombs(1);
										bomb = true;
									}
									if (bomb) {
										Audios.play("bomb", 0.3f);
									} else {
										Audios.play("pop", 0.3f);
									}
									this.level.turn(i, j, bomb);
									this.level.update();
									((StaticLabel) Engine.getObject("score")).text().content = "Score: "
											+ this.level.getScore() + "              Mouvements: "
											+ (this.moves >= 0 ? this.moves : "illimités");
									((BombCheckbox) Engine.getObject("bombs")).text().content = "Utiliser une bombe? ("
											+ this.level.getPlayer().getBombs() + ")";
									if (this.level.getPlayer().getBombs() < 0) {
										((BombCheckbox) Engine.getObject("bombs")).setChecked(false);
									}
								}
							}
						}
					}
				}

				@Override
				public void render() {
					super.render();
					final int boxSize = 60;
					final int offX = (this.application.window().getConfiguration().getWidth() / 2) - (boxSize * 3);
					final int offY = (this.application.window().getConfiguration().getHeight() / 2) - (boxSize * 3);
					for (int i = 0; i < this.level.getGrid().M; i += 1) {
						for (int j = 0; j < this.level.getGrid().N; j += 1) {
							this.level.getGrid().getBoxes()[i][j].draw(offX + (boxSize * j), offY + (boxSize * i),
									boxSize);
						}
					}
					if (PetRescue.this.keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
						this.level.getPlayer().bestScores[0] = Math.max(this.level.getScore(),
								this.level.getPlayer().bestScores[0]);
						try {
							this.level.getPlayer().save();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						Engine.switchToScene("levels-menu");
					}
				}
			}.addObjects(Utilities.createArray("background", "score", "end", "bombs")));
		} catch (final IOException ioException) {
			Logger.log(PetRescue.class, LogLevel.FATAL,
					"An exception occurred while loading data for infinite level : %s", ioException.getMessage());
		}
		((Scene) Engine.getObject("infinite-level")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("infinite-level")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		// Losing screen
		Engine.registerScene("lost-scene", new Scene(this) {
			@Override
			public void enter() {
				return;
			}

			@Override
			public void exit() {
				return;
			}
		}.addObjects(new String[] { "background", "lose-text", "back-button" }));
		((Scene) Engine.getObject("lost-scene")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("lost-scene")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		// Win Screen
		Engine.registerScene("won-scene", new Scene(this) {
			@Override
			public void enter() {
			}

			@Override
			public void exit() {
			}
		}.addObjects(new String[] { "background", "win-text", "back-button" }));
		((Scene) Engine.getObject("won-scene")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("won-scene")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		// Stats menu
		Engine.registerScene("stats-menu", new Scene(this) {
			@Override
			public void enter() {
				try {
					PetRescue.this.player.load(PetRescue.this.player.getName());
				} catch (FileNotFoundException exception) {
					Logger.log(Level.class, LogLevel.SEVERE, "An exception occurred while loading data: %s",
							exception.getMessage());
				}
				for (int i = 0; i <= Level.MAX_LEVEL; i++) {
					if (i == 0) {
						((Label) Engine.getObject("high-score-infinity")).text().content = "Infinity :"
								+ PetRescue.this.player.bestScores[0];
					} else {
						((Label) Engine.getObject("high-score-" + i)).text().content = "Level " + i + " :"
								+ PetRescue.this.player.bestScores[i];
					}
				}
			}

			@Override
			public void exit() {
			}
		}.addObjects(new String[] { "background", "high-score-infinity", "high-score-1", "high-score-2", "high-score-3",
				"high-score-4", "high-score-5", "high-score-6", "back-button" }));
		((Scene) Engine.getObject("won-scene")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("won-scene")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
		// Credits menu
		Engine.registerScene("credits-scene", new Scene(this) {
			@Override
			public void enter() {
			}

			@Override
			public void exit() {
			}
		}.addObjects(new String[] { "background", "thank-you-all", "enjoy", "hope-you-find-easter-egg",
				"go-back-menu-button" }));
		((Scene) Engine.getObject("credits-scene")).setInTransition(new FadeInTransition(this, 90, Color.BLACK));
		((Scene) Engine.getObject("credits-scene")).setOutTransition(new FadeOutTransition(this, 90, Color.BLACK));
	}

	@Override
	protected void registerBinds() {
		Binds.registerBind("splashscreen", Utilities.createArray(Keyboard.KEY_ENTER),
				Utilities.createArray(Keyboard.KeyState.DOWN_IN_CURRENT_FRAME), o -> {
					if (!this.exitingSplash) {
						this.exitingSplash = true;
						TaskScheduler.cancelTask("splash-out", false);
						Engine.switchToScene("main-menu");
					}
				});
		Binds.registerBind("main-menu", Utilities.createArray(Keyboard.KEY_ESCAPE),
				Utilities.createArray(Keyboard.KeyState.DOWN_IN_CURRENT_FRAME), object -> this.stop());
		Binds.registerBind("name-menu", Utilities.createArray(Keyboard.KEY_ESCAPE),
				Utilities.createArray(Keyboard.KeyState.DOWN_IN_CURRENT_FRAME), object -> {
					((Textfield) Engine.getObject("username-field")).setCurrentLine("");
					((Textfield) Engine.getObject("username-field")).setCursorPosition(0);
					Engine.switchToScene("main-menu");
				});
		Binds.registerBind("levels-menu", Utilities.createArray(Keyboard.KEY_ESCAPE),
				Utilities.createArray(Keyboard.KeyState.DOWN_IN_CURRENT_FRAME),
				object -> Engine.switchToScene("name-menu"));
		for (int i = 1; i <= Level.MAX_LEVEL; i++) {
			Binds.registerBind("level-" + i, Utilities.createArray(Keyboard.KEY_ESCAPE),
					Utilities.createArray(Keyboard.KeyState.DOWN_IN_CURRENT_FRAME),
					object -> Engine.switchToScene("levels-menu"));
		}
	}

	@Override
	protected WindowConfiguration windowConfiguration() {
		return new WindowConfiguration(PetRescue.WINDOW_TITLE, PetRescue.WINDOW_WIDTH, PetRescue.WINDOW_HEIGHT,
				PetRescue.FULLSCREEN, PetRescue.BUFFERS).setIconImagePath("res/images/icon.jpg");
	}

	@Override
	protected void createThread(final ThreadWrapperType type) {
		switch (type) {
		case RENDER:
			this.window.show();
		case UPDATE:
			return;
		}
	}

	@Override
	protected void destroyThread(final ThreadWrapperType type) {
		switch (type) {
		case RENDER:
			Audios.stopAll();
			this.window.hide(true);
			return;
		case UPDATE:
			return;
		}

	}

	@Override
	public String firstSceneIdentifier() {
		return "splashscreen";
	}

	@Override
	protected void render() {
		this.screen().focusOnQuality();
		super.render();
	}

	public static void main(final String[] args) {
		Logger.toggleDebugging(DebuggingType.CLASSIC, true);
		Logger.toggleDebugging(DebuggingType.DEVELOPMENT, false);
		new PetRescue().start();
	}
}
