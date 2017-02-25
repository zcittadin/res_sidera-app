package com.estatica.servicos.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.estatica.servicos.modbus.ModbusRTUService;
import com.estatica.servicos.model.Processo;
import com.estatica.servicos.service.ProcessoDBService;
import com.estatica.servicos.service.ProdutoDBService;
import com.estatica.servicos.service.impl.ProcessoDBServiceImpl;
import com.estatica.servicos.service.impl.ProdutoDBServiceImpl;
import com.estatica.servicos.util.Chronometer;
import com.estatica.servicos.view.ControlledScreen;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import zan.inc.custom.components.ImageViewResizer;

public class Reator2Controller implements Initializable, ControlledScreen {

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
	private static String TOASTER_CONF_SUCESSO = "Lote configurado com sucesso.";
	private static String TOASTER_FINALIZADO_SUCESSO = "Lote finalizado com sucesso.";
	private static String ALERT_FINALIZAR_PROCESSO = "Deseja realmente finalizar o processo em andamento?";
	private static String ALERT_FINALIZAR_PROCESSO_TITLE = "Encerramento";
	private static String NOME_REATOR = "REATOR1";
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
	private static String COM_PORT = "COM10";

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
	private static Integer baud = 9600;
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

	ScreensController myController;

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
	private Label lblCronometro;
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
	private ImageView imgEstatica;
	@FXML
	private ImageView imgSwitch;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}






















