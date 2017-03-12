package asset.datamodel;

import java.util.Date;

/**
 * @author Cheng-Hung Chou
 * @since 9/14/2005
 */
public class MemberHistory
{
   private long memberId;
   private Date checkoutDate;
   private Date checkinDate;
   private Date dueDate;
   private boolean isCheckedIn = false;
   
   private Asset asset = new Asset();
   
   /**
    * @return Returns the asset.
    */
   public Asset getAsset()
   {
      return asset;
   }
   
   /**
    * @return Returns the checkinDate.
    */
   public Date getCheckinDate()
   {
      return checkinDate;
   }
   
   /**
    * @param checkinDate The checkinDate to set.
    */
   public void setCheckinDate(Date checkinDate)
   {
      this.checkinDate = checkinDate;
   }
   
   /**
    * @return Returns the checkoutDate.
    */
   public Date getCheckoutDate()
   {
      return checkoutDate;
   }
   
   /**
    * @param checkoutDate The checkoutDate to set.
    */
   public void setCheckoutDate(Date checkoutDate)
   {
      this.checkoutDate = checkoutDate;
   }
   
   /**
    * @return Returns the dueDate.
    */
   public Date getDueDate()
   {
      return dueDate;
   }
   
   /**
    * @param dueDate The dueDate to set.
    */
   public void setDueDate(Date dueDate)
   {
      this.dueDate = dueDate;
   }
   
   /**
    * @return Returns the memberId.
    */
   public long getMemberId()
   {
      return memberId;
   }
   
   /**
    * @param memberId The memberId to set.
    */
   public void setMemberId(long memberId)
   {
      this.memberId = memberId;
   }

   /**
    * @return Returns the isCheckedIn.
    */
   public boolean isCheckedIn()
   {
      return isCheckedIn;
   }

   /**
    * @param isCheckedIn The isCheckedIn to set.
    */
   public void setCheckedIn(boolean isCheckedIn)
   {
      this.isCheckedIn = isCheckedIn;
   }
}
