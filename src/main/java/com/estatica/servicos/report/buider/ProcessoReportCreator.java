package com.estatica.servicos.report.buider;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.util.Date;

import com.estatica.servicos.model.Produto;
import com.estatica.servicos.report.template.ProcessoPdfTemplate;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.exception.DRException;

public class ProcessoReportCreator {

	public static int build(Produto produto, String path, String periodo, String producao) {
		
		TextColumnBuilder<Date> xColumn = col.column("Horário", "dtProcesso", type.timeHourToSecondType());
		TextColumnBuilder<Double> y1Column = col.column("Temperatura (ºC)", "tempReator", type.doubleType());
		TextColumnBuilder<Double> y2Column = col.column("Set-pont (ºC)", "spReator", type.doubleType());

		try {
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path);
			report()
			.setTemplate(ProcessoPdfTemplate.reportTemplate)
			.title(ProcessoPdfTemplate.createHeaderComponent(produto), 
					ProcessoPdfTemplate.createSeparatorComponent(),
					ProcessoPdfTemplate.createDadosComponent(produto, periodo, producao),
					ProcessoPdfTemplate.createSeparatorComponent(),
					ProcessoPdfTemplate.createChartComponent(produto),
					ProcessoPdfTemplate.createSeparatorComponent())
			.setDataSource(produto.getProcessos())
			.columns(xColumn, y1Column, y2Column)
			.summary(ProcessoPdfTemplate.createEmissaoComponent())
			.pageFooter(ProcessoPdfTemplate.footerComponent)
			.toPdf(pdfExporter);
			return 1;
		} catch (DRException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
