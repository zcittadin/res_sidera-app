package com.estatica.servicos.dto;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProcessoStatusDTO {

	private static Map<String, Boolean> processoStatus = new HashMap<>();

	public static void setProcessoStatus(String procName, Boolean status) {
		processoStatus.put(procName, status);
	}

	public static Boolean getProcessoStatus(String procName) {
		return processoStatus.get(procName);
	}

	public static Map<String, Boolean> getProcessos() {
		return processoStatus;
	}

	public static Boolean verifyProcessoRunning() {
		for (String procName : ProcessoStatusDTO.getProcessos().keySet()) {
			if (ProcessoStatusDTO.getProcessoStatus(procName)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Aten��o");
				alert.setHeaderText(
						"Existem processo que ainda n�o foram finalizados. Finalize-os antes de encerrar o sistema.");
				alert.showAndWait();
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
