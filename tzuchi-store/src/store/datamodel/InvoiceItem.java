/* $Id: InvoiceItem.java,v 1.3 2008/04/07 05:29:22 joelchou Exp $ */
package store.datamodel;

public class InvoiceItem
{
   private int invoiceItemId;
   private int quantity = 1;   
   private double price;
   private double total;  // sub-total
   private Invoice invoice;
   private ItemInfo itemInfo;
   
   public int getInvoiceItemId()
   {
      return invoiceItemId;
   }
   
   public void setInvoiceItemId(int invoiceItemId)
   {
      this.invoiceItemId = invoiceItemId;
   }   
   
   public Invoice getInvoice()
   {
      return invoice;
   }
   
   public void setInvoice(Invoice invoice)
   {
      this.invoice = invoice;
   }   
   
   public ItemInfo getItemInfo()
   {
      return itemInfo;
   }
   
   public void setItemInfo(ItemInfo itemInfo)
   {
      this.itemInfo = itemInfo;
   }
   
   public double getPrice()
   {
      return price;
   }
   
   public void setPrice(double price)
   {
      this.price = price;
   }
   
   public double getTotal()
   {
      return total;
   }
   
   public void setTotal(double total)
   {
      this.total = total;
   }   
   
   public int getQuantity()
   {
      return quantity;
   }
   
   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }
}
