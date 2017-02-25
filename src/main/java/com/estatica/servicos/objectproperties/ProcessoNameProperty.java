package com.estatica.servicos.objectproperties;

import javafx.beans.property.SimpleStringProperty;

public class ProcessoNameProperty {

	private static SimpleStringProperty nameProperty = new SimpleStringProperty();

	public static SimpleStringProperty nameProperty() {
		return nameProperty;
	}

	public static String getName() {
		return nameProperty().get();
	}

	public static void setName(String name) {
		nameProperty().set(name);
	}
}
