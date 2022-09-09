package com.skanderj.lucidityengine.exts;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.core.Priority;
import com.skanderj.lucidityengine.graphics.OnScreenText;
import com.skanderj.lucidityengine.graphics.OnScreenTextProperties;
import com.skanderj.lucidityengine.graphics.Screen;
import com.skanderj.lucidityengine.graphics.components.Textfield;

/**
 * A very basic textbox.
 *
 * @author Skander
 *
 */
public final class OneLineTextfield extends Textfield {
	private double x, y;
	private int width, height;
	private final int initialWidth;
	private Color backgroundColor;
	private final OnScreenTextProperties textProperties;
	// Does the cursor blink?
	protected boolean cursorBlink;
	// Cursor's blink related
	protected int blinkDelay, blinkTimer;

	/**
	 * Background color for rendering a simple box, text properties for the font and
	 * color, and amount maximumLines to display.
	 */
	public OneLineTextfield(final Application application, final double x, final double y, final int width, final Color backgroundColor, final OnScreenTextProperties textProperties) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.initialWidth = width;
		this.height = 0;
		this.backgroundColor = backgroundColor;
		this.textProperties = textProperties;
		// Cursor always blinks by default, I mean why not
		this.cursorBlink = false;
		// Cursor will blink 60/15 times per second (=4)
		this.blinkDelay = 20;
		// Timer, pretty basic
		this.blinkTimer = 0;
	}

	@Override
	public void update() {
		super.update();
		// Cursor blinking timer
		{
			this.blinkTimer += 1;
			if ((this.blinkTimer % this.blinkDelay) == 0) {
				this.cursorBlink = !this.cursorBlink;
			}
		}
	}

	/**
	 * The rendering routine.
	 */
	@Override
	public void render() {
		final Screen screen = this.application.screen();
		screen.setFont(this.textProperties.font);
		final FontMetrics metrics = screen.getFontMetrics();
		int oldWidth = this.width;
		this.setWidth(initialWidth + metrics.stringWidth(this.currentLine));
		int diff = oldWidth - this.getWidth();
		this.x += diff / 2;
		// Determine height if not done before (= 0)
		{
			if (this.height == 0) {
				this.height = metrics.getHeight() + (metrics.getHeight() / 3);
			}
		}
		// Background
		{
			screen.drawRectangle((int) this.x, (int) this.y, this.width, this.height, this.backgroundColor, 0, 0, true);
		}
		// Border
		{
			screen.drawRectangle((int) this.x, (int) this.y, this.width, this.height, this.backgroundColor.darker().darker(), 0, 0, false);
		}
		// Label color & font
		{
			screen.setColor(this.textProperties.color);
			screen.setFont(this.textProperties.font);
		}
		new OnScreenText(this.currentLine, this.textProperties).draw(screen, (int) this.x, (int) this.y, this.width, this.height);
//		if (this.hasFocus) {
//			if (this.cursorBlink) {
//				screen.drawRectangle(cursorX, cursorY, cursorWidth, cursorHeight, Color.WHITE, 0, 0, true);
//			}
//		}
	}

	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Returns a string width up to cursor-characters.
	 */
//	private int stringWidth(final FontMetrics metrics, final String string, final int cursor) {
//		return metrics.stringWidth(string.substring(0, cursor));
//	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	public Color backgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public OnScreenTextProperties textProperties() {
		return this.textProperties;
	}

	/**
	 * Self explanatory.
	 */
	public int blinkDelay() {
		return this.blinkDelay;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.NORMAL;
	}

	@Override
	public String toString() {
		return "OneLineTextField";
	}
}