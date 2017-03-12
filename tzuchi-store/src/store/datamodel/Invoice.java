/* $Id: Invoice.java,v 1.4 2008/04/07 05:29:22 joelchou Exp $ */
package store.datamodel;

import java.util.Set;

public class Invoice
{
   private int invoiceId;
   private String invoiceNumber;
   private String handledBy;  // username   
   private String purchasedBy = "";
   private java.util.Date invoiceDate;   
   private double total;
   private Set<InvoiceItem> items;
   
   public int getInvoiceId()
   {
      return invoiceId;
   }
   
   public void setInvoiceId(int invoiceId)
   {
      this.invoiceId = invoiceId;
   }   
   
   public String getInvoiceNumber()
   {
      return invoiceNumber;
   }
   
   public void setInvoiceNumber(String invoiceNumber)
   {
      this.invoiceNumber = invoiceNumber;
   }   
   
   public String getHandledBy()
   {
      return handledBy;
   }
   
   public void setHandledBy(String handledBy)
   {
      this.handledBy = handledBy;
   }
         
   public double getTotal()
   {
      return total;
   }
   
   public void setTotal(double total)
   {
      this.total = total;
   }   
   
   public String getPurchasedBy()
   {
      return purchasedBy;
   }
   
   public void setPurchasedBy(String purchasedBy)
   {
      this.purchasedBy = purchasedBy;
   }
   
   public java.util.Date getInvoiceDate()
   {
      return invoiceDate;
   }
   
   public void setInvoiceDate(java.util.Date invoiceDate)
   {
      this.invoiceDate = invoiceDate;
   }

   public Set<InvoiceItem> getItems()
   {
      return items;
   }

   public void setItems(Set<InvoiceItem> items)
   {
      this.items = items;
   }
}
