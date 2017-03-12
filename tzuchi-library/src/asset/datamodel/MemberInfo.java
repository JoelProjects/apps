package asset.datamodel;

import java.util.List;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/27/2005
 */
public class MemberInfo
{
   private long memberId;
   private String name;
   private String chineseName;
   private String homePhone;
   private String cellPhone;
   private String address;
   private String email;
   private int memberTypeId;
   private int libraryId;  // a sequential number (just in case we need this in the future) 
   private List<MemberHistory> checkoutAssets;
   
   /**
    * @return Returns the address.
    */
   public String getAddress()
   {
      return address;
   }
   
   /**
    * @param address The address to set.
    */
   public void setAddress(String address)
   {
      this.address = address;
   }
   
   /**
    * @return Returns the cellPhone.
    */
   public String getCellPhone()
   {
      return cellPhone;
   }
   
   /**
    * @param cellPhone The cellPhone to set.
    */
   public void setCellPhone(String cellPhone)
   {
      this.cellPhone = cellPhone;
   }
   
   /**
    * @return Returns the chineseName.
    */
   public String getChineseName()
   {
      return chineseName;
   }
   
   /**
    * @param chineseName The chineseName to set.
    */
   public void setChineseName(String chineseName)
   {
      this.chineseName = chineseName;
   }
   
   /**
    * @return Returns the email.
    */
   public String getEmail()
   {
      return email;
   }
   
   /**
    * @param email The email to set.
    */
   public void setEmail(String email)
   {
      this.email = email;
   }
   
   /**
    * @return Returns the homePhone.
    */
   public String getHomePhone()
   {
      return homePhone;
   }
   
   /**
    * @param homePhone The homePhone to set.
    */
   public void setHomePhone(String homePhone)
   {
      this.homePhone = homePhone;
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
    * @return Returns the memberTypeId.
    */
   public int getMemberTypeId()
   {
      return memberTypeId;
   }
   
   /**
    * @param memberTypeId The memberTypeId to set.
    */
   public void setMemberTypeId(int memberTypeId)
   {
      this.memberTypeId = memberTypeId;
   }   
   
   public List<MemberHistory> getCheckoutAssets()
   {
      return checkoutAssets;
   }

   public void setCheckoutAssets(List<MemberHistory> checkoutAssets)
   {
      this.checkoutAssets = checkoutAssets;
   }   
}
