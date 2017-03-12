/* $Id: UTestClassType.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 18, 2008
 * 
 * $Log: UTestClassType.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report;


import org.junit.Test;
import static org.junit.Assert.*;
import store.datamodel.Invoice;


public class UTestClassType
{
	@Test
	public void classTypeTest()
	{
		Object obj = new Invoice();
		assertEquals("Invoice", obj.getClass().getSimpleName());
	}
	
	@Test
	public void classEqual()
	{
		Class myClass = new Invoice().getClass();
		
		assertTrue(Invoice.class.equals(myClass));
	}
}
