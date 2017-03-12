/* $Id: AbstractReportBuilder.java,v 1.2 2008/03/18 21:26:55 jlin Exp $
 * Created on Jan 12, 2008
 * 
 * $Log: AbstractReportBuilder.java,v $
 * Revision 1.2  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.builder;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.common.report.model.ReportHandler;
import store.common.report.model.SearchCriteria;
import store.common.report.render.IReport;
import store.common.report.render.MSExcelReport;
import store.common.report.render.SimpleTableReport;
import store.common.report.render.IReport.ReportRenderEnum;
import store.common.report.utils.ReportConstants;
import store.utils.HibernateUtil;

/**
 *
 *
 * @author Jeff Lin
 * @version  $Date: 2008/03/18 21:26:55 $
 */
public abstract class AbstractReportBuilder implements IReportBuilder, ReportConstants
{
	private Logger logger = Logger.getLogger(this.getClass());
	protected ReportHandler handler;
	
	protected abstract String getSelectClause();
	protected abstract String getJoinStatement();
	protected List data;  //the data after executing query.
	private IReport report = null;
	
	

	/**
	 * 
	 */
	public AbstractReportBuilder(ReportHandler handler)
	{
		this.handler = handler;
	}
	
	protected String buildWhereClause(List <SearchCriteria>criterias)
	{
		if(criterias == null || criterias.size() == 0)
			return "";
		
		StringBuffer query = new StringBuffer(" WHERE ");

		Iterator iterator = criterias.iterator();
		while(iterator.hasNext())
		{
			SearchCriteria criteria = (SearchCriteria) iterator.next();
			//a flag to indicate if there is start value
			boolean flagStart = false;  
			if(criteria.getOperator()!=null)
			{
				query.append(criteria.getName()).append(" ");
				query.append(criteria.getOperator()).append(" ? ");
				flagStart = true;
			}
			if(criteria.getEndOperator()!=null && criteria.getEndValue()!=null)
			{
				if(flagStart)
					query.append(" AND ");
				query.append(criteria.getName()).append(" ");;
				query.append(criteria.getEndOperator()).append(" ? ");
				//if there is a start value, insert AND key workd
			}
			
			if(iterator.hasNext())
				query.append(" AND ");  //currently only support AND
		}
		
		return query.toString();
	}
	
	
	public String buildQuery()
	{
		Class primaryBean = handler.getPrimaryBean();
		StringBuffer query = new StringBuffer();
		query.append(getSelectClause());  // attached SELECT clause
		if(!handler.isNativeQuery())
		{
			query.append(" FROM ").append(primaryBean.getSimpleName());
			query.append(" as ").append(primaryBean.getSimpleName().toLowerCase()); // add table alias
		}
		query.append(" ").append(getJoinStatement());
		query.append(buildWhereClause(handler.getCriterias()));
		
		if(handler.getGroupColumn() !=null)
		{
			query.append(" GROUP BY ").append(handler.getGroupColumn());
		}
		
		if(handler.getSortColumn() !=null)
		{
			query.append(" ORDER BY ").append(handler.getSortColumn());
		}
		
		return query.toString();
	}
	
	public void executeQuery()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		String queryStr = buildQuery();
		
		if(logger.isDebugEnabled())
		{
			if(handler.isNativeQuery())
				logger.debug("SQL: " + queryStr);
			else
				logger.debug("HQL: " + queryStr);
		}
		Query query = null;
		if (handler.isNativeQuery())
			//using naive SQL query
			query = session.createSQLQuery(queryStr);
		else
			query = session.createQuery(queryStr);
		int paramIndex = 0;
		
		//set other params before where clause
		paramIndex = setParameters(query, handler.getExtraParams(), paramIndex);
		//set the param for where clause
		paramIndex = setParameters(query, handler.getCriterias(), paramIndex);
		data = query.list();
		report = buildReport();
		session.getTransaction().commit();
	}
	
	private IReport buildReport()
	{
		IReport report = null;
		
		List reportData = prepareData(data);
		
		if(reportData == null)
			return null;
		
		if(ReportRenderEnum.TEXT_RENDER.equals(handler.getRenderType()))
		{
			logger.error("Not supported yet");
		}else if(ReportRenderEnum.SIMPLE_TABLE_RENDER.equals(handler.getRenderType()))
		{
			report = new SimpleTableReport(reportData , handler);
		}else if(ReportRenderEnum.EXCEL_RENDER.equals(handler.getRenderType()))
		{
			report = new MSExcelReport(reportData, handler);
		}else
		{
			logger.error("Report Render Type must be specificed using " +
					"reportHandler.setsetRenderType()");
		}
		
		return report;
	}
	
	public IReport getReport()
	{
		return report;
	}
	
	public int setParameters(Query query, List <SearchCriteria> criterias, int paramIndex)
	{
		if(criterias == null)
			return paramIndex;  //no criterias
		
		for(SearchCriteria criteria: criterias)
		{
			if(criteria.getDataType().equals(Date.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setDate(paramIndex++, (Date)criteria.getStartValue());
				
				if(criteria.getEndOperator()!=null)
					query.setDate(paramIndex++, (Date)criteria.getEndValue());
				
			}else if(criteria.getDataType().equals(Integer.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setInteger(paramIndex++, (Integer)criteria.getStartValue());
				if(criteria.getEndOperator()!=null && criteria.getEndValue()!= null)
					query.setInteger(paramIndex++, (Integer)criteria.getEndValue());
			}else if(criteria.getDataType().equals(Long.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setLong(paramIndex++, (Long)criteria.getStartValue());
				if(criteria.getEndOperator() != null && criteria.getEndValue() != null)
					query.setLong(paramIndex++, (Long) criteria.getEndValue());
			}else if(criteria.getDataType().equals(Double.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setDouble(paramIndex++, (Double)criteria.getStartValue());
				if(criteria.getEndOperator() != null && criteria.getEndValue() != null)
					query.setDouble(paramIndex++, (Double) criteria.getEndValue());
			}else if(criteria.getDataType().equals(Float.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setFloat(paramIndex++, (Float)criteria.getStartValue());
				
				if(criteria.getEndOperator() != null && criteria.getEndValue() != null)
					query.setFloat(paramIndex++, (Float) criteria.getEndValue());
			}else if(criteria.getDataType().equals(Calendar.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setCalendarDate(paramIndex++, (Calendar)criteria.getStartValue());
				if(criteria.getEndOperator() != null && criteria.getEndValue() != null)
					query.setCalendarDate(paramIndex++, (Calendar) criteria.getEndValue());
			}else if(criteria.getDataType().equals(String.class))
			{
				if(criteria.getOperator()!= null && criteria.getValue() != null)
					query.setString(paramIndex++, (String)criteria.getStartValue());
				if(criteria.getEndOperator() != null && criteria.getEndValue() != null)
					query.setString(paramIndex++, (String) criteria.getEndValue());
			}else
			{
				logger.error("Unsupported data type " + criteria.getDataType().getSimpleName());
			}
		}
		
		return paramIndex;
	}
	
	/**
	 * 
	 * @param resultSet - result after execution query.  It could a list of 
	 * object model or a scalar
	 * @return
	 */
	protected abstract List prepareData(List resultSet);


}
