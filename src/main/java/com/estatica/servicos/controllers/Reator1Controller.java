package com.estatica.servicos.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import com.estatica.servicos.custom.Toast;
import com.estatica.servicos.dto.Reator1DTO;
import com.estatica.servicos.modbus.ModbusRTUService;
import com.estatica.servicos.model.Processo;
import com.estatica.servicos.objectproperties.MarkLineChartProperty;
import com.estatica.servicos.service.ProcessoDBService;
import com.estatica.servicos.service.ProcessoStatusManager;
import com.estatica.servicos.service.ProdutoDBService;
import com.estatica.servicos.service.impl.ProcessoDBServiceImpl;
import com.estatica.servicos.service.impl.ProdutoDBServiceImpl;
import com.estatica.servicos.util.Chronometer;
import com.estatica.servicos.util.HoverDataChart;
import com.estatica.servicos.view.ControlledScreen;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class Reator1Controller implements Initializable, ControlledScreen {

	private static Tooltip TOOLTIP_SWITCH_ANDAMENTO = new Tooltip("Clique para finalizar o processo em andamento.");
	private static Tooltip TOOLTIP_SWITCH_ESPERA = new Tooltip("Clique para iniciar o registro do lote configurado.");
	private static Tooltip TOOLTIP_SWITCH_FINALIZADO = new Tooltip(
			"Para iniciar o proceso é necessário configurar um lote de produção");
	private static Tooltip TOOLTIP_BT_NOVO = new Tooltip("Configurar novo lote de produção");
	private static Tooltip TOOLTIP_BT_EDIT = new Tooltip("Editar lote configurado");
	private static Tooltip TOOLTIP_BT_CANCELAR = new Tooltip("Cancelar lote configurado");
	private static Tooltip TOOLTIP_BT_REPORT = new Tooltip("Emitir relatorio de processo");
	private static Tooltip TOOLTIP_BT_CONF_CHART = new Tooltip("Opções de visualização do gráfico de linhas");
	private static Tooltip TOOLTIP_IMG_ESTATICA = new Tooltip("Estática Serviços e Manutenção Industrial");
	private static Tooltip TOOLTIP_LBL_TEMP_REATOR = new Tooltip("Temperatura atual no reator");
	private static Tooltip TOOLTIP_LBL_TEMP_CALDEIRA = new Tooltip("Temperatura atual na caldeira");
	private static Tooltip TOOLTIP_LBL_SP_REATOR = new Tooltip("Set-point programado no reator");
	private static Tooltip TOOLTIP_LBL_SP_CALDEIRA = new Tooltip("Set-point programado na caldeira");
	private static Tooltip TOOLTIP_LBL_STATUS = new Tooltip("Estado do processo");
	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; "
			+ "-fx-background-color: #2F4F4F; -fx-border-color: white; -fx-border-radius: 10px;";
	private static String LOGO_ESTATICA_PATH = "/img/logotipo.png";
	private static String WINDOW_ESTATICA_PATH = "/com/estatica/servicos/view/EstaticaInfo.fxml";
	private static String WINDOW_ESTATICA_TITLE = "Informações sobre o fabricante";
	private static String WINDOW_CONFIG_CHART_PATH = "/com/estatica/servicos/view/ConfigLineChart.fxml";
	private static String WINDOW_CONFIG_CHART_TITLE = "Opções do gráfico de linhas";
	private static String WINDOW_CONFIG_PROCESSO_PATH = "/com/estatica/servicos/view/ConfigProcesso.fxml";
	private static String WINDOW_CONFIG_PROCESSO_TITLE = "Novo lote de produção";
	private static String IMG_SWITCH_ON_PATH = "/com/estatica/servicos/view/img/switch_on.png";
	private static String IMG_SWITCH_OFF_PATH = "/com/estatica/servicos/view/img/switch_off.png";
	private static String ALERT_FINALIZAR_PROCESSO = "Deseja realmente finalizar o processo em andamento?";
	private static String ALERT_FINALIZAR_PROCESSO_TITLE = "Encerramento";
	private static String LBL_STATUS_ANDAMENTO = "Em andamento";
	private static String LBL_STATUS_ANDAMENTO_COLOR = "#1654ff";
	private static String LBL_STATUS_FINALIZADO = "Finalizado";
	private static String LBL_STATUS_FINALIZADO_COLOR = "#00ff4a";
	private static String LBL_STATUS_SEM_LOTE = "Sem lote";
	private static String LBL_STATUS_SEM_LOTE_COLOR = "#ffe700";
	private static String LBL_STATUS_ESPERA = "Em espera";
	private static String LBL_STATUS_ESPERA_COLOR = "#00ff4a";
	private static String FORMAT_HOUR = "00:00:00";
	private static String FORMAT_DECIMAL = "000,00";
	private static String FORMAT_INTEGER = "000";
	private static String TOASTER_CONF_SUCESSO = "Lote configurado com sucesso.";
	private static String TOASTER_FINALIZADO_SUCESSO = "Lote finalizado com sucesso.";
	private static String NOME_REATOR = "REATOR1";

	private static ModbusRTUService modService;
	private static ProcessoDBService processoService = new ProcessoDBServiceImpl();
	private static ProdutoDBService produtoService = new ProdutoDBServiceImpl();
	private static FadeTransition statusTransition;
	private static FadeTransition estaticaFadeTransition;
	private static Timeline tempChartAnimation;
	private static Timeline scanModbusSlaves;
	private static Timeline btNovoTimeLine;
	private static Timeline dadosParciaisTimeLine;
	private static ImageViewResizer imgResizer;
	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static Processo processo;
	private static Integer tempReator = 0;
	private static Integer setPointReator = 0;
	private static Integer tempMax = 300;
	private static Integer tempMin = 0;
	private static Double producao = new Double(0);
	private static Boolean isReady = Boolean.FALSE;
	private static Boolean isRunning = Boolean.FALSE;

	final Color btNovoStartColor = Color.web("#DCDCDC");
	final Color btNovoEndColor = Color.web("#4B0082");
	final ObjectProperty<Color> btNovoColor = new SimpleObjectProperty<Color>(btNovoStartColor);
	final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
	final Chronometer chronoMeter = new Chronometer();
	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();

	@FXML
	private AnchorPane mainPane;
	@FXML
	private LineChart<String, Number> chartReator;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxisTemp;
	@FXML
	private Label lblPrincipal;
	@FXML
	private Label lblTempReator;
	@FXML
	private Label lblSpReator;
	@FXML
	private Label lblTempCaldeira;
	@FXML
	private Label lblSpCaldeira;
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblProducao;
	@FXML
	private Label lblTempMin;
	@FXML
	private Label lblTempMax;
	@FXML
	private ImageView imgEstatica;
	@FXML
	private ImageView imgSwitch;
	@FXML
	private Label lblProduto;
	@FXML
	private Label lblHorario;
	@FXML
	private Label lblQuantidade;
	@FXML
	private Label lblOperador;
	@FXML
	private Label lblLote;
	@FXML
	private Label lblCronometro;
	@FXML
	private Button btNovo;
	@FXML
	private Button btEdit;
	@FXML
	private Button btCancela;
	@FXML
	private Button btReport;
	@FXML
	private Button btConfigLineChart;
	@FXML
	private ProgressIndicator progLote;

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		modService = new ModbusRTUService();
		initComponents();
		configAnimations();
		configLineChart();
		initModbusSlave();
		statusTransition.play();
		scanModbusSlaves.play();
	}

	// ===============================================
	// CHAMADAS DA INTERFACE GRÁFICA (@FXML)
	// ===============================================

	@FXML
	public void toggleProcess() {
		if (isReady && !isRunning) {
			initProcess();
		} else if (!isReady && !isRunning) {
			imgSwitch.setCursor(Cursor.OPEN_HAND);
			return;
		} else if (!isReady && isRunning) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(ALERT_FINALIZAR_PROCESSO_TITLE);
			alert.setHeaderText(ALERT_FINALIZAR_PROCESSO);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				finalizeProcess();
			}
		}
		imgSwitch.setCursor(Cursor.OPEN_HAND);
	}

	@FXML
	public void configureProcesso() {
		lblStatus.setTextFill(Color.web(LBL_STATUS_SEM_LOTE_COLOR));
		lblStatus.setText(LBL_STATUS_SEM_LOTE);
		lblProduto.requestFocus();
		clearLineChart();
	}

	@FXML
	public void switchIsPressing() {
		imgSwitch.setCursor(Cursor.CLOSED_HAND);
	}

	@FXML
	public void hoverBtNovo() {
		if (!isReady && !isRunning)
			btNovoTimeLine.play();
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
	private void handleButtonAction(ActionEvent event) throws IOException {
		Stage stage;
		Parent root;
		if (event.getSource() == btNovo) {
			stage = new Stage();
			root = FXMLLoader.load(getClass().getResource(WINDOW_CONFIG_PROCESSO_PATH));
			stage.setScene(new Scene(root));
			stage.setTitle(WINDOW_CONFIG_PROCESSO_TITLE);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(btNovo.getScene().getWindow());
			stage.setHeight(255);
			stage.setWidth(320);
			stage.setResizable(Boolean.FALSE);
			stage.showAndWait();

			if (Reator1DTO.getConfirmation() != null) {
				if (Reator1DTO.getConfirmation()) {
					lblProduto.setText(Reator1DTO.getCodProduto());
					lblHorario.setText(FORMAT_HOUR);
					lblCronometro.setText(FORMAT_HOUR);
					lblProducao.setText(FORMAT_DECIMAL);
					lblTempMin.setText(FORMAT_INTEGER);
					lblTempMax.setText(FORMAT_INTEGER);
					tempMax = 0;
					tempMin = 300;
					lblQuantidade.setText(Reator1DTO.getQuantidade());
					lblOperador.setText(Reator1DTO.getOperador());

					lblStatus.setTextFill(Color.web(LBL_STATUS_ESPERA_COLOR));
					lblStatus.setText(LBL_STATUS_ESPERA);
					lblLote.setText(Reator1DTO.getLote());
					btNovo.setDisable(Boolean.TRUE);
					Tooltip.install(imgSwitch, TOOLTIP_SWITCH_ESPERA);
					clearLineChart();
					isReady = Boolean.TRUE;
					Reator1DTO.setConfirmation(Boolean.FALSE);
					makeToast(TOASTER_CONF_SUCESSO);
				}
			}

		}
	}

	@FXML
	private void handleImgEstaticaAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource(WINDOW_ESTATICA_PATH));
		stage.setScene(new Scene(root));
		stage.setTitle(WINDOW_ESTATICA_TITLE);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
		estaticaFadeTransition.setFromValue(imgEstatica.getOpacity());
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}

	@FXML
	private void openConfigLineChart() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource(WINDOW_CONFIG_CHART_PATH));
		stage.setScene(new Scene(root));
		stage.setTitle(WINDOW_CONFIG_CHART_TITLE);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	// ===============================================
	// MÉTODOS PRIVADOS
	// ===============================================
	private void initProcess() {
		plotTemp();
		lblStatus.setTextFill(Color.web(LBL_STATUS_ANDAMENTO_COLOR));
		lblStatus.setText(LBL_STATUS_ANDAMENTO);
		imgSwitch.setImage(new Image(IMG_SWITCH_ON_PATH));
		Tooltip.install(imgSwitch, TOOLTIP_SWITCH_ANDAMENTO);
		lblHorario.setText(horasFormatter.format(LocalDateTime.now()));
		isReady = Boolean.FALSE;
		isRunning = Boolean.TRUE;
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR, isRunning);
		tempChartAnimation.play();
		dadosParciaisTimeLine.play();
		chronoMeter.start(lblCronometro);

		produtoService.updateDataInicial(Integer.parseInt(lblLote.getText()));
	}

	private void finalizeProcess() {
		produtoService.updateDataFinal(Integer.parseInt(lblLote.getText()));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblStatus.setTextFill(Color.web(LBL_STATUS_FINALIZADO_COLOR));
				lblStatus.setText(LBL_STATUS_FINALIZADO);
			}
		});
		lblStatus.setOpacity(1);
		tempChartAnimation.stop();
		dadosParciaisTimeLine.stop();
		isRunning = Boolean.FALSE;
		imgSwitch.setImage(new Image(IMG_SWITCH_OFF_PATH));
		Tooltip.install(imgSwitch, TOOLTIP_SWITCH_FINALIZADO);
		btNovo.setDisable(Boolean.FALSE);
		isRunning = Boolean.FALSE;
		ProcessoStatusManager.setProcessoStatus(NOME_REATOR, isRunning);
		chronoMeter.stop();
		makeToast(TOASTER_FINALIZADO_SUCESSO);
	}

	private void calculaProducao() {
		String[] fields = lblCronometro.getText().split(":");
		Integer hours = Integer.parseInt(fields[0]);
		Integer minutes = Integer.parseInt(fields[1]);
		Integer passedMinutes = 0;
		if (hours > 0) {
			passedMinutes = hours * 60;
			passedMinutes = passedMinutes + minutes;
			producao = (Double.parseDouble(lblQuantidade.getText().replace(",", ".")) / passedMinutes) * 60;
			String str = String.format("%1.2f", producao);
			producao = Double.valueOf(str.replace(",", "."));
			lblProducao.setText(producao.toString().replace(".", ","));
		}
	}

	private void plotTemp() {
		final XYChart.Data<String, Number> data = new XYChart.Data<>(horasFormatter.format(LocalDateTime.now()),
				tempReator);
		Node mark = new HoverDataChart(1, tempReator);
		if (!MarkLineChartProperty.getMark())
			mark.setVisible(Boolean.FALSE);
		valueMarks.add(mark);
		data.setNode(mark);
		tempSeries.getData().add(data);
		saveTemp();
	}

	private void saveTemp() {
		processo = new Processo(null, Reator1DTO.getProduto(), Calendar.getInstance().getTime(), tempReator,
				setPointReator);
		processoService.saveProcesso(processo);
	}

	private void clearLineChart() {
		tempSeries.getData().clear();
		chartReator.getData().clear();
		tempSeries = new XYChart.Series<String, Number>();
		chartReator.getData().add(tempSeries);
	}

	private void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) btNovo.getScene().getWindow();
		Toast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

	// ===============================================
	// INICIALIZAÇÔES e CONFIGURAÇÔES
	// ===============================================
	private void initModbusSlave() {
		modService.setConnectionParams("COM10", 9600);
		modService.openConnection();
	}

	private void configAnimations() {
		statusTransition = new FadeTransition(Duration.millis(1000), lblStatus);
		statusTransition.setFromValue(0.0);
		statusTransition.setToValue(1.0);
		statusTransition.setCycleCount(Timeline.INDEFINITE);
		statusTransition.setAutoReverse(Boolean.TRUE);

		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);

		btNovoTimeLine = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(btNovoColor, btNovoStartColor)),
				new KeyFrame(Duration.millis(500), new KeyValue(btNovoColor, btNovoEndColor)));
		btNovoTimeLine.setCycleCount(4);
		btNovoTimeLine.setAutoReverse(Boolean.TRUE);

		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tempReator = modService.readMultipleRegisters(1, 0, 1);
				setPointReator = modService.readMultipleRegisters(1, 1, 1);
				lblTempReator.setText(String.valueOf(tempReator) + " ºC");
				lblSpReator.setText(String.valueOf(setPointReator) + " ºC");
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);

		dadosParciaisTimeLine = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				calculaProducao();
				if (tempMin > tempReator) {
					tempMin = tempReator;
					lblTempMin.setText(tempMin.toString());
					produtoService.updateTemperaturaMin(Integer.parseInt(lblLote.getText()), tempMin);
				}
				if (tempMax < tempReator) {
					tempMax = tempReator;
					lblTempMax.setText(tempMax.toString());
					produtoService.updateTemperaturaMax(Integer.parseInt(lblLote.getText()), tempMax);
				}
			}
		}));
		dadosParciaisTimeLine.setCycleCount(Timeline.INDEFINITE);
	}

	private void configLineChart() {
		yAxisTemp.setAutoRanging(false);
		yAxisTemp.setLowerBound(0);
		yAxisTemp.setUpperBound(70);
		yAxisTemp.setTickUnit(10);

		tempChartAnimation = new Timeline();
		tempChartAnimation.getKeyFrames()
				.add(new KeyFrame(Duration.millis(3000), (ActionEvent actionEvent) -> plotTemp()));
		tempChartAnimation.setCycleCount(Animation.INDEFINITE);

		tempSeries = new XYChart.Series<String, Number>();
		tempSeries.getData().add(new XYChart.Data<>(horasFormatter.format(LocalDateTime.now()), 20));
		plotValuesList.add(tempSeries);
		chartReator.setData(plotValuesList);

	}

	private void initComponents() {
		lblStatus.setTextFill(Color.web("#ffe700"));
		lblStatus.setText("Sem lote");
		imgEstatica.setImage(new Image(LOGO_ESTATICA_PATH));
		imgResizer = new ImageViewResizer(imgEstatica, 138, 42);
		imgResizer.setLayoutX(150.0);
		imgResizer.setLayoutY(150.0);
		imgResizer.setLayoutX(1083);
		imgResizer.setLayoutY(607);
		mainPane.getChildren().addAll(imgResizer);

		btNovo.styleProperty().bind(Bindings.createStringBinding(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return String.format("-fx-body-color: rgb(%d, %d, %d);", (int) (256 * btNovoColor.get().getRed()),
						(int) (256 * btNovoColor.get().getGreen()), (int) (256 * btNovoColor.get().getBlue()));
			}
		}, btNovoColor));

		MarkLineChartProperty.markProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				for (Node mark : valueMarks) {
					mark.setVisible(newValue);
				}
			}
		});

		TOOLTIP_SWITCH_ANDAMENTO.setStyle(TOOLTIP_CSS);
		TOOLTIP_SWITCH_ESPERA.setStyle(TOOLTIP_CSS);
		TOOLTIP_SWITCH_FINALIZADO.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_NOVO.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_EDIT.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_CANCELAR.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_REPORT.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_CONF_CHART.setStyle(TOOLTIP_CSS);
		TOOLTIP_IMG_ESTATICA.setStyle(TOOLTIP_CSS);
		TOOLTIP_LBL_TEMP_REATOR.setStyle(TOOLTIP_CSS);
		TOOLTIP_LBL_TEMP_CALDEIRA.setStyle(TOOLTIP_CSS);
		TOOLTIP_LBL_SP_REATOR.setStyle(TOOLTIP_CSS);
		TOOLTIP_LBL_SP_CALDEIRA.setStyle(TOOLTIP_CSS);
		TOOLTIP_LBL_STATUS.setStyle(TOOLTIP_CSS);

		Tooltip.install(imgSwitch, TOOLTIP_SWITCH_FINALIZADO);
		Tooltip.install(btNovo, TOOLTIP_BT_NOVO);
		Tooltip.install(btEdit, TOOLTIP_BT_EDIT);
		Tooltip.install(btCancela, TOOLTIP_BT_CANCELAR);
		Tooltip.install(btReport, TOOLTIP_BT_REPORT);
		Tooltip.install(btConfigLineChart, TOOLTIP_BT_CONF_CHART);
		Tooltip.install(imgEstatica, TOOLTIP_IMG_ESTATICA);
		Tooltip.install(lblTempReator, TOOLTIP_LBL_TEMP_REATOR);
		Tooltip.install(lblTempCaldeira, TOOLTIP_LBL_TEMP_CALDEIRA);
		Tooltip.install(lblSpReator, TOOLTIP_LBL_SP_REATOR);
		Tooltip.install(lblSpCaldeira, TOOLTIP_LBL_SP_CALDEIRA);
		Tooltip.install(lblStatus, TOOLTIP_LBL_STATUS);
	}

}
