package asset.datamodel;

import java.util.Date;

/**
 * This is for system data.
 * 
 * @author Cheng-Hung Chou
 * @since 10/28/2005
 */
public class SystemData
{
   private Date lastScanDate;
   
   /**
    * @return Returns the lastScanDate.
    */
   public Date getLastScanDate()
   {
      return lastScanDate;
   }
   
   /**
    * @param lastScanDate The lastScanDate to set.
    */
   public void setLastScanDate(Date lastScanDate)
   {
      this.lastScanDate = lastScanDate;
   }   
}
