/* $Id: SingleTableReportBuilder.java,v 1.1 2008/03/13 20:29:18 jlin Exp $
 * Created on Jan 11, 2008
 * 
 * $Log: SingleTableReportBuilder.java,v $
 * Revision 1.1  2008/03/13 20:29:18  jlin
 * add simple asset report
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.builder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import store.common.report.model.ReportHandler;
import store.datamodel.Invoice;

/**
 * A generic report builder for building data from single database table
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class SingleTableReportBuilder extends AbstractReportBuilder
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	public SingleTableReportBuilder(ReportHandler handler)
	{
		super(handler);
	}

	@Override
	public String getSelectClause()
	{
		return "";
	}

	@Override
	protected List prepareData(List resultSet)
	{
		if(resultSet == null)
			return null;
		
		Iterator iterator = resultSet.iterator();
		List data = new ArrayList();
		while(iterator.hasNext())
		{
			Object object = handler.getPrimaryBean().cast(iterator.next());
			List row = new ArrayList();
			Set<String> columns = handler.getReportColumns().keySet();
			for(String column: columns)
			{
				String propertyName = column.substring(column.indexOf(".")+1);
				try
				{
					PropertyDescriptor pd = new PropertyDescriptor(propertyName, handler.getPrimaryBean());
					row.add(pd.getReadMethod().invoke(object, (Object []) null));
				}catch(IntrospectionException e)
				{
					logger.error(e);
				}catch(InvocationTargetException e)
				{
					logger.error(e);
				}catch(IllegalAccessException e)
				{
					logger.error(e);
				}
			}
			data.add(row); //add row to data pool
		}
		
		return data;
	}

	@Override
	protected String getJoinStatement()
	{
		return "";
	}

}
