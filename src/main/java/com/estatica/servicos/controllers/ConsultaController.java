package com.estatica.servicos.controllers;

import java.awt.Toolkit;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.estatica.servicos.model.Processo;
import com.estatica.servicos.model.Produto;
import com.estatica.servicos.service.ProdutoDBService;
import com.estatica.servicos.service.impl.ProdutoDBServiceImpl;
import com.estatica.servicos.view.ControlledScreen;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class ConsultaController implements Initializable, ControlledScreen {

	@FXML
	private TextField txtLote;
	@FXML
	private Label lblCodigo;
	@FXML
	private Label lblLote;
	@FXML
	private Label lblQuantidade;
	@FXML
	private Label lblProducao;
	@FXML
	private Label lblReator;
	@FXML
	private Label lblTempMin;
	@FXML
	private Label lblTempMax;
	@FXML
	private Label lblSetPoint;
	@FXML
	private Label lblInicio;
	@FXML
	private Label lblEncerramento;
	@FXML
	private Label lblTempoProcesso;
	@FXML
	private Label lblOperador;
	@FXML
	private LineChart<String, Number> chartConsulta;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private ProgressIndicator progA;
	@FXML
	private ProgressIndicator progB;
	@FXML
	private ProgressIndicator progC;
	@FXML
	private ProgressIndicator progD;
	@FXML
	private Button btConsultar;

	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();
	private static Double tempReator = new Double(0);
	private static Double setPointReator = new Double(0);
	private static Double tempMax = new Double(300);
	private static Double tempMin = new Double(0);
	private static Double producao = new Double(0);
	private static Produto produto;

	private static ProdutoDBService produtoService = new ProdutoDBServiceImpl();
	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configLineChart();
	}

	private void configLineChart() {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(70);
		yAxis.setTickUnit(10);
		tempSeries = new XYChart.Series<String, Number>();
		tempSeries.getData().add(new XYChart.Data<>(horasFormatter.format(LocalDateTime.now()), 20));
		plotValuesList.add(tempSeries);
		chartConsulta.setData(plotValuesList);
	}

	private void clearLineChart() {
		tempSeries.getData().clear();
		chartConsulta.getData().clear();
		tempSeries = new XYChart.Series<String, Number>();
		chartConsulta.getData().add(tempSeries);
	}

	private void populateLineChart() {
		clearLineChart();
		for (Processo processo : produto.getProcessos()) {
			LocalDateTime horario = processo.getDtProcesso().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			XYChart.Data<String, Number> data = new XYChart.Data<>(horasFormatter.format(horario),
					processo.getTempReator());
			tempSeries.getData().add(data);
		}
		// Node mark = new HoverDataChart(1, tempReator);
		// if (!MarkLineChartProperty.getMark())
		// mark.setVisible(Boolean.FALSE);
		// valueMarks.add(mark);
		// data.setNode(mark);
	}

	@FXML
	private void findByLote() {
		if ("".equals(txtLote.getText().trim())) {
			Toolkit.getDefaultToolkit().beep();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe o lote antes da consulta.");
			alert.showAndWait();
			txtLote.requestFocus();
			return;
		}

		initFetch();

		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				produto = produtoService.findByLote(Integer.parseInt(txtLote.getText()));
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				if (produto == null) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endFetch();
							Toolkit.getDefaultToolkit().beep();
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Atenção");
							alert.setHeaderText("Lote não encontrado.");
							alert.showAndWait();
							txtLote.requestFocus();
						}
					});
					return;
				}
				if (produto.getProcessos() == null) {
					System.out.println("Lista nula");
					endFetch();
					return;
				}
				if (produto.getProcessos().isEmpty()) {
					System.out.println("Lista vazia");
					endFetch();
					return;
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						endFetch();
						populateLineChart();
						populateFields();
					}
				});
			}
		});
		Thread t = new Thread(searchTask);
		t.start();

	}

	private void populateFields() {
		lblCodigo.setText(String.valueOf(produto.getCodigo()));
		lblLote.setText(String.valueOf(produto.getLote()));
		lblQuantidade.setText(String.valueOf(produto.getQuantidade()));
		lblReator.setText(produto.getNomeReator());
		lblTempMin.setText(String.valueOf(produto.getTempMin()));
		lblTempMax.setText(String.valueOf(produto.getTempMax()));
		lblSetPoint.setText(String.valueOf(produto.getProcessos().get(0).getSpReator()));
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		lblInicio.setText(sdf.format(produto.getDtInicial()));
		lblEncerramento.setText(sdf.format(produto.getDtFinal()));
		lblTempoProcesso.setText(formatPeriod(produto.getDtInicial(), produto.getDtFinal()));
		lblOperador.setText(produto.getOperador());
		calculaProducao();
	}

	private String formatPeriod(Date ini, Date fim) {
		long periodoMillis = produto.getDtFinal().getTime() - produto.getDtInicial().getTime();
		Duration duration = Duration.ofMillis(periodoMillis);
		long hours = duration.toHours();
		int minutes = (int) ((duration.getSeconds() % (60 * 60)) / 60);
		int seconds = (int) (duration.getSeconds() % 60);
		return (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":"
				+ (seconds < 10 ? "0" + seconds : seconds);
	}

	private void calculaProducao() {
		String[] fields = lblTempoProcesso.getText().split(":");
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
		} else {
			lblProducao.setText("000,00");
		}
	}

	private void initFetch() {
		progA.setVisible(Boolean.TRUE);
		progB.setVisible(Boolean.TRUE);
		progC.setVisible(Boolean.TRUE);
		progD.setVisible(Boolean.TRUE);
		txtLote.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		lblCodigo.setDisable(Boolean.TRUE);
		lblEncerramento.setDisable(Boolean.TRUE);
		lblInicio.setDisable(Boolean.TRUE);
		lblLote.setDisable(Boolean.TRUE);
		lblOperador.setDisable(Boolean.TRUE);
		lblProducao.setDisable(Boolean.TRUE);
		lblReator.setDisable(Boolean.TRUE);
		lblQuantidade.setDisable(Boolean.TRUE);
		lblSetPoint.setDisable(Boolean.TRUE);
		lblTempMax.setDisable(Boolean.TRUE);
		lblTempMin.setDisable(Boolean.TRUE);
		lblTempoProcesso.setDisable(Boolean.TRUE);
		chartConsulta.setDisable(Boolean.TRUE);
		xAxis.setDisable(Boolean.TRUE);
		yAxis.setDisable(Boolean.TRUE);

	}

	private void endFetch() {
		progA.setVisible(Boolean.FALSE);
		progB.setVisible(Boolean.FALSE);
		progC.setVisible(Boolean.FALSE);
		progD.setVisible(Boolean.FALSE);
		txtLote.setDisable(Boolean.FALSE);
		btConsultar.setDisable(Boolean.FALSE);
		lblCodigo.setDisable(Boolean.FALSE);
		lblEncerramento.setDisable(Boolean.FALSE);
		lblInicio.setDisable(Boolean.FALSE);
		lblLote.setDisable(Boolean.FALSE);
		lblOperador.setDisable(Boolean.FALSE);
		lblProducao.setDisable(Boolean.FALSE);
		lblReator.setDisable(Boolean.FALSE);
		lblQuantidade.setDisable(Boolean.FALSE);
		lblSetPoint.setDisable(Boolean.FALSE);
		lblTempMax.setDisable(Boolean.FALSE);
		lblTempMin.setDisable(Boolean.FALSE);
		lblTempoProcesso.setDisable(Boolean.FALSE);
		chartConsulta.setDisable(Boolean.FALSE);
		xAxis.setDisable(Boolean.FALSE);
		yAxis.setDisable(Boolean.FALSE);
	}

}
