package com.estatica.servicos.util;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class IntegerFilter implements UnaryOperator<TextFormatter.Change> {
	private final static Pattern DIGIT_PATTERN = Pattern.compile("\\d*");

	@Override
	public Change apply(TextFormatter.Change aT) {
		return DIGIT_PATTERN.matcher(aT.getText()).matches() ? aT : null;
	}
}
