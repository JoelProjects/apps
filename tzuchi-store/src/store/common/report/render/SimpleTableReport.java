/* $Id: SimpleTableReport.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 14, 2008
 * 
 * $Log: SimpleTableReport.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.render;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import store.common.report.model.ReportColumn;
import store.common.report.model.ReportHandler;
import store.ui.NotEditableTableModel;

/**
 * This class render the Simple Table report
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class SimpleTableReport extends AbstractReport
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Parameter:
	 * TableModel object to be populated with data
	 */
	public static final String PARAM_TABLE_MODEL = "tableModel";    //required
	
	private TableModel tableModel = null;

	/**
	 * @param data
	 * @param handler
	 */
	public SimpleTableReport(List data, ReportHandler handler)
	{
		super(data, handler);
	}
	
	public SimpleTableReport(IReport report)
	{
		super(report);
	}

	/**
	 * The TableModel object is returned.  No parameter is required
	 */
	public Object renderReport(Map parameters)
	{
		Object [] headers = getArrayHeader();
		Object [] [] data = getArrayData();
		
		tableModel = new NotEditableTableModel(data, headers);
		
		return tableModel;
	}
	
	/**
	 * get column header in Array format.  if there is no column header, 
	 * the return is null
	 * @return
	 */
	private String [] getArrayHeader()
	{
		Collection<ReportColumn> columns = handler.getReportColumns().values();
		String [] columnArray = new String [columns.size()];
		int i = 0;
		for(ReportColumn column: columns)
		{
			columnArray[i++] = column.getDisplayName();
		}
		
		return columns.size() > 0? columnArray: null;
	}
	
	private Object [][] getArrayData()
	{
		int rowCount = 0;
		Object [][] table = new Object [data.size()][] ;
		for(List dataRow: data)
		{
			table[rowCount] = new Object [dataRow.size()]; 
			populateRow(table[rowCount++], dataRow);
		}
		
		return table;
	}
	
	public void populateRow(final Object []row  , List dataRow)
	{
		Iterator dataIterator = dataRow.iterator();
		int index = 0;
		while(dataIterator.hasNext())
		{
			row [index++] = dataIterator.next(); 	
		}
	}
	

	

}
