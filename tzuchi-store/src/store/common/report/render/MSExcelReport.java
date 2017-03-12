/* $Id: MSExcelReport.java,v 1.6 2009/12/06 00:11:25 joelchou Exp $
 * Created on Feb 5, 2008
 * 
 * $Log: MSExcelReport.java,v $
 * Revision 1.6  2009/12/06 00:11:25  joelchou
 * Added inventory list
 *
 * Revision 1.5  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 * Revision 1.4  2008/03/13 20:29:18  jlin
 * add simple asset report
 *
 * Revision 1.3  2008/03/06 23:12:46  jlin
 * add new feature:  Sale Report by sale handlers
 *
 * Revision 1.2  2008/02/29 20:05:48  jlin
 * Add Daily Report for the backend and UI
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.render;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import store.common.report.model.ReportColumn;
import store.common.report.model.ReportHandler;
import store.common.report.model.ReportColumn.DataType;
import store.common.report.utils.DateEntity;

/**
 * renderReport() take the following parameter
 * PARAM_FILE_NAME (required): the file that spreadsheet is exorted to
 * PARAM_DOC_TITLE (Optional): the document title fle the sheet;
 * PARAM_DOC_SHORT_NAME (Optional): the short name is used as sheet name.
 * 
 * The return of the render report will a String object.  If the string is not null
 * and it is not empty, the excel spreed sheet has problem to create and the problematic
 * messages is return.
 * 
 *
 * @author Jeff Lin
 * @version  $Date: 2009/12/06 00:11:25 $
 */
public class MSExcelReport extends AbstractReport
{
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * report parameter
	 */
	public static final String PARAM_FILE_NAME = "fileName";    //required
	public static final String PARAM_DOC_TITLE = "documentTitle";
	public static final String PARAM_DOC_SHORT_NAME = "shortName";
	
	
	
	/**
	 * param value
	 */
	protected String fileName = null;
	protected String documentTitle = null;
	protected String shortName = null;
	
	protected HSSFWorkbook wb = null;
	protected static Map <DataType, HSSFCellStyle> cellStyleAndFontPool;
	
	/**
	 * @param data
	 * @param handler
	 */
	public MSExcelReport(List<List> data, ReportHandler handler)
	{
		super(data, handler);
	}
	
	public MSExcelReport(IReport report)
	{
		super(report);
	}

	/* (non-Javadoc)
	 * @see store.common.report.IReport#renderReport(java.util.Map)
	 */
	public Object renderReport(Map<String, String> parameters)
	{
		String msg = validateParam(parameters);
		int rowIndex = 0;
		
		if(msg !=null)
		{
			logger.error("Report Parameters is not valid: " + msg);
			return msg;
		}
		
		initWorkbook();
		HSSFSheet sheet = addNewSheet(shortName);
		rowIndex = addBusinessHeader(sheet, rowIndex);
		rowIndex = addDocumentTitle(sheet, rowIndex, documentTitle);
		rowIndex = addReportTime(sheet, rowIndex);
		rowIndex = addTableHeader(sheet, rowIndex, handler.getReportColumns().values());
		
		rowIndex = populateTable(sheet, rowIndex, data);
		
		short colIdx = 0;
        Collection<ReportColumn> columns = handler.getReportColumns().values();
        for(ReportColumn column: columns) {		
            if(column.getWidth() > 0)
                sheet.setColumnWidth(colIdx, (short)column.getWidth());
            
            colIdx++;
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
	
	protected String validateParam(Map <String, String>parameters)
	{
		//if the one passed is null, use the default one;
		Map internalParam = getParametersForUse(parameters);
		
		if(internalParam == null)
			return "Output file name must be specificed\n";
		
		StringBuffer buff = new StringBuffer();
		if(internalParam.get(PARAM_FILE_NAME) != null)
		{
			fileName = (String)internalParam.get(PARAM_FILE_NAME);
			
		}else
			buff.append("Output file name must be specificed\n");
		
		documentTitle = (String)internalParam.get(PARAM_DOC_TITLE);
		shortName = (String) internalParam.get(PARAM_DOC_SHORT_NAME);
		
		return buff.length()==0? null: buff.toString();
	}
	
	protected HSSFSheet addNewSheet(String title)
	{
		return wb.createSheet("title");
	}
	
	protected HSSFWorkbook initWorkbook()
	{
		wb = new HSSFWorkbook();
		initCellStyleAndFontPoll();  //every render need to do this part after web book is created
		return wb;
	}
	
	protected int addDocumentTitle(HSSFSheet sheet, int rowIndex, String header)
	{
		HSSFRow row = sheet.createRow(rowIndex++);
		HSSFCell cell = row.createCell((short)0);
		cell.setCellValue(header);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)14);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		cell.setCellStyle(style);
		
		return rowIndex;
	}
	
	protected int addBusinessHeader(HSSFSheet sheet, int rowIndex)
	{
		
		StringBuffer buff = new StringBuffer();
		HSSFRow row = sheet.createRow(rowIndex++);
		HSSFCell cell = row.createCell((short)0);
		cell.setCellValue("慈濟基金會波士頓聯絡處");
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)18);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		cell.setCellStyle(style);
		
		
		row = sheet.createRow(rowIndex++);
		cell = row.createCell((short)0);
		cell.setCellValue("Tzu Chi Foundation USA, Boston Service Center");
		style = wb.createCellStyle();
		font = wb.createFont();
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		cell.setCellStyle(style);
		
		row = sheet.createRow(rowIndex++);
		cell = row.createCell((short)0);
		cell.setCellValue("15 Summer St, Newton MA 02464");
		style = wb.createCellStyle();
		font = wb.createFont();
		font.setFontHeightInPoints((short)9);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		cell.setCellStyle(style);
		
		row = sheet.createRow(rowIndex++);
		cell = row.createCell((short)0);
		cell.setCellValue("Tel # 617-762-0569");
		style = wb.createCellStyle();
		font = wb.createFont();
		font.setFontHeightInPoints((short)9);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		cell.setCellStyle(style);
		
		rowIndex++;  // pad with an empty row
		
		return rowIndex;
		
	}
	
	protected int addReportTime(HSSFSheet sheet, int rowIndex)
	{
		DateEntity date = new DateEntity();
		date.getFormatedDateString();
		HSSFRow row = sheet.createRow(rowIndex++);
		HSSFCell cell = row.createCell((short)0);
		cell.setCellValue("報表日期:" + date.getFormatedDateString());
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		cell.setCellStyle(style);
		
		return rowIndex;
		
	}
	
	protected int addTableHeader(HSSFSheet sheet, int rowIndex, Collection<ReportColumn> columns)
	{
		if (columns == null || columns.size() == 0)
			return rowIndex;
		short cellIndex = 0;
		HSSFRow row = sheet.createRow(rowIndex++);
		for(ReportColumn column: columns)
		{
			HSSFCell cell = row.createCell(cellIndex++);
			cell.setCellValue(column.getDisplayName());
			HSSFCellStyle style= wb.createCellStyle();
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short)12);
			font.setFontName("Arial");
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cell.setCellStyle(style);
		}
		
		return rowIndex;
		
	}
	
	protected int populateTable(HSSFSheet sheet, int rowIndex, List<List> table)
	{
		for(List dataRow: table)
		{
			HSSFRow row = sheet.createRow(rowIndex++);
			rowIndex = populateRow(row, rowIndex, dataRow);
		}
		
		return rowIndex;
	}
	
	protected int populateRow(HSSFRow row, int rowIndex, List dataRow)
	{
		short cellIndex = 0;
		Iterator dataIterator = dataRow.iterator();
		Iterator columnIterator = handler.getReportColumns().values().iterator();
		while(dataIterator.hasNext())
		{
			HSSFCell cell = row.createCell(cellIndex++);
			ReportColumn column = (ReportColumn)(columnIterator.next());
			HSSFCellStyle style = getCellStyleAndFont(column);
			cell.setCellStyle(style);
			Object value = dataIterator.next();
			if(value instanceof Date)
			{
				cell.setCellValue((Date)value);
			}else if(value instanceof Double)
			{
				cell.setCellValue((Double)value);
			}else if(value instanceof Integer)
			{
				cell.setCellValue((Integer)value);
			}else if(value instanceof Calendar)
			{
				cell.setCellValue((Calendar)value);
			}else if(value instanceof Boolean)
			{
				cell.setCellValue((Boolean)value);
			}else if(value == null)
			{//if value is null, set it as empty string
				cell.setCellValue("");
			}
			else  
			{//use the String cell type as default
				cell.setCellValue(value.toString());
				style.setWrapText(true);
			}
		}
		
		return rowIndex;
	}
	
	protected HSSFCellStyle getCellStyleAndFont(ReportColumn column)
	{
		HSSFCellStyle style = cellStyleAndFontPool.get(column.getDataType());
		
		//return Text style as default
		return style == null? cellStyleAndFontPool.get(DataType.TEXT): style;
	}
	
	protected void writeWorkBook()
	throws FileNotFoundException, IOException
	{
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();
	}
	
	private void initCellStyleAndFontPoll()
	{
		cellStyleAndFontPool = new HashMap<DataType, HSSFCellStyle>();
		
		//DataType.DATE
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		formateCellStyle(DataType.DATE, style, font);
		cellStyleAndFontPool.put(DataType.DATE, style);
		
		//DataType.CURRENCY
		style = wb.createCellStyle();
		font = wb.createFont();
		formateCellStyle(DataType.CURRENCY, style, font);
		cellStyleAndFontPool.put(DataType.CURRENCY, style);
		
		//DataType.FLOAT
		style = wb.createCellStyle();
		font = wb.createFont();
		formateCellStyle(DataType.FLOAT, style, font);
		cellStyleAndFontPool.put(DataType.FLOAT, style);
		
		//DataType.INTEGER
		style = wb.createCellStyle();
		font = wb.createFont();
		formateCellStyle(DataType.INTEGER, style, font);
		cellStyleAndFontPool.put(DataType.INTEGER, style);
		
		//DataType.TEXT
		style = wb.createCellStyle();
		font = wb.createFont();
		formateCellStyle(DataType.TEXT, style, font);
		cellStyleAndFontPool.put(DataType.TEXT, style);
		
	}
	
	private HSSFCellStyle formateCellStyle(DataType type, HSSFCellStyle style, HSSFFont font)
	{
		font.setFontHeightInPoints((short)12);
		font.setFontName("Arial");
		style.setFont(font);
		//set cell align to Right by default
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		if(ReportColumn.DataType.DATE.equals(type))
		{
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		}else if(ReportColumn.DataType.CURRENCY.equals(type))
		{
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("($#,##0.00_);[Red]($#,##0.00)"));
		}else if(ReportColumn.DataType.FLOAT.equals(type))
		{
			HSSFDataFormat format = wb.createDataFormat();
			style.setDataFormat(format.getFormat("###,###.00"));
		}else if(ReportColumn.DataType.INTEGER.equals(type))
		{
			HSSFDataFormat format = wb.createDataFormat();
			style.setDataFormat(format.getFormat("###,###"));
		}else
		{ 	//default style
			//set cell align to left for string
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("General"));
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style.setFont(font);
		}
		
		return style;
	}
	
	
	
	
	

}
