package asset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import asset.datamodel.Asset;
import asset.datamodel.AssetManager;
import asset.datamodel.AssetStatus;
import asset.datamodel.MemberHistoryManager;

/**
 * This class is for the function Borrow.
 * 
 * @author Cheng-Hung Chou
 * @since 10/24/2005
 */
public class BorrowAssetsPanel extends AbstractAssetPanel
{  
   private long memberId = -1;
   private JTextField barcodeField;
   private JTable addedTable;
   private JComboBox dueDateCombo;
   private List selectedAssetIds = new ArrayList();  // a list of asset IDs for assets to be borrowed
   private List renewAssetIds = new ArrayList();  // a list of asset IDs for assets to be renewed
   
   public BorrowAssetsPanel(long memberId, String[] barcodes)
   {
      this.memberId = memberId;
      
      setLayout(new BorderLayout());
      
      // borrow panel
      JPanel borrowPanel = new JPanel();
      borrowPanel.add(new JLabel("Barcode: "));
      barcodeField = new JTextField(20);
      borrowPanel.add(barcodeField);
      JButton addBtn = new JButton("Add Borrowing");
      borrowPanel.add(addBtn);
      borrowPanel.add(new JLabel("   Due Date: "));
      dueDateCombo = new JComboBox(getDueDateList());
      borrowPanel.add(dueDateCombo);
      
      add(borrowPanel, BorderLayout.NORTH);   
      
      // table for assets to be borrowed
      Vector headers = new Vector();
      headers.add("Asset Name");
      headers.add("Asset Type");
      headers.add("Code");
      headers.add("Asset Barcode");

      Vector data = new Vector();
      NotEditableTableModel dataModel = new NotEditableTableModel(data, headers);
      addedTable = new JTable(dataModel);
      addedTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      addedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {400, 50, 50, 50}; // define column width
      GuiUtil.setColumnWidths(addedTable, widths);

      // moving table headers is not allowed
      addedTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(addedTable);

      add(tablePane, BorderLayout.CENTER);   
      
      // listener
      addBtn.addActionListener(new AddListener()); 
      
      // initialize table if there is any selected item for renewing
      if(barcodes != null)
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
            AssetManager assetMgr = new AssetManager(con);
            for(int i = 0; i < barcodes.length; i++)
            {  
               Asset asset = assetMgr.getAsset(barcodes[i]);
               DefaultTableModel tableModel = (DefaultTableModel)addedTable.getModel();
               tableModel.insertRow(i, getTableRow(asset));  
               renewAssetIds.add(new Integer(asset.getAssetId()));  
               selectedAssetIds.add(new Integer(asset.getAssetId()));               
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
   
   private Vector getTableRow(Asset asset)
   {
      Vector cols = new Vector();
      cols.add(asset.getName());
      cols.add(ContextManager.getAssetType(asset.getAssetTypeId()));  
      cols.add(asset.getAssetCode());    
      cols.add(asset.getAssetBarcode());       

      return cols;
   }   
   
   private Calendar getDefaultDueDate()
   {
      Calendar now = Calendar.getInstance();
      now.add(Calendar.DATE, Constants.CHECKOUT_PERIOD);
      
      return now;
   }
   
   private Vector getDueDateList()
   {
      Vector list = new Vector();
      Calendar now = getDefaultDueDate();
      list.add(Utility.getDateString(now.getTime()));
      for(int i = 0; i < ContextManager.getOutWeeks(); i++)
      {
         now.add(Calendar.DATE, Constants.CHECKOUT_PERIOD_INT);
         list.add(Utility.getDateString(now.getTime()));
      }
      
      return list;
   }
   
   // action listener of Add button
   class AddListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String barcode = barcodeField.getText();
         if(Utility.isEmpty(barcode))
         {
            GuiUtil.messageDialog(BorrowAssetsPanel.this, "Barcode is empty. Please provide the barcode.");
            return;
         }   
         
         Connection con = null;
         try
         {
            con = ContextManager.getConnection();
            AssetManager assetMgr = new AssetManager(con);
            Asset asset = assetMgr.getAsset(barcode.trim());
            
            if(asset == null)
            {
               GuiUtil.messageDialog(BorrowAssetsPanel.this, "Asset Not Found. Barcode: " + barcode);
               return;
            }
            
            if(asset.getAssetStatusId() != AssetStatus.AVAILABLE)
            {
               GuiUtil.messageDialog(BorrowAssetsPanel.this, "Asset Not Available. Barcode: " + barcode);
               return;
            }            
               
            if(selectedAssetIds.contains(new Integer(asset.getAssetId())))
            {
               GuiUtil.messageDialog(BorrowAssetsPanel.this, "Duplicate Asset. Barcode: " + barcode);
               return;
            }             
            
            // insert new row into table model
            DefaultTableModel tableModel = (DefaultTableModel)addedTable.getModel();
            tableModel.insertRow(0, getTableRow(asset));  
            
            selectedAssetIds.add(new Integer(asset.getAssetId()));
            
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
         
         int option = JOptionPane.showConfirmDialog(BorrowAssetsPanel.this,
               "Please verify the due date before you submit the borrowing asset(s).", "Borrow",
               JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

         if(option != JOptionPane.OK_OPTION)
            return;
         
         Date dueDate = null;
         try
         {
            String dueDateStr = (String)dueDateCombo.getSelectedItem();  
            dueDate = Utility.dateParser(dueDateStr, Utility.DEFAULT_DATE_PATTERN);
         }
         catch(ParseException e)
         {
            System.out.println(e);
            dueDate = new Date();
         }
         
         Connection con = null;
         try
         {
            // insert into member history and update asset status
            con = ContextManager.getConnection();            
            MemberHistoryManager historyMgr = new MemberHistoryManager(con);
            
            historyMgr.checkOutAssets(memberId, selectedAssetIds, renewAssetIds, 
               dueDate);      
            
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

            int option = JOptionPane.showConfirmDialog(BorrowAssetsPanel.this,
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
            option = JOptionPane.showConfirmDialog(BorrowAssetsPanel.this,
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
