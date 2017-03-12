/* $Id: UTestPropertyHelper.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 14, 2008
 * 
 * $Log: UTestPropertyHelper.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import store.common.report.model.SearchCriteria;
import store.common.report.model.SearchCriteria.OperatorEnum;
import store.common.report.utils.PropertyHelper;
import store.datamodel.Invoice;
import store.datamodel.InvoiceItem;
import store.datamodel.ItemHistory;
import store.datamodel.ItemInfo;


public class UTestPropertyHelper
{
	@Test
	public void testOneClass()
	{
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		helper.printPropertyMap();
	}
	
	@Test
	public void testMultipleClasses()
	{
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		helper.addBean(InvoiceItem.class);
		helper.addBean(ItemInfo.class);
		helper.addBean(ItemHistory.class);
		helper.printPropertyMap();
	}
	
	@Test(expected = UnmatchedPropertyException.class)
	public void testValidateProperty() throws Exception
	{
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		Set propertySet = new HashSet();
		propertySet.add("invoice.invoiceId");
		propertySet.add("invoice.invoiceNumber");
		propertySet.add("invoice.noneExit");
		
		try
		{
			helper.validateProperies(propertySet);
		}catch(UnmatchedPropertyException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyException(e);
		}
	}
	
	@Test(expected = UnmatchedPropertyTypeException.class)
	public void testValidatePropertyType() throws Exception
	{
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		Map typeMap = new HashMap();
		
		typeMap.put("invoice.invoiceId", new Integer(15));
		typeMap.put("invoice.invoiceNumber", "asdfas");
		typeMap.put("invoice.invoiceDate",  new Date());
		typeMap.put("invoice.total", new Integer(20));
		
		try
		{
			helper.validatePropertType(typeMap);

		}catch(UnmatchedPropertyException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyException(e);
		}catch(UnmatchedPropertyTypeException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyTypeException(e);
		}
	}
	
	@Test(expected = UnmatchedPropertyException.class)
	public void testValidatePropertyType2() throws Exception
	{
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		Map typeMap = new HashMap();
		
		typeMap.put("invoice.invoiceId", new Integer(15));
		typeMap.put("invoice.invoiceNumber", "asdfasd");
		typeMap.put("invoice.invoiceDate",  new Date());
		typeMap.put("invoice.nonexist", new Integer(20));
		
		try
		{
			helper.validatePropertType(typeMap);

		}catch(UnmatchedPropertyException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyException(e);
		}catch(UnmatchedPropertyTypeException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyTypeException(e);
		}
	}
	
	@Test(expected = UnmatchedPropertyException.class) 
	public void testValidateSearchCriteria() throws Exception
	{
		SearchCriteria sc1 = new SearchCriteria("invoice.invoiceId", OperatorEnum.EQUAL,
				new Integer(15));
		
		SearchCriteria sc2 = new SearchCriteria("invoice.invoiceNumber", OperatorEnum.EQUAL,
				"ddrgwref");
		
		SearchCriteria sc3 = new SearchCriteria("invoice.invoiceDate", OperatorEnum.EQUAL,
				new Date());
		
		SearchCriteria sc4 = new SearchCriteria("invoice.nonexist", OperatorEnum.EQUAL,
				new Integer(15));
		
		List scList = new ArrayList();
		scList.add(sc1);
		scList.add(sc2);
		scList.add(sc3);
		scList.add(sc4);
		
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		
		try
		{
			helper.validateSearchCriteria(scList);

		}catch(UnmatchedPropertyException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyException(e);
		}catch(UnmatchedPropertyTypeException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyTypeException(e);
		}
	}
	
	@Test(expected = UnmatchedPropertyTypeException.class) 
	public void testValidateSearchCriteria2() throws Exception
	{
		SearchCriteria sc1 = new SearchCriteria("invoice.invoiceId", OperatorEnum.EQUAL,
				new Integer(15));
		
		SearchCriteria sc2 = new SearchCriteria("invoice.invoiceNumber", OperatorEnum.EQUAL,
				"ddrgwref");
		
		SearchCriteria sc3 = new SearchCriteria("invoice.invoiceDate", OperatorEnum.EQUAL,
				new Date());
		
		SearchCriteria sc4 = new SearchCriteria("invoice.nonexist", OperatorEnum.EQUAL,
				new Integer(15));
		
		List scList = new ArrayList();
		scList.add(sc1);
		scList.add(sc2);
		scList.add(sc3);
		scList.add(sc4);
		
		PropertyHelper helper = new PropertyHelper();
		helper.addBean(Invoice.class);
		
		try
		{
			helper.validateSearchCriteria(scList);

		}catch(UnmatchedPropertyException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyException(e);
		}catch(UnmatchedPropertyTypeException e)
		{
			System.out.println(e.getMessage());
			throw new UnmatchedPropertyTypeException(e);
		}
	}
	
	
}
