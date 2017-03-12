package asset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import members.ui.utils.GuiUtil;
import asset.datamodel.Asset;
import asset.datamodel.AssetCategory;
import asset.datamodel.AssetManager;
import asset.datamodel.AssetStatus;
import asset.datamodel.AssetType;
import asset.datamodel.ExportManager;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public class AssetManagementPanel extends JPanel
{
   private static final int COL_NUM_STATUS = 6;
   
   private static Vector assetCategoryList;
   private static Vector assetTypeList;
   private static Vector assetStatusList;
   
   private JTextField searchField;
   private JComboBox catCombo;
   private JComboBox typeCombo;
   private JComboBox statusCombo;
   
   static
   {
      assetCategoryList = (Vector)ContextManager.getAssetCategoryList().clone();
      AssetCategory category = new AssetCategory();
      category.setId(-1);
      category.setName("All");
      assetCategoryList.add(0, category);
      
      assetTypeList = (Vector)ContextManager.getAssetTypeList().clone();
      AssetType type = new AssetType();
      type.setId(-1);
      type.setName("All");      
      assetTypeList.add(0, type);
      
      assetStatusList = (Vector)ContextManager.getAssetStatusList().clone();
      AssetStatus status = new AssetStatus();
      status.setId(-1);
      status.setName("All");       
      assetStatusList.add(0, status);      
   }
   
   private JTable assetTable;

   public AssetManagementPanel()
   {
      setLayout(new BorderLayout());

      // buttons
      JPanel buttonPanel = new JPanel();
      JButton newButton = new JButton("New");
      JButton deleteButton = new JButton("Delete");
      JButton copyButton = new JButton("Copy");   
      JButton exportButton = new JButton("Export"); 
      buttonPanel.add(newButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(copyButton);
      buttonPanel.add(exportButton);

      add(buttonPanel, BorderLayout.SOUTH);

      // combo boxes
      JPanel comboPanel = new JPanel();

      JPanel searchPanel = new JPanel();
      JLabel searchLabel = new JLabel("Name/Barcode");
      searchField = new JTextField(15);
      searchPanel.add(searchLabel);
      searchPanel.add(searchField);      
      
      JPanel catPanel = new JPanel();
      JLabel catLabel = new JLabel("Category");
      catCombo = new JComboBox(assetCategoryList);
      catPanel.add(catLabel);
      catPanel.add(catCombo);

      JPanel typePanel = new JPanel();
      JLabel typeLabel = new JLabel("Type");
      typeCombo = new JComboBox(assetTypeList);
      catPanel.add(typeLabel);
      catPanel.add(typeCombo);

      JPanel statusPanel = new JPanel();
      JLabel statusLabel = new JLabel("Status");
      statusCombo = new JComboBox(assetStatusList);
      catPanel.add(statusLabel);
      catPanel.add(statusCombo);

      JButton refreshButton = new JButton("Refresh");      
      
      comboPanel.add(searchPanel);
      comboPanel.add(catPanel);
      comboPanel.add(typePanel);
      comboPanel.add(statusPanel);
      comboPanel.add(refreshButton);

      add(comboPanel, BorderLayout.NORTH);

      // asset table
      Vector headers = new Vector();
      headers.add("ID");
      headers.add("Barcode");
      headers.add("Code");
      headers.add("Name");
      headers.add("Category");
      headers.add("Type");
      headers.add("Status");

      Connection con = null;
      Vector data = new Vector();      
      try
      {
        con = ContextManager.getConnection();
        AssetManager assetMgr = new AssetManager(con);
        List assets = assetMgr.getAssets(null, -1, -1, -1);
      
        for(int i = 0; i < assets.size(); i++)
        {
           Asset asset = (Asset)assets.get(i);
           data.add(getTableRow(asset));
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
         catch(Exception e){}
      }

      NotEditableTableModel dataModel = new NotEditableTableModel(data, headers);
      assetTable = new JTable(dataModel);
      assetTable.setDefaultRenderer(assetTable.getColumnClass(COL_NUM_STATUS), 
         new AssetStatusTableCellRender(COL_NUM_STATUS));
      assetTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      assetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {0, 50, 50, 350, 50, 50, 50}; // define column width
      GuiUtil.setColumnWidths(assetTable, widths);

      // moving table headers is not allowed
      assetTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(assetTable);

      add(tablePane, BorderLayout.CENTER);

      // register action listeners
      assetTable.addMouseListener(new TableListener());
      newButton.addActionListener(new NewListener());
      deleteButton.addActionListener(new DeleteListener());
      copyButton.addActionListener(new CopyListener());
      exportButton.addActionListener(new ExportListener());
      refreshButton.addActionListener(new RefreshListener());    
   }
   
   private Vector getTableRow(Asset asset)
   {
      Vector cols = new Vector();
      cols.add(Integer.toString(asset.getAssetId()));
      cols.add(asset.getAssetBarcode());
      cols.add(asset.getAssetCode());
      cols.add(asset.getName());
      cols.add(ContextManager.getAssetCategory(asset.getAssetCategoryId()));   
      cols.add(ContextManager.getAssetType(asset.getAssetTypeId()));                  
      cols.add(ContextManager.getAssetStatus(asset.getAssetStatusId())); 

      return cols;
   }

   class RefreshListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         String name = searchField.getText();
         AssetCategory category = (AssetCategory)catCombo.getSelectedItem();
         AssetType type = (AssetType)typeCombo.getSelectedItem();
         AssetStatus status = (AssetStatus)statusCombo.getSelectedItem();
         
         Connection con = null;
         Vector data = new Vector();      
         try
         {
           con = ContextManager.getConnection();
           AssetManager assetMgr = new AssetManager(con);
           List assets = assetMgr.getAssets(name, category.getId(), 
                    type.getId(), status.getId());
           
           // clear table           
           DefaultTableModel tableModel = (DefaultTableModel)assetTable.getModel();
           tableModel.setRowCount(0);
         
           for(int i = 0; i < assets.size(); i++)
           {
              Asset asset = (Asset)assets.get(i);
              tableModel.addRow(getTableRow(asset));
           }
         }
         catch(Exception ex)
         {
            System.out.println(ex);
         }
         finally
         {
            try
            {
               if(con != null)
                  con.close();
            }
            catch(Exception ex){}
         }         
      }
   }   
   
   /**
    * This is a table listener for double clicking on table row.
    */
   class TableListener extends MouseAdapter
   {
      public void mouseClicked(MouseEvent event)
      {
         if(event.getClickCount() == 2) // double-click
         {
            int selectedRow = assetTable.getSelectedRow();
            String assetId = (String)assetTable.getValueAt(selectedRow, 0);
            
            EditAssetDialog dialog = null;            
            Connection con = null;
            try
            {
               con = ContextManager.getConnection();
               AssetManager assetMgr = new AssetManager(con);
               Asset asset = assetMgr.getAsset(Integer.parseInt(assetId));
               
               // bring up the edit dialog
               dialog =
                  new EditAssetDialog(GuiUtil.getOwnerFrame(getParent()), true, asset.getName());
               dialog.setAssetCode(asset.getAssetCode());
               dialog.setAssetBarcode(asset.getAssetBarcode());
               dialog.setVendorBarcode(asset.getVendorBarcode());
               dialog.setAssetName(asset.getName());
               dialog.setPublishingCompany(asset.getPublishingCompany());
               dialog.setAuthor(asset.getAuthor());
               dialog.setPublishedDate(asset.getPublishedDate());
               dialog.setCategory(ContextManager.getAssetCategory(asset.getAssetCategoryId()));
               dialog.setType(ContextManager.getAssetType(asset.getAssetTypeId()));
               dialog.setStatus(ContextManager.getAssetStatus(asset.getAssetStatusId()));

               dialog.pack();
               dialog.setVisible(true);
               dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

               if(dialog.hasChanged())
               {    
                  asset.setAssetCode(dialog.getAssetCode());
                  asset.setName(dialog.getAssetName());
                  asset.setAssetBarcode(dialog.getAssetBarcode());
                  asset.setVendorBarcode(dialog.getVendorBarcode());
                  asset.setPublishingCompany(dialog.getPublishingCompany());
                  asset.setAuthor(dialog.getAuthor());
                  asset.setPublishedDate(dialog.getPublishedDate());                  
                  asset.setAssetCategoryId(dialog.getCategory().getId());
                  asset.setAssetTypeId(dialog.getType().getId());
                  asset.setAssetStatusId(dialog.getStatus().getId());
                   
                  // update db
                  assetMgr.update(asset);
                   
                  // update table
                  assetTable.setValueAt(asset.getAssetBarcode(), selectedRow, 1);
                  assetTable.setValueAt(asset.getAssetCode(), selectedRow, 2);                   
                  assetTable.setValueAt(asset.getName(), selectedRow, 3);    
                  assetTable.setValueAt(ContextManager.getAssetCategory(asset.getAssetCategoryId()), 
                        selectedRow, 4);   
                  assetTable.setValueAt(ContextManager.getAssetType(asset.getAssetTypeId()), 
                        selectedRow, 5);                  
                  assetTable.setValueAt(ContextManager.getAssetStatus(asset.getAssetStatusId()), 
                        selectedRow, 6);                  
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
            
            dialog.dispose();
         }
      }
   }

   // action listener of New button
   class NewListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         // bring the edit dialog
         EditAssetDialog dialog = new EditAssetDialog(GuiUtil
               .getOwnerFrame(getParent()), true);
         dialog.pack();
         dialog.setVisible(true);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

         if(dialog.hasChanged())
         {
            Asset asset = new Asset();
            asset.setAssetCode(dialog.getAssetCode());
            asset.setName(dialog.getAssetName());
            asset.setAssetBarcode(dialog.getAssetBarcode());
            asset.setVendorBarcode(dialog.getVendorBarcode());
            asset.setPublishingCompany(dialog.getPublishingCompany());
            asset.setAuthor(dialog.getAuthor());
            asset.setPublishedDate(dialog.getPublishedDate());
            asset.setAssetCategoryId(dialog.getCategory().getId());
            asset.setAssetTypeId(dialog.getType().getId());
            asset.setAssetStatusId(dialog.getStatus().getId());            
            
            Connection con = null;
            try
            {
               // insert into the db
               con = ContextManager.getConnection();            
               AssetManager assetMgr = new AssetManager(con);
               int assetId = assetMgr.insert(asset);
               asset.setAssetId(assetId);
               
               // insert new row into table model
               DefaultTableModel tableModel = (DefaultTableModel)assetTable.getModel();
               tableModel.insertRow(0, getTableRow(asset));          
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
   }

   // action listener of Delete button
   class DeleteListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         int selectedRow = assetTable.getSelectedRow();

         if(selectedRow >= 0) // if selected
         {
            // get selected asset ID
            String assetId = (String)assetTable.getValueAt(selectedRow, 0);
            String assetName = (String)assetTable.getValueAt(selectedRow, 2);

            int option = JOptionPane.showConfirmDialog(AssetManagementPanel.this,
                  "Delete Asset " + assetName, "Delete Asset",
                  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(option == JOptionPane.OK_OPTION)
            {
               Connection con = null;
               try
               {
                  con = ContextManager.getConnection();
                  AssetManager assetMgr = new AssetManager(con);
                  assetMgr.delete(Integer.parseInt(assetId));         
                  
                  // remove selected row from table model
                  DefaultTableModel tableModel = (DefaultTableModel)assetTable.getModel();
                  tableModel.removeRow(selectedRow);
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
      }
   }
   
   // action listener of Copy button
   class CopyListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         int selectedRow = assetTable.getSelectedRow();

         if(selectedRow >= 0) // if selected
         {
            // get selected asset ID
            String assetId = (String)assetTable.getValueAt(selectedRow, 0);
            EditAssetDialog dialog = null;            
            Connection con = null;
            try
            {
               con = ContextManager.getConnection();
               AssetManager assetMgr = new AssetManager(con);
               Asset asset = assetMgr.getAsset(Integer.parseInt(assetId));
               
               // bring up the edit dialog
               dialog =
                  new EditAssetDialog(GuiUtil.getOwnerFrame(getParent()), true, asset.getName());
               dialog.setAssetCode(asset.getAssetCode());
               dialog.setVendorBarcode(asset.getVendorBarcode());
               dialog.setAssetName(asset.getName());
               dialog.setPublishingCompany(asset.getPublishingCompany());
               dialog.setAuthor(asset.getAuthor());
               dialog.setPublishedDate(asset.getPublishedDate());
               dialog.setCategory(ContextManager.getAssetCategory(asset.getAssetCategoryId()));
               dialog.setType(ContextManager.getAssetType(asset.getAssetTypeId()));
               dialog.setStatus(ContextManager.getAssetStatus(AssetStatus.AVAILABLE));

               dialog.pack();
               dialog.setVisible(true);
               dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

               if(dialog.hasChanged())
               {    
                  asset.setAssetCode(dialog.getAssetCode());
                  asset.setName(dialog.getAssetName());
                  asset.setAssetBarcode(dialog.getAssetBarcode());
                  asset.setVendorBarcode(dialog.getVendorBarcode());
                  asset.setPublishingCompany(dialog.getPublishingCompany());
                  asset.setAuthor(dialog.getAuthor());
                  asset.setPublishedDate(dialog.getPublishedDate());                  
                  asset.setAssetCategoryId(dialog.getCategory().getId());
                  asset.setAssetTypeId(dialog.getType().getId());
                  asset.setAssetStatusId(dialog.getStatus().getId());
                   
                  int newAssetId = assetMgr.insert(asset);
                  asset.setAssetId(newAssetId);
                  
                  // insert new row into table model
                  DefaultTableModel tableModel = (DefaultTableModel)assetTable.getModel();
                  tableModel.insertRow(0, getTableRow(asset));                 
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
            
            dialog.dispose();            
         }
      }
   }
   
   // action listener of Export button
   class ExportListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         Connection con = null;
         try
         {
            con = ContextManager.getConnection();     
            ExportManager exportMgr = new ExportManager(con);
            exportMgr.export();
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
}
