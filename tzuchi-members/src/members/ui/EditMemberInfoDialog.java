package members.ui;

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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import members.ui.utils.GuiUtil;
import members.utils.Utility;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 1/27/2007
 */
public class EditMemberInfoDialog extends JDialog
{
   private boolean newMember = false;
   private boolean hasChanged = false;

   private JTextField memberIdField;
   private JTextField chFirstNameField;
   private JTextField chLastNameField;   
   private JTextField firstNameField;
   private JTextField lastNameField;
   private JTextField homePhoneField;
   private JTextField cellPhoneField;
   private JTextField workPhoneField;
   private JTextField emailField;   

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
   
   public String getChFirstName()
   {
      return (String)chFirstNameField.getText().trim();
   }

   public void setChFirstName(String chFirstName)
   {
      chFirstNameField.setText(chFirstName);
   }
   
   public String getChLastName()
   {
      return (String)chLastNameField.getText().trim();
   }

   public void setChLastName(String chLastName)
   {
      chLastNameField.setText(chLastName);
   }   
   
   public String getFirstName()
   {
      return (String)firstNameField.getText().trim();
   }

   public void setFirstName(String firstName)
   {
      firstNameField.setText(firstName);
   }
   
   public String getLastName()
   {
      return (String)lastNameField.getText().trim();
   }

   public void setLastName(String lastName)
   {
      lastNameField.setText(lastName);
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
   
   public String getWorkPhone()
   {
      return (String)workPhoneField.getText().trim();
   }

   public void setWorkPhone(String workPhone)
   {
      workPhoneField.setText(workPhone);
   }    
   
   public String getEmail()
   {
      return (String)emailField.getText().trim();
   }

   public void setEmail(String email)
   {
      emailField.setText(email);
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
      cons.anchor = GridBagConstraints.EAST;
      cons.insets = new Insets(2, 0, 2, 0);

      int row = 0;
      int col = 0;
      // label for member ID
      cons.gridx = col++;
      cons.gridy = row;
      JLabel memberIdLabel = new JLabel("Member ID ");
      gridbag.setConstraints(memberIdLabel, cons);
      dataPanel.add(memberIdLabel);
      // text field for member ID
      cons.gridx = col++;      
      cons.gridy = row;
      memberIdField = new JTextField(25);
      memberIdField.setEnabled(false);
      gridbag.setConstraints(memberIdField, cons);
      dataPanel.add(memberIdField);

      row++;
      col = 0;
      // label for Chinese last name
      cons.gridx = col++;
      cons.gridy = row;
      JLabel chLastNameLabel = new JLabel("Chinese Last Name ");
      gridbag.setConstraints(chLastNameLabel, cons);
      dataPanel.add(chLastNameLabel);
      // text field for Chinese last name
      cons.gridx = col++;      
      cons.gridy = row;
      chLastNameField = new JTextField(25);
      gridbag.setConstraints(chLastNameField, cons);
      dataPanel.add(chLastNameField);       
      
      // label for Chinese first name
      cons.gridx = col++;
      cons.gridy = row;
      JLabel chFirstNameLabel = new JLabel("Chinese First Name ");
      gridbag.setConstraints(chFirstNameLabel, cons);
      dataPanel.add(chFirstNameLabel);
      // text field for Chinese first name
      cons.gridx = col++;      
      cons.gridy = row;
      chFirstNameField = new JTextField(25);
      gridbag.setConstraints(chFirstNameField, cons);
      dataPanel.add(chFirstNameField);        
      
      row++;
      col = 0;
      // label for other last name
      cons.gridx = col++;
      cons.gridy = row;
      JLabel lastNameLabel = new JLabel("Last Name ");
      gridbag.setConstraints(lastNameLabel, cons);
      dataPanel.add(lastNameLabel);
      // text field for last name
      cons.gridx = col++;      
      cons.gridy = row;
      lastNameField = new JTextField(25);
      gridbag.setConstraints(lastNameField, cons);
      dataPanel.add(lastNameField);       
            
      // label for first name
      cons.gridx = col++;
      cons.gridy = row;
      JLabel firstNameLabel = new JLabel("First Name ");
      gridbag.setConstraints(firstNameLabel, cons);
      dataPanel.add(firstNameLabel);
      // text field for first name
      cons.gridx = col++;      
      cons.gridy = row;
      firstNameField = new JTextField(25);
      gridbag.setConstraints(firstNameField, cons);
      dataPanel.add(firstNameField); 
      
      row++;
      col = 0;      
      // label for home phone
      cons.gridx = col++;
      cons.gridy = row;
      JLabel homePhoneLabel = new JLabel("Home Phone ");
      gridbag.setConstraints(homePhoneLabel, cons);
      dataPanel.add(homePhoneLabel);
      // text field for home Phone
      cons.gridx = col++;
      cons.gridy = row;
      homePhoneField = new JTextField(25);
      gridbag.setConstraints(homePhoneField, cons);
      dataPanel.add(homePhoneField);

      row++;
      col = 0;      
      // label for cell phone
      cons.gridx = col++;
      cons.gridy = row;
      JLabel cellPhoneLabel = new JLabel("Cell Phone ");
      gridbag.setConstraints(cellPhoneLabel, cons);
      dataPanel.add(cellPhoneLabel);
      // text field for cell phone
      cons.gridx = col++;
      cons.gridy = row;
      cellPhoneField = new JTextField(25);
      gridbag.setConstraints(cellPhoneField, cons);
      dataPanel.add(cellPhoneField);  
   
      row++;
      col = 0;      
      // label for work phone
      cons.gridx = col++;
      cons.gridy = row;
      JLabel workPhoneLabel = new JLabel("Work Phone ");
      gridbag.setConstraints(workPhoneLabel, cons);
      dataPanel.add(workPhoneLabel);
      // text field for work phone
      cons.gridx = col++;
      cons.gridy = row;
      workPhoneField = new JTextField(25);
      gridbag.setConstraints(workPhoneField, cons);
      dataPanel.add(workPhoneField);       
      
      row++;
      col = 0;      
      // label for email
      cons.gridx = col++;
      cons.gridy = row;
      JLabel emailLabel = new JLabel("E-Mail ");
      gridbag.setConstraints(emailLabel, cons);
      dataPanel.add(emailLabel);
      // text field for email
      cons.gridx = col++;
      cons.gridy = row;
      emailField = new JTextField(25);
      gridbag.setConstraints(emailField, cons);
      dataPanel.add(emailField);        
                  
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
            if(Utility.isEmpty(getFirstName()) && Utility.isEmpty(getChFirstName()))
            {
               GuiUtil.messageDialog(EditMemberInfoDialog.this,
                     "One of the Name fields should not be empty.");
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
         
         setVisible(false);
      }
   }
}
