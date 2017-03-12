package members.ui.auth;

import javax.swing.JDialog;

public abstract class AbstractLoginDialog extends JDialog implements LoginDialog
{
   protected String username = null;
   protected String password = null;
   protected boolean cancelled = false;
   
   public String getUsername()
   {
      return username;
   }

   public String getPassword()
   {
      return password;
   }

   public boolean isCancelled()
   {
      return cancelled;
   }   
}
