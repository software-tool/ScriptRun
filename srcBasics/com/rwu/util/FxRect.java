package com.rwu.util;

public class FxRect {

	public double width;
	public double height;

	public FxRect(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "FxRect [width=" + width + ", height=" + height + "]";
	}

}
