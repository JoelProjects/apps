/* $Id: SaleReportBuilder.java,v 1.2 2008/10/22 06:13:37 joelchou Exp $
 * Created on Feb 3, 2008
 * 
 * $Log: SaleReportBuilder.java,v $
 * Revision 1.2  2008/10/22 06:13:37  joelchou
 * Changed to show display name instead of username.
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import members.datamodel.AccountInfo;

import org.apache.log4j.Logger;

import store.common.report.model.ReportColumn;
import store.common.report.model.ReportHandler;
import store.common.report.model.ReportColumn.DataType;
import store.manager.ContextManager;

/**
 * building the sale report base on a period of time.
 * 
 * the sample query is
 * SELECT type_info.type_name, info.item_no, info.name, info.price, item.quantity, item.price
 * FROM invoice i
 * JOIN (invoice_item item, item_info info, item_type_info type_info)
 * ON (i.invoice_id=item.invoice_id AND item.item_id=info.item_id
 * AND type_info.type_id=info.type_id)
 * WHERE i.invoice_date >= '2008-1-1' AND i.invoice_date <= '2008-2-1';
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class SaleReportBuilder extends AbstractReportBuilder
{
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @param handler
	 */
	public SaleReportBuilder(ReportHandler handler)
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
		if(handler.getReportColumns() == null)
			return "";
			
		Set <Map.Entry<String, ReportColumn>>columns = handler.getReportColumns().entrySet();
		if(columns.size() == 0)
			return "";
		StringBuffer selectClause = new StringBuffer("SELECT ");
		Iterator iterator = columns.iterator();
		while(iterator.hasNext())
		{
			Map.Entry<String, ReportColumn> entry = (Map.Entry<String, ReportColumn>)iterator.next();
			if(!entry.getValue().isDummyColumn())
			{	//if this is not a dummy column, add to select clause
				selectClause.append(entry.getKey());
			}else
			{
				//if this a dummy column remove the last coma seperator
				int beginIndex = selectClause.lastIndexOf(", ");  //get the last index of ","
				if(beginIndex != -1)
					selectClause.replace(beginIndex, selectClause.length(), "");
			}
			
			if(iterator.hasNext())
				selectClause.append(", ");
			
		}

		
		return selectClause.toString();
	}


	/* 
	 * Columns: category, item number, item name, list price, purchase quanity, purchase price,
	 */
	@Override
	protected List prepareData(List resultSet)
	{
		if(resultSet == null)
			return null;
		
		Collection <ReportColumn> columns = handler.getReportColumns().values();
		boolean hasSumColumn = false;  // a flag to see if there is a sum column added
		Iterator iterator = resultSet.iterator();
		List data = new ArrayList();
		List sumList = createAndInitializeList(columns.size());
		while(iterator.hasNext()) //looping the rows of result set
		{
			Object[] dataRow = (Object []) iterator.next();
			List row = new LinkedList();
			int dataColumnIndex = 0;  //reset to 0 in new row
			for(ReportColumn column: columns)
			{
				if(column.isSumColumn())
				{//caculate the sum column
					if(DataType.CURRENCY.equals(column.getDataType()) ||
							ReportColumn.DataType.FLOAT.equals(column.getDataType()))
					{// in the case of double
						Double total = (Double)sumList.get(column.getOrder());
						if(total==null)
							total = (Double)dataRow[dataColumnIndex];
						else
							total = total + (Double)dataRow[dataColumnIndex];
						
						sumList.set(column.getOrder(), total);  //put it back to list
						hasSumColumn = true;
					}else if(DataType.INTEGER.equals(column.getDataType()))
					{//in the case of integer
						Integer total = (Integer)sumList.get(column.getOrder());
						if(total==null)
							total = (Integer)dataRow[dataColumnIndex];
						else
							total = total + (Integer)dataRow[dataColumnIndex];
						
						sumList.set(column.getOrder(), total);  //put it back to list
						hasSumColumn = true;
					}else
						logger.error("unsupported sum column: " + column.getColumnName() + ":" +
								column.getDataType().toString());
						
				} else if(column.getColumnName().equals("invoice.handledBy")) {
				    String username = (String)dataRow[dataColumnIndex];
		            AccountInfo accountInfo = ContextManager.getAccountInfo(username);
		            String name = "";
		            if(accountInfo != null)
		                name = accountInfo.getMemberInfo().getName();
		            else
		                name = username;	
		            dataRow[dataColumnIndex] = name;
				}
				
				if(!column.isDummyColumn())
				{
					row.add(column.getOrder(),dataRow[dataColumnIndex++]);
				}else
				{
					//if this is dummy column, just add empty string
					row.add(column.getOrder(),"");
				}
				
			}
			//add row to table
			data.add(row);
		}
		
		// if there is any sum row in the object, add extra sum row to the end
		if(hasSumColumn)
		{
			List row = new ArrayList();
			for(ReportColumn column: columns)
			{
				if(column.getOrder()==0 && !column.isSumColumn())
					row.add(TOTAL_COLUMN_LABEL);   //put total label at the column
				else if(column.isSumColumn())
					row.add(sumList.get(column.getOrder()));	//put the total value
				else
					row.add("");
			}
			data.add(row); //add sum row to  data
		}

		return data;
	}

	@Override
	protected String getJoinStatement()
	{
		return "join invoiceitem.itemInfo iteminfo " +
		"join invoiceitem.invoice invoice " +
		"join invoiceitem.itemInfo.typeInfo itemtypeinfo ";
	}
	
	private List createAndInitializeList(int numberOfElement)
	{
		List list = new ArrayList();
		for(int i=0; i<numberOfElement; i++)
			list.add(null);
		
		return list;
	}

}
