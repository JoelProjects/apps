/* $Id: IReportBuilder.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 10, 2008
 * 
 * $Log: IReportBuilder.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.builder;

import store.common.report.render.IReport;

/**
 *
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public interface IReportBuilder
{
	public enum ReportTypeEnum
	{
		InvoiceReoprtType,
		TransactionReportTYpe
	}
	/**
	 * get the report
	 * @return
	 */
	public IReport getReport();
	
	/**
	 * build the query
	 * @return
	 */
	public String buildQuery();
	
	/**
	 * execute the query
	 *
	 */
	public void executeQuery();

}
