/* $Id: ItemHistory.java,v 1.4 2008/04/07 05:29:22 joelchou Exp $ */
package store.datamodel;

import java.util.Date;

public class ItemHistory
{
   private int historyId;
   private String modifiedBy;
   private Date modifiedDate;
   private int updatedQuantity;
   private String invoiceNumber;
   private Date invoiceDate;
   private ItemInfo itemInfo;
   
   public String getModifiedBy()
   {
      return modifiedBy;
   }
   
   public void setModifiedBy(String username)
   {
      this.modifiedBy = username;
   }
   
   public int getHistoryId()
   {
      return historyId;
   }
   
   public void setHistoryId(int historyId)
   {
      this.historyId = historyId;
   }
   
   public Date getModifiedDate()
   {
      return modifiedDate;
   }
   
   public void setModifiedDate(Date modifiedDate)
   {
      this.modifiedDate = modifiedDate;
   }
      
   public ItemInfo getItemInfo()
   {
      return itemInfo;
   }
   
   public void setItemInfo(ItemInfo itemInfo)
   {
      this.itemInfo = itemInfo;
   }

   /**
    * Gets quantity of an item got added/removed from this change.
    * 
    * @return
    */
   public int getUpdatedQuantity()
   {
      return updatedQuantity;
   }

   public void setUpdatedQuantity(int updatedQuantity)
   {
      this.updatedQuantity = updatedQuantity;
   }

   public Date getInvoiceDate()
   {
      return invoiceDate;
   }

   public void setInvoiceDate(Date invoiceDate)
   {
      this.invoiceDate = invoiceDate;
   }

   public String getInvoiceNumber()
   {
      return invoiceNumber;
   }

   public void setInvoiceNumber(String invoiceNumber)
   {
      this.invoiceNumber = invoiceNumber;
   } 
}
