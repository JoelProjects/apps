package asset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import members.ui.utils.GuiUtil;
import members.utils.Utility;
import asset.datamodel.MemberHistory;
import asset.datamodel.MemberHistoryManager;

/**
 * This class is for the function Return.
 * 
 * @author Cheng-Hung Chou
 * @since 10/25/2005
 */
public class ReturnAssetsPanel extends AbstractAssetPanel
{  
   private long memberId = -1;
   private JTextField barcodeField;
   private JTable addedTable;
   private List selectedAssetIds = new ArrayList();  // a list of asset IDs for assets to be borrowed
   
   public ReturnAssetsPanel(long memberId, String[] barcodes)
   {
      this.memberId = memberId;
      
      setLayout(new BorderLayout());
      
      // return panel
      JPanel returnPanel = new JPanel();
      returnPanel.add(new JLabel("Barcode: "));
      barcodeField = new JTextField(20);
      returnPanel.add(barcodeField);
      JButton addBtn = new JButton("Add Returning");
      returnPanel.add(addBtn);
      
      add(returnPanel, BorderLayout.NORTH);   
      
      // table for assets to be borrowed
      Vector headers = new Vector();
      headers.add("Asset Name");
      headers.add("Asset Type");
      headers.add("Code");
      headers.add("Asset Barcode");
      headers.add("Borrowed Date");
      headers.add("Due Date");

      Vector data = new Vector();
      NotEditableTableModel dataModel = new NotEditableTableModel(data, headers);
      addedTable = new JTable(dataModel);
      addedTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      addedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {350, 40, 50, 50, 60, 50}; // define column width
      GuiUtil.setColumnWidths(addedTable, widths);

      // moving table headers is not allowed
      addedTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(addedTable);

      add(tablePane, BorderLayout.CENTER);   
      
      // listener
      addBtn.addActionListener(new AddListener());    
      
      // initialize table if there is any selected item
      initTable(barcodes);
   }
   
   public JPanel getButtonsPanel()
   {
      JPanel buttonsPanel = new JPanel();
      JButton confirmBtn = new JButton("Confirm");
      buttonsPanel.add(confirmBtn);
      JButton removeBtn = new JButton("Remove");
      buttonsPanel.add(removeBtn);   
      JButton cancelBtn = new JButton("Cancel");
      buttonsPanel.add(cancelBtn);      
      
      confirmBtn.addActionListener(new ConfirmListener()); 
      removeBtn.addActionListener(new RemoveListener());
      cancelBtn.addActionListener(new CancelListener());      
      
      return buttonsPanel;
   }
   
   private void initTable(String[] barcodes)
   {
      if(barcodes != null)
      {
         Connection con = null;
         try
         {
            con = ContextManager.getConnection();
            MemberHistoryManager historyMgr = new MemberHistoryManager(con);
            for(int i = 0; i < barcodes.length; i++)
            {            
               MemberHistory history = historyMgr.getCheckOutAsset(barcodes[i], memberId);
               
               // insert new row into table model
               DefaultTableModel tableModel = (DefaultTableModel)addedTable.getModel();
               tableModel.insertRow(0, getTableRow(history));  
               
               selectedAssetIds.add(new Integer(history.getAsset().getAssetId()));
            }
         }
         catch(Exception e)
         {
            System.out.println(e);
         }
         finally
         {
            try 
            {
               if(con != null)
                  con.close();
            } 
            catch(Exception e){};
         }                           
      }
   }
   
   private Vector getTableRow(MemberHistory history)
   {
      Vector cols = new Vector();
      cols.add(history.getAsset().getName());
      cols.add(ContextManager.getAssetType(history.getAsset().getAssetTypeId()));  
      cols.add(history.getAsset().getAssetCode());    
      cols.add(history.getAsset().getAssetBarcode());     
      cols.add(Utility.getDateTimeString(history.getCheckoutDate()));   
      cols.add(Utility.getDateString(history.getDueDate()));         

      return cols;
   }   
      
   // action listener of Add button
   class AddListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String barcode = barcodeField.getText();
         if(Utility.isEmpty(barcode))
         {
            GuiUtil.messageDialog(ReturnAssetsPanel.this, "Barcode is empty. Please provide the barcode.");
            return;
         }   
         
         Connection con = null;
         try
         {
            con = ContextManager.getConnection();
            MemberHistoryManager historyMgr = new MemberHistoryManager(con);
            MemberHistory history = historyMgr.getCheckOutAsset(barcode.trim(), memberId);
            
            if(history == null)
            {
               GuiUtil.messageDialog(ReturnAssetsPanel.this, "Asset Not Found or Not Borrowed. Barcode: " + barcode);
               return;
            }
            
            if(history.isCheckedIn())
            {
               GuiUtil.messageDialog(ReturnAssetsPanel.this, "Asset Not Borrowed. Barcode: " + barcode);
               return;
            }            
               
            if(selectedAssetIds.contains(new Integer(history.getAsset().getAssetId())))
            {
               GuiUtil.messageDialog(ReturnAssetsPanel.this, "Duplicate Asset. Barcode: " + barcode);
               return;
            }             
            
            // insert new row into table model
            DefaultTableModel tableModel = (DefaultTableModel)addedTable.getModel();
            tableModel.insertRow(0, getTableRow(history));  
            
            selectedAssetIds.add(new Integer(history.getAsset().getAssetId()));
            
            // reset bracode field
            barcodeField.setText("");
         }
         catch(Exception e)
         {
            System.out.println(e);
         }
         finally
         {
            try 
            {
               if(con != null)
                  con.close();
            } 
            catch(Exception e){};
         }                        
      }
   }  
   
   // action listener of Confirm button
   class ConfirmListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         if(selectedAssetIds.size() == 0)
            return;
         
         int option = JOptionPane.showConfirmDialog(ReturnAssetsPanel.this,
               "Return the asset(s)?", "Return",
               JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

         if(option != JOptionPane.OK_OPTION)
            return;
                  
         Connection con = null;
         try
         {
            // update history and asset status
            con = ContextManager.getConnection();            
            MemberHistoryManager historyMgr = new MemberHistoryManager(con);
            
            historyMgr.checkInAssets(memberId, selectedAssetIds, new Date());      
            
            // back to the borrowed list
            ((ServiceCenterPanel)mainPanel).displayMainPanel(memberId);            
         }
         catch(Exception e)
         {
            System.out.println(e);
         }
         finally
         {
            try
            {
               if(con != null)
                  con.close();
            }
            catch(Exception e){}
         }              
      }         
   }
   
   // action listener of Remove button
   class RemoveListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         int selectedRow = addedTable.getSelectedRow();

         if(selectedRow >= 0) // if selected
         {
            String name = (String)addedTable.getValueAt(selectedRow, 0);

            int option = JOptionPane.showConfirmDialog(ReturnAssetsPanel.this,
                  "Remove selected asset: " + name, "Remove",
                  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(option == JOptionPane.OK_OPTION)
            {  
               // remove selected row from table model
               DefaultTableModel tableModel = (DefaultTableModel)addedTable.getModel();
               tableModel.removeRow(selectedRow);
                  
               selectedAssetIds.remove(selectedRow);
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
         if(selectedAssetIds.size() > 0)
         {  
            option = JOptionPane.showConfirmDialog(ReturnAssetsPanel.this,
               "Cancel and go back to previous page?", "Cancel",
               JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         }

         if(option == JOptionPane.OK_OPTION)
         {  
            // back to the borrowed list
            ((ServiceCenterPanel)mainPanel).displayMainPanel(memberId);               
         }
      }
   }         
}
