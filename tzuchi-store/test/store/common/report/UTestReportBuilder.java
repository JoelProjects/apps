/* $Id: UTestReportBuilder.java,v 1.2 2008/03/13 20:29:18 jlin Exp $
 * Created on Jan 30, 2008
 * 
 * $Log: UTestReportBuilder.java,v $
 * Revision 1.2  2008/03/13 20:29:18  jlin
 * add simple asset report
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Test;

import store.common.report.builder.IReportBuilder;
import store.common.report.builder.SingleTableReportBuilder;
import store.common.report.model.ReportHandler;
import store.common.report.model.SearchCriteria;
import store.common.report.model.ReportColumn.DataType;
import store.common.report.model.SearchCriteria.OperatorEnum;
import store.common.report.render.IReport;
import store.common.report.render.IReport.ReportRenderEnum;
import store.datamodel.Invoice;
import static org.junit.Assert.*;

public class UTestReportBuilder
{
	private static ReportHandler handler;
	
	@BeforeClass
	public static void buildGobalTestData()
	{
		handler = new ReportHandler(Invoice.class);
		List criterias = new ArrayList();
		SearchCriteria a = new SearchCriteria("invoice.handledBy", OperatorEnum.EQUAL, "admin"); 
		criterias.add(a);
		
		handler.setCriteria(criterias);
		handler.addReportColumn("invoice.invoiceNumber", "Invoice Number", DataType.TEXT, false, false);
		handler.addReportColumn("invoice.invoiceDate", "Transaction Time", DataType.DATE, false, false);
		handler.addReportColumn("invoice.handledBy", "Handle By", DataType.TEXT, false, false);
		handler.addReportColumn("invoice.purchasedBy", "Purchase By", DataType.TEXT, false, false);
		handler.addReportColumn("invoice.total", "Total", DataType.CURRENCY, false, false);
		
		
		try
		{
			handler.validate();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void InvoiceReportBuild_buildQuery()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		assertNotNull(query);
	}
	
	@Test
	public void InvoiceReportBuild_executeQuery()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
	}
	
	@Test
	public void InvoiceReportBuild_getReport()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		assertNotNull(report);
	}
	
	@Test
	public void InvoiceReportBuild_printReport()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
	}
	
	@Test
	public void InvoiceReportBuild_beginDate() throws Exception
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2008);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		SearchCriteria a = new SearchCriteria("invoice.invoiceDate", OperatorEnum.GREATER_EQUAL,
				calendar.getTime());
		List criterias = new ArrayList();
		criterias.add(a);
		handler.setCriteria(criterias
				);
		
		handler.validate();
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
	}
	
	@Test
	public void InvoiceReportBuild_beginDateNEndDate() throws Exception
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2008);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		SearchCriteria a = new SearchCriteria("invoice.invoiceDate", OperatorEnum.GREATER_EQUAL,
				calendar.getTime());
		calendar.set(Calendar.MONTH, 1);
		SearchCriteria b = new SearchCriteria("invoice.invoiceDate", OperatorEnum.LESS,
				calendar.getTime());
		List criterias = new ArrayList();
		criterias.add(a);
		criterias.add(b);
		handler.setCriteria(criterias
				);
		handler.validate();
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
	}
	
	@Test
	public void InvoiceReportBuild_endDate()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2008);
		calendar.set(Calendar.MONTH, 2);
		calendar.set(Calendar.DATE, 1);
		SearchCriteria a = new SearchCriteria("invoice.invoiceDate",
				calendar.getTime(), OperatorEnum.LESS_EQUAL);
		List criterias = new ArrayList();
		criterias.add(a);
		handler.setCriteria(criterias
				);
		IReportBuilder builder = new SingleTableReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
	}
	
	@Test
	public void InvoiceReportBuild_sortcolumn() throws Exception
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		handler.setCriteria(null); 
		handler.setSortColumn("invoice.invoiceDate");
		handler.validate();
		
		IReportBuilder builder = new SingleTableReportBuilder(handler);

		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
	}
}
