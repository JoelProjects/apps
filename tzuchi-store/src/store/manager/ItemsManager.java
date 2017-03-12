/* $Id: ItemsManager.java,v 1.5 2008/04/07 05:29:22 joelchou Exp $ */
package store.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.datamodel.ItemInfo;
import store.datamodel.ItemTypeInfo;
import store.ui.ItemsManagementPanel;
import store.utils.HibernateUtil;

/**
 * This is a manager class for items.
 * 
 * @author Cheng-Hung Chou
 * @since 2/22/2007
 *
 */
public class ItemsManager
{  
   private static Logger logger = Logger.getLogger(ItemsManager.class);
   
   public static ItemInfo getItem(String barcode)
   {
      Session session = HibernateUtil.getSession();
      ItemInfo item = null;
      try
      {
         session.beginTransaction();
         Query query = session.createQuery("FROM ItemInfo WHERE barcode=:barcode");
         query.setParameter("barcode", barcode);
         Iterator it = query.list().iterator();
         if(it.hasNext())
         {
            item = (ItemInfo)it.next();
         }
         
         session.getTransaction().commit();
      }
      catch(Exception e)
      {
         session.getTransaction().rollback();
         logger.error(e);
      }       
      
      return item;
   }   
   
   public static boolean barcodeExists(String barcode)
   {
      boolean existed = false;
      ItemInfo item = getItem(barcode);
      if(item != null)
         existed = true;
      
      return existed;
   }
   
   public static void refreshItemsTable(JTable itemsTable, 
      String name, ItemTypeInfo type)
   {
      Session session = HibernateUtil.getSession();
      try
      {
         session.beginTransaction();
         StringBuffer queryStr = new StringBuffer("FROM ItemInfo ");
         boolean isFirst = true;
         if(!Utility.isEmpty(name))
         {
            name = name.trim();
            queryStr.append("WHERE (name LIKE '%").append(name).append("%'"); 
            queryStr.append(" OR barcode LIKE '%").append(name).append("%')");
            isFirst = false;
         }
         if(type != null && type.getTypeId() > 0)
         {
            if(isFirst)
               queryStr.append("WHERE typeInfo=").append(type.getTypeId());
            else
               queryStr.append(" AND typeInfo=").append(type.getTypeId());               
         }
         Query query = session.createQuery(queryStr.toString());
         Iterator<ItemInfo> it = query.list().iterator();
         // clear table           
         DefaultTableModel tableModel = (DefaultTableModel)itemsTable.getModel();
         tableModel.setRowCount(0);         
         while(it.hasNext())
         {
            ItemInfo item = (ItemInfo)it.next();
            tableModel.addRow(ItemsManagementPanel.getTableRow(item)); 
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
