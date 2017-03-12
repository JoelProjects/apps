/* $Id: EditItemDialog.java,v 1.4 2008/04/07 05:29:22 joelchou Exp $ */
package store.ui;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import members.ui.utils.GuiUtil;
import members.utils.Utility;
import store.datamodel.ItemTypeInfo;
import store.manager.ContextManager;
import store.manager.ItemsManager;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 2/22/2007
 */
public class EditItemDialog extends JDialog
{
   private boolean newItem = false;
   private boolean hasChanged = false;

   private JTextField itemNoField;
   private JTextField barcodeField;
   private JTextField nameField;
   private JTextField priceField;
   private JComboBox typeCombo;

   private JButton okButton;
   private JButton cancelButton;

   // for new asset
   public EditItemDialog(Frame owner, boolean modal)
   {
      super(owner, "新增物品", modal);

      newItem = true;
      
      init();
   }

   // for existing asset
   public EditItemDialog(Frame owner, boolean modal, String itemName)
   {
      super(owner, modal);

      setTitle("編輯物品 \"" + itemName + "\"");

      init();
   }

   public String getItemNo()
   {
      return (String)itemNoField.getText().trim();
   }

   public void setItemNo(String itemNo)
   {
      itemNoField.setText(itemNo);
   }

   public String getBarcode()
   {
      return (String)barcodeField.getText().trim();
   }

   public void setBarcode(String barcode)
   {
      barcodeField.setText(barcode);
   }
      
   public String getName()
   {
      return (String)nameField.getText().trim();
   }

   public void setName(String name)
   {
      nameField.setText(name);
   }

   public double getPrice()
   {
      return Double.parseDouble(priceField.getText().trim());
   }

   public void setPrice(double price)
   {
      priceField.setText(String.valueOf(price));
   }   
   
   public ItemTypeInfo getType()
   {
      return (ItemTypeInfo)typeCombo.getSelectedItem();
   }

   public void setType(ItemTypeInfo obj)
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

      int row = 0;
      int col = 0;      
      // label for item no.
      cons.gridx = col++;
      cons.gridy = row;
      JLabel itemNoLabel = new JLabel("編號:");
      gridbag.setConstraints(itemNoLabel, cons);
      dataPanel.add(itemNoLabel);
      // text field for item no.
      cons.gridx = col++;      
      cons.gridy = row;
      itemNoField = new JTextField(15);
      gridbag.setConstraints(itemNoField, cons);
      dataPanel.add(itemNoField);

      row++;
      col = 0;      
      // label for item name
      cons.gridx = col++;
      cons.gridy = row;
      JLabel nameLabel = new JLabel("名稱:");
      gridbag.setConstraints(nameLabel, cons);
      dataPanel.add(nameLabel);
      // text field for item name
      cons.gridx = col++;
      cons.gridy = row;
      nameField = new JTextField(50);
      gridbag.setConstraints(nameField, cons);
      dataPanel.add(nameField);

      row++;
      col = 0;      
      // label for barcode
      cons.gridx = col++;
      cons.gridy = row;
      JLabel barcodeLabel = new JLabel("條碼:");
      gridbag.setConstraints(barcodeLabel, cons);
      dataPanel.add(barcodeLabel);
      // text field for barcode
      cons.gridx = col++;      
      cons.gridy = row;
      barcodeField = new JTextField(15);
      gridbag.setConstraints(barcodeField, cons);
      dataPanel.add(barcodeField);      
           
      row++;
      col = 0;      
      // label for price
      cons.gridx = col++;
      cons.gridy = row;
      JLabel priceLabel = new JLabel("單價:");
      gridbag.setConstraints(priceLabel, cons);
      dataPanel.add(priceLabel);
      // text field for price
      cons.gridx = col++;      
      cons.gridy = row;
      priceField = new JTextField(10);
      gridbag.setConstraints(priceField, cons);
      dataPanel.add(priceField);      
      
      row++;
      col = 0;      
      // label for type
      cons.gridx = col++;
      cons.gridy = row;
      JLabel typeLabel = new JLabel("類別:");
      gridbag.setConstraints(typeLabel, cons);
      dataPanel.add(typeLabel);
      // combo box for type
      cons.gridx = col++;
      cons.gridy = row;      
      typeCombo = new JComboBox(ContextManager.getTypeList());
      gridbag.setConstraints(typeCombo, cons);
      dataPanel.add(typeCombo);             
      
      content.add(dataPanel, BorderLayout.NORTH);

      // button panel
      JPanel buttonPanel = new JPanel();

      okButton = new JButton("確定");
      cancelButton = new JButton("取消");
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
            if(Utility.isEmpty(getBarcode()))
            {
               GuiUtil.messageDialog(EditItemDialog.this,
                     "條碼不可為空白");
               return;
            }

            if(Utility.isEmpty(getName()))
            {
               GuiUtil.messageDialog(EditItemDialog.this,
                     "名稱不可為空白��i���ť�");
               return;
            }

            if(Utility.isEmpty(priceField.getText()))
            {
               GuiUtil.messageDialog(EditItemDialog.this,
                     "單價不可為空白");
               return;
            }            
            
            if(newItem)
            {
               if(ItemsManager.barcodeExists(getBarcode()))
               {
                  GuiUtil.messageDialog(EditItemDialog.this,
                     "相同的條碼已存在,請輸入其他條碼");
                  return;
               }
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
