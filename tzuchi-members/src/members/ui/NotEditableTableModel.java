package members.ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

class NotEditableTableModel extends DefaultTableModel
{
   public NotEditableTableModel(Vector data, Vector headers)
   {
      super(data, headers);
   }

   public boolean isCellEditable(int row, int col)
   {
      return false; // not editable
   }
}
