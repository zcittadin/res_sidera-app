package com.estatica.servicos.controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.estatica.servicos.objectproperties.MarkLineChartProperty;
import com.estatica.servicos.util.HoverDataChart;
import com.estatica.servicos.view.ControlledScreen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
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

	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();
	private static Double tempReator = new Double(0);
	private static Double setPointReator = new Double(0);
	private static Double tempMax = new Double(300);
	private static Double tempMin = new Double(0);
	private static Double producao = new Double(0);

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
	
	private void plotTemp() {
		final XYChart.Data<String, Number> data = new XYChart.Data<>(horasFormatter.format(LocalDateTime.now()),
				tempReator);
		Node mark = new HoverDataChart(1, tempReator);
		if (!MarkLineChartProperty.getMark())
			mark.setVisible(Boolean.FALSE);
		valueMarks.add(mark);
		data.setNode(mark);
		tempSeries.getData().add(data);
	}

	@FXML
	private void findByLote() {

	}

}
