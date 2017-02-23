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
import com.estatica.servicos.util.ChronoMeter;
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

	private static ModbusRTUService modService;
	private static Timeline tempChartAnimation;
	private static Timeline scanModbusSlaves;
	private static Timeline btNovoTimeLine;
	private static Timeline dadosParciaisTimeLine;
	private static FadeTransition statusTransition;
	private static FadeTransition estaticaFadeTransition;
	private static ImageViewResizer imgResizer;
	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static Integer tempReator = 0;
	private static Integer setPointReator = 0;
	private static Integer tempMax = 300;
	private static Integer tempMin = 0;
	private static Double producao = new Double(0);
	private static Boolean isReady = Boolean.FALSE;
	private static Boolean isRunning = Boolean.FALSE;
	private static ProcessoDBService processoService = new ProcessoDBServiceImpl();
	private static ProdutoDBService produtoService = new ProdutoDBServiceImpl();
	private static Processo processo;

	final Color startColor = Color.web("#DCDCDC");
	final Color endColor = Color.web("#4B0082");
	final ObjectProperty<Color> color = new SimpleObjectProperty<Color>(startColor);
	final DecimalFormat df = new DecimalFormat("####0.00");
	final ChronoMeter chronoMeter = new ChronoMeter();

	final ObservableList<XYChart.Series<String, Number>> plotList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		modService = new ModbusRTUService();
		initAnimations();
		initLineChart();
		lblStatus.setTextFill(Color.web("#ffe700"));
		lblStatus.setText("Sem lote");
		statusTransition.play();
		Tooltip.install(imgSwitch, new Tooltip("Para iniciar o proceso é necessário configurar um lote de produção."));
		Tooltip.install(btNovo, new Tooltip("Configurar novo lote de produção"));
		Tooltip.install(btEdit, new Tooltip("Editar lote configurado"));
		Tooltip.install(btCancela, new Tooltip("Cancelar lote configurado"));
		Tooltip.install(btReport, new Tooltip("Emitir relatorio de processo"));
		Tooltip.install(btConfigLineChart, new Tooltip("Opções de visualização do gráfico de linhas"));
		Tooltip.install(imgEstatica, new Tooltip("Estática Serviços e Manutenção Industrial"));

		imgEstatica.setImage(new Image("/img/logotipo.png"));
		imgResizer = new ImageViewResizer(imgEstatica, 138, 42);
		imgResizer.setLayoutX(150.0);
		imgResizer.setLayoutY(150.0);
		imgResizer.setLayoutX(1083);
		imgResizer.setLayoutY(607);
		mainPane.getChildren().addAll(imgResizer);

		btNovo.styleProperty().bind(Bindings.createStringBinding(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return String.format("-fx-body-color: rgb(%d, %d, %d);", (int) (256 * color.get().getRed()),
						(int) (256 * color.get().getGreen()), (int) (256 * color.get().getBlue()));
			}
		}, color));

		MarkLineChartProperty.markProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				for (Node mark : valueMarks) {
					mark.setVisible(newValue);
				}
			}
		});

		modService.setConnectionParams("COM10", 9600);
		modService.openConnection();
		scanModbusSlaves.play();
	}

	@FXML
	public void toggleProcess() {
		if (isReady && !isRunning) {
			initProcess();
		} else if (!isReady && !isRunning) {
			imgSwitch.setCursor(Cursor.OPEN_HAND);
			return;
		} else if (!isReady && isRunning) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Encerramento");
			alert.setHeaderText("Deseja realmente finalizar o processo em andamento?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				finalizeProcess();
			}
		}
		imgSwitch.setCursor(Cursor.OPEN_HAND);
	}

	private void initProcess() {
		plotTemp();
		lblStatus.setTextFill(Color.web("#1654ff"));
		lblStatus.setText("Em andamento");
		imgSwitch.setImage(new Image("/com/estatica/servicos/view/img/switch_on.png"));
		Tooltip.install(imgSwitch, new Tooltip("Clique para finalizar o processo em andamento."));
		lblHorario.setText(horasFormatter.format(LocalDateTime.now()));
		isReady = Boolean.FALSE;
		isRunning = Boolean.TRUE;
		ProcessoStatusManager.setProcessoStatus("REATOR1", isRunning);
		tempChartAnimation.play();
		dadosParciaisTimeLine.play();
		chronoMeter.start(lblCronometro);

		produtoService.updateDataInicial(Integer.parseInt(lblLote.getText()));
	}

	private void finalizeProcess() {
//		produtoService.updateDataFinal(Integer.parseInt(lblLote.getText()));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblStatus.setTextFill(Color.web("#00ff4a"));
				lblStatus.setText("Finalizado");
			}
		});
		lblStatus.setOpacity(1);
		tempChartAnimation.stop();
		dadosParciaisTimeLine.stop();
		isRunning = Boolean.FALSE;
		imgSwitch.setImage(new Image("/com/estatica/servicos/view/img/switch_off.png"));
		Tooltip.install(imgSwitch, new Tooltip("Para iniciar o proceso é necessário configurar um lote de produção."));
		btNovo.setDisable(Boolean.FALSE);
		isRunning = Boolean.FALSE;
		ProcessoStatusManager.setProcessoStatus("REATOR1", isRunning);
		chronoMeter.stop();
		makeToast("Lote finalizado com sucesso.");
	}

	@FXML
	public void configureProcesso() {
		lblStatus.setTextFill(Color.web("#ffe700"));
		lblStatus.setText("Sem lote");
		clearFields();
		enableFields();
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

	private void initLineChart() {
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
		plotList.add(tempSeries);
		chartReator.setData(plotList);

	}

	private void initAnimations() {

		statusTransition = new FadeTransition(Duration.millis(1000), lblStatus);
		statusTransition.setFromValue(0.0);
		statusTransition.setToValue(1.0);
		statusTransition.setCycleCount(Timeline.INDEFINITE);
		statusTransition.setAutoReverse(Boolean.TRUE);

		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);

		btNovoTimeLine = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
				new KeyFrame(Duration.millis(500), new KeyValue(color, endColor)));
		btNovoTimeLine.setCycleCount(6);
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

		dadosParciaisTimeLine = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				calculaProducao();
				if (tempMin > tempReator) {
					tempMin = tempReator;
					lblTempMin.setText(tempMin.toString());
				}
				if (tempMax < tempReator) {
					tempMax = tempReator;
					lblTempMax.setText(tempMax.toString());
				}
			}
		}));
		dadosParciaisTimeLine.setCycleCount(Timeline.INDEFINITE);
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

	private void enableFields() {
		lblHorario.setDisable(Boolean.FALSE);
		lblOperador.setDisable(Boolean.FALSE);
		lblProduto.setDisable(Boolean.FALSE);
		lblQuantidade.setDisable(Boolean.FALSE);
	}

	private void clearFields() {
		lblHorario.setText("");
		lblOperador.setText("");
		lblProduto.setText("");
		lblQuantidade.setText("");
		lblProduto.requestFocus();
	}

	private void clearLineChart() {
		tempSeries.getData().clear();
		chartReator.getData().clear();
		tempSeries = new XYChart.Series<String, Number>();
		chartReator.getData().add(tempSeries);
	}

	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException {
		Stage stage;
		Parent root;
		if (event.getSource() == btNovo) {
			stage = new Stage();
			root = FXMLLoader.load(getClass().getResource("/com/estatica/servicos/view/ConfigProcesso.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("Novo lote de produção");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(btNovo.getScene().getWindow());
			stage.setHeight(255);
			stage.setWidth(320);
			stage.setResizable(Boolean.FALSE);
			stage.showAndWait();

			if (Reator1DTO.getConfirmation() != null) {
				if (Reator1DTO.getConfirmation()) {
					lblProduto.setText(Reator1DTO.getCodProduto());
					lblHorario.setText("00:00:00");
					lblCronometro.setText("00:00:00");
					lblProducao.setText("000,00");
					lblTempMin.setText("000");
					lblTempMax.setText("000");
					tempMax = 0;
					tempMin = 300;
					lblQuantidade.setText(Reator1DTO.getQuantidade());
					lblOperador.setText(Reator1DTO.getOperador());

					lblStatus.setTextFill(Color.web("#00ff4a"));
					lblStatus.setText("Em espera");
					lblLote.setText(Reator1DTO.getLote());
					btNovo.setDisable(Boolean.TRUE);
					Tooltip.install(imgSwitch, new Tooltip("Clique para iniciar o registro do lote configurado."));
					clearLineChart();
					isReady = Boolean.TRUE;
					Reator1DTO.setConfirmation(Boolean.FALSE);
					makeToast("Lote configurado com sucesso.");
				}
			}

		}
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

	@FXML
	private void openConfigLineChart() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/estatica/servicos/view/ConfigLineChart.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Opções do gráfico de linhas");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	private void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) btNovo.getScene().getWindow();
		Toast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

}
