package com.skanderj.lucidityengine.exts;

import java.awt.Rectangle;

import com.skanderj.lucidityengine.Application;
import com.skanderj.lucidityengine.core.Priority;
import com.skanderj.lucidityengine.graphics.OnScreenText;
import com.skanderj.lucidityengine.graphics.components.Label;

public class StaticLabel extends Label {
	private final double x0, y0;
	private final int width, height;

	public StaticLabel(final Application application, final OnScreenText text, final int x0, final int y0, final int width, final int height) {
		super(application, text);
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
	}

	@Override
	public Priority priority() {
		return Priority.NORMAL;
	}

	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x0, (int) this.y0, this.width, this.height).contains(x, y);
	}

	@Override
	public double getX() {
		return this.x0;
	}

	@Override
	public double getY() {
		return this.y0;
	}

	@Override
	public void setX(final double x) {
		return;
	}

	@Override
	public void setY(final double y) {
		return;
	}

	@Override
	public int getWidth() {
		return this.application.screen().getFontMetrics().stringWidth(this.text.content);
	}

	@Override
	public int getHeight() {
		return this.application.screen().getFontMetrics().getHeight();
	}

	@Override
	public void setWidth(final int width) {
		return;
	}

	@Override
	public void setHeight(final int height) {
		return;
	}

	@Override
	public void update() {
		return;
	}

	@Override
	public void render() {
		this.text.draw(this.application.screen(), (int) this.x0, (int) this.y0, this.width, this.height);
	}

	@Override
	public String toString() {
		return "StaticLabel";
	}
}
