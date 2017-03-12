package asset.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import asset.datamodel.AssetStatus;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 10/30/2005
 */
public class AssetStatusTableCellRender extends DefaultTableCellRenderer
{
   private int statusColumn;

   public AssetStatusTableCellRender(int statusColumn)
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
      int statusId = ContextManager.getAssetStatusId(status);

      switch(statusId)
      {
         case AssetStatus.AVAILABLE:
            render.setForeground(Constants.COLOR_AVAILABLE);
            render.setBackground(Constants.DEFAULT_BACKGROUND);
            break;
         case AssetStatus.CHECK_OUT:
            render.setForeground(Constants.COLOR_CHECK_OUT);
            render.setBackground(Constants.DEFAULT_BACKGROUND);
            break;
         case AssetStatus.OVER_DUE:
            render.setForeground(Constants.COLOR_OVER_DUE);
            render.setBackground(Constants.DEFAULT_BACKGROUND);
            break;
         case AssetStatus.MISSING:
            render.setForeground(Constants.DEFAULT_FOREGROUND);
            render.setBackground(Constants.COLOR_MISSING);
            break;
         default:
            render.setForeground(Constants.DEFAULT_FOREGROUND);
            render.setBackground(Constants.DEFAULT_BACKGROUND);            
            break;
      }

      return render;
   }
}
