/* $Id: UTestReportManager.java,v 1.3 2008/03/18 21:26:55 jlin Exp $
 * Created on Mar 6, 2008
 * 
 * $Log: UTestReportManager.java,v $
 * Revision 1.3  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 * Revision 1.2  2008/03/13 20:29:18  jlin
 * add simple asset report
 *
 * Revision 1.1  2008/03/06 23:12:46  jlin
 * add new feature:  Sale Report by sale handlers
 *
 */
package store.manager;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import store.common.report.render.IReport;
import store.common.report.utils.DateEntity;
import store.datamodel.InvoiceItem;
import store.utils.HibernateUtil;
import static org.junit.Assert.*;

public class UTestReportManager
{
	@Test
	public void getSalesPersonByMonth()
	{
		ReportsManager manager = ReportsManager.getInstance();
		
		List list = manager.getListOfSaleHandlersByMonth(new DateEntity(2008, 2));
		
		assertNotNull(list);
	}
	
	/**
	 * work around a outer join on a subquery
	 *
	 */
	@Test
	public void HQL_test()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		DateEntity feb = new DateEntity(2008, 1);
		DateEntity march = new DateEntity(2008,2);
		
		Query query = session.createQuery("SELECT iteminfo.itemId, iteminfo.quantity, sum(itemhistory.updatedQuantity) , sum(invoiceitem.quantity) " +
				"FROM ItemInfo iteminfo " +
				"join iteminfo.typeInfo typeinfo " +
				"left join iteminfo.history itemhistory with itemhistory.modifiedDate >=? and itemhistory.modifiedDate <=?, " +
				"InvoiceItem invoiceitem " +
				"join invoiceitem.invoice invoice with invoice.invoiceDate >=? and invoice.invoiceDate <=? " +
				"right join invoiceitem.itemInfo iteminfo2 " +
				"where  iteminfo = iteminfo2 " +
				"group by iteminfo.itemId " +
				"order by iteminfo.itemId").
				setDate(0, feb.getMonthStartDate()).
				setDate(1, feb.getMonthEndDate()).
				setDate(2, feb.getMonthStartDate()).
				setDate(3, feb.getMonthEndDate());
		
		List list = query.list();
		if(list != null)
		{
			Iterator iterator = list.iterator();
			while(iterator.hasNext())
			{
				Object [] row = (Object [])iterator.next();
				System.out.printf("%s\t%s\t%d\t%d\n", row[0], row[1], row[2], row[3]);
			}
		}else
			fail();
		
		session.getTransaction().commit();
		
	}
	
	/**
	 * Work around for outer join for a subquery
	 *
	 */
	@Test
	public void HQL_test2()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		DateEntity feb = new DateEntity(2008, 1);
		DateEntity march = new DateEntity(2008,2);
		
		Query query = session.createQuery("SELECT iteminfo.itemId, iteminfo.quantity, sum(invoiceitem.quantity) " +
				"FROM ItemInfo iteminfo  " +
				"join iteminfo.typeInfo typeinfo, " +
				"InvoiceItem invoiceitem " +
				"join invoiceitem.invoice invoice with invoice.invoiceDate >=? and invoice.invoiceDate <=? " +
				"right join invoiceitem.itemInfo iteminfo2 " +
				"where  iteminfo = iteminfo2 " +
				"group by iteminfo.itemId " +
				"order by iteminfo.itemId")
				.setDate(0, feb.getMonthStartDate())
				.setDate(1, feb.getMonthEndDate());
		
		List list = query.list();
		if(list != null)
		{
			Iterator iterator = list.iterator();
			while(iterator.hasNext())
			{
				Object [] row = (Object [])iterator.next();
				System.out.printf("%s\t%s\t%d\n", row[0], row[1], row[2]);
			}
		}else
			fail();
		
		session.getTransaction().commit();
		
	}
	
	@Test
	public void HQL_test3()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		DateEntity feb = new DateEntity(2008, 1);
		DateEntity march = new DateEntity(2008,2);
		
		Query query = session.createQuery("SELECT invoiceitem.invoiceItemId, invoiceitem.invoice.invoiceDate FROM InvoiceItem invoiceitem");
		
		List list = query.list();
		if(list != null)
		{
			Iterator iterator = list.iterator();
			while(iterator.hasNext())
			{
				Object [] item = (Object []) iterator.next();
				System.out.printf("%s\t%s\n", item[0], item[1]);
			}
		}else
			fail();
		
		session.getTransaction().commit();
		
	}
	
	@Test
	public void native_SQL_test()
	{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		DateEntity jan = new DateEntity(2008, 1);
		DateEntity feb = new DateEntity(2008,2);
		
		Query query = session.createSQLQuery("SELECT typeinfo.type_name, iteminfo.item_id, iteminfo.quantity, history1.u_quanity 1_quantity, history1.invoice_no 1invocie_no, history2.u_quanity 2_quantity, history2.invoice_no 2invocie_no, invoiceitem1.quantity 1i_quantity, invoiceitem2.quantity 2i_quantity " +
				"FROM item_info iteminfo " +
				"left join (SELECT item_id, sum(updated_quantity) u_quanity, concat(invoice_number, ' ') invoice_no FROM item_history where mod_date >=? and mod_date <=? group by item_id) history1 on iteminfo.item_id = history1.item_id " +
				"left join (SELECT item_id, sum(updated_quantity) u_quanity, concat(invoice_number, ' ') invoice_no FROM item_history where mod_date >=? and mod_date <=? group by item_id) history2 on iteminfo.item_id = history2.item_id " +
				"left join (SELECT item_id, sum(quantity) quantity from invoice_item, invoice where invoice.invoice_id = invoice_item.invoice_id and invoice.invoice_date >=? and invoice.invoice_date <=?  group by item_id) invoiceitem1 on iteminfo.item_id = invoiceitem1.item_id " +
				"left join (SELECT item_id, sum(quantity) quantity from invoice_item, invoice where invoice.invoice_id = invoice_item.invoice_id and invoice.invoice_date >=? and invoice.invoice_date <=?  group by item_id) invoiceitem2 on iteminfo.item_id = invoiceitem2.item_id " +
				"join item_type_info typeinfo on iteminfo.type_id = typeinfo.type_id " +
				"where iteminfo.type_id = typeinfo.type_id " +
				"and (history1.u_quanity is not null or history2.u_quanity is not null or invoiceitem1.quantity is not null or invoiceitem2.quantity is not null) " +
				"order by iteminfo.type_id, iteminfo.item_id").
				setDate(0, jan.getMonthStartDate()).
				setDate(1, jan.getMonthEndDate()).
				setDate(2, feb.getMonthStartDate()).
				setDate(3, feb.getMonthEndDate()).
				setDate(4, jan.getMonthStartDate()).
				setDate(5, jan.getMonthEndDate()).
				setDate(6, feb.getMonthStartDate()).
				setDate(7, feb.getMonthEndDate());
		
		List list = query.list();
		if(list != null)
		{
			Iterator iterator = list.iterator();
			while(iterator.hasNext())
			{
				Object [] row = (Object [])iterator.next();
				System.out.printf("%s\t%s\t%d\t%s\t%s\t%s\t%s\t%s\t%s\n", row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8]);
			}
		}else
			fail();
		
		session.getTransaction().commit();
	}
	
	@Test
	public void getAssetTransactionReport() throws Exception
	{
		ReportsManager manager = ReportsManager.getInstance();
		
		manager.renderAssetTransactionReportByMonth("C:\\temp\\asset.xls", new DateEntity(2008, 2));
		
	}
}
