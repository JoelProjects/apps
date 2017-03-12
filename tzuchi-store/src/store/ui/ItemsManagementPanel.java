/* $Id: ItemsManagementPanel.java,v 1.12 2010/08/08 05:55:17 joelchou Exp $ */
package store.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import members.datamodel.AccountInfo;
import members.manager.AccountManager;
import members.ui.utils.GuiUtil;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.datamodel.ItemHistory;
import store.datamodel.ItemInfo;
import store.datamodel.ItemTypeInfo;
import store.manager.ContextManager;
import store.manager.ItemsManager;
import store.utils.HibernateUtil;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 2/21/2007
 */
public class ItemsManagementPanel extends JPanel {
    private static final int COL_NUM_ID = 0;
    private static final int COL_NUM_ITEM_NO = 1;
    private static final int COL_NUM_BARCODE = 2;
    private static final int COL_NUM_NAME = 3;
    private static final int COL_NUM_PRICE = 4;
    private static final int COL_NUM_QUANTITY = 5;
    private static final int COL_NUM_TYPE = 6;

    private static Vector<ItemTypeInfo> typeList = null;
    static {
        typeList = (Vector)ContextManager.getTypeList().clone();
        ItemTypeInfo type = new ItemTypeInfo(-1, "");
        typeList.add(0, type);
    }

    private Logger logger = Logger.getLogger(ItemsManagementPanel.class);
    private JTextField searchField;
    private JComboBox typeCombo;
    private JTable itemsTable;
    private AccountInfo accountInfo;
    private AccountManager accountMgr = new AccountManager();

    public ItemsManagementPanel(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        setLayout(new BorderLayout());

        // buttons
        JPanel buttonPanel = new JPanel();
        JButton newButton = new JButton("新增");
        JButton deleteButton = new JButton("刪除");
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // combo boxes
        JPanel comboPanel = new JPanel();

        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("名稱/條碼");
        searchField = new JTextField(15);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JPanel typePanel = new JPanel();
        JLabel typeLabel = new JLabel("類別");
        typeCombo = new JComboBox(typeList);
        typePanel.add(typeLabel);
        typePanel.add(typeCombo);

        JButton refreshButton = new JButton("重新整理");

        comboPanel.add(searchPanel);
        comboPanel.add(typePanel);
        comboPanel.add(refreshButton);

        add(comboPanel, BorderLayout.NORTH);

        // asset table
        Vector<String> headers = new Vector<String>();
        headers.add("#");
        headers.add("編號");
        headers.add("條碼");
        headers.add("名稱");
        headers.add("單價");
        headers.add("數量");
        headers.add("類別");

        Vector<Vector<String>> data = new Vector<Vector<String>>();
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM ItemInfo ORDER BY name");
            Iterator<ItemInfo> it = query.list().iterator();
            while(it.hasNext()) {
                ItemInfo item = (ItemInfo)it.next();
                data.add(getTableRow(item));
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        NotEditableTableModel dataModel = new NotEditableTableModel(data,
            headers);
        itemsTable = new JTable(dataModel);
        itemsTable.setAutoCreateRowSorter(true);
        itemsTable.setPreferredScrollableViewportSize(new Dimension(600, 430));
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths = {10, 30, 60, 390, 40, 20, 40}; // define column width
        GuiUtil.setColumnWidths(itemsTable, widths);

        TableColumnModel columnModel = itemsTable.getColumnModel();
        TableColumn quantityColumn = columnModel.getColumn(COL_NUM_QUANTITY);
        quantityColumn.setCellRenderer(new ItemsTableCellRender());

        // moving table headers is not allowed
        itemsTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tablePane = new JScrollPane(itemsTable);

        add(tablePane, BorderLayout.CENTER);

        // register action listeners      
        refreshButton.addActionListener(new RefreshListener());
        if(accountMgr.isInRole(accountInfo, ContextManager.getAdminRoles())) {
            itemsTable.addMouseListener(new TableListener());
            newButton.addActionListener(new NewListener());
            deleteButton.addActionListener(new DeleteListener());
        }
        else {
            newButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    public static Vector<String> getTableRow(ItemInfo item) {
        Vector<String> cols = new Vector<String>();
        cols.add(Integer.toString(item.getItemId()));
        cols.add(item.getItemNo());
        cols.add(item.getBarcode());
        cols.add(item.getName());
        cols.add(Double.toString(item.getPrice()));
        cols.add(Integer.toString(item.getQuantity()));
        cols.add(item.getTypeInfo().toString());

        return cols;
    }

    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = searchField.getText();
            ItemTypeInfo type = (ItemTypeInfo)typeCombo.getSelectedItem();
            ItemsManager.refreshItemsTable(itemsTable, name, type);
        }
    }

    /**
     * This is a table listener for double clicking on table row.
     */
    class TableListener extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if(event.getClickCount() == 2) // double-click
            {
                int selectedRow = itemsTable.getSelectedRow();
                int selectedCol = itemsTable.getSelectedColumn();
                String itemId = (String)itemsTable.getValueAt(selectedRow,
                    COL_NUM_ID);
                String itemName = (String)itemsTable.getValueAt(selectedRow,
                    COL_NUM_NAME);

                Session session = null;
                ItemInfo item = null;
                switch(selectedCol) {
                    case COL_NUM_BARCODE:
                    case COL_NUM_NAME:
                        EditItemDialog dialog = null;
                        try {
                            session = HibernateUtil.getSession();
                            session.beginTransaction();
                            item = (ItemInfo)session.get(ItemInfo.class,
                                Integer.parseInt(itemId));

                            // bring up the edit dialog
                            dialog = new EditItemDialog(GuiUtil
                                .getOwnerFrame(getParent()), true, itemName);
                            dialog.setItemNo(item.getItemNo());
                            dialog.setName(item.getName());
                            dialog.setBarcode(item.getBarcode());
                            dialog.setPrice(item.getPrice());
                            dialog.setType(item.getTypeInfo());

                            session.getTransaction().commit();

                            dialog.pack();
                            dialog.setVisible(true);
                            dialog
                                .setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                            if(dialog.hasChanged()) {
                                session = HibernateUtil.getSession();
                                session.beginTransaction();

                                item.setItemNo(dialog.getItemNo());
                                item.setName(dialog.getName());
                                item.setBarcode(dialog.getBarcode());
                                item.setPrice(dialog.getPrice());
                                item.setTypeInfo(dialog.getType());

                                // update db
                                session.update(item);
                                session.getTransaction().commit();

                                // update table   
                                itemsTable.setValueAt(item.getItemNo(),
                                    selectedRow, COL_NUM_ITEM_NO);
                                itemsTable.setValueAt(item.getBarcode(),
                                    selectedRow, COL_NUM_BARCODE);
                                itemsTable.setValueAt(item.getName(),
                                    selectedRow, COL_NUM_NAME);
                                itemsTable.setValueAt(item.getPrice(),
                                    selectedRow, COL_NUM_PRICE);
                                itemsTable.setValueAt(item.getTypeInfo(),
                                    selectedRow, COL_NUM_TYPE);
                            }
                        }
                        catch(Exception e) {
                            session.getTransaction().rollback();
                            logger.error(e);
                        }

                        dialog.dispose();
                        break;
                    case COL_NUM_QUANTITY:
                        UpdateItemQuantityDialog updateDialog = null;
                        String quantityStr = (String)itemsTable.getValueAt(
                            selectedRow, COL_NUM_QUANTITY);
                        // bring up the update dialog
                        int quantity = Integer.parseInt(quantityStr);
                        updateDialog = new UpdateItemQuantityDialog(GuiUtil
                            .getOwnerFrame(getParent()), true, itemName,
                            quantity);
                        updateDialog.pack();
                        updateDialog.setVisible(true);
                        updateDialog
                            .setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                        if(updateDialog.hasChanged()) {
                            try {
                                session = HibernateUtil.getSession();
                                session.beginTransaction();
                                // update item quantity
                                int newQuantity = quantity
                                    + updateDialog.getUpdatedNumber();
                                Query query = session
                                    .createQuery("UPDATE ItemInfo SET quantity=:quantity WHERE itemId=:id");
                                query.setParameter("quantity", newQuantity);
                                query.setParameter("id", Integer
                                    .parseInt(itemId));
                                query.executeUpdate();
                                // update item history
                                item = (ItemInfo)session.get(ItemInfo.class,
                                    Integer.parseInt(itemId));
                                ItemHistory history = new ItemHistory();
                                history
                                    .setModifiedBy(accountInfo.getUsername());
                                history.setUpdatedQuantity(updateDialog
                                    .getUpdatedNumber());
                                history.setModifiedDate(new Date());
                                history.setInvoiceNumber(updateDialog
                                    .getInvoiceNumber());
                                history.setInvoiceDate(updateDialog
                                    .getInvoiceDate());
                                history.setItemInfo(item);
                                session.save(history);

                                session.getTransaction().commit();

                                // update item table on UI   
                                itemsTable.setValueAt(String
                                    .valueOf(newQuantity), selectedRow,
                                    COL_NUM_QUANTITY);
                            }
                            catch(Exception e) {
                                session.getTransaction().rollback();
                                logger.error(e);
                            }
                        }

                        updateDialog.dispose();
                        break;
                    default:
                }
            }
        }
    }

    // action listener of New button
    class NewListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // bring the edit dialog
            EditItemDialog dialog = new EditItemDialog(GuiUtil
                .getOwnerFrame(getParent()), true);
            dialog.pack();
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            if(dialog.hasChanged()) {
                ItemInfo item = new ItemInfo();
                item.setItemNo(dialog.getItemNo());
                item.setName(dialog.getName());
                item.setBarcode(dialog.getBarcode());
                item.setPrice(dialog.getPrice());
                item.setTypeInfo(dialog.getType());

                Session session = HibernateUtil.getSession();
                try {
                    // insert into the db
                    session.beginTransaction();
                    session.save(item);
                    session.getTransaction().commit();

                    // insert new row into table model
                    DefaultTableModel tableModel = (DefaultTableModel)itemsTable
                        .getModel();
                    tableModel.insertRow(0, getTableRow(item));
                }
                catch(Exception e) {
                    session.getTransaction().rollback();
                    logger.error(e);
                }
            }
        }
    }

    // action listener of Delete button
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int selectedRow = itemsTable.getSelectedRow();

            if(selectedRow >= 0) // if selected
            {
                // get selected item ID
                String itemId = (String)itemsTable.getValueAt(selectedRow,
                    COL_NUM_ID);
                String itemName = (String)itemsTable.getValueAt(selectedRow,
                    COL_NUM_NAME);

                int option = JOptionPane.showConfirmDialog(
                    ItemsManagementPanel.this, "刪除 \"" + itemName + "\"",
                    "刪除物品", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                if(option == JOptionPane.OK_OPTION) {
                    Session session = HibernateUtil.getSession();
                    try {
                        session.beginTransaction();
                        Query query = session
                            .createQuery("DELETE FROM ItemInfo WHERE itemId=:id");
                        query.setParameter("id", Integer.parseInt(itemId));
                        query.executeUpdate();
                        session.getTransaction().commit();
                        // remove selected row from table model
                        DefaultTableModel tableModel = (DefaultTableModel)itemsTable
                            .getModel();
                        // TableModel does not know how to do sorting, rather RowSorter will
                        // handle it.
                        int sortedRow = itemsTable.convertRowIndexToModel(selectedRow);
                        tableModel.removeRow(sortedRow);
                    }
                    catch(Exception e) {
                        session.getTransaction().rollback();
                        logger.error(e);
                        GuiUtil.messageDialog(ItemsManagementPanel.this, e
                            .toString());
                    }
                }
            }
        }
    }

    class ItemsTableCellRender extends DefaultTableCellRenderer {

        ItemsTableCellRender() {
            //setHorizontalAlignment(SwingConstants.RIGHT);  
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            Component comp = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);

            if(col == COL_NUM_QUANTITY) {
                ((JLabel)comp).setToolTipText("按下滑鼠鍵以更改數量");
            }

            return comp;
        }
    }
}
