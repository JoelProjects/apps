/* $Id: MembersManagementPanel.java,v 1.4 2008/10/19 06:38:12 joelchou Exp $ */
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
import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This is the page for members.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 */
public class MembersManagementPanel extends JPanel {
    private static final int COL_NUM_MEMBER_ID = 0;
    private static final int COL_NUM_NAME = 1;
    private static final int COL_NUM_USERNAME = 2;

    private Logger logger = Logger.getLogger(MembersManagementPanel.class);
    private JTable membersTable;

    public MembersManagementPanel() {
        setLayout(new BorderLayout());

        // buttons
        JPanel buttonPanel = new JPanel();
        JButton newButton = new JButton("New");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // account table
        Vector<String> headers = new Vector<String>();
        headers.add("ID");
        headers.add("Name");
        headers.add("Username");

        Vector<Vector<String>> data = new Vector<Vector<String>>();
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM MemberInfo");
            Iterator it = query.list().iterator();
            while(it.hasNext()) {
                MemberInfo member = (MemberInfo)it.next();
                data.add(getTableRow(member));
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        NotEditableTableModel dataModel = new NotEditableTableModel(data,
            headers);
        membersTable = new JTable(dataModel);
        membersTable
            .setPreferredScrollableViewportSize(new Dimension(600, 430));
        membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths = {20, 50, 50}; // define column width
        GuiUtil.setColumnWidths(membersTable, widths);

        membersTable.setDefaultRenderer(membersTable.getColumnClass(COL_NUM_USERNAME), 
            new MembersTableCellRender()); 

        // moving table headers is not allowed
        membersTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tablePane = new JScrollPane(membersTable);

        add(tablePane, BorderLayout.CENTER);

        // register action listeners
        membersTable.addMouseListener(new TableListener());
        newButton.addActionListener(new NewListener());
        deleteButton.addActionListener(new DeleteListener());
    }

    public void refresh() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM MemberInfo");
            Iterator it = query.list().iterator();
            // clear table           
            DefaultTableModel tableModel = (DefaultTableModel)membersTable.getModel();
            tableModel.setRowCount(0);            
            while(it.hasNext()) {
                MemberInfo account = (MemberInfo)it.next();
                tableModel.addRow(getTableRow(account));
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }         
    }    
    
    private Vector<String> getTableRow(MemberInfo member) {
        Vector<String> cols = new Vector<String>();
        cols.add(String.valueOf(member.getMemberId()));
        cols.add(member.getName());
        AccountInfo accountInfo = member.getAccountInfo();
        if(accountInfo != null)
            cols.add(accountInfo.getUsername());
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
                int selectedRow = membersTable.getSelectedRow();
                int selectedCol = membersTable.getSelectedColumn();
                String memberId = (String)membersTable.getValueAt(selectedRow,
                    COL_NUM_MEMBER_ID);

                EditDialogHelper dialogHelper = new EditDialogHelper(GuiUtil
                    .getOwnerFrame(getParent()));

                switch(selectedCol) {
                    case COL_NUM_USERNAME:
                        String username = (String)membersTable.getValueAt(
                            selectedRow, COL_NUM_USERNAME);
                        if(!Utility.isEmpty(username)) {
                            // existing account
                            dialogHelper.editAccountInfo(username);
                        }
                        else {
                            // create a new account
                            AccountInfo account = dialogHelper.addNewAccountInfo(Long
                                .parseLong(memberId));    
                            membersTable.setValueAt(account.getUsername(),
                                selectedRow, COL_NUM_USERNAME);
                            ContextManager.getAccountsManagementPanel().refresh();
                        }
                        break;
                    default:
                        MemberInfo member = dialogHelper.editMemberInfo(Long
                            .parseLong(memberId));
                        // update table                 
                        membersTable.setValueAt(member.getName(), selectedRow,
                            COL_NUM_NAME);
                }
            }
        }
    }

    // action listener of New button
    class NewListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EditDialogHelper dialogHelper = new EditDialogHelper(GuiUtil
                .getOwnerFrame(getParent()));
            MemberInfo member = dialogHelper.addNewMemberInfo();

            if(member != null) {
                // insert new row into table model
                DefaultTableModel tableModel = (DefaultTableModel)membersTable
                    .getModel();
                tableModel.insertRow(0, getTableRow(member));
            }
        }
    }

    // action listener of Delete button
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int selectedRow = membersTable.getSelectedRow();

            if(selectedRow >= 0) // if selected
            {
                // get selected member ID
                String memberId = (String)membersTable.getValueAt(selectedRow,
                    COL_NUM_MEMBER_ID);
                String name = (String)membersTable.getValueAt(selectedRow, 2);

                int option = JOptionPane.showConfirmDialog(
                    MembersManagementPanel.this, "Delete Member " + name,
                    "Delete Member", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                if(option == JOptionPane.OK_OPTION) {
                    Session session = HibernateUtil.getSession();
                    try {
                        session.beginTransaction();
                        Query query = session
                            .createQuery("DELETE FROM MemberInfo WHERE memberId=:id");
                        query.setParameter("id", Long.parseLong(memberId));
                        query.executeUpdate();
                        session.getTransaction().commit();
                        // remove selected row from table model
                        DefaultTableModel tableModel = (DefaultTableModel)membersTable
                            .getModel();
                        tableModel.removeRow(selectedRow);
                    }
                    catch(Exception e) {
                        session.getTransaction().rollback();
                        logger.error(e);
                        GuiUtil.messageDialog(MembersManagementPanel.this, e
                            .toString());
                    }
                }
            }
        }
    }

    class MembersTableCellRender extends DefaultTableCellRenderer {

        MembersTableCellRender() {
            //setHorizontalAlignment(SwingConstants.RIGHT);  
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            Component comp = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);

            if(col == COL_NUM_USERNAME)
                ((JLabel)comp)
                    .setToolTipText("Click to link this member to an account or edit it");
            else
                ((JLabel)comp).setToolTipText("Click to edit member info");

            return comp;
        }
    }
}
