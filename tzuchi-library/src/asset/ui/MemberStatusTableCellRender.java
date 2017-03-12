package asset.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import members.utils.Utility;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 10/19/2008
 */
public class MemberStatusTableCellRender extends DefaultTableCellRenderer
{
   private int statusColumn;

   public MemberStatusTableCellRender(int statusColumn)
   {
      super();
      
      this.statusColumn = statusColumn;
   }

   public Component getTableCellRendererComponent(JTable table, Object value, 
         boolean isSelected, boolean hasFocus, int row, int column)
   {
      Component render = super.getTableCellRendererComponent(table, value, 
         isSelected, hasFocus, row, column);
      
      // based on the status
      String status = table.getValueAt(row, statusColumn).toString();
      if(!Utility.isEmpty(status))
         render.setBackground(Color.YELLOW);
      else
         render.setBackground(Constants.DEFAULT_BACKGROUND);

      return render;
   }
}
