package com.estatica.servicos.report.buider;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.util.Date;

import com.estatica.servicos.model.Produto;
import com.estatica.servicos.report.template.ProcessoTemplate;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.constant.TimePeriod;
import net.sf.dynamicreports.report.exception.DRException;

public class ProcessoReportCreator {

	// private static TermoLoggerService service = new TermoLoggerServiceImpl();

	public static int build(Produto produto, String path) {
		FontBuilder boldFont = stl.fontArialBold().setFontSize(12);

		TextColumnBuilder<Date> xColumn = col.column("Horário", "dtProcesso", type.dateYearToSecondType());
		TextColumnBuilder<Double> y1Column = col.column("Temperatura(ºC)", "tempReator", type.doubleType());

		try {
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path);
//			JasperPdfExporterBuilder pdfExporter = export.pdfExporter("c:/Users/Zander/lote_" + produto.getLote() + ".pdf");
			report().setTemplate(ProcessoTemplate.reportTemplate).columns(xColumn, y1Column)
					.title(ProcessoTemplate.createTitleComponent("Nº do lote: " + produto.getLote()))
					.summary(cht.timeSeriesChart().setTitle("Histórico de acompanhamento").setTitleFont(boldFont)
							.setShowShapes(Boolean.FALSE).setShowLegend(Boolean.FALSE).setTimePeriod(xColumn)
							.setTimePeriodType(TimePeriod.SECOND).series(cht.serie(y1Column))
							.setTimeAxisFormat(cht.axisFormat().setLabel("Horário"))
							.setValueAxisFormat(cht.axisFormat().setLabel("Temperatura(ºC)")))
					.pageFooter(ProcessoTemplate.footerComponent).setDataSource(produto.getProcessos())
					.toPdf(pdfExporter);
			// .show();
			return 1;
		} catch (DRException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
