/* $Id: AccountInfo.java,v 1.3 2008/10/19 06:38:12 joelchou Exp $ */
package members.datamodel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * This is the parent class for user accounts which links to account_role_info 
 * through an association table - account_role. Each account only can link
 * to one member info.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 *
 */
public class AccountInfo implements Serializable
{
   private long memberId = -1;
   private String username;
   private String password;
   private MemberInfo memberInfo;
   private Set<AccountRoleInfo> roles;
   
   public AccountInfo()
   {
      
   }
      
   public long getMemberId()
   {
      if(memberInfo != null)
         memberId =  memberInfo.getMemberId();
      
      return memberId;
   }
   
   public void setMemberId(long memberId)
   {
      this.memberId = memberId;
   }
   
   public String getPassword()
   {
      return password;
   }
   
   public void setPassword(String password)
   {
      this.password = password;
   }
   
   public String getUsername()
   {
      return username;
   }
   
   public void setUsername(String username)
   {
      this.username = username;
   }
   
   public MemberInfo getMemberInfo()
   {
      return memberInfo;
   }

   public void setMemberInfo(MemberInfo memberInfo)
   {
      this.memberInfo = memberInfo;
   }

   public Set<AccountRoleInfo> getRoles()
   {
      return roles;
   }

   public void setRoles(Set<AccountRoleInfo> roles)
   {
      this.roles = roles;
   }
   
   public String getRoleNamesStr()
   {
      StringBuffer buffer = new StringBuffer();
      if(roles != null && !roles.isEmpty())
      {
         Iterator it = roles.iterator();
         while(it.hasNext())
         {
            AccountRoleInfo roleInfo = (AccountRoleInfo)it.next();
            if(buffer.length() > 0)
               buffer.append(", ");
            buffer.append(roleInfo.getAccountRoleName());
         }
      }
      
      return buffer.toString();
   }
   
   public String[] getRoleNames()
   {
      String[] array = null;
      if(roles != null && !roles.isEmpty())
      {
         array = new String[roles.size()];
         Iterator it = roles.iterator();
         int index = 0;
         while(it.hasNext())
         {
            AccountRoleInfo roleInfo = (AccountRoleInfo)it.next();
            array[index] = roleInfo.getAccountRoleName();
            index++;
         }
      }
      
      return array;
   }   
   
   public boolean equals(Object obj)
   {
      boolean flag = false;
      AccountInfo theObj = (AccountInfo)obj;
      if(theObj != null)
         flag = theObj.getUsername().equals(username);
      
      return flag;
   }
   
   public int hashCode() 
   {
      return username.hashCode();
   }   
}
