package com.estatica.servicos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.estatica.servicos.objectproperties.CurrentScreenProperty;
import com.estatica.servicos.objectproperties.ProcessoValueProperty;
import com.estatica.servicos.view.ControlledScreen;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class PaginaInicialController implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private ImageView imgReator1;
	@FXML
	private ImageView imgReator2;
	@FXML
	private ImageView imgReator3;
	@FXML
	private ImageView imgReator4;
	@FXML
	private ImageView imgReator5;
	@FXML
	private ImageView imgReator6;
	@FXML
	private Label lblTempR1;
	@FXML
	private Label lblTempR2;
	@FXML
	private Label lblTempR3;
	@FXML
	private Label lblTempR4;
	@FXML
	private Label lblTempR5;
	@FXML
	private Label lblTempR6;
	@FXML
	private Label lblSetPointR1;
	@FXML
	private Label lblSetPointR2;
	@FXML
	private Label lblSetPointR3;
	@FXML
	private Label lblSetPointR4;
	@FXML
	private Label lblSetPointR5;
	@FXML
	private Label lblSetPointR6;
	@FXML
	private ImageView imgEstatica;

	private static Timeline scanModbusSlaves;
	private static FadeTransition estaticaFadeTransition;
	private static ImageViewResizer imgResizer;
	private Double tempR1;
	private Double tempR2;
	private Double tempR3;
	private Double tempR4;
	private Double tempR5;
	private Double tempR6;
	private Double setPointR1;
	private Double setPointR2;
	private Double setPointR3;
	private Double setPointR4;
	private Double setPointR5;
	private Double setPointR6;

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);
		imgEstatica.setImage(new Image("/img/logotipo.png"));
		imgResizer = new ImageViewResizer(imgEstatica, 138, 42);
		imgResizer.setLayoutX(150.0);
		imgResizer.setLayoutY(150.0);
		imgResizer.setLayoutX(1083);
		imgResizer.setLayoutY(607);
		mainPane.getChildren().addAll(imgResizer);
		imgReator1.setImage(new Image("/img/siemens.png"));
		imgReator2.setImage(new Image("/img/siemens.png"));
		imgReator3.setImage(new Image("/img/siemens.png"));
		imgReator4.setImage(new Image("/img/siemens.png"));
		imgReator5.setImage(new Image("/img/siemens.png"));
		imgReator6.setImage(new Image("/img/siemens.png"));
		initModbusScan();
	}

	@FXML
	private void openReator1() {
		CurrentScreenProperty.setScreen("REATOR1");
		myController.setScreen("reator1");
	}

	@FXML
	private void openReator2() {
		CurrentScreenProperty.setScreen("REATOR2");
		myController.setScreen("reator2");
	}

	@FXML
	private void openReator3() {
		CurrentScreenProperty.setScreen("REATOR3");
		myController.setScreen("reator3");
	}

	@FXML
	private void openReator4() {
		CurrentScreenProperty.setScreen("REATOR4");
		myController.setScreen("reator4");
	}

	@FXML
	private void openReator5() {
		CurrentScreenProperty.setScreen("REATOR5");
		myController.setScreen("reator5");
	}

	@FXML
	private void openReator6() {
		CurrentScreenProperty.setScreen("REATOR6");
		myController.setScreen("reator6");
	}

	@FXML
	public void hoverImgEstatica() {
		estaticaFadeTransition.setFromValue(0.2);
		estaticaFadeTransition.setToValue(1.0);
		estaticaFadeTransition.play();
	}

	@FXML
	public void unhoverImgEstatica() {
		estaticaFadeTransition.setFromValue(1.0);
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}

	@FXML
	private void handleImgEstaticaAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/estatica/servicos/view/EstaticaInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o fabricante");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
		estaticaFadeTransition.setFromValue(imgEstatica.getOpacity());
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}

	private void initModbusScan() {
		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tempR1 = ProcessoValueProperty.getTempReator1();
				setPointR1 = ProcessoValueProperty.getSpReator1();
				tempR2 = ProcessoValueProperty.getTempReator2();
				setPointR2 = ProcessoValueProperty.getSpReator2();
				tempR3 = ProcessoValueProperty.getTempReator1();
				setPointR3 = ProcessoValueProperty.getSpReator1();
				tempR4 = ProcessoValueProperty.getTempReator2();
				setPointR4 = ProcessoValueProperty.getSpReator2();
				tempR5 = ProcessoValueProperty.getTempReator1();
				setPointR5 = ProcessoValueProperty.getSpReator1();
				tempR6 = ProcessoValueProperty.getTempReator2();
				setPointR6 = ProcessoValueProperty.getSpReator2();

				lblTempR1.setText(String.valueOf(tempR1));
				lblTempR2.setText(String.valueOf(tempR2));
				lblTempR3.setText(String.valueOf(tempR3));
				lblTempR4.setText(String.valueOf(tempR4));
				lblTempR5.setText(String.valueOf(tempR5));
				lblTempR6.setText(String.valueOf(tempR6));

				lblSetPointR1.setText(String.valueOf(setPointR1));
				lblSetPointR2.setText(String.valueOf(setPointR2));
				lblSetPointR3.setText(String.valueOf(setPointR3));
				lblSetPointR4.setText(String.valueOf(setPointR4));
				lblSetPointR5.setText(String.valueOf(setPointR5));
				lblSetPointR6.setText(String.valueOf(setPointR6));
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);
		scanModbusSlaves.play();
	}

}
