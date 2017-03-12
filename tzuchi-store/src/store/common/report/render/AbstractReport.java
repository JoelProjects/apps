/* $Id: AbstractReport.java,v 1.2 2008/02/29 20:05:48 jlin Exp $
 * Created on Jan 13, 2008
 * 
 * $Log: AbstractReport.java,v $
 * Revision 1.2  2008/02/29 20:05:48  jlin
 * Add Daily Report for the backend and UI
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import store.common.report.model.ReportColumn;
import store.common.report.model.ReportHandler;
import store.common.report.utils.ReportConstants;

/**
 * AbstractReport layer of the Reports
 *
 * @author Jeff Lin
 * @version  $Date: 2008/02/29 20:05:48 $
 */
public abstract class AbstractReport implements IReport, ReportConstants
{
	protected List <List>data;
	protected ReportHandler handler;
	protected Map <String, String>parameters;
	
	protected AbstractReport(List <List>data, ReportHandler handler)
	{
		this.data = data;
		this.handler = handler;
		parameters = new HashMap<String, String>();
	}
	
	protected AbstractReport(IReport report)
	{
		this.data = report.getData();
		this.handler = report.getHandler();
		this.parameters = report.getParameters();
	}
	

	public List getData()
	{
		return data;
	}
	
	public ReportHandler getHandler()
	{
		return handler;
	}
	
	public void print()
	{
		printHeader();
		for(List row: data)
		{
			Iterator iterator = row.iterator();
			while(iterator.hasNext())
			{
				String value = iterator.next().toString();
				System.out.printf("%s\t", value);
			}
			System.out.println("\n");
				
		}
		
	}
	
	
	private void printHeader()
	{
		Collection<ReportColumn> columns = handler.getReportColumns().values();
		for(ReportColumn columnHeader: columns)
		{
			System.out.printf("%s\t", columnHeader.getDisplayName());
		}

		System.out.printf("\n");
		
	}
	
	protected Class getPropertyType(String propertyName)
	{
		return handler.getPropertyHelper().getPropertyType(propertyName);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractReport other = (AbstractReport) obj;
		if (data == null)
		{
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (handler == null)
		{
			if (other.handler != null)
				return false;
		} else if (!handler.equals(other.handler))
			return false;
		return true;
	}
	
	public Map<String, String> getParameters()
	{
		return parameters;
	}
	
	/**
	 * if the passesed parameters is null, use the default parameters
	 * @param parameters
	 * @return
	 */
	protected Map <String, String>getParametersForUse(Map<String, String> parameters)
	{
		return parameters == null? this.parameters: parameters;
	}
	
	
}
