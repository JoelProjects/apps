/* $Id: ItemInfo.java,v 1.7 2008/04/07 05:29:22 joelchou Exp $ */
package store.datamodel;

import java.io.Serializable;
import java.util.Set;

public class ItemInfo implements Serializable
{
   private int itemId;
   private String itemNo;
   private String barcode;
   private String name;
   private double price;
   private int quantity; // current inventory
   private int addedQuantity; // quantity in the shopping cart 
   private ItemTypeInfo typeInfo;
   private Set history;
   private Set invoiceItem;
   
   public ItemInfo()
   {
   }   
   
   public String getBarcode()
   {
      return barcode;
   }
   
   public void setBarcode(String barcode)
   {
      this.barcode = barcode;
   }
   
   public int getItemId()
   {
      return itemId;
   }
   
   public void setItemId(int itemId)
   {
      this.itemId = itemId;
   }
   
   public String getItemNo()
   {
      return itemNo;
   }
   
   public void setItemNo(String itemNo)
   {
      this.itemNo = itemNo;
   }
   
   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }
   
   public double getPrice()
   {
      return price;
   }
   
   public void setPrice(double price)
   {
      this.price = price;
   }
   
   /**
    * Gets quantity in stock for an item.
    * 
    * @return
    */
   public int getQuantity()
   {
      return quantity;
   }
   
   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }
   
   public int getAddedQuantity()
   {
      return addedQuantity;
   }
   
   public void setAddedQuantity(int addedQuantity)
   {
      this.addedQuantity = addedQuantity;
   }   
   
   public ItemTypeInfo getTypeInfo()
   {
      return typeInfo;
   }
   
   public void setTypeInfo(ItemTypeInfo type)
   {
      this.typeInfo = type;
   }
   
   public boolean equals(Object obj)
   {
      boolean flag = false;
      ItemInfo theObj = (ItemInfo)obj;
      if(theObj != null)
         flag = theObj.getItemId() == itemId;
      
      return flag;
   }
   
   public int hashCode() 
   {
      return itemId;
   }

   public Set getHistory()
   {
	   return history;
   }

   public void setHistory(Set history)
   {
	   this.history = history;
   }

   public Set getInvoiceItem()
   {
	   return invoiceItem;
   }

   public void setInvoiceItem(Set invoiceItem)
   {
	   this.invoiceItem = invoiceItem;
   }  
}
