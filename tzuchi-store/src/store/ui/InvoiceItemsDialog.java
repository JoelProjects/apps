/* $Id: InvoiceItemsDialog.java,v 1.4 2008/03/23 21:09:53 joelchou Exp $ */
package store.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import members.ui.utils.GuiUtil;
import members.utils.Utility;

import org.apache.log4j.Logger;

import store.datamodel.Invoice;
import store.datamodel.InvoiceItem;
import store.datamodel.ItemInfo;
import store.manager.ReportsManager;

/**
 * A dialog to display items in an invoice.
 * 
 * @author Cheng-Hung Chou
 * @since 3/5/2008
 *
 */
public class InvoiceItemsDialog extends JDialog
{
   private static Logger logger = Logger.getLogger(InvoiceItemsDialog.class);
   
   private Invoice invoice;
   private boolean hasSectionB = false;
   
   public InvoiceItemsDialog(Frame owner, boolean modal, Invoice invoice)
   {
      super(owner, invoice.getInvoiceNumber(), modal);
      this.invoice = invoice;
      init();      
   }
   
   public void init()
   {
      setLayout(new BorderLayout());
      
      Vector<String> headers = new Vector<String>();
      headers.add("編號");
      headers.add("名稱");
      headers.add("單價");
      headers.add("數量");
      headers.add("類別");

      Vector<Vector<String>> data = new Vector<Vector<String>>();
      Iterator it = invoice.getItems().iterator();
      while(it.hasNext())
      {
         InvoiceItem item = (InvoiceItem)it.next();
         data.add(getTableRow(item));
         String itemNo = item.getItemInfo().getItemNo();
         // check if there are any items in section B
         if(!Utility.isEmpty(itemNo))
         {
            if(!hasSectionB && itemNo.toLowerCase().endsWith("b"))
               hasSectionB = true;
         }
      }

      NotEditableTableModel dataModel = new NotEditableTableModel(data, headers);
      JTable itemsTable = new JTable(dataModel);
      itemsTable.setPreferredScrollableViewportSize(new Dimension(600, 200));
      itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      int[] widths = {80, 370, 50, 40, 60}; // define column width
      GuiUtil.setColumnWidths(itemsTable, widths);

      // moving table headers is not allowed
      itemsTable.getTableHeader().setReorderingAllowed(false);
      JScrollPane tablePane = new JScrollPane(itemsTable);

      add(tablePane, BorderLayout.CENTER); 
      add(getButtonsPanel(), BorderLayout.SOUTH);      
   }
   
   public JPanel getButtonsPanel()
   {
      JPanel buttonsPanel = new JPanel();
      JButton receiptBtn = new JButton("產生收據");
      buttonsPanel.add(receiptBtn);
      JCheckBox sectionB = null;
      if(hasSectionB)
      {
         sectionB = new JCheckBox("包含B區項目");
         sectionB.setSelected(true);
         buttonsPanel.add(sectionB);      
      }
      
      receiptBtn.addActionListener(new GenerateReceiptListener(sectionB));      
      
      return buttonsPanel;
   }   
   
   public Vector<String> getTableRow(InvoiceItem item)
   {
      Vector<String> cols = new Vector<String>();
      ItemInfo info = item.getItemInfo();
      cols.add(info.getItemNo());
      cols.add(info.getName());
      cols.add(Double.toString(item.getPrice()));
      cols.add(Integer.toString(item.getQuantity()));
      cols.add(info.getTypeInfo().toString());                   

      return cols;
   }  
   
   /**
    * This is for generating receipt.
    */
   class GenerateReceiptListener implements ActionListener
   {
      private JCheckBox sectionB;
      
      public GenerateReceiptListener(JCheckBox sectionB)
      {
         this.sectionB = sectionB;
      }
      
      public void actionPerformed(ActionEvent e)
      {
         try
         {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("產生收據");
            chooser.setSelectedFile(new File(invoice.getInvoiceNumber() + ".pdf"));
            if(JFileChooser.APPROVE_OPTION == 
               chooser.showSaveDialog(GuiUtil.getOwnerFrame(getParent())))
            {
               File file = chooser.getSelectedFile();
               ReportsManager reportsMgr = ReportsManager.getInstance();
               boolean includeSectionB = true;
               if(sectionB != null)
                  includeSectionB = sectionB.isSelected();
               
               reportsMgr.generateReceipt(invoice, file, includeSectionB);
            }
         }
         catch(Exception ex)
         {
            logger.error(ex);
         }
      }
   }
}
