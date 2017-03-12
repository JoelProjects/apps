/* $Id: UTestSaleReportBuilder.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Feb 4, 2008
 * 
 * $Log: UTestSaleReportBuilder.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import store.common.report.builder.IReportBuilder;
import store.common.report.builder.SaleReportBuilder;
import store.common.report.model.ReportHandler;
import store.common.report.model.SearchCriteria;
import store.common.report.model.ReportColumn.DataType;
import store.common.report.model.SearchCriteria.OperatorEnum;
import store.common.report.render.IReport;
import store.common.report.render.MSExcelReport;
import store.common.report.render.IReport.ReportRenderEnum;
import store.common.report.utils.DateEntity;
import store.datamodel.Invoice;
import store.datamodel.InvoiceItem;
import store.datamodel.ItemInfo;
import store.datamodel.ItemTypeInfo;
import store.manager.ReportsManager;
import store.utils.HibernateUtil;

public class UTestSaleReportBuilder
{
	private static ReportHandler handler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		handler = new ReportHandler(InvoiceItem.class);
		List criterias = new ArrayList();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2008);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		
		SearchCriteria a = new SearchCriteria("invoice.invoiceDate", OperatorEnum.GREATER_EQUAL, calendar.getTime()); 
		criterias.add(a);
		
		handler.setCriteria(criterias);
		
		handler.addBeanClass(ItemTypeInfo.class);
		handler.addBeanClass(Invoice.class);
		handler.addBeanClass(ItemInfo.class);
		
		handler.addReportColumn("itemtypeinfo.typeName", "Category", DataType.TEXT,
				false, false);
		handler.addReportColumn("iteminfo.itemNo", "Item No", DataType.TEXT,
				false, false);
		handler.addReportColumn("iteminfo.name", "Item Name", DataType.TEXT,
				false, false);
		handler.addReportColumn("invoiceitem.price", "List Price", DataType.CURRENCY,
				false, false);
		handler.addReportColumn("invoiceitem.quantity", "Quantity", DataType.INTEGER,
				true, false);
		handler.addReportColumn("invoiceitem.total", "Purchase Price", DataType.CURRENCY,
				true, false);
		handler.addReportColumn("dummy", "Comments", DataType.CURRENCY,
				false, true);
		
		try
		{
			handler.validate();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void SaleReport_buildQuery()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SaleReportBuilder(handler);
		
		String query = builder.buildQuery();
		assertNotNull(query);
	}
	
	@Test
	public void SaleReport_executeQuery()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SaleReportBuilder(handler);
		
		String query = builder.buildQuery();
		assertNotNull(query);
		System.out.println(query);
		builder.executeQuery();
	}
	
	@Test
	public void SaleReport_getReport()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SaleReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		assertNotNull(report);
	}
	
	@Test
	public void SaleReport_printReport()
	{
		handler.setRenderType(ReportRenderEnum.SIMPLE_TABLE_RENDER);
		IReportBuilder builder = new SaleReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
	}
	
	@Test
	public void join_table_test()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("FROM InvoiceItem as invoiceitem " +
				"join invoiceitem.itemInfo iteminfo " +
				"join invoiceitem.invoice invoice " +
				"join invoiceitem.itemInfo.typeInfo typeInfo " + 
				"where invoice.invoiceDate <=?");
		query.setDate(0, new Date());
		
		List list = query.list();
		
		session.getTransaction().commit();
	}
	
	
	@Test
	public void scalar_resule_test()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT itemtypeinfo.typeName, iteminfo.itemNo, invoiceitem.price " +
				"FROM InvoiceItem as invoiceitem " +
				"join invoiceitem.itemInfo iteminfo " +
				"join invoiceitem.invoice invoice " +
				"join invoiceitem.itemInfo.typeInfo itemtypeinfo " + 
				"where invoice.invoiceDate <=?");
		query.setDate(0, new Date());
		
		List list = query.list();
		Iterator iterator = list.iterator();
		while(iterator.hasNext())
		{
			Object [] row = (Object [])iterator.next();
			System.out.printf("%s\t%s\t%.2f\n", row[0], row[1], row[2]);
			
			
		}
		session.getTransaction().commit();
	}
	
	@Test
	public void SaleReport_excelReport()
	{
		handler.setRenderType(ReportRenderEnum.EXCEL_RENDER);
		IReportBuilder builder = new SaleReportBuilder(handler);
		
		String query = builder.buildQuery();
		builder.executeQuery();
		IReport report = builder.getReport();
		report.print();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(MSExcelReport.PARAM_FILE_NAME, "C:\\temp\\salereport.xls");
		parameters.put(MSExcelReport.PARAM_DOC_TITLE, "Monthly Salse Report");
		parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Monthly");
		
		String msg = (String)report.renderReport(parameters);
		
		if(msg != null)
			fail("failed to create report: " + msg);
	}
	
	@Test
	public void SaleReport_ReportManager() throws Exception
	{
		ReportsManager manager = ReportsManager.getInstance();
		
		manager.renderSaleItemsReportByMonth("C:\\temp\\salereport_1.xls", ReportsManager.REGION_B,
				new DateEntity(2008, 2));
	}
	
	@Test
	public void SaleReportByDate_ReportManager() throws Exception
	{
		ReportsManager manager = ReportsManager.getInstance();
		
		manager.renderSaleItemsReportByDate("C:\\temp\\salereport_daily.xls", ReportsManager.REGION_A,
				new DateEntity(2008, 2, 15));
	}

}
