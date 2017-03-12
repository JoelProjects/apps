package members.datamodel;

import java.io.Serializable;

/**
 * This class represents account role of user account. Each account might contain 
 * multiple roles.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 *
 */
public class AccountRoleInfo implements Serializable
{
   private int accountRoleId;
   private String accountRoleName;
   
   public AccountRoleInfo()
   {
   }

   public AccountRoleInfo(int accountRoleId, String accountRoleName)
   {
      this.accountRoleId = accountRoleId;
      this.accountRoleName = accountRoleName;
   }   
   
   public int getAccountRoleId()
   {
      return accountRoleId;
   }

   public void setAccountRoleId(int accountRoleId)
   {
      this.accountRoleId = accountRoleId;
   }

   public String getAccountRoleName()
   {
      return accountRoleName;
   }

   public void setAccountRoleName(String accountRoleName)
   {
      this.accountRoleName = accountRoleName;
   }
   
   public boolean equals(Object obj)
   {
      boolean flag = false;
      AccountRoleInfo theObj = (AccountRoleInfo)obj;
      if(theObj != null)
         flag = theObj.getAccountRoleName().equals(accountRoleName);
      
      return flag;
   }
   
   public int hashCode() 
   {
      return getAccountRoleName().hashCode();
   }
   
   public String toString()
   {
      return accountRoleName;
   }
}
