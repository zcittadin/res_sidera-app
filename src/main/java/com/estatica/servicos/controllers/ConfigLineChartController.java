package com.estatica.servicos.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.estatice.servicos.objectproperties.MarkLineChartProperty;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class ConfigLineChartController implements Initializable {

	@FXML
	private Button btOk;
	@FXML
	private CheckBox chkMarcadores;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(MarkLineChartProperty.getMark())
			chkMarcadores.setSelected(Boolean.TRUE);

	}
	
	@FXML
	public void toggleMark(){
		MarkLineChartProperty.setMark(chkMarcadores.isSelected());
	}
	
	@FXML
	public void exit() {
		Stage stage = (Stage) btOk.getScene().getWindow();
		stage.close();
	}

}