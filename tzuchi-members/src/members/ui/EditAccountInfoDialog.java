package members.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import members.datamodel.AccountRoleInfo;
import members.manager.AccountManager;
import members.manager.ContextManager;
import members.ui.utils.GuiUtil;
import members.utils.Utility;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 1/27/2007
 */
public class EditAccountInfoDialog extends JDialog
{
   private boolean newAccount = false;
   private boolean hasChanged = false;
   private boolean isPasswordChanged = false;

   private JTextField usernameField;
   private JPasswordField newPasswordField; 
   private JPasswordField verifyPasswordField;   
   private JCheckBox useBlankPassword;
   private JList roleList; 

   private JButton okButton;
   private JButton cancelButton;

   // for new account
   public EditAccountInfoDialog(Frame owner, boolean modal)
   {
      super(owner, "New Account", modal);

      newAccount = true;
      
      init();
   }

   // for existing account
   public EditAccountInfoDialog(Frame owner, boolean modal, String username)
   {
      super(owner, modal);

      setTitle("Edit Account " + username);

      init();
   }

   public String getUsername()
   {
      return usernameField.getText().trim();
   }

   public void setUsername(String username)
   {
      usernameField.setText(username);
   }    

   public char[] getNewPassword()
   {
      return newPasswordField.getPassword();
   }   
   
   public char[] getVerifyPassword()
   {
      return verifyPasswordField.getPassword();
   }  
   
   public void setRoles(String[] roles)
   {
      if(roles != null && roles.length > 0)
      {
         int[] indices = new int[roles.length];
         for(int i = 0; i < roles.length; i++)
         {
            for(int row = 0; row < roleList.getModel().getSize(); row++)
            {
               AccountRoleInfo role = (AccountRoleInfo)roleList.getModel().getElementAt(row);
               if(roles[i].equals(role.getAccountRoleName()))
               {
                  indices[i] = row;
                  break;
               }
            }
         }
         
         roleList.setSelectedIndices(indices);
      }
   }
   
   public Set<AccountRoleInfo> getRoles()
   {
      int[] indices = roleList.getSelectedIndices();
      Set<AccountRoleInfo> roleSet = new HashSet<AccountRoleInfo>();
      for(int i = 0; i < indices.length; i++)
      {
         AccountRoleInfo role = 
            (AccountRoleInfo)roleList.getModel().getElementAt(indices[i]);
         roleSet.add(role);
      }
      
      return roleSet;
   }
         
   public boolean hasChanged()
   {
      return hasChanged;
   }
   
   public boolean isPasswordChanged()
   {
      return isPasswordChanged;
   }   

   private void init()
   {
      // not resizable
      setResizable(false);

      Container content = getContentPane();
      // set layout
      content.setLayout(new BorderLayout());

      JPanel dataPanel = new JPanel();
      dataPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      dataPanel.setLayout(gridbag);
      cons.anchor = GridBagConstraints.EAST;
      cons.insets = new Insets(2, 0, 2, 0);

      int row = 0;
      int col = 0;
      // label for username
      cons.gridx = col++;
      cons.gridy = row;
      JLabel usernameLabel = new JLabel("Username ");
      gridbag.setConstraints(usernameLabel, cons);
      dataPanel.add(usernameLabel);
      // text field for username
      cons.gridx = col++;      
      cons.gridy = row;
      usernameField = new JTextField(25);
      if(!newAccount)
         usernameField.setEnabled(false);
      cons.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(usernameField, cons);
      dataPanel.add(usernameField);
      
      cons.anchor = GridBagConstraints.EAST;
      
      row++;
      col = 0;
      // label new password
      cons.gridx = col++;
      cons.gridy = row;
      JLabel passwordLabel = new JLabel("New Password ");
      gridbag.setConstraints(passwordLabel, cons);
      dataPanel.add(passwordLabel);
      // text field for new password
      cons.gridx = col++;      
      cons.gridy = row;
      newPasswordField = new JPasswordField(25);
      gridbag.setConstraints(newPasswordField, cons);
      dataPanel.add(newPasswordField);       
      
      row++;
      col = 0;
      // label verify password
      cons.gridx = col++;
      cons.gridy = row;
      JLabel verifyPasswordLabel = new JLabel("Verify Password ");
      gridbag.setConstraints(verifyPasswordLabel, cons);
      dataPanel.add(verifyPasswordLabel);
      // text field for verify password
      cons.gridx = col++;      
      cons.gridy = row;
      verifyPasswordField = new JPasswordField(25);
      gridbag.setConstraints(verifyPasswordField, cons);
      dataPanel.add(verifyPasswordField);       

      row++;
      col = 0;
      // check box for using blank password
      cons.gridx = col++;
      cons.gridy = row;
      cons.gridwidth = 2;
      cons.anchor = GridBagConstraints.WEST;
      useBlankPassword = new JCheckBox("Use Blank Password ");
      gridbag.setConstraints(useBlankPassword, cons);
      dataPanel.add(useBlankPassword);      
      
      row++;
      col = 0;      
      // label for role
      cons.gridx = col++;
      cons.gridy = row;
      cons.gridwidth = 1;
      cons.anchor = GridBagConstraints.EAST;
      JLabel roleLabel = new JLabel("Role ");
      gridbag.setConstraints(roleLabel, cons);
      dataPanel.add(roleLabel);
      // combo box for role
      cons.gridx = col++;
      cons.gridy = row;   
      cons.anchor = GridBagConstraints.WEST;
      roleList = new JList(ContextManager.getRoleList());
      JScrollPane roleScrollPane = new JScrollPane(roleList);
      roleScrollPane.setPreferredSize(new Dimension(120, 70));
      gridbag.setConstraints(roleScrollPane, cons);
      dataPanel.add(roleScrollPane);      
      
      content.add(dataPanel, BorderLayout.NORTH);

      // button panel
      JPanel buttonPanel = new JPanel();

      okButton = new JButton("Ok");
      cancelButton = new JButton("Cancel");     
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      
      content.add(buttonPanel, BorderLayout.SOUTH);

      // register listeners
      MyActionListener actionListener = new MyActionListener();
      okButton.addActionListener(actionListener);
      cancelButton.addActionListener(actionListener);
      
      // window listener
      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            closeDialog();
         }
      });
   }

   private void closeDialog()
   {
   }
   
   /**
    * This is the listener for Ok and Cancel buttons.
    */
   class MyActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(okButton == e.getSource()) // OK button
         {
            if(newAccount)
            {
               if(Utility.isEmpty(getUsername()))
               {
                  GuiUtil.messageDialog(EditAccountInfoDialog.this,
                     "Username field should not be empty.");
                  return;
               }
               
               // username should be unique
               if(AccountManager.accountExists(getUsername()))
               {
                  GuiUtil.messageDialog(EditAccountInfoDialog.this,
                     "Duplicate username, please enter another username.");
                  return;
               }               
            }
            
            if(!useBlankPassword.isSelected())
            {
               if(!Arrays.equals(getNewPassword(), getVerifyPassword()))
               {
                  GuiUtil.messageDialog(EditAccountInfoDialog.this,
                     "Please verify your new password.");
                  newPasswordField.setText("");
                  verifyPasswordField.setText("");
                  return;               
               }
               else
               {
                  // If new password is blank, password won't be changed
                  // unless "Use Blank Password" is checked.
                  if(!Utility.isEmpty(String.valueOf(getNewPassword())))
                     isPasswordChanged = true;
               }
            }
            else
            {
               newPasswordField.setText("");     
               verifyPasswordField.setText("");
               isPasswordChanged = true;
            }
            
            if(roleList.getSelectedIndices().length == 0)
            {
               GuiUtil.messageDialog(EditAccountInfoDialog.this,
                  "At least one role needs to be assigned.");
               return;               
            }            
            
            hasChanged = true;
         }
         else if(cancelButton == e.getSource()) // Cancel button
         {
            closeDialog();
         }
         
         setVisible(false);
      }
   }
}
