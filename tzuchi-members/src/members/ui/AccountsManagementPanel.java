/* $Id: AccountsManagementPanel.java,v 1.4 2008/10/19 06:38:12 joelchou Exp $ */
package members.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import members.datamodel.AccountInfo;
import members.datamodel.MemberInfo;
import members.manager.ContextManager;
import members.ui.utils.EditDialogHelper;
import members.ui.utils.GuiUtil;
import members.utils.HibernateUtil;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This is the page for accounts.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 */
public class AccountsManagementPanel extends JPanel {
    private static final int COL_NUM_USERNAME = 0;
    private static final int COL_NUM_ROLE = 1;
    private static final int COL_NUM_NAME = 2;

    private Logger logger = Logger.getLogger(AccountsManagementPanel.class);
    private JTable accountsTable;

    public AccountsManagementPanel() {
        setLayout(new BorderLayout());

        // buttons
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // account table
        Vector<String> headers = new Vector<String>();
        headers.add("Username");
        headers.add("Role");
        headers.add("Name");

        Vector<Vector<String>> data = new Vector<Vector<String>>();
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM AccountInfo ORDER BY username");
            Iterator it = query.list().iterator();
            while(it.hasNext()) {
                AccountInfo account = (AccountInfo)it.next();
                data.add(getTableRow(account));
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        NotEditableTableModel dataModel = new NotEditableTableModel(data,
            headers);
        accountsTable = new JTable(dataModel);
        accountsTable
            .setPreferredScrollableViewportSize(new Dimension(600, 430));
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths = {20, 50, 50}; // define column width
        GuiUtil.setColumnWidths(accountsTable, widths);
        
        accountsTable.setDefaultRenderer(accountsTable.getColumnClass(COL_NUM_NAME), 
            new AccountsTableCellRender());

        // moving table headers is not allowed
        accountsTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tablePane = new JScrollPane(accountsTable);

        add(tablePane, BorderLayout.CENTER);

        // register action listeners
        accountsTable.addMouseListener(new TableListener());
        //newButton.addActionListener(new NewListener());
        deleteButton.addActionListener(new DeleteListener());
    }

    public void refresh() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM AccountInfo ORDER BY username");
            Iterator it = query.list().iterator();
            // clear table           
            DefaultTableModel tableModel = (DefaultTableModel)accountsTable.getModel();
            tableModel.setRowCount(0);            
            while(it.hasNext()) {
                AccountInfo account = (AccountInfo)it.next();
                tableModel.addRow(getTableRow(account));
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }         
    }
    
    private Vector<String> getTableRow(AccountInfo account) {
        Vector<String> cols = new Vector<String>();
        cols.add(String.valueOf(account.getUsername()));
        cols.add(account.getRoleNamesStr());
        MemberInfo member = account.getMemberInfo();
        if(member != null)
            cols.add(member.getName());
        else
            cols.add("");

        return cols;
    }

    /**
     * This is a table listener for double clicking on table row.
     */
    class TableListener extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if(event.getClickCount() == 2) // double-click
            {
                int selectedRow = accountsTable.getSelectedRow();
                int selectedCol = accountsTable.getSelectedColumn();
                String username = (String)accountsTable.getValueAt(selectedRow,
                    COL_NUM_USERNAME);
                EditDialogHelper dialogHelper = new EditDialogHelper(GuiUtil
                    .getOwnerFrame(getParent()));

                switch(selectedCol) {
                    case COL_NUM_NAME:
                        MemberInfo member = dialogHelper.editMemberInfo(username);
                        // update table                 
                        accountsTable.setValueAt(member.getName(), selectedRow,
                            COL_NUM_NAME);
                        ContextManager.getMembersManagementPanel().refresh();
                        break;
                    default:
                        AccountInfo account = dialogHelper
                            .editAccountInfo(username);
                        accountsTable.setValueAt(account.getRoleNamesStr(),
                            selectedRow, COL_NUM_ROLE);
                }
            }
        }
    }

    // action listener of Delete button
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int selectedRow = accountsTable.getSelectedRow();

            if(selectedRow >= 0) // if selected
            {
                // get selected username
                String username = (String)accountsTable.getValueAt(selectedRow,
                    COL_NUM_USERNAME);

                int option = JOptionPane.showConfirmDialog(
                    AccountsManagementPanel.this, "Delete Account " + username,
                    "Delete Account", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                if(option == JOptionPane.OK_OPTION) {
                    Session session = HibernateUtil.getSession();
                    try {
                        session.beginTransaction();
                        Query query = session
                            .createQuery("DELETE FROM AccountInfo WHERE username=:username");
                        query.setParameter("username", username);
                        query.executeUpdate();
                        session.getTransaction().commit();
                        // remove selected row from table model
                        DefaultTableModel tableModel = (DefaultTableModel)accountsTable
                            .getModel();
                        tableModel.removeRow(selectedRow);
                    }
                    catch(Exception e) {
                        session.getTransaction().rollback();
                        logger.error(e);
                        GuiUtil.messageDialog(AccountsManagementPanel.this, e
                            .toString());
                    }
                }
            }
        }
    }

    class AccountsTableCellRender extends DefaultTableCellRenderer {

        AccountsTableCellRender() {
            //setHorizontalAlignment(SwingConstants.RIGHT);  
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            Component comp = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);

            if(col == COL_NUM_NAME)
                ((JLabel)comp)
                    .setToolTipText("Click to edit member info of this account");
            else
                ((JLabel)comp).setToolTipText("Click to edit this account");

            return comp;
        }
    }
}
