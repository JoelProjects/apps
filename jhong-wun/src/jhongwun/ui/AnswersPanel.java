package jhongwun.ui;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import jhongwun.util.GuiUtil;

/**
 *
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class AnswersPanel extends JPanel
{
   private DefaultTableModel dataModel;
   
   public AnswersPanel()
   {
      setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      
      Vector headers = new Vector();
      headers.add("#");
      headers.add("選擇答案");
      headers.add("正確答案");
      dataModel = new DefaultTableModel(null, headers);
      JTable assetTable = new JTable(dataModel);
      assetTable.setPreferredScrollableViewportSize(new Dimension(180, 430));
      int[] widths = {20, 80, 80}; // define column width
      GuiUtil.setColumnWidths(assetTable, widths);
      JScrollPane tablePane = new JScrollPane(assetTable);

      add(tablePane);      
   }
   
   public void addAnswerSet(Vector answerSet)
   {
      dataModel.insertRow(dataModel.getRowCount(), answerSet);
   }
}
