package asset.datamodel;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/6/2005
 */
public class Asset
{
   private int assetId;
   private String assetCode;
   private String assetBarcode;  // our own barcode
   private String vendorBarcode;  // original barcode on the item
   private String name;
   private String author;
   private String publishingCompany;
   private java.util.Date publishedDate;
   private int assetCategoryId;
   private int assetTypeId;
   private java.util.Date entryDate;
   private int assetStatusId;
   private long userId;
   private java.util.Date statusModDate;
   
   /**
    * @return Returns the assetCategoryId.
    */
   public int getAssetCategoryId()
   {
      return assetCategoryId;
   }
   
   /**
    * @param assetCategoryId The assetCategoryId to set.
    */
   public void setAssetCategoryId(int assetCategoryId)
   {
      this.assetCategoryId = assetCategoryId;
   }
   
   /**
    * @return Returns the assetCode.
    */
   public String getAssetCode()
   {
      return assetCode;
   }
   
   /**
    * @param assetCode The assetCode to set.
    */
   public void setAssetCode(String assetCode)
   {
      this.assetCode = assetCode;
   }
   
   /**
    * @return Returns the assetId.
    */
   public int getAssetId()
   {
      return assetId;
   }
   
   /**
    * @param assetId The assetId to set.
    */
   public void setAssetId(int assetId)
   {
      this.assetId = assetId;
   }
   
   /**
    * @return Returns the assetStatusId.
    */
   public int getAssetStatusId()
   {
      return assetStatusId;
   }
   
   /**
    * @param assetStatusId The assetStatusId to set.
    */
   public void setAssetStatusId(int assetStatusId)
   {
      this.assetStatusId = assetStatusId;
   }
   
   /**
    * @return Returns the assetTypeId.
    */
   public int getAssetTypeId()
   {
      return assetTypeId;
   }
   
   /**
    * @param assetTypeId The assetTypeId to set.
    */
   public void setAssetTypeId(int assetTypeId)
   {
      this.assetTypeId = assetTypeId;
   }
   
   /**
    * @return Returns the author.
    */
   public String getAuthor()
   {
      return author;
   }
   
   /**
    * @param author The author to set.
    */
   public void setAuthor(String author)
   {
      this.author = author;
   }
   
   /**
    * @return Returns the assetBarcode.
    */
   public String getAssetBarcode()
   {
      return assetBarcode;
   }
   
   /**
    * @param assetBarcode The assetBarcode to set.
    */
   public void setAssetBarcode(String assetBarcode)
   {
      this.assetBarcode = assetBarcode;
   }
   
   /**
    * @return Returns the vendorBarcode.
    */
   public String getVendorBarcode()
   {
      return vendorBarcode;
   }
   
   /**
    * @param vendorBarcode The vendorBarcode to set.
    */
   public void setVendorBarcode(String vendorBarcode)
   {
      this.vendorBarcode = vendorBarcode;
   }   
   
   /**
    * @return Returns the entryDate.
    */
   public java.util.Date getEntryDate()
   {
      return entryDate;
   }
   
   /**
    * @param entryDate The entryDate to set.
    */
   public void setEntryDate(java.util.Date entryDate)
   {
      this.entryDate = entryDate;
   }
   
   /**
    * @return Returns the name.
    */
   public String getName()
   {
      return name;
   }
   
   /**
    * @param name The name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }
   
   /**
    * @return Returns the publishedDate.
    */
   public java.util.Date getPublishedDate()
   {
      return publishedDate;
   }
   
   /**
    * @param publishedDate The publishedDate to set.
    */
   public void setPublishedDate(java.util.Date publishedDate)
   {
      this.publishedDate = publishedDate;
   }
   
   /**
    * @return Returns the publishingCompany.
    */
   public String getPublishingCompany()
   {
      return publishingCompany;
   }
   
   /**
    * @param publishingCompany The publishingCompany to set.
    */
   public void setPublishingCompany(String publishingCompany)
   {
      this.publishingCompany = publishingCompany;
   }
   
   /**
    * @return Returns the statusModDate.
    */
   public java.util.Date getStatusModDate()
   {
      return statusModDate;
   }
   
   /**
    * @param statusModDate The statusModDate to set.
    */
   public void setStatusModDate(java.util.Date statusModDate)
   {
      this.statusModDate = statusModDate;
   }
   
   /**
    * @return Returns the userId.
    */
   public long getUserId()
   {
      return userId;
   }
   
   /**
    * @param userId The userId to set.
    */
   public void setUserId(long userId)
   {
      this.userId = userId;
   }   
}
