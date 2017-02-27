package com.estatica.servicos.controllers;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.estatica.servicos.custom.Toast;
import com.estatica.servicos.model.Processo;
import com.estatica.servicos.model.Produto;
import com.estatica.servicos.report.buider.ProcessoReportCreator;
import com.estatica.servicos.service.ConsultaMailService;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

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
	private ProgressIndicator progReport;
	@FXML
	private Button btConsultar;
	@FXML
	private Button btReport;
	@FXML
	private Button btXls;
	@FXML
	private Button btClear;
	@FXML
	private CheckBox checkMail;

	private static Tooltip TOOLTIP_BT_CLEAR = new Tooltip("Limpar consulta");
	private static Tooltip TOOLTIP_BT_PDF = new Tooltip("Exportar relatório em PDF");
	private static Tooltip TOOLTIP_BT_XLS = new Tooltip("Exportar planilha de dados em XLS");
	private static Tooltip TOOLTIP_BT_SEARCH = new Tooltip("Pesquisar lote de produção");
	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; "
			+ "-fx-background-color: #2F4F4F; -fx-border-color: white; -fx-border-radius: 10px;";

	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static SimpleDateFormat horasSdf = new SimpleDateFormat("hh:mm:ss");
	private static SimpleDateFormat dataHoraSdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();
	// private static Double tempReator = new Double(0);
	// private static Double setPointReator = new Double(0);
	// private static Double tempMax = new Double(300);
	// private static Double tempMin = new Double(0);
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

		TOOLTIP_BT_CLEAR.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_PDF.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_SEARCH.setStyle(TOOLTIP_CSS);
		TOOLTIP_BT_XLS.setStyle(TOOLTIP_CSS);
		Tooltip.install(btClear, TOOLTIP_BT_CLEAR);
		Tooltip.install(btConsultar, TOOLTIP_BT_SEARCH);
		Tooltip.install(btReport, TOOLTIP_BT_PDF);
		Tooltip.install(btXls, TOOLTIP_BT_XLS);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtLote.requestFocus();

			}
		});
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
							btReport.setDisable(Boolean.TRUE);
							btXls.setDisable(Boolean.TRUE);
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
						txtLote.requestFocus();
					}
				});
			}
		});

		progReport.progressProperty().bind(searchTask.progressProperty());

		Thread t = new Thread(searchTask);
		t.start();
	}

	@FXML
	private void clearConsulta() {
		lblCodigo.setText("0");
		lblLote.setText("0");
		lblQuantidade.setText("000,00");
		lblProducao.setText("000,00");
		lblReator.setText("");
		lblTempMin.setText("00.0");
		lblTempMax.setText("00.0");
		lblSetPoint.setText("00.0");
		lblInicio.setText("00:00:00");
		lblEncerramento.setText("00:00:00");
		lblTempoProcesso.setText("00:00:00");
		lblOperador.setText("");
		clearLineChart();
		produto = null;
		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		txtLote.setText("");
		txtLote.requestFocus();
	}

	@FXML
	public void saveXls() {
		Stage stage = new Stage();
		stage.initOwner(btReport.getScene().getWindow());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XLS Files", "*.xls"));
		fileChooser.setTitle("Salvar planilha de processo");
		fileChooser.setInitialFileName("lote_" + produto.getLote() + ".xls");
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile != null) {
			generateXlsReport(savedFile);
		}
	}

	@SuppressWarnings("resource")
	private void generateXlsReport(File file) {
		progReport.setVisible(Boolean.TRUE);
		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		btClear.setDisable(Boolean.TRUE);
		txtLote.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		Task<Void> xlsTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				int maximum = 20;
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet firstSheet = workbook.createSheet("Aba1");
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					int line = 0;
					HSSFRow titleRow = firstSheet.createRow(line);
					line++;
					titleRow.createCell(0).setCellValue("Horário");
					titleRow.createCell(1).setCellValue("Temperatura no reator");
					titleRow.createCell(2).setCellValue("Set-point");
					for (Processo processo : produto.getProcessos()) {
						HSSFRow row = firstSheet.createRow(line);
						row.createCell(0).setCellValue(dataHoraSdf.format(processo.getDtProcesso()));
						row.createCell(1).setCellValue(processo.getTempReator());
						row.createCell(2).setCellValue(processo.getSpReator());
						line++;
					}
					workbook.write(fos);
					for (int i = 0; i < maximum; i++) {
						updateProgress(i, maximum);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao exportar arquivo");
				} finally {
					try {
						fos.flush();
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};

		xlsTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progReport.setVisible(Boolean.FALSE);
				btReport.setDisable(Boolean.FALSE);
				btXls.setDisable(Boolean.FALSE);
				btClear.setDisable(Boolean.FALSE);
				txtLote.setDisable(Boolean.FALSE);
				btConsultar.setDisable(Boolean.FALSE);
				if(checkMail.isSelected())
					sendMailReport(file);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Concluído");
				alert.setHeaderText("Planilha de dados emitida com sucesso. Deseja visualizar?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		progReport.progressProperty().bind(xlsTask.progressProperty());
		Thread t = new Thread(xlsTask);
		t.start();
	}

	@FXML
	public void saveReport() {
		Stage stage = new Stage();
		stage.initOwner(btReport.getScene().getWindow());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
		fileChooser.setTitle("Salvar relatório de processo");
		fileChooser.setInitialFileName("lote_" + produto.getLote() + ".pdf");
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile != null) {
			generatePdfReport(savedFile);
		}

	}

	private void generatePdfReport(File file) {
		progReport.setVisible(Boolean.TRUE);
		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		btClear.setDisable(Boolean.TRUE);
		txtLote.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		Task<Integer> reportTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				int result = ProcessoReportCreator.build(produto, file.getAbsolutePath(), lblTempoProcesso.getText(),
						lblProducao.getText());
				int maximum = 20;
				for (int i = 0; i < maximum; i++) {
					updateProgress(i, maximum);
				}
				return new Integer(result);
			}
		};

		reportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progReport.setVisible(Boolean.FALSE);
				btReport.setDisable(Boolean.FALSE);
				btXls.setDisable(Boolean.FALSE);
				btClear.setDisable(Boolean.FALSE);
				txtLote.setDisable(Boolean.FALSE);
				btConsultar.setDisable(Boolean.FALSE);
				int r = reportTask.getValue();
				if (r != 1) {
					Toolkit.getDefaultToolkit().beep();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erro");
					alert.setHeaderText("Houve uma falha na emissão do relatório.");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Concluído");
				alert.setHeaderText("Relatório emitido com sucesso. Deseja visualizar?");
				if (checkMail.isSelected())
					sendMailReport(file);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		progReport.progressProperty().bind(reportTask.progressProperty());

		Thread t = new Thread(reportTask);
		t.start();
	}

	private void sendMailReport(File file) {
		Task<Void> mailTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				ConsultaMailService mailService = new ConsultaMailService();
				mailService.sendMailReport(produto, file);
				return null;
			}
		};

		mailTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				String toastMsg = "E-mail enviado com sucesso.";
				int toastMsgTime = 5000;
				int fadeInTime = 600;
				int fadeOutTime = 600;
				Stage stage = (Stage) btConsultar.getScene().getWindow();
				Toast.makeToast(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
			}
		});
		Thread t = new Thread(mailTask);
		t.start();
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

	private void populateFields() {
		lblCodigo.setText(String.valueOf(produto.getCodigo()));
		lblLote.setText(String.valueOf(produto.getLote()));
		lblQuantidade.setText(String.valueOf(produto.getQuantidade()));
		lblReator.setText(produto.getNomeReator());
		lblTempMin.setText(String.valueOf(produto.getTempMin()));
		lblTempMax.setText(String.valueOf(produto.getTempMax()));
		lblSetPoint.setText(String.valueOf(produto.getProcessos().get(0).getSpReator()));
		lblInicio.setText(horasSdf.format(produto.getDtInicial()));
		lblEncerramento.setText(horasSdf.format(produto.getDtFinal()));
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
		progReport.setVisible(Boolean.TRUE);
		txtLote.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		btReport.setDisable(Boolean.TRUE);
		btClear.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
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
		progReport.setVisible(Boolean.FALSE);
		txtLote.setDisable(Boolean.FALSE);
		btConsultar.setDisable(Boolean.FALSE);
		btReport.setDisable(Boolean.FALSE);
		btClear.setDisable(Boolean.FALSE);
		btXls.setDisable(Boolean.FALSE);
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
