package asset.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import members.ui.utils.GuiUtil;
import asset.datamodel.MemberInfo;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 10/22/2005
 */
public class MemberListDialog extends JDialog
{
   private JTable membersTable;
   private List members;
   private MemberInfo member;
   
   public MemberListDialog(Frame owner, boolean modal, List members)
   {
      super(owner, "Searched Member List", modal);
      
      this.members = members;
      
      Container content = getContentPane();
      content.setLayout(new BorderLayout());
      
      // account table
      Vector headers = new Vector();
      headers.add("ID");
      headers.add("English Name");
      headers.add("Chinese Name");
      headers.add("Member Type");

      Vector data = new Vector();
      for(int i = 0; i < members.size(); i++)
      {
         MemberInfo member = (MemberInfo)members.get(i);
         data.add(getTableRow(member));
      }

      MyDefaultTableModel dataModel = new MyDefaultTableModel(data, headers);
      membersTable = new JTable(dataModel);
      membersTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
      membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {25, 50, 50, 20}; // define column width
      GuiUtil.setColumnWidths(membersTable, widths);

      // moving table headers is not allowed
      membersTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(membersTable);

      content.add(tablePane, BorderLayout.CENTER);

      // register action listeners
      membersTable.addMouseListener(new TableListener());  
   }
   
   public MemberInfo getSelectedMember()
   {
      return member;
   }
   
   private Vector getTableRow(MemberInfo member)
   {
      Vector cols = new Vector();
      cols.add(String.valueOf(member.getMemberId()));
      cols.add(member.getName());
      cols.add(member.getChineseName());   
      cols.add(ContextManager.getMemberType(member.getMemberTypeId()));                  

      return cols;
   }

   class MyDefaultTableModel extends DefaultTableModel
   {
      public MyDefaultTableModel(Vector data, Vector headers)
      {
         super(data, headers);
      }

      public boolean isCellEditable(int row, int col)
      {
         return false; // not editable
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
            int selectedRow = membersTable.getSelectedRow();
            member = (MemberInfo)members.get(selectedRow);
            setVisible(false);
         }
      }
   }   
}
