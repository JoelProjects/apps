package asset.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import members.ui.utils.GuiUtil;
import members.utils.Utility;
import asset.datamodel.MemberInfoManager;
import asset.datamodel.MemberType;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public class EditMemberInfoDialog extends JDialog
{
   private boolean newMember = false;
   private boolean hasChanged = false;

   private JTextField memberIdField;
   private JTextField nameField;
   private JTextField chineseNameField;
   private JTextField homePhoneField;
   private JTextField cellPhoneField;
   private JTextField addressField;
   private JTextField emailField;   
   private JComboBox typeCombo;

   private JButton syncButton;
   private JButton okButton;
   private JButton cancelButton;

   // for new member
   public EditMemberInfoDialog(Frame owner, boolean modal)
   {
      super(owner, "New Member", modal);

      newMember = true;
      
      init();
   }

   // for existing member
   public EditMemberInfoDialog(Frame owner, boolean modal, String memberName)
   {
      super(owner, modal);

      setTitle("Edit Member " + memberName);

      init();
   }

   public long getMemberId()
   {
      return Long.parseLong(memberIdField.getText().trim());
   }

   public void setMemberId(long id)
   {
      memberIdField.setText(String.valueOf(id));
   }   
   
   public String getName()
   {
      return (String)nameField.getText().trim();
   }

   public void setName(String name)
   {
      nameField.setText(name);
   }
   
   public String getChineseName()
   {
      return (String)chineseNameField.getText().trim();
   }

   public void setChineseName(String name)
   {
      chineseNameField.setText(name);
   } 
   
   public String getHomePhone()
   {
      return (String)homePhoneField.getText().trim();
   }

   public void setHomePhone(String phone)
   {
      homePhoneField.setText(phone);
   }   
   
   public String getCellPhone()
   {
      return (String)cellPhoneField.getText().trim();
   }

   public void setCellPhone(String phone)
   {
      cellPhoneField.setText(phone);
   }    
   
   public String getAddress()
   {
      return (String)addressField.getText().trim();
   }

   public void setAddress(String address)
   {
      addressField.setText(address);
   }    
   
   public String getEmail()
   {
      return (String)emailField.getText().trim();
   }

   public void setEmail(String email)
   {
      emailField.setText(email);
   }    
   
   public MemberType getMemberType()
   {
      return (MemberType)typeCombo.getSelectedItem();
   }

   public void setMemberType(MemberType obj)
   {
      typeCombo.setSelectedItem(obj);
   }       
   
   public boolean hasChanged()
   {
      return hasChanged;
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
      cons.anchor = GridBagConstraints.WEST;
      cons.insets = new Insets(2, 0, 2, 0);

      // label for member ID
      cons.gridx = 0;
      cons.gridy = 0;
      JLabel memberIdLabel = new JLabel("Member ID ");
      gridbag.setConstraints(memberIdLabel, cons);
      dataPanel.add(memberIdLabel);
      // text field for member ID
      cons.gridx = 1;      
      cons.gridy = 0;
      memberIdField = new JTextField(50);
      memberIdField.setEnabled(false);
      gridbag.setConstraints(memberIdField, cons);
      dataPanel.add(memberIdField);

      // label for member name
      cons.gridx = 0;
      cons.gridy = 1;
      JLabel nameLabel = new JLabel("English Name ");
      gridbag.setConstraints(nameLabel, cons);
      dataPanel.add(nameLabel);
      // text field for member name
      cons.gridx = 1;
      cons.gridy = 1;
      nameField = new JTextField(50);
      gridbag.setConstraints(nameField, cons);
      dataPanel.add(nameField);

      // label for Chinese name
      cons.gridx = 0;
      cons.gridy = 2;
      JLabel chineseNameLabel = new JLabel("Chinese Name ");
      gridbag.setConstraints(chineseNameLabel, cons);
      dataPanel.add(chineseNameLabel);
      // text field for Chinese name
      cons.gridx = 1;      
      cons.gridy = 2;
      chineseNameField = new JTextField(50);
      gridbag.setConstraints(chineseNameField, cons);
      dataPanel.add(chineseNameField);      
      
      // label for home phone
      cons.gridx = 0;
      cons.gridy = 3;
      JLabel homePhoneLabel = new JLabel("Home Phone ");
      gridbag.setConstraints(homePhoneLabel, cons);
      dataPanel.add(homePhoneLabel);
      // text field for home Phone
      cons.gridx = 1;
      cons.gridy = 3;
      homePhoneField = new JTextField(50);
      gridbag.setConstraints(homePhoneField, cons);
      dataPanel.add(homePhoneField);

      // label for cell phone
      cons.gridx = 0;
      cons.gridy = 4;
      JLabel cellPhoneLabel = new JLabel("Cell Phone ");
      gridbag.setConstraints(cellPhoneLabel, cons);
      dataPanel.add(cellPhoneLabel);
      // text field for cell phone
      cons.gridx = 1;
      cons.gridy = 4;
      cellPhoneField = new JTextField(50);
      gridbag.setConstraints(cellPhoneField, cons);
      dataPanel.add(cellPhoneField);  
      
      // label for address
      cons.gridx = 0;
      cons.gridy = 5;
      JLabel addressLabel = new JLabel("Address ");
      gridbag.setConstraints(addressLabel, cons);
      dataPanel.add(addressLabel);
      // text field for address
      cons.gridx = 1;
      cons.gridy = 5;
      addressField = new JTextField(50);
      gridbag.setConstraints(addressField, cons);
      dataPanel.add(addressField);     
      
      // label for email
      cons.gridx = 0;
      cons.gridy = 6;
      JLabel emailLabel = new JLabel("E-Mail ");
      gridbag.setConstraints(emailLabel, cons);
      dataPanel.add(emailLabel);
      // text field for email
      cons.gridx = 1;
      cons.gridy = 6;
      emailField = new JTextField(50);
      gridbag.setConstraints(emailField, cons);
      dataPanel.add(emailField);        
      
      // label for member type
      cons.gridx = 0;
      cons.gridy = 7;
      JLabel typeLabel = new JLabel("Member Type ");
      gridbag.setConstraints(typeLabel, cons);
      dataPanel.add(typeLabel);
      // combo box for member type
      cons.gridx = 1;
      cons.gridy = 7;      
      typeCombo = new JComboBox(ContextManager.getMemberTypeList());
      gridbag.setConstraints(typeCombo, cons);
      dataPanel.add(typeCombo);       
            
      content.add(dataPanel, BorderLayout.NORTH);

      // button panel
      JPanel buttonPanel = new JPanel();

      okButton = new JButton("Ok");
      cancelButton = new JButton("Cancel");
      syncButton = new JButton("Sync User");  
      buttonPanel.add(syncButton);      
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
    * This is the listener for Ok, Cancel and Help buttons.
    */
   class MyActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(okButton == e.getSource()) // OK button
         {
            if(Utility.isEmpty(getName()) && Utility.isEmpty(getChineseName()))
            {
               GuiUtil.messageDialog(EditMemberInfoDialog.this,
                     "One of the Name fields should not be empty.");
               return;
            }

            if(Utility.isEmpty(getAddress()))
            {
               GuiUtil.messageDialog(EditMemberInfoDialog.this,
                     "Address should not be empty.");
               return;
            }
            
            if(Utility.isEmpty(getHomePhone()) && Utility.isEmpty(getCellPhone()))
            {
               GuiUtil.messageDialog(EditMemberInfoDialog.this,
                     "At least one phone number should not be empty.");
               return;
            }            

            hasChanged = true;
         }
         else if(cancelButton == e.getSource()) // Cancel button
         {
            closeDialog();
         }
         else if(syncButton == e.getSource()) // Sync User button
         {            
            Connection con = null;
            try
            {
               // insert into the db
               con = ContextManager.getUserDBConnection();            
               MemberInfoManager memberMgr = new MemberInfoManager(con);
               //MemberInfo info = memberMgr.getExternalMember();
               hasChanged = true;
            }
            catch(Exception uex)
            {
               System.out.println(uex);
            }
            finally
            {
               try
               {
                  if(con != null)
                     con.close();
               }
               catch(Exception uex){}
            }              
         }
         
         setVisible(false);
      }
   }
}
