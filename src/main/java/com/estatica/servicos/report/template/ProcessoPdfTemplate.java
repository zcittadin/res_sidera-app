package com.estatica.servicos.report.template;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import com.estatica.servicos.model.Produto;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.chart.TimeSeriesChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.TimePeriod;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

public class ProcessoPdfTemplate {

	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;

	public static final ReportTemplateBuilder reportTemplate;
	public static final ComponentBuilder<?, ?> separatorComponent;
	public static final ComponentBuilder<?, ?> footerComponent;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");
	private static SimpleDateFormat horasSdf = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat dataSdf = new SimpleDateFormat("dd/MM/YYYY");

	static {
		rootStyle = stl.style().setPadding(2);
		boldStyle = stl.style(rootStyle).bold();
		italicStyle = stl.style(rootStyle).italic();
		boldCenteredStyle = stl.style(boldStyle).setTextAlignment(HorizontalTextAlignment.CENTER,
				VerticalTextAlignment.MIDDLE);
		bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
		columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold();
		groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());
		
		StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(stl.pen1Point());
		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer().setHeadingStyle(0,
				stl.style(rootStyle).bold());
		
		reportTemplate = template().setLocale(Locale.getDefault()).setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle).setGroupStyle(groupStyle).setGroupTitleStyle(groupStyle)
				.setSubtotalStyle(subtotalStyle).highlightDetailEvenRows().crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle).setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle).setCrosstabCellStyle(crosstabCellStyle)
				.setTableOfContentsCustomizer(tableOfContentsCustomizer);
		
		separatorComponent = cmp.horizontalList(cmp.verticalList(cmp.verticalGap(10), cmp.line(), cmp.verticalGap(10)));
		footerComponent = cmp.pageXofY().setStyle(stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));
	}

	public static ComponentBuilder<?, ?> createHeaderComponent(Produto produto) {
		return cmp.horizontalList().add(cmp.horizontalList(
				cmp.image(ProcessoPdfTemplate.class.getResource("/img/resicolor.png"))
				.setFixedDimension(80, 80),
			cmp.horizontalGap(10),
			cmp.verticalList(
					cmp.text("Relatório de processo")
						.setStyle(bold18CenteredStyle)
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
						.setFixedWidth(300),
						cmp.text("Lote " + produto.getLote())
						.setStyle(boldStyle.setFontSize(12))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
						cmp.text(produto.getNomeReator())
						.setStyle(boldStyle.setFontSize(12))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
						.setFixedWidth(300)
				),
			cmp.horizontalGap(10)
		));
	}

	public static ComponentBuilder<?, ?> createChartComponent(Produto produto) {
		return cmp.horizontalList(createLineChart(produto));
	}

	public static ComponentBuilder<?, ?> createSeparatorComponent() {
		return separatorComponent;
	}

	public static ComponentBuilder<?, ?> createEmissaoComponent() {
		return cmp.horizontalList(cmp.verticalList(separatorComponent,
				cmp.text("Data de emissão: " + horasFormatter.format(LocalDateTime.now()))
				.setStyle(stl.style().setFontSize(10))
//				.setStyle(boldStyle)
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)));
	}

	public static ComponentBuilder<?, ?> createDadosComponent(Produto produto, String periodo, String producao) {
		return cmp.horizontalList(
				cmp.verticalList(
						cmp.text("Data de produção: " + dataSdf.format(produto.getDtInicial())), 
						cmp.text("Código do produto: " + produto.getCodigo()), 
						cmp.text("Quantidade: " + produto.getQuantidade() + " Kg"), 
						cmp.text("Produção média: " + producao + " Kg/h")),
				cmp.verticalList(
						cmp.text("Horário de início: " + horasSdf.format(produto.getDtInicial())), 
						cmp.text("Horário de encerramento: " + horasSdf.format(produto.getDtFinal())), 
						cmp.text("Tempo de processo: " + periodo), 
						cmp.text("Operador: " + produto.getOperador())),
				cmp.verticalList(
						cmp.text("Temperatura mínima: " + produto.getTempMin() + " ºC"), 
						cmp.text("Temperatura máxima: " + produto.getTempMax() + " ºC"),
						cmp.text("Set-point: " + produto.getProcessos().get(0).getSpReator() + " ºC"))
				);
	}
	
	private static TimeSeriesChartBuilder createLineChart(Produto produto) {
		FontBuilder boldFont = stl.fontArialBold().setFontSize(10);
		TextColumnBuilder<Date> xColumn = col.column("Horário", "dtProcesso", type.dateYearToSecondType());
		TextColumnBuilder<Double> y1Column = col.column(produto.getNomeReator(), "tempReator", type.doubleType());
		return cht.timeSeriesChart()
				.setTitle("Temperatura x tempo")
				.setTitleFont(boldFont)
				.setShowShapes(Boolean.FALSE)
				.seriesColors(Color.MAGENTA)
				.setShowLegend(Boolean.TRUE)
				.setTimePeriod(xColumn)
				.setTimePeriodType(TimePeriod.SECOND)
				.series(cht.serie(y1Column))
				.setTimeAxisFormat(cht.axisFormat())
				.setValueAxisFormat(cht.axisFormat().setLabel("ºC")
						.setRangeMaxValueExpression(70)
						.setRangeMinValueExpression(0));
	}

}
