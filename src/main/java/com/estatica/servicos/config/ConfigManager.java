package com.estatica.servicos.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

	private static Map<String, ProcessoConfig> configMap = new HashMap<>();

	public static Map<String, ProcessoConfig> getConfigMap() {
		return configMap;
	}

	public static void setConfigMap(Map<String, ProcessoConfig> confList) {
		ConfigManager.configMap = confList;
	}

}
