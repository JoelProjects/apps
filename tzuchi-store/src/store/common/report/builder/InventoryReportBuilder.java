/* $Id: InventoryReportBuilder.java,v 1.1 2009/12/06 00:11:25 joelchou Exp $ */
package store.common.report.builder;

import store.common.report.model.ReportHandler;

/**
 * This builder uses native SQL query to build inventory list.
 *
 * @author Joel Chou
 * @since  12/3/2009
 */
public class InventoryReportBuilder extends SaleReportBuilder {
    
	public InventoryReportBuilder(ReportHandler handler) {
		super(handler);
	}

	@Override
	protected String getJoinStatement() {
		return "LEFT JOIN (\n" +
		       "SELECT\n" +
		       "item_id, sum(updated_quantity) u_quantity,\n" +
		       "group_concat(concat(date_format(mod_date, '%b-%d %H:%i'),' ',invoice_number, ' (', updated_quantity, ')', '\n') ORDER BY hist_id SEPARATOR '') item_in\n" +
		       "FROM item_history\n" +
		       "WHERE mod_date >=? and mod_date <?\n" +
		       "group by item_id\n" +
		       ") history1 on iteminfo.item_id = history1.item_id\n" +
		       "LEFT JOIN (\n" +
		       "SELECT item_id, sum(quantity) quantity,\n" +
		       "group_concat(concat(date_format(invoice_date, '%b-%d %H:%i'), ' (', quantity, ')', '\n') ORDER BY invoice.invoice_id SEPARATOR '') item_out\n" +
		       "FROM invoice_item, invoice\n" +
		       "WHERE invoice.invoice_id = invoice_item.invoice_id\n" +
		       "and invoice.invoice_date >=? and invoice.invoice_date <?\n" +
		       "GROUP BY item_id\n" +
		       ") invoiceitem1 on iteminfo.item_id = invoiceitem1.item_id\n" +
		       "JOIN\n" +
		       "item_type_info typeinfo on iteminfo.type_id = typeinfo.type_id\n" +
		       "WHERE iteminfo.type_id = typeinfo.type_id and\n" +
		       "(history1.u_quantity is not null or invoiceitem1.quantity is not null)\n";
	}
	
	@Override
	protected String getSelectClause() {
		return "SELECT\n" +
				"typeinfo.type_name,\n" +
		        "iteminfo.item_no,\n" +
		        "iteminfo.name,\n" + 
		        "convert(history1.item_in using utf8) item_in,\n" +
		        "history1.u_quantity in_quantity,\n" +
		        "convert(invoiceitem1.item_out using utf8) item_out,\n" +
		        "invoiceitem1.quantity out_quantity,\n" +
		        "iteminfo.quantity\n" +
		        "FROM item_info iteminfo\n";
	}
}
