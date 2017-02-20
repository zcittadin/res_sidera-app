package com.estatica.servicos.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.estatica.servicos.view.ControlledScreen;

import javafx.fxml.Initializable;

public class Reator5Controller implements Initializable, ControlledScreen {

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
