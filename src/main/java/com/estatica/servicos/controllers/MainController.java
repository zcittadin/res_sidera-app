package com.estatica.servicos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.estatica.servicos.modbus.ModbusRTUService;
import com.estatica.servicos.objectproperties.CurrentScreenProperty;
import com.estatica.servicos.objectproperties.ProcessoConfigParams;
import com.estatica.servicos.objectproperties.ProcessoMapProperty;
import com.estatica.servicos.objectproperties.ProcessoValueProperty;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	public static String screenInicioID = "inicio";
	public static String screenInicioFile = "/com/estatica/servicos/view/PaginaInicial.fxml";

	public static Tooltip TOOLTIP_BT_SAIR = new Tooltip("Sair do sistema");
	public static Tooltip TOOLTIP_BT_CONF_CLOCK = new Tooltip("Alterar estilo do rel�gio");
	public static Tooltip TOOLTIP_LOGO_CLIENTE = new Tooltip("Resicolor Ind�stria de Produtos Qu�micos Ltda.");

	public static String IMG_CLIENTE_PATH = "/img/resicolor.png";
	public static String ALERT_EXIT = "Deseja realmente sair do sistema?";
	public static String ALERT_EXIT_TITLE = "Confirmar encerramento";

	public static String WINDOW_CONFIG_CLOCK_PATH = "/com/estatica/servicos/view/ConfigClockStyle.fxml";
	public static String WINDOW_CONFIG_CLOCK_TITLE = "Estilo do rel�gio";
	public static String WINDOW_CLIENTE_INFO_PATH = "/com/estatica/servicos/view/ClienteInfo.fxml";
	public static String WINDOW_CLIENTE_INFO_TITLE = "Informa��es sobre o cliente";
	private static String COM_PORT = "COM4";

	public static String NOME_REATOR_1 = "REATOR1";
	public static String NOME_REATOR_2 = "REATOR2";
	public static String NOME_REATOR_3 = "REATOR3";
	public static String NOME_REATOR_4 = "REATOR4";
	public static String NOME_REATOR_5 = "REATOR5";
	public static String NOME_REATOR_6 = "REATOR6";

	private static Integer baud = 9600;

	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; "
			+ "-fx-background-color: #2F4F4F; -fx-border-color: white; -fx-border-radius: 10px;";

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
	private static Timeline scanModbusSlaves = new Timeline();
	private static Timeline tmlBtClockGrow = new Timeline();
	private static Timeline tmlBtClockShrink = new Timeline();
	private static ModbusRTUService modService = new ModbusRTUService();

	private static int slaveID = 1;
	private Double tempReator = new Double(0);
	private Double setPointReator = new Double(0);

	ScreensController mainContainer = new ScreensController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		modService.setConnectionParams(COM_PORT, baud);
		modService.openConnection();
		initModbusReadSlaves();

		StyleClockProperty.lcdDesignProperty().addListener(new ChangeListener<LcdDesign>() {
			@Override
			public void changed(ObservableValue<? extends LcdDesign> observable, LcdDesign oldValue,
					LcdDesign newValue) {
				clock.setLcdDesign(newValue);
			}
		});

		imgCliente.setImage(new Image(IMG_CLIENTE_PATH));
		imgClienteResizer = new ImageViewResizer(imgCliente, 126, 70);
		imgClienteResizer.setLayoutX(16);
		imgClienteResizer.setLayoutY(6);
		imgExitResizer = new ImageViewResizer(imgExit, 70, 71);
		imgExitResizer.setLayoutX(50);
		imgExitResizer.setLayoutY(633);
		mainPane.getChildren().addAll(imgClienteResizer, imgExitResizer);

		TOOLTIP_BT_SAIR.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_CONF_CLOCK.setStyle(TOOLTIP_CSS);
		TOOLTIP_LOGO_CLIENTE.setStyle(TOOLTIP_CSS);

		Tooltip.install(imgExit, TOOLTIP_BT_SAIR);
		Tooltip.install(btStyleClock, TOOLTIP_BT_CONF_CLOCK);
		Tooltip.install(imgCliente, TOOLTIP_LOGO_CLIENTE);

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
		mainContainer.loadScreen(screenInicioID, screenInicioFile);

		ProcessoStatusManager.setProcessoStatus(NOME_REATOR_1, Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR_2, Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR_3, Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR_4, Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR_5, Boolean.FALSE);
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR_6, Boolean.FALSE);

		ProcessoMapProperty.setConfigParam(NOME_REATOR_1, new ProcessoConfigParams("Reator 1"));
		ProcessoMapProperty.setConfigParam(NOME_REATOR_2, new ProcessoConfigParams("Reator 2"));
		ProcessoMapProperty.setConfigParam(NOME_REATOR_3, new ProcessoConfigParams("Reator 3"));
		ProcessoMapProperty.setConfigParam(NOME_REATOR_4, new ProcessoConfigParams("Reator 4"));
		ProcessoMapProperty.setConfigParam(NOME_REATOR_5, new ProcessoConfigParams("Reator 5"));
		ProcessoMapProperty.setConfigParam(NOME_REATOR_6, new ProcessoConfigParams("Reator 6"));

		CurrentScreenProperty.setScreen(NOME_REATOR_1);

		mainContainer.setScreen(screenInicioID);
		centralPane.getChildren().addAll(mainContainer);
		clock.setLcdDesign(LcdDesign.STANDARD_GREEN);
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.STANDARD_GREEN);

		scanModbusSlaves.play();
	}

	private void initModbusReadSlaves() {
		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (slaveID == 1) {
					tempReator = modService.readMultipleRegisters(slaveID, 0, 1);
					setPointReator = modService.readMultipleRegisters(slaveID, 1, 1);
					ProcessoValueProperty.setTempReator1(tempReator);
					ProcessoValueProperty.setSpReator1(setPointReator);
				}
				if (slaveID == 2) {
					tempReator = modService.readMultipleRegisters(slaveID, 0, 1);
					setPointReator = modService.readMultipleRegisters(slaveID, 18, 1);
					ProcessoValueProperty.setTempReator2(tempReator);
					ProcessoValueProperty.setSpReator2(setPointReator);
				}
				slaveID++;
				if (slaveID == 3)
					slaveID = 1;
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);

	}

	@FXML
	private void openInicio() {
//		CurrentScreenProperty.setScreen(NOME_REATOR_1);
		mainContainer.setScreen(screenInicioID);
	}
	
	@FXML
	private void openReator1() {
		CurrentScreenProperty.setScreen(NOME_REATOR_1);
		mainContainer.setScreen(screen1ID);
	}

	@FXML
	private void openReator2() {
		CurrentScreenProperty.setScreen(NOME_REATOR_2);
		mainContainer.setScreen(screen2ID);
	}

	@FXML
	private void openReator3() {
		CurrentScreenProperty.setScreen(NOME_REATOR_3);
		mainContainer.setScreen(screen3ID);
	}

	@FXML
	private void openReator4() {
		CurrentScreenProperty.setScreen(NOME_REATOR_4);
		mainContainer.setScreen(screen4ID);
	}

	@FXML
	private void openReator5() {
		CurrentScreenProperty.setScreen(NOME_REATOR_5);
		mainContainer.setScreen(screen5ID);
	}

	@FXML
	private void openReator6() {
		CurrentScreenProperty.setScreen(NOME_REATOR_6);
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
		alert.setTitle(ALERT_EXIT_TITLE);
		alert.setHeaderText(ALERT_EXIT);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			scanModbusSlaves.stop();
			modService.closeConnection();
			Platform.exit();
		}
	}

	@FXML
	public void openStyleOptions() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource(WINDOW_CONFIG_CLOCK_PATH));
		stage.setScene(new Scene(root));
		stage.setTitle(WINDOW_CONFIG_CLOCK_TITLE);
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
		root = FXMLLoader.load(getClass().getResource(WINDOW_CLIENTE_INFO_PATH));
		stage.setScene(new Scene(root));
		stage.setTitle(WINDOW_CLIENTE_INFO_TITLE);
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