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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import members.ui.utils.GuiUtil;
import members.utils.Utility;
import asset.datamodel.MemberHistory;
import asset.datamodel.MemberHistoryManager;
import asset.datamodel.MemberInfo;
import asset.datamodel.MemberInfoManager;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/27/2005
 */
public class MemberManagementPanel extends JPanel
{
   private static final int COL_NUM_STATUS = 4;   
   private JTable membersTable;
   
   public MemberManagementPanel()
   {
      setLayout(new BorderLayout());

      // buttons
      JPanel buttonPanel = new JPanel();
      JButton newButton = new JButton("New");
      JButton deleteButton = new JButton("Delete");
      buttonPanel.add(newButton);
      buttonPanel.add(deleteButton);

      add(buttonPanel, BorderLayout.SOUTH);
      
      // account table
      Vector headers = new Vector();
      headers.add("ID");
      headers.add("English Name");
      headers.add("Chinese Name");
      headers.add("Member Type");
      headers.add("Status");

      Connection con = null;
      Vector data = new Vector();      
      try
      {
        con = ContextManager.getConnection();
        MemberInfoManager memberMgr = new MemberInfoManager(con);
        MemberHistoryManager historyMgr = new MemberHistoryManager(con);
        List members = memberMgr.getMembers();
      
        for(int i = 0; i < members.size(); i++)
        {
           MemberInfo member = (MemberInfo)members.get(i);
           List checkoutAssets = historyMgr.getCheckOutAssets(member.getMemberId());
           member.setCheckoutAssets(checkoutAssets);
           data.add(getTableRow(member));
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
      membersTable = new JTable(dataModel);
      membersTable.setDefaultRenderer(membersTable.getColumnClass(COL_NUM_STATUS), 
         new MemberStatusTableCellRender(COL_NUM_STATUS));      
      membersTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {20, 50, 50, 20, 50}; // define column width
      GuiUtil.setColumnWidths(membersTable, widths);

      // moving table headers is not allowed
      membersTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(membersTable);

      add(tablePane, BorderLayout.CENTER);

      // register action listeners
      membersTable.addMouseListener(new TableListener());  
      newButton.addActionListener(new NewListener());
      deleteButton.addActionListener(new DeleteListener());      
   }
   
   private Vector getTableRow(MemberInfo member)
   {
      Vector cols = new Vector();
      cols.add(String.valueOf(member.getMemberId()));
      cols.add(member.getName());
      cols.add(member.getChineseName());   
      cols.add(ContextManager.getMemberType(member.getMemberTypeId()));   
      List<MemberHistory> checkoutAssets = member.getCheckoutAssets();
      if(checkoutAssets == null || checkoutAssets.size() == 0)
         cols.add("");
      else
         cols.add("Checkout #: " + checkoutAssets.size());

      return cols;
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
            int selectedRow = membersTable.getSelectedRow();
            int selectedCol = membersTable.getSelectedColumn();
            String memberId = (String)membersTable.getValueAt(selectedRow, 0);
            
            switch(selectedCol) 
            {
               case COL_NUM_STATUS:
                  JDialog borrowedDialog = new BorrowedAssetsDialog(GuiUtil.getOwnerFrame(getParent()), 
                     true, Long.parseLong(memberId));
                  borrowedDialog.setSize(new Dimension(800, 400));
                  //borrowedDialog.pack();
                  borrowedDialog.setVisible(true);
                  borrowedDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);                  
                  break;
               default:
                  EditMemberInfoDialog dialog = null;            
                  Connection con = null;
                  try
                  {
                     con = ContextManager.getConnection();
                     MemberInfoManager memberMgr = new MemberInfoManager(con);
                     MemberInfo member = memberMgr.getMemberById(Long.parseLong(memberId));
               
                     // bring up the edit dialog
                     dialog =
                        new EditMemberInfoDialog(GuiUtil.getOwnerFrame(getParent()), true, member.getName());
                     dialog.setMemberId(member.getMemberId());
                     dialog.setName(member.getName());
                     dialog.setChineseName(member.getChineseName());
                     dialog.setHomePhone(member.getHomePhone());
                     dialog.setCellPhone(member.getCellPhone());
                     dialog.setAddress(member.getAddress());
                     dialog.setEmail(member.getEmail());
                     dialog.setMemberType(ContextManager.getMemberType(member.getMemberTypeId()));

                     dialog.pack();
                     dialog.setVisible(true);
                     dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                     if(dialog.hasChanged())
                     {    
                        member.setMemberId(dialog.getMemberId());
                        member.setName(dialog.getName());
                        member.setChineseName(dialog.getChineseName());
                        member.setHomePhone(dialog.getHomePhone());
                        member.setCellPhone(dialog.getCellPhone());
                        member.setAddress(dialog.getAddress());
                        member.setEmail(dialog.getEmail());
                        member.setMemberTypeId(dialog.getMemberType().getId());
                   
                        // update db
                        memberMgr.update(member);
                   
                        // update table                 
                        membersTable.setValueAt(member.getName(), selectedRow, 1);    
                        membersTable.setValueAt(member.getChineseName(), selectedRow, 2); 
                        membersTable.setValueAt(ContextManager.getMemberType(member.getMemberTypeId()), 
                           selectedRow, 3);                  
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
   }

   // action listener of New button
   class NewListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         // bring the edit dialog
         EditMemberInfoDialog dialog = new EditMemberInfoDialog(GuiUtil
               .getOwnerFrame(getParent()), true);
         dialog.setMemberId(Long.parseLong(Utility.getLongDateTimeStr()));
         dialog.pack();
         dialog.setVisible(true);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

         if(dialog.hasChanged())
         {
            MemberInfo member = new MemberInfo();
            member.setMemberId(dialog.getMemberId());
            member.setName(dialog.getName());
            member.setChineseName(dialog.getChineseName());
            member.setHomePhone(dialog.getHomePhone());
            member.setCellPhone(dialog.getCellPhone());
            member.setAddress(dialog.getAddress());
            member.setEmail(dialog.getEmail());
            member.setMemberTypeId(dialog.getMemberType().getId()); 
            
            Connection con = null;
            try
            {
               // insert into the db
               con = ContextManager.getConnection();            
               MemberInfoManager memberMgr = new MemberInfoManager(con);
               memberMgr.insert(member);
               
               // insert new row into table model
               DefaultTableModel tableModel = (DefaultTableModel)membersTable.getModel();
               tableModel.insertRow(0, getTableRow(member));          
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
         int selectedRow = membersTable.getSelectedRow();

         if(selectedRow >= 0) // if selected
         {
            // get selected member ID
            String memberId = (String)membersTable.getValueAt(selectedRow, 0);
            String name = (String)membersTable.getValueAt(selectedRow, 2);

            int option = JOptionPane.showConfirmDialog(MemberManagementPanel.this,
                  "Delete Member " + name, "Delete Member",
                  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(option == JOptionPane.OK_OPTION)
            {
               Connection con = null;
               try
               {
                  con = ContextManager.getConnection();
                  MemberInfoManager memberMgr = new MemberInfoManager(con);
                  memberMgr.delete(Long.parseLong(memberId));         
                  
                  // remove selected row from table model
                  DefaultTableModel tableModel = (DefaultTableModel)membersTable.getModel();
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
}
