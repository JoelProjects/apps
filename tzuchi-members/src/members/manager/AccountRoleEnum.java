package members.manager;

/**
 * AccountRoleEnum contains all account roles. 
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 *
 */
public enum AccountRoleEnum
{
   SYSTEM_ADMIN(1),
   STORE_ADMIN(2),
   LIB_ADMIN(3),
   STORE_STAFF(4),
   LIB_STAFF(5);   
   
   private int roleId;
   AccountRoleEnum(int roleId)
   {
      this.roleId = roleId;
   }
      
   public int getRoleId()
   {
      return roleId;
   }
}
