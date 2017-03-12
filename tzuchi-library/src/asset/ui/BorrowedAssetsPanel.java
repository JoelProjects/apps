package asset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import members.ui.utils.GuiUtil;
import members.utils.Utility;
import asset.datamodel.MemberHistory;
import asset.datamodel.MemberHistoryManager;

/**
 * This class gets a list of borrowed assets of a member and displays them
 * in a table.
 * 
 * @author Cheng-Hung Chou
 * @since 10/23/2005
 */
public class BorrowedAssetsPanel extends JPanel
{  
   public final static int ASSET_NAME_COL = 0;    
   public final static int ASSET_TYPE_COL = 1;  
   public final static int CODE_COL = 2;   
   public final static int BAR_CODE_COL = 3;
   public final static int BORROWED_DATE_COL = 4;
   public final static int DUE_DATE_COL = 5;
   
   private JTable borrowedTable;
   
   public BorrowedAssetsPanel(long memberId)
   {
      setLayout(new BorderLayout());
      
      // borrowed assets table
      Vector headers = new Vector();
      headers.add(ASSET_NAME_COL, "Asset Name");
      headers.add(ASSET_TYPE_COL, "Asset Type");
      headers.add(CODE_COL, "Code");
      headers.add(BAR_CODE_COL, "Asset Barcode");
      headers.add(BORROWED_DATE_COL, "Borrowed Date");
      headers.add(DUE_DATE_COL, "Due Date");

      Connection con = null;
      Vector data = new Vector();      
      try
      {
        con = ContextManager.getConnection();
        MemberHistoryManager historyMgr = new MemberHistoryManager(con);
        List borrowedAssets = historyMgr.getCheckOutAssets(memberId);
      
        for(int i = 0; i < borrowedAssets.size(); i++)
        {
           MemberHistory history = (MemberHistory)borrowedAssets.get(i);
           data.add(getTableRow(history));
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
      borrowedTable = new JTable(dataModel);
      borrowedTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      borrowedTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      int[] widths = {350, 40, 50, 50, 60, 50}; // define column width
      GuiUtil.setColumnWidths(borrowedTable, widths);

      // moving table headers is not allowed
      borrowedTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(borrowedTable);

      add(tablePane, BorderLayout.CENTER);   
   }
   
   public JTable getBorrowedTable()
   {
      return borrowedTable;
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
}
