/* $Id: ReportHandler.java,v 1.3 2009/12/06 00:11:25 joelchou Exp $
 * Created on Jan 10, 2008
 * 
 * $Log: ReportHandler.java,v $
 * Revision 1.3  2009/12/06 00:11:25  joelchou
 * Added inventory list
 *
 * Revision 1.2  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 * Revision 1.1  2008/02/29 13:01:20  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import store.common.report.UnmatchedPropertyException;
import store.common.report.UnmatchedPropertyTypeException;
import store.common.report.model.ReportColumn.DataType;
import store.common.report.render.IReport.ReportRenderEnum;
import store.common.report.utils.DateEntity;
import store.common.report.utils.PropertyHelper;

/**
 * ReportHandler contain the bean infomation, report format and styles.
 * ReportHandler is used to feed the ReportBuilder to generate the Report
 * object.
 * 
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class ReportHandler
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Class primaryBean;
	private List beanClasses;
	private PropertyHelper propertyHelper;
	private List <SearchCriteria>criterias;  //used to set parameters for where clause
	private List <SearchCriteria>extraParams;  //used to hold the parameters other than where clause
	private Map <String, ReportColumn> reportColumnMap;
	private String sortColumn;
	private String groupColumn;
	private ReportRenderEnum renderType;
	private boolean nativeQuery;  //indicate this is a native SQL query

	/**
	 * Create a instance of report handler by specifying the target object.
	 * The search criteria is retrieved from targetObject itself.  The default 
	 * report columns are assumed to be the all properties of the target object
	 * @param targetObject - the target Objects to be reported
	 */
	public ReportHandler(Class targetObject)
	{
		this(targetObject, null);
	}
	
	
	/**
	 * Create a ReportHandler instance by specifying the target object and the
	 * search criteria and the reported columns.</br>
	 * <P>Please noted:
	 * <li>The search criteria will be used as specified which is 
	 * overriding the value that stored in the object itself. the same
	 * <li>the reportColumns can only be the properties of the object 
	 * @param targetObject
	 * @param criteria - key-value paire Map.  The key have to one of the bean
	 * properties. Otherwise, UnmatchedPropertyException is thrown
	 * The value has to be the object corresponding to the type of the object,
	 * Otherwise UnmatchPropertyTypeExcpetion(uncheck) is thrown. 
	 * @param reportColumns The reportColumn is the column data will be retreieved
	 * from data source.  If reportColumn is not specify at all (null value),
	 * The default reportColumns are all properties of the target object
	 */
	public ReportHandler(Class targetObject, List <SearchCriteria> criteria)
	{
		primaryBean = targetObject;
		propertyHelper = new PropertyHelper();
		propertyHelper.addBean(primaryBean);
		nativeQuery = false;  //default to HQL query
		this.criterias = criteria;
	}
	
	/**
	 * validate the search criteria and column header.
	 * @throws UnmatchedPropertyException - at lease specified property does existed.
	 * @throws UnmatchedPropertyTypeException - The value of the criteria does not 
	 * match the property type
	 */
	public void validate() throws UnmatchedPropertyException,
	UnmatchedPropertyTypeException
	{
		//validate search criterias
		if(criterias != null  || !isNativeQuery())
		{
			propertyHelper.validateSearchCriteria(criterias);
		}
		
		//validate column label 
		if(reportColumnMap !=null || !isNativeQuery())
		{
			propertyHelper.validateReportColumn(reportColumnMap);
		}
		
		//validate sort column
		if(sortColumn != null || !isNativeQuery())
		{
			propertyHelper.validateProperty(sortColumn);
		}

	}
	
	public void addSearchCriteria(SearchCriteria criteria)
	{
		criterias.add(criteria);
	}
	
	

	public Class getPrimaryBean()
	{
		return primaryBean;
	}
	
	public void addBeanClass(Class bean)
	{
		if(beanClasses == null)
		{
			beanClasses = new ArrayList();
		}
		
		beanClasses.add(bean);
		propertyHelper.addBean(bean);
		
	}

	public List <SearchCriteria> getCriterias()
	{
		return criterias;
	}

	public void setCriteria(List<SearchCriteria> criterias)
	{
		this.criterias = criterias;
	}

	public Map getReportColumns()
	{
		return reportColumnMap;
	}

	public void setReportColumnLabelMap(LinkedHashMap<String, ReportColumn> reportColumnLabelMap)
	{
		this.reportColumnMap = reportColumnLabelMap;
	}

	public String getSortColumn()
	{
		return sortColumn;
	}

	public void setSortColumn(String sortColumn)
	{
		this.sortColumn = sortColumn;
	}

	public ReportRenderEnum getRenderType()
	{
		return renderType;
	}

	public void setRenderType(ReportRenderEnum renderType)
	{
		this.renderType = renderType;
	}

	public PropertyHelper getPropertyHelper()
	{
		return propertyHelper;
	}

	
	/**
	 * get the index of column
	 * @param columnName
	 * @return
	 */
	public List getSumColumnIndex()
	{
		ArrayList sumColumns = null;
		
		if(reportColumnMap != null && !reportColumnMap.isEmpty())
		{
			sumColumns = new ArrayList();
			Collection <ReportColumn> columns = reportColumnMap.values();
			for(ReportColumn column: columns)
			{
				if(column.isSumColumn())
				{
					sumColumns.add(column.getOrder());
				}
			}
		}
		
		return sumColumns;
	}
	
	public void addReportColumn(String columnName,
								String displayName,
								DataType dataType,
								boolean isSumColumn,
								boolean isDummyColumn)
	{
		addReportColumn(new ReportColumn(columnName,
										 displayName,
										 dataType,
										 isSumColumn,
										 isDummyColumn));	
	}
	
    public void addReportColumn(String columnName,
        String displayName,
        DataType dataType,
        boolean isSumColumn,
        boolean isDummyColumn, int width) {
        addReportColumn(new ReportColumn(columnName,
                 displayName,
                 dataType,
                 isSumColumn,
                 isDummyColumn, width));   
    }	
	
	public void addReportColumn(ReportColumn column)
	{
		if(reportColumnMap == null)
			reportColumnMap = new LinkedHashMap();
		
		column.setOrder(reportColumnMap.size()); //set the order of the report Column
		reportColumnMap.put(column.getColumnName(), column);
	}


	public boolean isNativeQuery()
	{
		return nativeQuery;
	}


	public void setNativeQuery(boolean nativeQuery)
	{
		this.nativeQuery = nativeQuery;
	}


	public List<SearchCriteria> getExtraParams()
	{
		return extraParams;
	}


	public void setExtraParams(List<SearchCriteria> extraParams)
	{
		this.extraParams = extraParams;
	}


	public String getGroupColumn()
	{
		return groupColumn;
	}


	public void setGroupColumn(String groupColumn)
	{
		this.groupColumn = groupColumn;
	}


}
