/* $Id: AssetTransactionReportBuilder.java,v 1.3 2009/12/01 05:45:36 joelchou Exp $
 * Created on Mar 17, 2008
 * 
 * $Log: AssetTransactionReportBuilder.java,v $
 * Revision 1.3  2009/12/01 05:45:36  joelchou
 * Fixed issues on monthly inventory report. Changed to use older version of MySQL JDBC driver since Hibernate was complaining "column u_quantity not found" when using new version of driver.
 *
 * Revision 1.2  2008/03/19 19:03:04  jlin
 * Add modified_by column to Asset Transation report.
 *
 * Revision 1.1  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 */
package store.common.report.builder;


import store.common.report.model.ReportHandler;

/**
 * This builder use native SQL query which might contain some DB dependent query
 *
 * @author Jeff Lin
 * @version  $Date: 2009/12/01 05:45:36 $
 */
public class AssetTransactionReportBuilder extends SaleReportBuilder
{

	/**
	 * @param handler
	 */
	public AssetTransactionReportBuilder(ReportHandler handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see store.common.report.builder.AbstractReportBuilder#getJoinStatement()
	 */
	@Override
	protected String getJoinStatement()
	{
		return "left join (SELECT item_id, sum(updated_quantity) u_quantity, " +
				"group_concat(invoice_number ORDER BY hist_id) invoice_no, " +
				"group_concat(modified_by ORDER BY hist_id) modified_by " +
				"FROM item_history where mod_date >=? and mod_date <? group by item_id) " +
				"history1 on iteminfo.item_id = history1.item_id " +
		//"left join (SELECT item_id, sum(updated_quantity) u_quanity, group_concat(invoice_number ORDER BY invoice_number DESC) invoice_no " +
		//"FROM item_history where mod_date >=? and mod_date <=? group by item_id) history2 " +
		//"on iteminfo.item_id = history2.item_id " +
		"left join (SELECT item_id, sum(quantity) quantity from invoice_item, invoice where invoice.invoice_id = invoice_item.invoice_id and invoice.invoice_date >=? and invoice.invoice_date <?  group by item_id) invoiceitem1 on iteminfo.item_id = invoiceitem1.item_id " +
		//"left join (SELECT item_id, sum(quantity) quantity from invoice_item, invoice where invoice.invoice_id = invoice_item.invoice_id and invoice.invoice_date >=? and invoice.invoice_date <=?  group by item_id) invoiceitem2 on iteminfo.item_id = invoiceitem2.item_id " +
		"join item_type_info typeinfo on iteminfo.type_id = typeinfo.type_id " +
		"where iteminfo.type_id = typeinfo.type_id " +
		"and (history1.u_quantity is not null " +
		//"or history2.u_quanity is not null " +
		"or invoiceitem1.quantity is not null " +
		//"or invoiceitem2.quantity is not null" +
		") ";
	}
	
	@Override
	protected String getSelectClause()
	{
		return "SELECT typeinfo.type_name, " +
				"iteminfo.item_no, iteminfo.name, " +
				"history1.u_quantity 1_quantity, " +
				"history1.invoice_no, " +
				"history1.modified_by, " +
				//"history2.u_quanity 2_quantity, " +
				//"history2.invoice_no, " +
				"invoiceitem1.quantity 1i_quantity " +
				//"invoiceitem2.quantity 2i_quantity  " +
				"FROM item_info iteminfo";
	}

}
