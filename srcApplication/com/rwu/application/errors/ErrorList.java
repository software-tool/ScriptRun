package com.rwu.application.errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorList {

	private List<String> errors = new ArrayList<>();

	public void addError(String error) {
		this.errors.add(error);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public String getText(int textLimit) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (String error : errors) {
			if (sb.length() > textLimit) {
				// Max length exceeded

				sb.append("\n");
				sb.append("(...)");
				break;
			}

			if (i != 0) {
				sb.append("\n");
			}

			sb.append(error);

			i++;
		}

		return sb.toString();
	}
}
