/* $Id: InvoiceItemReportBuilder.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Feb 4, 2008
 * 
 * $Log: InvoiceItemReportBuilder.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.builder;

import java.util.List;

import store.common.report.model.ReportHandler;

/**
 * This build the Item sales report by a period of time
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class InvoiceItemReportBuilder extends AbstractReportBuilder
{

	/**
	 * @param handler
	 */
	public InvoiceItemReportBuilder(ReportHandler handler)
	{
		super(handler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see store.common.report.AbstractReportBuilder#getSelectClause()
	 */
	@Override
	protected String getSelectClause()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see store.common.report.AbstractReportBuilder#prepareData(java.util.List)
	 */
	@Override
	protected List prepareData(List resultSet)
	{
		return null;
	}

	@Override
	protected String getJoinStatement()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
