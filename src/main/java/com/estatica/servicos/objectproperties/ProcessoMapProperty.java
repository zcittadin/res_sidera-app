package com.estatica.servicos.objectproperties;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;

public class ProcessoMapProperty {

	private static SimpleStringProperty keyProperty = new SimpleStringProperty();
	private static Map<String, ProcessoConfigParams> configMapParams = new HashMap<>();

	public static SimpleStringProperty keyProperty() {
		return keyProperty;
	}

	public static String getKey() {
		return keyProperty().get();
	}

	public static void setKey(String key) {
		keyProperty().set(key);
	}

	public static void setConfigParam(String key, ProcessoConfigParams params) {
		configMapParams.put(key, params);
	}

	public static ProcessoConfigParams getConfigParam(String key) {
		return configMapParams.get(key);
	}

	public static Map<String, ProcessoConfigParams> getConfigMapParams() {
		return configMapParams;
	}
}
