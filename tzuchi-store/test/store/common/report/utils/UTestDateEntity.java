/* $Id: UTestDateEntity.java,v 1.1 2008/02/29 13:01:20 jlin Exp $
 * Created on Feb 20, 2008
 * 
 * $Log: UTestDateEntity.java,v $
 * Revision 1.1  2008/02/29 13:01:20  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.utils;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Test DateEntity class
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class UTestDateEntity
{
	private static DateEntity month = null;
	@BeforeClass
	public static void setUpTestData()
	{
		month = new DateEntity(2008, 2, 15); //luner year
	}
	
	@Test
	public void testGetMonthStartDate()
	{
		Date date = month.getMonthStartDate();
		System.out.println(date.toString());
	}
	
	@Test
	public void testMonthEndDate()
	{
		Date date = month.getMonthEndDate();
		System.out.println(date.toString());
	}
	
	@Test
	public void testGetStartDate()
	{
		Date date = month.getStartDate();
		System.out.println(date.toString());
	}
	
	@Test
	public void testGetEndDate()
	{
		Date date = month.getEndDate();
		System.out.println(date.toString());
	}
	
	
}
