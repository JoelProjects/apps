/* $Id: UpdateItemQuantityDialog.java,v 1.6 2008/04/07 05:29:22 joelchou Exp $ */
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
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import store.utils.Messages;

import members.ui.utils.GuiUtil;
import members.utils.Utility;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 4/30/2007
 */
public class UpdateItemQuantityDialog extends JDialog
{
   private boolean hasChanged = false;
   private int inventory = 0;
   private int updatedNumber = 0;
   private Date invoiceDate;
   private JComboBox updateCombo;   
   private JTextField updateField;
   private JTextField invoiceField;
   private JTextField invoiceDateField;

   private JButton okButton;
   private JButton cancelButton;

   public UpdateItemQuantityDialog(Frame owner, boolean modal, String itemName, 
      int inventory)
   {
      super(owner, modal);
      this.inventory = inventory;
      setTitle("庫存變更 \"" + itemName + "\"");
      init();
   }
   
   public int getUpdatedNumber()
   {
      return updatedNumber;
   }
   
   public String getInvoiceNumber()
   {
      return invoiceField.getText();
   }   
   
   public Date getInvoiceDate()
   {
      return invoiceDate;
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
      // label for current inventory.
      cons.gridx = col++;
      cons.gridy = row;
      JLabel inventoryLabel = new JLabel("目前庫存:");
      gridbag.setConstraints(inventoryLabel, cons);
      dataPanel.add(inventoryLabel);
      // text field for current inventory.
      cons.gridx = col++;      
      cons.gridy = row;
      JLabel quantityLabel = new JLabel(String.valueOf(inventory));
      gridbag.setConstraints(quantityLabel, cons);
      dataPanel.add(quantityLabel);

      row++;
      col = 0;      
      // label for invoice number
      cons.gridx = col++;
      cons.gridy = row;
      JLabel invoiceLabel = new JLabel("收據號碼:");
      gridbag.setConstraints(invoiceLabel, cons);
      dataPanel.add(invoiceLabel);
      // text field for invoice number
      cons.gridx = col++;
      cons.gridy = row;
      invoiceField = new JTextField(50);
      gridbag.setConstraints(invoiceField, cons);
      dataPanel.add(invoiceField);      
      
      row++;
      col = 0;      
      // label for invoice date
      cons.gridx = col++;
      cons.gridy = row;
      JLabel invoiceDateLabel = new JLabel("收據日期:");
      gridbag.setConstraints(invoiceDateLabel, cons);
      dataPanel.add(invoiceDateLabel);
      // text field for invoice date
      cons.gridx = col++;
      cons.gridy = row;
      invoiceDateField = new JTextField(50);
      invoiceDateField.setToolTipText(Messages.SUPPORTED_DATE_FORMATS);
      gridbag.setConstraints(invoiceDateField, cons);
      dataPanel.add(invoiceDateField);      
      
      row++;
      col = 0;      
      // label for item name
      cons.gridx = col++;
      cons.gridy = row;
      Object[] updates = new String[]{"增加", "減少"};
      updateCombo = new JComboBox(updates);
      gridbag.setConstraints(updateCombo, cons);
      dataPanel.add(updateCombo);
      // text field for updated number
      cons.gridx = col++;
      cons.gridy = row;
      updateField = new JTextField(50);
      gridbag.setConstraints(updateField, cons);
      dataPanel.add(updateField);
      
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
            if(Utility.isEmpty(updateField.getText()))
            {
               GuiUtil.messageDialog(UpdateItemQuantityDialog.this,
                  "數量不可為空白");
               return;
            }               
            updatedNumber = Integer.parseInt(updateField.getText());
            if(updatedNumber < 0)
            {
               GuiUtil.messageDialog(UpdateItemQuantityDialog.this,
                  "數量不可為負數");
               return;
            }
            
            if(updateCombo.getSelectedIndex() == 1)
            {
               if(updatedNumber > inventory)
               {
                  GuiUtil.messageDialog(UpdateItemQuantityDialog.this,
                     "減少數量不可大於庫存數量");
                  return;                  
               }
               
               updatedNumber = -updatedNumber;
            }
            
            // check format of invoice date
            String invoiceDateStr = invoiceDateField.getText();
            if(!Utility.isEmpty(invoiceDateStr))
            {
               try
               {
                  invoiceDate = Utility.dateParser(invoiceDateStr);
               }
               catch(ParseException ex)
               {
                  GuiUtil.messageDialog(UpdateItemQuantityDialog.this, Messages.SUPPORTED_DATE_FORMATS);
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
