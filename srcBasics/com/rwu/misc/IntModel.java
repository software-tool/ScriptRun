package com.rwu.misc;

public class IntModel extends ValueModel<Integer> {

	public IntModel() {
		super(0);
	}

	public IntModel(Integer value) {
		super(value);
	}

	public void increment() {
		add(1);
	}

}
