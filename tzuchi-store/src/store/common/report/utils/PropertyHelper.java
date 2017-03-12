/* $Id: PropertyHelper.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 14, 2008
 * 
 * $Log: PropertyHelper.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;

import store.common.report.UnmatchedPropertyException;
import store.common.report.UnmatchedPropertyTypeException;
import store.common.report.model.ReportColumn;
import store.common.report.model.SearchCriteria;
import store.utils.HibernateUtil;

/**
 * PropertyHelper contains the all of the property name and property types with
 * for multiple beans.  It provide validation method for properties property types
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class PropertyHelper
{
	private Logger logger = Logger.getLogger(this.getClass()); 
	private List beanClasses;
	private Map propertyTypeMap;

	/**
	 * 
	 */
	public PropertyHelper()
	{
		beanClasses = new ArrayList();
		propertyTypeMap = new HashMap();
	}
	
	public void addBean(Class bean)
	{
		beanClasses.add(bean);
		addTypeMap(bean);
	}
	
	private void addTypeMap(Class bean)
	{
		Session session = HibernateUtil.getSession();
		String beanName = bean.getSimpleName().toLowerCase();
		
		ClassMetadata classMeta = session.getSessionFactory().getClassMetadata(bean);
		String [] properties = classMeta.getPropertyNames();
		
		for(String name: properties)
		{
			try
			{
				PropertyDescriptor pd = new PropertyDescriptor(name, bean);
				Class type = pd.getPropertyType();
				propertyTypeMap.put(beanName+"."+ name, type);
			}catch(IntrospectionException e)
			{
				logger.error(e);
			}
		}
		
		//add identifier
		
		String idProperty = classMeta.getIdentifierPropertyName();
		try
		{
			PropertyDescriptor pd = new PropertyDescriptor(idProperty, bean);
			Class type = pd.getPropertyType();
			//if this is primitive type we wrap with Object class
			if(type.equals(int.class))
				type = Integer.class;
			else if(type.equals(long.class))
				type = Long.class;
			propertyTypeMap.put(beanName+"."+ idProperty, type);
		}catch(IntrospectionException e)
		{
			logger.error(e);
		}
			
	}
	
	public void printPropertyMap()
	{
		Set <Map.Entry<String, Class>>entries = propertyTypeMap.entrySet();
		
		for(Map.Entry<String, Class> entry: entries)
		{
			System.out.println(entry.getKey() + " is type of " + entry.getValue().getName());
		}
	}
	
	public void validateProperty(String propertyName)
		throws UnmatchedPropertyException
	{
		if(!propertyTypeMap.containsKey(propertyName))
			throw new UnmatchedPropertyException(propertyName + " does not matched");
	}
	
	public void validateProperies(Set source)
		throws UnmatchedPropertyException
	{
		for(Object name : source)
		{
			if(!propertyTypeMap.containsKey(name))
				throw new UnmatchedPropertyException(name + " does not matched");
		}
	}
	
	public void validateReportColumn(Map<String, ReportColumn> reportColumn)
		throws UnmatchedPropertyException
	{
		Set <Map.Entry<String, ReportColumn>> entries = reportColumn.entrySet();
		
		for(Map.Entry<String, ReportColumn> entry: entries)
		{
			ReportColumn column = entry.getValue();
			if(!column.isDummyColumn())  //exclude dummy column
			{
				Class myClass = (Class)propertyTypeMap.get(column.getColumnName());
				
				if(myClass == null)
				{
					throw new UnmatchedPropertyException(entry.getKey() + " does not matched");
				}
			}
		}
	}
	
	
	/**
	 * validate the Property name and Data type 
	 * @param propertyMap
	 * @throws UnmatchedPropertyException
	 * @throws UnmatchedPropertyTypeException
	 */
	public void validatePropertType(Map propertyMap)
	throws UnmatchedPropertyException, UnmatchedPropertyTypeException
	{
		Set <Map.Entry<String, Object>>entries = propertyMap.entrySet();
		
		for (Map.Entry <String, Object> entry: entries)
		{
			Class myClass = (Class)propertyTypeMap.get(entry.getKey());
			if (myClass != null)
			{
				if(!myClass.equals(entry.getValue().getClass()))
				{
					//when type is not equal
					throw new UnmatchedPropertyTypeException("Type of property " 
							+ entry.getKey() + " does not matched. " + 
							entry.getValue().getClass().getSimpleName()+ " is observed" );
				}
			}else
			{
				throw new UnmatchedPropertyException(entry.getKey() + " does not matched");
			}
		}
	}
	
	/**
	 * Validate the name and the data type of the search criterias from a List
	 * of SearchCriteria object
	 * @param searchCriterias
	 * @throws UnmatchedPropertyException
	 * @throws UnmatchedPropertyTypeException
	 */
	public void validateSearchCriteria(List<SearchCriteria> searchCriterias)
	throws UnmatchedPropertyException, UnmatchedPropertyTypeException
	{
		for(SearchCriteria critera: searchCriterias)
		{
			Class myClass = (Class)propertyTypeMap.get(critera.getName());
			if(myClass !=null)
			{
				if(!myClass.equals(critera.getDataType()))
				{
					//when type is not equal
					throw new UnmatchedPropertyTypeException("Type of property " 
							+ critera.getName() + " does not matched. " + 
							critera.getDataType().getSimpleName()+ " is observed" );
				}	
			}else
			{
				throw new UnmatchedPropertyException(critera.getName() + " does not matched");
			}

		}
	}
	
	public Class getPropertyType(String propertyName)
	{
		return (Class)propertyTypeMap.get(propertyName);
	}
	
	

}
