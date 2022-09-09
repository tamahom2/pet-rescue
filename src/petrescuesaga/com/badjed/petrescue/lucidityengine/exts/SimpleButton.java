package com.badjed.petrescue.lucidityengine.exts;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.core.Priority;
import com.skanderj.lucidityengine.graphics.OnScreenText;
import com.skanderj.lucidityengine.graphics.Screen;
import com.skanderj.lucidityengine.graphics.components.Button;

/**
 * A round edges version of the button. Still very basic.
 *
 * @author Skander
 *
 */
public final class SimpleButton extends Button {
	private double x, y;
	private int width, height;
	private OnScreenText text;
	private Color backgroundColor, borderColor;
	// Border incline = how many pixels will be shaved off at each edge
	private int borderIncline;

	public SimpleButton(final Application application, final double x, final double y, final int width, final int height, final OnScreenText text, final Color backgroundColor, final Color borderColor, final int borderIncline) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderIncline = borderIncline;
	}

	/**
	 * Draws a simple round rectangle for the background, draws the border and the
	 * text.
	 */
	@Override
	public void render() {
		final Screen screen = this.application.screen();
		screen.drawRectangle((int) this.x, (int) this.y, this.width, this.height, this.backgroundColor, this.borderIncline, this.borderIncline, true);
		this.text.draw(screen, (int) this.x, (int) this.y, this.width, this.height);
		screen.drawRectangle((int) this.x, (int) this.y, this.width, this.height, this.borderColor, this.borderIncline, this.borderIncline, false);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height).contains(x, y);
	}

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
	public OnScreenText text() {
		return this.text;
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
	public Color borderColor() {
		return this.borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public int borderIncline() {
		return this.borderIncline;
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
	public void setText(final OnScreenText text) {
		this.text = text;
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
	public void setBorderColor(final Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public void setBorderIncline(final int borderIncline) {
		this.borderIncline = borderIncline;
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
		return "PlainButton";
	}
}