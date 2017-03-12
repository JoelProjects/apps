/* $Id: POSPanel.java,v 1.8 2008/10/24 05:55:21 joelchou Exp $ */
package store.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import members.datamodel.AccountInfo;
import members.ui.utils.GuiUtil;
import members.utils.Utility;
import store.datamodel.ItemInfo;
import store.manager.ItemsManager;
import store.manager.TransactionManager;
import store.utils.Messages;

/**
 * This class handles user transactions.
 * 
 * @author Cheng-Hung Chou
 * @since 5/3/2007
 */
public class POSPanel extends JPanel
{  
   private static final int COL_NUM_BARCODE = 0;   
   private static final int COL_NUM_NAME = 1;
   private static final int COL_NUM_PRICE = 2;
   private static final int COL_NUM_QUANTITY = 3;
   private static final int COL_NUM_TOTAL = 4;
   
   private static Logger logger = Logger.getLogger(POSPanel.class); 
   
   private JTextField barcodeField;
   private JTextField quantityField;
   private JTextField purchaserField;
   private JTextField processedDateField;
   private JLabel totalLabel;
   private JTable shoppingCartTable;
   
   private AccountInfo accountInfo;
   // [barcode, quantity]
   private Map<String, ItemInfo> addedItems = new HashMap<String, ItemInfo>();
   private double transTotal = 0;
   
   public POSPanel(AccountInfo accountInfo)
   {
      this.accountInfo = accountInfo;
      
      setLayout(new BorderLayout());
      
      // buttons
      add(getButtonsPanel(), BorderLayout.SOUTH);      
     
      // add item panel
      JPanel addPanel = new JPanel();
      addPanel.add(new JLabel("條碼:"));
      barcodeField = new JTextField(10);
      addPanel.add(barcodeField);
      addPanel.add(new JLabel("數量:"));
      quantityField = new JTextField(2);
      quantityField.setText("1");
      addPanel.add(quantityField);      
      JButton addBtn = new JButton("加入購物籃");
      addPanel.add(addBtn);
      addPanel.add(new JLabel(" 總金額:"));
      totalLabel = new JLabel("0");
      addPanel.add(totalLabel);
      addPanel.add(new JLabel(" 購買者:"));
      purchaserField = new JTextField(20);
      addPanel.add(purchaserField);  
      addPanel.add(new JLabel(" 處理日期:"));
      processedDateField = new JTextField(8);
      processedDateField.setToolTipText("用於批次處理, " + 
         Messages.SUPPORTED_DATE_FORMATS);
      addPanel.add(processedDateField);       
      
      add(addPanel, BorderLayout.NORTH);   
      
      // shopping cart
      Vector<String> headers = new Vector<String>();
      headers.add("條碼");
      headers.add("名稱");
      headers.add("單價");
      headers.add("數量");
      headers.add("金額");

      Vector<Vector<String>> data = new Vector<Vector<String>>();
      NotEditableTableModel dataModel = new NotEditableTableModel(data, headers);
      shoppingCartTable = new JTable(dataModel);
      shoppingCartTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      shoppingCartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {40, 400, 30, 30, 40}; // define column width
      GuiUtil.setColumnWidths(shoppingCartTable, widths);

      // moving table headers is not allowed
      shoppingCartTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(shoppingCartTable);

      add(tablePane, BorderLayout.CENTER);   
      
      // listener
      addBtn.addActionListener(new AddListener()); 
   }
   
   public JPanel getButtonsPanel()
   {
      JPanel buttonsPanel = new JPanel();
      JButton confirmBtn = new JButton("確認");
      buttonsPanel.add(confirmBtn);
      JButton removeBtn = new JButton("移除");
      removeBtn.setToolTipText("使用老鼠鍵選擇單一項目,或搭配Shift鍵選擇一組項目或Ctrl鍵選擇多個項目");
      buttonsPanel.add(removeBtn);   
      JButton cancelBtn = new JButton("取消");
      buttonsPanel.add(cancelBtn);      
      
      confirmBtn.addActionListener(new ConfirmListener()); 
      removeBtn.addActionListener(new RemoveListener());
      cancelBtn.addActionListener(new CancelListener());      
      
      return buttonsPanel;
   }
      
   private Vector<String> getTableRow(String barcode, String name, double price, 
      int quantity, double total)
   {
      Vector<String> cols = new Vector<String>();
      cols.add(barcode);
      cols.add(name);
      cols.add(String.valueOf(price));  
      cols.add(String.valueOf(quantity));    
      cols.add(String.valueOf(total));       

      return cols;
   }   
   
   private void clean()
   {
      DefaultTableModel tableModel = (DefaultTableModel)shoppingCartTable.getModel();
      tableModel.setRowCount(0);
      addedItems.clear();
      purchaserField.setText("");   
      totalLabel.setText("0");
      transTotal = 0;
   }
   
   // action listener of Add button
   class AddListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String barcode = barcodeField.getText();
         if(Utility.isEmpty(barcode))
         {
            GuiUtil.messageDialog(POSPanel.this, "請輸入條碼");
            return;
         }   
         
         ItemInfo item = ItemsManager.getItem(barcode);
         if(item == null)
         {
            GuiUtil.messageDialog(POSPanel.this, "尋找的物品不存在");
            return;            
         }
         
         String quantityStr = quantityField.getText();
         if(Utility.isEmpty(quantityStr))
         {
            GuiUtil.messageDialog(POSPanel.this, "請輸入購買的數量");
            return;
         }
         
         int quantity = 1;
         try
         {
            quantity = Integer.parseInt(quantityStr);
         }
         catch(NumberFormatException ex)
         {
            GuiUtil.messageDialog(POSPanel.this, "購買數量須為整數");
            return;            
         }
         
         // check if this item has been added or not
         ItemInfo addedItem = addedItems.get(barcode);
         boolean sameItem = false;  // same item is already in the shopping cart
         int totalQuantity = 0;
         if(addedItem != null)
         {
            totalQuantity = addedItem.getAddedQuantity() + quantity;
            sameItem = true;
         }
         else
         {
            totalQuantity = quantity;
         }
         
         if(item.getQuantity() < totalQuantity)
         {
            GuiUtil.messageDialog(POSPanel.this, "存貨數量不夠,目前存貨數量僅為 " + 
               item.getQuantity());
            return;
         }        
    
         double total = item.getPrice()*totalQuantity;
         item.setAddedQuantity(totalQuantity);
         addedItems.put(barcode, item);         
         if(sameItem)
         {
            transTotal = transTotal + item.getPrice()*quantity;
            totalLabel.setText(String.valueOf(transTotal));    
            for(int i = 0; i < shoppingCartTable.getRowCount(); i++)
            {
               String str = (String)shoppingCartTable.getValueAt(i, COL_NUM_BARCODE);
               if(barcode.equals(str))
               {
                  shoppingCartTable.setValueAt(String.valueOf(totalQuantity), i, COL_NUM_QUANTITY);
                  shoppingCartTable.setValueAt(String.valueOf(total), i, COL_NUM_TOTAL);
                  break;
               }
            }
         }
         else
         {
            // new item
            transTotal = transTotal + total;
            totalLabel.setText(String.valueOf(transTotal));
            // insert new row into table model
            DefaultTableModel tableModel = (DefaultTableModel)shoppingCartTable.getModel();
            tableModel.insertRow(0, getTableRow(item.getBarcode(), item.getName(), 
               item.getPrice(), quantity, total));  
         }
         
         // reset
         barcodeField.setText("");
         quantityField.setText("1");                   
      }
   }  
   
   // action listener of Confirm button
   class ConfirmListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         if(addedItems.size() == 0)
            return;
         
         if(Utility.isEmpty(purchaserField.getText()))
         {
            GuiUtil.messageDialog(POSPanel.this, "請輸入購買者名字");
            return;
         }           
         
         Date processedDate = null;
         String dateStr = processedDateField.getText();
         if(!Utility.isEmpty(dateStr))
         {
            try
            {
               processedDate = Utility.dateParser(dateStr);
            }
            catch(ParseException ex)
            {
               GuiUtil.messageDialog(POSPanel.this, Messages.SUPPORTED_DATE_FORMATS);
               return;
            }
         }           
         
         int option = JOptionPane.showConfirmDialog(POSPanel.this,
               "總金額: " + transTotal + "\n購買者: " + purchaserField.getText() +
               "\n\n確定完成本交易?", "交易總結",
               JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

         if(option != JOptionPane.OK_OPTION)
            return;
         
         TransactionManager.commit(accountInfo.getUsername(), purchaserField.getText().trim(), transTotal,
            addedItems, processedDate);
         
         clean();
      }         
   }
   
   // action listener of Remove button
   class RemoveListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         int selectedRow = shoppingCartTable.getSelectedRow();
         if(selectedRow >= 0) // if selected
         {
            String name = (String)shoppingCartTable.getValueAt(selectedRow, COL_NUM_NAME);

            int option = JOptionPane.showConfirmDialog(POSPanel.this,
                  "移除選擇的物品: " + name, "移除",
                  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(option == JOptionPane.OK_OPTION)
            {  
               String barcode = (String)shoppingCartTable.getValueAt(selectedRow, COL_NUM_BARCODE);
               String totalStr = (String)shoppingCartTable.getValueAt(selectedRow, COL_NUM_TOTAL);

               transTotal = transTotal - Double.parseDouble(totalStr);
               totalLabel.setText(String.valueOf(transTotal)); 
               
               // remove selected row from table model
               DefaultTableModel tableModel = (DefaultTableModel)shoppingCartTable.getModel();
               tableModel.removeRow(selectedRow);
                  
               addedItems.remove(barcode);              
            }
         }
      }
   }  
   
   // action listener of Cancel button
   class CancelListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         int option = JOptionPane.OK_OPTION;
         if(addedItems.size() > 0)
         {           
            option = JOptionPane.showConfirmDialog(POSPanel.this,
               "確定取消本次交易?", "取消",
               JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         }

         if(option == JOptionPane.OK_OPTION)
         {       
            clean();
         }
      }
   }         
}
