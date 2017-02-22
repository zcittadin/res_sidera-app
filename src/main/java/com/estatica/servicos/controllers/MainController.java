package com.estatica.servicos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.estatica.servicos.objectproperties.StyleClockProperty;
import com.estatica.servicos.service.ProcessoStatusManager;

import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.LcdDesign;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class MainController implements Initializable {

	public static String screen1ID = "reator1";
	public static String screen1File = "/com/estatica/servicos/view/Reator1.fxml";
	public static String screen2ID = "reator2";
	public static String screen2File = "/com/estatica/servicos/view/Reator2.fxml";
	public static String screen3ID = "reator3";
	public static String screen3File = "/com/estatica/servicos/view/Reator3.fxml";
	public static String screen4ID = "reator4";
	public static String screen4File = "/com/estatica/servicos/view/Reator4.fxml";
	public static String screen5ID = "reator5";
	public static String screen5File = "/com/estatica/servicos/view/Reator5.fxml";
	public static String screen6ID = "reator6";
	public static String screen6File = "/com/estatica/servicos/view/Reator6.fxml";
	public static String screenConsultaID = "consulta";
	public static String screenConsultaFile = "/com/estatica/servicos/view/Consultas.fxml";

	@FXML
	private AnchorPane mainPane;
	@FXML
	private ImageView imgCliente;
	@FXML
	private ImageView imgExit;
	@FXML
	private Pane centralPane;
	@FXML
	private Button btStyleClock;
	@FXML
	private Clock clock;

	private static ImageViewResizer imgClienteResizer;
	private static ImageViewResizer imgExitResizer;
	private static Timeline tmlBtClockGrow = new Timeline();
	private static Timeline tmlBtClockShrink = new Timeline();
	ScreensController mainContainer = new ScreensController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		StyleClockProperty.lcdDesignProperty().addListener(new ChangeListener<LcdDesign>() {
			@Override
			public void changed(ObservableValue<? extends LcdDesign> observable, LcdDesign oldValue,
					LcdDesign newValue) {
				clock.setLcdDesign(newValue);
			}
		});

		imgCliente.setImage(new Image("/img/resicolor.png"));
		imgClienteResizer = new ImageViewResizer(imgCliente, 124, 70);
		imgClienteResizer.setLayoutX(12);
		imgClienteResizer.setLayoutY(6);
		imgExitResizer = new ImageViewResizer(imgExit, 70, 71);
		imgExitResizer.setLayoutX(50);
		imgExitResizer.setLayoutY(633);
		mainPane.getChildren().addAll(imgClienteResizer, imgExitResizer);

		Tooltip.install(imgExit, new Tooltip("Sair do sistema"));
		Tooltip.install(btStyleClock, new Tooltip("Alterar estilo do relógio"));
		Tooltip.install(imgCliente, new Tooltip("Resicolor Tintas e Solventes"));

		tmlBtClockGrow.getKeyFrames()
				.addAll(new KeyFrame(Duration.seconds(0.3), new KeyValue(btStyleClock.translateXProperty(), -105)));
		tmlBtClockShrink.getKeyFrames()
				.addAll(new KeyFrame(Duration.seconds(0.3), new KeyValue(btStyleClock.translateXProperty(), 0)));

		mainContainer.loadScreen(screen1ID, screen1File);
		mainContainer.loadScreen(screen2ID, screen2File);
		mainContainer.loadScreen(screen3ID, screen3File);
		mainContainer.loadScreen(screen4ID, screen4File);
		mainContainer.loadScreen(screen5ID, screen5File);
		mainContainer.loadScreen(screen6ID, screen6File);
		mainContainer.loadScreen(screenConsultaID, screenConsultaFile);

		ProcessoStatusManager.setProcessoStatus("REATOR1", Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus("REATOR2", Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus("REATOR3", Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus("REATOR4", Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus("REATOR5", Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus("REATOR6", Boolean.FALSE);

		mainContainer.setScreen(screen1ID);
		centralPane.getChildren().addAll(mainContainer);
		clock.setLcdDesign(LcdDesign.STANDARD_GREEN);
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.STANDARD_GREEN);
	}

	@FXML
	private void openReator1() {
		mainContainer.setScreen(screen1ID);
	}

	@FXML
	private void openReator2() {
		mainContainer.setScreen(screen2ID);
	}

	@FXML
	private void openReator3() {
		mainContainer.setScreen(screen3ID);
	}

	@FXML
	private void openReator4() {
		mainContainer.setScreen(screen4ID);
	}

	@FXML
	private void openReator5() {
		mainContainer.setScreen(screen5ID);
	}

	@FXML
	private void openReator6() {
		mainContainer.setScreen(screen6ID);
	}

	@FXML
	private void openConsultas() {
		mainContainer.setScreen(screenConsultaID);
	}

	@FXML
	private void exit() {
		if (ProcessoStatusManager.verifyProcessoRunning())
			return;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente sair do sistema?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Platform.exit();
		}
	}

	@FXML
	public void openStyleOptions() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/estatica/servicos/view/ConfigClockStyle.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Estilo do relógio");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgExit.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	@FXML
	private void handleImgClienteAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/estatica/servicos/view/ClienteInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o cliente");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgCliente.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	@FXML
	private void hoverBtClock() {
		tmlBtClockGrow.play();
	}

	@FXML
	private void unhoverBtClock() {
		tmlBtClockShrink.play();
	}

}