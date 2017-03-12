/* $Id: TransactionManager.java,v 1.6 2008/04/07 05:29:22 joelchou Exp $ */
package store.manager;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.datamodel.Invoice;
import store.datamodel.InvoiceItem;
import store.datamodel.ItemInfo;
import store.utils.HibernateUtil;

/**
 * This is a manager class for user transactions.
 * 
 * @author Cheng-Hung Chou
 * @since 5/4/2007
 *
 */
public class TransactionManager
{
   private static Logger logger = Logger.getLogger(TransactionManager.class);
   
   public static void commit(String username, String purchaser, double total,
      Map items, Date processedDate)
   {
      Session session = HibernateUtil.getSession();
      try
      {
         session.beginTransaction();
         Date now = null;
         if(processedDate != null)
            now = processedDate;  // for batch processing
         else
            now = new Date();
         String transNo = Utility.getLongDateTimeStr();
         
         // invoice
         Invoice invoice = new Invoice();
         invoice.setHandledBy(username);
         invoice.setPurchasedBy(purchaser);    
         invoice.setInvoiceDate(now);
         invoice.setInvoiceNumber(transNo); 
         invoice.setTotal(total);
         session.save(invoice);
         
         Query update = session.createQuery("UPDATE ItemInfo SET quantity=quantity-:purchased WHERE barcode=:barcode");
         for(Iterator<Map.Entry<String, ItemInfo>> it = items.entrySet().iterator(); 
            it.hasNext(); )
         {
            Map.Entry<String, ItemInfo> en = it.next();
            ItemInfo item = en.getValue();
            
            // update quantity of in-stock item
            update.setParameter("purchased", item.getAddedQuantity());
            update.setParameter("barcode", en.getKey());
            update.executeUpdate();
            
            // added to invoice history
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(invoice);
            invoiceItem.setItemInfo(item);
            invoiceItem.setQuantity(item.getAddedQuantity());
            invoiceItem.setPrice(item.getPrice());
            invoiceItem.setTotal(item.getAddedQuantity()*item.getPrice());

            session.save(invoiceItem);          
         }
                 
         session.getTransaction().commit();
      }
      catch(Exception e)
      {
         session.getTransaction().rollback();
         logger.error(e);
      }       
   }     
}
