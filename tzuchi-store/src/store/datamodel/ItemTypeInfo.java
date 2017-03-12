/* $Id: ItemTypeInfo.java,v 1.3 2008/04/07 05:29:22 joelchou Exp $ */
package store.datamodel;

import java.io.Serializable;

/**
 * This class represents item type.
 * 
 * @author Cheng-Hung Chou
 * @since 2/21/2007
 *
 */
public class ItemTypeInfo implements Serializable
{
   private int typeId;
   private String typeName;
   
   public ItemTypeInfo()
   {
   }

   public ItemTypeInfo(int typeId, String typeName)
   {
      this.typeId = typeId;
      this.typeName = typeName;
   }   
   
   public int getTypeId()
   {
      return typeId;
   }

   public void setTypeId(int typeId)
   {
      this.typeId = typeId;
   }

   public String getTypeName()
   {
      return typeName;
   }

   public void setTypeName(String typeName)
   {
      this.typeName = typeName;
   }
   
   public boolean equals(Object obj)
   {
      boolean flag = false;
      ItemTypeInfo theObj = (ItemTypeInfo)obj;
      if(theObj != null)
         flag = theObj.getTypeId() == typeId;
      
      return flag;
   }
   
   public int hashCode() 
   {
      return getTypeName().hashCode();
   }
   
   public String toString()
   {
      return typeName;
   }
}
