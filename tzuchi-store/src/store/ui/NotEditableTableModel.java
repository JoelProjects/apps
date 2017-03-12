/* $Id: NotEditableTableModel.java,v 1.4 2008/04/07 05:29:22 joelchou Exp $ */
package store.ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class NotEditableTableModel extends DefaultTableModel
{
   public NotEditableTableModel(Vector data, Vector headers)
   {
      super(data, headers);
   }
   
   public NotEditableTableModel(Object[][] data, Object [] headers)
   {
      super(data, headers);
   }

   public boolean isCellEditable(int row, int col)
   {
      return false; // not editable
   }
}
