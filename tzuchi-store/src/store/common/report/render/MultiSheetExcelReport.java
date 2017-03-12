/* $Id: MultiSheetExcelReport.java,v 1.2 2008/03/13 20:29:18 jlin Exp $
 * Created on Mar 5, 2008
 * 
 * $Log: MultiSheetExcelReport.java,v $
 * Revision 1.2  2008/03/13 20:29:18  jlin
 * add simple asset report
 *
 * Revision 1.1  2008/03/06 23:12:46  jlin
 * add new feature:  Sale Report by sale handlers
 *
 */
package store.common.report.render;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import store.common.report.model.ReportHandler;

/**
 * This class renders multi-sheet excel report.
 * Use {@link #addReport(IReport)} method to add additional report
 *
 * @author Jeff Lin
 * @version  $Date: 2008/03/13 20:29:18 $
 * status: draft (not tested)
 */
public class MultiSheetExcelReport extends MSExcelReport
{
	private Logger logger = Logger.getLogger(this.getClass());
	List <IReport>reports = null;
	
	/**
	 * @param data
	 * @param handler
	 */
	public MultiSheetExcelReport(List<List> data, ReportHandler handler)
	{
		super(data, handler);
		reports = new ArrayList<IReport>();
		reports.add(this); //add table of itself to the report list
	}
	
	public MultiSheetExcelReport(IReport report)
	{
		super(report);
		reports = new ArrayList<IReport>();
		reports.add(this); //add table of itself to the report list
	}
	
	/**
	 * add additional report to be rendered
	 * @param report
	 */
	public void addReport(IReport report)
	{
		reports.add(report);
	}
	
	@Override
	public Object renderReport(Map<String, String> parameters)
	{
		String msg = validateParam(parameters);
		
		if(msg !=null)
		{
			logger.error("Report Parameters is not valid: " + msg);
			return msg;
		}
		
		initWorkbook();
		
		for(IReport report: reports)
		{
			//inital phase
			int rowIndex = 0;  //reset rowIndex
			String documentTitle = getParam(report, PARAM_DOC_TITLE);
			String shortName = getParam(report, PARAM_DOC_SHORT_NAME);
			
			HSSFSheet sheet = addNewSheet(shortName);
			rowIndex = addBusinessHeader(sheet, rowIndex);
			rowIndex = addDocumentTitle(sheet, rowIndex, documentTitle);
			rowIndex = addReportTime(sheet, rowIndex);
			rowIndex = addTableHeader(sheet, rowIndex, handler.getReportColumns().values());
			rowIndex = populateTable(sheet, rowIndex, report.getData());
		}
		
		try
		{
			writeWorkBook();
		}catch(FileNotFoundException e)
		{
			logger.error(e);
			msg = "can not access file: " + fileName;
		}catch(IOException e)
		{
			logger.error(e);
			msg = "problem to export the Excel file: " + fileName;
		}
		return msg;
		
	}
	
	private String getParam(IReport report, String paramName)
	{
		return report.getParameters().get(paramName);
	}

}
