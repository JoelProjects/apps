/* $Id: ReportsPanel.java,v 1.12 2008/10/24 05:55:21 joelchou Exp $ */
package store.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import members.datamodel.AccountInfo;
import members.ui.utils.GuiUtil;
import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.datamodel.Invoice;
import store.manager.InvoiceManager;
import store.utils.HibernateUtil;
import store.utils.Messages;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 5/7/2007
 */
public class ReportsPanel extends JPanel {
    public static final int COL_NUM_TRANS_NO = 0;
    public static final int COL_NUM_TRANS_DATE = 1;
    public static final int COL_NUM_CASHIER = 2;
    public static final int COL_NUM_PURCHASER = 3;
    public static final int COL_NUM_TOTAL = 4;

    private Logger logger = Logger.getLogger(ReportsPanel.class);
    private JTextField startDateField;
    private JTextField endDateField;
    private JButton refreshButton;
    private JTable reportsTable;
    private AccountInfo accountInfo;

    public ReportsPanel(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        
        setLayout(new BorderLayout());

        // combo boxes
        JPanel comboPanel = new JPanel();
        JPanel searchPanel = new JPanel();
        startDateField = new JTextField(8);
        searchPanel.add(new JLabel("日期(起)"));
        startDateField.setToolTipText(Messages.SUPPORTED_DATE_FORMATS);
        searchPanel.add(startDateField);
        endDateField = new JTextField(8);
        endDateField.setToolTipText(Messages.SUPPORTED_DATE_FORMATS);
        searchPanel.add(new JLabel("日期(訖)"));
        searchPanel.add(endDateField);
        refreshButton = new JButton("重新整理");

        comboPanel.add(searchPanel);
        comboPanel.add(refreshButton);

        add(comboPanel, BorderLayout.NORTH);

        // invoice table
        List<String> headers = new ArrayList<String>();
        headers.add("交易號碼");
        headers.add("交易時間");
        headers.add("經收人");
        headers.add("購買者");
        headers.add("總金額");

        InvoiceTableModel dataModel = 
            new InvoiceTableModel(new ArrayList<Invoice>(), headers);

        //NotEditableTableModel dataModel = new NotEditableTableModel(data, headers);
        reportsTable = new JTable(dataModel);
        reportsTable
            .setPreferredScrollableViewportSize(new Dimension(600, 430));
        reportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths = {30, 30, 40, 40, 40}; // define column width
        GuiUtil.setColumnWidths(reportsTable, widths);

        // moving table headers is not allowed
        reportsTable.getTableHeader().setReorderingAllowed(false);
        reportsTable.setToolTipText("按下滑鼠鍵以得到所選擇交易的詳細資料");
        JScrollPane tablePane = new JScrollPane(reportsTable);

        add(tablePane, BorderLayout.CENTER);

        // register action listeners
        reportsTable.addMouseListener(new TableListener());
        refreshButton.addActionListener(new RefreshListener());
    }

    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String startDateStr = startDateField.getText();
            String endDateStr = endDateField.getText();
            // MM/dd/yyyy
            Date startDate = null;
            Date endDate = null;
            if(!Utility.isEmpty(startDateStr)) {
                try {
                    startDate = Utility.dateParser(startDateStr);
                }
                catch(ParseException ex) {
                    GuiUtil.messageDialog(ReportsPanel.this,
                        Messages.SUPPORTED_DATE_FORMATS);
                    return;
                }
            }
            if(!Utility.isEmpty(endDateStr)) {
                try {
                    endDate = Utility.dateParser(endDateStr);
                }
                catch(ParseException ex) {
                    GuiUtil.messageDialog(ReportsPanel.this,
                        Messages.SUPPORTED_DATE_FORMATS);
                    return;
                }
            }
            InvoiceManager
                .refreshReportsTable(reportsTable, accountInfo, startDate, endDate);
        }
    }

    /**
     * This is a table listener for double clicking on table row.
     */
    class TableListener extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if(event.getClickCount() == 2) // double-click
            {
                int selectedRow = reportsTable.getSelectedRow();
                InvoiceTableModel tableModel = (InvoiceTableModel)reportsTable
                    .getModel();
                Invoice invoice = tableModel.getRow(selectedRow);
                Session session = HibernateUtil.getSession();
                try {
                    session.beginTransaction();
                    session.refresh(invoice); // reattach and load invoice items
                    JDialog dialog = new InvoiceItemsDialog(GuiUtil
                        .getOwnerFrame(getParent()), true, invoice);
                    session.getTransaction().commit();
                    dialog.pack();
                    dialog.setVisible(true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                }
                catch(Exception e) {
                    session.getTransaction().rollback();
                    logger.error(e);
                }
            }
        }
    }

    public Date getReportEndDate() {
        Date date = null;
        String dateStr = endDateField.getText();
        if(!Utility.isEmpty(dateStr)) {
            try {
                date = Utility.dateParser(dateStr);
            }
            catch(ParseException ex) {
                logger.error(ex);
            }
        }

        return date;
    }

    public void setReportEndDate(Date date) {
        String dateStr = "";
        if(date != null)
            dateStr = Utility.getDateString(date);
        this.endDateField.setText(dateStr);
    }

    public Date getReportStartDate() {
        Date date = null;
        String dateStr = startDateField.getText();
        if(!Utility.isEmpty(dateStr)) {
            try {
                date = Utility.dateParser(dateStr);
            }
            catch(ParseException ex) {
                logger.error(ex);
            }
        }

        return date;
    }

    public void setReportStartDate(Date date) {
        String dateStr = "";
        if(date != null)
            dateStr = Utility.getDateString(date);
        this.startDateField.setText(dateStr);
    }

    public void refresh() {
        refreshButton.doClick();
    }
}
