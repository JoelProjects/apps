/* $Id: InvoiceTableModel.java,v 1.4 2008/10/22 06:13:37 joelchou Exp $ */
package store.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import members.datamodel.AccountInfo;
import members.utils.Utility;
import store.datamodel.Invoice;
import store.manager.ContextManager;

/**
 * A table model for invoice table.
 * 
 * @author Cheng-Hung Chou
 * @since 3/8/2008
 */
public class InvoiceTableModel extends AbstractTableModel {
    private List<Invoice> invoices;
    private List<String> columnNames;
    
    /**
     * Constructs an invoice table model with data and headers.
     * 
     * @param invoices a list of Invoice objects
     * @param columnNames column headers
     */
    public InvoiceTableModel(List<Invoice> invoices,
        List<String> columnNames) {
        this.invoices = invoices;
        this.columnNames = columnNames;
    }

    public int getColumnCount() {
        return 5;
    }

    public int getRowCount() {
        return invoices.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        Invoice invoice = invoices.get(rowIndex);
        if(invoice == null)
            return value;
      
        switch(columnIndex) {
            case (ReportsPanel.COL_NUM_TRANS_NO):
                value = invoice.getInvoiceNumber();
                break;
            case (ReportsPanel.COL_NUM_TRANS_DATE):
                value = Utility.getDateTimeString(invoice.getInvoiceDate());
                break;
            case (ReportsPanel.COL_NUM_CASHIER):
                String username = invoice.getHandledBy();
                AccountInfo accountInfo = ContextManager.getAccountInfo(username);  
                if(accountInfo != null)
                    value = accountInfo.getMemberInfo().getName();
                else
                    value = username;
                break;
            case (ReportsPanel.COL_NUM_PURCHASER):
                value = invoice.getPurchasedBy();
                break;
            case (ReportsPanel.COL_NUM_TOTAL):
                value = Utility.numberFormatter(invoice.getTotal());
                break;
        }

        return value;
    }

    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    public boolean isCellEditable(int row, int col) {
        return false; // not editable
    }

    /**
     * Removes data in the table model
     */
    public void clear() {
        int row = getRowCount();
        if(row == 0)
            return;

        invoices.clear();
        fireTableRowsDeleted(0, row - 1);
    }

    /**
     * Adds a new row to the table model.
     * 
     * @param invoice
     */
    public void addRow(Invoice invoice) {
        int row = getRowCount() + 1;
        invoices.add(invoice);
        fireTableRowsInserted(row, row);
    }

    /**
     * Gets an Invoice.
     * 
     * @param row
     * @return
     */
    public Invoice getRow(int row) {
        return invoices.get(row);
    }
}
