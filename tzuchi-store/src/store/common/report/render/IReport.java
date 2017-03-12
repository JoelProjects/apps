/* $Id: IReport.java,v 1.2 2008/02/29 20:05:48 jlin Exp $
 * Created on Jan 13, 2008
 * 
 * $Log: IReport.java,v $
 * Revision 1.2  2008/02/29 20:05:48  jlin
 * Add Daily Report for the backend and UI
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.render;

import java.util.List;
import java.util.Map;

import store.common.report.model.ReportHandler;

/**
 * The class is responsible for the presentation of the report by supported 
 * RenderType.
 *
 * @author Jeff Lin
 * @version  $Date: 2008/02/29 20:05:48 $
 */
public interface IReport
{
	enum ReportRenderEnum
	{
		SIMPLE_TABLE_RENDER,
		TEXT_RENDER,
		EXCEL_RENDER
	}
	
	/**
	 * action to Render the report object 
	 * @param parameters - parameter to render the report 
	 * @return Object - could be a object representation report object or message
	 * from the result of the rendering report.  The returns is more specific in
	 * the derived object.
	 * 
	 */
	public Object renderReport(Map<String, String> parameters);
	
	
	/**
	 * get the List of the objects that represent the report the results
	 * @return
	 */
	public List getData();
	
	/**
	 * print the report on the console
	 * @return
	 */
	public void print();
	
	/**
	 * Get report handler
	 *
	 */
	public ReportHandler getHandler();
	
	/**
	 * Get parameter for the report
	 * @return
	 */
	public Map<String, String> getParameters();
	

}
